package br.com.bootcamp.desafio_quality.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComodoRequestDTO {

    @NotBlank(message = "O nome do comodo não pode ser vazio")
    @Size(max = 30, message = "O comprimento do nome não pode exceder 30 caracteres")
    @Pattern(regexp = "^[A-Z][\\w\\s]*", message = " O nome do comodo deve ser começar com uma letra maiuscula")
    private String nome;


    @NotNull
    @DecimalMax(value = 25.0, message = "O comprimento do nome não pode exceder 30 caracteres")
    private Double altura;
    private Double largura;
}
