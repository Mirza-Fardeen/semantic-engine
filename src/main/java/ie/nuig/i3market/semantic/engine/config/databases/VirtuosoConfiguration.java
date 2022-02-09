package ie.nuig.i3market.semantic.engine.config.databases;


import ie.nuig.i3market.semantic.engine.config.ApplicationProperties;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */

@Configuration
public class VirtuosoConfiguration {


    private final ApplicationProperties applicationProperties;

    @Autowired
    public VirtuosoConfiguration(ApplicationProperties applicationProperties1) {
        this.applicationProperties = applicationProperties1;
    }


    public ApplicationProperties getApplicationProperties() {
        return applicationProperties;
    }



    public HttpContext getVirtuosoContext() {

        HttpContext httpContext=null;
        try {
            httpContext = new BasicHttpContext();
            CredentialsProvider provider = new BasicCredentialsProvider();
            provider.setCredentials(new AuthScope(AuthScope.ANY_HOST,
                    AuthScope.ANY_PORT), new UsernamePasswordCredentials(applicationProperties.getVirtuosoProperties().getUsername(), applicationProperties.getVirtuosoProperties().getPassword()));
            httpContext.setAttribute(HttpClientContext.CREDS_PROVIDER, provider);
        }catch(Exception e){
            e.printStackTrace();
        }
        return httpContext;

    }

    public String getGraph() {
        return applicationProperties.getVirtuosoProperties().getGraph();
    }

    public String getCategoriesGraph() {
        return applicationProperties.getVirtuosoProperties().getCategories_graph();
    }

    public String getQueryInterface() {
        return applicationProperties.getVirtuosoProperties().getQuery_interface();
    }

    public String getEndpointUrl() {
        return applicationProperties.getVirtuosoProperties().getUrl();
    }

    public String getUpdateInterface() {
        return applicationProperties.getVirtuosoProperties().getUpdate_interface();
    }

    public String getEndpointPort() {
        return applicationProperties.getVirtuosoProperties().getEndpoint_port();
    }
    public String getScheme_authority() {
        return applicationProperties.getVirtuosoProperties().getScheme_authority();
    }
    public String getPassword() {
        return applicationProperties.getVirtuosoProperties().getPassword();
    }

    public String getGraphUrl() {
        return applicationProperties.getVirtuosoProperties().getUsername();
    }


    public List <String> whenSelectQuery() {

        List<String> endp= new ArrayList<>();
        String endpoints [] = applicationProperties.getVirtuosoProperties().getUrl().split(",");

        for (String endpoint: endpoints)
        {
            endp.add(endpoint.concat(applicationProperties.getVirtuosoProperties().getQuery_interface()));
        }
        return endp;
    }

    public String whenUpdateQuery() {
        return applicationProperties.getVirtuosoProperties().getUrl().concat(applicationProperties.getVirtuosoProperties().getUpdate_interface());
    }

}
