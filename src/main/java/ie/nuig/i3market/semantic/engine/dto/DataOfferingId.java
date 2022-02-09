package ie.nuig.i3market.semantic.engine.dto;

import java.io.Serializable;

public class DataOfferingId implements Serializable {

    private static final long serialVersionUID = 42L;

    private String dataOfferingId;

    public DataOfferingId(String dataOfferingId) {
        this.dataOfferingId = dataOfferingId;
    }

    public String getDataOfferingId() {
        return dataOfferingId;
    }

    public void setDataOfferingId(String dataOfferingId) {
        this.dataOfferingId = dataOfferingId;
    }


}
