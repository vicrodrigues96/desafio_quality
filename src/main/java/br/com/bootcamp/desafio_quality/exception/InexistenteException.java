package br.com.bootcamp.desafio_quality.exception;

public class InexistenteException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InexistenteException() {
        super();
    }

    public InexistenteException(String message) {
        super(message);
    }
}
