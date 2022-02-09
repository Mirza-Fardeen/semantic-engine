package ie.nuig.i3market.semantic.engine.domain.pricing;


import ie.nuig.i3market.semantic.engine.common.Vocabulary;
import ie.nuig.i3market.semantic.engine.common.annotations.RDF;
import ie.nuig.i3market.semantic.engine.common.annotations.RDFSubject;
import ie.nuig.i3market.semantic.engine.common.annotations.Type;
import io.swagger.v3.oas.annotations.media.Schema;

public class PaymentOnFreePrice {

    @Type(typeOf = Vocabulary.PricingModel.Classes.PaymentOnFreePrice)
    @RDFSubject(prefix = Vocabulary.RESOURCE_URI)
    @RDF(Vocabulary.PricingModel.Properties.paymentId)
    @Schema(hidden = true)
    private String paymentId;

    @RDF(Vocabulary.PricingModel.Properties.hasPriceFree)
    @Schema(example = "FREE", required = true)
    private String hasPriceFree;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getHasPriceFree() {
        return hasPriceFree;
    }

    public void setHasPriceFree(String hasPriceFree) {
        this.hasPriceFree = hasPriceFree;
    }
}
