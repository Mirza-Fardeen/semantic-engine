package ie.nuig.i3market.semantic.engine.domain;

import ie.nuig.i3market.semantic.engine.common.Vocabulary;
import ie.nuig.i3market.semantic.engine.common.annotations.RDF;
import ie.nuig.i3market.semantic.engine.common.annotations.RDFSubject;
import ie.nuig.i3market.semantic.engine.common.annotations.Type;
import ie.nuig.i3market.semantic.engine.domain.dataset.DatasetInformation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */

public class Dataset {

    @Type(typeOf = Vocabulary.DCAT.Classes.Dataset)
    @RDFSubject(prefix = Vocabulary.RESOURCE_URI)
    @RDF(Vocabulary.CORE.Properties.datasetId)
//    @ApiModelProperty(hidden = true)
    //@JsonIgnore
    @Schema(hidden = true)
    private String datasetId;

    @RDF(Vocabulary.DCTERMS.Properties.title)
    private String title;

    @RDF(Vocabulary.DCTERMS.Properties.keyword)
    private String keyword;

    @RDF(Vocabulary.DCTERMS.Properties.dataset)
    private String dataset;

    @RDF(Vocabulary.DCTERMS.Properties.description)
    private String description;

    @RDF(Vocabulary.DCTERMS.Properties.issued)
    private String issued;

    @RDF(Vocabulary.DCTERMS.Properties.modified)
    private String modified;

    @RDF(Vocabulary.DCTERMS.Properties.temporal)
    private String temporal;

    @RDF(Vocabulary.DCTERMS.Properties.language)
    private String language;

    @RDF(Vocabulary.DCTERMS.Properties.spatial)
    private String spatial;

    @RDF(Vocabulary.DCTERMS.Properties.accrualPeriodicity)
    private String accrualPeriodicity;

    @RDF(Vocabulary.DCAT.Properties.temporalResolution)
    private String temporalResolution;

    @RDF(Vocabulary.DCAT.Properties.theme)
    private List<String> theme;


    @RDF(Vocabulary.DCAT.Properties.distribution)
    private List<Distribution> distribution;


    @RDF(Vocabulary.CORE.Properties.datasetInformation)
    private List<DatasetInformation> datasetInformation;


    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public List<String> getTheme() {
        return theme;
    }

    public void setTheme(List<String> theme) {
        this.theme = theme;
    }

    public String getTemporal() {
        return temporal;
    }

    public void setTemporal(String temporal) {
        this.temporal = temporal;
    }

    public String getLanguage() {
        return language;
    }

    public String getSpatial() {
        return spatial;
    }

    public void setSpatial(String spatial) {
        this.spatial = spatial;
    }

    public String getAccrualPeriodicity() {
        return accrualPeriodicity;
    }

    public void setAccrualPeriodicity(String accrualPeriodicity) {
        this.accrualPeriodicity = accrualPeriodicity;
    }

    public String getTemporalResolution() {
        return temporalResolution;
    }

    public void setTemporalResolution(String temporalResolution) {
        this.temporalResolution = temporalResolution;
    }

    public List<Distribution> getDistribution() {
        return distribution;
    }

    public void setDistribution(List<Distribution> distribution) {
        this.distribution = distribution;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<DatasetInformation> getDatasetInformation() {
        return datasetInformation;
    }

    public void setDatasetInformation(List<DatasetInformation> datasetInformation) {
        this.datasetInformation = datasetInformation;
    }

    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }
}
