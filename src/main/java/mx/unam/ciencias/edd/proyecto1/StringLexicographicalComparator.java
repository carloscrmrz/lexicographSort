package mx.unam.ciencias.edd.proyecto1;

import java.util.Comparator;

public class StringLexicographicalComparator implements Comparator<String> {

	@Override
	public int compare(String str1, String str2) {
		str1 = StripAccents.stripAccents(str1); 
		str2 = StripAccents.stripAccents(str2); 
		str1 = str1.replaceAll("[¿?¡!,.]", "");
		str2 = str2.replaceAll("[¿?¡!,.]", "");

		return str1.compareToIgnoreCase(str2);
	}
}
