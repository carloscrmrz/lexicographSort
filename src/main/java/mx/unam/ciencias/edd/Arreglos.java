package mx.unam.ciencias.edd;

import java.util.Comparator;

/**
 * Clase para ordenar y buscar arreglos genéricos.
 */
public class Arreglos {

    /* Constructor privado para evitar instanciación. */
    private Arreglos() {}

    private static <T> void intercambiaIndices(T[] arreglo, int i, int j) {
	    // Creamos una variable temporal para guardar alguno de los valores del arreglo
	    T x = arreglo[i];

	    // Hacemos el intercambio de indices
	    arreglo[i] = arreglo[j];
	    arreglo[j] = x;
    }

    private static <T> void
    quickSort(T[] arreglo, int left, int right, Comparator<T> comparador) {
	    if ( right <=  left ) 
		    return;

	    int i = left + 1;
	    int j = right;

	    while ( i < j ) {
		    if ( comparador.compare(arreglo[left], arreglo[i]) < 0 &&
			 comparador.compare(arreglo[j], arreglo[left]) <= 0   ) {

            		intercambiaIndices(arreglo, i, j);
			i++;
			j--;
		    } else {
			    if ( comparador.compare(arreglo[i], arreglo[left]) <= 0 ) {
				   i++;
			    } else {
				   j--;
			    }
		    }
	    }

	   if ( comparador.compare(arreglo[left], arreglo[i]) < 0 )
			  i--;

	   intercambiaIndices(arreglo, left, i);
	   quickSort(arreglo, left, i-1, comparador);
	   quickSort(arreglo, i+1, right, comparador);
    }

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordenar el arreglo.
     */

    public static <T> void
    quickSort(T[] arreglo, Comparator<T> comparador) {
	    quickSort(arreglo,0,arreglo.length-1,comparador);
    }

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    quickSort(T[] arreglo) {
        quickSort(arreglo, (a, b) -> a.compareTo(b));
    }

    
    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordernar el arreglo.
     */
    public static <T> void
    selectionSort(T[] arreglo, Comparator<T> comparador) {
        for ( int i = 0; i < arreglo.length; i++ ) {
		int m = i;
		for ( int j = i + 1; j < arreglo.length; j++ ) {
			if (comparador.compare(arreglo[j], arreglo[m]) < 0 )
				m = j;
		}

		intercambiaIndices(arreglo, i, m);
	}
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    selectionSort(T[] arreglo) {
        selectionSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo dónde buscar.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador para hacer la búsqueda.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T> int
    busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador) {
	    int min = 0;
	    int max = arreglo.length - 1;
	    int guess;

	    while ( min < max + 1) {
		    guess = min + ( max - min ) / 2;
		    if ( comparador.compare(arreglo[guess], elemento) == 0 ) 
				    return guess;
	 	    if ( comparador.compare(elemento, arreglo[guess]) < 0 ) {
			    max = guess - 1;
		    } else {
			    min = guess + 1;
		    }
	    }

	    return -1;
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     * @param elemento el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int
    busquedaBinaria(T[] arreglo, T elemento) {
        return busquedaBinaria(arreglo, elemento, (a, b) -> a.compareTo(b));
    }
}
