package ie.nuig.i3market.semantic.engine.common;

import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */

public class CommonUtil {


    static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);


    public static Object getFieldValue(Object object, Field annotatedField) {

        Class<?> clazz = object.getClass();
        String fieldName = annotatedField.getName();
        PropertyDescriptor pd;

        try {
            pd = new PropertyDescriptor(fieldName, clazz);
            Method method = pd.getReadMethod();
            Object value = method.invoke(object);
            // temporarily checking "string" have to remove in future
            if (value != null && !value.equals("string"))
                return value;

        } catch (InvocationTargetException| IllegalAccessException| IntrospectionException e) {
            e.printStackTrace();

        }
        return null;
    }

    public static Field getAnnotatedField(Field[] declaredFields, Class<? extends Annotation> annotationClass) {
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(annotationClass)) {
                return field;
            }
        }
        return null;
    }


    public static boolean isCollection(Field field) {
        return List.class.isAssignableFrom(field.getType()) || Set.class.isAssignableFrom(field.getType());
    }

    public static String getFieldClassName_LowerCase(Field field) throws ClassNotFoundException {
        //java.lang.reflect.Type claz = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        Type claz = field.getGenericType();
        String className = Class.forName(claz.getTypeName()).getSimpleName().toLowerCase();

        return className;
    }

    public static String getClassName_LowerCase(Class<?> clazz) throws ClassNotFoundException {

        String className = clazz.getSimpleName().toLowerCase();
        return className;
    }


    public static String getFieldDeclaringClassName_LowerCase(Field field) throws ClassNotFoundException {

        String declaringClassName = field.getDeclaringClass().getSimpleName().toLowerCase();

        return declaringClassName;
    }

    public static Class<?> getFieldDeclaringClass(Field field) throws ClassNotFoundException {

        Class<?> clazz = Class.forName(field.getDeclaringClass().getName());

        return clazz;
    }

    public static Object getGenericTypeInstanceOfListField(Field field) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Type claz = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        Class<?> clazz = Class.forName(claz.getTypeName());
        Object object = clazz.newInstance();

        if (object != null)
            return object;

        return null;
    }

    public static FileWriter fileWrite() {
        File file = null;
        FileWriter writer = null;
        try {
            file = new File("IDsGenerator.json");
            if (file.createNewFile()) {
                writer = new FileWriter(file.getName());
            } else {
                writer = new FileWriter(file.getName(), true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return writer;
    }

    public static JsonReader fileRead() {
        File file = null;
        JsonReader reader = null;
        try {
            file = new File("IDsGenerator.json");
            if (file.createNewFile()) {
                // add json string to file before reading it
                reader = new JsonReader(new FileReader(file.getName()));
            } else {
                reader = new JsonReader(new FileReader(file.getName()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return reader;
    }

//    public static String  getLocalHostAddress() throws UnknownHostException {
////        String ip = null;
//        String ip = "";
//        Process p;
//        try {
//            String command = "curl ifconfig.co";
//            p = Runtime.getRuntime().exec(command);
//            BufferedReader br = new BufferedReader(
//                    new InputStreamReader(p.getInputStream()));
//            String s;
//            while ((s = br.readLine()) != null)
//                ip=s;
//               logger.info("local host address is {} ", ip);
//            p.waitFor();
//               logger.info("exit: {} " , p.exitValue());
//            p.destroy();
//        } catch (Exception e) {
//            logger.error(" failed when call localHostAddress");
//            e.printStackTrace();
//        }
//        return ip;
//    }


    public static String findMyIpAddress() {
        try {
            logger.info("External host address is {} ", InetAddress.getLocalHost().getHostAddress());
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "unknown IP address";
        }
    }
}


