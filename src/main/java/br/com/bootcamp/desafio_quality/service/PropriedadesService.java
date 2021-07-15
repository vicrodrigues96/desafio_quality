package br.com.bootcamp.desafio_quality.service;

import br.com.bootcamp.desafio_quality.dto.ComodoRequestDTO;
import br.com.bootcamp.desafio_quality.dto.ComodoResponseDTO;
import br.com.bootcamp.desafio_quality.dto.PropriedadeDTO;
import br.com.bootcamp.desafio_quality.dto.PropriedadeInfoDTO;
import br.com.bootcamp.desafio_quality.entity.Comodo;
import br.com.bootcamp.desafio_quality.entity.Propriedade;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropriedadesService implements IPropriedadesService {

    @Override
    public PropriedadeInfoDTO calcularInfos(PropriedadeDTO propriedadeDTO) {
        Propriedade propriedade = propriedadeDTO.toEntity();
        List<Comodo> comodos = propriedade.getComodos();

        double areaTotal = calcularAreaTotal(comodos);
        BigDecimal valorTotal = calcularValorTotal(areaTotal, propriedade.getBairro().getCustoPorMetroQuadrado());
        Comodo maiorComodo = getMaiorComodo(comodos);

        List<ComodoResponseDTO> comodoResponseDTOS = comodos.stream()
                .map(Comodo::toDTO)
                .collect(Collectors.toList());

        return new PropriedadeInfoDTO(areaTotal, valorTotal, maiorComodo.toDTO(), comodoResponseDTOS);
    }

    private double calcularAreaTotal(List<Comodo> comodos) {
        return comodos.stream()
                .mapToDouble(Comodo::getArea)
                .sum();
    }

    private BigDecimal calcularValorTotal(double areaTotal, BigDecimal precoMetroQuadrado) {
        return precoMetroQuadrado.multiply(BigDecimal.valueOf(areaTotal));
    }

    private Comodo getMaiorComodo(List<Comodo> comodos) {
        return comodos.stream()
                .max(Comparator.comparing(Comodo::getArea))
                .orElse(null);
    }

}
