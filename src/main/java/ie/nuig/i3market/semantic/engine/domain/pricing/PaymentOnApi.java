package ie.nuig.i3market.semantic.engine.domain.pricing;

import ie.nuig.i3market.semantic.engine.common.Vocabulary;
import ie.nuig.i3market.semantic.engine.common.annotations.RDF;
import ie.nuig.i3market.semantic.engine.common.annotations.RDFSubject;
import ie.nuig.i3market.semantic.engine.common.annotations.Type;
import io.swagger.v3.oas.annotations.media.Schema;

public class PaymentOnApi {

    @Type(typeOf = Vocabulary.PricingModel.Classes.PaymentOnApi)
    @RDFSubject(prefix = Vocabulary.RESOURCE_URI)
    @RDF(Vocabulary.PricingModel.Properties.paymentId)
    @Schema(hidden = true)
    private String paymentId;

    @RDF(Vocabulary.PricingModel.Properties.paymentOnApiName)
    private String paymentOnApiName;

    @RDF(Vocabulary.DCTERMS.Properties.description)
    private String description;

    @RDF(Vocabulary.PricingModel.Properties.numberOfObject)
    private String numberOfObject; // Number of Object moved via API

    @RDF(Vocabulary.PricingModel.Properties.hasApiPrice)
    private String hasApiPrice;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentOnApiName() {
        return paymentOnApiName;
    }

    public void setPaymentOnApiName(String paymentOnApiName) {
        this.paymentOnApiName = paymentOnApiName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNumberOfObject() {
        return numberOfObject;
    }

    public void setNumberOfObject(String numberOfObject) {
        this.numberOfObject = numberOfObject;
    }

    public String getHasApiPrice() {
        return hasApiPrice;
    }

    public void setHasApiPrice(String hasApiPrice) {
        this.hasApiPrice = hasApiPrice;
    }
}
