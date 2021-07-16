package br.com.bootcamp.desafio_quality.service;

import br.com.bootcamp.desafio_quality.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IPropriedadesService {

    Integer inserirPropriedade(PropriedadeDTO propriedade);
    AreaTotalDTO calcularAreaPropriedade(int id);
    ValorPropriedadeDTO calcularValor(int id);
    List<ComodoResponseDTO> calcularAreaComodos(int id);
    ComodoResponseDTO buscarMaiorComodo(int id);
    PropriedadeDTO buscarPropriedadePorId(int id);
}
