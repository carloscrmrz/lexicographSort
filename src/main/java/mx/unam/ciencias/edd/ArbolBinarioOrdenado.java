package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;

        /* Inicializa al iterador. */
        private Iterador() {
		
		pila = new Pila<Vertice>();
		Vertice n = raiz;

		while ( n != null ) {
			pila.mete(n);
			n = n.izquierdo;
		}
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return (!pila.esVacia());
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {
		Vertice temp = pila.saca();
		T e = temp.elemento;

		if ( temp.derecho != null ) {
			pila.mete(temp.derecho);
			temp = temp.derecho.izquierdo;
			while ( temp != null ) {
				pila.mete(temp);
				temp = temp.izquierdo;
			}
		}
		return e;
	}
    }

    /**
     * El vértice del último elemento agegado. Este vértice sólo se puede
     * garantizar que existe <em>inmediatamente</em> después de haber agregado
     * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
     * árbol se ejecuta después de haber agregado un elemento, el estado de esta
     * variable es indefinido.
     */
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    private void agrega(Vertice a, Vertice n) {
	    if ( a == null )
		    return;

	    if ( n.elemento.compareTo(a.elemento) <= 0 ) {
		    if ( a.izquierdo == null ) {
			    a.izquierdo = n;
			    n.padre = a;
		    } else {
			    agrega(a.izquierdo, n);
		    }
	    } else {
		    if ( a.derecho == null ) {
			    a.derecho = n;
			    n.padre = a;
		    } else {
			    agrega(a.derecho, n);
		    }
	    }
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
    	if ( elemento == null )
		throw new IllegalArgumentException();
    	Vertice n = nuevoVertice(elemento);
	elementos++;
	ultimoAgregado = n;

	if ( raiz == null ) {
		raiz = n;
	} else {
		agrega(raiz, n);
	}
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
    	if ( elemento == null )
		return;


	Vertice n = (Vertice) busca(elemento);
	if ( n == null )
		return;

	elementos--;

	if ( n.izquierdo != null && n.derecho != null ) {
		n = intercambiaEliminable(n);
    	}

	eliminaVertice(n);
    }

    private Vertice maximoEnSubArbol(Vertice v) {
	    if ( v.derecho == null )
		    return v;
	    return maximoEnSubArbol(v.derecho);
    }
    /**
     * Intercambia el elemento de un vértice con dos hijos distintos de
     * <code>null</code> con el elemento de un descendiente que tenga a lo más
     * un hijo.
     * @param vertice un vértice con dos hijos distintos de <code>null</code>.
     * @return el vértice descendiente con el que vértice recibido se
     *         intercambió. El vértice regresado tiene a lo más un hijo distinto
     *         de <code>null</code>.
     */
    protected Vertice intercambiaEliminable(Vertice vertice) {
	    Vertice max = maximoEnSubArbol(vertice.izquierdo);
	    T e = vertice.elemento;

	    vertice.elemento = max.elemento;
	    max.elemento = e;

	    return max;
    }

    private boolean isLeftSon(Vertice vertice) {
	    if ( vertice == null ) 
		    return false;

	    if ( vertice.equals(vertice.padre.izquierdo) )
			   return true;
	    return false;
    } 
    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice vertice) {
	    Vertice padre = vertice.padre;
	    Vertice hijo;
	    if ( vertice.izquierdo != null )
		    hijo = vertice.izquierdo;
	    else 
		    hijo = vertice.derecho;

	    if ( padre != null ) {
		    if (isLeftSon(vertice)) 
			    padre.izquierdo = hijo;
		    else 
			    padre.derecho = hijo;
	    } else {
		    raiz = hijo;
	    }

	    if ( hijo != null )
		    hijo.padre = padre;
    }

    private Vertice busca(T elemento, Vertice v) {
	if ( v == null )
		return null;

	if ( v.elemento.equals(elemento) )
		return v;

	if ( v.elemento.compareTo(elemento) >  0 ) {
		return busca(elemento, v.izquierdo);
	} else { 
		return busca(elemento, v.derecho);
	}

    }
    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <code>null</code>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <code>null</code> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
    		return busca(elemento, raiz);
    }

    /**
     * Regresa el vértice que contiene el último elemento agregado al
     * árbol. Este método sólo se puede garantizar que funcione
     * <em>inmediatamente</em> después de haber invocado al método {@link
     * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
     * ejecuta después de haber agregado un elemento, el comportamiento de este
     * método es indefinido.
     * @return el vértice que contiene el último elemento agregado al árbol, si
     *         el método es invocado inmediatamente después de agregar un
     *         elemento al árbol.
     */
    public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
        return ultimoAgregado;
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
	    Vertice v = (Vertice) vertice;
	    if ( v.izquierdo == null || v == null ) 
		    return;
	    Vertice izquierdo = v.izquierdo;

	    izquierdo.padre = v.padre;
	    if ( v.padre != null ) {
		    if ( v.equals(v.padre.izquierdo) ) {
			    v.padre.izquierdo = izquierdo;
		    } else { 
			    v.padre.derecho = izquierdo;
		    }
	    } else {
		    raiz = izquierdo;
	    }

	    v.padre = izquierdo;
	    v.izquierdo = izquierdo.derecho; 

	    if ( v.izquierdo != null ) {
		    izquierdo.derecho.padre = v;
	    }

	    izquierdo.derecho = v;
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
	    Vertice v = (Vertice) vertice;
	    if ( v.derecho == null || v == null )
		    return;
	    Vertice derecho = v.derecho;

	    derecho.padre = v.padre;
	    if ( v.padre != null ) {
		    if ( v.equals(v.padre.izquierdo) ) { 
			    v.padre.izquierdo = derecho;
		    } else {
			    v.padre.derecho = derecho;
		    } 
	    } else { 
		    raiz = derecho;
	    } 


	    v.padre = derecho;
	    v.derecho = derecho.izquierdo;

	    if ( v.derecho != null ) {
		    derecho.izquierdo.padre = v;
	    }

	    derecho.izquierdo = v;
    }

    private void dfsPreOrder(AccionVerticeArbolBinario<T> accion, Vertice v) {
	    if ( v == null )
		    return;

	    accion.actua(v);
	    dfsPreOrder(accion, v.izquierdo);
	    dfsPreOrder(accion, v.derecho);
    }

    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
	    	dfsPreOrder(accion, raiz);
    }

    private void dfsInOrder(AccionVerticeArbolBinario<T> accion, Vertice v) {
	    if ( v == null )
		    return;

	    dfsInOrder(accion, v.izquierdo);
	    accion.actua(v);
	    dfsInOrder(accion, v.derecho);
    }

    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
		dfsInOrder(accion, raiz);	    
    }

    private void dfsPostOrder(AccionVerticeArbolBinario<T> accion, Vertice v) {
	    if ( v == null )
		    return;

	    dfsPostOrder(accion, v.izquierdo);
	    dfsPostOrder(accion, v.derecho);
	    accion.actua(v);
    }
    /**
     * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
	    	dfsPostOrder(accion, raiz);
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
