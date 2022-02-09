package ie.nuig.i3market.semantic.engine.domain.entities.lists;


import ie.nuig.i3market.semantic.engine.common.Vocabulary;
import ie.nuig.i3market.semantic.engine.common.annotations.RDF;
import ie.nuig.i3market.semantic.engine.common.annotations.RDFSubject;
import ie.nuig.i3market.semantic.engine.common.annotations.Type;

/**
 * AUTHOR: Chi-Hung Le
 */
public class MarketIdList {
    @Type(typeOf = Vocabulary.CORE.Classes.Provider)
    @RDFSubject(prefix = Vocabulary.RESOURCE_URI)
    @RDF(Vocabulary.RDF.type)
    private String marketId;

    public String getMarketId() {
        return marketId;
    }
}
