package ie.nuig.i3market.semantic.engine.common;


import ie.nuig.i3market.semantic.engine.common.annotations.Provider;
import ie.nuig.i3market.semantic.engine.common.annotations.RDF;
import ie.nuig.i3market.semantic.engine.common.annotations.RDFSubject;
import ie.nuig.i3market.semantic.engine.common.annotations.Type;
import ie.nuig.i3market.semantic.engine.common.queries.SPARQLUtil;
import ie.nuig.i3market.semantic.engine.domain.DataProvider;
import ie.nuig.i3market.semantic.engine.exceptions.BindingException;
import org.apache.jena.base.Sys;
import org.apache.jena.graph.BlankNodeId;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.XSD;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */
public class RDFBinding {

    Model model;
    boolean updateFlag;

    private String dataOfferingId;

    public String getDataOfferingId(){
        return this.dataOfferingId;
    }

    public RDFBinding(Model model){
        this.model=model;
    }

    public RDFBinding(Model model, boolean updateFlag)  {
    this.model= model;
    this.updateFlag = updateFlag;
    }


    /**
     *  input is the entity class which needs to be mapped in jena model
     * @param object
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public Resource marshal(Object object) throws IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {
        Class<?> clazz= object.getClass();
        Field [] declaredFields= clazz.getDeclaredFields();
        Resource subject = createSubject(object,declaredFields);
        marshalIntoRDF(object,subject,declaredFields);
        return subject;
    }

    private void marshalIntoRDF(Object object, Resource subject, Field[] declaredFields) throws IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException {
        addStatements(object,subject,declaredFields,null);
    }


    private Resource createSubject(Object object, Field[] declaredFields) throws ClassNotFoundException {

        // get subject field annotated with (RDFSubject)from this class object
        Field annotatedField= CommonUtil.getAnnotatedField(declaredFields, RDFSubject.class);
        if (annotatedField == null)
            throw new NullPointerException();
        // get value of this subject annotated field
        Object subjectValue= CommonUtil.getFieldValue (object, annotatedField);
        // get annotation of this subject
        RDFSubject rdfSubject = annotatedField.getAnnotation(RDFSubject.class);

        Resource rs = null;
        //if subject field is null
        if (subjectValue == null ) {
            // create resource URI which includes provider
            Field providerAnnotation= CommonUtil.getAnnotatedField(declaredFields, Provider.class);
            String provider= CommonUtil.getFieldValue (object, providerAnnotation).toString();

            // send as property to generate
            Field fieldId = CommonUtil.getAnnotatedField(declaredFields, RDF.class);
            RDF  resourceId = fieldId.getAnnotation(RDF.class);
            Property property = model.createProperty(resourceId.value());
            // if request is not for update
            if(!updateFlag)
            rs=createCustomId(declaredFields,provider,property);
        }else {
            if(!updateFlag)
               rs = model.createResource(rdfSubject.prefix() + CommonUtil.getFieldDeclaringClassName_LowerCase(annotatedField) + "/" + subjectValue.toString());
            else
                rs = model.createResource(rdfSubject.prefix() + subjectValue.toString());
        }

        return rs;
    }



    private <T> void addStatements(Object o, Resource subject, Field[] fields, Class<? extends Annotation> annotationClass) throws IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {

        String providerId = null;

        for (Field field : fields) {

            // the provider id is used to link this with other classes
            Field providerAnnotation= CommonUtil.getAnnotatedField(fields, Provider.class);
            providerId= (String) CommonUtil.getFieldValue (o, providerAnnotation);

            Property predicate = null;

            if (field.isAnnotationPresent(RDF.class) && !CommonUtil.isCollection(field)) {
                predicate = createProperty(field);
                RDFNode object = extractNodeFromField(o, field);

                if(subject!=null && predicate!=null && object!=null)
                   model.add(subject, predicate, object);

            }
            if ((field.isAnnotationPresent(RDF.class) && CommonUtil.isCollection(field))) {
                processCollectionFields(o, subject,field, providerId);
            }
            if(field.isAnnotationPresent(Type.class) && !CommonUtil.isCollection(field)){
                if(!updateFlag)
                    createCustomId(fields, providerId, null);

                Type typeAnnotation = field.getAnnotation(Type.class);
                model.add(subject, org.apache.jena.vocabulary.RDF.type, model.createResource(typeAnnotation.typeOf()));

            }

        }
    }

    private void addDatatypeProperty(Object o, Field field, Resource subject) throws InvocationTargetException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (field.isAnnotationPresent(RDF.class)) {
            Property predicate = createProperty(field);
            if(subject!=null && predicate!=null && o!=null)
                model.add(subject, predicate, o.toString());
         }
    }
    private <T> void processCollectionFields (Object o, Resource subject, Field field, String providerId) throws IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {

       try {
           Object objectList = CommonUtil.getFieldValue(o, field);
           List<T> genericType = (List<T>) objectList;

           if (genericType != null){
               for (T type : genericType) {

                   // if the class is not  CUSTOMIZED class, just  get the values and populate in as RDF
                   if (type.getClass().equals(String.class)) {
                       addDatatypeProperty(type, field, subject);
                       continue;
                   }

                   Resource rs = null;
                   // if request is not for update
                   if (!updateFlag) {
                       rs = createCustomId(type.getClass().getDeclaredFields(), providerId, null);
                   } else {
                       rs = getCustomId(type, providerId, null);
                   }


                   RDF rdfAnnotation = field.getAnnotation(RDF.class);
                   model.add(subject, model.createProperty(rdfAnnotation.value()), rs);
                   //createLD4Ids(type.getClass().getDeclaredFields(),providerId,model.createProperty(rdfAnnotation.value()));
                   //model.add(subject,model.createProperty(rdfAnnotation.value()),resource);

                   if (field.isAnnotationPresent(Type.class)) {
                       Type typeAnnotation = field.getAnnotation(Type.class);
                       model.add(rs, org.apache.jena.vocabulary.RDF.type, model.createResource(typeAnnotation.typeOf()));
                       //model.add(collectionRDFNode.asResource(), CommonUtil.getFieldValue(type, field).toString(),model.createProperty())
                   }

                   addStatementsOfCollection(type, rs, providerId);
               }

           // if the class is not  CUSTOMIZED class, just  get the values and populate in as RDF
           Object clazz = CommonUtil.getGenericTypeInstanceOfListField(field);
           System.out.println("class name " + clazz.getClass());
           if ((!clazz.getClass().equals(String.class) && genericType.size() == 0)) {

               Resource rs = null;
               // if request is not for update
               if (!updateFlag) {
                   rs = createCustomId(clazz.getClass().getDeclaredFields(), providerId, null);
               } else {
                   rs = getCustomId(clazz, providerId, null);
               }
               RDF rdfAnnotation = field.getAnnotation(RDF.class);
               model.add(subject, model.createProperty(rdfAnnotation.value()), rs);
           }
       }
       }catch (NullPointerException | BindingException e){
           throw new NullPointerException("null pointer exception");
       }

    }
    private <T> void addStatementsOfCollection(T type, Resource source, String providerId) throws IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException, BindingException {

        Field [] fields=type.getClass().getDeclaredFields();

        for (Field field:fields){

            Property predicate=null;

            if (field.isAnnotationPresent(RDF.class)) {
                predicate = createProperty(field);

                if(CommonUtil.isCollection(field)){
                    processCollectionFields(type,source,field,providerId);
                }else{
                RDFNode object = extractNodeFromField(type, field);
                if(source!=null && predicate!=null && object!=null){
                  model.add(source, predicate, object);}
                else{

                    // if request is not for update
                    if(!updateFlag) {
                        //do nothing
                        //createCustomId(fields, providerId, predicate);
                    }else{
                        if(object!=null)
                        getCustomId(fields, providerId, predicate);
                    }
                }

                }
            }if(field.isAnnotationPresent(Type.class)){
                Type typeAnnotation= field.getAnnotation(Type.class);
                model.add(source, org.apache.jena.vocabulary.RDF.type,model.createResource(typeAnnotation.typeOf()));
                if(!updateFlag)
                createCustomId(fields, providerId, predicate);
                //model.add(collectionRDFNode.asResource(), CommonUtil.getFieldValue(type, field).toString(),model.createProperty())
            }
        }

    }


    private RDFNode marshalObject(Object value, Field field) throws  ClassNotFoundException {
        if (value == null) {
            return null;
        }
        Resource dtUri = getDatatypeURI(value);
        //RDFDatatype datatype = org.apache.jena.vocabulary.RDF;
        if (dtUri != null) {
            // Literal
            String s = value.toString();
            if (value instanceof Date) {
                s = DateUtils.getISO8601DateFormat().format(((Date) value).toInstant());
            }

            if(field.isAnnotationPresent(ie.nuig.i3market.semantic.engine.common.annotations.Resource.class) && field.isAnnotationPresent(Provider.class)) {
                return model.createResource(Vocabulary.RESOURCE_URI+CommonUtil.getClassName_LowerCase(DataProvider.class)+"/"+s);
            }

            if(field.isAnnotationPresent(ie.nuig.i3market.semantic.engine.common.annotations.Resource.class)&& field.isAnnotationPresent(ie.nuig.i3market.semantic.engine.common.annotations.Category.class)) {
                 // convert to lowercase whatever the value is
                return model.createResource(Vocabulary.RESOURCE_URI+s.toLowerCase());
            }

            return model.createTypedLiteral(s, dtUri.toString());
        }
        if (Set.class.isAssignableFrom(value.getClass()) || List.class.isAssignableFrom(value.getClass())) {
            // RDF Container
            String type = Set.class.isAssignableFrom(value.getClass()) ? Vocabulary.RDFContainer.Bag : Vocabulary.RDFContainer.Seq;
            BlankNodeId collection = BlankNodeId.create();
            RDFNode collectionRDFNode= model.createResource(collection.toString());

            model.add(collectionRDFNode.asResource(), org.apache.jena.vocabulary.RDF.type,type);
            int i = 0;
            for (Object o : (Collection) value) {
                model.add(collectionRDFNode.asResource(), org.apache.jena.vocabulary.RDF.li(i),marshalObject(o, field));
                i++;
            }
            return  collectionRDFNode;
        }

        return null;
    }

    private RDFNode extractNodeFromField(Object object, Field field) throws ClassNotFoundException {
        Object value = CommonUtil.getFieldValue(object, field);
        return marshalObject(value, field);
    }

    private Property createProperty(Field field) {
        return model.createProperty(field.getAnnotation(RDF.class).value());
    }

    private <T> Resource getCustomId(T object, String providerId, Property predicate) throws BindingException {
        Field [] fields= object.getClass().getDeclaredFields();
        Resource rs =null;
        Field typeField= CommonUtil.getAnnotatedField(fields, Type.class);
        Type typeOfSubject = typeField.getAnnotation(Type.class);
        Object id = CommonUtil.getFieldValue(object, typeField);

        Field annotatedField= CommonUtil.getAnnotatedField(fields, RDFSubject.class);
        RDFSubject rdfSubject = annotatedField.getAnnotation(RDFSubject.class);
        //rs = model.createResource(rdfSubject.prefix() +providerId+"_"+ CommonUtil.getFieldDeclaringClassName_LowerCase(typeField)+id);
        rs = model.createResource(rdfSubject.prefix() +id);

        return rs;
    }

    private Resource createCustomId(Field [] fields, String providerId, Property predicate) throws ClassNotFoundException {
        Resource rs =null;


        Field typeField= CommonUtil.getAnnotatedField(fields, Type.class);
        Type typeOfSubject = typeField.getAnnotation(Type.class);

        Field annotatedField= CommonUtil.getAnnotatedField(fields, RDFSubject.class);
        RDFSubject rdfSubject = annotatedField.getAnnotation(RDFSubject.class);

        String id= SPARQLUtil.getMaxId(SPARQLUtil.getLocalName(providerId), typeOfSubject.typeOf());

//        System.out.println(id);

        rs = model.createResource(rdfSubject.prefix() +providerId+"_"+ CommonUtil.getFieldDeclaringClassName_LowerCase(typeField)+id)
               .addProperty(model.createProperty(Vocabulary.CORE.Properties.providerId), providerId)
                .addProperty(model.createProperty(Vocabulary.CORE.Properties.id),model.createTypedLiteral(Long.valueOf(id)));

        if(predicate!=null)
        model.add(rs, predicate, providerId+"_"+ CommonUtil.getFieldDeclaringClassName_LowerCase(typeField)+id);

        dataOfferingId = providerId+"_"+ "dataoffering"+id; //  Extract data offering

        return  rs;
    }


    private static Resource getDatatypeURI(Object o) {
        Class <?> cls = o.getClass();
        if (String.class.isAssignableFrom(cls)) {
            //System.out.println("string type in class rdfbinding getDataTypeURI" + XSDDatatype.XSDstring);
            return XSD.xstring;
        } else if (Boolean.class.isAssignableFrom(cls)) {
            return XSD.xboolean;
        } else if (Byte.class.isAssignableFrom(cls)) {
            return XSD.xbyte;
        } else if (Long.class.isAssignableFrom(cls)) {
            return XSD.xlong;
        } else if (Short.class.isAssignableFrom(cls)) {
            return XSD.xshort;
        } else if (Integer.class.isAssignableFrom(cls)) {
            return XSD.xint;
        } else if (Float.class.isAssignableFrom(cls)) {
            return XSD.xfloat;
        } else if (Double.class.isAssignableFrom(cls)) {
            return XSD.xdouble;
        } else if (Date.class.isAssignableFrom(cls)) {
            return XSD.dateTime;
        } else if (java.net.URI.class.isAssignableFrom(cls)) {
            return XSD.anyURI;
        }
        return null;
    }

    private static Object getDatatypeValue(Literal l) {
        Literal dt = l;
        String s = dt.getValue().toString();
        if (dt.equals(XSD.xstring)) {
            return s;
        } else if (dt.equals(XSD.xboolean)) {
            return Boolean.valueOf(s);
        } else if (dt.equals(XSD.xint)) {
            return Integer.valueOf(s);
        } else if (dt.equals(XSD.xbyte)) {
            return Byte.valueOf(s);
        } else if (dt.equals(XSD.xlong)) {
            return Long.valueOf(s);
        } else if (dt.equals(XSD.xlong)) {
            return Short.valueOf(s);
        } else if (dt.equals(XSD.xfloat)) {
            return Float.valueOf(s);
        } else if (dt.equals(XSD.xdouble)) {
            return Double.valueOf(s);
        } else if (dt.equals(XSD.dateTime)) {
            return DateUtils.getISO8601DateFormat().parse(s);
        } else if (dt.equals(XSD.anyURI)) {
            return java.net.URI.create(s);
        }
        return s;
    }

}
