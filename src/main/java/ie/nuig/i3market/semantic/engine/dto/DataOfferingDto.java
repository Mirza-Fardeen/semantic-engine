package ie.nuig.i3market.semantic.engine.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import ie.nuig.i3market.semantic.engine.domain.Dataset;
import ie.nuig.i3market.semantic.engine.domain.PricingModel;
import ie.nuig.i3market.semantic.engine.domain.contracts.ContractParameters;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataOfferingDto {

    private String dataOfferingTitle;

    private String dataOfferingDescription;

    private String status;

    private String category;

    private String provider;

    private String marketId;

    private String owner;

    private String dataOfferingExpirationTime;

    private List<ContractParameters> contractParameters;

    private List<PricingModel> hasPricingModel;

    private List<Dataset> hasDataset;

    public DataOfferingDto() {

    }

    public DataOfferingDto(String dataOfferingTitle, String dataOfferingDescription,
                           String status, String category, String provider,
                           String owner, String dataOfferingExpirationTime,
                           String marketId,
                           List<ContractParameters> contractParameters,
                           List<PricingModel> hasPricingModel,
                           List<Dataset> hasDataset) {
        this.dataOfferingTitle = dataOfferingTitle;
        this.dataOfferingDescription = dataOfferingDescription;
        this.status = status;
        this.category = category;
        this.provider = provider;
        this.marketId = marketId;
        this.owner = owner;
        this.dataOfferingExpirationTime = dataOfferingExpirationTime;
        this.contractParameters = contractParameters;
        this.hasPricingModel = hasPricingModel;
        this.hasDataset = hasDataset;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
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
}
