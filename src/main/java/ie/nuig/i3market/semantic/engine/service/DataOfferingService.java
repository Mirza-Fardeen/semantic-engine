package ie.nuig.i3market.semantic.engine.service;

import ie.nuig.i3market.semantic.engine.domain.DataOffering;
import ie.nuig.i3market.semantic.engine.domain.DataProvider;
import ie.nuig.i3market.semantic.engine.domain.Organization;
import ie.nuig.i3market.semantic.engine.domain.entities.lists.CategoriesList;
import ie.nuig.i3market.semantic.engine.domain.entities.lists.OfferingsList;
import ie.nuig.i3market.semantic.engine.domain.entities.lists.ProvidersList;
import ie.nuig.i3market.semantic.engine.domain.optimise.OfferingContracts;
import ie.nuig.i3market.semantic.engine.dto.*;
import ie.nuig.i3market.semantic.engine.exceptions.*;
import ie.nuig.i3market.semantic.engine.repository.OfferingRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Service
public class DataOfferingService {

    private final static Logger log = LoggerFactory.getLogger(DataOfferingService.class);

    private final OfferingRepository offeringRepository;

    private final Mapper mapper;

    private final CacheManager cacheManager;

    private SimpleDateFormat simpleformat = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz");


    @Autowired
    public DataOfferingService(OfferingRepository offeringRepository, Mapper mapper, CacheManager cacheManager) {
        this.offeringRepository = offeringRepository;
        this.mapper = mapper;
        this.cacheManager = cacheManager;
    }

    // Get all category list

    public Flux<CategoriesList> getAllCategories() {

        List<CategoriesList> categoriesList = offeringRepository.getCategoriesList();

        FluxGeneric<CategoriesList> offering = new FluxGeneric<>(Schedulers.boundedElastic());

        return offering.fluxList(categoriesList, "No category has been found");
    }

    //Save data provider
    public Mono<Void> saveDataProvider(DataProvider body) throws ClassNotFoundException, BindingException {

        return Mono.fromRunnable(() -> {
            try {
                saveProviderInternal(body);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (BindingException e) {
                e.printStackTrace();
            }
        })
                .onErrorMap(err -> new DuplicateKeyException("The data providerId '" + body.getProviderId() + "' is already existed."))
                .subscribeOn(Schedulers.boundedElastic()).then();

    }

    private void saveProviderInternal(DataProvider body) throws ClassNotFoundException, BindingException {
        var existingProvider = offeringRepository.getProviderById(body.getProviderId(), createPageable(0, 1));

        if(existingProvider.size() != 0) {
            throw new DuplicateKeyException("The data providerId '" + body.getProviderId() + "' is already existed.");
        }

        offeringRepository.saveTemplate(body);
    }

    //This is to save an offering
    public Flux<DataOfferingId> saveDataOffering(DataOfferingDto body) {


        // Need to delete cache
        // Need cache to speed up performance of semantic engine
        cacheManager.getCacheNames().stream()
                .forEach(cache -> cacheManager.getCache(cache).clear());

        DataOffering entity = mapper.dtoToEntity(body);

        // Set version for a new data offering
        var version = 1;
        entity.setVersion(String.valueOf(version)); // Convert into String

        Date today = Calendar.getInstance().getTime();

        long time = today.getTime();

        var date = simpleformat.format(today);

//        entity.setDataOfferingCreated(Long.toString(time));
        entity.setDataOfferingCreated(date);

        return offeringRepository.saveTemplate(entity)
                .onErrorMap(err -> new InvalidInputException(HttpStatus.BAD_REQUEST, "Failed to read HTTP message"))
                .subscribeOn(Schedulers.boundedElastic());
    }


    //This is to update an offering
    public Mono<Void> updateOffering(DataOffering body) throws BindingException {

        List<ExtDataOfferingDto> offerings = offeringRepository.offeringAggregateByOfferingId(body.getDataOfferingId(),
                createPageable(0, 1));

        var offeringId = offerings.get(0).getDataOfferingId();

//        var version = Integer.parseInt(body.getVersion()) + 1;

        if (offerings.size() == 0) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Data offering for id : " + offeringId + " does not exist");
        }

        cacheManager.getCacheNames().stream()
                .forEach(cache -> cacheManager.getCache(cache).clear());

        Date today = Calendar.getInstance().getTime();

        long time = today.getTime();

        var date = simpleformat.format(today);

        body.setLastModified(date);

        var version = Integer.parseInt(body.getVersion());

        if (offerings.get(0).getVersion() > version) {
            throw new InvalidOfferingUpdateException("You have updated with old version");
        } else {
            version = version + 1;
            body.setVersion(String.valueOf(version));
            body.setLastModified(simpleformat.format(new Date()));
            update(body);
        }

        return Mono.empty();
    }


