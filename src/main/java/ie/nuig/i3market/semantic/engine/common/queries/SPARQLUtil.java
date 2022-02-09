package ie.nuig.i3market.semantic.engine.common.queries;


import ie.nuig.i3market.semantic.engine.common.CommonUtil;
import ie.nuig.i3market.semantic.engine.common.DateUtils;
import ie.nuig.i3market.semantic.engine.common.Vocabulary;
import ie.nuig.i3market.semantic.engine.config.databases.VirtuosoConfiguration;
import org.apache.jena.datatypes.xsd.XSDDateTime;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.core.Var;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */
@Component
public class SPARQLUtil {



    static VirtuosoConfiguration virtuosoConfiguration;

    public SPARQLUtil(VirtuosoConfiguration virtuosoConfiguration) {
        this.virtuosoConfiguration =virtuosoConfiguration;
    }

    public static Query createQuery(String queryStr, Object resource, Pageable pageable) {

        Query query;

        // should not be null
        if (pageable == null) {
            throw new IllegalArgumentException("Invalid Pagination information");
        }

        try {
            // get the subject for query
            java.net.URI subject = SPARQLUtil.resourceFromIdentifier(resource);
            queryStr = String.format(queryStr, subject, pageable.getPageSize(), pageable.getOffset());
            query = QueryFactory.create(queryStr);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return query;

    }

    public static java.net.URI resourceFromIdentifier(Object identifier) {
        try {
            return new java.net.URI(Vocabulary.RESOURCE_URI + identifier);
        } catch (URISyntaxException uriSyntaxException) {
            throw new IllegalArgumentException(uriSyntaxException.getMessage(), uriSyntaxException);
        }
    }

    public static String prepareResourceOrLiteral(String value){
        return value.startsWith("http") ? "<" + value + ">" : "'" + value + "'";
    }

    public static boolean isVariable(String value){
        return value.startsWith("http") ? false : true;
    }


    public static Node createNode(String str) {
        return NodeFactory.createURI(str);
    }

    public static String removeStrBeginWithUnderscore(String string, String className){
        if(string== null)
            throw  new NullPointerException();
        if(string.contains("_")) {
            String remainingStr=string.substring(string. indexOf("_")+1).trim();
            if (remainingStr.equals(className))
            return string.substring(0, string.indexOf("_"));
        }
        return string;

    }

    public static String getLocalName(String str){
        if(str== null)
            throw  new NullPointerException();
                if(!isResource(str))
                    return str;

        return str.substring(str.lastIndexOf("/")+1).trim();
    }


    public static void setObjectProperty(Object object, String varName, RDFNode node) throws NoSuchFieldException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        Field field = object.getClass().getDeclaredField(varName);

        // we are not setting value to rdf type therefore, does not need to set such fields values
        //if (!field.isAnnotationPresent(Type.class)) {
            if(field!=null) {
                field.setAccessible(true);

                if(!CommonUtil.isCollection(field)){
                // if result is uri then remove IRI part otherwise get the lexical form literal
                if (node.isResource()) {
                    field.set(object, getLocalName(node.toString()));
                } else {
                    Class fieldClass = field.getType();
                    if (fieldClass.equals(Date.class)) {
                        setLocalDateTimeField(node, field, object);
                    } else if (fieldClass.equals(Integer.class)) {
                        setIntegerField(node, field, object);
                    } else {
                        field.set(object, node.asLiteral().getLexicalForm());
                    }
                }
            }else{
                    Object genericType = CommonUtil.getGenericTypeInstanceOfListField(field);
                    if(genericType.getClass().equals(String.class)){

                        List<String> strList = new ArrayList<>();

                        if(node!=null){
                        strList.add(node.toString());
                        field.set(object,strList);}
                    }
                }
        }
    //}
    }

    private static void setLocalDateTimeField(RDFNode node, Field field, Object object) throws IllegalAccessException {

        Class clazz = node.asLiteral().getDatatype().getJavaClass();

        if (clazz.equals(XSDDateTime.class)) {
            Date dateTime = DateUtils.getLocalDate(node.asLiteral().getLexicalForm());
            if (dateTime != null)
                field.set(object, dateTime);
        }
    }


    private static void setIntegerField(RDFNode node, Field field, Object object) throws IllegalAccessException {

        Class clazz = node.asLiteral().getDatatype().getJavaClass();

        if (clazz.equals(Integer.class)) {
            field.set(object, node.asLiteral().getInt());
        } else if (clazz.equals(String.class)) {
            field.set(object, Integer.parseInt(node.asLiteral().getLexicalForm()));
        }
    }



