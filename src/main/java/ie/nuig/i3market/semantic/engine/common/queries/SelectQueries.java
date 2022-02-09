package ie.nuig.i3market.semantic.engine.common.queries;


import ie.nuig.i3market.semantic.engine.common.CommonUtil;
import ie.nuig.i3market.semantic.engine.common.annotations.RDF;
import ie.nuig.i3market.semantic.engine.common.annotations.Type;
import ie.nuig.i3market.semantic.engine.exceptions.BadRequestAlertException;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.*;
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
import org.springframework.data.domain.Pageable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */


public class SelectQueries {

    private static final Logger log = LoggerFactory.getLogger(SelectQueries.class);
    Query query;
     static QueryExecution qExe;
    static boolean isCollection;

    public SelectQueries(Query query){
        this.query=query;
    }

    /**
     * To generate a simple query for any entity that includes only one field
     * @param entityObject, whose field should be set with the value e.g., class, or object value
     * @param subject
     * @param pageable
     */
    public void createQueryToListInstances(Object entityObject, String subject, Pageable pageable){
        final  ElementGroup elementGroup = new ElementGroup();
        query.setQuerySelectType();
        Field [] fields= entityObject.getClass().getDeclaredFields();
        Arrays.stream(fields).forEach(field ->{
            try {
                Object objectValue = CommonUtil.getFieldValue(entityObject, field);
                String classNameinLower = CommonUtil.getFieldDeclaringClassName_LowerCase(field);
                RDF annotationAsPredicate = field.getAnnotation(RDF.class);
                query.setDistinct(true);

                // parameters are: query object, elementgroup to hold the triple patters, subject, predicate, object, field name, and entity class name in lower case
                createTripleWithoutFilter(query, elementGroup, subject, annotationAsPredicate.value(), objectValue.toString(), field.getName(), classNameinLower);

                query.setQueryPattern(elementGroup);

                setPageable(pageable);

            } catch (ClassNotFoundException  e) {
                e.printStackTrace();
            }
        });
    }



    // https://www.tabnine.com/code/java/methods/org.apache.jena.query.Query/setLimit

    private void setPageable (Pageable pageable){
        query.setReduced(false);
        if (pageable == null) {
            query.setLimit(Query.NOLIMIT);
        }

        else{
            log.info("pagination page size:{}  page number: {}  page offset {}", pageable.getPageSize(), pageable.getPageNumber(), pageable.getOffset());
            query.setLimit(pageable.getPageSize());
            query.setOffset(pageable.getPageNumber());

        }


    }

    public void createEntityToSelect(Object giveObject, String subject, Pageable pageable) throws ClassNotFoundException {

        final  ElementGroup elementGroup = new ElementGroup();

        try {
            // set the SPARQL query type to SELECT
            query.setQuerySelectType();
            // get the mapper fields
            Field[] fields = giveObject.getClass().getDeclaredFields();

            for (Field field : fields) {
                Object objectValue = CommonUtil.getFieldValue(giveObject, field);
                if (objectValue != null)
                    createFieldTriple(query, elementGroup, subject, objectValue.toString(), field, CommonUtil.getFieldDeclaringClassName_LowerCase(field));
                else
                    createFieldTriple(query, elementGroup, subject, null, field, CommonUtil.getFieldDeclaringClassName_LowerCase(field));

        }

            query.setQueryPattern(elementGroup);
            setPageable(pageable);

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
           else if (annotation instanceof Type) {

                String classType = ((Type) annotation).typeOf();
                createTripleForRDFType(query, elementGroup, subject, org.apache.jena.vocabulary.RDF.type.toString(), classType);
            }
        }
    }

    private static void createCollectionTriples(Query query, ElementGroup elementGroup, String subject, Field field) throws InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {

        java.lang.reflect.Type claz = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        String fieldClassName =Class.forName(claz.getTypeName()).getSimpleName().toLowerCase();
        Field fields[] = Class.forName(claz.getTypeName()).getDeclaredFields();

        if(!claz.getClass().equals(String.class))
        for (Field feeld : fields) {


                createFieldTriple(query, elementGroup, subject, null,feeld, fieldClassName);
        }
    }

    private static void createTripleForRDFType (Query query, ElementGroup elementGroup, String subject, String predicate, String rdfType){

        ElementTriplesBlock elementTriplesBlock = new ElementTriplesBlock();

        if(SPARQLUtil.isVariable(subject)) {

            elementTriplesBlock.addTriple(Triple.create(Var.alloc(subject), SPARQLUtil.createNode(predicate), NodeFactory.createURI(rdfType)));
        }
        else {
            elementTriplesBlock.addTriple(Triple.create(SPARQLUtil.createNode(subject), SPARQLUtil.createNode(predicate), Var.alloc(rdfType)));
        }
        elementGroup.addElement(elementTriplesBlock);
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
//        log.info("query is executed at endpoint {}", endpoint);
//        log.info("the actual query is {}", qry);
        try {
            Thread.sleep(100);
            System.out.println("Theard = " + Thread.currentThread().getName());
            //log.info("is query execution closed {}", qExe.isClosed());
            qExe = QueryExecutionFactory.sparqlService(endpoint, query, graph);
//            qExe.setTimeout(2, TimeUnit.SECONDS);
            resultSet = qExe.execSelect();
//            qExe.close();
        }catch(QueryException | InterruptedException exp){
            log.debug("query exception within select {}", exp);
            exp.printStackTrace();
            qExe.close();
            // Try to return an empty list, e.g.
            return null;
        }
//        qExe.close();
        return resultSet;
    }

    public void closeQueryExec(){
        qExe.close();
    }

    public static void closeQueryExec1(){
        qExe.close();
    }

}
