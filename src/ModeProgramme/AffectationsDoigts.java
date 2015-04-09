package ModeProgramme;

import Classements.*;
import Divers.*;
import Interface.Keyboard;
import BaseDeDonnees.*;

public class AffectationsDoigts extends ModeProgramme {

	@Override
	public String description() {
		return "Affectations des numeros de doigts aux equipes";
	}

	@Override
	public ModesProgramme mode() {
		return ModesProgramme.MODE_AFFECTATION_DOIGTS;
	}
	
	public void printHeader()
	{
		System.out.println();
		System.out.println("*************************************************************");
		System.out.println(description());
		System.out.println("*************************************************************");
		System.out.println();
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
			Equipe equipe = null;
			int dossard = 0;
			
			while(equipe==null)
			{
				try
				{
					System.out.print("Numero d'equipe (dossard) : ");
					String numEquipe = Keyboard.KEYBOARD.myTake();
					//String numEquipe = KeyboardInput.getInput();
					if(numEquipe.equals("q"))
					{
						System.exit(0);
					}
					
					if(numEquipe.equals("r"))
					{
						shouldStop = true;
						break;
					}
				
					dossard = Integer.parseInt(numEquipe);
					
					equipe = Equipe.equipeAvecDossard(dossard);
					
					if(equipe==null)
					{
					  String msg="Le numero d'equipe indique n'existe pas !";
					  System.out.println(msg);
		        Keyboard.KEYBOARD.editOutput(msg);
					}
				}
				catch(Exception e)
				{
					throw new IllegalArgumentException("Dossard invalide.");
				}
			}
			
			if(shouldStop)
			{
				break;
			}
			
			if(equipe.doigt()!=null)
			{
			  String msg="L'equipe n°"+dossard+" a actuellement le doigt electronique n°"+equipe.doigt().id();
				System.out.println(msg);
				Keyboard.KEYBOARD.editOutput(msg);
			}
			else
			{
				String msg="L'equipe n°"+dossard+" n'est pour le moment pas associee a un doigt electronique";
				System.out.println(msg);
        Keyboard.KEYBOARD.editOutput(msg);
			}
			
			System.out.print("Nouveau numero de doigt electronique (taper 'a' pour annuler) : ");
//			String numDoigt = KeyboardInput.getInput();
			String numDoigt = Keyboard.KEYBOARD.myTake();
			
			if(numDoigt.equals("q"))
			{
				System.exit(0);
			}
			
			if(numDoigt.equals("r"))
			{
				shouldStop = true;
				break;
			}
			
			if(!numDoigt.equals("a"))
			{
				int doigt;
				
				try
				{
					doigt = Integer.parseInt(numDoigt);
					
					BaseDeDonnees.shared().setDoigtForEquipe(new Doigt(doigt), equipe);
					String msg = "Le numero de doigt a ete mis a jour avec succes";
					System.out.println(msg);
	        Keyboard.KEYBOARD.editOutput(msg);
					System.out.println();
				}
				catch(Exception e)
				{
					throw new IllegalArgumentException("Numero de doigt invalide : "+numDoigt);
				}
			}
			else
			{
				System.out.println();
			}
			
		}
		ModeProgramme.mode(ModesProgramme.MODE_DEFAULT).toggle();
	}

}
