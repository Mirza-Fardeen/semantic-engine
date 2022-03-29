package ie.nuig.i3market.semantic.engine.repository;

import ie.nuig.i3market.semantic.engine.common.CommonUtil;
import ie.nuig.i3market.semantic.engine.common.ServiceUtil;
import ie.nuig.i3market.semantic.engine.common.Vocabulary;
import ie.nuig.i3market.semantic.engine.common.annotations.Type;
import ie.nuig.i3market.semantic.engine.common.queries.DeleteQueries;
import ie.nuig.i3market.semantic.engine.common.queries.SPARQLUtil;
import ie.nuig.i3market.semantic.engine.common.queries.SelectQueries;
import ie.nuig.i3market.semantic.engine.config.databases.DistributedStorageConfiguration;
import ie.nuig.i3market.semantic.engine.config.databases.VirtuosoConfiguration;
import ie.nuig.i3market.semantic.engine.config.notifications.NotificationsConfiguration;
import ie.nuig.i3market.semantic.engine.domain.DataProvider;
import ie.nuig.i3market.semantic.engine.domain.distributed.EnginesDetails;
import ie.nuig.i3market.semantic.engine.domain.optimise.OfferingContracts;
import ie.nuig.i3market.semantic.engine.dto.Mapper;
import ie.nuig.i3market.semantic.engine.exceptions.BindingException;
import org.apache.jena.query.Query;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.core.Var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is a generic class that can be used to perform different queries for different parts of offering
 * e.g. pricing model, contract parameters, dataset, etc.
 * @param <T>
 */

public class General<T> {

    private static final Logger log = LoggerFactory.getLogger(General.class);

    private VirtuosoConfiguration virtuosoConfiguration;

    private OfferingContracts obj;

    private DistributedStorageConfiguration distributedStorageConfiguration;
    private NotificationsConfiguration notificationsConfiguration;

    @Autowired
    private Mapper mapper;

    @Autowired
    private ServiceUtil serviceUtil;

    public General(VirtuosoConfiguration virtuosoConfiguration, OfferingContracts obj, DistributedStorageConfiguration distributedStorageConfiguration, NotificationsConfiguration notificationsConfiguration, Mapper mapper, ServiceUtil serviceUtil) {
        this.virtuosoConfiguration = virtuosoConfiguration;
        this.obj = obj;
        this.distributedStorageConfiguration = distributedStorageConfiguration;
        this.notificationsConfiguration = notificationsConfiguration;
        this.mapper = mapper;
        this.serviceUtil = serviceUtil;
    }

    public General() {

    }

    //This method will not query offering from distributed database
    public Page<T> offeringComponentById(String id, Pageable pageable, CreateNewObjectClazz<T> p, T offering, Field annotatedField) throws BindingException {

        List<T> lstOfOffering =  listOfOfferingComponentById(id, pageable, p, offering, annotatedField);
        return new PageImpl<T>(lstOfOffering, pageable, lstOfOffering.size());
    }


    //This method will not query offering from distributed database
    public List<T> listOfOfferingComponentById(String id, Pageable pageable, CreateNewObjectClazz<T> p, T offering, Field annotatedField) throws BindingException {

        Query query = new Query();

        List<T> lstOfOffering = new ArrayList<>();

        SelectQueries selectQueries = new SelectQueries(query);


        try {

            annotatedField.setAccessible(true);
            annotatedField.set(offering, id);
//            System.out.println("Field: " + annotatedField);
            selectQueries.createEntityToSelect(offering, "", pageable);

            ResultSet resultSet = null;

            log.info("before query execution");
            resultSet = selectQueries.execSelect(query, virtuosoConfiguration.getEndpointUrl()
                    + virtuosoConfiguration.getQueryInterface(), null);
            log.info("..query successfully executed for getting offerings list filtered by provider id..");
            while (resultSet != null && resultSet.hasNext()) {

                    offering = p.createNewObject();

                    QuerySolution solution = resultSet.nextSolution();

                    SPARQLUtil.solutionToEntityMapping(offering, query.getProjectVars(), solution);

                    lstOfOffering.add(offering);

            }
            //close the execution to release the resources
            selectQueries.closeQueryExec();
            //}
        } catch (IllegalAccessException | ClassNotFoundException | InstantiationException | NoSuchFieldException e) {
            throw new BindingException(e.toString());
        }
        // 3rd parameter should be total count of query result. since list adds the results, therefore, its size can be used instead of total count from query.
        return lstOfOffering;
    }



