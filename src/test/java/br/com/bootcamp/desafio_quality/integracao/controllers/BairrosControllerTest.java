package br.com.bootcamp.desafio_quality.integracao.controllers;

import br.com.bootcamp.desafio_quality.entity.Bairro;
import br.com.bootcamp.desafio_quality.integracao.IntegrationTestBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Objects;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class BairrosControllerTest extends IntegrationTestBase {

    public static final String BASE_PATH = "/bairros";
    public static final String PATH_NAME = BASE_PATH + "/{name}";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public BairrosControllerTest(Environment env) {
      super(env);
    }

    @Test
    void buscarBairro_inexistente_deveRetornar404() throws Exception {
        mvc.perform(get(PATH_NAME, "bairro2")).andExpect(status().isBadRequest());
    }

    @Test
    void buscarBairro_existente_deveRetornarBairro() throws Exception {
        var nomeBairro = "nomeBairro";
        var bairro = new Bairro(nomeBairro, new BigDecimal("10.0"));
        persisteBairro(bairro);

        mvc.perform(get(PATH_NAME, nomeBairro))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is(nomeBairro)))
                .andExpect(jsonPath("$.custoPorMetroQuadrado", is(10.0)));
    }

    @Test
    void persistirBairro_bairroValido_deveRetornarBairro() throws Exception {
        var bairro = new Bairro("Leblon", new BigDecimal("10.0"));

        mvc.perform(post(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(bairro)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is(bairro.getNome())))
                .andExpect(jsonPath("$.custoPorMetroQuadrado", is(bairro.getCustoPorMetroQuadrado().doubleValue())));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/integrations/nome-bairros-cenarios.csv", numLinesToSkip = 1)
    void persistirBairro_nomesInvalidos(String nome, Integer statusCodeEsperado, String mensagemEsperada) throws Exception {
        var bairro = new Bairro(nome, new BigDecimal("10.0"));

        mvc.perform(post(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(bairro)))
                .andExpect(status().is(statusCodeEsperado))
                .andExpect(jsonPath("$.statusCode", is(statusCodeEsperado)))
                .andExpect(jsonPath("$.message", is(mensagemEsperada)));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/integrations/valor-bairros-cenarios.csv", numLinesToSkip = 1)
    void persistirBairro_valoresInvalidos(String valor, Integer statusCodeEsperado, String mensagemEsperada) throws Exception {
        var bairro = new Bairro("Lapa", Objects.isNull(valor) ? null : new BigDecimal(valor));

        mvc.perform(post(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(bairro)))
                .andExpect(status().is(statusCodeEsperado))
                .andExpect(jsonPath("$.statusCode", is(statusCodeEsperado)))
                .andExpect(jsonPath("$.message", is(mensagemEsperada)));
    }

    @Test
    void deletarBairro_bairroExistente_deveRetornarNoContent() throws Exception {
        var nomeBairro = "nomeBairro";
        var bairro = new Bairro(nomeBairro, new BigDecimal("10.0"));
        persisteBairro(bairro);

        mvc.perform(delete(PATH_NAME, nomeBairro))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());

        assertFalse(existeBairro(nomeBairro));
    }

    @Test
    void deletarBairro_bairroInexistente_deveRetornarNotFound() throws Exception {
        var nomeBairro = "nomeBairro";

        mvc.perform(delete(PATH_NAME, nomeBairro))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode", is(400)))
                .andExpect(jsonPath("$.message", is("Bairro a ser deletado não encontrado")));
    }

    @Test
    void alterarBairro_bairroExistente_deveRetornarSucesso() throws Exception {
        var nomeBairro = "nomeBairro";
        var bairro = new Bairro(nomeBairro, new BigDecimal("10.0"));
        persisteBairro(bairro);

        var novoBairro = new Bairro(nomeBairro, new BigDecimal("12.0"));

        mvc.perform(put(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(novoBairro)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is(nomeBairro)))
                .andExpect(jsonPath("$.custoPorMetroQuadrado", is(novoBairro.getCustoPorMetroQuadrado().doubleValue())));

        var bairroPersistido = getBairro(nomeBairro);
        assertNotNull(bairroPersistido);
        assertEquals(novoBairro.getCustoPorMetroQuadrado(), bairroPersistido.getCustoPorMetroQuadrado());
    }

    @Test
    void alterarBairro_bairroInexistente_deveRetornarBadRequest() throws Exception {
        var nomeBairro = "nomeBairro";
        var bairro = new Bairro(nomeBairro, new BigDecimal("10.0"));

        mvc.perform(put(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(bairro)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode", is(400)))
                .andExpect(jsonPath("$.message", is("Bairro não existe!")));
    }

}
