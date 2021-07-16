package br.com.bootcamp.desafio_quality.repository.impl;

import br.com.bootcamp.desafio_quality.entity.Propriedade;
import br.com.bootcamp.desafio_quality.exception.ConflictException;
import br.com.bootcamp.desafio_quality.exception.PersistenceException;
import br.com.bootcamp.desafio_quality.repository.IPropriedadeRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Repository
public class PropriedadeRepositoryImpl implements IPropriedadeRepository {

    private static final File FILE = new File("src/main/resources/repository/propriedades.json");
    private final ObjectMapper mapper;
    
    private final static Random random = new Random();

    @Autowired
    public PropriedadeRepositoryImpl(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Propriedade persistePropriedade(Propriedade propriedade) {
        HashMap<Integer, Propriedade> propriedades = this.getPropriedades();
        validarPropriedadeExistente(propriedade, propriedades);

        var id = random.nextInt();
        propriedade.setId(id);

        propriedades.put(id, propriedade);

        persistirJson(propriedades);

        return propriedade;
    }

    @Override
    public Optional<Propriedade> buscarPropriedade(Integer id) {
        return Optional.of(getPropriedades().get(id));
    }

    private HashMap<Integer, Propriedade> getPropriedades() {
        HashMap<Integer, Propriedade> propriedades;
        try {
            propriedades = mapper.readValue(FILE, new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new PersistenceException("Não foi possível obter os propriedades.");
        }
        return propriedades;
    }

    private void persistirJson(HashMap<Integer, Propriedade> propriedades) {
        try {
            mapper.writeValue(FILE, propriedades);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistenceException("Não foi possível salvar o propriedade.");
        }
    }

    private void validarPropriedadeExistente(Propriedade propriedade, HashMap<Integer, Propriedade> propriedades) { //TODO passar a usar ID e generalizar para usar em Bairro
        if (propriedades
                .values()
                .stream()
                .anyMatch(p -> Objects.equals(p.getNome().toLowerCase(), propriedade.getNome().toLowerCase()))) {
            throw new ConflictException("Propriedade já existe!");
        }
    }
}
