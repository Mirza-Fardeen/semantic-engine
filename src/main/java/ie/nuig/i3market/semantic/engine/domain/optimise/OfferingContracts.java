package ie.nuig.i3market.semantic.engine.domain.optimise;

import ie.nuig.i3market.semantic.engine.common.Vocabulary;
import ie.nuig.i3market.semantic.engine.common.annotations.*;
import ie.nuig.i3market.semantic.engine.domain.contracts.ContractParameters;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.util.List;

public class OfferingContracts {
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

    public OfferingContracts(String dataOfferingId, String provider, String category, List<ContractParameters> contractParameters) {
        this.dataOfferingId = dataOfferingId;
        this.provider = provider;
        this.category = category;
        this.contractParameters = contractParameters;
    }

    public OfferingContracts(){

    }

    @NotNull
    @RDF(Vocabulary.CORE.Properties.contractParameters)


    private List<ContractParameters> contractParameters;

    public List<ContractParameters> getContractParameters() {
        return contractParameters;
    }

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

    public void setContractParameters(List<ContractParameters> contractParameters) {
        this.contractParameters = contractParameters;
    }
}