    public static void entityFieldsMapping(Object parentObject, Field parentClassfield, List<Var> varList, QuerySolution solution) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        for (Var var: varList) {
            String varName = var.getVarName().replace("?", "");
            RDFNode node = solution.get(var.getVarName());
            if (node == null)
                continue;
            // TODO send class name to this function, check if var name belongs to this class or not
            varName = removeStrBeginWithUnderscore(varName, CommonUtil.getClassName_LowerCase(parentObject.getClass()));
            if (parentClassfield.getName().equals(removeStrBeginWithUnderscore(varName, CommonUtil.getClassName_LowerCase(parentObject.getClass())))) {
                setObjectProperty(parentObject, varName, node);
                return;
            }
        }
    }

    public static void entityFieldsMapping(Object parentObject, List<Var> varList, QuerySolution solution) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        Field[] declaredFields = parentObject.getClass().getDeclaredFields();

        for (Var var: varList) {
            String varName = var.getVarName().replace("?", "");
            RDFNode node = solution.get(var.getVarName());
            if (node == null)
                continue;
            varName = removeStrBeginWithUnderscore(varName, CommonUtil.getClassName_LowerCase(parentObject.getClass()));
            String finalVarName = varName;
            boolean booleanVal = Arrays.stream(declaredFields).anyMatch(field ->
                    field.getName().equals(finalVarName));
            if (booleanVal) {
                setObjectProperty(parentObject, varName, node);
            }
        }
    }

    public static Object solutionMapping_EntityWithOneField(Object parentObject, List<Var> var, QuerySolution solution){
        Field [] declaredField = parentObject.getClass().getDeclaredFields();

       try {
           var.stream().forEach(c -> {

               Arrays.stream(declaredField).forEach(field -> {
                   RDFNode objectValue = solution.get(c.getVarName());
                   if (objectValue == null)
                       return;
                   String objectID = SPARQLUtil.getLocalName(objectValue.toString());
                   try {
                       field.setAccessible(true);
                       field.set(parentObject, objectID);
                   } catch (IllegalAccessException e) {
                       e.printStackTrace();
                   }
               });
           });
       }catch(Exception e){
           e.printStackTrace();
       }

        return parentObject;
    }


    public static Object solutionToEntityMapping(Object parentObject, List<Var> varList, QuerySolution solution) throws IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchFieldException {

        Field[] declaredFields = parentObject.getClass().getDeclaredFields();

        for (Field parentClassfield : declaredFields) {
            entityFieldsMapping(parentObject, parentClassfield,varList,solution);

            if (CommonUtil.isCollection(parentClassfield)) {
                //get the class of that list field
                Object object= CommonUtil.getGenericTypeInstanceOfListField(parentClassfield);

                Object valueObject = CommonUtil.getGenericTypeInstanceOfListField(parentClassfield);
                if(!object.getClass().equals(String.class) ){

                    entityFieldsMapping(object,varList,solution);

                    List<Object> lst = new ArrayList<>();
                    lst.add(object);

                    parentClassfield.setAccessible(true);
                    parentClassfield.set(parentObject,lst);

                }
                solutionToEntityMapping(object,varList,solution);
            }

        }
        return parentObject;
    }

    public static boolean isResource(String str) {
        return str.startsWith("http");
    }

    public static String getMaxId(String provider, String typeOf){

        String qry = "SELECT DISTINCT ?id {" +
                "?s a <%s>. \n" +
                "?s <" +Vocabulary.CORE.Properties.providerId+"> '%s' . \n" +
                "?s <" +Vocabulary.CORE.Properties.id+"> ?id . \n" +
                "} \n"+
                "ORDER BY DESC (?id) \n" +
                "LIMIT 1";
        qry = String.format(qry,typeOf,provider);


        Query query= QueryFactory.create(qry);

        try(QueryExecution qExe = QueryExecutionFactory.sparqlService(virtuosoConfiguration.getEndpointUrl()+
                virtuosoConfiguration.getQueryInterface(), query)) {

            ResultSet result = qExe.execSelect();

            if (!result.hasNext()) {
                return "1";
            } else {
                QuerySolution sol = result.nextSolution();
                RDFNode id = sol.get("id");
                long value = (Long.valueOf(id.asLiteral().getValue().toString()) + 1);
                String maxIdValue = Long.toString(value);
                return maxIdValue;
            }
        }

    }


    public static String  getResourceId(){

        return  null;

    }


}
