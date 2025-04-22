package ejercicio2.order;

import ejercicio2.database.DataBaseManager;
import ejercicio2.exceptions.OrderException;
import ejercicio2.interfaces.IDataBaseManager;
import ejercicio2.model.Order;
import ejercicio2.model.Pizza;
import ejercicio2.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderManagerTest {

    private IDataBaseManager dbManager;
    private OrderManager orderManager;
    private User testUser;
    private List<Pizza> testPizzas;

    @BeforeEach
    public void setUp() {
        dbManager = new DataBaseManager();
        dbManager.connect();
        orderManager = new OrderManager(dbManager);

        // Crear usuario de prueba
        testUser = new User("testOrderUser", "password", "test@example.com");
        testUser = dbManager.save(testUser);

        // Crear pizzas de prueba
        Pizza pizza1 = new Pizza("Margherita", "Medium", 9.99);
        Pizza pizza2 = new Pizza("Pepperoni", "Large", 12.99);
        pizza1 = dbManager.save(pizza1);
        pizza2 = dbManager.save(pizza2);

        testPizzas = Arrays.asList(pizza1, pizza2);
    }

    @Test
    public void testCreateOrder() throws OrderException {
        Order order = orderManager.createOrder(testUser, testPizzas);

        assertNotNull(order);
        assertNotNull(order.getId());
        assertEquals(testUser, order.getUser());
        assertEquals(2, order.getPizzas().size());
        assertEquals("PENDING", order.getStatus());
        assertEquals(testPizzas.get(0).getPrice() + testPizzas.get(1).getPrice(), order.getTotalPrice(), 0.01);
    }

    @Test
    public void testCreateOrderWithNullUser() {
        assertThrows(OrderException.class, () -> {
            orderManager.createOrder(null, testPizzas);
        });
    }

    @Test
    public void testCreateOrderWithEmptyPizzaList() {
        assertThrows(OrderException.class, () -> {
            orderManager.createOrder(testUser, new ArrayList<>());
        });
    }

    @Test
    public void testGetOrderById() throws OrderException {
        // Crear un pedido
        Order createdOrder = orderManager.createOrder(testUser, testPizzas);

        // Obtener el pedido por ID
        Order retrievedOrder = orderManager.getOrderById(createdOrder.getId());

        assertNotNull(retrievedOrder);
        assertEquals(createdOrder.getId(), retrievedOrder.getId());
        assertEquals(testUser.getId(), retrievedOrder.getUser().getId());
        assertEquals(2, retrievedOrder.getPizzas().size());
    }

    @Test
    public void testGetOrderByNonExistentId() {
        assertThrows(OrderException.class, () -> {
            orderManager.getOrderById(9999L);
        });
    }

    @Test
    public void testGetUserOrders() throws OrderException {
        // Crear varios pedidos para el usuario
        orderManager.createOrder(testUser, testPizzas);
        orderManager.createOrder(testUser, testPizzas.subList(0, 1));

        // Obtener todos los pedidos del usuario
        List<Order> userOrders = orderManager.getUserOrders(testUser);

        assertNotNull(userOrders);
        assertEquals(2, userOrders.size());
        for (Order order : userOrders) {
            assertEquals(testUser.getId(), order.getUser().getId());
        }
    }

    @Test
    public void testUpdateOrderStatus() throws OrderException {
        // Crear un pedido
        Order order = orderManager.createOrder(testUser, testPizzas);

        // Actualizar el estado del pedido
        Order updatedOrder = orderManager.updateOrderStatus(order.getId(), "PROCESSING");

        assertEquals("PROCESSING", updatedOrder.getStatus());

        // Verificar que el cambio se ha guardado en la base de datos
        Order retrievedOrder = orderManager.getOrderById(order.getId());
        assertEquals("PROCESSING", retrievedOrder.getStatus());
    }

    @Test
    public void testUpdateOrderStatusWithInvalidStatus() throws OrderException {
        // Crear un pedido
        Order order = orderManager.createOrder(testUser, testPizzas);

        // Intentar actualizar con un estado inválido
        assertThrows(OrderException.class, () -> {
            orderManager.updateOrderStatus(order.getId(), "INVALID_STATUS");
        });
    }

    @Test
    public void testCancelOrder() throws OrderException {
        // Crear un pedido
        Order order = orderManager.createOrder(testUser, testPizzas);

        // Cancelar el pedido
        boolean cancelled = orderManager.cancelOrder(order.getId());

        assertTrue(cancelled);

        // Verificar que el estado se ha actualizado
        Order cancelledOrder = orderManager.getOrderById(order.getId());
        assertEquals("CANCELLED", cancelledOrder.getStatus());
    }

    @Test
    public void testCancelDeliveredOrder() throws OrderException {
        // Crear un pedido
        Order order = orderManager.createOrder(testUser, testPizzas);

        // Marcar el pedido como entregado
        orderManager.updateOrderStatus(order.getId(), "DELIVERED");

        // Intentar cancelar un pedido ya entregado
        assertThrows(OrderException.class, () -> {
            orderManager.cancelOrder(order.getId());
        });
    }

    @Test
    public void testCalculateOrderPrice() throws OrderException {
        // Crear pizzas con precios conocidos
        Pizza pizza1 = new Pizza("Test Pizza 1", "Small", 5.99);
        Pizza pizza2 = new Pizza("Test Pizza 2", "Medium", 7.99);
        List<Pizza> pizzas = Arrays.asList(pizza1, pizza2);

        // Crear un pedido con estas pizzas
        Order order = new Order(testUser, pizzas);

        // Calcular el precio total
        double totalPrice = orderManager.calculateOrderPrice(order);

        // Verificar que el cálculo es correcto
        assertEquals(13.98, totalPrice, 0.01);
    }
}