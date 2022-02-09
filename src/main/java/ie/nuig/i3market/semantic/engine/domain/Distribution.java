package ie.nuig.i3market.semantic.engine.domain;

import ie.nuig.i3market.semantic.engine.common.Vocabulary;
import ie.nuig.i3market.semantic.engine.common.annotations.RDF;
import ie.nuig.i3market.semantic.engine.common.annotations.RDFSubject;
import ie.nuig.i3market.semantic.engine.common.annotations.Type;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */

public class Distribution {

    @Type(typeOf = Vocabulary.DCAT.Classes.Distribution)
    @RDFSubject(prefix = Vocabulary.RESOURCE_URI)
    @RDF(Vocabulary.CORE.Properties.distributionId)
//    @ApiModelProperty(hidden = true)
    //@JsonIgnore
    @Schema(hidden = true)
    private String distributionId;

    @RDF(Vocabulary.DCTERMS.Properties.title)
    private String title;

//    @RDF(Vocabulary.DCAT.Properties.distribution)
//    private String distribution;

    @RDF(Vocabulary.DCTERMS.Properties.description)
    private String description;

    @RDF(Vocabulary.DCTERMS.Properties.license)
    private String license;

    @RDF(Vocabulary.DCTERMS.Properties.accessRights)
    private String accessRights;

    @RDF(Vocabulary.DCTERMS.Properties.downloadType)
    private String downloadType;

    @RDF(Vocabulary.DCTERMS.Properties.conformsTo)
    private String conformsTo;

    @RDF(Vocabulary.DCAT.Properties.mediaType)
    private String mediaType;

    @RDF(Vocabulary.DCAT.Properties.packageFormat)
    private String packageFormat;

    @NotNull
    @RDF(Vocabulary.DCAT.Properties.accessService)
    private List<AccessService> accessService;


    public String getDistributionId() {
        return distributionId;
    }

    public void setDistributionId(String distributionId) {
        this.distributionId = distributionId;
    }

//    public String getDistribution() {
//        return distribution;
//    }
//
//    public void setDistribution(String distribution) {
//        this.distribution = distribution;
//    }

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

    public String getAccessRights() {
        return accessRights;
    }

    public void setAccessRights(String accessRights) {
        this.accessRights = accessRights;
    }

    public String getDownloadType() {
        return downloadType;
    }

    public void setDownloadType(String downloadType) {
        this.downloadType = downloadType;
    }

    public String getConformsTo() {
        return conformsTo;
    }

    public void setConformsTo(String conformsTo) {
        this.conformsTo = conformsTo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getPackageFormat() {
        return packageFormat;
    }

    public void setPackageFormat(String packageFormat) {
        this.packageFormat = packageFormat;
    }

    public List<AccessService> getAccessService() {
        return accessService;
    }

    public void setAccessService(List<AccessService> accessService) {
        this.accessService = accessService;
    }
}
