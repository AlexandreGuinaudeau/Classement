package Divers;

public class StringManipulation
{
	public static String uppercaseFirstLetter(String s)
	{
		String firstLetter = s.substring(0, 1);
		String queue = s.substring(1);
		
		String uppercaseFirstLetter = firstLetter.toUpperCase();
		
		return uppercaseFirstLetter+queue;
	}
}
