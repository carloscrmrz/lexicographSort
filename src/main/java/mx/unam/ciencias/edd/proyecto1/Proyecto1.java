package mx.unam.ciencias.edd.proyecto1;

import mx.unam.ciencias.edd.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;

public class Proyecto1 {

	/* Metodo auxiliar para guardar el archivo ya ordenado en disco duro. */
	private static void escritura(String nombreArchivo, Lista<String> ordenada) {
		try {
			FileOutputStream fileOut = new FileOutputStream(nombreArchivo);
			OutputStreamWriter osOut = new OutputStreamWriter(fileOut);
			BufferedWriter out = new BufferedWriter(osOut);

			for ( String c : ordenada ) {
				out.write(c);
				out.newLine();
			}

			out.close();
		} catch ( IOException ioe ) {
			System.out.printf("No fue posible guardar en el archivo \"%s\"", nombreArchivo);
			System.exit(1);
		}
	}

	/** 
	 * Metodo que se encarga de la escritura del archivo, una vez ya ordenado.
	 * @param ordenada la lista ya ordenada.
	 */

	private static void write(Lista<String> ordenada) {
		boolean hasUserOutput = ArgumentProcessorProyecto1.userOutput();
		String userOutput;

		if ( hasUserOutput ) {
			userOutput = ArgumentProcessorProyecto1.getUserOutput();
			escritura(userOutput, ordenada);
			System.out.printf("\nGuardado exitosamente en \"%s\"\n", userOutput);
		} else {
			for ( String c : ordenada ) {
				System.out.println(c);
			}
		}

	}
	/* Crea una lista y la llena cargandola del disco duro. Despues la regresa*/
	private static Lista<String> lectura(Lista<String> listaArchivos) {
		Lista<String> lineas = new Lista<String>();

		for ( String f : listaArchivos ) {
			String nombreArchivo = f;
			try {
				FileInputStream fileIn = new FileInputStream(nombreArchivo);
				InputStreamReader isIn = new InputStreamReader(fileIn);
				BufferedReader in = new BufferedReader(isIn);
				carga(in, lineas);		
				in.close();
			} catch (IOException ioe) {
				System.out.printf("No se pudo cargar el archivo \"%s\".\n",
						  nombreArchivo);
				System.exit(1);
			}

			System.out.printf("\"%s\" cargado exitosamente.\n", nombreArchivo);
		}

		return lineas;
	}

	private static void carga(BufferedReader in, Lista<String> lista) throws IOException {
		String currentLine;
		while ( (currentLine = in.readLine() ) != null ) {
			lista.agrega(currentLine);
		}
	}	



	/* Imprime en pantalla como se usa el programa y lo termina */
	private static void uso() {
		System.out.println("Uso: java -jar proyecto1.jar [OPCIONES]... [ARCHIVOS]...\n\n" +
				   "Escribe la concatenacion ordenada de todos los ARCHIVO(s) a la salida estandar.\n" +
				   "Sin ARCHIVO, o cuando el ARCHIVO es -, se lee la entrada estandar\n\n" +
				   "Opciones:\n\n" +
				   "-r\t\t Imprime en orden inverso el resultado de las comparaciones.\n" +
				   "-o\t\t Define una salida diferente a la salida estandar.");
		System.exit(1);
	}

	public static void main(String[] args) {
		ArgumentProcessorProyecto1.separateList(args);
		boolean printReverse = ArgumentProcessorProyecto1.printReverse();

		Lista<String> filesList = ArgumentProcessorProyecto1.getFilesList();
		Lista<String> lineas = new Lista<String>();
		if ( filesList.getLongitud() == 0 ) {
			System.out.println("No se encontraron archivos, se leera de la entrada estandar");
			try {			
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				carga(br,lineas);
				br.close();
			} catch (IOException ioe) {
				uso();
				System.exit(1);
			}
		} else {
			lineas = lectura(filesList);
		}

		Lista<String> ordenada = lineas.mergeSort(new StringLexicographicalComparator());
		if ( printReverse ) {
			System.out.println("Se escogio la opcion de regresar la salida en orden inverso.");
			write(ordenada.reversa());
		} else {
			write(ordenada);
		}
	}

}
