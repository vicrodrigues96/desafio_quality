package br.com.bootcamp.desafio_quality.service;

import br.com.bootcamp.desafio_quality.dto.ComodoRequestDTO;
import br.com.bootcamp.desafio_quality.dto.ComodoResponseDTO;
import br.com.bootcamp.desafio_quality.dto.PropriedadeDTO;
import br.com.bootcamp.desafio_quality.dto.PropriedadeInfoDTO;
import br.com.bootcamp.desafio_quality.entity.Comodo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PropriedadesService implements IPropriedadesService {

    @Override
    public PropriedadeInfoDTO calcularInfos(PropriedadeDTO propriedade) {
        return null;
    }

    private double calcularAreaTotal(List<Comodo> comodos) {
        return 0;
    }

    private double calcularValorTotal(double areaTotal, BigDecimal precoMetroQuadrado) {
        return 0;
    }

    private Comodo getMaiorComodo(List<Comodo> comodos) {
        return null;
    }

    private List<Comodo> calculaAreaPorComodo(List<Comodo> comodos) {
        return null;
    }

}
