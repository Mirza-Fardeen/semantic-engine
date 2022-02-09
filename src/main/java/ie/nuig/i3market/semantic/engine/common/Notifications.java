package ie.nuig.i3market.semantic.engine.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */
public class Notifications {

    static final Logger logger =  LoggerFactory.getLogger(Notifications.class);
    public static void postData(Object object, String url) {

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(object);

            // did manual creation of JSON, because the target server does'nt accept JSONOBJECT
            // also does not accept the escaped json
            // does not either Entity class
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{\n");
            stringBuilder.append("\"receiver_id\""+":"+"\""+UUID.randomUUID()+"\"\n");
            stringBuilder.append(",");
            stringBuilder.append("\n");
            stringBuilder.append("\"message\""+":"+json);
            stringBuilder.append("\n}");

            //logger.info("json {}",stringBuilder);
            //HttpEntity <?> entity = new HttpEntity<>(new NotificationModel(UUID.randomUUID().toString(), json),headers);
            HttpEntity <?> entity = new HttpEntity<>(stringBuilder.toString(),headers);

            String notifications = restTemplate.postForObject(url, entity, String.class);
            logger.info("response from notification server located at {} is= {}", url, notifications);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
