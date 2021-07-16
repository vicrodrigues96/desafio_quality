package br.com.bootcamp.desafio_quality.entity;

import br.com.bootcamp.desafio_quality.dto.ComodoRequestDTO;
import br.com.bootcamp.desafio_quality.dto.ComodoResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comodo {

    private String nome;
    private double comprimento;
    private double largura;
    private double area;

    public Comodo(String nome, double comprimento, double largura) {
        this.nome = nome;
        this.comprimento = comprimento;
        this.largura = largura;
        this.area = comprimento * largura;
    }

    public ComodoResponseDTO toComodoResponseDto() {
        return new ComodoResponseDTO(this.nome, this.area);
    }

    public ComodoRequestDTO toComodoRequestDto() {
        return new ComodoRequestDTO(this.nome, this.largura, this.comprimento);
    }
}
