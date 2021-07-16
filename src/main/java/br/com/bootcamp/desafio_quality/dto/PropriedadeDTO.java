package br.com.bootcamp.desafio_quality.dto;

import br.com.bootcamp.desafio_quality.entity.Bairro;
import br.com.bootcamp.desafio_quality.entity.Comodo;
import br.com.bootcamp.desafio_quality.entity.Propriedade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropriedadeDTO {

    @NotBlank(message = "O nome da propriedade não pode ser vazio")
    @Size(max = 30, message = "O comprimento do nome não pode exceder 30 caracteres")
    @Pattern(regexp = "^[A-Z][\\w\\s]*", message = " O nome da propriedade deve ser começar com uma letra maiuscula")
    private String nome;

    @NotNull(message = "O bairro não pode ser nulo")
    @Valid
    private String bairro;

    @NotNull(message = "A lista de comodos não pode ser nula")
    @Valid
    private List<ComodoRequestDTO> comodos;

    public Propriedade toEntity() {
        List<Comodo> comodoEntities =  comodos.stream()
                .map(ComodoRequestDTO::toEntity)
                .collect(Collectors.toList());
        return new Propriedade(this.nome, this.bairro, comodoEntities);
    }
}
