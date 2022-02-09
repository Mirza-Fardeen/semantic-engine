package ie.nuig.i3market.semantic.engine.dto;

import ie.nuig.i3market.semantic.engine.domain.DataOffering;
import ie.nuig.i3market.semantic.engine.domain.Dataset;
import ie.nuig.i3market.semantic.engine.domain.PricingModel;
import ie.nuig.i3market.semantic.engine.domain.contracts.ContractParameters;
import ie.nuig.i3market.semantic.engine.domain.optimise.Offering;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */


@Component
public class Mapper {

    public ExtDataOfferingDto entityToDTO(DataOffering entity) {

        ExtDataOfferingDto dto = new ExtDataOfferingDto();

        var version = Integer.parseInt(entity.getVersion());

        dto.setDataOfferingTitle(entity.getDataOfferingTitle());
        dto.setDataOfferingDescription(entity.getDataOfferingDescription());
        dto.setStatus(entity.getStatus());
        dto.setVersion(version);
        dto.setCategory(entity.getCategory());
        dto.setProvider(entity.getProvider());
        dto.setMarketId(entity.getMarketId());
        dto.setOwner(entity.getOwner());
        dto.setDataOfferingExpirationTime(entity.getDataOfferingExpirationTime());
        dto.setContractParameters(entity.getContractParameters());
        dto.setHasPricingModel(entity.getHasPricingModel());
        dto.setHasDataset(entity.getHasDataset());

        return dto;

    }

    public static ExtDataOfferingDto entityToDTOMap(DataOffering entity) {

        ExtDataOfferingDto dto = new ExtDataOfferingDto();

        var version = Integer.parseInt(entity.getVersion());

//        dto.setDataOfferingId(entity.getDataOfferingId());
        dto.setDataOfferingTitle(entity.getDataOfferingTitle());
        dto.setDataOfferingDescription(entity.getDataOfferingDescription());
        dto.setStatus(entity.getStatus());
        dto.setVersion(version);
        dto.setCategory(entity.getCategory());
        dto.setProvider(entity.getProvider());
        dto.setMarketId(entity.getMarketId());
        dto.setOwner(entity.getOwner());
        dto.setDataOfferingExpirationTime(entity.getDataOfferingExpirationTime());
        dto.setContractParameters(entity.getContractParameters());
        dto.setHasPricingModel(entity.getHasPricingModel());
        dto.setHasDataset(entity.getHasDataset());

        return dto;

    }


    //This is to zip all components to form a final offering information
    public ExtDataOfferingDto offeringDtoAggregate(Offering offering,
                                                   List<ContractParameters> contractList,
                                                   List<PricingModel> pricingModelsList,
                                                   List<Dataset> datasetList) {
        ExtDataOfferingDto offr = new ExtDataOfferingDto();

        var version = Integer.parseInt(offering.getVersion());

        offr.setDataOfferingId(offering.getDataOfferingId());

        offr.setProvider(offering.getProvider());

        offr.setMarketId(offering.getMarketId());

        offr.setDataOfferingTitle(offering.getDataOfferingTitle());

        offr.setDataOfferingDescription(offering.getDataOfferingDescription());

        offr.setStatus(offering.getStatus());

        offr.setVersion(version);

        offr.setCategory(offering.getCategory());

        offr.setOwner(offering.getOwner());

        offr.setDataOfferingExpirationTime(offering.getDataOfferingExpirationTime());

        offr.setDataOfferingCreated(offering.getDataOfferingCreated());

        offr.setLastModified(offering.getLastModified());

        offr.setContractParameters(contractList);

        offr.setHasPricingModel(pricingModelsList);

        offr.setHasDataset(datasetList);

        return offr;
    }


    public DataOffering dtoToEntity(DataOfferingDto dto) {

        DataOffering entity = new DataOffering();

        entity.setProvider(dto.getProvider().toLowerCase());
        entity.setMarketId(dto.getMarketId());
        entity.setOwner(dto.getOwner());
        entity.setDataOfferingTitle(dto.getDataOfferingTitle());
        entity.setDataOfferingDescription(dto.getDataOfferingDescription());
        entity.setCategory(dto.getCategory().toLowerCase());
        entity.setStatus(dto.getStatus().toLowerCase());
        entity.setDataOfferingExpirationTime(dto.getDataOfferingExpirationTime());
        entity.setContractParameters(dto.getContractParameters());
        entity.setHasPricingModel(dto.getHasPricingModel());
        entity.setHasDataset(dto.getHasDataset());
        return entity;
    }


    public DataOffering extDtoToEntity(ExtDataOfferingDto dto) {

        DataOffering entity = new DataOffering();

        entity.setProvider(dto.getProvider());
        entity.setMarketId(dto.getMarketId());
        entity.setOwner(dto.getOwner());

        String version = Integer.toString(dto.getVersion());
        entity.setVersion(version);

        entity.setDataOfferingTitle(dto.getDataOfferingTitle());

        entity.setDataOfferingDescription(dto.getDataOfferingDescription());

        entity.setCategory(dto.getCategory());

        entity.setStatus(dto.getStatus());

        entity.setDataOfferingExpirationTime(dto.getDataOfferingExpirationTime());

        entity.setContractParameters(dto.getContractParameters());

        entity.setHasPricingModel(dto.getHasPricingModel());

        entity.setHasDataset(dto.getHasDataset());

        return entity;
    }

}
