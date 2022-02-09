package ie.nuig.i3market.semantic.engine.domain.entities.lists;

import ie.nuig.i3market.semantic.engine.common.Vocabulary;
import ie.nuig.i3market.semantic.engine.common.annotations.RDF;
import ie.nuig.i3market.semantic.engine.common.annotations.RDFSubject;
import ie.nuig.i3market.semantic.engine.common.annotations.Type;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */
public class OfferingsList {
    @Type(typeOf = Vocabulary.CORE.Classes.DataOffering)
    @RDFSubject(prefix = Vocabulary.RESOURCE_URI)
    @RDF(Vocabulary.RDF.type)
    private String offering;

    public OfferingsList(String offering) {
        this.offering = offering;
    }

    public OfferingsList() {}

    public String getOffering() {
        return offering;
    }

    public void setOffering(String offering) {
        this.offering = offering;
    }
}
