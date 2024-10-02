package postilion.realtime.generictrantest.util;

import java.time.LocalDate;

public class Utils {
	
	public static String constructExpDateForCommand(String expiryDate) {
		String currentYearFirstTwoDigits = String.valueOf(LocalDate.now().getYear()).substring(0, 2);

	    // Extrae el año y el mes del parámetro
	    String year = currentYearFirstTwoDigits + expiryDate.substring(0, 2);
	    String month = expiryDate.substring(2);

	    // Crea una fecha con el día 01
	    String formattedDate = year + month + "01" + "00000000";

	    return formattedDate;
	}

}
