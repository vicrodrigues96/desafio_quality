package br.com.bootcamp.desafio_quality.controller;

import br.com.bootcamp.desafio_quality.dto.*;
import br.com.bootcamp.desafio_quality.service.IPropriedadesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/propriedades")
public class PropriedadesController {

    private final IPropriedadesService propriedadesService;

    @Autowired
    public PropriedadesController(IPropriedadesService propriedadesService) {
        this.propriedadesService = propriedadesService;
    }

    @PostMapping
    public ResponseEntity<?> inserirPropriedade(@RequestBody @Valid PropriedadeDTO propriedade,
                                                UriComponentsBuilder uriComponentsBuilder) {
        int id = propriedadesService.inserirPropriedade(propriedade);
        UriComponents uriComponents = uriComponentsBuilder.path("/propriedades/{id}").buildAndExpand(id);

        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    @GetMapping("{id}")
    public PropriedadeDTO buscarPropriedadePorId(@PathVariable int id) {
        return propriedadesService.buscarPropriedadePorId(id);
    }

    @GetMapping("{id}/area")
    public AreaTotalDTO calcularAreaPropriedade(@PathVariable int id) {
        return propriedadesService.calcularAreaPropriedade(id);
    }

    @GetMapping("{id}/valor")
    public ValorPropriedadeDTO calcularValor(@PathVariable int id) {
        return propriedadesService.calcularValor(id);
    }

    @GetMapping("{id}/comodos/area")
    public List<ComodoResponseDTO> calcularAreaComodos(@PathVariable int id) {
        return propriedadesService.calcularAreaComodos(id);
    }

    @GetMapping("{id}/comodos/maior")
    public ComodoResponseDTO buscarMaiorComoda(@PathVariable int id) {
        return propriedadesService.buscarMaiorComodo(id);
    }
}
