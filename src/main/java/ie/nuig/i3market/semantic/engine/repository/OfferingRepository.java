package ie.nuig.i3market.semantic.engine.repository;

import ie.nuig.i3market.semantic.engine.common.CommonUtil;
import ie.nuig.i3market.semantic.engine.common.RDFBinding;
import ie.nuig.i3market.semantic.engine.common.ServiceUtil;
import ie.nuig.i3market.semantic.engine.common.Vocabulary;
import ie.nuig.i3market.semantic.engine.common.annotations.Category;
import ie.nuig.i3market.semantic.engine.common.annotations.ID;
import ie.nuig.i3market.semantic.engine.common.annotations.Provider;
import ie.nuig.i3market.semantic.engine.common.annotations.Type;
import ie.nuig.i3market.semantic.engine.common.queries.UpdateQueries;
import ie.nuig.i3market.semantic.engine.config.databases.DistributedStorageConfiguration;
import ie.nuig.i3market.semantic.engine.config.databases.VirtuosoConfiguration;
import ie.nuig.i3market.semantic.engine.config.notifications.NotificationsConfiguration;
import ie.nuig.i3market.semantic.engine.domain.DataProvider;
import ie.nuig.i3market.semantic.engine.domain.Dataset;
import ie.nuig.i3market.semantic.engine.domain.PricingModel;
import ie.nuig.i3market.semantic.engine.domain.contracts.ContractParameters;
import ie.nuig.i3market.semantic.engine.domain.entities.lists.CategoriesList;
import ie.nuig.i3market.semantic.engine.domain.entities.lists.OfferingsList;
import ie.nuig.i3market.semantic.engine.domain.entities.lists.ProvidersList;
import ie.nuig.i3market.semantic.engine.domain.optimise.Offering;
import ie.nuig.i3market.semantic.engine.domain.optimise.OfferingContracts;
import ie.nuig.i3market.semantic.engine.domain.optimise.OfferingDataset;
import ie.nuig.i3market.semantic.engine.domain.optimise.OfferingPricings;
import ie.nuig.i3market.semantic.engine.dto.DataOfferingId;
import ie.nuig.i3market.semantic.engine.dto.ExtDataOfferingDto;
import ie.nuig.i3market.semantic.engine.dto.Mapper;
import ie.nuig.i3market.semantic.engine.exceptions.BindingException;
import org.apache.http.protocol.HttpContext;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class OfferingRepository {

    private static final Logger log = LoggerFactory.getLogger(OfferingRepository.class);

    private VirtuosoConfiguration virtuosoConfiguration;

    private OfferingContracts obj;

    private DistributedStorageConfiguration distributedStorageConfiguration;
    private NotificationsConfiguration notificationsConfiguration;

    @Autowired
    private Mapper mapper;

    @Autowired
    private ServiceUtil serviceUtil;


    public OfferingRepository(VirtuosoConfiguration virtuosoConfiguration,
                              DistributedStorageConfiguration distributedStorageConfiguration,
                              NotificationsConfiguration notificationsConfiguration) {
        this.virtuosoConfiguration = virtuosoConfiguration;
        this.distributedStorageConfiguration = distributedStorageConfiguration;
        this.notificationsConfiguration = notificationsConfiguration;
    }

    // Get all category list

    public synchronized List<CategoriesList> getCategoriesList() {
        try {
            List<CategoriesList> categories = distributedStorageConfiguration.listCategories();
            log.debug("categories received from distributed storage are: {}", categories);
            return categories;
        } catch (NullPointerException e) {
            throw new NullPointerException("no category found");
        }
    }


    public List<ExtDataOfferingDto> offeringAggregateByProviderId(String id, Pageable pageable) throws BindingException, ClassNotFoundException, InterruptedException {

        String providerId = Vocabulary.RESOURCE_URI + CommonUtil.getClassName_LowerCase(DataProvider.class) + "/" + id;

        Field offeringFields = CommonUtil.getAnnotatedField(Offering.class.getDeclaredFields(), Provider.class);
        Field contractFields = CommonUtil.getAnnotatedField(OfferingContracts.class.getDeclaredFields(), Provider.class);
        Field datasetFields = CommonUtil.getAnnotatedField(OfferingDataset.class.getDeclaredFields(), Provider.class);
        Field pricingFields = CommonUtil.getAnnotatedField(OfferingPricings.class.getDeclaredFields(), Provider.class);
        return offeringAggregateById(providerId, pageable, offeringFields, contractFields, pricingFields, datasetFields);
    }

    public List<Offering> getOfferingComponentById(String offeringId, Pageable pageable) throws BindingException {
        Field offeringFields = CommonUtil.getAnnotatedField(Offering.class.getDeclaredFields(), ID.class);
        // Get offering component
        return offeringComponentById(offeringId, pageable, offeringFields).getContent();
    }


    public List<ExtDataOfferingDto> offeringAggregateByOfferingId(String id, Pageable pageable) throws BindingException {

        Field offeringFields = CommonUtil.getAnnotatedField(Offering.class.getDeclaredFields(), ID.class);
        Field contractFields = CommonUtil.getAnnotatedField(OfferingContracts.class.getDeclaredFields(), ID.class);
        Field datasetFields = CommonUtil.getAnnotatedField(OfferingDataset.class.getDeclaredFields(), ID.class);
        Field pricingFields = CommonUtil.getAnnotatedField(OfferingPricings.class.getDeclaredFields(), ID.class);
        return offeringAggregateById(id, pageable, offeringFields, contractFields, pricingFields, datasetFields);
    }


    private List<ExtDataOfferingDto> offeringAggregateById(String id, Pageable pageable,
                                                           Field offeringFields,
                                                           Field contractFields,
                                                           Field pricingFields,
                                                           Field datasetFields) throws BindingException {

        log.debug("Executed from offeringAggregateById in OfferingRepository class");

        // Get offering component
        List<Offering> getAllOfferings = offeringComponentById(id, pageable, offeringFields).getContent();

        if (getAllOfferings.size() == 0) {
            return null;
        }

        List<ExtDataOfferingDto> offeringList = new ArrayList<>();

        getAllOfferings.stream().parallel().forEach(offering -> {

            // Get list of contractParameters
            List<ContractParameters> contractList = new ArrayList<>();

            List<OfferingContracts> getAllContractById = null;
            try {
                getAllContractById = offeringContractComponentById(id, pageable, contractFields).getContent();
            } catch (BindingException e) {
                e.printStackTrace();
            }

            if (getAllContractById.size() > 0) {

                getAllContractById.forEach(contract -> {

                    if (offering.getDataOfferingId().equals(contract.getDataOfferingId()) && offering.getProvider().equals(contract.getProvider())) {

                        contract.getContractParameters().forEach(e -> {
                            contractList.add(e);
                        });
                    }
                });
            }

            // Get list of pricing

            List<PricingModel> pricingModelList = new ArrayList<>();

            List<OfferingPricings> getAllPricingByProviderId = null;
            try {
                getAllPricingByProviderId = offeringPricingComponentById(id, pageable, pricingFields).getContent();
            } catch (BindingException e) {
                e.printStackTrace();
            }

            if (getAllPricingByProviderId.size() > 0) {

                getAllPricingByProviderId.forEach(pricings -> {

                    if (offering.getDataOfferingId().toLowerCase().equals(pricings.getDataOfferingId().toLowerCase())

                            && offering.getProvider().toLowerCase().equals(pricings.getProvider().toLowerCase())) {

                        pricings.getHasPricingModel().forEach(e -> {

                            pricingModelList.add(e);

                        });

                    }
                });
            }

            // Get a list of dataset
            List<Dataset> datasetList = new ArrayList<>();

            List<OfferingDataset> getAllDatasetByProviderId = null;
            try {
                getAllDatasetByProviderId = offeringDatasetComponentById(id, pageable, datasetFields).getContent();
            } catch (BindingException e) {
                e.printStackTrace();
            }

            if (getAllDatasetByProviderId.size() > 0) {
                getAllDatasetByProviderId.forEach(datasets -> {

                    if (offering.getDataOfferingId().toLowerCase().equals(datasets.getDataOfferingId().toLowerCase())

                            && offering.getProvider().toLowerCase().equals(datasets.getProvider().toLowerCase())) {

                        datasets.getHasDataset().forEach(e -> {

                            datasetList.add(e);

                        });

                    }

                });
            }

            ExtDataOfferingDto offr = mapper.offeringDtoAggregate(offering, contractList, pricingModelList, datasetList);

            offeringList.add(offr);

        });

        return offeringList;
    }




    public Page<OfferingContracts> getOfferingContractByOfferingId(String offeringId, Pageable pageable) throws BindingException {

        log.debug("Executed from getOfferingContractByOfferingId in OfferingRepository class");

        Field annotatedField = CommonUtil.getAnnotatedField(OfferingContracts.class.getDeclaredFields(), ID.class);

        return offeringContractComponentById(offeringId, pageable, annotatedField);
    }


    //Get a providerList by category
    public List<ProvidersList> getProviderListByCategory(String category) throws BindingException {



        log.debug("Executed from getAllOfferingByOfferingId in OfferingRepository class");

        // Create a new object for general class
        General<Offering> gen = new General<>(virtuosoConfiguration, obj, distributedStorageConfiguration, notificationsConfiguration, mapper, serviceUtil);

       //Create a new object
        CreateNewObjectClazz<Offering> createNewObjectClazz = () -> new Offering();

        Offering offerings = new Offering();


        Field annotatedField = CommonUtil.getAnnotatedField(Offering.class.getDeclaredFields(), Category.class);

        String id = Vocabulary.RESOURCE_URI + category.toLowerCase();

        Set<String> providerSet = gen.listOfOfferingComponentById(id, null, createNewObjectClazz, offerings, annotatedField)
                .stream().filter(e -> e.getDataOfferingId() != null && e.getProvider() != null)
                .map(e -> e.getProvider()).collect(Collectors.toSet());


       List<ProvidersList> list = new ArrayList<>();

       providerSet.stream().forEach( e-> {
           ProvidersList provider = new ProvidersList();
           provider.setProvider(e);
           list.add(provider);
       });

        System.out.println("SizeL " + providerSet.size());

        return list;

    }

    public synchronized Page<Offering> offeringComponentById(String id, Pageable pageable, Field annotatedField) throws BindingException {

        // Create a new object for general class
        General<Offering> gen = new General<>(virtuosoConfiguration, obj, distributedStorageConfiguration, notificationsConfiguration, mapper, serviceUtil);

        //Create a new object

        CreateNewObjectClazz<Offering> createNewObjectClazz = () -> new Offering();

        return gen.offeringComponentById(id, pageable, createNewObjectClazz, new Offering(), annotatedField);

    }


    // Get offering contract component for optimisation
    private synchronized Page<OfferingContracts> offeringContractComponentById(String id, Pageable pageable, Field annotatedField) throws BindingException {

        // Create a new object for general class
        General<OfferingContracts> gen = new General<>(virtuosoConfiguration, obj, distributedStorageConfiguration, notificationsConfiguration, mapper, serviceUtil);

        CreateNewObjectClazz<OfferingContracts> createNewObjectClazz = () -> new OfferingContracts();

        return gen.offeringComponentById(id, pageable, createNewObjectClazz, new OfferingContracts(), annotatedField);
    }


    // Get offering pricing model component for optimisation
    private synchronized Page<OfferingPricings> offeringPricingComponentById(String id, Pageable pageable, Field annotatedField) throws BindingException {

        // Create a new object for general class
        General<OfferingPricings> gen = new General<>(virtuosoConfiguration, obj, distributedStorageConfiguration, notificationsConfiguration, mapper, serviceUtil);

        List<String> list = List.of("Hello");

        //Create a new  object
        CreateNewObjectClazz<OfferingPricings> createNewObjectClazz = () -> new OfferingPricings();

        return gen.offeringComponentById(id, pageable, createNewObjectClazz, new OfferingPricings(), annotatedField);
    }


    // Get offering pricing model component for optimisation
    private synchronized Page<OfferingDataset> offeringDatasetComponentById(String id, Pageable pageable, Field annotatedField) throws BindingException {

        // Create a new object for general class
        General<OfferingDataset> gen = new General<>(virtuosoConfiguration, obj, distributedStorageConfiguration, notificationsConfiguration, mapper, serviceUtil);

        //Create a new  object
        CreateNewObjectClazz<OfferingDataset> createNewObjectClazz = () ->  new OfferingDataset();


        return gen.offeringComponentById(id, pageable, createNewObjectClazz, new OfferingDataset(), annotatedField);
    }



    //*************************************PROVIDER*************************




    // Get offering pricing model component for optimisation
    public synchronized List<DataProvider> getProviderById(String id, Pageable pageable) throws BindingException, ClassNotFoundException {

        String providerId = id;

        Field annotatedField = CommonUtil.getAnnotatedField(DataProvider.class.getDeclaredFields(), Type.class);

        // Create a new object for general class
        General<DataProvider> gen = new General<>(virtuosoConfiguration, obj, distributedStorageConfiguration, notificationsConfiguration, mapper, serviceUtil);

        //Create a new  object
        CreateNewObjectClazz<DataProvider> createNewObjectClazz = () ->  new DataProvider();


        return gen.offeringComponentById(providerId, pageable, createNewObjectClazz, new DataProvider(), annotatedField).getContent();
    }


    /**
     * Filter by category
     *
     * @param
     * @param pageable
     * @return
     * @throws BindingException
     */

    public List<ExtDataOfferingDto> getOfferingAggregateByCategory(String category, Pageable pageable) throws BindingException, InterruptedException {

        String id = Vocabulary.RESOURCE_URI + category.toLowerCase();
        Field offeringFields = CommonUtil.getAnnotatedField(Offering.class.getDeclaredFields(), Category.class);
        Field contractFields = CommonUtil.getAnnotatedField(OfferingContracts.class.getDeclaredFields(), Category.class);
        Field datasetFields = CommonUtil.getAnnotatedField(OfferingDataset.class.getDeclaredFields(), Category.class);
        Field pricingFields = CommonUtil.getAnnotatedField(OfferingPricings.class.getDeclaredFields(), Category.class);

        return offeringAggregateByCategory(id, pageable,
                offeringFields, contractFields, pricingFields, datasetFields);
    }


    //This is to return a list of Data Offering
    private List<ExtDataOfferingDto> offeringAggregateByCategory(String id, Pageable pageable,
                                                                 Field offeringFields,
                                                                 Field contractFields,
                                                                 Field pricingFields,
                                                                 Field datasetFields) throws BindingException {

        log.debug("Executed from offeringAggregateById in OfferingRepository class");

        List<ExtDataOfferingDto> offeringList = new ArrayList<>();

        try {


            List<Offering> getAllOfferings = offeringComponentByCategory(id, pageable, offeringFields).getContent();

            if (getAllOfferings.size() == 0) {
                return null;
            }

            getAllOfferings.forEach(offering -> {

                // Get list of contractParameters
                List<ContractParameters> contractList = new ArrayList<>();

                List<OfferingContracts> getAllContractById = null;
                try {
                    getAllContractById = offeringContractComponentByCategory(id, pageable, contractFields).getContent();
                } catch (BindingException e) {
                    log.error("Error when request offering by category...");
                    e.printStackTrace();
                }

                if (getAllContractById.size() > 0) {

                    getAllContractById.forEach(contract -> {

                        if (offering.getDataOfferingId().equals(contract.getDataOfferingId()) && offering.getProvider().equals(contract.getProvider())) {

                            contract.getContractParameters().forEach(e -> {
                                contractList.add(e);
                            });
                        }
                    });
                }

                // Get list of pricing

                List<PricingModel> pricingModelList = new ArrayList<>();

                List<OfferingPricings> getAllPricingByProviderId = null;
                try {
                    getAllPricingByProviderId = offeringPricingComponentByCategory(id, pageable, pricingFields).getContent();
                } catch (BindingException e) {
                    e.printStackTrace();
                }

                if (getAllPricingByProviderId.size() > 0) {

                    getAllPricingByProviderId.forEach(pricings -> {

                        if (offering.getDataOfferingId().toLowerCase().equals(pricings.getDataOfferingId().toLowerCase())

                                && offering.getProvider().toLowerCase().equals(pricings.getProvider().toLowerCase())) {

                            pricings.getHasPricingModel().forEach(e -> {

                                pricingModelList.add(e);

                            });

                        }
                    });
                }

                // Get a list of dataset
                List<Dataset> datasetList = new ArrayList<>();

                List<OfferingDataset> getAllDatasetByProviderId = null;
                try {
                    getAllDatasetByProviderId = offeringDatasetComponentByCategory(id, pageable, datasetFields).getContent();
                } catch (BindingException e) {
                    e.printStackTrace();
                }

                if (getAllDatasetByProviderId.size() > 0) {
                    getAllDatasetByProviderId.forEach(datasets -> {

                        if (offering.getDataOfferingId().toLowerCase().equals(datasets.getDataOfferingId().toLowerCase())

                                && offering.getProvider().toLowerCase().equals(datasets.getProvider().toLowerCase())) {

                            datasets.getHasDataset().forEach(e -> {

                                datasetList.add(e);

                            });

                        }

                    });
                }

                ExtDataOfferingDto offr = mapper.offeringDtoAggregate(offering, contractList, pricingModelList, datasetList);

                offeringList.add(offr);

            });
//            Thread.sleep(100L);
//            return offeringList;

        } catch (BindingException exp) {
            exp.printStackTrace();
        }

        return offeringList;

    }



    // Get offering component using category. This will get data offering from all nodes in the distributed database
    public synchronized Page<Offering> offeringComponentByCategory(String category, Pageable pageable, Field annotatedField) throws BindingException {

        // Create a new object for general class
        General<Offering> gen = new General<>(virtuosoConfiguration, obj, distributedStorageConfiguration, notificationsConfiguration, mapper, serviceUtil);

        //Create a new object
        CreateNewObjectClazz<Offering> createNewObjectClazz = () -> new Offering();


        return gen.offeringComponentByCategory(category, pageable, createNewObjectClazz, new Offering(), annotatedField);
    }


    // Get contractParameters component using category. This will get data offering from all nodes in the distributed database
    public synchronized Page<OfferingContracts> offeringContractComponentByCategory(String category, Pageable pageable, Field annotatedField) throws BindingException {

        // Create a new object for general class
        General<OfferingContracts> gen = new General<>(virtuosoConfiguration, obj, distributedStorageConfiguration, notificationsConfiguration, mapper, serviceUtil);

        // Create a new object
        CreateNewObjectClazz<OfferingContracts> createNewObjectClazz = () -> new OfferingContracts();

        return gen.offeringComponentByCategory(category, pageable, createNewObjectClazz, new OfferingContracts(), annotatedField);
    }


    // Get pricing model component using category. This will get data offering from all nodes in the distributed database
    public synchronized Page<OfferingPricings> offeringPricingComponentByCategory(String category, Pageable pageable, Field annotatedField) throws BindingException {

        // Create a new object for general class
        General<OfferingPricings> gen = new General<>(virtuosoConfiguration, obj, distributedStorageConfiguration, notificationsConfiguration, mapper, serviceUtil);

      //Create a new object
      CreateNewObjectClazz<OfferingPricings> createNewObjectClazz = () -> new OfferingPricings();

        return gen.offeringComponentByCategory(category, pageable, createNewObjectClazz, new OfferingPricings(), annotatedField);
    }


    // Get pricing model component using category. This will get data offering from all nodes in the distributed database
    public synchronized Page<OfferingDataset> offeringDatasetComponentByCategory(String category, Pageable pageable, Field annotatedField) throws BindingException {

        // Create a new object for general class
        General<OfferingDataset> gen = new General<>(virtuosoConfiguration, obj, distributedStorageConfiguration, notificationsConfiguration, mapper, serviceUtil);

        //Create a new object
        CreateNewObjectClazz<OfferingDataset> createNewObjectClazz = () -> new OfferingDataset();


        return gen.offeringComponentByCategory(category, pageable, createNewObjectClazz, new OfferingDataset(), annotatedField);
    }


//    private GenClazz<OfferingContracts> genClazz = new GenClazz<OfferingContracts>();

    public void deleteOffering(String offeringId) throws ClassNotFoundException, BindingException {

        HttpContext context = virtuosoConfiguration.getVirtuosoContext();

        Set<String> optimisedDeleteQueriesList = new HashSet<>();

        // Get offering infor
        Offering offering = new Offering();

        General<Offering> offeringGenClazz = new General<>();

        Set<String> offeringDeleteQueryList = offeringGenClazz.deleteQueryList(offeringId, virtuosoConfiguration, offering, Offering.class);

//        var optimisedDeleteQueriesList = Flux.fromIterable(offeringDeleteQueryList);
        optimisedDeleteQueriesList.addAll(offeringDeleteQueryList);


        // Get contract parameter infor
        OfferingContracts offeringContract = new OfferingContracts();

        General<OfferingContracts> contractGenClazz = new General<OfferingContracts>();
        Set<String> contractDeleteQueryList = contractGenClazz.deleteQueryList(offeringId, virtuosoConfiguration, offeringContract, OfferingContracts.class);

        optimisedDeleteQueriesList.addAll(contractDeleteQueryList);


        // Get pricing info
        OfferingPricings offeringPricings = new OfferingPricings();

        General<OfferingPricings> pricingGenClazz = new General<OfferingPricings>();

        Set<String> pricingDeleteQueryList = pricingGenClazz.deleteQueryList(offeringId, virtuosoConfiguration, offeringPricings, OfferingPricings.class);

        optimisedDeleteQueriesList.addAll(pricingDeleteQueryList);


        // Get dataset info
        OfferingDataset offeringDataset = new OfferingDataset();

        General<OfferingDataset> datasetGenClazz = new General<OfferingDataset>();

        Set<String> datasetDeleteQueryList = datasetGenClazz.deleteQueryList(offeringId, virtuosoConfiguration, offeringDataset, OfferingDataset.class);

        optimisedDeleteQueriesList.addAll(datasetDeleteQueryList);

        // Delete an offering using the final list
        optimisedDeleteQueriesList.stream().forEach(
                (e -> {
                    UpdateRequest updateRequest = UpdateFactory.create(e);
                    UpdateQueries.exec(updateRequest, virtuosoConfiguration.whenUpdateQuery(), context);
                }));
    }


    // Get offering pricing model component for optimisation
    public synchronized int getTotalOffering(String providerId) throws BindingException, ClassNotFoundException {

        // Create a new object for general class
        General<Offering> gen = new General<>(virtuosoConfiguration, obj, distributedStorageConfiguration, notificationsConfiguration, mapper, serviceUtil);

        // Override the predicate interface
        CreateNewObjectClazz<Offering> createNewObjectClazz = () -> new Offering();

        Offering offerings = new Offering();

//        Field annotatedField = CommonUtil.getAnnotatedField(Offering.class.getDeclaredFields(), Provider.class);

        String id = Vocabulary.RESOURCE_URI + CommonUtil.getClassName_LowerCase(DataProvider.class) + "/" + providerId;

        Field annotatedField = CommonUtil.getAnnotatedField(Offering.class.getDeclaredFields(), Provider.class);

        List<String> list = gen.listOfOfferingComponentById(id, null, createNewObjectClazz, offerings, annotatedField)
                .stream().filter(e -> e.getDataOfferingId() != null)
                .map(e -> e.getDataOfferingId()).collect(Collectors.toList());

        return list.size();
    }

    // Get offering component using category. This will get data offering from all nodes in the distributed database
    public Integer getTotalOfferingByCategoryAndProviderId(String category, String providerId) throws BindingException {

        // Create a new object for general class
        General<Offering> gen = new General<>(virtuosoConfiguration, obj, distributedStorageConfiguration, notificationsConfiguration, mapper, serviceUtil);

       // Create new object
       CreateNewObjectClazz<Offering> createNewObjectClazz = () -> new Offering();

        String id = Vocabulary.RESOURCE_URI + category.toLowerCase();
        Field annotatedField = CommonUtil.getAnnotatedField(Offering.class.getDeclaredFields(), Category.class);

        List<String> list = gen.listOfOfferingComponentByCategory(id, null, createNewObjectClazz, new Offering(), annotatedField)
                .stream().filter(e -> e != null && e.getProvider().toLowerCase().equals(providerId))
                .map(e -> e.getDataOfferingId()).collect(Collectors.toList());

        return list.size();
    }

    // Get offering pricing model component for optimisation
    public List<Offering> getOfferingComponent(String providerId) throws BindingException, ClassNotFoundException {

        // Create a new object for general class
        General<Offering> gen = new General<>(virtuosoConfiguration, obj, distributedStorageConfiguration, notificationsConfiguration, mapper, serviceUtil);

        // Override the predicate interface
        CreateNewObjectClazz<Offering> createNewObjectClazz = () -> new Offering();

        Offering offerings = new Offering();

//        Field annotatedField = CommonUtil.getAnnotatedField(Offering.class.getDeclaredFields(), Provider.class);

        String id = Vocabulary.RESOURCE_URI + CommonUtil.getClassName_LowerCase(DataProvider.class) + "/" + providerId;

        Field annotatedField = CommonUtil.getAnnotatedField(Offering.class.getDeclaredFields(), Provider.class);

        var list = gen.listOfOfferingComponentById(id, null, createNewObjectClazz, offerings, annotatedField);

        return list;
    }


    public Flux<DataOfferingId> saveTemplate(Object type) {

        HttpContext context = virtuosoConfiguration.getVirtuosoContext();
        Model model = ModelFactory.createDefaultModel();
        RDFBinding rdfBinding = new RDFBinding(model);
        String dataOfferingId = "";

        try {

            // notificationsConfiguration.sendDataToNotificationService(type);
            //distributedStorageConfiguration.saveDataToDistributedStorage(type, virtuosoConfiguration);

            rdfBinding.marshal(type);
            StmtIterator stmtIter = model.listStatements();
            for (StmtIterator it = stmtIter; it.hasNext(); ) {
                Statement statement = it.next();

//                System.out.println(statement);

                String insertQuery = UpdateQueries.insertQuery(statement, virtuosoConfiguration.getGraph());

//                System.out.println(insertQuery);
                UpdateRequest updateRequest = UpdateFactory.create(insertQuery);
                UpdateQueries.exec(updateRequest, virtuosoConfiguration.whenUpdateQuery(), context);
            }
            model.close();

            dataOfferingId = rdfBinding.getDataOfferingId();

            System.out.println(dataOfferingId);

        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        var dataOfferingID = new DataOfferingId(dataOfferingId);
        return Flux.just(dataOfferingID);
    }

    //This is to update an offering
    public synchronized void update(Object type) throws BindingException {
        HttpContext context = virtuosoConfiguration.getVirtuosoContext();
        Model model = ModelFactory.createDefaultModel();
        boolean updateFlag = true;
        RDFBinding rdfBinding = new RDFBinding(model, updateFlag);
        try {
            rdfBinding.marshal(type);
            model.write(System.out, "NTriple");
            StmtIterator stmtIter = model.listStatements();
            for (StmtIterator it = stmtIter; it.hasNext(); ) {
                Statement statement = it.next();
                String updateQuery = UpdateQueries.updateQuery(statement, virtuosoConfiguration.getEndpointUrl()
                        + virtuosoConfiguration.getQueryInterface(), virtuosoConfiguration.getGraph());
                UpdateRequest updateRequest = UpdateFactory.create(updateQuery);
                UpdateQueries.exec(updateRequest, virtuosoConfiguration.whenUpdateQuery(), context);
            }

            model.close();

        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | ClassNotFoundException e) {
            throw new BindingException(e.getMessage(), "update registration offering invalid-input");
        }
    }

    //Get offering list
    public synchronized Page<OfferingsList> getOfferingsList(Pageable pageable) {

        Field annotatedField = CommonUtil.getAnnotatedField(OfferingsList.class.getDeclaredFields(), Type.class);

        // Create a new object for general class
        General<OfferingsList> gen = new General<>(virtuosoConfiguration, obj, distributedStorageConfiguration, notificationsConfiguration, mapper, serviceUtil);

       CreateNewObjectClazz<OfferingsList> createNewObjectClazz = () -> new OfferingsList();

        List<OfferingsList> offeringsList = gen.getOfferingsList(pageable, createNewObjectClazz, new OfferingsList(), annotatedField);

        // 3rd parameter should be total count of query result. since list adds the results, therefore, its size can be used instead of total count from query.
        return new PageImpl<>(offeringsList, pageable, offeringsList.size());
    }

    public List<OfferingsList> offeringsLists(Pageable pageable) {
        Field annotatedField = CommonUtil.getAnnotatedField(OfferingsList.class.getDeclaredFields(), Type.class);

        // Create a new object for general class
        General<OfferingsList> gen = new General<>(virtuosoConfiguration, obj, distributedStorageConfiguration, notificationsConfiguration, mapper, serviceUtil);

        CreateNewObjectClazz<OfferingsList> createNewObjectClazz = () -> new OfferingsList();

        List<OfferingsList> offeringsList = gen.getOfferingsList(pageable, createNewObjectClazz, new OfferingsList(), annotatedField);

        return offeringsList;

    }


    //Get a list of provider
    public synchronized Page<ProvidersList> getProvidersList(Pageable pageable) {

        Field annotatedField = CommonUtil.getAnnotatedField(ProvidersList.class.getDeclaredFields(), Type.class);

        // Create a new object for general class
        General<ProvidersList> gen = new General<>(virtuosoConfiguration, obj, distributedStorageConfiguration, notificationsConfiguration, mapper, serviceUtil);

        //Create a new object
        CreateNewObjectClazz<ProvidersList> createNewObjectClazz = () -> new ProvidersList();

        List<ProvidersList> providersList = gen.getOfferingsList(pageable, createNewObjectClazz, new ProvidersList(), annotatedField);

        // 3rd parameter should be total count of query result. since list adds the results, therefore, its size can be used instead of total count from query.
        return new PageImpl<>(providersList, pageable, providersList.size());
    }

    // Get offering contract
    MultipleFunctions<String, Pageable, Field, Offering, List<ContractParameters>> getContracts =

            (id, pageable, fields, offering) -> {

                List<ContractParameters> contractList = new ArrayList<>();

//                getAllOfferings.stream().forEach(offering -> {

                List<OfferingContracts> getAllContractById = null;

                try {
                    getAllContractById = offeringContractComponentById(id, pageable, fields).getContent();
                } catch (BindingException e) {
                    e.printStackTrace();
                }

                if (getAllContractById.size() > 0) {

                    getAllContractById.parallelStream().forEach(contract -> {

                        if (offering.getDataOfferingId().equals(contract.getDataOfferingId())
                                && offering.getProvider().equals(contract.getProvider())) {

                            contract.getContractParameters().forEach(e -> {contractList.add(e);});
                        }
                    });

                }

                return contractList;
            };

    //Get Pricing model
    MultipleFunctions<String, Pageable, Field, Offering,  List<PricingModel>> getPricingModel =

            (id, pageable, fields, offering) -> {

                List<PricingModel> pricingModelList = new ArrayList<>();

                List<OfferingPricings> getAllPricingById = null;

                try {
                    getAllPricingById = offeringPricingComponentById(id, pageable, fields).getContent();
                } catch (BindingException e) {
                    e.printStackTrace();
                }

                if (getAllPricingById.size() > 0) {

                    getAllPricingById.parallelStream().forEach(price -> {

                        if (offering.getDataOfferingId().equals(price.getDataOfferingId())
                                && offering.getProvider().equals(price.getProvider())) {
                            price.getHasPricingModel().stream().forEach(e -> pricingModelList.add(e));
                        }
                    });
                }

                return pricingModelList;
            };


    //Get dataset
    MultipleFunctions<String, Pageable, Field, Offering,  List<Dataset>> getDataset =

            (id, pageable, fields, offering) -> {

                List<Dataset> datasetList = new ArrayList<>();

//                getAllOfferings.parallelStream().forEach(offering -> {

                List<OfferingDataset> getAllDatasetById = null;

                try {
                    getAllDatasetById = offeringDatasetComponentById(id, pageable, fields).getContent();
                } catch (BindingException e) {
                    e.printStackTrace();
                }

                if (getAllDatasetById.size() > 0) {

                    getAllDatasetById.parallelStream().forEach(dataset -> {

                        if (offering.getDataOfferingId().equals(dataset.getDataOfferingId())
                                && offering.getProvider().equals(dataset.getProvider())) {
                            dataset.getHasDataset().stream().forEach(e -> datasetList.add(e));
                        }
                    });
                }

                return datasetList;
            };



    public void deleteDataProviderById(String providerId) throws ClassNotFoundException, BindingException {

        log.info("From delete providerId in repository");

        HttpContext context = virtuosoConfiguration.getVirtuosoContext();

        Set<String> optimisedDeleteQueriesList = new HashSet<>();

        General<DataProvider> providerGenClazz = new General<>();

        Set<String> providerDeleteQueryList = providerGenClazz.deleteProviderQueryList(providerId, virtuosoConfiguration, new DataProvider(), DataProvider.class);

//        var optimisedDeleteQueriesList = Flux.fromIterable(offeringDeleteQueryList);
        optimisedDeleteQueriesList.addAll(providerDeleteQueryList);


        // Delete provider using the final list
        optimisedDeleteQueriesList.stream().forEach(
                (e -> {
                    System.out.println(e);
                    UpdateRequest updateRequest = UpdateFactory.create(e);
                    UpdateQueries.exec(updateRequest, virtuosoConfiguration.whenUpdateQuery(), context);
                }));
    }


}
