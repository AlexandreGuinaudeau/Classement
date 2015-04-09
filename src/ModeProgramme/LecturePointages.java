package ModeProgramme;

import BaseDeDonnees.BaseDeDonnees;
import Divers.KeyboardInput;
import Interface.Keyboard;
import Classements.*;

public class LecturePointages extends ModeProgramme {

	@Override
	public String description() {
		return "Verification des pointages des equipes";
	}

	@Override
	public ModesProgramme mode() {
		return ModesProgramme.MODE_LECTURE_POINTAGES;
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
						ModeProgramme.mode(ModesProgramme.MODE_DEFAULT).toggle();
						break;
					}
				
					dossard = Integer.parseInt(numEquipe);
					
					equipe = Equipe.equipeAvecDossard(dossard);
					
					if(equipe==null)
					{
						Keyboard.KEYBOARD.println("Le numero d'equipe indique n'existe pas !");
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
			
			ResultatBrutEquipe brut = new ResultatBrutEquipe(equipe);
			
			brut.affichage();
		}
	}

}
