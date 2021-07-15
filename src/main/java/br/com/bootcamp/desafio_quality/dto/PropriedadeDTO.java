package br.com.bootcamp.desafio_quality.dto;

import br.com.bootcamp.desafio_quality.entity.Bairro;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropriedadeDTO {

    private String nome;
    private Bairro bairro;
    private List<ComodoRequestDTO> comodos;
}
