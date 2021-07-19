package br.com.bootcamp.desafio_quality.integracao;

import br.com.bootcamp.desafio_quality.entity.Bairro;
import br.com.bootcamp.desafio_quality.entity.Propriedade;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

public abstract class IntegrationTestBase {

    @Autowired
    protected ObjectMapper objectMapper;

    protected final File bairrosFile;
    protected final File propriedadesFile;

    public IntegrationTestBase(Environment env) {
        var fileBairrosPath = env.getProperty("repository.bairros.path",
                "src/test/resources/repository/bairros.json");
        bairrosFile = new File(fileBairrosPath);

        var filePropriedadesPath = env.getProperty("repository.propriedades.path",
                "src/test/resources/repository/propriedades.json");
        propriedadesFile = new File(filePropriedadesPath);
    }

    @BeforeEach
    void setUp() throws IOException {
        objectMapper.writeValue(bairrosFile, new ArrayList<>());
        objectMapper.writeValue(propriedadesFile, new HashMap<>());
    }

    protected Bairro getBairro(String nomeBairro) {
        return getBairros()
                .stream()
                .filter(b -> Objects.equals(b.getNome(), nomeBairro))
                .findFirst()
                .orElse(null);
    }

    protected void persisteBairro(Bairro bairro) {
        List<Bairro> bairros = this.getBairros();
        bairros.add(bairro);
        try {
            objectMapper.writeValue(bairrosFile, bairros);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Não foi possível salvar o bairro.");
        }
    }

    protected List<Bairro> getBairros() {
        try {
            return objectMapper.readValue(bairrosFile, new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            fail("Não foi possível obter os bairros.");
        }
        return null;
    }

    protected boolean existeBairro(String nome) {
        return getBairros().stream().anyMatch(b -> Objects.equals(b.getNome(), nome));
    }

    protected void persistePropriedade(Integer id, Propriedade propriedade) {
        HashMap<Integer, Propriedade> propriedades = this.getPropriedades();

        propriedade.setId(id);
        propriedades.put(id, propriedade);

        try {
            objectMapper.writeValue(propriedadesFile, propriedades);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Não foi possível salvar o propriedade.");
        }
    }

    protected HashMap<Integer, Propriedade> getPropriedades() {
        try {
            return objectMapper.readValue(propriedadesFile, new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            fail("Não foi possível obter os propriedades.");
        }
        return null;
    }

    protected boolean existePropriedade(int id) {
        return getPropriedades().containsKey(id);
    }
}
