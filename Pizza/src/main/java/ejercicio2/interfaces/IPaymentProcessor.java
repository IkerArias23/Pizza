package ejercicio2.interfaces;

import ejercicio2.exceptions.PaymentException;
import ejercicio2.model.Order;

/**
 * Interfaz para el procesamiento de pagos.
 * Define los métodos básicos para procesar pagos y verificar su estado.
 */
public interface IPaymentProcessor {
    /**
     * Procesa el pago de un pedido.
     *
     * @param order Pedido a pagar
     * @param cardNumber Número de tarjeta
     * @param expiryDate Fecha de expiración
     * @param cvv Código de seguridad
     * @return ID de la transacción
     * @throws PaymentException Si hay un error en el pago
     */
    String processPayment(Order order, String cardNumber, String expiryDate, String cvv) throws PaymentException;

    /**
     * Verifica el estado de un pago.
     *
     * @param transactionId ID de la transacción
     * @return Estado del pago
     * @throws PaymentException Si hay un error al verificar el pago
     */
    String verifyPayment(String transactionId) throws PaymentException;

    /**
     * Reembolsa un pago.
     *
     * @param transactionId ID de la transacción a reembolsar
     * @return true si el reembolso fue exitoso, false si no
     * @throws PaymentException Si hay un error al procesar el reembolso
     */
    boolean refundPayment(String transactionId) throws PaymentException;

    /**
     * Obtiene el historial de pagos de un pedido.
     *
     * @param orderId ID del pedido
     * @return Historial de pagos en formato de cadena
     * @throws PaymentException Si hay un error al obtener el historial
     */
    String getPaymentHistory(Long orderId) throws PaymentException;
}