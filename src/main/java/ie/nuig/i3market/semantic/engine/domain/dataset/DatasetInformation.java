package ie.nuig.i3market.semantic.engine.domain.dataset;

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
public class DatasetInformation {

    @Type(typeOf = Vocabulary.CORE.Classes.DatasetInformation)
    @RDFSubject(prefix = Vocabulary.RESOURCE_URI)
    @RDF(Vocabulary.CORE.Properties.datasetInformationId)
//    @ApiModelProperty(hidden = true)
    //@JsonIgnore
    @Schema(hidden = true)
    private String datasetInformationId;

    @RDF(Vocabulary.CORE.Properties.measurementType)
    private String measurementType;

    @RDF(Vocabulary.CORE.Properties.measurementChannelType)
    private String measurementChannelType;

    @RDF(Vocabulary.CORE.Properties.sensorId)
    private String sensorId;

    @RDF(Vocabulary.CORE.Properties.deviceId)
    private String deviceId;

    @RDF(Vocabulary.CORE.Properties.cppType)
    private String cppType;

    @RDF(Vocabulary.CORE.Properties.sensorType)
    private String sensorType;


    public String getDatasetInformationId() {
        return datasetInformationId;
    }

    public void setDatasetInformationId(String datasetInformationId) {
        this.datasetInformationId = datasetInformationId;
    }

    public String getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
    }

    public String getMeasurementChannelType() {
        return measurementChannelType;
    }

    public void setMeasurementChannelType(String measurementChannelType) {
        this.measurementChannelType = measurementChannelType;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCppType() {
        return cppType;
    }

    public void setCppType(String cppType) {
        this.cppType = cppType;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }
}
