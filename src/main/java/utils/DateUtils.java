package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("MM/dd/yy");

	public static String getCurrentDate() {
		LocalDate currentDate = LocalDate.now();
		return currentDate.format(FMT);
	}

	public static String getFutureDate() {
		LocalDate futureDate = LocalDate.now().plusDays(7);
		return futureDate.format(FMT);
	}
	
	public static String getDatePlusTwo() {
		LocalDate futureDate = LocalDate.now().plusDays(2);
		return futureDate.format(FMT);
	}
    public static String plusDays(int n)  { return LocalDate.now().plusDays(n).format(FMT); }
    public static String tomorrow()       { return plusDays(1); }
    public static String inOneWeek()      { return plusDays(7); }
    public static String inTwoDays()      { return plusDays(2); }
}


