package ejercicio2.model;

/**
 * Clase que representa a un usuario del sistema de pedidos.
 */
public class User {
    private Long id;
    private String username;
    private String password; // En una aplicación real se guardaría encriptada
    private String email;
    private String address;
    private String phoneNumber;

    /**
     * Constructor por defecto.
     */
    public User() {
    }

    /**
     * Constructor con parámetros básicos.
     *
     * @param username Nombre de usuario
     * @param password Contraseña
     * @param email Correo electrónico
     */
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    /**
     * Constructor completo.
     *
     * @param id Identificador único
     * @param username Nombre de usuario
     * @param password Contraseña
     * @param email Correo electrónico
     * @param address Dirección
     * @param phoneNumber Número de teléfono
     */
    public User(Long id, String username, String password, String email, String address, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}