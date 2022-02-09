package ie.nuig.i3market.semantic.engine.config.databases;

import com.fasterxml.jackson.core.JsonProcessingException;
import ie.nuig.i3market.semantic.engine.common.DistributedStorage;
import ie.nuig.i3market.semantic.engine.config.ApplicationProperties;
import ie.nuig.i3market.semantic.engine.domain.distributed.EnginesDetails;
import ie.nuig.i3market.semantic.engine.domain.entities.lists.CategoriesList;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */
@Configuration
public class DistributedStorageConfiguration {

    private final ApplicationProperties applicationProperties;

    public DistributedStorageConfiguration(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;

    }

    public  List<EnginesDetails> listEnginesWithDetails () throws IOException {

        return DistributedStorage.parse(applicationProperties.getDistributedStorageIntegration().getSearch_engines_GET());

    }

    public void saveDataToDistributedStorage(Object object, VirtuosoConfiguration virtuosoConfiguration) throws JsonProcessingException {

    DistributedStorage.postData(object, applicationProperties.getDistributedStorageIntegration().getSearch_engines_POST(), virtuosoConfiguration);
    }

    public List<CategoriesList> listCategories(){
        return  DistributedStorage.getCategories(applicationProperties.getDistributedStorageIntegration().getSearchCategories_GET());
    }
}
