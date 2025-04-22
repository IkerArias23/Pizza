package ejercicio2.interfaces;

import ejercicio2.exceptions.AuthenticationException;
import ejercicio2.model.User;

/**
 * Interfaz para la autenticación de usuarios.
 * Define los métodos básicos para registro, login y verificación.
 */
public interface IAuthenticator {
    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param username Nombre de usuario
     * @param password Contraseña
     * @return Usuario creado
     * @throws AuthenticationException Si hay un error en el registro
     */
    User register(String username, String password) throws AuthenticationException;

    /**
     * Autentica un usuario con sus credenciales.
     *
     * @param username Nombre de usuario
     * @param password Contraseña
     * @return Usuario autenticado
     * @throws AuthenticationException Si las credenciales son inválidas
     */
    User login(String username, String password) throws AuthenticationException;

    /**
     * Verifica si un token de autenticación es válido.
     *
     * @param token Token de autenticación
     * @return true si el token es válido, false si no
     */
    boolean validateToken(String token);

    /**
     * Genera un token de autenticación para un usuario.
     *
     * @param user Usuario para el que se genera el token
     * @return Token de autenticación
     */
    String generateToken(User user);

    /**
     * Cierra la sesión de un usuario.
     *
     * @param token Token de autenticación a invalidar
     */
    void logout(String token);
}