    private void update(Object type) throws BindingException {
        offeringRepository.update(type);
    }

    //This is to get all data provider list
    @SuppressWarnings("RedundantThrows")
    public Flux<ProvidersList> getAllProviders(int page, int size) throws BindingException {

        FluxGeneric<ProvidersList> gen = new FluxGeneric<>(Schedulers.boundedElastic());

        var providersList = gen.getList.apply(offeringRepository.getProvidersList(createPageable(page, size)).getContent());

        return gen.fluxList(providersList, "No provider found.");
    }

    //This is to get all data offering
    @Cacheable(cacheNames = "getAllDataOffering")
    public Flux<OfferingsList> getAllDataOffering(int page, int size) throws BindingException {


        FluxGeneric<OfferingsList> gen = new FluxGeneric<>(Schedulers.boundedElastic());

        var offering = gen.getList.apply(offeringRepository.getOfferingsList(createPageable(page, size)).getContent());

        return gen.fluxList(offering, "No offering found.");
    }


    //Get offering by Provider ID
    @Cacheable(cacheNames = "offeringsByProviderId", sync = true)
    public Flux<ExtDataOfferingDto> offeringAggregateByProviderId(String providerId, int page, int size) throws BindingException, ClassNotFoundException, InterruptedException {


        var offerings = offeringRepository.offeringAggregateByProviderId(providerId,
                createPageable(page, size));


        FluxGeneric<ExtDataOfferingDto> gen = new FluxGeneric<>(Schedulers.boundedElastic());

        return gen.fluxList(offerings, "No offering found for providerId:" + providerId);

    }


    @Cacheable(cacheNames = "offeringById")
    public Flux<ExtDataOfferingDto> offeringAggregateByOfferingId(String id) throws BindingException {

        var offering = offeringRepository.offeringAggregateByOfferingId(id,
                createPageable(0, 1));

        FluxGeneric<ExtDataOfferingDto> gen = new FluxGeneric<>(Schedulers.boundedElastic());

        return gen.fluxList(offering, "No offering found for offering Id:" + id);
    }


    @Cacheable("offeringByCategory")
    public Flux<ExtDataOfferingDto> offeringAggregateByCategory(String category, int page, int size) throws BindingException, InterruptedException {

        var offering = offeringRepository.getOfferingAggregateByCategory(category, createPageable(page, size));

        FluxGeneric<ExtDataOfferingDto> gen = new FluxGeneric<>(Schedulers.boundedElastic());

        return gen.fluxList(offering, "No Offering found for category: " + category);
    }


