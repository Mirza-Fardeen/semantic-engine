package ie.nuig.i3market.semantic.engine.domain;

import ie.nuig.i3market.semantic.engine.common.Vocabulary;
import ie.nuig.i3market.semantic.engine.common.annotations.Provider;
import ie.nuig.i3market.semantic.engine.common.annotations.RDF;
import ie.nuig.i3market.semantic.engine.common.annotations.RDFSubject;
import ie.nuig.i3market.semantic.engine.common.annotations.Type;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */

public class DataProvider {

    @Type (typeOf = Vocabulary.CORE.Classes.Provider)
    @RDFSubject (prefix = Vocabulary.RESOURCE_URI)
    @RDF(Vocabulary.CORE.Properties.providerId)
    @Provider
    @NotNull
    private String providerId;

    @RDF(Vocabulary.SCHEMA.Properties.name)
    private String name;

    @RDF(Vocabulary.SCHEMA.Properties.description)
    private String description;

    @RDF(Vocabulary.SCHEMA.Properties.sourceOrganization)
    List<Organization> organization;

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
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
    

    public List<Organization> getOrganization() {
        return organization;
    }

    public void setOrganization(List<Organization> organization) {
        this.organization = organization;
    }

    @Override
    public String toString() {
        return "DataProvider{" +
                "providerId='" + providerId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", organization=" + organization +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataProvider that = (DataProvider) o;
        return providerId.equals(that.providerId) && name.equals(that.name) && description.equals(that.description) && organization.equals(that.organization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(providerId, name, description, organization);
    }
}
