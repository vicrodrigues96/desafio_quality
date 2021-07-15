package br.com.bootcamp.desafio_quality.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BairroDTO {

    @NotBlank(message = "O nome do bairro não pode ser vazio")
    @Size(max = 45, message = "O comprimento do nome não pode exceder 45 caracteres")
    private String nome;

    @NotNull(message = "O valor do metro quadrado do bairro não pode ser vazio")
    @Size(max = 13, message = "O comprimento máximo não pode excedor 13 digitos")
    private BigDecimal custoPorMetroQuadrado;
}
