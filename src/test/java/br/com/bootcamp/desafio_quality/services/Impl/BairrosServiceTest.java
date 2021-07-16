package br.com.bootcamp.desafio_quality.services.Impl;

import br.com.bootcamp.desafio_quality.dto.BairroDTO;
import br.com.bootcamp.desafio_quality.exception.BairroInexistenteException;
import br.com.bootcamp.desafio_quality.exception.ConflictException;
import br.com.bootcamp.desafio_quality.service.impl.BairrosService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
public class BairrosServiceTest {

    @Autowired
    private BairrosService service;


    BairroDTO bairroDTOMock() {
        return new BairroDTO("Tijuca", new BigDecimal("60.0"));
    }

    BairroDTO addBairroDTOMock() {
        return new BairroDTO("Ponta negra", new BigDecimal("60.0"));
    }

    @Test
    public void deveRetornarUmBairro() {
        Assertions.assertEquals(service.buscarBairro("Tijuca"), bairroDTOMock());
    }

    @Test
    public void deveLancarAExcecaoBairroInexistenteException() {
        Assertions.assertThrows(BairroInexistenteException.class, () -> service.buscarBairro("Tijuca1"));
    }

    @Test
    public void deveLancarAExcecaoConflictException() {
        Assertions.assertThrows(ConflictException.class, () -> service.persisteBairro(bairroDTOMock()));
    }

    @Test
    public void deveRetornarOBairroAdicionado() {
        Assertions.assertEquals(addBairroDTOMock(), service.persisteBairro(addBairroDTOMock()));
    }
}
