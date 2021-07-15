package br.com.bootcamp.desafio_quality.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Propriedade {
    private Integer id;
    private String nome;
    private Bairro bairro;
    private List<Comodo> comodos;

    public Propriedade(String nome, Bairro bairro, List<Comodo> comodos) {
        this.nome = nome;
        this.bairro = bairro;
        this.comodos = comodos;
    }
}
