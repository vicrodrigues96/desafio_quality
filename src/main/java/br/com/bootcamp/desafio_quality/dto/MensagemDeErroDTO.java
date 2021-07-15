package br.com.bootcamp.desafio_quality.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MensagemDeErroDTO {

    private int statusCode;
    private String Message;

}
