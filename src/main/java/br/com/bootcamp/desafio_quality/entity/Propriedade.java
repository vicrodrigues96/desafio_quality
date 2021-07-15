package br.com.bootcamp.desafio_quality.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Propriedade {

    private String nome;
    private Bairro bairro;
    private List<Comodo> comodos;
}
