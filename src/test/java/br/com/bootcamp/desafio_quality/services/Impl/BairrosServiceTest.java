package br.com.bootcamp.desafio_quality.services.Impl;

import br.com.bootcamp.desafio_quality.dto.BairroDTO;
import br.com.bootcamp.desafio_quality.exception.BairroInexistenteException;
import br.com.bootcamp.desafio_quality.exception.ConflictException;
import br.com.bootcamp.desafio_quality.repository.IBairroRepository;
import br.com.bootcamp.desafio_quality.service.impl.BairrosService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
public class BairrosServiceTest {

    @InjectMocks
    private BairrosService service;

    @Mock
    public IBairroRepository bairroRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Environment env;

    @BeforeEach
    void setUp() throws IOException {
        var filePath = env.getProperty("repository.bairros.path", "src/test/resources/repository/bairros.json");
        var file = new File(filePath);
        objectMapper.writeValue(file, new HashMap<>());
    }


    BairroDTO bairroDTOMock() {
        return new BairroDTO("Tijuca", new BigDecimal("60.0"));
    }

    BairroDTO addBairroDTOMock() {
        return new BairroDTO("Ponta negra", new BigDecimal("60.0"));
    }

    @Test
    public void deveRetornarUmBairro() {

        when(bairroRepository.buscarBairro("Tijuca")).thenReturn(Optional.of(bairroDTOMock().toEntity()));
        Assertions.assertEquals(service.buscarBairro("Tijuca"), bairroDTOMock());
    }

    @Test
    public void deveLancarAExcecaoBairroInexistenteException() {
        Assertions.assertThrows(BairroInexistenteException.class, () -> service.buscarBairro("Tijuca1"));
    }

    @Test
    public void deveLancarAExcecaoConflictException() {

        when(bairroRepository.persisteBairro(bairroDTOMock().toEntity())).thenThrow(new ConflictException("Bairro já " +
                "existe!"));
        Assertions.assertThrows(ConflictException.class, () -> service.persisteBairro(bairroDTOMock()));
    }

    @Test
    public void deveRetornarOBairroAdicionado() {

        when(bairroRepository.persisteBairro(addBairroDTOMock().toEntity())).thenReturn(addBairroDTOMock().toEntity());
        Assertions.assertEquals(addBairroDTOMock(), service.persisteBairro(addBairroDTOMock()));
    }
}
