package ie.nuig.i3market.semantic.engine.common;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.nuig.i3market.semantic.engine.config.databases.VirtuosoConfiguration;
import ie.nuig.i3market.semantic.engine.domain.DataOffering;
import ie.nuig.i3market.semantic.engine.domain.distributed.EnginesDetails;
import ie.nuig.i3market.semantic.engine.domain.entities.lists.CategoriesList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */
public class DistributedStorage {

    static final Logger logger = LoggerFactory.getLogger(DistributedStorage.class);
    /**
     * get data from distributed storage
     * @param url
     * @return
     * @throws IOException
     */
    public static List<EnginesDetails> parse(String url) throws IOException {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper objectMapper = new ObjectMapper();
            Object remoteObject = restTemplate.getForObject(url, Object.class);

            return objectMapper.convertValue(remoteObject, new TypeReference<>() {
            });
        }catch (Exception e){
            return null;
        }

    }

    public static void postData(Object object, String url, VirtuosoConfiguration virtuosoConfiguration) throws JsonProcessingException {

       try {
           RestTemplate restTemplate = new RestTemplate();
           HttpHeaders headers = new HttpHeaders();
           headers.setContentType(MediaType.APPLICATION_JSON);

           List<String> data_categories = new ArrayList<>();
//           data_categories.add("Manufacturing");
           if (object instanceof DataOffering)
               data_categories.add(((DataOffering) object).getCategory());

           String endpoint_location = virtuosoConfiguration.getScheme_authority()+InetAddress.getLocalHost().getHostAddress()
                   +":" +virtuosoConfiguration.getEndpointPort()+virtuosoConfiguration.getQueryInterface();

           HttpEntity<?> entity = new HttpEntity<>(new EnginesDetails(data_categories,"testing endpoint description",endpoint_location), headers);

           EnginesDetails personResultAsJsonStr = restTemplate.postForObject(url, entity, EnginesDetails.class);

       }catch (Exception e) {
           e.printStackTrace();
       }
    }

    public static List<CategoriesList> getCategories(String url) {

        try {
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper objectMapper = new ObjectMapper();
            Object remoteObject = restTemplate.getForObject(url, Object.class);
           // ResponseEntity<CategoriesList[]> entity = restTemplate.getForEntity(url, CategoriesList[].class);

            Object rm = objectMapper.convertValue(remoteObject, new TypeReference<>() {});
            List<CategoriesList> categoriesLists  = objectMapper.convertValue(remoteObject, new TypeReference<>() {});
            return categoriesLists;

        }catch (Exception e){
            return null;
        }
    }
}
