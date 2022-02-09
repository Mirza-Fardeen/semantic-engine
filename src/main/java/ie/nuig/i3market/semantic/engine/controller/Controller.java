package ie.nuig.i3market.semantic.engine.controller;


import ie.nuig.i3market.semantic.engine.domain.optimise.OfferingContracts;
import ie.nuig.i3market.semantic.engine.service.DataOfferingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/registration")
public class Controller {


    private final DataOfferingService dataOfferingService;

    @Autowired
    public Controller(DataOfferingService dataOfferingService) {
        this.dataOfferingService = dataOfferingService;
    }


    @GetMapping(
            value = "/hello"
    )
    public String getHello(){
        return "Hello";
    }




}
