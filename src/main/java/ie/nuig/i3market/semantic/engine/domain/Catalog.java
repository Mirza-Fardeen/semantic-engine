package ie.nuig.i3market.semantic.engine.domain;

import ie.nuig.i3market.semantic.engine.common.Vocabulary;
import ie.nuig.i3market.semantic.engine.common.annotations.RDF;
import ie.nuig.i3market.semantic.engine.common.annotations.Resource;

import java.util.List;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */

public class Catalog {

    @RDF(Vocabulary.CORE.Properties.providerId)
    @Resource
    private String providerId;

    @RDF(Vocabulary.DCTERMS.Properties.title)
    private String title;

    @RDF(Vocabulary.RDF.label)
    private String label;

    @RDF(Vocabulary.DCTERMS.Properties.description)
    private String description;

    @RDF(Vocabulary.DCTERMS.Properties.license)
    private String license;

    @RDF(Vocabulary.CORE.Properties.provider)
    private String provider;

    @RDF(Vocabulary.CORE.Properties.owner)
    private String isOwnedBy;

    @RDF(Vocabulary.DCTERMS.Properties.language)
    private String language;


    @RDF(Vocabulary.DCAT.Properties.dataset)
    private List<Dataset> dataset;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Dataset> getDataset() {
        return dataset;
    }

    public void setDataset(List<Dataset> dataset) {
        this.dataset = dataset;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getIsProvideBy() {
        return provider;
    }

    public void setIsProvideBy(String provider) {
        this.provider = provider;
    }

    public String getIsOwnedBy() {
        return isOwnedBy;
    }

    public void setIsOwnedBy(String isOwnedBy) {
        this.isOwnedBy = isOwnedBy;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    @Override
    public String toString() {
        return "Catalog{" +
            "providerId='" + providerId + '\'' +
            ", title='" + title + '\'' +
            ", label='" + label + '\'' +
            ", description='" + description + '\'' +
            ", license='" + license + '\'' +
            ", isProvideBy='" + provider + '\'' +
            ", isOwnedBy='" + isOwnedBy + '\'' +
            ", language='" + language + '\'' +
            ", dataset=" + dataset +
            '}';
    }
}
