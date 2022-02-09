package ie.nuig.i3market.semantic.engine.dto;

import ie.nuig.i3market.semantic.engine.domain.Dataset;
import ie.nuig.i3market.semantic.engine.domain.PricingModel;
import ie.nuig.i3market.semantic.engine.domain.contracts.ContractParameters;

import java.util.List;

public class ExtDataOfferingDto extends DataOfferingDto {

    private String dataOfferingId;

    private int version;

    private String dataOfferingCreated;

    private String lastModified;

    public String getDataOfferingId() {
        return dataOfferingId;
    }

    public void setDataOfferingId(String dataOfferingId) {
        this.dataOfferingId = dataOfferingId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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

    public ExtDataOfferingDto() {

    }

    public ExtDataOfferingDto(String dataOfferingTitle, String dataOfferingDescription, String status, String category, String provider, String owner, String dataOfferingExpirationTime, String marketId, List<ContractParameters> contractParameters, List<PricingModel> hasPricingModel, List<Dataset> hasDataset, String dataOfferingId, int version, String dataOfferingCreated, String lastModified) {
        super(dataOfferingTitle, dataOfferingDescription, status, category, provider, owner, dataOfferingExpirationTime, marketId, contractParameters, hasPricingModel, hasDataset);
        this.dataOfferingId = dataOfferingId;
        this.version = version;
        this.dataOfferingCreated = dataOfferingCreated;
        this.lastModified = lastModified;
    }
}
