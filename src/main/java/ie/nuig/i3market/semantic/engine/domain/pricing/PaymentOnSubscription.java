package ie.nuig.i3market.semantic.engine.domain.pricing;

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

public class PaymentOnSubscription {

    @Type(typeOf = Vocabulary.PricingModel.Classes.PaymentOnSubscription)
    @RDFSubject(prefix = Vocabulary.RESOURCE_URI)
    @RDF(Vocabulary.PricingModel.Properties.paymentId)
//    @ApiModelProperty(hidden = true)
    @Schema(hidden = true)
    //@JsonIgnore
    private String paymentId;

    @RDF(Vocabulary.PricingModel.Properties.paymentOnSubscriptionName)
    private String paymentOnSubscriptionName;

    @RDF(Vocabulary.PricingModel.Properties.paymentType)
    private String paymentType;

    @RDF(Vocabulary.PricingModel.Properties.timeDuration)
    private String timeDuration;

    @RDF(Vocabulary.DCTERMS.Properties.description)
    private String description;

    @RDF(Vocabulary.PricingModel.Properties.repeat)
    @Schema(example = "DAILY", required = true, type = "String")
    private String repeat;

    @RDF(Vocabulary.PricingModel.Properties.hasSubscriptionPrice)
    private String hasSubscriptionPrice;


    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentOnSubscriptionName() {
        return paymentOnSubscriptionName;
    }

    public void setPaymentOnSubscriptionName(String paymentOnSubscriptionName) {
        this.paymentOnSubscriptionName = paymentOnSubscriptionName;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(String timeDuration) {
        this.timeDuration = timeDuration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getHasSubscriptionPrice() {
        return hasSubscriptionPrice;
    }

    public void setHasSubscriptionPrice(String hasSubscriptionPrice) {
        this.hasSubscriptionPrice = hasSubscriptionPrice;
    }

}
