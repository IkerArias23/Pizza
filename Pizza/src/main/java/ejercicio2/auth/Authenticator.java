package ejercicio2.auth;

import ejercicio2.exceptions.AuthenticationException;
import ejercicio2.interfaces.IAuthenticator;
import ejercicio2.interfaces.IDataBaseManager;
import ejercicio2.model.User;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementación de IAuthenticator para manejar la autenticación de usuarios.
 */
public class Authenticator implements IAuthenticator {

    private final IDataBaseManager dbManager;
    private final Map<String, Long> activeTokens; // Token -> userId

    /**
     * Constructor con inyección de dependencia.
     *
     * @param dbManager Gestor de base de datos
     */
    public Authenticator(IDataBaseManager dbManager) {
        this.dbManager = dbManager;
        this.activeTokens = new HashMap<>();
    }

    @Override
    public User register(String username, String password, String email) throws AuthenticationException {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new AuthenticationException("El nombre de usuario y la contraseña son obligatorios");
        }

        // En una implementación real habría que validar que el usuario no exista ya
        // y encriptar la contraseña antes de guardarla

        User user = new User(username, password, email);
        return dbManager.save(user);
    }

    @Override
    public User login(String username, String password) throws AuthenticationException {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new AuthenticationException("El nombre de usuario y la contraseña son obligatorios");
        }

        // En una implementación real, se buscaría al usuario por username en la base de datos
        // y se verificaría la contraseña encriptada

        // Simulación de búsqueda de usuario
        // En una implementación real se haría una consulta específica
        // Aquí simplificamos asumiendo que tenemos el usuario
        User user = new User(1L, username, password, username + "@example.com", "Dirección de ejemplo", "123456789");

        if (!user.getPassword().equals(password)) {
            throw new AuthenticationException("Contraseña incorrecta");
        }

        String token = generateToken(user);
        activeTokens.put(token, user.getId());

        return user;
    }

    @Override
    public boolean validateToken(String token) {
        return token != null && activeTokens.containsKey(token);
    }

    @Override
    public String generateToken(User user) {
        String token = UUID.randomUUID().toString();
        activeTokens.put(token, user.getId());
        return token;
    }

    @Override
    public void logout(String token) {
        activeTokens.remove(token);
    }

    /**
     * Obtiene un usuario por su token de autenticación.
     *
     * @param token Token de autenticación
     * @return Usuario correspondiente al token o null si no existe
     * @throws AuthenticationException Si el token no es válido
     */
    public User getUserByToken(String token) throws AuthenticationException {
        if (!validateToken(token)) {
            throw new AuthenticationException("Token inválido o expirado");
        }

        Long userId = activeTokens.get(token);
        Optional<User> userOpt = dbManager.findById(userId, User.class);

        return userOpt.orElseThrow(() -> new AuthenticationException("Usuario no encontrado"));
    }
}