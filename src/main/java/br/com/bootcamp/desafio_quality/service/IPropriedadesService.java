package br.com.bootcamp.desafio_quality.service;

import br.com.bootcamp.desafio_quality.dto.PropriedadeDTO;
import br.com.bootcamp.desafio_quality.dto.PropriedadeInfoDTO;
import org.springframework.stereotype.Service;

@Service
public interface IPropriedadesService {

    PropriedadeInfoDTO calcularInfos(PropriedadeDTO propriedade);
}
