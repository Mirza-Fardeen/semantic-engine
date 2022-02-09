package ie.nuig.i3market.semantic.engine.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to I 3 Market.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */

@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    public ApplicationProperties() {
    }

    VirtuosoProperties virtuosoProperties= new VirtuosoProperties();
    DistributedStorageIntegration distributedStorageIntegration = new DistributedStorageIntegration();
    NotificationMangerIntegration notificationMangerIntegration = new NotificationMangerIntegration();

    public VirtuosoProperties getVirtuosoProperties() {
        return virtuosoProperties;
    }
    public DistributedStorageIntegration getDistributedStorageIntegration(){return distributedStorageIntegration;}
    public NotificationMangerIntegration getNotificationMangerIntegration(){return notificationMangerIntegration;}

    /**
     * virtuoso properties
     */

    public static  class VirtuosoProperties{
        private String url;
        private String query_interface;
        private String update_interface;
        private String username;
        private String password;
        private String graph;
        private String categories_graph;
        private String scheme_authority;
        private String endpoint_port;


        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getQuery_interface() {
            return query_interface;
        }

        public void setQuery_interface(String query_interface) {
            this.query_interface = query_interface;
        }

        public String getUpdate_interface() {
            return update_interface;
        }

        public void setUpdate_interface(String update_interface) {
            this.update_interface = update_interface;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getGraph() {
            return graph;
        }

        public void setGraph(String graph) {
            this.graph = graph;
        }

        public String getCategories_graph() {
            return categories_graph;
        }

        public void setCategories_graph(String categories_graph) {
            this.categories_graph = categories_graph;
        }

        public String getScheme_authority() {
            return scheme_authority;
        }

        public void setScheme_authority(String scheme_authority) {
            this.scheme_authority = scheme_authority;
        }

        public String getEndpoint_port() {
            return endpoint_port;
        }

        public void setEndpoint_port(String endpoint_port) {
            this.endpoint_port = endpoint_port;
        }
    }


    /**
     * this class properties will be used for integration with distributed storage
     * this class contains the APIs exposed via distributed storage http://95.211.3.244:7500/docs
     */
    public  static  class DistributedStorageIntegration{
        /* this will be used for fetching all the SPARQL endpoints distributed across */
        private String search_engines_GET;
        private String search_engines_POST;
        private String searchCategories_GET;


        public String getSearchCategories_GET() {  return searchCategories_GET;  }

        public void setSearchCategories_GET(String searchCategories_GET) { this.searchCategories_GET = searchCategories_GET; }

        public String getSearch_engines_POST() { return search_engines_POST;}

        public void setSearch_engines_POST(String search_engines_POST) {this.search_engines_POST = search_engines_POST;}

        public String getSearch_engines_GET() {
            return search_engines_GET;
        }

        public void setSearch_engines_GET(String search_engines_GET) {
            this.search_engines_GET = search_engines_GET;
        }
    }

    /**
     * this class represents the properties used to integrate with the notification manager
     */
    public static class NotificationMangerIntegration{
        private String notificationService_POST;

        public String getNotificationService_POST() {
            return notificationService_POST;
        }

        public void setNotificationService_POST(String notificationService_POST) {
            this.notificationService_POST = notificationService_POST;
        }
    }

}
