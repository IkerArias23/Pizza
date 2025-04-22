package ejercicio2.exceptions;

/**
 * Excepción lanzada cuando ocurre un error en el procesamiento de pagos.
 */
public class PaymentException extends Exception {

    /**
     * Constructor por defecto.
     */
    public PaymentException() {
        super("Error en el procesamiento del pago");
    }

    /**
     * Constructor con mensaje personalizado.
     *
     * @param message Mensaje descriptivo del error
     */
    public PaymentException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa.
     *
     * @param message Mensaje descriptivo del error
     * @param cause Causa original del error
     */
    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}