package ejercicio2.interfaces;

import java.util.List;
import ejercicio2.exceptions.OrderException;
import ejercicio2.model.Order;
import ejercicio2.model.Pizza;
import ejercicio2.model.User;

/**
 * Interfaz para la gestión de pedidos.
 * Define los métodos básicos para crear, obtener y gestionar pedidos.
 */
public interface IOrderManager {
    /**
     * Crea un nuevo pedido para un usuario.
     *
     * @param user Usuario que realiza el pedido
     * @param pizzas Lista de pizzas que contiene el pedido
     * @return Pedido creado
     * @throws OrderException Si hay un error al crear el pedido
     */
    Order createOrder(User user, List<Pizza> pizzas) throws OrderException;

    /**
     * Obtiene un pedido por su ID.
     *
     * @param orderId ID del pedido
     * @return Pedido encontrado
     * @throws OrderException Si el pedido no existe
     */
    Order getOrderById(Long orderId) throws OrderException;

    /**
     * Obtiene todos los pedidos de un usuario.
     *
     * @param user Usuario del que se quieren obtener los pedidos
     * @return Lista de pedidos del usuario
     */
    List<Order> getUserOrders(User user);

    /**
     * Actualiza el estado de un pedido.
     *
     * @param orderId ID del pedido
     * @param status Nuevo estado del pedido
     * @return Pedido actualizado
     * @throws OrderException Si hay un error al actualizar el pedido
     */
    Order updateOrderStatus(Long orderId, String status) throws OrderException;

    /**
     * Cancela un pedido.
     *
     * @param orderId ID del pedido a cancelar
     * @return true si se canceló correctamente, false si no
     * @throws OrderException Si hay un error al cancelar el pedido
     */
    boolean cancelOrder(Long orderId) throws OrderException;

    /**
     * Calcula el precio total de un pedido.
     *
     * @param order Pedido del que se quiere calcular el precio
     * @return Precio total del pedido
     */
    double calculateOrderPrice(Order order);
}