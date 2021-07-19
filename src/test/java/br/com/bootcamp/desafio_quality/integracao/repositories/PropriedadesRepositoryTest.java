package br.com.bootcamp.desafio_quality.integracao.repositories;

import br.com.bootcamp.desafio_quality.entity.Propriedade;
import br.com.bootcamp.desafio_quality.exception.ConflictException;
import br.com.bootcamp.desafio_quality.exception.PersistenceException;
import br.com.bootcamp.desafio_quality.integracao.IntegrationTestBase;
import br.com.bootcamp.desafio_quality.repository.IPropriedadeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class PropriedadesRepositoryTest extends IntegrationTestBase {

    @Autowired
    private IPropriedadeRepository propriedadeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public PropriedadesRepositoryTest(Environment env) {
        super(env);
    }

    @BeforeEach
    void setUp() throws IOException {
        objectMapper.writeValue(propriedadesFile, new HashMap<>());
    }

    @Test
    void buscarPropriedade_bancoVazio_deveRetornarOptionNull() {
        var propriedade = propriedadeRepository.buscarPropriedade(1);

        assertTrue(propriedade.isEmpty());
    }

    @Test
    void buscarPropriedade_propInexistente_deveRetornarOptionNull() {
        var propriedade = new Propriedade("Prop1", "Bairro1", null);
        persistePropriedade(1, propriedade);

        var propriedadeOptional = propriedadeRepository.buscarPropriedade(2);

        assertTrue(propriedadeOptional.isEmpty());
    }

    @Test
    void buscarPropriedade_propExistente_deveRetornarPropriedade() {
        var propriedade1 = new Propriedade("Prop1", "Bairro1", null);
        var propriedade2 = new Propriedade("Prop2", "Bairro2", null);

        var id2 = 2;
        persistePropriedade(1, propriedade1);
        persistePropriedade(id2, propriedade2);

        var propriedadeOptional = propriedadeRepository.buscarPropriedade(id2);

        assertTrue(propriedadeOptional.isPresent());
        assertEquals(propriedade2, propriedadeOptional.get());
    }

    @Test
    void buscarPropriedade_erroBanco_deveRetornarExcessao() {
        assertDoesNotThrow(() -> objectMapper.writeValue(propriedadesFile, ""));

        PersistenceException persistenceException = assertThrows(PersistenceException.class,
                                                                    () -> propriedadeRepository.buscarPropriedade(1));
        assertEquals("Não foi possível obter os propriedades.", persistenceException.getMessage());
    }

    @Test
    void persistePropriedade_bancoVazio_devePersistir() {
        var propriedade = new Propriedade("Prop1", "Bairro1", null);

        var propriedadePersistida = propriedadeRepository.persistePropriedade(propriedade);

        assertNotNull(propriedadePersistida);
        assertNotNull(propriedadePersistida.getId());
        assertTrue(existePropriedade(propriedade.getId()));
    }

    @Test
    void persistePropriedade_bancoPopulado_devePersistir() {
        var propriedade1 = new Propriedade("Prop1", "Bairro1", null);
        var propriedade2 = new Propriedade("Prop2", "Bairro2", null);
        persistePropriedade(1, propriedade2);

        var propriedadePersistida = propriedadeRepository.persistePropriedade(propriedade1);

        assertNotNull(propriedadePersistida);
        assertNotNull(propriedadePersistida.getId());
        assertTrue(existePropriedade(propriedade1.getId()));
    }

    @Test
    void persistePropriedade_comIdExistente_devePersistirNovoId() {
        var propriedade1 = new Propriedade("Prop1", "Bairro1", null);
        var id = 10;
        propriedade1.setId(id);

        var propriedadePersistida = propriedadeRepository.persistePropriedade(propriedade1);

        assertNotNull(propriedadePersistida);
        assertNotNull(propriedadePersistida.getId());
        assertTrue(existePropriedade(propriedadePersistida.getId()));
        assertNotEquals(id, propriedadePersistida.getId());
    }

    @Test
    void persistePropriedade_propJaPersistida_deveRetornarExcessao() {
        var propriedade1 = new Propriedade("Prop1", "Bairro1", null);
        persistePropriedade(1, propriedade1);

        ConflictException conflictException = assertThrows(ConflictException.class,
                                                             () -> propriedadeRepository.persistePropriedade(propriedade1));

        assertEquals("Propriedade já existe!", conflictException.getMessage());
    }

}
