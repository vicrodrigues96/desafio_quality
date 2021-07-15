package br.com.bootcamp.desafio_quality.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comodo {

    private String nome;
    private double altura;
    private double largura;
    private double area;
}
