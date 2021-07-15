package br.com.bootcamp.desafio_quality.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bairro {
    private String nome;
    private BigDecimal custoPorMetroQuadrado;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bairro bairro = (Bairro) o;
        return nome.equals(bairro.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }
}
