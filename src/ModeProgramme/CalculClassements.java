package ModeProgramme;

import java.util.*;

import BaseDeDonnees.BaseDeDonnees;
import Classements.*;
import Divers.KeyboardInput;
import Interface.Keyboard;

import java.text.*;

public class CalculClassements extends ModeProgramme
{
	@Override
	public String description() {
		return "Calcul des classements";
	}

	@Override
	public ModesProgramme mode() {
		return ModesProgramme.MODE_CALCUL_CLASSEMENTS;
	}

	@Override
	public void toggle() {
		super.toggle();
		
		boolean shouldStop = false;
		
		System.out.println();
		System.out.println("A tout moment, taper r pour revenir au menu precedent, ou q pour quitter.");
		System.out.println();
		
		while(!shouldStop)
		{
			Classement classement = null;
			
			while(classement==null)
			{
				System.out.println("Choix du classement a calculer : ");
				System.out.println();
				
				Classement.printChoixClassements();
				System.out.println();
				
				System.out.print("Classement choisi : ");
				
				try
				{
					String choix = Keyboard.KEYBOARD.myTake();
					//String choix = KeyboardInput.getInput();
					if(choix.equals("q"))
					{
						System.exit(0);
					}
					
					if(choix.equals("r"))
					{
						shouldStop = true;
						ModeProgramme.mode(ModesProgramme.MODE_DEFAULT).toggle();
						break;
					}
				
					int numChoix = Integer.parseInt(choix);
					
					classement = Classement.classementAvecChoix(numChoix);
				}
				catch(Exception e)
				{
					String msg = "Le classement demande n'existe pas.";
					System.out.println(msg);
					Keyboard.KEYBOARD.editOutput(msg);
				}
				System.out.println();
			}
			
			if(shouldStop)
			{
				break;
			}
			
			Date upToDate = null;
			
			while(upToDate==null)
			{
				System.out.println("Classement a calculer : ");
				System.out.println();
				
				Circuit[] circuits = BaseDeDonnees.shared().circuits();
				
				SimpleDateFormat format = new SimpleDateFormat("EEEE dd MMMM");
				
				while (!Keyboard.KEYBOARD.currentChoice.isEmpty()){
          Keyboard.KEYBOARD.currentChoice.removeFirst();
          System.out.println("currentChoice is not empty!");
          throw new RuntimeException();
        }
				//Keyboard.KEYBOARD.currentChoice.add("\n["+"]");
				
				for(int i=0 ; i<circuits.length-1 ; i++)
				{
					Circuit circuit = circuits[i];
					
					Date date = circuit.date();
					
					System.out.println((i+1)+" : Classement provisoire du "+format.format(date));
					Keyboard.KEYBOARD.currentChoice.add((i+1)+" : Classement provisoire du "+format.format(date));
				}
				{
					Circuit circuit = circuits[circuits.length-1];
				
					Date date = circuit.date();
				
					System.out.println(circuits.length + " : Classement final du " + format.format(date));
					Keyboard.KEYBOARD.currentChoice.add(circuits.length + " : Classement final du " + format.format(date));
				}
				System.out.println();
				
				System.out.print("Classement choisi ('a' pour annuler le choix precedent) : ");
				
				try
				{
					String choix = Keyboard.KEYBOARD.myTake();
					//String choix = KeyboardInput.getInput();
					if(choix.equals("q"))
					{
						System.exit(0);
					}
					
					if(choix.equals("r"))
					{
						shouldStop = true;
						ModeProgramme.mode(ModesProgramme.MODE_DEFAULT).toggle();
						break;
					}
					
					if(choix.equals("a"))
					{
						break;
					}
				
				
					int numChoix = Integer.parseInt(choix);
					
					upToDate = circuits[numChoix-1].date();
				}
				catch(Exception e)
				{
				  String msg = "Le classement demande n'existe pas.";
          System.out.println(msg);
          Keyboard.KEYBOARD.editOutput(msg);
				}
				System.out.println();
			}
			
			if(shouldStop)
			{
				break;
			}
			
			classement.calculClassement(upToDate);
		}
	}

}
