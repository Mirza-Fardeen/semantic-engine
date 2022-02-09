package ie.nuig.i3market.semantic.engine.common;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */

public class Vocabulary {

    private Vocabulary(){}

    private static final String NAMESPACE = "http://i3-market.org/";
    public static final String RESOURCE_URI = NAMESPACE + "resource/";

    /**
     * i3-Market Core ontology
     */
    public static  final  class CORE {

        private CORE(){}

        //**** CORE PROPERTIES ******
        public static final class Properties {




            private Properties(){}
            public static final String CORE = "http://i3-market.org/core#";

            public static final String id = CORE + "id";
            public static final String providerId = CORE + "providerId";
            public static final String organizationId = CORE + "organizationId";
            public static final String owner = CORE +"owner";
            public static final String dataOfferingLabel = CORE + "dataOfferingLabel";
            public static final String category = CORE +"category";
            public static final String provider = CORE + "provider";
            public static final String serviceSpecs = CORE + "serviceSpecs";
            public static final String dataOffering = CORE + "dataOffering";
            public static final String dataOfferingId = CORE + "dataOfferingId";
            public static final String hasPricingModel = CORE + "hasPricingModel";
            public static final String hasDataset = CORE + "hasDataset";
            public static final String dataOfferingExpirationTime = CORE + "dataOfferingExpirationTime";
            public static final String dataOfferingCreated = CORE + "dataOfferingCreated";
            public static final String lastModified = CORE + "lastModified";
            public static final String dataOfferingDescription = CORE + "dataOfferingDescription";
            public static final String marketId = CORE + "marketId";
            public static final String contractParameters = CORE + "contractParameters";
            public static final String status = CORE + "status";
            //            public static final String version= CORE +"version";
            public static final String datasetId = CORE + "datasetId";
            public static final String contractId = CORE + "contractId";
            //datasetinformation
            public static final String datasetInformation = CORE + "datasetInformation";
            public static final String datasetInformationId = CORE + "datasetInformationId";
            public static final String measurementType = CORE + "measurementType";
            public static final String measurementChannelType = CORE + "measurementChannelType";
            public static final String sensorId = CORE + "sensorId";
            public static final String deviceId = CORE + "deviceId";
            public static final String cppType = CORE + "cppType";
            public static final String sensorType = CORE + "sensorType";
            public static final String distributionId = CORE + "distributionId";
            public static final String dataServiceId = CORE + "dataServiceId";
            // contractual properties
            public static final String contractParametersId = CORE + "contractParametersId";
            public static final String interestOfProvider = CORE + "interestOfProvider";
            public static final String interestDescription = CORE + "interestDescription";
            public static final String hasGoverningJurisdiction = CORE + "hasGoverningJurisdiction";
            public static final String purpose = CORE + "purpose";
            public static final String purposeDescription = CORE + "purposeDescription";
            public static final String testName = CORE + "testName";
            public static final String hasIntendedUse = CORE + "hasIntendedUse";
            public static final String intendedUseId  = CORE + "intendedUseId";
            public static final String hasLicenseGrant  = CORE + "hasLicenseGrant";
            public static final String licenseGrantId  = CORE + "licenseGrantId";
            public static final String processData  = CORE +"processData";
            public static final String shareDataWithThirdParty  = CORE +"shareDataWithThirdParty";
            public static final String editData  = CORE +"editData";
            public static final String copyData  = CORE + "copyData";
            public static final String transferable  = CORE + "transferable";
            public static final String exclusiveness = CORE +"exclusiveness";
            public static final String revocable = CORE +"revocable";
        }

        //**** CORE CLASSES ******
        public static final class Classes {
            private Classes(){}
            public static final String CORE = "http://i3-market.org/core#";
            public static final String Provider = CORE+"Provider";
            public static final String DataOffering = CORE+"DataOffering";
            public static final String ContractParameters = CORE+"ContractParameters";
            public static final String IntendedUse = CORE+"IntendedUse";
            public static final String LicenseGrant = CORE + "LicenseGrant";
            public static final String DatasetInformation = CORE+"DatasetInformation";
        }
    }


    public static  final  class PricingModel {

        private PricingModel(){}

        //**** PricingModel PROPERTIES ******
        public static final class Properties {
            private Properties() {
            }

