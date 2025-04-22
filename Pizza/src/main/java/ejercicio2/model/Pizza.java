package ejercicio2.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una pizza en el sistema de pedidos.
 */
public class Pizza {
    private Long id;
    private String name;
    private String size;
    private List<String> toppings;
    private double price;

    /**
     * Constructor por defecto.
     */
    public Pizza() {
        this.toppings = new ArrayList<>();
    }

    /**
     * Constructor con parámetros básicos.
     *
     * @param name Nombre de la pizza
     * @param size Tamaño de la pizza
     * @param price Precio de la pizza
     */
    public Pizza(String name, String size, double price) {
        this.name = name;
        this.size = size;
        this.price = price;
        this.toppings = new ArrayList<>();
    }

    /**
     * Constructor completo.
     *
     * @param id Identificador único
     * @param name Nombre de la pizza
     * @param size Tamaño de la pizza
     * @param toppings Ingredientes de la pizza
     * @param price Precio de la pizza
     */
    public Pizza(Long id, String name, String size, List<String> toppings, double price) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.toppings = toppings != null ? toppings : new ArrayList<>();
        this.price = price;
    }

    /**
     * Añade un ingrediente a la pizza.
     *
     * @param topping Ingrediente a añadir
     * @return this para permitir encadenamiento
     */
    public Pizza addTopping(String topping) {
        if (toppings == null) {
            toppings = new ArrayList<>();
        }
        toppings.add(topping);
        return this;
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public List<String> getToppings() {
        return toppings;
    }

    public void setToppings(List<String> toppings) {
        this.toppings = toppings;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Pizza{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", toppings=" + toppings +
                ", price=" + price +
                '}';
    }
}