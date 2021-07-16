package br.com.bootcamp.desafio_quality.entity;

import br.com.bootcamp.desafio_quality.dto.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Propriedade {
    private Integer id;
    private String nome;
    private String bairro;
    private List<Comodo> comodos;

    public Propriedade(String nome, String bairro, List<Comodo> comodos) {
        this.nome = nome;
        this.bairro = bairro;
        this.comodos = comodos;
    }

    public PropriedadeDTO toDto() {
        List<ComodoRequestDTO> comodosDtos =  comodos.stream()
                .map(Comodo::toComodoRequestDto)
                .collect(Collectors.toList());

        return new PropriedadeDTO(this.nome, this.bairro, comodosDtos);
    }
}
