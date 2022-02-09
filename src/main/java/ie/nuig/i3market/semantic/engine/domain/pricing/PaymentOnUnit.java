package ie.nuig.i3market.semantic.engine.domain.pricing;

import ie.nuig.i3market.semantic.engine.common.Vocabulary;
import ie.nuig.i3market.semantic.engine.common.annotations.RDF;
import ie.nuig.i3market.semantic.engine.common.annotations.RDFSubject;
import ie.nuig.i3market.semantic.engine.common.annotations.Type;
import io.swagger.v3.oas.annotations.media.Schema;

public class PaymentOnUnit {


    @Type(typeOf = Vocabulary.PricingModel.Classes.PaymentOnUnit)
    @RDFSubject(prefix = Vocabulary.RESOURCE_URI)
    @RDF(Vocabulary.PricingModel.Properties.paymentId)
    @Schema(hidden = true)
    private String paymentId;

    @RDF(Vocabulary.PricingModel.Properties.paymentOnUnitName)
    private String paymentOnUnitName;

    @RDF(Vocabulary.DCTERMS.Properties.description)
    private String description;

    @RDF(Vocabulary.PricingModel.Properties.dataUnit)
    private String dataUnit;

    @RDF(Vocabulary.PricingModel.Properties.hasUnitPrice)
    private String hasUnitPrice;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentOnUnitName() {
        return paymentOnUnitName;
    }

    public void setPaymentOnUnitName(String paymentOnUnitName) {
        this.paymentOnUnitName = paymentOnUnitName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDataUnit() {
        return dataUnit;
    }

    public void setDataUnit(String dataUnit) {
        this.dataUnit = dataUnit;
    }

    public String getHasUnitPrice() {
        return hasUnitPrice;
    }

    public void setHasUnitPrice(String hasUnitPrice) {
        this.hasUnitPrice = hasUnitPrice;
    }

}
