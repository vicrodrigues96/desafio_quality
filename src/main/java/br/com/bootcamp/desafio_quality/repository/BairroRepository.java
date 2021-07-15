package br.com.bootcamp.desafio_quality.repository;

import br.com.bootcamp.desafio_quality.entity.Bairro;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class BairroRepository implements IBairroRepository{
    
    private static final File FILE = new File("src/main/resources/repository/bairros.json");
    private final ObjectMapper mapper;

    @Autowired
    public BairroRepository(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public boolean existe(String nome) {
       return getList().stream().anyMatch(b -> Objects.equals(b.getNome(),nome));
    }

    private List<Bairro> getList(){
        List<Bairro> bairros = new ArrayList<>();
        try {
            bairros = mapper.readValue(FILE, new TypeReference<>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bairros;
    }
}
