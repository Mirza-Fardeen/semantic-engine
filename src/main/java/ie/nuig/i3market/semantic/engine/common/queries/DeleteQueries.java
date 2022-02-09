package ie.nuig.i3market.semantic.engine.common.queries;


import ie.nuig.i3market.semantic.engine.common.CommonUtil;
import ie.nuig.i3market.semantic.engine.common.Vocabulary;
import ie.nuig.i3market.semantic.engine.common.annotations.RDF;
import ie.nuig.i3market.semantic.engine.exceptions.BadRequestAlertException;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.expr.E_Equals;
import org.apache.jena.sparql.expr.E_Str;
import org.apache.jena.sparql.expr.ExprVar;
import org.apache.jena.sparql.expr.nodevalue.NodeValueNode;
import org.apache.jena.sparql.expr.nodevalue.NodeValueString;
import org.apache.jena.sparql.syntax.ElementFilter;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.apache.jena.sparql.syntax.ElementOptional;
import org.apache.jena.sparql.syntax.ElementTriplesBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */

public class DeleteQueries {

    private static final Logger log = LoggerFactory.getLogger(DeleteQueries.class);

    Query query;
    static QueryExecution qExe;
    static boolean isCollection;

    public DeleteQueries(Query query){
        this.query=query;
    }


    public void createEntityToSelect(Object dataOffering, String subject) throws ClassNotFoundException {

        final  ElementGroup elementGroup = new ElementGroup();

        try {
            // set the SPARQL query type to SELECT
            query.setQuerySelectType();
            // get the mapper fields
            Field[] fields = dataOffering.getClass().getDeclaredFields();

            for (Field field : fields) {
                Object objectValue = CommonUtil.getFieldValue(dataOffering, field);
                if (objectValue != null)
                    createFieldTriple(query, elementGroup, subject, objectValue.toString(), field, CommonUtil.getFieldDeclaringClassName_LowerCase(field));
                else
                    createFieldTriple(query, elementGroup, subject, null, field, CommonUtil.getFieldDeclaringClassName_LowerCase(field));
            }

            query.setQueryPattern(elementGroup);


        }catch (Exception e){
            throw new BadRequestAlertException(e.getMessage(),"select registration offering", "invalid-input");
        }

    }

    private static void createFieldTriple(Query query, ElementGroup elementGroup, String subject, String object, Field field, String fieldClassName) throws IllegalAccessException, ClassNotFoundException, InvocationTargetException, InstantiationException {

        if (subject=="" || subject==null)
            subject = fieldClassName;

        for (Annotation annotation : field.getAnnotations()) {
            isCollection=false;
            if (annotation instanceof RDF && CommonUtil.isCollection(field)) {
                Object fieldGenericClass = CommonUtil.getGenericTypeInstanceOfListField(field);


                if(fieldGenericClass.equals(""))
                    isCollection=false;
                else
                    isCollection =true;
                // createTriple(query, elementTriplesBlock, subject, ((ie.nuig.i3market.common.annotations.RDF) annotation).value(), field.getName(), null);
                if(object!=null)
                    createTriple(query, elementGroup, subject,((RDF) annotation).value(), object, field.getName(), fieldClassName);
                else
                    createTriple(query, elementGroup, subject, ((RDF) annotation).value(), object, field.getName(), null);

                createCollectionTriples(query, elementGroup, field.getName(), field);
            }
            if (annotation instanceof RDF && !CommonUtil.isCollection(field)) {
                if(object!=null)
                    createTriple(query, elementGroup, subject,((RDF) annotation).value(), object, field.getName(), fieldClassName);
                else
                    createTriple(query, elementGroup, subject, ((RDF) annotation).value(), object,field.getName(), fieldClassName);
            }
/*            else if (annotation instanceof Type) {

                createTriple(query, elementGroup, subject, RDF.type.toString(), object,field.getName(), CommonUtil.getFieldClassName_LowerCase(field));
            }*/
        }
    }

    private static void createCollectionTriples(Query query, ElementGroup elementGroup, String subject, Field field) throws InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {

        java.lang.reflect.Type claz = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        String fieldClassName =Class.forName(claz.getTypeName()).getSimpleName().toLowerCase();
        Field fields[] = Class.forName(claz.getTypeName()).getDeclaredFields();

        if(!claz.getClass().equals(String.class))
            for (Field feeld : fields) {

        /*    Object objectValue = CommonUtil.getFieldValue(claz, field);

            if(objectValue!=null)
                //set the object value
                createFieldTriple(query, elementGroup, subject, objectValue.toString(),feeld, fieldClassName);
            else*/
                createFieldTriple(query, elementGroup, subject, null,feeld, fieldClassName);
            }
    }

