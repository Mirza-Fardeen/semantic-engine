package ie.nuig.i3market.semantic.engine;

import ie.nuig.i3market.semantic.engine.controller.RegistrationOfferingController;
import ie.nuig.i3market.semantic.engine.domain.*;
import ie.nuig.i3market.semantic.engine.domain.contracts.ContractParameters;
import ie.nuig.i3market.semantic.engine.domain.contracts.IntendedUse;
import ie.nuig.i3market.semantic.engine.domain.contracts.LicenseGrant;
import ie.nuig.i3market.semantic.engine.domain.dataset.DatasetInformation;
import ie.nuig.i3market.semantic.engine.domain.pricing.*;
import ie.nuig.i3market.semantic.engine.dto.DataOfferingDto;
import ie.nuig.i3market.semantic.engine.dto.ExtDataOfferingDto;
import ie.nuig.i3market.semantic.engine.exceptions.BindingException;
import ie.nuig.i3market.semantic.engine.exceptions.NotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SemanticEngineIntegrationTests {

    @Autowired
    private RegistrationOfferingController controller;

    @BeforeEach
    public void setup() throws ClassNotFoundException, BindingException, InterruptedException {
        String offeringId = "uiot_dataoffering1";
        StepVerifier.create(controller.deleteOffering(offeringId)).verifyComplete();


        String offeringId2 = "uiot_dataoffering2";
        StepVerifier.create(controller.deleteOffering(offeringId2)).verifyComplete();

        StepVerifier.create(controller.deleteDataProvider("uiot")).verifyComplete();
    }

    @AfterEach
    public void cleanUp() throws ClassNotFoundException, BindingException, InterruptedException {
        String offeringId = "uiot_dataoffering1";
        StepVerifier.create(controller.deleteOffering(offeringId)).verifyComplete();


        String offeringId2 = "uiot_dataoffering2";
        StepVerifier.create(controller.deleteOffering(offeringId2)).verifyComplete();

        StepVerifier.create(controller.deleteDataProvider("uiot")).verifyComplete();
    }

    @Test()
    @Order(1)
    public void registerNewProviderTest() throws ClassNotFoundException, BindingException, InterruptedException {
        var registerProvider = controller.saveDataProvider(dataProvider());
        StepVerifier.create(registerProvider)
                .expectComplete().verify();
    }

    @Test
    @Order(2)
    public void shouldReturnErrorWhenThereIsDuplicatedProviderId() throws ClassNotFoundException, BindingException {
        var registerProvider = controller.saveDataProvider(dataProvider());
        StepVerifier.create(registerProvider)
                .expectComplete().verify();

        var duplicatedProvider = controller.saveDataProvider(dataProvider());
        StepVerifier.create(duplicatedProvider)
                .expectError(DuplicateKeyException.class)
                .verify();
    }

    @Test
    @Order(3)
    public void shouldReturnNotFoundErrorIfNoProviderFound() throws BindingException {

        var noProviderListFound = controller.getProviderByCategory("Test", 0, 1).log();

        StepVerifier.create(noProviderListFound)
                .expectError(NotFoundException.class).verify();
    }

    @Test
    @Order(4)
    public void shouldReturnNotFoundErrorFindProviderByIdTest() throws BindingException, ClassNotFoundException {

        var registerProvider = controller.saveDataProvider(dataProvider());
        StepVerifier.create(registerProvider)
                .expectComplete().verify();

        var getProviderById = controller.getDataProviderById("uiot");

        StepVerifier.create(getProviderById)
                .expectNextMatches(provider -> provider.getProviderId().equalsIgnoreCase("uiot"))
                .verifyComplete();
    }


    @Test
    @Order(5)
    public void shouldReturnNotFoundErrorWhenNoDataProvider() throws BindingException, ClassNotFoundException {

        var getProviderById = controller.getDataProviderById("notAProvider");

        StepVerifier.create(getProviderById)
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    @Order(6)
    @DisplayName("Delete Provider with no Offering")
    public void deleteProviderWithoutAnExistingOfferingTest() throws ClassNotFoundException, BindingException, InterruptedException {

        var registerProvider = controller.saveDataProvider(dataProvider());

        StepVerifier.create(registerProvider)
                .expectComplete().verify();

        String providerId = dataProvider().getProviderId();
        var deleteProvider = controller.deleteDataProvider(providerId);

        StepVerifier.create(deleteProvider)
                .expectComplete().verify();

        String offeringId = "uiot_dataoffering1";
        StepVerifier.create(controller.deleteOffering(offeringId)).verifyComplete();
        StepVerifier.create(controller.deleteDataProvider("uiot"));
    }



    @Test
    @Order(7)
    @DisplayName("Failed to delete a provider with an existing offering")
    public void failedToDeleteProviderIfThereIsStillAssociatedExistingOffering() throws ClassNotFoundException,
            InterruptedException, BindingException{

        var registerProvider = controller.saveDataProvider(dataProvider());

        StepVerifier.create(registerProvider)
                .expectComplete().verify();

        var offering = controller.saveDataOffering(generateOffering()).log();

        String offeringId = "uiot_dataoffering1";

        StepVerifier.create(offering)
                .expectNextMatches(dataOfferingID -> dataOfferingID.getDataOfferingId().equalsIgnoreCase(offeringId))
                .verifyComplete();


        String providerId = dataProvider().getProviderId();
        var deleteProvider = controller.deleteDataProvider(providerId);

        StepVerifier.create(deleteProvider)
                        .expectError().verify();

        StepVerifier.create(controller.deleteOffering(offeringId)).verifyComplete();
        StepVerifier.create(controller.deleteDataProvider("uiot"));

    }

    @Test
    @Order(8)
    public void registerNewOfferingTest() {
        String offeringId = "uiot_dataoffering1";
        var offering = controller.saveDataOffering(generateOffering()).log();
        StepVerifier.create(offering)
                .expectNextMatches(dataOfferingID ->  dataOfferingID.getDataOfferingId().equalsIgnoreCase(offeringId))
                .verifyComplete();

    }

    @Test
    @Order(9)
    public void updatingAnExistingOffering() throws BindingException, ClassNotFoundException, InterruptedException {

        String offeringId = "uiot_dataoffering1";
        StepVerifier.create(controller.deleteOffering(offeringId)).verifyComplete();
        StepVerifier.create(controller.deleteDataProvider("uiot"));

        var offering = controller.saveDataOffering(generateOffering()).log();

        StepVerifier.create(offering)
                .expectNextMatches(dataOfferingID -> dataOfferingID.getDataOfferingId().equalsIgnoreCase(offeringId))
                .verifyComplete();

        var extDataOfferingDTO = controller.offeringAggregateByOfferingId(offeringId);

        var dto = extDataOfferingDTO
                .toStream().collect(Collectors.toList()).get(0);

        System.out.println(dto.getDataOfferingId());

        StepVerifier.create(extDataOfferingDTO)
                .expectNextMatches(extDataOfferingDTO1 -> extDataOfferingDTO1.getVersion() == 1).verifyComplete();


        var updatingOffering = extDataOfferingDTO.toStream().collect(Collectors.toList()).get(0);
        updatingOffering.setDataOfferingTitle("Updated title");

        var updatedOffering = extDtoToEntity(updatingOffering);

        var update = controller.updateOffering(updatedOffering);
        
        StepVerifier.create(update).expectComplete().verify();


        var getUpdatedOffering = controller.offeringAggregateByOfferingId(offeringId);

        var updatedDto = extDataOfferingDTO
                .toStream().collect(Collectors.toList()).get(0);

        System.out.println(dto.getDataOfferingId());

        StepVerifier.create(getUpdatedOffering)
                .expectNextMatches(extDataOfferingDTO1 -> extDataOfferingDTO1.getVersion() == 2
                        && extDataOfferingDTO1.getDataOfferingTitle().equalsIgnoreCase("Updated title")).verifyComplete();

    }

    @Test
    @Order(10)
    public void shouldReturnErrorIfNoOfferingFound() throws BindingException {
        var notFoundOffering = controller.offeringAggregateByOfferingId("notFoundId");

        StepVerifier.create(notFoundOffering).expectError().verify();
    }


    @Test
    @Order(11)
    public void getProviderListByCategoryTest() throws BindingException {
        String category = "EnginEEring";

        String offeringId = "uiot_dataoffering1";

        var offering = controller.saveDataOffering(generateOffering()).log();

        StepVerifier.create(offering)
                .expectNextMatches(dataOfferingID -> dataOfferingID.getDataOfferingId().equalsIgnoreCase(offeringId))
                .verifyComplete();

        var providerId = controller.getProviderByCategory(category, 0, 1).log();

        StepVerifier.create(providerId)
                .expectNextMatches(id -> id.getProvider().equalsIgnoreCase("uiot"))
                .verifyComplete();
    }

    @Test
    @Order(12)
    public void getOfferingByCategoryAndShouldHaveNoErrorTest() throws InterruptedException, BindingException {

        var offering = controller.saveDataOffering(generateOffering()).log();
        String offeringId = "uiot_dataoffering1";
        StepVerifier.create(offering)
                .expectNextMatches(dataOfferingID -> dataOfferingID.getDataOfferingId().equalsIgnoreCase(offeringId))
                .verifyComplete();

        String category = "engineering";
        var getOfferingByCategory = controller.offeringAggregateByCategory(category,0, 1);

        StepVerifier.create(getOfferingByCategory)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @Order(13)
    public void getOfferingByCategoryAndShouldReturnNotFoundOfferingTest() throws InterruptedException, BindingException {
        String category = "notExistingCategory";
        var getOfferingByNotExistingCategory = controller.offeringAggregateByCategory(category, 0, 1).log();
        StepVerifier.create(getOfferingByNotExistingCategory).expectError().verify();
    }


    @Test
    @Order(14)
    @DisplayName("Find All Offerings by providerId")
    public void findAllOfferingByProviderIdTest() throws ClassNotFoundException, InterruptedException, BindingException {

        var offering = controller.saveDataOffering(generateOffering()).log();

        String offeringId = "uiot_dataoffering1";

        StepVerifier.create(offering)
                .expectNextMatches(dataOfferingID -> dataOfferingID.getDataOfferingId().equalsIgnoreCase(offeringId))
                .verifyComplete();

        var findOfferingByProviderId = controller.offeringAggregateByProviderId("uiot", 0, 1);

        StepVerifier.create(findOfferingByProviderId)
                .expectNextMatches(savedOffering -> savedOffering.getVersion() == 1)
                .verifyComplete();
    }

    @Test
    @Order(15)
    @DisplayName("Find All Offerings by providerId")
    public void shouldReturnErrorIfNoOfferingFoundForAProviderId() throws ClassNotFoundException, InterruptedException, BindingException {

        var findOfferingByProviderId = controller.offeringAggregateByProviderId("uiot1", 0, 1);

        StepVerifier.create(findOfferingByProviderId).expectError().verify();
    }

    @Test
    @DisplayName("Get offering by OfferingId")
    @Order(16)
    public void getAnOfferingByOfferingIdTest() throws BindingException {

        String offeringId = "uiot_dataoffering1";

        var offering = controller.saveDataOffering(generateOffering()).log();

        StepVerifier.create(offering)
                .expectNextMatches(dataOfferingID -> dataOfferingID.getDataOfferingId().equalsIgnoreCase(offeringId))
                .verifyComplete();

        var getOfferingById = controller.offeringAggregateByOfferingId(offeringId).log();

        StepVerifier.create(getOfferingById)
                .expectNextMatches(getOffering -> getOffering.getDataOfferingId().equalsIgnoreCase(offeringId))
                .verifyComplete();
    }

    @Test
    @Order(17)
    public void returnErrorWhenGettingOfferingWhichDoesNotExist() throws BindingException {
        String offeringId = "uiot_dataoffering1";
        var getOfferingById = controller.offeringAggregateByOfferingId(offeringId).log();

        StepVerifier.create(getOfferingById)
                .expectError().verify();
    }




    @Test
    @Order(18)
    @DisplayName("Has contract parameter test")
    public void getContractParameterByOfferingIdTest() throws ClassNotFoundException, BindingException {

        String offeringId = "uiot_dataoffering1";

        var newContractParameter = controller.saveDataOffering(generateOffering())
                .thenMany(controller.contractParametersAggregateByOfferingId(offeringId))
                .log();

        StepVerifier.create(newContractParameter)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @Order(19)
    @DisplayName("No contract parameter test")
    public void shouldReturnNotFoundErrorWhenThereIsNoContractParameter() throws ClassNotFoundException, BindingException {
        String offeringId = "uiot_dataoffering1";
        var getContractParamter = controller.contractParametersAggregateByOfferingId(offeringId).log();
        StepVerifier.create(getContractParamter)
                .expectError(NotFoundException.class).verify();
    }


    @Test
    @Order(20)
    @DisplayName("Get data offering list")
    public void getDataOfferingListTest() throws BindingException, ClassNotFoundException {

        String offeringId = "uiot_dataoffering1";

        var getOfferingList = controller.saveDataOffering(generateOffering())
                .thenMany(controller.getAllDataOffering(0, 5)).log();

       StepVerifier.create(getOfferingList)
               .expectNextMatches(e -> e.getOffering().equalsIgnoreCase(offeringId))
               .verifyComplete();
    }

    @Test
    @Order(21)
    @DisplayName("No Offering list found")
    public void shouldReturnNotFoundErrorWhenNoOfferingListFound() throws BindingException {
        var getOfferingList = controller.getAllDataOffering(0, 5).log();
        StepVerifier.create(getOfferingList)
                .expectError(NotFoundException.class)
                .verify();
    }



    @Test
    @Order(22)
    @DisplayName("Get Provider List")
    public void getProviderListTest() throws BindingException, ClassNotFoundException {

        var provider = controller.saveDataProvider(dataProvider()).log();

        StepVerifier.create(provider).expectComplete().verify();

        var getProviderList = controller.getAllProviders(0, 5).log();

        StepVerifier.create(getProviderList)
                .expectNextMatches(e -> e.getProvider().equalsIgnoreCase(dataProvider().getProviderId()))
                .verifyComplete();
    }

    @Test
    @Order(23)
    @DisplayName("Get Provider List with Not Found Error")
    public void shouldReturnNotFoundErrorWhenNoProvider() throws BindingException {
        var getProviderList = controller.getAllProviders(0, 5).log();
        StepVerifier.create(getProviderList)
                .expectError(NotFoundException.class).verify();
    }
    

    @Test
    @Order(28)
    public void getSortedOfferingListForADataProviderIdTest() throws ClassNotFoundException, BindingException, InterruptedException {

        String offeringId = "uiot_dataoffering1";

        var  saveOffering = controller.saveDataOffering(generateOffering()).log();

        StepVerifier.create(saveOffering)
                .expectNextMatches(e -> e.getDataOfferingId().equalsIgnoreCase(offeringId)).verifyComplete();

        var offeringList = controller.getOfferingByProviderIdAndCategorySorted("uiot", "All", 0, 5, "title", "desc");

        StepVerifier.create(offeringList)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @Order(29)
    public void getSortedOfferingListForAllProviderIdTest() throws ClassNotFoundException, BindingException, InterruptedException {

        String offeringId1 = "uiot_dataoffering1";
        String offeringId2 = "uiot_dataoffering2";

        var  saveOffering = controller.saveDataOffering(generateOffering())
                        .thenMany(controller.saveDataOffering(generateOffering2()))
                                .thenMany(controller.offeringAggregateByOfferingId(offeringId1))

                                        .log();

        StepVerifier.create(saveOffering)
                .expectNextMatches(e -> e.getDataOfferingId().equalsIgnoreCase(offeringId1))
                .verifyComplete();

        var offeringList = controller.getOfferingByProviderIdAndCategorySorted("All", "All", 0, 5, "title", "desc");

        StepVerifier.create(offeringList)
                .expectNextCount(2)
                .verifyComplete();

    }


    private DataOfferingDto generateOffering() {
        DataOfferingDto dto = new DataOfferingDto();

        dto.setDataOfferingTitle("Wind Turbines");
        dto.setDataOfferingDescription("Wind Turbine Dataset");
        dto.setStatus("activated");
        dto.setCategory("Engineering");
        dto.setProvider("uioT");
        dto.setMarketId("UIOT-M");
        dto.setOwner("chi");
        dto.setDataOfferingExpirationTime("1 Month");
        dto.setContractParameters(contractParameters());
        dto.setHasPricingModel(pricingModels());
        dto.setHasDataset(datasets());

        return dto;

    }

    private DataOfferingDto generateOffering2() {
        DataOfferingDto dto = new DataOfferingDto();

        dto.setDataOfferingTitle("Wind Turbines");
        dto.setDataOfferingDescription("Wind Turbine Dataset");
        dto.setStatus("activated");
        dto.setCategory("wellbeing");
        dto.setProvider("uioT");
        dto.setMarketId("UIOT-M");
        dto.setOwner("chi");
        dto.setDataOfferingExpirationTime("1 Month");
        dto.setContractParameters(contractParameters());
        dto.setHasPricingModel(pricingModels());
        dto.setHasDataset(datasets());

        return dto;

    }

    private List<ContractParameters> contractParameters() {

        ContractParameters contractParameters = new ContractParameters();
        contractParameters.setInterestOfProvider("Test");
        contractParameters.setInterestDescription("Contract desc");
        contractParameters.setHasGoverningJurisdiction("Test Governing");
        contractParameters.setPurpose("Wind turbine data");
        contractParameters.setPurposeDescription("Test purpose des");
        contractParameters.setHasIntendedUse(intendedUses());
        contractParameters.setHasLicenseGrant(licenseGrants());

        return List.of(contractParameters);
    }

    private List<IntendedUse> intendedUses() {

        IntendedUse intendedUse = new IntendedUse();

        intendedUse.setProcessData("Test");
        intendedUse.setShareDataWithThirdParty("true");
        intendedUse.setEditData("false");

        return List.of(intendedUse);
    }

    private List<LicenseGrant> licenseGrants() {
        LicenseGrant licenseGrant = new LicenseGrant();

        licenseGrant.setCopyData("true");
        licenseGrant.setTransferable("false");
        licenseGrant.setExclusiveness("false");
        licenseGrant.setRevocable("false");

        return List.of(licenseGrant);
    }

    private List<PricingModel> pricingModels() {
        PricingModel pricingModel = new PricingModel();

        pricingModel.setPricingModelName("Pricide test");
        pricingModel.setBasicPrice("1000");
        pricingModel.setCurrency("EUR");
        pricingModel.setHasPaymentOnSubscription(paymentOnSubscriptions());
        pricingModel.setHasPaymentOnApi(paymentOnApi());
        pricingModel.setHasPaymentOnUnit(paymentOnUnits());
        pricingModel.setHasPaymentOnSize(paymentOnSizes());
        pricingModel.setHasFreePrice(paymentOnFreePrices());

        return List.of(pricingModel);
    }

    private List<PaymentOnSubscription> paymentOnSubscriptions() {
        PaymentOnSubscription paymentOnSubscription = new PaymentOnSubscription();

        paymentOnSubscription.setPaymentOnSubscriptionName("Test");
        paymentOnSubscription.setPaymentType("Transfer");
        paymentOnSubscription.setTimeDuration("1 day");
        paymentOnSubscription.setDescription("Payment");
        paymentOnSubscription.setRepeat("true");
        paymentOnSubscription.setHasSubscriptionPrice("true");


        return List.of(paymentOnSubscription);
    }

    private List<PaymentOnApi> paymentOnApi() {
        PaymentOnApi paymentOnAPI = new PaymentOnApi();

        paymentOnAPI.setPaymentOnApiName("API payment test");
        paymentOnAPI.setDescription("Api desc payment");
        paymentOnAPI.setNumberOfObject("10");
        paymentOnAPI.setHasApiPrice("true");

        return List.of(paymentOnAPI);
    }

    private List<PaymentOnUnit> paymentOnUnits() {
        PaymentOnUnit paymentOnUnit = new PaymentOnUnit();

        paymentOnUnit.setPaymentOnUnitName("Test");
        paymentOnUnit.setDescription("test");
        paymentOnUnit.setDataUnit("Test unit");
        paymentOnUnit.setHasUnitPrice("true");

        return List.of(paymentOnUnit);

    }

    private List<PaymentOnSize> paymentOnSizes() {
        PaymentOnSize paymentOnSize = new PaymentOnSize();
        paymentOnSize.setPaymentOnSizeName("Payment on size test");
        paymentOnSize.setDescription("Test");
        paymentOnSize.setDataSize("100Mb");
        paymentOnSize.setHasSizePrice("true");

        return List.of(paymentOnSize);
    }

    private List<PaymentOnFreePrice> paymentOnFreePrices() {
        PaymentOnFreePrice paymentOnFreePrice = new PaymentOnFreePrice();

        paymentOnFreePrice.setHasPriceFree("true");

        return List.of(paymentOnFreePrice);
    }

    private List<Dataset> datasets() {
        Dataset dataset = new Dataset();
        dataset.setTitle("Test dataset");
        dataset.setKeyword("Key, word");
        dataset.setDataset("Dataset");
        dataset.setDescription("Dataset desc");
        dataset.setIssued("2021");
        dataset.setModified("2021");
        dataset.setTemporal("Temp test");
        dataset.setLanguage("ENG");
        dataset.setSpatial("Test");
        dataset.setAccrualPeriodicity("1 month");
        dataset.setTemporalResolution("good");
        dataset.setTheme(themes());
        dataset.setDistribution(distributions());
        dataset.setDatasetInformation(datasetInformation());

        return List.of(dataset);
    }

    private List<String> themes() {

        return List.of("Theme test");
    }

    private List<Distribution> distributions() {
        Distribution distribution = new Distribution();
        distribution.setTitle("Test");
        distribution.setDescription("Desc");
        distribution.setLicense("MIT");
        distribution.setAccessRights("false");
        distribution.setDownloadType("URL");
        distribution.setConformsTo("Dont know");
        distribution.setMediaType("PDF");
        distribution.setPackageFormat("NA");
        distribution.setAccessService(accessServices());

        return List.of(distribution);
    }

    private List<AccessService> accessServices() {
        AccessService accessService = new AccessService();
        accessService.setConformsTo("Dont know");
        accessService.setEndpointDescription("From AWS");
        accessService.setEndpointDescription("http//example.ie");
        accessService.setServesDataset("NA");
        accessService.setServiceSpecs("NA");

        return List.of(accessService);
    }

    private List<DatasetInformation> datasetInformation() {

        DatasetInformation datasetInformation = new DatasetInformation();

        datasetInformation.setMeasurementType("Dont know");
        datasetInformation.setMeasurementChannelType("2.4MHz");
        datasetInformation.setSensorId("1234");
        datasetInformation.setCppType("12345");
        datasetInformation.setDeviceId("123456");
        datasetInformation.setSensorType("Pressure");

        return List.of(datasetInformation);
    }


    public DataOffering extDtoToEntity(ExtDataOfferingDto dto) {

        DataOffering entity = new DataOffering();

        entity.setDataOfferingId(dto.getDataOfferingId());

        entity.setProvider(dto.getProvider());
        entity.setMarketId(dto.getMarketId());
        entity.setOwner(dto.getOwner());

        String version = Integer.toString(dto.getVersion());
        entity.setVersion(version);

        entity.setDataOfferingTitle(dto.getDataOfferingTitle());

        entity.setDataOfferingDescription(dto.getDataOfferingDescription());

        entity.setCategory(dto.getCategory());

        entity.setStatus(dto.getStatus());

        entity.setDataOfferingExpirationTime(dto.getDataOfferingExpirationTime());

        entity.setContractParameters(dto.getContractParameters());

        entity.setHasPricingModel(dto.getHasPricingModel());

        entity.setHasDataset(dto.getHasDataset());

        return entity;
    }


    private DataProvider dataProvider() {
        DataProvider newDataProvider = new DataProvider();

        newDataProvider.setProviderId("uiot");
        newDataProvider.setName("provider");
        newDataProvider.setDescription("Provider_test");
        newDataProvider.setOrganization(organisation());

        return newDataProvider;
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

    //This is to create a customised Pageable
    private Pageable createPageable(int page, int size) {
        Pageable pageable = new Pageable() {
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
        return pageable;
    }
}
