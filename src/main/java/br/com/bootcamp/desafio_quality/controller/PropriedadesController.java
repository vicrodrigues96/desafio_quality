package br.com.bootcamp.desafio_quality.controller;

import br.com.bootcamp.desafio_quality.dto.PropriedadeDTO;
import br.com.bootcamp.desafio_quality.dto.PropriedadeInfoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/propriedades")
public class PropriedadesController {


    @PostMapping("/info")
    public ResponseEntity<PropriedadeInfoDTO> calcularInfos(@RequestBody @Valid PropriedadeDTO propriedade) {
        throw new RuntimeException("not implemented");
    }
}
