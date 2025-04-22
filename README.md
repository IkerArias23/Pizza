# Pizza
# Ejercicios Prácticos de Técnicas de Programación

Este proyecto contiene dos ejercicios prácticos implementados en Java que demuestran diferentes técnicas de programación:

1. **Ejercicio 1**: Implementación de una clase Matriz en Java
2. **Ejercicio 2**: Sistema de Gestión de Pedidos de Pizza en Línea con inyección de dependencias

## Requisitos

- Java JDK 11 o superior
- Maven 3.6 o superior

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   ├── ejercicio1/
│   │   │   └── Matriz.java
│   │   └── ejercicio2/
│   │       ├── database/
│   │       │   └── DataBaseManager.java
│   │       ├── auth/
│   │       │   └── Authenticator.java
│   │       ├── order/
│   │       │   └── OrderManager.java
│   │       ├── payment/
│   │       │   └── PaymentProcessor.java
│   │       ├── model/
│   │       │   ├── User.java
│   │       │   ├── Order.java
│   │       │   └── Pizza.java
│   │       ├── exceptions/
│   │       │   ├── AuthenticationException.java
│   │       │   ├── OrderException.java
│   │       │   └── PaymentException.java
│   │       ├── interfaces/
│   │       │   ├── IDataBaseManager.java
│   │       │   ├── IAuthenticator.java
│   │       │   ├── IOrderManager.java
│   │       │   └── IPaymentProcessor.java
│   │       └── Main.java
└── test/
    ├── java/
    │   ├── ejercicio1/
    │   │   └── MatrizTest.java
    │   └── ejercicio2/
    │       ├── auth/
    │       │   └── AuthenticatorTest.java
    │       ├── order/
    │       │   └── OrderManagerTest.java
    │       ├── payment/
    │       │   └── PaymentProcessorTest.java
    │       └── database/
    │           └── DataBaseManagerTest.java
```

## Ejercicio 1: Clase Matriz

Este ejercicio implementa una clase `Matriz` que permite:

- Crear una matriz con valores iniciales
- Imprimir la matriz en forma legible
- Calcular la transpuesta de la matriz

### Ejemplo de uso

```java
Matriz m = new Matriz(new int[][]{{1, 2}, {3, 4}});
m.imprimir();
// Imprime:
// 1 2
// 3 4

Matriz mTranspuesta = m.transpuesta();
mTranspuesta.imprimir();
// Imprime:
// 1 3
// 2 4
```

## Ejercicio 2: Sistema de Gestión de Pedidos de Pizza

Este ejercicio implementa un sistema de gestión de pedidos de pizza con inyección de dependencias. Los componentes principales son:

- **DataBaseManager**: Maneja la conexión y operaciones con la base de datos
- **Authenticator**: Gestiona la autenticación de usuarios
- **OrderManager**: Administra los pedidos de los usuarios
- **PaymentProcessor**: Procesa los pagos de los pedidos

### Principios de Diseño Aplicados

1. **Inyección de Dependencias**: Las dependencias se inyectan a través de constructores
2. **Principio de Responsabilidad Única**: Cada clase tiene una única responsabilidad
3. **Principio de Inversión de Dependencias**: Se depende de abstracciones, no de implementaciones concretas
4. **Principio de Abierto/Cerrado**: El código está abierto para extensión pero cerrado para modificación

### Ejemplo de uso

```java
// Creación de dependencias
IDataBaseManager dbManager = new DataBaseManager();
IAuthenticator auth = new Authenticator(dbManager);
IPaymentProcessor paymentProcessor = new PaymentProcessor(dbManager);
IOrderManager orderManager = new OrderManager(dbManager, paymentProcessor);

// Uso del sistema
try {
    // Registro de usuario
    User user = auth.register("usuario1", "contraseña123", "usuario@example.com");
    
    // Crear pizzas
    List<Pizza> pizzas = new ArrayList<>();
    pizzas.add(new Pizza("Margarita", "Grande", 12.99));
    pizzas.add(new Pizza("Pepperoni", "Mediana", 10.99));
    
    // Crear pedido
    Order order = orderManager.createOrder(user, pizzas);
    
    // Procesar pago
    String transactionId = paymentProcessor.processPayment(
        order, "1234-5678-9012-3456", "12/25", "123"
    );
    
    // Actualizar estado del pedido
    orderManager.updateOrderStatus(order.getId(), "PROCESSING");
    
    System.out.println("Pedido creado con éxito. ID de transacción: " + transactionId);
    
} catch (Exception e) {
    System.err.println("Error: " + e.getMessage());
}
```

## Compilación y Ejecución

Para compilar el proyecto:

```bash
mvn clean compile
```

Para ejecutar las pruebas:

```bash
mvn test
```

Para crear un JAR ejecutable:

```bash
mvn package
```

Para ejecutar el programa principal:

```bash
java -jar target/ejercicio-practico-1.0-SNAPSHOT.jar
```

## Evaluación

### Ejercicio 1 (3 Puntos):
- Claridad y limpieza del código (30%)
- Implementación correcta de la responsabilidad única (40%)
- Uso correcto de las características de Java (30%)

### Ejercicio 2 (7 Puntos):
- Creación de Clases y Métodos (25%)
- Inyección de Dependencias (25%)
- Pruebas (25%)
- Reutilización de Código y Principios de Diseño de Software (25%)

## Autor

Desarrollado para el curso de Técnicas de Programación.
