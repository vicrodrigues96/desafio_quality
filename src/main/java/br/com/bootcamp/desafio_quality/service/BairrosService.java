package br.com.bootcamp.desafio_quality.service;

import br.com.bootcamp.desafio_quality.dto.BairroDTO;
import br.com.bootcamp.desafio_quality.exception.BairroInexistenteException;
import br.com.bootcamp.desafio_quality.repository.IBairroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BairrosService implements IBairrosService{


    private final IBairroRepository bairroRepository;

    @Autowired
    public BairrosService(IBairroRepository bairroRepository) {
        this.bairroRepository = bairroRepository;
    }

    @Override
    public BairroDTO buscarBairro(String nome) {
        return bairroRepository.buscarBairro(nome)
                .orElseThrow(() -> new BairroInexistenteException("Bairro não encontrado"))
                .toDTO();
    }

    @Override
    public BairroDTO persisteBairro(BairroDTO bairro) {
        return  bairroRepository.persisteBairro(bairro.toEntity()).toDTO();
    }

    @Override
    public void deletarBairro(String nome) {
        bairroRepository.deletarBairro(nome);
    }

    @Override
    public BairroDTO alterarBairro(BairroDTO bairro) {
        return bairroRepository.alterarBairro(bairro.toEntity()).toDTO();
    }
}
