package ie.nuig.i3market.semantic.engine.domain;

import ie.nuig.i3market.semantic.engine.common.Vocabulary;
import ie.nuig.i3market.semantic.engine.common.annotations.RDF;
import ie.nuig.i3market.semantic.engine.common.annotations.RDFSubject;
import ie.nuig.i3market.semantic.engine.common.annotations.Type;
import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.annotations.ApiModelProperty;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */

public class AccessService {

    @Type(typeOf = Vocabulary.DCAT.Classes.DataService)
    @RDFSubject(prefix = Vocabulary.RESOURCE_URI)
    @RDF(Vocabulary.CORE.Properties.dataServiceId)
//    @ApiModelProperty(hidden = true)
    //@JsonIgnore
    @Schema(hidden = true)
    private String dataServiceId;

    @RDF(Vocabulary.DCTERMS.Properties.conformsTo)
    private String conformsTo;

    @RDF(Vocabulary.DCAT.Properties.endpointDescription)
    private String endpointDescription;

    @RDF(Vocabulary.DCAT.Properties.endpointURL)
    private String endpointURL;

    @RDF(Vocabulary.DCAT.Properties.servesDataset)
    private String servesDataset;

    @RDF(Vocabulary.CORE.Properties.serviceSpecs)
    private String serviceSpecs;


    public String getDataServiceId() {
        return dataServiceId;
    }

    public void setDataServiceId(String dataServiceId) {
        this.dataServiceId = dataServiceId;
    }

    public String getConformsTo() {
        return conformsTo;
    }

    public void setConformsTo(String conformsTo) {
        this.conformsTo = conformsTo;
    }

    public String getEndpointDescription() {
        return endpointDescription;
    }

    public void setEndpointDescription(String endpointDescription) {
        this.endpointDescription = endpointDescription;
    }

    public String getEndpointURL() {
        return endpointURL;
    }

    public void setEndpointURL(String endpointURL) {
        this.endpointURL = endpointURL;
    }

    public String getServesDataset() {
        return servesDataset;
    }

    public void setServesDataset(String servesDataset) {
        this.servesDataset = servesDataset;
    }

    public String getServiceSpecs() {
        return serviceSpecs;
    }

    public void setServiceSpecs(String serviceSpecs) {
        this.serviceSpecs = serviceSpecs;
    }
}
