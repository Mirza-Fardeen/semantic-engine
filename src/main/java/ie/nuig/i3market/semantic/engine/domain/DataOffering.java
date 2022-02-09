package ie.nuig.i3market.semantic.engine.domain;


import ie.nuig.i3market.semantic.engine.common.Vocabulary;
import ie.nuig.i3market.semantic.engine.common.annotations.*;
import ie.nuig.i3market.semantic.engine.domain.contracts.ContractParameters;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */


// for some reason with RDF, extends will not work
//public class DataOffering extends Offering implements Serializable {
public class DataOffering implements Serializable {
    private static final long serialVersionUID = 1L;

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

    @RDF(Vocabulary.CORE.Properties.marketId)
    private String marketId;

    @RDF(Vocabulary.CORE.Properties.owner)
    @Owner
    @Resource
    @NotNull
    private String owner;

    @RDF(Vocabulary.DCTERMS.Properties.version)
    @Version
    @Resource
    private String version;

    @RDF(Vocabulary.DCTERMS.Properties.title)
    private String dataOfferingTitle;

    @RDF(Vocabulary.CORE.Properties.dataOfferingDescription)
    private String dataOfferingDescription;

    @Resource
    @Category
    @RDF(Vocabulary.CORE.Properties.category)
    private String category;

    @RDF(Vocabulary.CORE.Properties.status)
    private String status;

    @RDF(Vocabulary.CORE.Properties.dataOfferingExpirationTime)
    private String dataOfferingExpirationTime;

    @RDF(Vocabulary.CORE.Properties.dataOfferingCreated)
    private String dataOfferingCreated;

    @RDF(Vocabulary.CORE.Properties.lastModified)
    private String lastModified;

    @NotNull
    @RDF(Vocabulary.CORE.Properties.contractParameters)
    private List<ContractParameters> contractParameters;

    @NotNull
    @RDF(Vocabulary.CORE.Properties.hasPricingModel)
    private List<PricingModel> hasPricingModel;

    @NotNull
    @RDF(Vocabulary.CORE.Properties.hasDataset)
    private List<Dataset> hasDataset;


    public String getDataOfferingId() {
        return dataOfferingId;
    }

    public void setDataOfferingId(String dataOfferingId) {
        this.dataOfferingId = dataOfferingId;
    }

    public String getDataOfferingTitle() {
        return dataOfferingTitle;
    }

    public void setDataOfferingTitle(String dataOfferingTitle) {
        this.dataOfferingTitle = dataOfferingTitle;
    }


    public String getDataOfferingDescription() {
        return dataOfferingDescription;
    }

    public void setDataOfferingDescription(String dataOfferingDescription) {
        this.dataOfferingDescription = dataOfferingDescription;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProvider() {
        return provider;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDataOfferingExpirationTime() {
        return dataOfferingExpirationTime;
    }

    public void setDataOfferingExpirationTime(String dataOfferingExpirationTime) {
        this.dataOfferingExpirationTime = dataOfferingExpirationTime;
    }

    public String getDataOfferingCreated() {
        return dataOfferingCreated;
    }

    public void setDataOfferingCreated(String dataOfferingCreated) {
        this.dataOfferingCreated = dataOfferingCreated;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public List<ContractParameters> getContractParameters() {
        return contractParameters;
    }

    public void setContractParameters(List<ContractParameters> contractParameters) {
        this.contractParameters = contractParameters;
    }

    public List<PricingModel> getHasPricingModel() {
        return hasPricingModel;
    }

    public void setHasPricingModel(List<PricingModel> hasPricingModel) {
        this.hasPricingModel = hasPricingModel;
    }

    public List<Dataset> getHasDataset() {
        return hasDataset;
    }

    public void setHasDataset(List<Dataset> hasDataset) {
        this.hasDataset = hasDataset;
    }

    @Override
    public String toString() {
        return "DataOffering{" +
                "dataOfferingId='" + dataOfferingId + '\'' +
                ", provider='" + provider + '\'' +
                ", marketID='" + marketId + '\'' +
                ", owner='" + owner + '\'' +
                ", version='" + version + '\'' +
                ", dataOfferingTitle='" + dataOfferingTitle + '\'' +
                ", dataOfferingDescription='" + dataOfferingDescription + '\'' +
                ", category='" + category + '\'' +
                ", status='" + status + '\'' +
                ", dataOfferingExpirationTime='" + dataOfferingExpirationTime + '\'' +
                ", dataOfferingCreated='" + dataOfferingCreated + '\'' +
                ", lastModified='" + lastModified + '\'' +
                ", contractParameters=" + contractParameters +
                ", hasPricingModel=" + hasPricingModel +
                ", hasDataset=" + hasDataset +
                '}';
    }
}
