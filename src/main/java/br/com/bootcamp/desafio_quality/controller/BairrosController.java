package br.com.bootcamp.desafio_quality.controller;

import br.com.bootcamp.desafio_quality.dto.BairroDTO;
import br.com.bootcamp.desafio_quality.service.IBairrosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/bairros")
public class BairrosController {

    private final IBairrosService bairrosService;

    @Autowired
    public BairrosController(IBairrosService bairrosService) {
        this.bairrosService = bairrosService;
    }


    @GetMapping("/{nome}")
    public BairroDTO buscarBairro(@PathVariable  String nome) {
        return bairrosService.buscarBairro(nome);
    }

    @PostMapping
    public ResponseEntity<BairroDTO> persistirBairro(@RequestBody @Valid BairroDTO bairro) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bairrosService.persisteBairro(bairro));
    }

    @DeleteMapping("/{nome}")
    ResponseEntity<?> deletarBairro(@PathVariable String nome) {
        bairrosService.deletarBairro(nome);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping
    public BairroDTO alterarBairro(@RequestBody @Valid BairroDTO bairro) {
        return bairrosService.alterarBairro(bairro);
    }
}
