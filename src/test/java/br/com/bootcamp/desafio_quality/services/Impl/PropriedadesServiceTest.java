package br.com.bootcamp.desafio_quality.services.Impl;
import br.com.bootcamp.desafio_quality.dto.ComodoRequestDTO;
import br.com.bootcamp.desafio_quality.dto.ComodoResponseDTO;
import br.com.bootcamp.desafio_quality.dto.PropriedadeDTO;
import br.com.bootcamp.desafio_quality.entity.Bairro;
import br.com.bootcamp.desafio_quality.exception.ConflictException;
import br.com.bootcamp.desafio_quality.exception.PropriedadeInexistenteException;
import br.com.bootcamp.desafio_quality.repository.IBairroRepository;
import br.com.bootcamp.desafio_quality.repository.IPropriedadeRepository;
import br.com.bootcamp.desafio_quality.service.impl.PropriedadesService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PropriedadesServiceTest {


    @Mock
    public IPropriedadeRepository propriedadeRepository;

    @Mock
    public IBairroRepository bairroRepository;

    @InjectMocks
    public PropriedadesService propriedadesService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Environment env;

    @BeforeEach
    void setUp() throws IOException {
        var filePath = env.getProperty("repository.propriedades.path", "src/test/resources/repository/propriedades.json");
        var file = new File(filePath);
        objectMapper.writeValue(file, new HashMap<>());
    }

    @Test
    void inserirPropriedade_NovaPropriedade_Sucesso() {

        List<ComodoRequestDTO> comodos = Arrays.asList(
                new ComodoRequestDTO("quarto", 10.0, 10.0),
                new ComodoRequestDTO("sala", 10.0, 5.0));
        PropriedadeDTO propriedadeDTO = new PropriedadeDTO("Propriedade do MELI1", "Centro", comodos);

        when(propriedadeRepository.persistePropriedade(propriedadeDTO.toEntity())).thenReturn(propriedadeDTO.toEntity());

        propriedadesService.inserirPropriedade(propriedadeDTO);
    }

    @Test
    void inserirPropriedade_JaExistente_Falha() {
        List<ComodoRequestDTO> comodos = Arrays.asList(
                new ComodoRequestDTO("quarto", 10.0, 10.0),
                new ComodoRequestDTO("sala", 10.0, 5.0));
        PropriedadeDTO propriedadeDTO = new PropriedadeDTO("Propriedade do MELI1", "Centro", comodos);

        when(propriedadeRepository.persistePropriedade(propriedadeDTO.toEntity())).thenThrow(new ConflictException("Propriedade já existe!"));
        assertThrows(ConflictException.class, () -> propriedadesService.inserirPropriedade(propriedadeDTO));
    }

    @Test
    void calcularAreaPropriedadeIgualA150m2() {
        List<ComodoRequestDTO> comodos = Arrays.asList(
                new ComodoRequestDTO("quarto", 10.0, 10.0),
                new ComodoRequestDTO("sala", 10.0, 5.0));
        PropriedadeDTO propriedadeDTO = new PropriedadeDTO("Propriedade do MELI1", "Centro", comodos);
        int id = 1;

        when(propriedadeRepository.buscarPropriedade(id)).thenReturn(Optional.of(propriedadeDTO.toEntity()));

        assertEquals(150.0, propriedadesService.calcularAreaPropriedade(id).getAreaTotal());
    }

    @Test
    void calcularAreaTotal_unicoComodo_5m2() {
        List<ComodoRequestDTO> comodos = Arrays.asList(
                new ComodoRequestDTO("quarto", 1.0, 5.0));
        PropriedadeDTO propriedadeDTO = new PropriedadeDTO("Propriedade do MELI1", "Centro", comodos);
        int id = 1;
        when(propriedadeRepository.buscarPropriedade(id)).thenReturn(Optional.of(propriedadeDTO.toEntity()));

        assertEquals(5.0, propriedadesService.calcularAreaPropriedade(id).getAreaTotal());
    }

    @Test
    void calcularAreaTotal_35m2() {
        List<ComodoRequestDTO> comodos = Arrays.asList(
                new ComodoRequestDTO("quarto", 1.0, 5.0),
                new ComodoRequestDTO("sala", 2.0, 5.0),
                new ComodoRequestDTO("cozinha", 4.0, 5.0));
        PropriedadeDTO propriedadeDTO = new PropriedadeDTO("Propriedade do MELI1", "Centro", comodos);
        int id = 1;
        when(propriedadeRepository.buscarPropriedade(id)).thenReturn(Optional.of(propriedadeDTO.toEntity()));

        assertEquals(35.0, propriedadesService.calcularAreaPropriedade(id).getAreaTotal());
    }

    @Test
    void calcularValor_bairroCentro_valor1_resultado150() {
        List<ComodoRequestDTO> comodos = Arrays.asList(
                new ComodoRequestDTO("quarto", 30.0, 5.0));
        PropriedadeDTO propriedadeDTO = new PropriedadeDTO("Propriedade do MELI1", "Centro", comodos);
        int id = 1;
        when(propriedadeRepository.buscarPropriedade(id)).thenReturn(Optional.of(propriedadeDTO.toEntity()));
        Bairro bairro = new Bairro("Centro", BigDecimal.valueOf(1));
        when(bairroRepository.buscarBairro("Centro")).thenReturn(Optional.of(bairro));
        assertEquals(BigDecimal.valueOf(150.0), propriedadesService.calcularValor(id).getValorTotal());
    }

    @Test
    void calcularValorTotal_bairroBGV_valor10_resultado1000() {
        List<ComodoRequestDTO> comodos = Arrays.asList(
                new ComodoRequestDTO("quarto", 10.0, 5.0),
                new ComodoRequestDTO("sala", 10.0, 5.0));

        PropriedadeDTO propriedadeDTO = new PropriedadeDTO("Propriedade do MELI1", "BGV", comodos);
        int id = 1;
        when(propriedadeRepository.buscarPropriedade(id)).thenReturn(Optional.of(propriedadeDTO.toEntity()));

        Bairro bairro = new Bairro("BGV", BigDecimal.valueOf(10));
        when(bairroRepository.buscarBairro("BGV")).thenReturn(Optional.of(bairro));

        assertEquals(BigDecimal.valueOf(500.0), propriedadesService.calcularValor(id).getValorTotal());
    }

    @Test
    void calcularAreaComodos_resultado150() {

        List<ComodoRequestDTO> comodos = Arrays.asList(new ComodoRequestDTO("quarto", 10.0, 10.0),
                                                       new ComodoRequestDTO("sala", 10.0, 5.0));
        PropriedadeDTO propriedadeDTO = new PropriedadeDTO("Propriedade do MELI1", "Centro", comodos);
        int id = 1;

        when(propriedadeRepository.buscarPropriedade(id)).thenReturn(Optional.of(propriedadeDTO.toEntity()));

        List<ComodoResponseDTO> comodoResponseDTOS = Arrays.asList(new ComodoResponseDTO("quarto", 100.0),
                                                                   new ComodoResponseDTO("sala", 50.0));

        assertEquals(comodoResponseDTOS, propriedadesService.calcularAreaComodos(id));
    }

    @Test
    void calcularAreaComodos_comodoUnico_resultado100() {

        List<ComodoRequestDTO> comodos = Arrays.asList(new ComodoRequestDTO("quarto", 10.0, 10.0));
        PropriedadeDTO propriedadeDTO = new PropriedadeDTO("Propriedade do MELI1", "Centro", comodos);
        int id = 1;

        when(propriedadeRepository.buscarPropriedade(id)).thenReturn(Optional.of(propriedadeDTO.toEntity()));

        List<ComodoResponseDTO> comodoResponseDTOS = Arrays.asList(new ComodoResponseDTO("quarto", 100.0));

        assertEquals(comodoResponseDTOS, propriedadesService.calcularAreaComodos(id));
    }

    @Test
    void buscarMaiorComodo_unicoComodo_resultadoQuarto() {
        List<ComodoRequestDTO> comodos = Arrays.asList(new ComodoRequestDTO("quarto", 10.0, 10.0));
        PropriedadeDTO propriedadeDTO = new PropriedadeDTO("Propriedade do MELI1", "Centro", comodos);
        int id = 1;

        when(propriedadeRepository.buscarPropriedade(id)).thenReturn(Optional.of(propriedadeDTO.toEntity()));

        ComodoResponseDTO comodoResponseDTOS = new ComodoResponseDTO("quarto", 100.0);

        assertEquals(comodoResponseDTOS.getNome(), propriedadesService.buscarMaiorComodo(id).getNome());
    }

    @Test
    void buscarMaiorComodo_empate_retornaOrdemInsercao() {
        List<ComodoRequestDTO> comodos = Arrays.asList(new ComodoRequestDTO("quarto", 10.0, 10.0),
                                                       new ComodoRequestDTO("sala", 10.0, 10.0));
        PropriedadeDTO propriedadeDTO = new PropriedadeDTO("Propriedade do MELI1", "Centro", comodos);
        int id = 1;

        when(propriedadeRepository.buscarPropriedade(id)).thenReturn(Optional.of(propriedadeDTO.toEntity()));

        ComodoResponseDTO comodoResponseDTOS = new ComodoResponseDTO("quarto", 100.0);

        assertEquals(comodoResponseDTOS.getNome(), propriedadesService.buscarMaiorComodo(id).getNome());
    }

    @Test
    void buscarMaiorComodo_diferentes_resultadoCozinha() {
        List<ComodoRequestDTO> comodos = Arrays.asList(new ComodoRequestDTO("quarto", 10.0, 10.0),
                                                       new ComodoRequestDTO("sala", 10.0, 10.0),
                                                       new ComodoRequestDTO("cozinha", 20.0, 20.0));
        PropriedadeDTO propriedadeDTO = new PropriedadeDTO("Propriedade do MELI1", "Centro", comodos);
        int id = 1;

        when(propriedadeRepository.buscarPropriedade(id)).thenReturn(Optional.of(propriedadeDTO.toEntity()));

        ComodoResponseDTO comodoResponseDTOS = new ComodoResponseDTO("cozinha", 400.0);

        assertEquals(comodoResponseDTOS.getNome(), propriedadesService.buscarMaiorComodo(id).getNome());
    }

    @Test
    void buscarPropriedadePorId_existente_Sucesso() {
        List<ComodoRequestDTO> comodos = Arrays.asList(new ComodoRequestDTO("quarto", 10.0, 10.0),
                new ComodoRequestDTO("sala", 10.0, 10.0),
                new ComodoRequestDTO("cozinha", 20.0, 20.0));
        PropriedadeDTO propriedadeDTO = new PropriedadeDTO("Propriedade do MELI1", "Centro", comodos);
        int id = 1;

        when(propriedadeRepository.buscarPropriedade(id)).thenReturn(Optional.of(propriedadeDTO.toEntity()));

        assertEquals(propriedadeDTO, propriedadesService.buscarPropriedadePorId(id));
    }

    @Test
    void buscarPropriedadePorId_naoExistente_Falha() { //TODO criar exception handler para propriedade nao existente
        assertThrows(PropriedadeInexistenteException.class, () -> propriedadesService.buscarPropriedadePorId(10005));
    }
 }
