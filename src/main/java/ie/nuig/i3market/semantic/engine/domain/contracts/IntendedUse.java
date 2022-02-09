package ie.nuig.i3market.semantic.engine.domain.contracts;

import ie.nuig.i3market.semantic.engine.common.Vocabulary;
import ie.nuig.i3market.semantic.engine.common.annotations.RDF;
import ie.nuig.i3market.semantic.engine.common.annotations.RDFSubject;
import ie.nuig.i3market.semantic.engine.common.annotations.Type;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */

public class IntendedUse {
    @Type(typeOf = Vocabulary.CORE.Classes.IntendedUse)
    @RDFSubject(prefix = Vocabulary.RESOURCE_URI)
    @RDF(Vocabulary.CORE.Properties.intendedUseId)
//    @ApiModelProperty(hidden = true)
//    @JsonIgnore
    @Schema(hidden = true)
    private String intendedUseId;

    @RDF(Vocabulary.CORE.Properties.processData)
    private String processData;

    @RDF(Vocabulary.CORE.Properties.shareDataWithThirdParty)
    public String shareDataWithThirdParty;

    @RDF(Vocabulary.CORE.Properties.editData)
    public String editData;


    public String getIntendedUseId() {
        return intendedUseId;
    }

    public void setIntendedUseId(String intendedUseId) {
        this.intendedUseId = intendedUseId;
    }

    @Schema(example = "true OR false", required = true, allowableValues = {"true", "false"}, type = "String")
    public String getProcessData() {
        return processData;
    }

    public void setProcessData(String processData) {
        this.processData = processData;
    }

    @Schema(example = "true OR false", required = true, allowableValues = {"true", "false"}, type = "String")
    public String getShareDataWithThirdParty() {
        return shareDataWithThirdParty;
    }

    public void setShareDataWithThirdParty(String shareDataWithThirdParty) {
        this.shareDataWithThirdParty = shareDataWithThirdParty;
    }

    @Schema(example = "true OR false", required = true, allowableValues = {"true", "false"}, type = "String")
    public String getEditData() {
        return editData;
    }

    public void setEditData(String editData) {
        this.editData = editData;
    }
}
