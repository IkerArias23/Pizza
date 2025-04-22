package ejercicio2;

import ejercicio2.auth.Authenticator;
import ejercicio2.database.DataBaseManager;
import ejercicio2.exceptions.AuthenticationException;
import ejercicio2.exceptions.OrderException;
import ejercicio2.exceptions.PaymentException;
import ejercicio2.interfaces.IAuthenticator;
import ejercicio2.interfaces.IDataBaseManager;
import ejercicio2.interfaces.IOrderManager;
import ejercicio2.interfaces.IPaymentProcessor;
import ejercicio2.model.Order;
import ejercicio2.model.Pizza;
import ejercicio2.model.User;
import ejercicio2.order.OrderManager;
import ejercicio2.payment.PaymentProcessor;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal que demuestra la inyección de dependencias
 * en el sistema de gestión de pedidos de pizza.
 */
public class Main {

    public static void main(String[] args) {
        try {
            // Crear las dependencias
            IDataBaseManager dbManager = new DataBaseManager();
            dbManager.connect();

            IAuthenticator authenticator = new Authenticator(dbManager);
            IOrderManager orderManager = new OrderManager(dbManager);
            IPaymentProcessor paymentProcessor = new PaymentProcessor(dbManager);

            // Registrar un usuario
            User user = authenticator.register("usuario1", "password123", "usuario1@example.com");
            System.out.println("Usuario registrado: " + user);

            // Iniciar sesión
            String token = authenticator.generateToken(user);
            System.out.println("Token generado: " + token);

            // Crear pizzas para el pedido
            List<Pizza> pizzas = new ArrayList<>();
            Pizza pizza1 = new Pizza("Margarita", "Mediana", 10.99);
            pizza1.addTopping("Queso").addTopping("Tomate").addTopping("Albahaca");

            Pizza pizza2 = new Pizza("Pepperoni", "Grande", 14.99);
            pizza2.addTopping("Queso").addTopping("Pepperoni").addTopping("Orégano");

            pizzas.add(pizza1);
            pizzas.add(pizza2);

            // Crear un pedido
            Order order = orderManager.createOrder(user, pizzas);
            System.out.println("Pedido creado: " + order);

            // Procesar el pago
            String transactionId = paymentProcessor.processPayment(order, "1234 5678 9012 3456", "12/25", "123");
            System.out.println("Pago procesado, transacción: " + transactionId);

            // Verificar el estado del pago
            String paymentStatus = paymentProcessor.verifyPayment(transactionId);
            System.out.println("Estado del pago: " + paymentStatus);

            // Actualizar el estado del pedido
            order = orderManager.updateOrderStatus(order.getId(), "DELIVERED");
            System.out.println("Pedido actualizado: " + order);

            // Obtener el historial de pagos
            String paymentHistory = paymentProcessor.getPaymentHistory(order.getId());
            System.out.println("Historial de pagos:");
            System.out.println(paymentHistory);

            // Cerrar la conexión con la base de datos
            dbManager.disconnect();

        } catch (AuthenticationException | OrderException | PaymentException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}