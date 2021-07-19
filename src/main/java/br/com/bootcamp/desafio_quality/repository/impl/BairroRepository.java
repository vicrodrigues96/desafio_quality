package br.com.bootcamp.desafio_quality.repository.impl;

import br.com.bootcamp.desafio_quality.entity.Bairro;
import br.com.bootcamp.desafio_quality.exception.BairroInexistenteException;
import br.com.bootcamp.desafio_quality.exception.ConflictException;
import br.com.bootcamp.desafio_quality.exception.PersistenceException;
import br.com.bootcamp.desafio_quality.repository.IBairroRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class BairroRepository implements IBairroRepository {

    private final File FILE;
    private final ObjectMapper mapper;

    @Autowired
    public BairroRepository(ObjectMapper mapper, Environment env) {
        this.mapper = mapper;
        var filePath = env.getProperty("repository.bairros.path", "src/main/resources/repository/bairros.json");
        this.FILE = new File(filePath);
    }

    @Override
    public Optional<Bairro> buscarBairro(String nome) {
        return getList().stream().filter(b -> b.getNome().equalsIgnoreCase(nome)).findFirst();
    }

    @Override
    public Bairro persisteBairro(Bairro bairro) {

        List<Bairro> bairros = this.getList();
        validarBairroExistente(bairro, bairros);

        bairros.add(bairro);

        persistirJson(bairros);

        return bairro;
    }

    @Override
    public void deletarBairro(String nome) {
        List<Bairro> bairros = this.getList();
        boolean foiRemovido = bairros.removeIf(b -> b.getNome().equalsIgnoreCase(nome));
        if (!foiRemovido) {
            throw new BairroInexistenteException("Bairro a ser deletado não encontrado");
        }

        persistirJson(bairros);
    }

    @Override
    public Bairro alterarBairro(Bairro bairro) {
        List<Bairro> bairros = this.getList();
        int indice = bairros.indexOf(bairro);

        if (indice < 0) {
            throw new BairroInexistenteException("Bairro não existe!");
        }

        bairros.set(indice, bairro);
        persistirJson(bairros);
        return bairro;
    }

    private List<Bairro> getList() {
        List<Bairro> bairros;
        try {
            bairros = mapper.readValue(FILE, new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new PersistenceException("Não foi possível obter os bairros.");
        }
        return bairros;
    }

    private void persistirJson(List<Bairro> bairros) {
        try {
            mapper.writeValue(FILE, bairros);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistenceException("Não foi possível salvar o bairro.");
        }
    }

    private void validarBairroExistente(Bairro bairro, List<Bairro> bairros) {
        if (bairros
                .stream()
                .anyMatch(b -> Objects.equals(b.getNome().toLowerCase(), bairro.getNome().toLowerCase()))) {
            throw new ConflictException("Bairro já existe!");
        }
    }

}
