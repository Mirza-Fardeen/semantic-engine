package ie.nuig.i3market.semantic.engine.domain.distributed;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnginesDetails {

    private String location;
    private List<String> data_categories;
    private String registered_by;
    private String description;
    private String uuid;

    public EnginesDetails(){}

    public EnginesDetails(String location, List<String> data_categories){
        this.location = location;
        this.data_categories = data_categories;
    }

    public EnginesDetails (List<String> data_categories, String description, String location ){
        this.data_categories = data_categories;
        this.description =description;
        this.location = location;
    }

    public EnginesDetails(String location, List<String> data_categories, String registered_by, String description, String uuid) {
        this.location = location;
        this.data_categories = data_categories;
        this.registered_by = registered_by;
        this.description = description;
        this.uuid = uuid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRegistered_by() {
        return registered_by;
    }

    public void setRegistered_by(String registered_by) {
        this.registered_by = registered_by;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<String> getData_categories() {
        return data_categories;
    }

    public void setData_categories(List<String> data_categories) {
        this.data_categories = data_categories;
    }

    @Override
    public String toString() {
        return "EnginesDetails{" +
                "location='" + location + '\'' +
                ", data_categories=" + data_categories +
                ", registered_by='" + registered_by + '\'' +
                ", description='" + description + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }

}