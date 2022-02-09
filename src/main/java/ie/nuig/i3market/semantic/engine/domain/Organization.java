package ie.nuig.i3market.semantic.engine.domain;

import ie.nuig.i3market.semantic.engine.common.Vocabulary;
import ie.nuig.i3market.semantic.engine.common.annotations.ID;
import ie.nuig.i3market.semantic.engine.common.annotations.RDF;
import ie.nuig.i3market.semantic.engine.common.annotations.RDFSubject;
import ie.nuig.i3market.semantic.engine.common.annotations.Type;

import java.util.Objects;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */

public class Organization {

    @Type (typeOf = Vocabulary.SCHEMA.Classes.Organization)
    @RDFSubject(prefix = Vocabulary.RESOURCE_URI)
    @RDF(Vocabulary.CORE.Properties.organizationId)
    @ID
    private String organizationId;

    @RDF(Vocabulary.SCHEMA.Properties.name)
    private String name;

    @RDF(Vocabulary.DCTERMS.Properties.description)
    private String description;

    @RDF(Vocabulary.SCHEMA.Properties.address)
    private String address;

    @RDF(Vocabulary.SCHEMA.Properties.contactPoint)
    private String contactPoint;

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactPoint() {
        return contactPoint;
    }

    public void setContactPoint(String contactPoint) {
        this.contactPoint = contactPoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return organizationId.equals(that.organizationId) && name.equals(that.name) && description.equals(that.description) && address.equals(that.address) && contactPoint.equals(that.contactPoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizationId, name, description, address, contactPoint);
    }
}
