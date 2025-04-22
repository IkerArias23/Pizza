package ejercicio2.exceptions;

/**
 * Excepción lanzada cuando ocurre un error en la gestión de pedidos.
 */
public class OrderException extends Exception {

    /**
     * Constructor por defecto.
     */
    public OrderException() {
        super("Error en la gestión del pedido");
    }

    /**
     * Constructor con mensaje personalizado.
     *
     * @param message Mensaje descriptivo del error
     */
    public OrderException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa.
     *
     * @param message Mensaje descriptivo del error
     * @param cause Causa original del error
     */
    public OrderException(String message, Throwable cause) {
        super(message, cause);
    }
}