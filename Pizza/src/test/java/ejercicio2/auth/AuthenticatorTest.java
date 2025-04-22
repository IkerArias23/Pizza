package ejercicio2.auth;

import ejercicio2.database.DataBaseManager;
import ejercicio2.exceptions.AuthenticationException;
import ejercicio2.interfaces.IDataBaseManager;
import ejercicio2.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Authenticator.
 */
public class AuthenticatorTest {

    private IDataBaseManager dbManager;
    private Authenticator authenticator;

    @BeforeEach
    public void setUp() {
        dbManager = new DataBaseManager();
        dbManager.connect();
        authenticator = new Authenticator(dbManager);
    }

    @Test
    public void testRegister() throws AuthenticationException {
        User user = authenticator.register("testUser", "password123", "test@example.com");

        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    public void testRegisterInvalid() {
        // Username nulo
        assertThrows(AuthenticationException.class, () -> {
            authenticator.register(null, "password123", "test@example.com");
        });

        // Password vacío
        assertThrows(AuthenticationException.class, () -> {
            authenticator.register("testUser", "", "test@example.com");
        });
    }

    @Test
    public void testLogin() throws AuthenticationException {
        // Registrar primero
        User registeredUser = authenticator.register("loginUser", "password123", "login@example.com");

        // Luego hacer login
        User loggedInUser = authenticator.login("loginUser", "password123");

        assertNotNull(loggedInUser);
        assertEquals("loginUser", loggedInUser.getUsername());
    }

    @Test
    public void testLoginInvalid() {
        // Username nulo
        assertThrows(AuthenticationException.class, () -> {
            authenticator.login(null, "password123");
        });

        // Password vacío
        assertThrows(AuthenticationException.class, () -> {
            authenticator.login("testUser", "");
        });
    }

    @Test
    public void testTokenGeneration() throws AuthenticationException {
        User user = authenticator.register("tokenUser", "password123", "token@example.com");

        String token = authenticator.generateToken(user);

        assertNotNull(token);
        assertTrue(authenticator.validateToken(token));
    }

    @Test
    public void testLogout() throws AuthenticationException {
        User user = authenticator.register("logoutUser", "password123", "logout@example.com");
        String token = authenticator.generateToken(user);

        assertTrue(authenticator.validateToken(token));

        authenticator.logout(token);

        assertFalse(authenticator.validateToken(token));
    }
}