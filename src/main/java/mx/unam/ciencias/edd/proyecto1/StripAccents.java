package mx.unam.ciencias.edd.proyecto1;

public class StripAccents {

	public static String stripAccents(String str1) {

		str1 = str1.replaceAll("[áàâä]", "a");
		str1 = str1.replaceAll("[èéêë]","e");
		str1 = str1.replaceAll("[ìíîï]","i");
		str1 = str1.replaceAll("[òóôö]","o");
		str1 = str1.replaceAll("[ùûúü]","u");

		str1 = str1.replaceAll("[ÀÂÁÄ]","A");
		str1 = str1.replaceAll("[ÈÊÉË]","E");
		str1 = str1.replaceAll("[ÌÎÏÍ]","I");
		str1 = str1.replaceAll("[ÒÔÓÖ]","O");
		str1 = str1.replaceAll("[ÙÛÚÜ]","U");

		return str1;
	}
}
