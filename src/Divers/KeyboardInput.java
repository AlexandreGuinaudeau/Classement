package Divers;


import java.io.*;

public class KeyboardInput {
	public static String getInput()
	{
	    String input = null;

	    InputStreamReader stream = null;
	    BufferedReader reader = null;
	    try {
	        // Open a stream to stdin
	        stream = new InputStreamReader(System.in);

	        // Create a buffered reader to stdin
	        reader = new BufferedReader(stream);

	        // Try to read the string
	        input = reader.readLine();           
	    } catch (IOException e) {
	        e.printStackTrace();
	    } 

	    return input;
	}
}
