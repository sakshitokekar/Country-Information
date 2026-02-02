package com.example.NettyCountryInfoProject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;
import tools.jackson.databind.node.ObjectNode;

@RestController
@CrossOrigin(origins="*")
public class NettyCountryInfoProjectController {

    @Autowired
    NettyCountryInfoProjectService service;

    @PostMapping("/gci")
    public Mono<ObjectNode> gci(@RequestBody ObjectNode input){
        return service.gci(input);
    }
}
