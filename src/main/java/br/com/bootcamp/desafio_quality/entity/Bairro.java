package br.com.bootcamp.desafio_quality.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bairro {

    private String nome;
    private BigDecimal custoPorMetroQuadrado;
}