            public static final String PRICING_Model = "http://i3-market.org/pricingmodel#";
            public static final String pricingModelName = PRICING_Model + "pricingModelName";
            public static final String basicPrice = PRICING_Model + "basicPrice";
            public static final String timeDuration = PRICING_Model + "timeDuration";
            public static final String repeat = PRICING_Model + "repeat";
            public static final String hasSubscriptionPrice = PRICING_Model + "hasSubscriptionPrice";
            public static final String hasPaymentType = PRICING_Model + "hasPaymentType";
            public static final String paymentType = PRICING_Model + "paymentType";
            public static final String currency = PRICING_Model + "currency";
            public static final String fromValue = PRICING_Model + "fromValue";
            public static final String toValue = PRICING_Model + "toValue";
            public static final String pricingModelId = PRICING_Model + "pricingModelId";
            public static final String paymentId = PRICING_Model + "paymentId";
            public static final String hasPlanPrice = PRICING_Model + "hasPlanPrice";
            public static final String hasPaymentOnPlan = PRICING_Model + "hasPaymentOnPlan";
            public static final String hasPaymentOnSubscription = PRICING_Model + "hasPaymentOnSubscription";
            public static final String numberOfObject = PRICING_Model + "numberOfObject";
            public static final String hasApiPrice = PRICING_Model + "hasApiPrice";
            public static final String hasPaymentOnApi = PRICING_Model + "hasPaymentOnApi";
            public static final String dataUnit = PRICING_Model + "dataUnit";
            public static final String unitID = PRICING_Model + "unitID";
            public static final String hasUnitPrice = PRICING_Model + "hasUnitPrice";
            public static final String hasPaymentOnUnit = PRICING_Model + "hasPaymentOnUnit";
            public static final String dataSize = PRICING_Model + "dataSize";
            public static final String hasSizePrice = PRICING_Model + "hasSizePrice";
            public static final String hasPaymentOnSize = PRICING_Model + "hasPaymentOnSize";
            public static final String hasPriceFree = PRICING_Model + "hasPriceFree";
            public static final String hasFreePrice = PRICING_Model + "hasFreePrice";
            public static final String paymentOnPlanName = PRICING_Model + "paymentOnPlanName";
            public static final String paymentOnApiName = PRICING_Model + "paymentOnApiName";
            public static final String paymentOnUnitName = PRICING_Model + "paymentOnUnitName";
            public static final String paymentOnSizeName = PRICING_Model + "paymentOnSizeName";
            public static final String paymentOnSubscriptionName = PRICING_Model + "paymentOnSubscriptionName";
        }

        //**** PricingModel CLASSES ******
        public static final class Classes {
            private Classes() {
            }

            public static final String PRICING_Model = "http://i3-market.org/pricingmodel#";
            public static final String PricingModel = PRICING_Model + "PricingModel";
            public static final String PaymentOnSubscription = PRICING_Model + "PaymentOnSubscription";
            public static final String PaymentOnPlan = PRICING_Model + "PaymentOnPlan";
            public static final String PaymentOnApi = PRICING_Model + "PaymentOnApi";
            public static final String PaymentOnUnit = PRICING_Model + "PaymentOnUnit";
            public static final String PaymentOnSize = PRICING_Model + "PaymentOnSize";
            public static final String PaymentOnFreePrice = PRICING_Model + "PaymentOnFreePrice";

        }
    }

    /**
     * http://dcat.org/
     * DCAT vocabulary
     */
    public static  final  class DCAT {

        private DCAT(){}