    private static  void  createTripleWithoutFilter(Query query, ElementGroup elementGroup, String subject, String predicate, String object, Object fieldName, String fieldClassName){

        if (fieldName == null)
            return;

        if (subject=="" || subject==null)
            subject = fieldClassName;

        String objectVar =null;

        // if object value is not provided
        if(fieldClassName!=null && object==null) {
            objectVar = fieldName.toString() + "_" + fieldClassName.trim();
            query.addResultVar(Var.alloc(objectVar));
        }
        else{
            objectVar = object;
            query.addResultVar(Var.alloc(subject));
        }

        ElementTriplesBlock elementTriplesBlock = new ElementTriplesBlock();

        if(SPARQLUtil.isVariable(subject)) {

            elementTriplesBlock.addTriple(Triple.create(Var.alloc(subject), SPARQLUtil.createNode(predicate), NodeFactory.createURI(objectVar)));
        }
        else {
            elementTriplesBlock.addTriple(Triple.create(SPARQLUtil.createNode(subject), SPARQLUtil.createNode(predicate), Var.alloc(objectVar)));
        }

        if (object!=null)
            elementGroup.addElement(elementTriplesBlock);

    }
    private static void createTriple(Query query, ElementGroup elementGroup, String subject, String predicate, String object, Object fieldName, String fieldClassName) {
        if (fieldName == null)
            return;

        String objectVar;

        // if object value is not provided
        if(fieldClassName!=null && object==null) {
            objectVar = fieldName.toString() + "_" + fieldClassName.trim();
            query.addResultVar(Var.alloc(objectVar));
        }
        else{
            objectVar = fieldName.toString().trim();
            query.addResultVar(Var.alloc(objectVar));
        }

        ElementTriplesBlock elementTriplesBlock = new ElementTriplesBlock();

        if(SPARQLUtil.isVariable(subject)) {

            elementTriplesBlock.addTriple(Triple.create(Var.alloc(subject), SPARQLUtil.createNode(predicate), Var.alloc(objectVar)));
        }
        else {
            elementTriplesBlock.addTriple(Triple.create(SPARQLUtil.createNode(subject), SPARQLUtil.createNode(predicate), Var.alloc(objectVar)));
        }

        // add optional here
        if(!isCollection)
        {ElementOptional elementOptional = new ElementOptional(elementTriplesBlock);
            elementGroup.addElement(elementOptional);}
        else{
            elementGroup.addElement(elementTriplesBlock);
        }

        if (object!=null)
            elementGroup.addElement(qryEqualFilter(objectVar, object));

    }

    public  static ElementFilter qryEqualFilter(String var, String value){

        boolean isResource= SPARQLUtil.isResource(value);

        E_Equals expr;
        if (isResource)
            expr = new E_Equals(new ExprVar(var), new NodeValueNode(NodeFactory.createURI(value)));
        else
            expr = new E_Equals(new ExprVar(var), new E_Str(new NodeValueString(value)));
        return new ElementFilter(expr);

    }

    public  ResultSet execSelect (Query qry, String endpoint, String graph){

        Query query = QueryFactory.create(qry);
        ResultSet resultSet = null;
        log.info("query is executed at endpoint {}", endpoint);
//        log.info("the actual query is {}", qry);

        try {
            Thread.sleep(100);
            qExe = QueryExecutionFactory.sparqlService(endpoint, query, graph);
            //qExe.setTimeout(1500);
            resultSet = qExe.execSelect();
        }catch(QueryException | InterruptedException exp){
            //throw  new QueryException(exp);
            return null;
        }
        return resultSet;
    }

    public  void closeQueryExec(){
        qExe.close();
    }

    public static String  delete(Statement statement, String graph){

        if(statement== null)
            throw new NullPointerException();

        Resource subject = statement.getSubject();
        Property predicate = statement.getPredicate();
        RDFNode object = statement.getObject();

        if(object==null || object.toString().isEmpty())
            return null;
        // here theme will be skipped, otherwise current implementation  will remove
        // or overwrite all the existing themes in database

        if (predicate.toString()!= Vocabulary.DCAT.Properties.theme) {
            String query = "WITH <" + graph + "> \n" +
                    "DELETE {?s ?p ?o} \n" +
                    "INSERT { ?s ?p " + SPARQLUtil.prepareResourceOrLiteral(object.toString()) + " } \n" +
                    "WHERE { \n" +
                    "?s ?p ?o. \n" +
                    "VALUES (?s ?p ) {(<" + subject + "> <" + predicate + ">) } \n" +
                    "}";
            return query;
        }
        return null;
    }

    public static String deleteQuery(RDFNode rdfNode, String graph) {

        if (rdfNode==null)
            return null;

        String query = "WITH <" + graph + "> \n" +
                "DELETE {"
                + SPARQLUtil.prepareResourceOrLiteral(rdfNode.toString()) + " ?p ?o. \n" +
                "?p1 ?o1 "+ SPARQLUtil.prepareResourceOrLiteral(rdfNode.toString()) + "\n" +
                "} \n" +
                "WHERE {"
                + SPARQLUtil.prepareResourceOrLiteral(rdfNode.toString()) + " ?p ?o. \n" +
                "OPTIONAL {"+
                "?p1 ?o1 "+ SPARQLUtil.prepareResourceOrLiteral(rdfNode.toString()) + "\n" +
                "} \n"+
                "} \n";

        return query;
    }
}