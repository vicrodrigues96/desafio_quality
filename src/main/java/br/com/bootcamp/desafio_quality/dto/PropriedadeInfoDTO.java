package br.com.bootcamp.desafio_quality.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropriedadeInfoDTO {

    private double areaTotal;
    private BigDecimal valorTotal;
    private ComodoResponseDTO maiorComodo;
    private List<ComodoResponseDTO> comodos;
}