        //**** DCAT PROPERTIES ******
        public static final class Properties {
            private Properties(){}
            public  static final String DCAT = "http://www.w3.org/ns/dcat#";
            public  static final String dataset = DCAT+"dataset";
            public  static final String contracts = DCAT+"contracts";
            public  static final String keyword = DCAT+"keyword";
            public  static final String temporalResolution = DCAT+"temporalResolution";
            public  static final String distribution = DCAT+"distribution";
            public  static final String theme = DCAT+"theme";
            public  static final String accessService = DCAT+"accessService";
            public  static final String endpointDescription = DCAT+"endpointDescription";
            public  static final String endpointURL = DCAT+"endpointURL";
            public  static final String servesDataset = DCAT+"servesDataset";
            public  static final String mediaType = DCAT+"mediaType";
            public  static final String packageFormat = DCAT+"packageFormat";
        }
        //**** DCAT CLASSES ******
        public static final class Classes {
            private Classes(){}
            public  static final String DCAT = "http://www.w3.org/ns/dcat#";
            public  static final String  Dataset = DCAT+"Dataset";
            public  static final String  Contract = DCAT+"Contract";
            public  static final String  ContractParameters = DCAT+"ContractParameters";
            public  static final String  Catalog = DCAT+"Catalog";
            public  static final String  Distribution = DCAT+"Distribution";
            public  static final String  DataService = DCAT+"DataService";
        }



    }

    /**
     * http://http://purl.org/dc/terms/
     * DCTERMS vocabulary
     */
    public static  final  class DCTERMS {

        private DCTERMS(){}

        //**** DCTERMS PROPERTIES ******
        public static final class Properties {
            private Properties(){}
            public  static final String DCTERMS = "http://purl.org/dc/terms/";

            public  static final String language = DCTERMS+"language";
            public  static final String publisher = DCTERMS+"publisher";
            public  static final String version = DCTERMS+"version";
            public  static final String title = DCTERMS+"title";
            public  static final String issued = DCTERMS+"issued";
            public  static final String modified = DCTERMS+"modified";
            public  static final String spatial = DCTERMS+"spatial";
            public  static final String creator = DCTERMS+"creator";
            public  static final String temporal = DCTERMS+"temporal";
            public  static final String conformsTo = DCTERMS+"conformsTo";
            public static final String description = DCTERMS + "description";
            public static final String fullname = DCTERMS + "fullname";
            public static final String description_test = DCTERMS + "description_test";
            public static final String license = DCTERMS + "license";
            public static final String accessRights = DCTERMS + "accessRights";
            public static final String downloadType = DCTERMS + "downloadType";
            public  static final String accrualPeriodicity = DCTERMS+"accrualPeriodicity";
            public static final String keyword = DCTERMS + "keyword";
            public static final String dataset = DCTERMS + "datasetName";
        }
        //**** DCTERMS CLASSES ******
        public static final class Classes {
            private Classes(){}
            public  static final String DCTERMS = "http://purl.org/dc/terms/";

        }
    }


    /**
     * http://schame.org
     * SCHEMA vocabulary
     */
    public static  final  class SCHEMA {

        private SCHEMA(){}
        //**** SCHEMA PROPERTIES ******
        public static final class Properties {
            private Properties(){}
            public static final String SCHEMA = "http://schema.org/";
            public  static final String language = SCHEMA+"language";
            public static final String name = SCHEMA + "name";
            public static final String description = SCHEMA + "description";
            public static final String description_test = SCHEMA + "description_test";
            public static final String sourceOrganization = SCHEMA + "sourceOrganization";
            public static final String address = SCHEMA + "address";
            public static final String contactPoint = SCHEMA + "contactPoint";
        }
        //**** SCHEMA CLASSES ******
        public static final class Classes {
            private Classes(){}
            public static final String SCHEMA = "http://schema.org/";
            public static final String Organization = SCHEMA+"Organization";
        }
    }

    public static  final  class SKOS {

        private SKOS(){}

        //**** SKOS PROPERTIES ******
        public static final class Properties {
            private Properties(){}
        }
        //**** SKOS CLASSES ******
        public static final class Classes {
            private Classes(){}
            public static final String SKOS = "http://www.w3.org/2004/02/skos/core#";

            public static final String Concept = SKOS+"Concept";
        }
    }


    /**
     * RDF related terms
     */
    public static final class RDF{
        private RDF(){}

        public static final String RDFS= "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
        public static final String RDF = "http://www.w3.org/2000/01/rdf-schema#";
        public static final String XSD ="http://www.w3.org/2001/XMLSchema#";
        public static final String label= RDFS+"label";
        public static final String type= RDFS+"type";
    }

    /**
     * RDF containers
     */
    public static final class RDFContainer{

        public static final String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

        public static final String Alt = RDF_NS+"Alt";
        public static final String Bag = RDF_NS+"Bag";
        public static final String Seq = RDF_NS+"Seq";

    }

}
