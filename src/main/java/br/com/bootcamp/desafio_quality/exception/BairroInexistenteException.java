package br.com.bootcamp.desafio_quality.exception;

public class BairroInexistenteException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public BairroInexistenteException() {
        super();
    }

    public BairroInexistenteException(String message) {
        super(message);
    }
}
