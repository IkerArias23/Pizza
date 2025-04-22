package ejercicio1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MatrizTest {

    @Test
    public void testCreacionMatriz() {
        int[][] elementos = {{1, 2}, {3, 4}};
        Matriz matriz = new Matriz(elementos);

        assertNotNull(matriz);
        assertArrayEquals(elementos, matriz.getElementos());
        assertEquals(2, matriz.getFilas());
        assertEquals(2, matriz.getColumnas());
    }

    @Test
    public void testCreacionMatrizVacia() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Matriz(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Matriz(new int[][]{});
        });
    }

    @Test
    public void testCreacionMatrizIrregular() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Matriz(new int[][]{{1, 2}, {3}});
        });
    }

    @Test
    public void testTranspuesta() {
        int[][] elementos = {{1, 2, 3}, {4, 5, 6}};
        Matriz matriz = new Matriz(elementos);

        Matriz transpuesta = matriz.transpuesta();

        assertNotNull(transpuesta);
        assertEquals(3, transpuesta.getFilas());
        assertEquals(2, transpuesta.getColumnas());

        int[][] elementosTranspuestos = transpuesta.getElementos();
        assertEquals(1, elementosTranspuestos[0][0]);
        assertEquals(4, elementosTranspuestos[0][1]);
        assertEquals(2, elementosTranspuestos[1][0]);
        assertEquals(5, elementosTranspuestos[1][1]);
        assertEquals(3, elementosTranspuestos[2][0]);
        assertEquals(6, elementosTranspuestos[2][1]);
    }

    @Test
    public void testTranspuestaMatrizCuadrada() {
        int[][] elementos = {{1, 2}, {3, 4}};
        Matriz matriz = new Matriz(elementos);

        Matriz transpuesta = matriz.transpuesta();

        assertNotNull(transpuesta);
        assertEquals(2, transpuesta.getFilas());
        assertEquals(2, transpuesta.getColumnas());

        int[][] elementosTranspuestos = transpuesta.getElementos();
        assertEquals(1, elementosTranspuestos[0][0]);
        assertEquals(3, elementosTranspuestos[0][1]);
        assertEquals(2, elementosTranspuestos[1][0]);
        assertEquals(4, elementosTranspuestos[1][1]);
    }

    @Test
    public void testEncapsulamiento() {
        int[][] elementos = {{1, 2}, {3, 4}};
        Matriz matriz = new Matriz(elementos);

        // Modificamos el array original
        elementos[0][0] = 99;

        // Verificamos que la matriz no se ve afectada
        int[][] elementosMatriz = matriz.getElementos();
        assertEquals(1, elementosMatriz[0][0]);

        // Modificamos el array devuelto
        elementosMatriz[1][1] = 88;

        // Verificamos que la matriz interna no se ve afectada
        assertEquals(4, matriz.getElementos()[1][1]);
    }
}