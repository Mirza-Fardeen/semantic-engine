package ie.nuig.i3market.semantic.engine.domain.optimise;

import ie.nuig.i3market.semantic.engine.common.Vocabulary;
import ie.nuig.i3market.semantic.engine.common.annotations.*;
import ie.nuig.i3market.semantic.engine.domain.Dataset;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.util.List;

public class OfferingDataset {

    @Type(typeOf = Vocabulary.CORE.Classes.DataOffering)
    @RDFSubject(prefix = Vocabulary.RESOURCE_URI)
    @RDF(Vocabulary.CORE.Properties.dataOfferingId)
//    @ApiModelProperty(hidden = true)
    @Schema(hidden = true)
    @ID
    private String dataOfferingId;

    @RDF(Vocabulary.CORE.Properties.provider)
    @Provider
    @Resource
    @NotNull
    private String provider;


    @Resource
    @Category
    @RDF(Vocabulary.CORE.Properties.category)
    private String category;


    @NotNull
    @RDF(Vocabulary.CORE.Properties.hasDataset)
    private List<Dataset> hasDataset;

    public String getDataOfferingId() {
        return dataOfferingId;
    }

    public void setDataOfferingId(String dataOfferingId) {
        this.dataOfferingId = dataOfferingId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Dataset> getHasDataset() {
        return hasDataset;
    }

    public void setHasDataset(List<Dataset> hasDataset) {
        this.hasDataset = hasDataset;
    }
}
