package ejercicio2.database;

import ejercicio2.model.User;
import ejercicio2.model.Order;
import ejercicio2.model.Pizza;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DataBaseManagerTest {

    private DataBaseManager dbManager;

    @BeforeEach
    public void setUp() {
        dbManager = new DataBaseManager();
        dbManager.connect();
    }

    @Test
    public void testConnection() {
        assertTrue(dbManager.isConnected());

        dbManager.disconnect();
        assertFalse(dbManager.isConnected());

        assertTrue(dbManager.connect());
        assertTrue(dbManager.isConnected());
    }

    @Test
    public void testSaveAndFindById() {
        // Crear y guardar un usuario
        User user = new User("testuser", "password", "test@example.com");
        user = dbManager.save(user);

        // Verificar que se ha asignado un ID
        assertNotNull(user.getId());

        // Buscar el usuario por ID
        Optional<User> foundUser = dbManager.findById(user.getId(), User.class);

        // Verificar que se ha encontrado
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    public void testUpdate() {
        // Crear y guardar un usuario
        User user = new User("originaluser", "password", "original@example.com");
        user = dbManager.save(user);
        Long userId = user.getId();

        // Modificar el usuario
        user.setUsername("updateduser");
        user.setEmail("updated@example.com");

        // Actualizar en la base de datos
        dbManager.update(user);

        // Buscar el usuario actualizado
        Optional<User> updatedUser = dbManager.findById(userId, User.class);

        // Verificar que se ha actualizado
        assertTrue(updatedUser.isPresent());
        assertEquals("updateduser", updatedUser.get().getUsername());
        assertEquals("updated@example.com", updatedUser.get().getEmail());
    }

    @Test
    public void testDelete() {
        // Crear y guardar un usuario
        User user = new User("userToDelete", "password", "delete@example.com");
        user = dbManager.save(user);
        Long userId = user.getId();

        // Verificar que existe
        assertTrue(dbManager.findById(userId, User.class).isPresent());

        // Eliminar el usuario
        boolean deleted = dbManager.delete(userId, User.class);

        // Verificar que se ha eliminado
        assertTrue(deleted);
        assertFalse(dbManager.findById(userId, User.class).isPresent());
    }

    @Test
    public void testFindAll() {
        // Limpiar registros previos
        List<User> initialUsers = dbManager.findAll(User.class);
        for (User user : initialUsers) {
            dbManager.delete(user.getId(), User.class);
        }

        // Crear y guardar varios usuarios
        User user1 = new User("user1", "pass1", "user1@example.com");
        User user2 = new User("user2", "pass2", "user2@example.com");
        User user3 = new User("user3", "pass3", "user3@example.com");

        dbManager.save(user1);
        dbManager.save(user2);
        dbManager.save(user3);

        // Buscar todos los usuarios
        List<User> allUsers = dbManager.findAll(User.class);

        // Verificar que se han encontrado todos
        assertEquals(3, allUsers.size());
    }

    @Test
    public void testMultipleEntityTypes() {
        // Crear y guardar un usuario
        User user = new User("testuser", "password", "test@example.com");
        user = dbManager.save(user);

        // Crear y guardar una pizza
        Pizza pizza = new Pizza("Margherita", "Medium", 9.99);
        pizza = dbManager.save(pizza);

        // Crear y guardar un pedido
        List<Pizza> pizzas = new ArrayList<>();
        pizzas.add(pizza);
        Order order = new Order(user, pizzas);
        order = dbManager.save(order);

        // Verificar que todos se han guardado correctamente
        assertTrue(dbManager.findById(user.getId(), User.class).isPresent());
        assertTrue(dbManager.findById(pizza.getId(), Pizza.class).isPresent());
        assertTrue(dbManager.findById(order.getId(), Order.class).isPresent());

        // Verificar que los IDs son distintos para cada tipo
        assertNotEquals(user.getId(), pizza.getId());
        assertNotEquals(user.getId(), order.getId());
        assertNotEquals(pizza.getId(), order.getId());
    }

    @Test
    public void testOperationsWhenDisconnected() {
        dbManager.disconnect();

        User user = new User("testuser", "password", "test@example.com");

        assertThrows(IllegalStateException.class, () -> {
            dbManager.save(user);
        });

        assertThrows(IllegalStateException.class, () -> {
            dbManager.findById(1L, User.class);
        });

        assertThrows(IllegalStateException.class, () -> {
            dbManager.update(user);
        });

        assertThrows(IllegalStateException.class, () -> {
            dbManager.delete(1L, User.class);
        });

        assertThrows(IllegalStateException.class, () -> {
            dbManager.findAll(User.class);
        });
    }
}