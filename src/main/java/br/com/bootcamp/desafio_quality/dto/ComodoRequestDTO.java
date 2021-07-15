package br.com.bootcamp.desafio_quality.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComodoRequestDTO {

    private String nome;
    private double altura;
    private double largura;
}
