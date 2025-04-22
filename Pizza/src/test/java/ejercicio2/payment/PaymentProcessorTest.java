package ejercicio2.payment;

import ejercicio2.database.DataBaseManager;
import ejercicio2.exceptions.PaymentException;
import ejercicio2.model.Order;
import ejercicio2.model.Pizza;
import ejercicio2.model.User;
import ejercicio2.interfaces.IDataBaseManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentProcessorTest {

    private IDataBaseManager dbManager;
    private PaymentProcessor paymentProcessor;
    private Order testOrder;

    @BeforeEach
    public void setUp() {
        dbManager = new DataBaseManager();
        dbManager.connect();
        paymentProcessor = new PaymentProcessor(dbManager);

        // Crear usuario, pizzas y orden de prueba
        User user = new User("paymentUser", "password", "payment@example.com");
        user = dbManager.save(user);

        Pizza pizza1 = new Pizza("Hawaiian", "Large", 14.99);
        Pizza pizza2 = new Pizza("Vegetarian", "Medium", 11.99);
        pizza1 = dbManager.save(pizza1);
        pizza2 = dbManager.save(pizza2);

        List<Pizza> pizzas = Arrays.asList(pizza1, pizza2);

        testOrder = new Order(user, pizzas);
        testOrder.calculateTotalPrice();
        testOrder = dbManager.save(testOrder);
    }

    @Test
    public void testProcessPayment() throws PaymentException {
        String transactionId = paymentProcessor.processPayment(
                testOrder,
                "4111111111111111", // Número de tarjeta de prueba
                "12/25", // Fecha de expiración
                "123" // CVV
        );

        assertNotNull(transactionId);
        assertFalse(transactionId.isEmpty());

        // Verificar que el ID de transacción se ha guardado en el pedido
        Order updatedOrder = dbManager.findById(testOrder.getId(), Order.class).orElseThrow();
        assertEquals(transactionId, updatedOrder.getPaymentTransactionId());

        // Verificar que el estado del pedido se ha actualizado
        assertEquals("PROCESSING", updatedOrder.getStatus());
    }

    @Test
    public void testProcessPaymentWithInvalidCardNumber() {
        assertThrows(PaymentException.class, () -> {
            paymentProcessor.processPayment(
                    testOrder,
                    "1234", // Número de tarjeta inválido
                    "12/25",
                    "123"
            );
        });
    }

    @Test
    public void testProcessPaymentWithExpiredCard() {
        assertThrows(PaymentException.class, () -> {
            paymentProcessor.processPayment(
                    testOrder,
                    "4111111111111111",
                    "01/20", // Fecha expirada
                    "123"
            );
        });
    }

    @Test
    public void testProcessPaymentWithInvalidCVV() {
        assertThrows(PaymentException.class, () -> {
            paymentProcessor.processPayment(
                    testOrder,
                    "4111111111111111",
                    "12/25",
                    "1" // CVV inválido
            );
        });
    }

    @Test
    public void testVerifyPayment() throws PaymentException {
        // Procesar un pago
        String transactionId = paymentProcessor.processPayment(
                testOrder,
                "4111111111111111",
                "12/25",
                "123"
        );

        // Verificar el estado del pago
        String status = paymentProcessor.verifyPayment(transactionId);

        assertNotNull(status);
        assertEquals("COMPLETED", status);
    }

    @Test
    public void testVerifyNonExistentPayment() {
        assertThrows(PaymentException.class, () -> {
            paymentProcessor.verifyPayment("non-existent-transaction-id");
        });
    }

    @Test
    public void testRefundPayment() throws PaymentException {
        // Procesar un pago
        String transactionId = paymentProcessor.processPayment(
                testOrder,
                "4111111111111111",
                "12/25",
                "123"
        );

        // Reembolsar el pago
        boolean refunded = paymentProcessor.refundPayment(transactionId);

        assertTrue(refunded);

        // Verificar que el estado del pago ha cambiado
        String status = paymentProcessor.verifyPayment(transactionId);
        assertEquals("REFUNDED", status);

        // Verificar que el estado del pedido ha cambiado
        Order updatedOrder = dbManager.findById(testOrder.getId(), Order.class).orElseThrow();
        assertEquals("CANCELLED", updatedOrder.getStatus());
    }

    @Test
    public void testRefundNonExistentPayment() {
        assertThrows(PaymentException.class, () -> {
            paymentProcessor.refundPayment("non-existent-transaction-id");
        });
    }

    @Test
    public void testGetPaymentHistory() throws PaymentException {
        // Procesar un pago
        String transactionId = paymentProcessor.processPayment(
                testOrder,
                "4111111111111111",
                "12/25",
                "123"
        );

        // Obtener el historial de pagos
        String history = paymentProcessor.getPaymentHistory(testOrder.getId());

        assertNotNull(history);
        assertTrue(history.contains(transactionId));
        assertTrue(history.contains("COMPLETED"));

        // Reembolsar el pago
        paymentProcessor.refundPayment(transactionId);

        // Obtener el historial actualizado
        String updatedHistory = paymentProcessor.getPaymentHistory(testOrder.getId());
        assertTrue(updatedHistory.contains("REFUNDED"));
    }

    @Test
    public void testGetPaymentHistoryForNonExistentOrder() {
        assertThrows(PaymentException.class, () -> {
            paymentProcessor.getPaymentHistory(9999L);
        });
    }

    @Test
    public void testPaymentWithZeroAmount() {
        // Crear un pedido con importe cero
        User user = new User("zeroUser", "password", "zero@example.com");
        user = dbManager.save(user);

        Pizza freePizza = new Pizza("Free Pizza", "Small", 0.0);
        freePizza = dbManager.save(freePizza);

        Order zeroOrder = new Order(user, Arrays.asList(freePizza));
        zeroOrder.calculateTotalPrice();
        zeroOrder = dbManager.save(zeroOrder);

        // Intentar procesar el pago
        Order finalZeroOrder = zeroOrder;
        assertThrows(PaymentException.class, () -> {
            paymentProcessor.processPayment(
                    finalZeroOrder,
                    "4111111111111111",
                    "12/25",
                    "123"
            );
        });
    }
}