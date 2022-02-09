package ie.nuig.i3market.semantic.engine.controller;


import ie.nuig.i3market.semantic.engine.common.GenerateTemplate;
import ie.nuig.i3market.semantic.engine.domain.DataOffering;
import ie.nuig.i3market.semantic.engine.domain.DataProvider;
import ie.nuig.i3market.semantic.engine.domain.entities.lists.CategoriesList;
import ie.nuig.i3market.semantic.engine.domain.entities.lists.OfferingsList;
import ie.nuig.i3market.semantic.engine.domain.entities.lists.ProvidersList;
import ie.nuig.i3market.semantic.engine.domain.optimise.OfferingContracts;
import ie.nuig.i3market.semantic.engine.dto.DataOfferingDto;
import ie.nuig.i3market.semantic.engine.dto.DataOfferingId;
import ie.nuig.i3market.semantic.engine.dto.ExtDataOfferingDto;
import ie.nuig.i3market.semantic.engine.dto.Offerings;
import ie.nuig.i3market.semantic.engine.exceptions.BindingException;
import ie.nuig.i3market.semantic.engine.service.DataOfferingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.io.IOException;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */


@RestController
@RequestMapping("/api/registration")
@Tag(name = "Semantic Engine APIs list")
@Validated
public class RegistrationOfferingController {

    private final DataOfferingService dataOfferingService;

    @Autowired
    public RegistrationOfferingController(DataOfferingService dataOfferingService) {
        this.dataOfferingService = dataOfferingService;
    }

