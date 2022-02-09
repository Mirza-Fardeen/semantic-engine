package ie.nuig.i3market.semantic.engine.domain.pricing;

import ie.nuig.i3market.semantic.engine.common.Vocabulary;
import ie.nuig.i3market.semantic.engine.common.annotations.RDF;
import ie.nuig.i3market.semantic.engine.common.annotations.RDFSubject;
import ie.nuig.i3market.semantic.engine.common.annotations.Type;
import io.swagger.v3.oas.annotations.media.Schema;

public class PaymentOnPlan {

    @Type(typeOf = Vocabulary.PricingModel.Classes.PaymentOnPlan)
    @RDFSubject(prefix = Vocabulary.RESOURCE_URI)
    @RDF(Vocabulary.PricingModel.Properties.paymentId)
    @Schema(hidden = true)
    private String paymentId;

    @RDF(Vocabulary.PricingModel.Properties.paymentOnPlanName)
    private String paymentOnPlanName;

    @RDF(Vocabulary.DCTERMS.Properties.description)
    private String description;

    @RDF(Vocabulary.PricingModel.Properties.timeDuration)
    private String planDuration;

    @RDF(Vocabulary.PricingModel.Properties.hasPlanPrice)
    private String hasPlanPrice;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentOnPlanName() {
        return paymentOnPlanName;
    }

    public void setPaymentOnPlanName(String paymentOnPlanName) {
        this.paymentOnPlanName = paymentOnPlanName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlanDuration() {
        return planDuration;
    }

    public void setPlanDuration(String planDuration) {
        this.planDuration = planDuration;
    }

    public String getHasPlanPrice() {
        return hasPlanPrice;
    }

    public void setHasPlanPrice(String hasPlanPrice) {
        this.hasPlanPrice = hasPlanPrice;
    }
}
