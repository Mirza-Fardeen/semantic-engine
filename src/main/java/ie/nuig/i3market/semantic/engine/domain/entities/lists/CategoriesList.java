package ie.nuig.i3market.semantic.engine.domain.entities.lists;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoriesList {
  String name;
  String description;

    public CategoriesList() {
    }

    public CategoriesList(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CategoriesList{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
