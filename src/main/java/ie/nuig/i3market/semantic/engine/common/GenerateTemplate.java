package ie.nuig.i3market.semantic.engine.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */

public class GenerateTemplate<T> {

    public static String getTemplate(Object clazz) throws ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(generateTemplate(clazz));
    }

    /**
     *  generate template based on the provided class
     * @param parentObject
     * @return returns the object of provided class to the getTemplate call
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     */
    private static Object generateTemplate(Object parentObject) throws IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchFieldException {

        Field[] declaredFields = parentObject.getClass().getDeclaredFields();

        for (Field parentClassfield : declaredFields) {

            if (!CommonUtil.isCollection(parentClassfield))
            initializeFieldBasedOnType(parentObject, parentClassfield);

            if (CommonUtil.isCollection(parentClassfield)) {
                //get the class of that list field
                Object object= CommonUtil.getGenericTypeInstanceOfListField(parentClassfield);

                List<Object> lst = new ArrayList<>();
                lst.add(object);

                parentClassfield.setAccessible(true);
                parentClassfield.set(parentObject,lst);
                generateTemplate(object);
            }

        }
        return parentObject;
    }

    /**
     * initialize a field with its own type
     * @param clazz
     * @param field
     * @throws IllegalAccessException
     */
    public static void initializeFieldBasedOnType( Object clazz, Field field) throws IllegalAccessException {

        Class<?> dt = field.getType();
        field.setAccessible(true);

        // we dont have to include Ids in template, these are basically generated internally by system and user doesn't have to provide,
        // therefore we skip those fields which are annotated with "Type", which are also used as Id other than model Classes.

        //if(field.isAnnotationPresent(Type.class))
            //return;

        if (dt.equals(String.class)) {
            field.set(clazz,field.getType().getSimpleName());
        } else if (dt.equals(Boolean.class)) {
            field.set(clazz,field.getType());
        } else if (dt.equals(Integer.class)) {
            field.set(clazz,field.getType());
        } else if (dt.equals(Byte.class)) {
            field.set(clazz,field.getType());
        } else if (dt.equals(Long.class)) {
            field.set(clazz,field.getType());
        } else if (dt.equals(Float.class)) {
            field.set(clazz,field.getType());
        } else if (dt.equals(Double.class)) {
            field.set(clazz,field.getType());
        } else if (dt.equals(Date.class)) {
            //DateUtils.getISO8601DateFormat().parse(s);
            field.set(clazz, new Date());
        } else if (dt.equals(URI.class)) {
            field.set(clazz,field.getType());
        }else if (dt.equals(URL.class)) {
            field.set(clazz,field.getType());
        }

    }


}