    //This is to save a data provider using the following form. The form might have been revised so please check
    // http://localhost:8002/Swagger-ui/html for the latest update
    @Operation(
            summary = "${api.i3market-semantic-engine.save-data-provider.description}",
            description = "${api.i3market-semantic-engine.save-data-provider.notes}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
            @ApiResponse(responseCode = "400", description = "${api.responseCodes.saveProviderBadRequest.description}"),
            @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
            @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
    })
    @PostMapping
    public Mono<Void> saveDataProvider(@RequestBody @Valid DataProvider dataProviderTemplate) throws ClassNotFoundException, BindingException {
        return dataOfferingService.saveDataProvider(dataProviderTemplate);
    }

    //This is to save for data offering. Please check http://localhost:8002/Swagger-ui/html for the latest update
    @PostMapping(
            value = "/data-offering",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "register a data offering")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "failed to save offerings"),
            @ApiResponse(responseCode = "422", description = "Unprocessable")})
    public Flux<DataOfferingId> saveDataOffering(@Valid @RequestBody DataOfferingDto dataOffering) {
        return dataOfferingService.saveDataOffering(dataOffering);
    }

    //This API is to get a list of category , which is stored at http://95.211.3.244:7500/data_categories
    @GetMapping(value = "/categories-list")
    @Operation(summary = "get a list of categories")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "failed to get list of categories"),
            @ApiResponse(responseCode = "422", description = "Unprocessable")})
    public Flux<CategoriesList> getAllCategories() {
        return dataOfferingService.getAllCategories();
    }


    //This is to get a data offering template in the JSON data format
    @GetMapping(value = "/offering/offering-template")
    @Operation(summary = "download offering template")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "internal server error to create template"),
            @ApiResponse(responseCode = "422", description = "Unprocessable")})
    public ResponseEntity<String> getOfferingTemplate() throws IOException, ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + DataOffering.class.getSimpleName())
                .contentType(MediaType.APPLICATION_JSON)
                .body(GenerateTemplate.getTemplate(new DataOffering()));
    }



    //This is to delete a data provider
    @DeleteMapping("/provider/{providerId}/delete")
    public Mono<Void> deleteDataProvider(@PathVariable(name = "providerId") String providerId) throws ClassNotFoundException, BindingException, InterruptedException {
        return dataOfferingService.deleteDataProviderById(providerId);
    }


    //This is to get a data offering by providerID. It is not necessary to have a data provider in the senamtic engine to make
    // a data offering registration
    @GetMapping(value = "/offering/{id}/providerId")
    @Operation(summary = "get a registered data offering by provider id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "failed to get offerings by provider id"),
            @ApiResponse(responseCode = "422", description = "Unprocessable")})
    public Flux<ExtDataOfferingDto> offeringAggregateByProviderId(@PathVariable(name = "id") String id,
                                                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                                                  @RequestParam(value = "size", defaultValue = "5") int size
    ) throws BindingException, ClassNotFoundException, InterruptedException {
        return dataOfferingService.offeringAggregateByProviderId(id, page, size);
    }


    //This is to get contract parameter by offeringId
    @GetMapping(value = "/contract-parameter/{offeringId}/offeringId")
    @Operation(summary = "get a registered data offering by provider id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "failed to get offerings by provider id"),
            @ApiResponse(responseCode = "422", description = "Unprocessable")})
    public Flux<OfferingContracts> contractParametersAggregateByOfferingId(@PathVariable(name = "offeringId") String offeringId
    ) throws BindingException, ClassNotFoundException {
        return dataOfferingService.contractParametersAggregateByOfferingId(offeringId);
    }


    //This is to get an offering by its offering ID
    @GetMapping(value = "/offering/{id}/offeringId")
    @Operation(summary = "get a registered data offering by offeringId")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "failed to get offerings by offering id"),
            @ApiResponse(responseCode = "422", description = "Unprocessable")})
    public Flux<ExtDataOfferingDto> offeringAggregateByOfferingId(@PathVariable(name = "id") String id
    ) throws BindingException {
        return dataOfferingService.offeringAggregateByOfferingId(id);
    }

    //This is to get an offering by its category
    @GetMapping(value = "/offering/{category}")
    @Operation(summary = "get a registered data offerings by category")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "failed to get offerings by category"),
            @ApiResponse(responseCode = "422", description = "Unprocessable")})
    public Flux<ExtDataOfferingDto> offeringAggregateByCategory(@PathVariable(name = "category") String category,
                                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                                @RequestParam(value = "size", defaultValue = "5") int size
    ) throws BindingException, InterruptedException {
        return dataOfferingService.offeringAggregateByCategory(category, page, size);
    }


    //This is to get a data provider list, registed in the semantic engine
    @GetMapping(value = "/providers-list")
    @Operation(summary = "get a list of providers")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "failed to get list of providers"),
            @ApiResponse(responseCode = "422", description = "Unprocessable")})
    public Flux<ProvidersList> getAllProviders(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) throws BindingException {
        return dataOfferingService.getAllProviders(page, size);
    }


    //This is to get all data offering in the semantic engine
    @GetMapping(value = "/offerings-list")
    @Operation(summary = "get a list of offerings")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "failed to get list of offerings"),
            @ApiResponse(responseCode = "422", description = "Unprocessable")})
    public Flux<OfferingsList> getAllDataOffering(@RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "5") int size)
            throws BindingException {
        return dataOfferingService.getAllDataOffering(page, size);
    }


    //This is to update an existing offering in the semantic engine
    @Operation(
            summary = "${api.i3market-semantic-engine.update-offering.description}",
            description = "${api.i3market-semantic-engine.update-offering.notes}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
            @ApiResponse(responseCode = "400", description = "${api.responseCodes.updateOfferingBadRequest.description}"),
            @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
            @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
    })
    @PatchMapping("/update-offering")
    public Mono<Void> updateOffering(@RequestBody DataOffering body) throws BindingException {
        return dataOfferingService.updateOffering(body).then();
    }


    //This is to delete a data offering using its offering Id
    @DeleteMapping("/delete-offering/{offeringId}")
    @Operation(summary = "Delete a data offering")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "failed to delete an offerings by id"),
            @ApiResponse(responseCode = "422", description = "Unprocessable")})
    public Mono<Void> deleteOffering(@PathVariable(name = "offeringId") String offeringId) throws BindingException, ClassNotFoundException {
        return dataOfferingService.deleteOffering(offeringId);
    }

    //This is to get provider by category. This might be necessary for web-ri development
    @GetMapping(value = "/providers/{category}/category")
    @Operation(summary = "get a data providerId by category")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "failed to get offerings by category"),
            @ApiResponse(responseCode = "422", description = "Unprocessable")})
    public Flux<ProvidersList> getProviderByCategory(@PathVariable(name = "category") String category,
                                                              @RequestParam(value = "page", defaultValue = "0") int page,
                                                              @RequestParam(value = "size", defaultValue = "5") int size
    ) throws BindingException {
        return dataOfferingService.getProviderListComponentByCategory(category, page, size);
    }

    @GetMapping(
            value = "offerings/provider/{providerId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Flux<DataProvider> getDataProviderById(@PathVariable(name = "providerId") String providerId) throws BindingException, ClassNotFoundException {
        return dataOfferingService.getDataProviderById(providerId);
    }


    @Operation(
            summary = "${api.i3market-semantic-engine.get-total-offering.description}",
            description = "${api.i3market-semantic-engine.get-total-offering.notes}")
    @GetMapping("/offerings")
    public Mono<Offerings> getOfferingByProviderIdAndCategorySorted(@RequestParam(value = "providerId", defaultValue = "All") String providerId,
                                                                    @RequestParam(value = "category", defaultValue = "All") String category,
                                                                    @RequestParam(value = "page", defaultValue = "0") int page,
                                                                    @RequestParam(value = "size", defaultValue = "5") int size,
                                                                    @RequestParam(value = "orderBy", defaultValue = "time") String orderBy,
                                                                    @RequestParam(value = "sortBy", defaultValue = "desc") String sortBy

    )  {

        return dataOfferingService.getOfferingByProviderIdAndCategorySorted(providerId, category, page, size, sortBy, orderBy);

    }


}