    //Delete an offering by Id
    public Mono<Void> deleteOffering(String offeringId) throws ClassNotFoundException, BindingException {

        return Mono.fromRunnable(() -> {
                    try {
                        internalDeleteOfferingById(offeringId);
                        cacheManager.getCacheNames().stream()
                                .forEach(cache -> cacheManager.getCache(cache).clear());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (BindingException e) {
                        e.printStackTrace();
                    }
                })
                .onErrorMap(err -> err)
                .subscribeOn(Schedulers.boundedElastic()).then();
    }

    private void internalDeleteOfferingById(String offeringId) throws ClassNotFoundException, BindingException {
        offeringRepository.deleteOffering(offeringId);
    }


    public Mono<Void> deleteDataProviderById(String providerId) throws ClassNotFoundException, BindingException, InterruptedException {


        return Mono.fromRunnable(() -> {
                    try {
                        internalDeleteDataProviderById(providerId.toLowerCase().strip());
//                        log.info("From delete providerId");
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (BindingException | InterruptedException e) {
                        e.printStackTrace();
                    }
                })
                .onErrorMap(err -> err)
                .subscribeOn(Schedulers.boundedElastic()).then();
    }

    private void internalDeleteDataProviderById(String providerId) throws ClassNotFoundException, BindingException, InterruptedException {
        List<ExtDataOfferingDto> offerings = offeringRepository.offeringAggregateByProviderId(providerId,
                createPageable(0, 1));

        if (offerings != null) {
            throw new OfferingConstraintViolationException("Provider with Id: " + providerId + " cannot be deleted as there is an existing offering with this provider");
        }
        cacheManager.getCacheNames().stream()
                .forEach(cache -> cacheManager.getCache(cache).clear());

        offeringRepository.deleteDataProviderById(providerId);
    }


    @Cacheable(cacheNames = "contractParameters")
    public Flux<OfferingContracts> contractParametersAggregateByOfferingId(String offeringId) throws BindingException {

        FluxGeneric<OfferingContracts> gen = new FluxGeneric<>(Schedulers.boundedElastic());

        var offering = gen.getList.apply(offeringRepository.getOfferingContractByOfferingId(offeringId,
                createPageable(0, 1)).getContent());

        return gen.fluxList(offering, "No offering found for Id: " + offeringId);

    }

    // Get offeringComponent by category
    @Cacheable(cacheNames = "providerByCategory")
    public Flux<ProvidersList> getProviderListComponentByCategory(String category, int page, int size) throws BindingException {
        String finalCategory = category.toLowerCase();
        FluxGeneric<ProvidersList> gen = new FluxGeneric<>(Schedulers.boundedElastic());

//        var offering = offeringRepository.getProviderListByCategory(finalCategory);

        var offering = gen.getList.apply(offeringRepository.getProviderListByCategory(finalCategory));
        return gen.fluxList(offering, "No provider found for category: " + category);
    }

    public Mono<Integer> getTotalOffering(String providerId) throws BindingException, ClassNotFoundException {

        int i = offeringRepository.getTotalOffering(providerId);
        return Mono.just(i)
                .map(integer -> integer)
                .log(log.getName(), Level.FINE)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Schema(example = "All")
    public Mono<Integer> getTotalOfferingByCategory(String category, String providerId) throws BindingException {
        int i = offeringRepository.getTotalOfferingByCategoryAndProviderId(category, providerId);
        return Mono.just(i)
                .map(integer -> integer)
                .log(log.getName(), Level.FINE)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Flux<DataProvider> getDataProviderById(String providerId) throws BindingException, ClassNotFoundException {


        var getProviderById = getProviderByIdInternal(providerId);

        return Mono.fromCallable(() -> getProviderById)
                .switchIfEmpty(Mono.error(new NotFoundException(HttpStatus.NOT_FOUND, "No provider for providerId: " + providerId)))
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic());
    }


    private List<DataProvider> getProviderByIdInternal(String providerId) throws BindingException, ClassNotFoundException {

        var dataProvider = offeringRepository.getProviderById(providerId, createPageable(0, 1))  ;

        if(dataProvider.size() != 0) {
            System.out.println(dataProvider.size());
            return dataProvider;
        }
        return null;
    }

    //This is to create a customised Pageable
    private Pageable createPageable(int page, int size) {
        return new Pageable() {
            @Override
            public int getPageNumber() {
                return page;
            }

            @Override
            public int getPageSize() {
                return size;
            }

            @Override
            public long getOffset() {
                return 0;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public Pageable next() {
                return null;
            }

            @Override
            public Pageable previousOrFirst() {
                return null;
            }

            @Override
            public Pageable first() {
                return null;
            }

            @Override
            public Pageable withPage(int i) {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }
        };
    }

    private List<Organization> organisation() {
        Organization organization = new Organization();
        organization.setOrganizationId("NUIG");
        organization.setName("O_NUIG");
        organization.setDescription("Education");
        organization.setAddress("Galway");
        organization.setContactPoint("info@sample.com");
        return List.of(organization);
    }


    @Cacheable(cacheNames = "offeringID")
    public Mono<Offerings> getOfferingByProviderIdAndCategorySorted(String inputProviderId, String inputCategory,
                                                                    long page, long size, String sortBy, String orderBy) {

        log.debug("Executed from getOfferingByProviderIdAndCategorySorted");

        String providerId = inputProviderId.strip();

        String category = inputCategory.strip();

        if(providerId.isEmpty() || providerId.isBlank()|| providerId.length() == 0) {

            throw new InvalidInputException(HttpStatus.BAD_REQUEST, "You have inputted an invalid providerId");

        } else if (category.isBlank() || category.isEmpty() || category.length() == 0) {

            throw new InvalidInputException(HttpStatus.BAD_REQUEST, "You have inputted an invalid category");

        }

        //Get a list of offeringId
        var offeringIdList = offeringRepository.offeringsLists(null);

        Offerings offering = new Offerings();

        List<ExtDataOfferingDto> sortedOfferingDTOList = new ArrayList<>();

        List<OfferingsList> finalOfferingList = new ArrayList<>();

        //Filter offeringId list by providerId
        if (providerId.equalsIgnoreCase("All")) {

            finalOfferingList.addAll(offeringIdList);

        } else {

            finalOfferingList.addAll(getSortedOfferingByProviderId(offeringIdList, providerId));

        }

        finalOfferingList.parallelStream().forEach(e -> {
            try {
                ExtDataOfferingDto obj = offeringRepository.offeringAggregateByOfferingId(e.getOffering(), createPageable(0, 1)).get(0);
                sortedOfferingDTOList.add(obj);
            } catch (BindingException ex) {
                ex.printStackTrace();
            }
        });


        Comparator<ExtDataOfferingDto> comparedByCreatedTime = Comparator.comparing(e -> e.getDataOfferingCreated());
        Comparator<ExtDataOfferingDto> comparedByOfferingTitle = Comparator.comparing(e -> e.getDataOfferingTitle().toLowerCase());


        var list = sortedOfferingDTOList.parallelStream().filter(e -> {
            if (!category.equalsIgnoreCase("All")) {
                return e.getCategory().equalsIgnoreCase(category);
            }
            return true;
        }).collect(Collectors.toList());

        if (orderBy.equalsIgnoreCase("title")) {
            if(sortBy.equalsIgnoreCase("desc")) {
                list.sort(comparedByOfferingTitle.reversed());
            } else {
                list.sort(comparedByOfferingTitle);
            }

        }

        if (orderBy.equalsIgnoreCase("time")) {
            System.out.println(orderBy);
            if(sortBy.equalsIgnoreCase("asc")) {
                System.out.println(sortBy);
                list.sort(comparedByCreatedTime);
            } else {
                list.sort(comparedByCreatedTime.reversed());
            }

        }

        offering.setResult(list.stream().skip(size*page).limit(size).collect(Collectors.toList()));

        offering.setTotalOffering(list.size());

//        offering.getResult().stream().forEach(e -> System.out.println(e.getDataOfferingCreated()));

        return Mono.fromCallable(() -> offering)
                .subscribeOn(Schedulers.boundedElastic());
    }



    private List<OfferingsList> getSortedOfferingByProviderId(List<OfferingsList> list, String providerId) {
        return list.parallelStream().filter(e -> getProviderId(e.getOffering()).equalsIgnoreCase(providerId))
                .collect(Collectors.toList());
    }


    private long getOfferingNum(String offeringId)  {
        var ids = offeringId.split("_dataoffering", 0);

        return Long.valueOf(ids[1]);
    }

    private String getProviderId(String offeringId) {
        var ids = offeringId.split("_dataoffering", 0);

        return ids[0];
    }

}
