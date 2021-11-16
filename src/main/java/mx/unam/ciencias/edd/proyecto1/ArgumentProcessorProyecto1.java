package mx.unam.ciencias.edd.proyecto1;

import mx.unam.ciencias.edd.Lista;

public final class ArgumentProcessorProyecto1 {
	
	private static Lista<String> flagsList = new Lista<String>();
	private static Lista<String> filesList = new Lista<String>();
	private static String userOutput;

	/* Hacemos al constructor privado, evitando intanciaciones de clase */
	private ArgumentProcessorProyecto1() {}

	/** 
	 * Regresa la lista de banderas.
	 * @return la lista de banderas.
	 */
	public static Lista<String> getFlagsList() {
		return flagsList;
	}

	/**
	 * Regresa la lista de ARCHIVO(s).
	 * @return la lista de ARCHIVO(s).
	 */
	public static Lista<String> getFilesList() {
		return filesList;
	}

	/** 
	 * Regresa (si existe) el ARCHIVO al que se escribira la salida del programa.
	 * @return (si existe) el ARCHIVO al que se escribira la salida del programa.
	 */
	public static String getUserOutput() {
		return userOutput;
	}
	
	/**
	 * Depura la lista de argumentos, separando las banderas de los ARCHIVO(s) a ordenar
	 * @param args la lista de argumentos en linea de comandos del programa.
	 */
	public static void separateList(String[] args) {
		for ( int i = 0; i < args.length; i++) {
			if ( args[i].equals(userOutput) )
					continue;
			switch (args[i]) {
				case "-o":
					flagsList.agrega(args[i]);
					if (args[i+1].indexOf("-") != -1) 
						throw new IllegalArgumentException("Nombre de archivo de salida invalido");
					userOutput = args[i+1];
					break;
				case "-r":
					flagsList.agrega(args[i]);
					break;
				default:
					if (args[i].indexOf("-") != 1 )
						filesList.agrega(args[i]);
					break;
			}
		}
	}	

	/** 
	 * Busca si a nuestra aplicacion se le paso la bandera de reversa
	 * @param args la lista de argumentos de linea de comandos del programa.
	 * @return true si se tiene la bandera de reversa, false en otro caso. 
	 */
	public static boolean printReverse() {
		for ( String f : flagsList ) {
			if ( f.equals("-r") )
				return true;
		}
		return false;
	}

	/** 
	 * Busca si a nuestra aplicacion se le paso la bandera de output
	 * @param args la lista de argumentos de linea de comandos del programa.
	 * @return true si se tiene la bandera de output, false en otro caso.
	 */
	public static boolean userOutput() {
		for ( String o : flagsList ) {
			if ( o.equals("-o") )
				return true;
		}
		return false;
	}
}
