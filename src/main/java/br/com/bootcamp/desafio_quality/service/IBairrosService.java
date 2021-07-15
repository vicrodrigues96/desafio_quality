package br.com.bootcamp.desafio_quality.service;

import br.com.bootcamp.desafio_quality.dto.BairroDTO;
import br.com.bootcamp.desafio_quality.entity.Bairro;

import java.util.Optional;

public interface IBairrosService {
    BairroDTO buscarBairro(String nome);
    BairroDTO persisteBairro(BairroDTO bairro);
    void deletarBairro(String nome);
    BairroDTO alterarBairro(BairroDTO bairro);
}
