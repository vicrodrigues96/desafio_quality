package br.com.bootcamp.desafio_quality.repository;

import br.com.bootcamp.desafio_quality.entity.Propriedade;

import java.util.Optional;

public interface IPropriedadeRepository {
    Propriedade persistePropriedade(Propriedade propriedade);
    Optional<Propriedade> buscarPropriedade(Integer id);
}
