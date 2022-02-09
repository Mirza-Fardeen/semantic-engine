package ie.nuig.i3market.semantic.engine.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ie.nuig.i3market.semantic.engine.common.Vocabulary;
import ie.nuig.i3market.semantic.engine.common.annotations.RDF;
import ie.nuig.i3market.semantic.engine.common.annotations.RDFSubject;
import ie.nuig.i3market.semantic.engine.common.annotations.Type;
import ie.nuig.i3market.semantic.engine.domain.pricing.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */

public class PricingModel {

    @Type(typeOf = Vocabulary.PricingModel.Classes.PricingModel)
    @RDFSubject(prefix = Vocabulary.RESOURCE_URI)
    @RDF(Vocabulary.PricingModel.Properties.pricingModelId)
    @Schema(hidden = true)
    @JsonIgnore
    private String pricingModelId;

    @RDF(Vocabulary.PricingModel.Properties.pricingModelName)
    private String pricingModelName;

    @RDF(Vocabulary.PricingModel.Properties.basicPrice)
    private String basicPrice;

    @RDF(Vocabulary.PricingModel.Properties.currency)
    private String currency;


    @RDF(Vocabulary.PricingModel.Properties.hasPaymentOnSubscription)
    private List<PaymentOnSubscription> hasPaymentOnSubscription;

    //    @NotNull
    @RDF(Vocabulary.PricingModel.Properties.hasPaymentOnApi)
    private List<PaymentOnApi> hasPaymentOnApi;

    //    @NotNull
    @RDF(Vocabulary.PricingModel.Properties.hasPaymentOnUnit)
    private List<PaymentOnUnit> hasPaymentOnUnit;

    //    @NotNull
    @RDF(Vocabulary.PricingModel.Properties.hasPaymentOnSize)
    public List<PaymentOnSize> hasPaymentOnSize;

    //    @NotNull
    @RDF(Vocabulary.PricingModel.Properties.hasFreePrice)
    public List<PaymentOnFreePrice> hasFreePrice;


    public String getPricingModelId() {
        return pricingModelId;
    }

    public void setPricingModelId(String pricingModelId) {
        this.pricingModelId = pricingModelId;
    }

    public String getPricingModelName() {
        return pricingModelName;
    }

    public void setPricingModelName(String pricingModelName) {
        this.pricingModelName = pricingModelName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBasicPrice() {
        return basicPrice;
    }

    public void setBasicPrice(String basicPrice) {
        this.basicPrice = basicPrice;
    }

    public List<PaymentOnSubscription> getHasPaymentOnSubscription() {
        return hasPaymentOnSubscription;
    }

    public void setHasPaymentOnSubscription(List<PaymentOnSubscription> hasPaymentOnSubscription) {
        this.hasPaymentOnSubscription = hasPaymentOnSubscription;
    }

    public List<PaymentOnApi> getHasPaymentOnApi() {
        return hasPaymentOnApi;
    }

    public void setHasPaymentOnApi(List<PaymentOnApi> hasPaymentOnApi) {
        this.hasPaymentOnApi = hasPaymentOnApi;
    }

    public List<PaymentOnUnit> getHasPaymentOnUnit() {
        return hasPaymentOnUnit;
    }

    public void setHasPaymentOnUnit(List<PaymentOnUnit> hasPaymentOnUnit) {
        this.hasPaymentOnUnit = hasPaymentOnUnit;
    }

    public List<PaymentOnSize> getHasPaymentOnSize() {
        return hasPaymentOnSize;
    }

    public void setHasPaymentOnSize(List<PaymentOnSize> hasPaymentOnSize) {
        this.hasPaymentOnSize = hasPaymentOnSize;
    }

    public List<PaymentOnFreePrice> getHasFreePrice() {
        return hasFreePrice;
    }

    public void setHasFreePrice(List<PaymentOnFreePrice> hasFreePrice) {
        this.hasFreePrice = hasFreePrice;
    }
}
