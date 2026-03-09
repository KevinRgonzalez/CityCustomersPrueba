package com.ts4.customer.data.client;


import com.ts4.customer.data.model.comercialstructure.OutputComercialStructure;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "CommercialStructureClient", url = "${cityclub.url.comercial-structure}")
public interface CommercialStructureClient {

    @GetMapping("/categories/({idCategorias})?levels=1")
    @Headers({ "Content-Type: application/json" ,"Accept: application/json"})
    ResponseEntity<OutputComercialStructure> getCategorias(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("idCategorias") List<String> idCategorias);

}
