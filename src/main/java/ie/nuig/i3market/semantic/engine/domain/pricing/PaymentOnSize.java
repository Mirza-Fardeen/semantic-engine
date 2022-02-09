package ie.nuig.i3market.semantic.engine.domain.pricing;

import ie.nuig.i3market.semantic.engine.common.Vocabulary;
import ie.nuig.i3market.semantic.engine.common.annotations.RDF;
import ie.nuig.i3market.semantic.engine.common.annotations.RDFSubject;
import ie.nuig.i3market.semantic.engine.common.annotations.Type;
import io.swagger.v3.oas.annotations.media.Schema;

public class PaymentOnSize {

    @Type(typeOf = Vocabulary.PricingModel.Classes.PaymentOnSize)
    @RDFSubject(prefix = Vocabulary.RESOURCE_URI)
    @RDF(Vocabulary.PricingModel.Properties.paymentId)
    @Schema(hidden = true)
    private String paymentId;

    @RDF(Vocabulary.PricingModel.Properties.paymentOnSizeName)
    private String paymentOnSizeName;

    @RDF(Vocabulary.DCTERMS.Properties.description)
    private String description;

    @RDF(Vocabulary.PricingModel.Properties.dataSize)
    private String dataSize;

    @RDF(Vocabulary.PricingModel.Properties.hasSizePrice)
    private String hasSizePrice;


    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentOnSizeName() {
        return paymentOnSizeName;
    }

    public void setPaymentOnSizeName(String paymentOnSizeName) {
        this.paymentOnSizeName = paymentOnSizeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDataSize() {
        return dataSize;
    }

    public void setDataSize(String dataSize) {
        this.dataSize = dataSize;
    }

    public String getHasSizePrice() {
        return hasSizePrice;
    }

    public void setHasSizePrice(String hasSizePrice) {
        this.hasSizePrice = hasSizePrice;
    }
}