    //This method will query offering from internal and distributed database
    public  Page<T> offeringComponentByCategory(String category, Pageable pageable, CreateNewObjectClazz<T> p, T offering, Field annotatedField) throws BindingException {

        List<T> lstOfOffering = listOfOfferingComponentByCategory(category, pageable, p, offering, annotatedField);

        // 3rd parameter should be total count of query result. since list adds the results, therefore, its size can be used instead of total count from query.
        return new PageImpl<>(lstOfOffering, pageable, lstOfOffering.size());
    }


    //This method will query list offering from distributed database
    // IF you want to ignore the query from distributed database,
    //see below comments
    public  List<T> listOfOfferingComponentByCategory(String category, Pageable pageable, CreateNewObjectClazz<T> p, T offering, Field annotatedField) throws BindingException {

//        Offering dataOffering = new Offering();
        Query query = new Query();
        List<T> lstOfOffering = new ArrayList<>();
        SelectQueries selectQueries = new SelectQueries(query);

        try {

            annotatedField.setAccessible(true);
            annotatedField.set(offering, category);

            selectQueries.createEntityToSelect(offering, "", pageable);

            List<EnginesDetails> engineDetailsLst = new ArrayList<>();

            // get remote endpoints details
            List<EnginesDetails> remoteEndpointsDetails = distributedStorageConfiguration.listEnginesWithDetails();
            if (remoteEndpointsDetails != null)
                engineDetailsLst.addAll(remoteEndpointsDetails);

            // add local endpoint in remote endpoints list
            List<String> localEndpCategory = new ArrayList<>();
            localEndpCategory.add(SPARQLUtil.getLocalName(category));
            engineDetailsLst.add(new EnginesDetails(virtuosoConfiguration.getEndpointUrl(), localEndpCategory));

            // get
//            String localHostAddress =CommonUtil.getLocalHostAddress();

            // This will be excluded when semantic engine search for external offering
            String excludedDatabaseIpAddress = serviceUtil.findExecludedDatabaseIpAddress();


            // This for loop will query data from distributed databases. Remove this for loop if you dont want to query from distributed database
            for (EnginesDetails enginesDetails : engineDetailsLst) {
                //skip querying endpoint from remote data whose IP is the same of the current host, hence it will take time for docker networking issues  OR may not work
                // we are using container name for local endpoint as host name ( i.e., http://virtuoso:8890/sparql)

                log.debug("engine address is {} : ", enginesDetails.getLocation());
//                if (enginesDetails.getLocation().equals(endpointToExclude) || enginesDetails.getLocation() == endpointToExclude ||

                if (enginesDetails.getLocation().equals(excludedDatabaseIpAddress) || enginesDetails.getLocation() == excludedDatabaseIpAddress) {
                    log.info("skipped query execution for the endpoint {} ", enginesDetails.getLocation());
                    continue;
                }


                // convert to lowercase each category from remote list
                List<String> lowercaseList = enginesDetails.getData_categories().stream().map(String::toLowerCase).collect(Collectors.toList());
                log.debug("engine is about categories {} : ", enginesDetails.getData_categories());

                // only create and execute query if the category requested is present in the list
                if (lowercaseList != null && lowercaseList.contains(SPARQLUtil.getLocalName(category))) {
                    ResultSet resultSet = null;
                    // resultSet = selectQueries.execSelect(query, endp);
                    log.info("before query execution");
                    resultSet = selectQueries.execSelect(query, enginesDetails.getLocation()
                            + virtuosoConfiguration.getQueryInterface(), null);
                    System.out.println("Query is: " + query.toString());
                    log.info("..query successfully executed for getting offerings filtered by category..");
                    while (resultSet != null && resultSet.hasNext()) {



                            offering = p.createNewObject();

                            QuerySolution solution = resultSet.nextSolution();

                            SPARQLUtil.solutionToEntityMapping(offering, query.getProjectVars(), solution);

                            lstOfOffering.add(offering);

                    }
                    selectQueries.closeQueryExec();
                }
            }
        } catch (IllegalAccessException | ClassNotFoundException | InstantiationException | NoSuchFieldException | IOException e) {
            throw new BindingException("can not get the list of offerings because of:  " + e.toString());
        }
        // 3rd parameter should be total count of query result. since list adds the results, therefore, its size can be used instead of total count from query.
        return lstOfOffering;
    }


