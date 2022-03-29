package ie.nuig.i3market.semantic.engine.MyPackage;


import ie.nuig.i3market.semantic.engine.domain.DataProvider;
import ie.nuig.i3market.semantic.engine.exceptions.BindingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.function.Function;
import java.util.function.Predicate;

@RestController
@RequestMapping("/api/registration/provider")
class ResController {

    @Autowired
    private DTOservice dtOservice;

    @Operation(
          //  summary = "${api.i3market-semantic-engine.save-data-provider.description}",
            summary = "Dummy Provider post API",
            description = "${api.i3market-semantic-engine.save-data-provider.notes}")

    @PostMapping
    public DataProvider saveDataProvider(@RequestBody  DataProvider dataProviderTemplate) throws ClassNotFoundException, BindingException {
        if(provider.test(dataProviderTemplate) ){
            throw new RuntimeException("field is empty");
        }
         dtOservice.saveDataProvider(dataProviderTemplate);

         return dataProviderTemplate;
    }
    Predicate<DataProvider> provider = e-> e.getProviderId().isEmpty() || e.getName().isEmpty() || e.getDescription().isEmpty() || e.getOrganization().isEmpty();

}
