package ejercicio2.database;

import ejercicio2.interfaces.IDataBaseManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementación de IDataBaseManager que simula una base de datos en memoria.
 */
public class DataBaseManager implements IDataBaseManager {

    private boolean connected;
    private final Map<Class<?>, Map<Long, Object>> database;
    private final Map<Class<?>, AtomicLong> idGenerators;

    /**
     * Constructor por defecto.
     */
    public DataBaseManager() {
        this.database = new HashMap<>();
        this.idGenerators = new HashMap<>();
        this.connected = false;
    }

    @Override
    public boolean connect() {
        // Simulación de conexión a base de datos
        this.connected = true;
        return true;
    }

    @Override
    public void disconnect() {
        this.connected = false;
    }

    @Override
    public boolean isConnected() {
        return this.connected;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T save(T object) {
        if (!isConnected()) {
            throw new IllegalStateException("No hay conexión con la base de datos");
        }

        Class<?> clazz = object.getClass();

        // Aseguramos que exista la tabla para esta clase
        database.putIfAbsent(clazz, new HashMap<>());

        // Aseguramos que exista un generador de IDs para esta clase
        idGenerators.putIfAbsent(clazz, new AtomicLong(1));

        // Asignamos un ID al objeto si no tiene uno
        try {
            java.lang.reflect.Field idField = clazz.getDeclaredField("id");
            idField.setAccessible(true);
            Long id = (Long) idField.get(object);

            if (id == null) {
                Long newId = idGenerators.get(clazz).getAndIncrement();
                idField.set(object, newId);
                database.get(clazz).put(newId, object);
            } else {
                database.get(clazz).put(id, object);
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error al guardar el objeto: " + e.getMessage(), e);
        }

        return object;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> findById(Long id, Class<T> clazz) {
        if (!isConnected()) {
            throw new IllegalStateException("No hay conexión con la base de datos");
        }

        Map<Long, Object> table = database.get(clazz);
        if (table == null) {
            return Optional.empty();
        }

        return Optional.ofNullable((T) table.get(id));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T update(T object) {
        if (!isConnected()) {
            throw new IllegalStateException("No hay conexión con la base de datos");
        }

        Class<?> clazz = object.getClass();

        try {
            java.lang.reflect.Field idField = clazz.getDeclaredField("id");
            idField.setAccessible(true);
            Long id = (Long) idField.get(object);

            if (id == null) {
                throw new IllegalArgumentException("No se puede actualizar un objeto sin ID");
            }

            Map<Long, Object> table = database.get(clazz);
            if (table == null || !table.containsKey(id)) {
                throw new IllegalArgumentException("El objeto con ID " + id + " no existe en la base de datos");
            }

            table.put(id, object);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error al actualizar el objeto: " + e.getMessage(), e);
        }

        return object;
    }

    @Override
    public <T> boolean delete(Long id, Class<T> clazz) {
        if (!isConnected()) {
            throw new IllegalStateException("No hay conexión con la base de datos");
        }

        Map<Long, Object> table = database.get(clazz);
        if (table == null || !table.containsKey(id)) {
            return false;
        }

        table.remove(id);
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findAll(Class<T> clazz) {
        if (!isConnected()) {
            throw new IllegalStateException("No hay conexión con la base de datos");
        }

        Map<Long, Object> table = database.get(clazz);
        if (table == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>((List<T>) table.values());
    }
}