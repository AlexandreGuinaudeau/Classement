import Classements.*;
import Divers.KeyboardInput;
import BaseDeDonnees.*;
import Interface.Keyboard;
import Interface.SwingMain;
import ModeProgramme.*;
import Interface.*;

public class Programme {
  
	public static void main(String[] args) {
		try {
			Runtime.getRuntime().addShutdownHook(new OnShutDown());
			
			System.out.println("Bienvenue sur le programme de calcul de classements pour le Raid de l'X !");
			System.out.println();
		
			System.out.println("Preparation de la base de donnees...");
			System.out.println();
			BaseDeDonnees.prepare();
			System.out.println();
			System.out.println("Fin du chargement de la base de donnees.");
			System.out.println();
			
			SwingMain.main(null);
			
			ModeProgramme.startDefault();
		}
		catch(RuntimeException e)
		{
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}

}
