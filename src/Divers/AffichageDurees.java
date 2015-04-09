package Divers;

public class AffichageDurees
{
	public static String heuresMinutesSecondes(double fsecondes)
	{
		String res = "";
		if(fsecondes < 0)
		{
			res += "-";
			fsecondes = -fsecondes;
		}
		
		long secondes = Math.round(fsecondes);
		
		long minutes = (secondes/60)%60;
		long heures = secondes/3600;
		secondes %= 60;
		
		res += String.format("%dh%02d'%02d\"", heures, minutes, secondes);
		
		return res;
	}
	
	public static String minutesSecondes(double fsecondes)
	{
		String res = "";
		if(fsecondes < 0)
		{
			res += "-";
			fsecondes = -fsecondes;
		}
		
		long secondes = Math.round(fsecondes);
		
		long minutes = secondes/60;
		secondes %= 60;
		
		res += String.format("%d'%02d\"", minutes, secondes);
		
		return res;
	}
}
