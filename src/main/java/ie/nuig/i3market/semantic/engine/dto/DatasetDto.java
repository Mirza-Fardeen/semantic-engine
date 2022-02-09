package ie.nuig.i3market.semantic.engine.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DatasetDto {
    private String datasetId;
    private String dataset;
    private String description;
    private String title;
    private String creator;
    private String issued;
    private String modified;
    private List<String> theme;
    private String temporal;
    private String language;
    private String publisher;


    public DatasetDto withDatasetId (String datasetId){
        this.datasetId=datasetId;
        return this;
    }

    public DatasetDto with (String publisher){
        this.publisher=publisher;
        return this;
    }

    public DatasetDto withLanguage (String language){
        this.language=language;
        return this;
    }

    public DatasetDto withTemporal (String temporal){
        this.temporal=temporal;
        return this;
    }

    public DatasetDto withTheme (List<String> theme){
        this.theme=theme;
        return this;
    }

    public DatasetDto withModified (String modified){
        this.modified=modified;
        return this;
    }

    public DatasetDto withIssued (String issued){
        this.issued=issued;
        return this;
    }

    public DatasetDto withCreator (String creator){
        this.creator=creator;
        return this;
    }

    public DatasetDto withTitle (String title){
        this.title=title;
        return this;
    }

    public DatasetDto withDescription (String description){
        this.description=description;
        return this;
    }

    public DatasetDto withDataset(String dataset){
        this.dataset=dataset;
        return this;
    }

    public String getDataset() {
        return dataset;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getCreator() {
        return creator;
    }

    public String getIssued() {
        return issued;
    }

    public String getModified() {
        return modified;
    }

    public List<String> getTheme() {
        return theme;
    }

    public String getTemporal() {
        return temporal;
    }

    public String getLanguage() {
        return language;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getDatasetId() {
        return datasetId;
    }
}
