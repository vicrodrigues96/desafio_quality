package br.com.bootcamp.desafio_quality.controller;

import br.com.bootcamp.desafio_quality.dto.PropriedadeDTO;
import br.com.bootcamp.desafio_quality.dto.PropriedadeInfoDTO;
import br.com.bootcamp.desafio_quality.service.IPropriedadesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/propriedades")
public class PropriedadesController {

    private final IPropriedadesService propriedadesService;

    @Autowired
    public PropriedadesController(IPropriedadesService propriedadesService) {
        this.propriedadesService = propriedadesService;
    }

    @PostMapping("/info")
    public PropriedadeInfoDTO calcularInfos(@RequestBody @Valid PropriedadeDTO propriedade) {
       return propriedadesService.calcularInfos(propriedade);
    }
}
