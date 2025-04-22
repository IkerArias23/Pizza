package ejercicio2.payment;

import ejercicio2.exceptions.PaymentException;
import ejercicio2.interfaces.IDataBaseManager;
import ejercicio2.interfaces.IPaymentProcessor;
import ejercicio2.model.Order;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Implementación de IPaymentProcessor para procesar pagos.
 */
public class PaymentProcessor implements IPaymentProcessor {

    private final IDataBaseManager dbManager;
    private final Map<String, Map<String, Object>> payments; // transactionId -> payment info

    /**
     * Constructor con inyección de dependencia.
     *
     * @param dbManager Gestor de base de datos
     */
    public PaymentProcessor(IDataBaseManager dbManager) {
        this.dbManager = dbManager;
        this.payments = new HashMap<>();
    }

    @Override
    public String processPayment(Order order, String cardNumber, String expiryDate, String cvv) throws PaymentException {
        // Validar datos de entrada
        if (order == null) {
            throw new PaymentException("El pedido no puede ser nulo");
        }

        if (cardNumber == null || cardNumber.isEmpty() ||
                expiryDate == null || expiryDate.isEmpty() ||
                cvv == null || cvv.isEmpty()) {
            throw new PaymentException("Los datos de la tarjeta son incompletos");
        }

        // Validar el formato de los datos de la tarjeta
        if (!isValidCardNumber(cardNumber)) {
            throw new PaymentException("Número de tarjeta inválido");
        }

        if (!isValidExpiryDate(expiryDate)) {
            throw new PaymentException("Fecha de expiración inválida");
        }

        if (!isValidCVV(cvv)) {
            throw new PaymentException("CVV inválido");
        }

        // Simular el procesamiento del pago
        String transactionId = UUID.randomUUID().toString();

        // Guardar la información del pago
        Map<String, Object> paymentInfo = new HashMap<>();
        paymentInfo.put("orderId", order.getId());
        paymentInfo.put("amount", order.getTotalPrice());
        paymentInfo.put("cardNumber", maskCardNumber(cardNumber));
        paymentInfo.put("timestamp", System.currentTimeMillis());
        paymentInfo.put("status", "COMPLETED");

        payments.put(transactionId, paymentInfo);

        // Actualizar el pedido con el ID de la transacción
        order.setPaymentTransactionId(transactionId);
        order.setStatus("PROCESSING");
        dbManager.update(order);

        return transactionId;
    }

    @Override
    public String verifyPayment(String transactionId) throws PaymentException {
        if (transactionId == null || transactionId.isEmpty()) {
            throw new PaymentException("El ID de transacción no puede ser nulo o vacío");
        }

        Map<String, Object> paymentInfo = payments.get(transactionId);
        if (paymentInfo == null) {
            throw new PaymentException("Transacción no encontrada: " + transactionId);
        }

        return (String) paymentInfo.get("status");
    }

    @Override
    public boolean refundPayment(String transactionId) throws PaymentException {
        if (transactionId == null || transactionId.isEmpty()) {
            throw new PaymentException("El ID de transacción no puede ser nulo o vacío");
        }

        Map<String, Object> paymentInfo = payments.get(transactionId);
        if (paymentInfo == null) {
            throw new PaymentException("Transacción no encontrada: " + transactionId);
        }

        // Verificar si ya se ha reembolsado
        if ("REFUNDED".equals(paymentInfo.get("status"))) {
            throw new PaymentException("La transacción ya ha sido reembolsada");
        }

        // Actualizar el estado del pago
        paymentInfo.put("status", "REFUNDED");
        paymentInfo.put("refundTimestamp", System.currentTimeMillis());

        // Actualizar el pedido
        Long orderId = (Long) paymentInfo.get("orderId");
        try {
            Order order = dbManager.findById(orderId, Order.class)
                    .orElseThrow(() -> new PaymentException("Pedido no encontrado: " + orderId));

            order.setStatus("CANCELLED");
            dbManager.update(order);
        } catch (Exception e) {
            throw new PaymentException("Error al actualizar el pedido: " + e.getMessage(), e);
        }

        return true;
    }

    @Override
    public String getPaymentHistory(Long orderId) throws PaymentException {
        if (orderId == null) {
            throw new PaymentException("El ID del pedido no puede ser nulo");
        }

        // Buscar todas las transacciones relacionadas con el pedido
        StringBuilder history = new StringBuilder();

        payments.forEach((transactionId, paymentInfo) -> {
            if (orderId.equals(paymentInfo.get("orderId"))) {
                history.append("Transacción: ").append(transactionId)
                        .append(", Estado: ").append(paymentInfo.get("status"))
                        .append(", Monto: ").append(paymentInfo.get("amount"))
                        .append(", Fecha: ").append(new java.util.Date((long) paymentInfo.get("timestamp")))
                        .append("\n");

                if (paymentInfo.containsKey("refundTimestamp")) {
                    history.append("  Reembolso: ")
                            .append(new java.util.Date((long) paymentInfo.get("refundTimestamp")))
                            .append("\n");
                }
            }
        });

        return history.toString();
    }

    /**
     * Valida un número de tarjeta.
     *
     * @param cardNumber Número de tarjeta
     * @return true si el número es válido, false si no
     */
    private boolean isValidCardNumber(String cardNumber) {
        // Simulación de validación
        return cardNumber.replaceAll("\\s", "").matches("\\d{16}");
    }

    /**
     * Valida una fecha de expiración.
     *
     * @param expiryDate Fecha de expiración
     * @return true si la fecha es válida, false si no
     */
    private boolean isValidExpiryDate(String expiryDate) {
        // Simulación de validación
        return expiryDate.matches("\\d{2}/\\d{2}");
    }

    /**
     * Valida un CVV.
     *
     * @param cvv Código de seguridad
     * @return true si el CVV es válido, false si no
     */
    private boolean isValidCVV(String cvv) {
        // Simulación de validación
        return cvv.matches("\\d{3}");
    }

    /**
     * Enmascara un número de tarjeta para seguridad.
     *
     * @param cardNumber Número de tarjeta
     * @return Número de tarjeta enmascarado
     */
    private String maskCardNumber(String cardNumber) {
        // Quitar espacios y mostrar solo los últimos 4 dígitos
        String cleaned = cardNumber.replaceAll("\\s", "");
        return "XXXX-XXXX-XXXX-" + cleaned.substring(cleaned.length() - 4);
    }
}