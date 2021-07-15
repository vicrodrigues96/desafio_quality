package br.com.bootcamp.desafio_quality.repository;

import br.com.bootcamp.desafio_quality.dto.BairroDTO;
import br.com.bootcamp.desafio_quality.entity.Bairro;

import java.util.Optional;

public interface IBairroRepository {

    Optional<Bairro> buscarBairro(String nome);
    Bairro persisteBairro(Bairro bairro);
    void deletarBairro(String nome);
    Bairro alterarBairro(Bairro bairro);
}
