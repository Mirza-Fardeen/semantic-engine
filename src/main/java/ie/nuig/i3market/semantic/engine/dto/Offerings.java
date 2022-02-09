package ie.nuig.i3market.semantic.engine.dto;

import java.util.List;

public class Offerings {
    private long totalOffering;

    private List<ExtDataOfferingDto> result;

    public Offerings(long totalOffering, List<ExtDataOfferingDto> result) {
        this.totalOffering = totalOffering;
        this.result = result;
    }

    public Offerings() {

    }

    public long getTotalOffering() {
        return totalOffering;
    }

    public void setTotalOffering(long totalOffering) {
        this.totalOffering = totalOffering;
    }

    public List<ExtDataOfferingDto> getResult() {
        return result;
    }

    public void setResult(List<ExtDataOfferingDto> result) {
        this.result = result;
    }

}
