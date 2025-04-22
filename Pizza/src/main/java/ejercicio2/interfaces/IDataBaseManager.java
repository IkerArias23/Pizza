package ejercicio2.interfaces;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz para la gestión de operaciones con la base de datos.
 * Define los métodos básicos para operaciones CRUD.
 */
public interface IDataBaseManager {
    /**
     * Guarda un objeto en la base de datos.
     *
     * @param <T> Tipo del objeto a guardar
     * @param object Objeto a guardar
     * @return El objeto guardado con su ID asignado
     */
    <T> T save(T object);

    /**
     * Busca un objeto por su ID.
     *
     * @param <T> Tipo del objeto a buscar
     * @param id ID del objeto
     * @param clazz Clase del objeto
     * @return Optional con el objeto encontrado o vacío si no existe
     */
    <T> Optional<T> findById(Long id, Class<T> clazz);

    /**
     * Actualiza un objeto existente en la base de datos.
     *
     * @param <T> Tipo del objeto a actualizar
     * @param object Objeto con los datos actualizados
     * @return El objeto actualizado
     */
    <T> T update(T object);

    /**
     * Elimina un objeto de la base de datos.
     *
     * @param <T> Tipo del objeto a eliminar
     * @param id ID del objeto a eliminar
     * @param clazz Clase del objeto
     * @return true si se eliminó correctamente, false si no
     */
    <T> boolean delete(Long id, Class<T> clazz);

    /**
     * Busca todos los objetos de un tipo específico.
     *
     * @param <T> Tipo de objetos a buscar
     * @param clazz Clase de los objetos
     * @return Lista con todos los objetos encontrados
     */
    <T> List<T> findAll(Class<T> clazz);

    /**
     * Establece conexión con la base de datos.
     *
     * @return true si la conexión se estableció correctamente, false si no
     */
    boolean connect();

    /**
     * Cierra la conexión con la base de datos.
     */
    void disconnect();

    /**
     * Verifica si la conexión está activa.
     *
     * @return true si la conexión está activa, false si no
     */
    boolean isConnected();
}