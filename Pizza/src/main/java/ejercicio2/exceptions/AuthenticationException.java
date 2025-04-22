package ejercicio2.exceptions;

/**
 * Excepción lanzada cuando ocurre un error en el proceso de autenticación.
 */
public class AuthenticationException extends Exception {

    /**
     * Constructor por defecto.
     */
    public AuthenticationException() {
        super("Error en la autenticación");
    }

    /**
     * Constructor con mensaje personalizado.
     *
     * @param message Mensaje descriptivo del error
     */
    public AuthenticationException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa.
     *
     * @param message Mensaje descriptivo del error
     * @param cause Causa original del error
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}