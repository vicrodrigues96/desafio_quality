package br.com.bootcamp.desafio_quality.exception.handler;

import br.com.bootcamp.desafio_quality.dto.MensagemDeErroDTO;
import br.com.bootcamp.desafio_quality.exception.BairroInexistenteException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler({BairroInexistenteException.class})
    public ResponseEntity<?> defaultHandler(RuntimeException e){
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MensagemDeErroDTO> methodArgumentNotValidHandler(MethodArgumentNotValidException e){

        List<String> fieldErrorsString = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(f -> f.getField()+": "+f.getDefaultMessage())
                .collect(Collectors.toList());

        String stringErrors = String.join(", ", fieldErrorsString);

        return ResponseEntity.badRequest().body(new MensagemDeErroDTO(HttpStatus.BAD_REQUEST.value(), stringErrors));
    }

}
