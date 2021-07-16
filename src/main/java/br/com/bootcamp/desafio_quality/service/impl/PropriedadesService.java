package br.com.bootcamp.desafio_quality.service.impl;

import br.com.bootcamp.desafio_quality.dto.*;
import br.com.bootcamp.desafio_quality.entity.Bairro;
import br.com.bootcamp.desafio_quality.entity.Comodo;
import br.com.bootcamp.desafio_quality.entity.Propriedade;
import br.com.bootcamp.desafio_quality.exception.BairroInexistenteException;
import br.com.bootcamp.desafio_quality.exception.PropriedadeInexistenteException;
import br.com.bootcamp.desafio_quality.repository.IBairroRepository;
import br.com.bootcamp.desafio_quality.repository.IPropriedadeRepository;
import br.com.bootcamp.desafio_quality.service.IPropriedadesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PropriedadesService implements IPropriedadesService {

    private final IBairroRepository bairroRepository;
    private final IPropriedadeRepository propriedadeRepository;

    @Autowired
    public PropriedadesService(IBairroRepository bairroRepository, IPropriedadeRepository propriedadeRepository) {
        this.bairroRepository = bairroRepository;
        this.propriedadeRepository = propriedadeRepository;
    }

    @Override
    public Integer inserirPropriedade(PropriedadeDTO propriedadeDto) {
        Propriedade propriedade = propriedadeDto.toEntity();
        existeBairro(propriedade.getBairro());

        return this.propriedadeRepository.persistePropriedade(propriedade).getId();
    }

    @Override
    public AreaTotalDTO calcularAreaPropriedade(int id) {
        Propriedade propriedade = buscarPropriedade(id);

        return new AreaTotalDTO(calcularAreaTotal(propriedade.getComodos()));
    }

    @Override
    public ValorPropriedadeDTO calcularValor(int id) {
        Propriedade propriedade = buscarPropriedade(id);
        Bairro bairro = buscarBairro(propriedade.getBairro());

        double areaTotal = calcularAreaTotal(propriedade.getComodos());
        BigDecimal valorMetroQuadrado = bairro.getCustoPorMetroQuadrado();

        return new ValorPropriedadeDTO(calcularValorTotal(areaTotal, valorMetroQuadrado));
    }

    @Override
    public List<ComodoResponseDTO> calcularAreaComodos(int id) {
        Propriedade propriedade = buscarPropriedade(id);

        return propriedade.getComodos()
                .stream()
                .map(Comodo::toComodoResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ComodoResponseDTO buscarMaiorComodo(int id) {
        Propriedade propriedade = buscarPropriedade(id);
        Comodo comodo = getMaiorComodo(propriedade.getComodos());

        return comodo.toComodoResponseDto();
    }

    @Override
    public PropriedadeDTO buscarPropriedadePorId(int id) {
        return buscarPropriedade(id).toDto();
    }

    private void existeBairro(String nome) {
        if (Objects.isNull(bairroRepository.buscarBairro(nome))) {
            throw new BairroInexistenteException("Não foi possível cadastrar a propriedade, bairro não existe.");
        }
    }

    private double calcularAreaTotal(List<Comodo> comodos) {
        return comodos.stream()
                .mapToDouble(Comodo::getArea)
                .sum();
    }

    private Propriedade buscarPropriedade(int id) {
        return this.propriedadeRepository
                .buscarPropriedade(id)
                .orElseThrow(() -> new PropriedadeInexistenteException("Propriedade inexistente!"));
    }

    private Bairro buscarBairro(String nome) {
        return this.bairroRepository
                .buscarBairro(nome)
                .orElseThrow(() -> new BairroInexistenteException("Bairro não existe!"));
    }

    private BigDecimal calcularValorTotal(double areaTotal, BigDecimal precoMetroQuadrado) {
        return precoMetroQuadrado.multiply(BigDecimal.valueOf(areaTotal));
    }

    private Comodo getMaiorComodo(List<Comodo> comodos) {
        return comodos.stream()
                .max(Comparator.comparing(Comodo::getArea))
                .orElse(null);
    }
}
