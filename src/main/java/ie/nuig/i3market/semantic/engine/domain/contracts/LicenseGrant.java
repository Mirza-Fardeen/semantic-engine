package ie.nuig.i3market.semantic.engine.domain.contracts;

import ie.nuig.i3market.semantic.engine.common.Vocabulary;
import ie.nuig.i3market.semantic.engine.common.annotations.RDF;
import ie.nuig.i3market.semantic.engine.common.annotations.RDFSubject;
import ie.nuig.i3market.semantic.engine.common.annotations.Type;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 16/Sep/2021
 * Autho: Chi-Hung Le
 * Contact: chihung.le@nuigalway.ie
 */

public class LicenseGrant {

    @Type(typeOf = Vocabulary.CORE.Classes.LicenseGrant)
    @RDFSubject(prefix = Vocabulary.RESOURCE_URI)
    @RDF(Vocabulary.CORE.Properties.licenseGrantId)
//    @ApiModelProperty(hidden = true)
//    @JsonIgnore
    @Schema(hidden = true)
    private String licenseGrantId;

    @RDF(Vocabulary.CORE.Properties.copyData)
    private String copyData;

    @RDF(Vocabulary.CORE.Properties.transferable)
    private String transferable;

    @RDF(Vocabulary.CORE.Properties.exclusiveness)
    private String exclusiveness;

    @RDF(Vocabulary.CORE.Properties.revocable)
    private String revocable;

    public String getLicenseGrantId() {
        return licenseGrantId;
    }

    public void setLicenseGrantId(String licenseGrantId) {
        this.licenseGrantId = licenseGrantId;
    }

    @Schema(example = "true OR false", required = true, allowableValues = {"true", "false"}, type = "String")
    public String getCopyData() {
        return copyData;
    }

    public void setCopyData(String copyData) {
        this.copyData = copyData;
    }

    @Schema(example = "true OR false", required = true, allowableValues = {"true", "false"}, type = "String")
    public String getTransferable() {
        return transferable;
    }

    public void setTransferable(String transferable) {
        this.transferable = transferable;
    }

    @Schema(example = "true OR false", required = true, allowableValues = {"true", "false"}, type = "String")
    public String getExclusiveness() {
        return exclusiveness;
    }

    public void setExclusiveness(String exclusiveness) {
        this.exclusiveness = exclusiveness;
    }

    @Schema(example = "true OR false", required = true, allowableValues = {"true", "false"}, type = "String")
    public String getRevocable() {
        return revocable;
    }

    public void setRevocable(String revocable) {
        this.revocable = revocable;
    }
}
