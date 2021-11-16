package mx.unam.ciencias.edd;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas no aceptan a <code>null</code> como elemento.</p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase interna privada para nodos. */
    private class Nodo {
        /* El elemento del nodo. */
        private T elemento;
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nodo con un elemento. */
        private Nodo(T elemento) {
		this.elemento = elemento;
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nuevo iterador. */
        private Iterador() {
		anterior = null;
		siguiente = cabeza;
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
		return ( siguiente != null );
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
		if ( siguiente != null ) {
			anterior = siguiente;
			siguiente = siguiente.siguiente;
		} else { 
			throw new NoSuchElementException();
		}

		return anterior.elemento;
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
        	return ( anterior != null );
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
        	if ( anterior != null ) {
			siguiente = anterior;
			anterior = anterior.anterior;
		} else { 
			throw new NoSuchElementException();
		}

		return siguiente.elemento;
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
        	anterior = null;
		siguiente = cabeza;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
        	siguiente = null;
		anterior = rabo;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
	    return longitud;
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    @Override public int getElementos() {
    	return longitud;
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
    	return ( cabeza == null ); 
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. El método es idéntico a
     * {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
    	if ( elemento == null ) 
		throw new IllegalArgumentException();

	Nodo n = new Nodo(elemento);
	if ( rabo == null ) {
		cabeza = rabo = n;
	} else {
		rabo.siguiente = n;
		n.anterior = rabo;
		rabo = n;
	}

	longitud++;
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
	    agrega(elemento);
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
	    if ( elemento == null ) 
		    throw new IllegalArgumentException();

	    Nodo n = new Nodo(elemento);
	    if ( cabeza == null ) {
		    cabeza = rabo = n;
	    } else {
		    cabeza.anterior = n;
		    n.siguiente = cabeza;
		    cabeza = n;
	    }

	    longitud++;
    }

    private Nodo getIEsimoNodo(int i) {
	    int index = 0;

	    Nodo n = cabeza;
	    while ( n != null ) {
		    if ( index ==  i ) 
			    return n;
	    	    n = n.siguiente;
		    index++;
	    }

	    return n;
    }
    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) {
	    if ( elemento == null ) 
		    throw new IllegalArgumentException();

	    if ( i < 1 ) {
		    agregaInicio(elemento);
	    } else if ( i > longitud - 1 ) {
		    agregaFinal(elemento);
	    } else {
		    Nodo n = new Nodo(elemento);
		    Nodo s = getIEsimoNodo(i);
		    Nodo a = s.anterior;

		    n.anterior = a;
		    a.siguiente = n;

		    n.siguiente = s;
		    s.anterior = n;

		    longitud++;
	    }
    }

    private Nodo buscarNodo(T elemento) {
	    Nodo n =  cabeza;

	    while ( n != null ) {
		    if ( n.elemento.equals(elemento) ) 
			    return n;
		    n = n.siguiente;
	    }

	    return n;
    }

    private void eliminaNodo(Nodo n) {
	    if ( rabo == cabeza ) {
		    rabo = null;
		    cabeza = null;
	    } else if ( n == cabeza ) { 
		    Nodo s = n.siguiente;
		    s.anterior = null;
		    cabeza = s;
	    } else if ( n == rabo ) {
		    Nodo a = n.anterior;
		    a.siguiente = null;
		    rabo = a;
	    } else {
		    Nodo s = n.siguiente;
		    Nodo a = n.anterior;

		    a.siguiente = s;
		    s.anterior = a;
	    }

	    longitud--;
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
    	if ( elemento == null )
		return;
	
	Nodo n = buscarNodo(elemento);
	if ( n == null )
		return;

	eliminaNodo(n);
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
	    if ( cabeza == null )
		    throw new NoSuchElementException();

	    T e = cabeza.elemento;
	    eliminaNodo(cabeza);

	    return e;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
	    if ( rabo == null ) 
		    throw new NoSuchElementException();

	    T e = rabo.elemento;
	    eliminaNodo(rabo);

	    return e;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <code>true</code> si <code>elemento</code> está en la lista,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
    	return (buscarNodo(elemento) != null);
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
	    Lista<T> reversa = new Lista<T>();
	    Nodo n = cabeza;

	    while ( n != null ) {
		    reversa.agregaInicio(n.elemento);
		    n = n.siguiente;
	    }

	    return reversa;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
	    Lista<T> copia = new Lista<T>();
	    Nodo n = cabeza;

	    while ( n != null ) {
		    copia.agregaFinal(n.elemento);
		    n = n.siguiente;
	    }

	    return copia;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
    	cabeza = null;
	rabo = null;
	longitud = 0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
	    if ( cabeza == null )
		    throw new NoSuchElementException();

	    return cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
	    if ( rabo == null ) 
		    throw new NoSuchElementException();

	    return rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
	    if ( i < 0 || i > longitud - 1 )
		    throw new ExcepcionIndiceInvalido();

	    return getIEsimoNodo(i).elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
	    int index = 0;

	    Nodo n = cabeza;
	    while ( n != null ) {
		    if ( n.elemento.equals(elemento) ) 
			    return index;
		    n = n.siguiente;
		    index++;
	    }

	    return -1;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
    	if ( cabeza == null ) 
		return "[]";

    	String s = "[";
	Nodo n = cabeza;

	while ( n != rabo ) {
		s += String.format("%d, ", n.elemento);
		n = n.siguiente;
	}

	s += String.format("%d]", rabo.elemento);

	return s;
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la lista es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;
	if ( longitud != lista.longitud ) {
		return false;
	} else {
		Nodo a = cabeza;
		Nodo o = lista.cabeza;

		while ( a != null ) {
			if ( !a.elemento.equals(o.elemento) ) 
				return false;
			a = a.siguiente;
			o = o.siguiente;
		} 
	}
		return true;
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }

    private Lista<T> merge(Lista<T> left, Lista<T> right, Comparator<T> comparador) {
	    Lista<T> juntas = new Lista<T>();

	    Nodo l = left.cabeza;
	    Nodo r = right.cabeza;

	    while ( l != null && r != null ) {
		    if ( comparador.compare( l.elemento, r.elemento ) <= 0 ) {
			    juntas.agrega(l.elemento);
			    l = l.siguiente;
		    } else { 
			    juntas.agrega(r.elemento);
			    r = r.siguiente;
		    }
	    }

	    while ( l != null ) { 
		    juntas.agrega(l.elemento);
		    l = l.siguiente;
	    }

	    while ( r != null ) {
		    juntas.agrega(r.elemento);
		    r = r.siguiente;
	    }

	    return juntas;
    }

    private Lista<T> mergeSort(Comparator<T> comparador, Lista<T> lista) {
	    // Caso base de la recursion, si la lista es vacio o tiene un solo elemento
	    // esta sera trivialmente ordenada.
	    if ( lista.longitud <= 1 ) {
		    return lista;
	    }

	    Lista<T> right = new Lista<T>();
	    Lista<T> left = new Lista<T>();

	    // Partimos en dos nuestra lista.
	    Nodo n = lista.cabeza;
	    for ( int i = 0; i < lista.longitud; i++ ) {
		    if ( i < ( lista.longitud / 2 ) ) {
			    left.agrega(n.elemento);
			    n = n.siguiente;
		    } else { 
			    right.agrega(n.elemento);
			    n = n.siguiente;
		    }
	    }

	    // Las volvemos a partir de manera recursiva hasta llegar al caso base.
	    left = mergeSort(comparador, left);
	    right =  mergeSort(comparador, right);

	    // Ordenamos nuestras listas con nuestro metodo auxiliar.
	    return merge(left, right, comparador);
    }

    /**
     * Regresa una copia de la lista, pero ordenada. Para poder hacer el
     * ordenamiento, el método necesita una instancia de {@link Comparator} para
     * poder comparar los elementos de la lista.
     * @param comparador el comparador que la lista usará para hacer el
     *                   ordenamiento.
     * @return una copia de la lista, pero ordenada.
     */
    public Lista<T> mergeSort(Comparator<T> comparador) {
	    return mergeSort(comparador, copia());
    }

    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>>
    Lista<T> mergeSort(Lista<T> lista) {
        return lista.mergeSort((a, b) -> a.compareTo(b));
    }

    /**
     * Busca un elemento en la lista ordenada, usando el comparador recibido. El
     * método supone que la lista está ordenada usando el mismo comparador.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador con el que la lista está ordenada.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public boolean busquedaLineal(T elemento, Comparator<T> comparador) {
	    Nodo n = cabeza;

	    while ( n != null ) {
		    if ( comparador.compare(n.elemento, elemento ) == 0 )
			    return true;
		    n = n.siguiente;
	    } 
	    
	    return false;
    }

    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista donde se buscará.
     * @param elemento el elemento a buscar.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public static <T extends Comparable<T>>
    boolean busquedaLineal(Lista<T> lista, T elemento) {
        return lista.busquedaLineal(elemento, (a, b) -> a.compareTo(b));
    }
}
