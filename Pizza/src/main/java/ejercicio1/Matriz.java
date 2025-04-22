package ejercicio1;

/**
 * Clase que representa una matriz bidimensional de enteros.
 * Permite crear, imprimir y calcular la transpuesta de una matriz.
 */
public class Matriz {
    private int[][] elementos;
    private int filas;
    private int columnas;

    /**
     * Constructor que inicializa la matriz con un array bidimensional de enteros.
     *
     * @param elementos Array bidimensional que representa los elementos de la matriz
     */
    public Matriz(int[][] elementos) {
        if (elementos == null || elementos.length == 0) {
            throw new IllegalArgumentException("La matriz no puede estar vacía");
        }

        this.filas = elementos.length;
        this.columnas = elementos[0].length;

        // Verificar que todas las filas tengan la misma longitud
        for (int i = 0; i < filas; i++) {
            if (elementos[i].length != columnas) {
                throw new IllegalArgumentException("Todas las filas deben tener la misma longitud");
            }
        }

        // Copiar los elementos para evitar modificaciones externas
        this.elementos = new int[filas][columnas];
        for (int i = 0; i < filas; i++) {
            System.arraycopy(elementos[i], 0, this.elementos[i], 0, columnas);
        }
    }

    /**
     * Imprime la matriz en un formato legible,
     * con cada fila en una nueva línea y elementos separados por espacios.
     */
    public void imprimir() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                System.out.print(elementos[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Calcula la transpuesta de la matriz.
     *
     * @return Una nueva instancia de Matriz que representa la transpuesta
     */
    public Matriz transpuesta() {
        int[][] transpuesta = new int[columnas][filas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                transpuesta[j][i] = elementos[i][j];
            }
        }

        return new Matriz(transpuesta);
    }

    /**
     * Obtiene los elementos de la matriz.
     *
     * @return Array bidimensional con los elementos de la matriz
     */
    public int[][] getElementos() {
        // Devolver una copia para evitar modificaciones externas
        int[][] copia = new int[filas][columnas];
        for (int i = 0; i < filas; i++) {
            System.arraycopy(elementos[i], 0, copia[i], 0, columnas);
        }
        return copia;
    }

    /**
     * Obtiene el número de filas de la matriz.
     *
     * @return Número de filas
     */
    public int getFilas() {
        return filas;
    }

    /**
     * Obtiene el número de columnas de la matriz.
     *
     * @return Número de columnas
     */
    public int getColumnas() {
        return columnas;
    }
}