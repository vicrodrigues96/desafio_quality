package br.com.bootcamp.desafio_quality.integracao.controllers;

import br.com.bootcamp.desafio_quality.entity.Bairro;
import br.com.bootcamp.desafio_quality.exception.PersistenceException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class BairrosControllerTest {

    public static final String BASE_PATH = "/bairros";
    public static final String PATH_NAME = BASE_PATH + "/{name}";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final File file;

    @Autowired
    public BairrosControllerTest(Environment env) {
        var filePath = env.getProperty("repository.bairros.path", "src/test/resources/repository/bairros.json");
        file = new File(filePath);
    }

    @BeforeEach
    void setUp() throws IOException {
        objectMapper.writeValue(file, new ArrayList<>());
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

    private Bairro getBairro(String nomeBairro) {
        return getBairros()
                .stream()
                .filter(b -> Objects.equals(b.getNome(), nomeBairro))
                .findFirst()
                .orElse(null);
    }

    private void persisteBairro(Bairro bairro) {
        List<Bairro> bairros = this.getBairros();
        bairros.add(bairro);
        persistirJson(bairros);
    }

    private List<Bairro> getBairros() {
        List<Bairro> bairros;
        try {
            bairros = objectMapper.readValue(file, new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new PersistenceException("Não foi possível obter os bairros.");
        }
        return bairros;
    }

    private void persistirJson(List<Bairro> bairros) {
        try {
            objectMapper.writeValue(file, bairros);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistenceException("Não foi possível salvar o bairro.");
        }
    }

    private boolean existeBairro(String nome) {
        return getBairros().stream().anyMatch(b -> Objects.equals(b.getNome(), nome));
    }

}
