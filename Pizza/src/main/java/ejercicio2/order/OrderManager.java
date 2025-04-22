package ejercicio2.order;

import ejercicio2.exceptions.OrderException;
import ejercicio2.interfaces.IDataBaseManager;
import ejercicio2.interfaces.IOrderManager;
import ejercicio2.model.Order;
import ejercicio2.model.Pizza;
import ejercicio2.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación de IOrderManager para manejar los pedidos.
 */
public class OrderManager implements IOrderManager {

    private final IDataBaseManager dbManager;

    /**
     * Constructor con inyección de dependencia.
     *
     * @param dbManager Gestor de base de datos
     */
    public OrderManager(IDataBaseManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public Order createOrder(User user, List<Pizza> pizzas) throws OrderException {
        if (user == null) {
            throw new OrderException("El usuario no puede ser nulo");
        }

        if (pizzas == null || pizzas.isEmpty()) {
            throw new OrderException("El pedido debe contener al menos una pizza");
        }

        // Guardar las pizzas si no están guardadas
        List<Pizza> savedPizzas = new ArrayList<>();
        for (Pizza pizza : pizzas) {
            savedPizzas.add(dbManager.save(pizza));
        }

        // Crear y guardar el pedido
        Order order = new Order(user, savedPizzas);
        order.calculateTotalPrice();

        return dbManager.save(order);
    }

    @Override
    public Order getOrderById(Long orderId) throws OrderException {
        if (orderId == null) {
            throw new OrderException("El ID del pedido no puede ser nulo");
        }

        Optional<Order> orderOpt = dbManager.findById(orderId, Order.class);

        return orderOpt.orElseThrow(() -> new OrderException("Pedido no encontrado con ID: " + orderId));
    }

    @Override
    public List<Order> getUserOrders(User user) {
        if (user == null || user.getId() == null) {
            return new ArrayList<>();
        }

        // En una implementación real se haría una consulta específica
        // Aquí simulamos filtrando todos los pedidos
        return dbManager.findAll(Order.class).stream()
                .filter(order -> order.getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Order updateOrderStatus(Long orderId, String status) throws OrderException {
        if (orderId == null) {
            throw new OrderException("El ID del pedido no puede ser nulo");
        }

        if (status == null || status.isEmpty()) {
            throw new OrderException("El estado no puede ser nulo o vacío");
        }

        // Validar que el estado sea válido
        if (!isValidStatus(status)) {
            throw new OrderException("Estado no válido: " + status);
        }

        Order order = getOrderById(orderId);
        order.setStatus(status);

        return dbManager.update(order);
    }

    @Override
    public boolean cancelOrder(Long orderId) throws OrderException {
        if (orderId == null) {
            throw new OrderException("El ID del pedido no puede ser nulo");
        }

        Order order = getOrderById(orderId);

        // Verificar si se puede cancelar
        if (!"PENDING".equals(order.getStatus()) && !"PROCESSING".equals(order.getStatus())) {
            throw new OrderException("No se puede cancelar un pedido con estado: " + order.getStatus());
        }

        order.setStatus("CANCELLED");
        dbManager.update(order);

        return true;
    }

    @Override
    public double calculateOrderPrice(Order order) {
        if (order == null || order.getPizzas() == null) {
            return 0;
        }

        double total = 0;
        for (Pizza pizza : order.getPizzas()) {
            total += pizza.getPrice();
        }

        return total;
    }

    /**
     * Valida si un estado es válido.
     *
     * @param status Estado a validar
     * @return true si el estado es válido, false si no
     */
    private boolean isValidStatus(String status) {
        return "PENDING".equals(status) ||
                "PROCESSING".equals(status) ||
                "DELIVERED".equals(status) ||
                "CANCELLED".equals(status);
    }
}