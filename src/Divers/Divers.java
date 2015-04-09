package Divers;

import java.text.SimpleDateFormat;
import java.util.*;

public class Divers {

	public static boolean isSameDay(Date d1,Date d2)
	{
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String day1 = format.format(d1);
		String day2 = format.format(d2);
		
		return day1.equals(day2);
	}
}
