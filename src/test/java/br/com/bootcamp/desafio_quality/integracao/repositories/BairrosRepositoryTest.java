package br.com.bootcamp.desafio_quality.integracao.repositories;

import br.com.bootcamp.desafio_quality.entity.Bairro;
import br.com.bootcamp.desafio_quality.exception.BairroInexistenteException;
import br.com.bootcamp.desafio_quality.exception.ConflictException;
import br.com.bootcamp.desafio_quality.exception.PersistenceException;
import br.com.bootcamp.desafio_quality.repository.IBairroRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class BairrosRepositoryTest {

    @Autowired
    private IBairroRepository bairroRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Environment env;

    private File file;

    @BeforeEach
    void setUp() throws IOException {
        var filePath = env.getProperty("repository.bairros.path", "src/test/resources/repository/bairros.json");
        file = new File(filePath);
        objectMapper.writeValue(file, new ArrayList<>());
    }

    @Test
    void buscarBairro_bancoVazio_deveRetornarOptionNull(){
        var bairroBuscado = bairroRepository.buscarBairro("Bairro1");

        assertTrue(bairroBuscado.isEmpty());
    }

    @Test
    void buscarBairro_bairroInexistente_deveRetornarOptionNull(){
        var bairroCriado = new Bairro("Bairro1", new BigDecimal(15));

        salvaBairro(bairroCriado);

        var bairroBuscado = bairroRepository.buscarBairro("Bairro2");

        assertTrue(bairroBuscado.isEmpty());
    }

    @Test
    void buscarBairro_bairroExistente_deveRetornarBairroBuscado(){
        var nomeDoBairroBuscado = "Bairro2";
        var bairroCriado1 = new Bairro("Bairro1", new BigDecimal(15));
        var bairroCriado2 = new Bairro(nomeDoBairroBuscado, new BigDecimal(20));

        salvaBairro(bairroCriado1);
        salvaBairro(bairroCriado2);

        var bairroBuscado = bairroRepository.buscarBairro(nomeDoBairroBuscado);

        assertTrue(bairroBuscado.isPresent());
        assertEquals(bairroCriado2, bairroBuscado.get());
    }

    @Test
    void buscarBairro_erroBanco_deveRetornarExcessao() {
        assertDoesNotThrow(() -> objectMapper.writeValue(file, ""));

        PersistenceException persistenceException = assertThrows(PersistenceException.class,
                () -> bairroRepository.buscarBairro("Bairro1"));
        assertEquals("Não foi possível obter os bairros.", persistenceException.getMessage());
    }

    @Test
    void persisteBairro_bancoVazio_devePersistir(){
        var nomeDoBairroCriado = "Bairro1";
        var bairroCriado = new Bairro(nomeDoBairroCriado, new BigDecimal(15));

        var bairroPersistido = bairroRepository.persisteBairro(bairroCriado);

        assertNotNull(bairroPersistido);
        assertNotNull(bairroPersistido.getNome());
        assertTrue(existeBairro(nomeDoBairroCriado));
    }


    @Test
    void persisteBairro_bancoPopulado_devePersistir() {
        var nomeDoBairroCriado2 = "Bairro2";
        var bairroCriado1 = new Bairro("Bairro1", new BigDecimal(15));
        var bairroCriado2 = new Bairro(nomeDoBairroCriado2, new BigDecimal(20));

        salvaBairro(bairroCriado1);

        var bairroPersistido = bairroRepository.persisteBairro(bairroCriado2);

        assertNotNull(bairroPersistido);
        assertNotNull(bairroPersistido.getNome());
        assertTrue(existeBairro(nomeDoBairroCriado2));
    }

    @Test
    void persisteBairro_bairroJaPersistido_deveRetornarExcessao() {
        var bairroCriado1 = new Bairro("Bairro1", new BigDecimal(15));

        salvaBairro(bairroCriado1);

        ConflictException conflictException = assertThrows(ConflictException.class,
                () -> bairroRepository.persisteBairro(bairroCriado1));

        assertEquals("Bairro já existe!", conflictException.getMessage());
    }

    @Test
    void deletaBairro_bancoComBairro_deveDeletar(){
        var nomeDoBairroCriado = "Bairro1";
        var bairroCriado1 = new Bairro(nomeDoBairroCriado, new BigDecimal(15));

        salvaBairro(bairroCriado1);

        bairroRepository.deletarBairro(nomeDoBairroCriado);

        assertFalse(existeBairro(nomeDoBairroCriado));
    }

    @Test
    void alteraBairro_bancoVazio_deveRetornarExcessao(){
        var bairroCriado1 = new Bairro("Bairro1", new BigDecimal(15));

        BairroInexistenteException bairroInexistenteException = assertThrows(BairroInexistenteException.class,
                () -> bairroRepository.alterarBairro(bairroCriado1));

        assertEquals("Bairro não existe!", bairroInexistenteException.getMessage());
    }

    @Test
    void alteraBairro_bancoPopulado_deveRetornarExcessao(){
        var bairroCriado1 = new Bairro("Bairro1", new BigDecimal(15));
        var bairroCriado2 = new Bairro("Bairro2", new BigDecimal(20));

        salvaBairro(bairroCriado1);

        BairroInexistenteException bairroInexistenteException = assertThrows(BairroInexistenteException.class,
                () -> bairroRepository.alterarBairro(bairroCriado2));

        assertEquals("Bairro não existe!", bairroInexistenteException.getMessage());
    }

    @Test
    void alteraBairro_bairroExiste_deveAlterar(){
        var valor1 = new BigDecimal(15);
        var bairroCriado1 = new Bairro("Bairro1", valor1);

        salvaBairro(bairroCriado1);

        var valor2 = new BigDecimal(25);
        var bairroCriado2 = new Bairro("Bairro1", valor2);

        var bairroAlterado = bairroRepository.alterarBairro(bairroCriado2);

        assertNotEquals(bairroAlterado.getCustoPorMetroQuadrado(),bairroCriado1.getCustoPorMetroQuadrado());
        assertEquals(bairroAlterado,bairroCriado2);
    }

    private void salvaBairro(Bairro bairro) {
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

    private boolean existeBairro(String nome) {
        return getBairros()
                .stream()
                .anyMatch(b -> Objects.equals(b.getNome().toLowerCase(), nome.toLowerCase()));
    }

    private void persistirJson(List<Bairro> bairros) {
        try {
            objectMapper.writeValue(file, bairros);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistenceException("Não foi possível salvar o bairro.");
        }
    }
}
