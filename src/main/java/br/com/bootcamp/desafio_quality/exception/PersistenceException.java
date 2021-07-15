package br.com.bootcamp.desafio_quality.exception;

public class PersistenceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PersistenceException() {
        super();
    }

    public PersistenceException(String message) {
        super(message);
    }
}
