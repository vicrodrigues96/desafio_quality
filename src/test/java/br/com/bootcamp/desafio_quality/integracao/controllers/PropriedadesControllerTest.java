package br.com.bootcamp.desafio_quality.integracao.controllers;

import br.com.bootcamp.desafio_quality.dto.ComodoRequestDTO;
import br.com.bootcamp.desafio_quality.dto.PropriedadeDTO;
import br.com.bootcamp.desafio_quality.entity.Propriedade;
import br.com.bootcamp.desafio_quality.exception.PersistenceException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PropriedadesControllerTest {

    @Autowired
    private MockMvc mock;

    @Autowired
    private ObjectMapper mapper;

    private String filePath;
    private File file;

    @Autowired
    public PropriedadesControllerTest(Environment env) {
        this.filePath = env.getProperty("repository.propriedades.path",
                "src/test/resources/repository/propriedades.json");
        this.file = new File(filePath);
    }

    @BeforeEach
    void setUp() throws IOException {
        mapper.writeValue(this.file, new HashMap<>());
    }

    @Test
    public void devePersistirUmaPropriedade() throws Exception {
        String payload = mapper.writeValueAsString(retornaUmJsonPropriedade());

        mock.perform(post("/propriedades/")
                .contentType("application/json")
                .content(payload))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("*://*/propriedades/*"));
    }

    @Test
    public void NaoDevePersistirUmaPropriedadeExistente() throws Exception {
        salvaPropriedadeTabela(100, retornaUmJsonPropriedade().toEntity());

        String payload = mapper.writeValueAsString(retornaUmJsonPropriedade());

        mock.perform(post("/propriedades/")
                .contentType("application/json")
                .content(payload))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.statusCode", is(409)))
                .andExpect(jsonPath("$.message", is("Propriedade já existe!")));
    }

    @Test
    public void deveListarUmaPropriedadeCriada() throws Exception {
        salvaPropriedadeTabela(100, retornaUmJsonPropriedade().toEntity());

        mock.perform(get("/propriedades/{id}", "100"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.nome", is("Propriedade de Teste")))
                .andExpect(jsonPath("$.bairro", is("Centro")));
    }

    @Test
    public void naoDeveListarUmaPropriedadeInexistente() throws Exception {

        mock.perform(get("/propriedades/{id}", "100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode", is(400)))
                .andExpect(jsonPath("$.message", is("Propriedade inexistente!")));
    }

    @Test
    public void deveCalcularAreaTotal() throws Exception {
        salvaPropriedadeTabela(100, retornaUmJsonPropriedade().toEntity());

        mock.perform(get("/propriedades/{id}/area", "100"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.areaTotal", is(67.0)));
    }

    @Test
    public void naoDeveCalcularAreaDePropriedadeInexistente() throws Exception {

        mock.perform(get("/propriedades/{id}/area", "100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode", is(400)))
                .andExpect(jsonPath("$.message", is("Propriedade inexistente!")));
    }

    @Test
    public void deveCalcularValorTotal() throws Exception {
        salvaPropriedadeTabela(100, retornaUmJsonPropriedade().toEntity());

        mock.perform(get("/propriedades/{id}/valor", "100"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.valorTotal", is(2010.0)));
    }

    @Test
    public void naoDeveCalcularValorDePropriedadeInexistente() throws Exception {

        mock.perform(get("/propriedades/{id}/area", "100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode", is(400)))
                .andExpect(jsonPath("$.message", is("Propriedade inexistente!")));
    }

    @Test
    public void naoDeveCalcularValorTotalQuandoNaoExistirBairro() throws Exception {
        salvaPropriedadeTabela(
                100, new Propriedade("Propriedade sem bairro", "bairroInexistente", null)
        );

        mock.perform(get("/propriedades/{id}/valor", "100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode", is(400)))
                .andExpect(jsonPath("$.message", is("Bairro não existe!")));
    }

    @Test
    public void deveRetornarOMaiorComodo() throws Exception {
        salvaPropriedadeTabela(100, retornaUmJsonPropriedade().toEntity());

        mock.perform(get("/propriedades/{id}/comodos/maior", "100"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.nome", is("Sala")))
                .andExpect(jsonPath("$.area", is(30.0)));
    }

    @Test
    public void naoDeveRetornarMaiorComodoDePropriedadeInexistente() throws Exception {

        mock.perform(get("/propriedades/{id}/area", "100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode", is(400)))
                .andExpect(jsonPath("$.message", is("Propriedade inexistente!")));
    }

    @Test
    public void deveRetornarUmaListaDeComodosComArea() throws Exception {
        salvaPropriedadeTabela(100, retornaUmJsonPropriedade().toEntity());

        mock.perform(get("/propriedades/{id}/comodos/area", "100"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.[0].nome", is("Cozinha")))
                .andExpect(jsonPath("$.[0].area", is(25.0)))
                .andExpect(jsonPath("$.[1].area", is(30.0)))
                .andExpect(jsonPath("$.[2].area", is(12.0)));
    }

    @Test
    public void naoDeveRetornarListaDeComodosDePropriedadeInexistente() throws Exception {

        mock.perform(get("/propriedades/{id}/area", "100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode", is(400)))
                .andExpect(jsonPath("$.message", is("Propriedade inexistente!")));
    }

    @Test
    public void naoDevePersistirPropriedadeComNomeIniciandoComLetraMinuscula() throws Exception {
        List<ComodoRequestDTO> comodosDto = retornaListaDomodosDtos();
        PropriedadeDTO propriedadeDTO = new PropriedadeDTO("nomeLetraMinuscula", "Centro", comodosDto);
        String payload = mapper.writeValueAsString(propriedadeDTO);

        mock.perform(post("/propriedades/")
                .contentType("application/json")
                .content(payload))
                .andExpect(jsonPath("$.statusCode", is(400)))
                .andExpect(jsonPath("$.message", is("nome:  O nome da propriedade deve ser começar com uma letra maiuscula")));
    }

    @Test
    public void naoDeveCadastrarUmaPropriedadeSemComodos() throws Exception {
        PropriedadeDTO propriedadeDTO = new PropriedadeDTO("Imovel para Testes", "Centro", null);
        String payload = mapper.writeValueAsString(propriedadeDTO);

        mock.perform(post("/propriedades/")
                .contentType("application/json")
                .content(payload))
                .andExpect(jsonPath("$.statusCode", is(400)))
                .andExpect(jsonPath("$.message", is("comodos: A lista de comodos não pode ser nula")));
    }

    @Test
    public void naoDevePersistirUmaPropriedadeQuandoNaoExistirOBairro() throws Exception {
        List<ComodoRequestDTO> comodosDto = retornaListaDomodosDtos();
        PropriedadeDTO propriedadeDTO = new PropriedadeDTO("Bairro Inexistente", "Bairro Inexistente Teste", comodosDto);
        String payload = mapper.writeValueAsString(propriedadeDTO);

        mock.perform(post("/propriedades/")
                .contentType("application/json")
                .content(payload))
                .andExpect(jsonPath("$.statusCode", is(400)))
                .andExpect(jsonPath("$.message", is("Não foi possível cadastrar a propriedade, bairro não existe.")));
    }

    @Test
    public void naoDevePersistirUmaPropriedadeComComodosComLetraMinuscula() throws Exception {
        List<ComodoRequestDTO> comodosDto = Arrays.asList(new ComodoRequestDTO("cozinha", 10.0, 10.0));
        PropriedadeDTO propriedadeDTO = new PropriedadeDTO("Bairro Inexistente", "Centro", comodosDto);
        String payload = mapper.writeValueAsString(propriedadeDTO);

        mock.perform(post("/propriedades/")
                .contentType("application/json")
                .content(payload))
                .andExpect(jsonPath("$.statusCode", is(400)))
                .andExpect(jsonPath("$.message", containsString("O nome do comodo deve ser começar com uma letra maiuscula")));
    }

    public PropriedadeDTO retornaUmJsonPropriedade() {
        List<ComodoRequestDTO> comodosDto = retornaListaDomodosDtos();

        return new PropriedadeDTO("Propriedade de Teste", "Centro", comodosDto);
    }

    public List<ComodoRequestDTO> retornaListaDomodosDtos() {

        return Arrays.asList(
                new ComodoRequestDTO("Cozinha", 5.0, 5.0),
                new ComodoRequestDTO("Sala", 6.0, 5.0),
                new ComodoRequestDTO("Quarto", 4.0, 3.0)
        );
    }

    private void salvaPropriedadeTabela(Integer id, Propriedade propriedade) {
        HashMap<Integer, Propriedade> propriedades = this.getPropriedades();

        propriedade.setId(id);
        propriedades.put(id, propriedade);

        persistirJson(propriedades);
    }

    private void persistirJson(HashMap<Integer, Propriedade> propriedades) {
        try {
            mapper.writeValue(this.file, propriedades);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistenceException("Não foi possível salvar o propriedade.");
        }
    }

    private HashMap<Integer, Propriedade> getPropriedades() {
        HashMap<Integer, Propriedade> propriedades;
        try {
            propriedades = mapper.readValue(this.file, new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new PersistenceException("Não foi possível obter os propriedades.");
        }
        return propriedades;
    }
}
