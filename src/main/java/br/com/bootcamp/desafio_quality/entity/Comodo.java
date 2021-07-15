package br.com.bootcamp.desafio_quality.entity;

import br.com.bootcamp.desafio_quality.dto.ComodoResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class Comodo {

    private final String nome;
    private final double altura;
    private final double largura;
    private final double area;

    public Comodo(String nome, double altura, double largura) {
        this.nome = nome;
        this.altura = altura;
        this.largura = largura;
        this.area = altura * largura;
    }

    public ComodoResponseDTO toDTO() {
        return new ComodoResponseDTO(this.nome, this.area);
    }
}
