package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<code>null</code>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
		super(elemento);
		color = Color.NINGUNO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        public String toString() {
		String s = "";
		if ( color == Color.ROJO )
			s += "R{";
		if ( color == Color.NEGRO )
			s += "N{";

		s += String.format("%s}", elemento);

		return s;
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)objeto;
	    return (color == vertice.color &&
			    super.equals(vertice));
            
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
	    super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
    	return new VerticeRojinegro(elemento);
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
	    VerticeRojinegro v = (VerticeRojinegro) vertice;
	    return v.color;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
	super.agrega(elemento);
	VerticeRojinegro v = (VerticeRojinegro) ultimoAgregado;
	v.color = Color.ROJO;

	rebalancea(v);	
    }

    private boolean isRed(VerticeRojinegro v ) {
	    return ( v != null && 
		     v.color == Color.ROJO );
    }

    private VerticeRojinegro getPadre(VerticeRojinegro v) { 
	    if ( v == null )
		    return null;

	    return (VerticeRojinegro)v.padre;
    }

    private VerticeRojinegro getAbuelo(VerticeRojinegro v) {
	    if ( v == null )
		    return null;
	    
	    return getPadre(getPadre(v));
    }

    private VerticeRojinegro getTio(VerticeRojinegro v) { 
	    if ( v == null )
		    return null;
	    VerticeRojinegro abuelo = getAbuelo(v);

	    if ( isLeftSon(getPadre(v)) ) 
		    return (VerticeRojinegro)abuelo.derecho;
	    else 
		    return (VerticeRojinegro)abuelo.izquierdo;
    }

    private void rebalancea(VerticeRojinegro vertice) {
	   if ( vertice == null ) 
		  return;

	   // Vemos que el vertice recibido sea ROJO.
	   if ( !isRed(vertice) ) 
		  return;

	   // Si el vertice no tiene padre significa que es la raiz
	   if ( vertice.padre == null ) {
		  vertice.color = Color.NEGRO;
		  return;
	   }
	   
	   // Si el padre ya es NEGRO el arbol ya esta balanceado.
	   VerticeRojinegro padre = getPadre(vertice);
	   if ( !isRed(padre) ) {
		   return;
	   } else {
		   VerticeRojinegro tio = getTio(vertice);
		   VerticeRojinegro abuelo = getAbuelo(vertice);

		   // Si el tio de vertice es ROJO pintamos al padre y al tio de NEGRO
		   // y hacemos recursion sobre el abuelo de v.
		   if ( tio != null ) {
	  		   if (isRed(tio)) {
		 		   tio.color = Color.NEGRO;
	 			   padre.color =  Color.NEGRO;
				   abuelo.color = Color.ROJO;
				   rebalancea(abuelo);
	 			   return;
			   }
		   } 

		   // Giramos los vertices si estan" cruzados"
		   if ( (isLeftSon(vertice) && !isLeftSon(padre)) || 
			(!isLeftSon(vertice) && isLeftSon(padre))    ) {
			   if ( isLeftSon(padre) ) {
				   super.giraIzquierda(padre);
				   padre = (VerticeRojinegro) vertice;
				   vertice = (VerticeRojinegro) padre.izquierdo;
			   } else {
				   super.giraDerecha(padre);
				   padre = (VerticeRojinegro) vertice;
				   vertice = (VerticeRojinegro) padre.derecho;
			   }
			   

		   }

		   // Cuando llegamos a esta parte del codigo el vertice y su padre
		   // estan del mismo lado, ie ambos son izquierdos o derechos.

		   padre.color = Color.NEGRO;
		   abuelo.color = Color.ROJO;

		   if ( isLeftSon(vertice) ) {
			   super.giraDerecha(abuelo);
		   } else {
			   super.giraIzquierda(abuelo);
		   }
		   return;
	   }
    }

    private boolean isLeftSon(Vertice v) {
	    return ( v != null &&
		     v.equals(v.padre.izquierdo) );
    }	

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
    	if ( elemento == null )
		return;

	VerticeRojinegro eliminado = (VerticeRojinegro) busca(elemento);
	if ( eliminado == null )
		return;

	VerticeRojinegro fantasma = (VerticeRojinegro) nuevoVertice(null);
	fantasma.color = Color.NEGRO;

	if ( eliminado.izquierdo != null && eliminado.derecho != null )
	       	eliminado = (VerticeRojinegro) intercambiaEliminable(eliminado);
	if ( eliminado.izquierdo == null && eliminado.derecho == null ) {
		eliminado.izquierdo = fantasma;
		fantasma.padre = eliminado;
	}

	VerticeRojinegro hijoEliminado;
	// Vemos si tenemos un hijo derecho, si no, tenemos el hijo izquierdo garantizado.
	if ( eliminado.derecho != null )
		hijoEliminado = (VerticeRojinegro) eliminado.derecho;
	else 
		hijoEliminado = (VerticeRojinegro) eliminado.izquierdo;

	eliminaVertice(eliminado);
	elementos--;

	// Si el hijo es ROJO, entonces el vertice eliminado es NEGRO, el hijo ahora es NEGRO.
	if ( isRed(hijoEliminado) )
		hijoEliminado.color = Color.NEGRO;

	// Si el eliminado es ROJO, el hijo sera NEGRO y no hacemos nada 

	// Ambos vertices son NEGROS, entonces rebalanceamos sobre el hijo.
	if ( !isRed(eliminado) && !isRed(hijoEliminado) )
		rebalanceaElimina(hijoEliminado);

	// Si el hijo resulta ser fantasma, lo eliminamos.
	if ( hijoEliminado.equals( fantasma ) )
		eliminaVertice(hijoEliminado);
    }

    private VerticeRojinegro getHermano(VerticeRojinegro v) {
	    if ( v == null ) 
		    return null;

	    if ( isLeftSon(v) ) 
		    return (VerticeRojinegro)v.padre.derecho;
	    return (VerticeRojinegro)v.padre.izquierdo;
    }	    

    private void rebalanceaElimina(VerticeRojinegro v) {
	    if ( v == null )
		    return;

	    VerticeRojinegro padre = getPadre(v);
	    VerticeRojinegro hermano;

	    // Caso 1, v no tiene padre.
	    if ( padre == null ) { 
		    return;
	    } else {
		    hermano = getHermano(v);

		    // Caso 2, hermano es ROJO y por ende padre es NEGRO.
		    if ( isRed(hermano) ) {
			    padre.color = Color.ROJO;
			    hermano.color = Color.NEGRO;
			    if ( isLeftSon(v) )
				    super.giraIzquierda(padre);
			    else 
				    super.giraDerecha(padre);
		    }

			    hermano = getHermano(v);

			    VerticeRojinegro hijoIzqHermano = (VerticeRojinegro) hermano.izquierdo;
			    VerticeRojinegro hijoDerHermano = (VerticeRojinegro) hermano.derecho;

			    // Checamos los posibles casos donde los sobrinos de nuestro vertice a rebalancear son ambos NEGROS
			    
			    if ( ( hijoIzqHermano == null && hijoDerHermano == null ) ||
				 ( hijoIzqHermano != null && !isRed(hijoIzqHermano) && hijoDerHermano == null ) ||
		                 ( hijoDerHermano != null && !isRed(hijoDerHermano) && hijoIzqHermano == null ) ||
		                 ( hijoIzqHermano != null && hijoDerHermano != null && !isRed(hijoIzqHermano) && !isRed(hijoDerHermano) ) ) {



				   // Caso 3
			           if ( padre.color == Color.NEGRO && hermano.color == Color.NEGRO ) {
       					   hermano.color = Color.ROJO;
    					   rebalanceaElimina(padre);
       					   return;
       				   }

				   // Caso 4
		  		   if ( padre.color == Color.ROJO && hermano.color == Color.NEGRO ) {
					   hermano.color = Color.ROJO;
       					   padre.color = Color.NEGRO;
       					   return;			 
				   }

		            }

			    // Casos donde los sobrinos de nuestro vertice a rebalancear es al menos uno ROJO

			    // Caso 5
			    if ( (hijoIzqHermano != null || hijoDerHermano != null) &&
			         ((hijoIzqHermano != null && isRed(hijoIzqHermano)) ||
				 (hijoDerHermano != null && isRed(hijoDerHermano))    )  ) {
				    
				    hermano.color = Color.ROJO;
				    // Caso 5a, hijoIzqHermano es ROJO y v es hijo izquierdo.
				    if ( isLeftSon(v) && hijoIzqHermano != null && isRed(hijoIzqHermano) ) {
					    hijoIzqHermano.color = Color.NEGRO;
					    super.giraDerecha(hermano);
				    }

				    // Caso 5b, hijoDerHermano es ROJO y v es hijo derecho.
				    if ( !isLeftSon(v) && hijoDerHermano != null && isRed(hijoDerHermano) ) {
					    hijoDerHermano.color = Color.NEGRO;
					    super.giraIzquierda(hermano);
				    }

				    // Actualizamos al hermano y sus hijos.
				    hermano        = getHermano(v);
				    hijoIzqHermano = (VerticeRojinegro) hermano.izquierdo;
				    hijoDerHermano = (VerticeRojinegro) hermano.derecho;

				    // Caso 6.
				    hermano.color = padre.color;
				    padre.color = Color.NEGRO;
				    if ( isLeftSon(v) ) {
					    hijoDerHermano.color = Color.NEGRO;
					    super.giraIzquierda(padre);
				    } else {
					    hijoIzqHermano.color = Color.NEGRO;
					    super.giraDerecha(padre);
				    }
			    }

		    }
	    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
}
