package ie.nuig.i3market.semantic.engine.domain.contracts;

import ie.nuig.i3market.semantic.engine.common.Vocabulary;
import ie.nuig.i3market.semantic.engine.common.annotations.RDF;
import ie.nuig.i3market.semantic.engine.common.annotations.RDFSubject;
import ie.nuig.i3market.semantic.engine.common.annotations.Type;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class ContractParameters {

    @Type(typeOf = Vocabulary.DCAT.Classes.ContractParameters)
    @RDFSubject(prefix = Vocabulary.RESOURCE_URI)
    @RDF(Vocabulary.CORE.Properties.contractParametersId)
//    @ApiModelProperty(hidden = true)
    @Schema(hidden = true)
    //@JsonIgnore
    private String contractParametersId;

    @RDF(Vocabulary.CORE.Properties.interestOfProvider)
    private  String interestOfProvider;

    @RDF(Vocabulary.CORE.Properties.interestDescription)
    private String interestDescription;

    @RDF(Vocabulary.CORE.Properties.hasGoverningJurisdiction)
    private String hasGoverningJurisdiction;

    @RDF(Vocabulary.CORE.Properties.purpose)
    private String purpose;


    @RDF(Vocabulary.CORE.Properties.purposeDescription)
    private String purposeDescription;


    @RDF(Vocabulary.CORE.Properties.hasIntendedUse)
    private List<IntendedUse> hasIntendedUse;


    @RDF(Vocabulary.CORE.Properties.hasLicenseGrant)
    private List<LicenseGrant> hasLicenseGrant;
    
    public String getContractParametersId() {
        return contractParametersId;
    }

    public void setContractParametersId(String contractParametersId) {
        this.contractParametersId = contractParametersId;
    }

    public String getInterestOfProvider() {
        return interestOfProvider;
    }

    public void setInterestOfProvider(String interestOfProvider) {
        this.interestOfProvider = interestOfProvider;
    }

    public String getInterestDescription() {
        return interestDescription;
    }

    public void setInterestDescription(String interestDescription) {
        this.interestDescription = interestDescription;
    }

    public String getHasGoverningJurisdiction() {
        return hasGoverningJurisdiction;
    }

    public void setHasGoverningJurisdiction(String hasGoverningJurisdiction) {
        this.hasGoverningJurisdiction = hasGoverningJurisdiction;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getPurposeDescription() {
        return purposeDescription;
    }

    public void setPurposeDescription(String purposeDescription) {
        this.purposeDescription = purposeDescription;
    }

    public List<IntendedUse> getHasIntendedUse() {
        return hasIntendedUse;
    }

    public void setHasIntendedUse(List<IntendedUse> hasIntendedUse) {
        this.hasIntendedUse = hasIntendedUse;
    }

    public List<LicenseGrant> getHasLicenseGrant() {
        return hasLicenseGrant;
    }

    public void setHasLicenseGrant(List<LicenseGrant> hasLicenseGrant) {
        this.hasLicenseGrant = hasLicenseGrant;
    }


}
