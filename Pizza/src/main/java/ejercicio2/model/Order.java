package ejercicio2.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un pedido en el sistema.
 */
public class Order {
    private Long id;
    private User user;
    private List<Pizza> pizzas;
    private LocalDateTime orderDate;
    private String status; // PENDING, PROCESSING, DELIVERED, CANCELLED
    private double totalPrice;
    private String paymentTransactionId;

    /**
     * Constructor por defecto.
     */
    public Order() {
        this.pizzas = new ArrayList<>();
        this.orderDate = LocalDateTime.now();
        this.status = "PENDING";
    }

    /**
     * Constructor con parámetros básicos.
     *
     * @param user Usuario que realiza el pedido
     * @param pizzas Lista de pizzas en el pedido
     */
    public Order(User user, List<Pizza> pizzas) {
        this.user = user;
        this.pizzas = pizzas != null ? pizzas : new ArrayList<>();
        this.orderDate = LocalDateTime.now();
        this.status = "PENDING";
        calculateTotalPrice();
    }

    /**
     * Constructor completo.
     *
     * @param id Identificador único
     * @param user Usuario que realiza el pedido
     * @param pizzas Lista de pizzas en el pedido
     * @param orderDate Fecha y hora del pedido
     * @param status Estado del pedido
     * @param totalPrice Precio total del pedido
     * @param paymentTransactionId ID de la transacción de pago
     */
    public Order(Long id, User user, List<Pizza> pizzas, LocalDateTime orderDate,
                 String status, double totalPrice, String paymentTransactionId) {
        this.id = id;
        this.user = user;
        this.pizzas = pizzas != null ? pizzas : new ArrayList<>();
        this.orderDate = orderDate != null ? orderDate : LocalDateTime.now();
        this.status = status;
        this.totalPrice = totalPrice;
        this.paymentTransactionId = paymentTransactionId;
    }

    /**
     * Calcula el precio total del pedido.
     */
    public void calculateTotalPrice() {
        this.totalPrice = 0;
        for (Pizza pizza : pizzas) {
            this.totalPrice += pizza.getPrice();
        }
    }

    /**
     * Añade una pizza al pedido.
     *
     * @param pizza Pizza a añadir
     * @return this para permitir encadenamiento
     */
    public Order addPizza(Pizza pizza) {
        if (pizzas == null) {
            pizzas = new ArrayList<>();
        }
        pizzas.add(pizza);
        calculateTotalPrice();
        return this;
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Pizza> getPizzas() {
        return pizzas;
    }

    public void setPizzas(List<Pizza> pizzas) {
        this.pizzas = pizzas;
        calculateTotalPrice();
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public void setPaymentTransactionId(String paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user=" + user +
                ", pizzas=" + pizzas +
                ", orderDate=" + orderDate +
                ", status='" + status + '\'' +
                ", totalPrice=" + totalPrice +
                ", paymentTransactionId='" + paymentTransactionId + '\'' +
                '}';
    }
}