    // Get a list of all offering from internal database

    public List<T> getOfferingsList(Pageable pageable, CreateNewObjectClazz<T> p, T offering, Field annotatedField) {
//        OfferingsList offerings = new OfferingsList();
        Query query = new Query();

        List<T> offeringsList = new ArrayList<>();
        SelectQueries selectQueries = new SelectQueries(query);

        try {
            // set the dataset field value that will be used as an object value in query
//            Field annotatedField = CommonUtil.getAnnotatedField(OfferingsList.class.getDeclaredFields(), Type.class);
            Type annotation = annotatedField.getAnnotation(Type.class);
            annotatedField.setAccessible(true);
            annotatedField.set(offering, annotation.typeOf());
            System.out.println("Annotation field: " + annotatedField.getName());
            selectQueries.createQueryToListInstances(offering, null, pageable);

            log.info("before query execution");
            ResultSet resultSet = selectQueries.execSelect(query, virtuosoConfiguration.getEndpointUrl()
                    + virtuosoConfiguration.getQueryInterface(), null);
            log.info("..query successfully executed for providers list..");
            while (resultSet!=null && resultSet.hasNext()) {

                    offering = p.createNewObject();

                    QuerySolution solution = resultSet.nextSolution();

                    SPARQLUtil.solutionMapping_EntityWithOneField(offering, query.getProjectVars(), solution);

                    offeringsList.add(offering);

            }
            selectQueries.closeQueryExec();

        }catch (IllegalAccessException e){
            new BindingException(e.toString());
        }catch (NullPointerException nullPointerException){
            throw new NullPointerException ("no result found: " + nullPointerException.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // 3rd parameter should be total count of query result. since list adds the results, therefore, its size can be used instead of total count from query.
        return offeringsList;
    }



    //This method is to delete an offering
    public Set<String> deleteQueryList(String offeringId,
                                       VirtuosoConfiguration virtuosoConfiguration,
                                       T dataOffering,
                                       Class theClazz
//                                        Field annotatedField1
    ) throws BindingException, ClassNotFoundException {

//        List<String> deleteQueryList = new ArrayList<>();

//        Set<String> deleteQueryList = new HashSet<String>();
        Set<String> deleteQueryList = new HashSet<>();

        Field annotatedField= CommonUtil.getAnnotatedField(theClazz.getDeclaredFields(), Type.class);


//        OfferingContracts dataOffering = new OfferingContracts();
        String providerId = extractProviderId(offeringId);
//        String  providerIdUrl = Vocabulary.RESOURCE_URI+"dataprovider/"+providerId;

        String  providerIdUrl = Vocabulary.RESOURCE_URI + CommonUtil.getClassName_LowerCase(DataProvider.class) + "/" + providerId;

        System.out.println("ProviderId URL: " + providerIdUrl);
        offeringId = Vocabulary.RESOURCE_URI + offeringId;
        Query query = new Query();
        DeleteQueries deleteQueries = new DeleteQueries(query);
        ResultSet resultSet=null;

        try {
            // set the offeringid field value that will be used as an object value in query
//            Field annotatedField= CommonUtil.getAnnotatedField(offeringContractsClazz.getDeclaredFields(), Type.class);

            annotatedField.setAccessible(true);
            annotatedField.set(dataOffering,offeringId);

            deleteQueries.createEntityToSelect(dataOffering, offeringId);

            resultSet = deleteQueries.execSelect(query, virtuosoConfiguration.getEndpointUrl()
                    + virtuosoConfiguration.getQueryInterface(), virtuosoConfiguration.getGraph());

//            log.info("resultSet = "+ resultSet.nextSolution().toString());

            while (resultSet!=null &&  resultSet.hasNext()){
                QuerySolution querySolution = resultSet.nextSolution();
                for (Var var: query.getProjectVars()){
                    RDFNode rdfNode = querySolution.get(var.getVarName());
                    // Return an "s" from RDF database
//                    if (rdfNode== null)
                    if (rdfNode == null || rdfNode.asNode().hasURI(providerIdUrl)) // Eliminate deletion of provider ID
                        continue;
                    String deleteQuery=deleteQueries.deleteQuery(rdfNode, virtuosoConfiguration.getGraph());

                    deleteQueryList.add(deleteQuery);

                }
            }

            deleteQueries.closeQueryExec();


        } catch (ClassNotFoundException | IllegalAccessException e) {
            throw new BindingException(e.getMessage(),"delete offering invalid-input");
        }

        return deleteQueryList;
    }

    //This function is to extract provider to avoid deletion of data provider when deleting an offering
    private String extractProviderId(String offeringId){
        String[] words = offeringId.split("_", 0);

        String regx = "_"+words[words.length-1];

        String[] finalProviderIDs = offeringId.split(regx, 2);

        return finalProviderIDs[0];
    }

    //This method is to delete an offering
    public Set<String> deleteProviderQueryList(String providerId,
                                       VirtuosoConfiguration virtuosoConfiguration,
                                       T dataOffering,
                                       Class theClazz
//                                        Field annotatedField1
    ) throws BindingException, ClassNotFoundException {

        Set<String> deleteQueryList = new HashSet<>();

        Field annotatedField= CommonUtil.getAnnotatedField(theClazz.getDeclaredFields(), Type.class);


        String  providerIdUrl = Vocabulary.RESOURCE_URI + CommonUtil.getClassName_LowerCase(DataProvider.class) + "/" + providerId;


//        String  providerIdUrl = Vocabulary.RESOURCE_URI + providerId;
        System.out.println("ProviderId URL: " + providerIdUrl);

//        String  additionalUrl = Vocabulary.RESOURCE_URI + CommonUtil.getClassName_LowerCase(DataProvider.class) + "/" + providerId;
        String idUrl = Vocabulary.RESOURCE_URI + providerId + "_" + CommonUtil.getClassName_LowerCase(DataProvider.class) + "1";

        Query query = new Query();
        DeleteQueries deleteQueries = new DeleteQueries(query);
        ResultSet resultSet=null;

        try {
            // set the offeringid field value that will be used as an object value in query
//            Field annotatedField= CommonUtil.getAnnotatedField(offeringContractsClazz.getDeclaredFields(), Type.class);

            annotatedField.setAccessible(true);
            annotatedField.set(dataOffering,providerIdUrl);

            deleteQueries.createEntityToSelect(dataOffering, providerIdUrl);

            resultSet = deleteQueries.execSelect(query, virtuosoConfiguration.getEndpointUrl()
                    + virtuosoConfiguration.getQueryInterface(), virtuosoConfiguration.getGraph());

//            log.info("resultSet = "+ resultSet.nextSolution().toString());

            while (resultSet!=null &&  resultSet.hasNext()){
                QuerySolution querySolution = resultSet.nextSolution();
                for (Var var: query.getProjectVars()){
                    RDFNode rdfNode = querySolution.get(var.getVarName());
                    // Return an "s" from RDF database
                    if (rdfNode== null)
                        continue;
                    String deleteQuery=deleteQueries.deleteQuery(rdfNode, virtuosoConfiguration.getGraph());

                    System.out.println("Delete queries for provider: " + deleteQuery);

                    deleteQueryList.add(deleteQuery);

                }
            }

            deleteQueries.closeQueryExec();


        } catch (ClassNotFoundException | IllegalAccessException e) {
            throw new BindingException(e.getMessage(),"delete offering invalid-input");
        }

        String graph = virtuosoConfiguration.getGraph();

//        String url = "http://i3-market.org/resource/uiot-provider_dataprovider1";
//        System.out.println(additionalUrlForProvider(graph, idUrl));

//        var t = virtuosoConfiguration.getGraph();

        deleteQueryList.add(additionalUrlForProvider(graph, idUrl));

        String  url2 = Vocabulary.RESOURCE_URI + CommonUtil.getClassName_LowerCase(DataProvider.class) + "/" + providerId;
        deleteQueryList.add(additionalUrlForProvider(graph, url2));

        return deleteQueryList;
    }


    public String additionalUrlForProvider(String graph, String idUrl) throws ClassNotFoundException {

        String query1 = "WITH <" + graph + "> \n" +
                "DELETE { <" + idUrl + "> ?p ?o. \n" +
                "?p1 ?o1 <"+ idUrl + ">\n" +
                "} \n" +
                "WHERE {<"
                + idUrl + "> ?p ?o. \n" +
                "OPTIONAL {"+
                "?p1 ?o1 <"+ idUrl + ">\n" +
                "} \n"+
                "} \n";

        return query1;
    }



    //This method will not query offering from distributed database
    public Page<T> getProviderById(String id, Pageable pageable, CreateNewObjectClazz<T> p, T offering, Field annotatedField) throws BindingException, ClassNotFoundException {

        List<T> lstOfOffering =  providerById(id, pageable, p, offering, annotatedField);
        return new PageImpl<T>(lstOfOffering, pageable, lstOfOffering.size());
    }

    //This method will not query offering from distributed database
    public List<T> providerById(String providerId, Pageable pageable, CreateNewObjectClazz<T> p, T offering, Field annotatedField) throws BindingException, ClassNotFoundException {

        String  id = Vocabulary.RESOURCE_URI + CommonUtil.getClassName_LowerCase(DataProvider.class) + "/" + providerId;

        Query query = new Query();

        List<T> lstOfOffering = new ArrayList<>();

        SelectQueries selectQueries = new SelectQueries(query);

        try {

            annotatedField.setAccessible(true);
            annotatedField.set(offering, id);
            System.out.println("Field: " + annotatedField);
            selectQueries.createEntityToSelect(offering, "", pageable);

            ResultSet resultSet = null;

            log.info("before query execution");
            resultSet = selectQueries.execSelect(query, virtuosoConfiguration.getEndpointUrl()
                    + virtuosoConfiguration.getQueryInterface(), null);
            log.info("..query successfully executed for getting offerings list filtered by provider id..");
            while (resultSet != null && resultSet.hasNext()) {

                offering = p.createNewObject();

                QuerySolution solution = resultSet.nextSolution();

                SPARQLUtil.solutionToEntityMapping(offering, query.getProjectVars(), solution);

                lstOfOffering.add(offering);

            }
            //close the execution to release the resources
            selectQueries.closeQueryExec();
            //}
        } catch (IllegalAccessException | ClassNotFoundException | InstantiationException | NoSuchFieldException e) {
            throw new BindingException(e.toString());
        }
        // 3rd parameter should be total count of query result. since list adds the results, therefore, its size can be used instead of total count from query.
        return lstOfOffering;
    }

}
