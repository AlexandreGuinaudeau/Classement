package ModeProgramme;

import BaseDeDonnees.BaseDeDonnees;
import Classements.*;
import Divers.KeyboardInput;
import Interface.Keyboard;

import java.text.*;
import java.util.*;

public class EquipesSansPointages extends ModeProgramme {

	@Override
	public String description() {
		return "Afficher la liste des equipes sans pointages";
	}

	@Override
	public ModesProgramme mode() {
		return ModesProgramme.MODE_EQUIPESSANSPOINTAGES;
	}
	
	public void toggle()
	{
		super.toggle();
		
		Equipe[] sansDoigts = BaseDeDonnees.shared().equipesSansDoigt();
		
		if(sansDoigts == null || sansDoigts.length == 0)
		{
			Keyboard.KEYBOARD.println("Toutes les equipes sont affectees a un doigt.");
		}
		else
		{
			String msg = "Attention, les equipes suivantes sont parties et n'ont pas abandonne,\n"
			    + "pourtant elles ne sont pas affectees a un doigt : ";
			for(Equipe equipe : sansDoigts)
			{
				msg+="\n"+equipe.dossard();
			}
			Keyboard.KEYBOARD.println(msg);
		}
		
		boolean shouldStop = false;
		
		System.out.println();
		System.out.println("A tout moment, taper r pour revenir au menu precedent, ou q pour quitter.");
		System.out.println();
		
		SimpleDateFormat format = new SimpleDateFormat("EEEE dd MMMM");
		
		while(!shouldStop)
		{
			Date date = null;
			
			while(date==null)
			{
				System.out.print("Jour a verifier : ");
				System.out.println();
				
				Circuit[] circuits = BaseDeDonnees.shared().circuits();
				while (!Keyboard.KEYBOARD.currentChoice.isEmpty()){
				  Keyboard.KEYBOARD.currentChoice.removeFirst();
				  System.out.println("currentChoice is not empty!");
				  throw new RuntimeException();
				}
				
				for(int i=0 ; i<circuits.length ; i++)
				{
					System.out.println((i+1)+" : "+format.format(circuits[i].date()));
					Keyboard.KEYBOARD.currentChoice.add((i+1)+" : "+format.format(circuits[i].date()));
				}
				
				System.out.println();
				
				System.out.print("Jour choisi : ");
				
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
					  ModeProgramme.mode(ModesProgramme.MODE_DEFAULT).toggle();
						return;
					}
				
					int numChoix = Integer.parseInt(choix);
					
					date = circuits[numChoix-1].date();
				}
				catch(Exception e)
				{
					throw new IllegalArgumentException("Jour invalide.");
				}
			}
			
			System.out.println();
			
			Equipe[] sansPointages = BaseDeDonnees.shared().equipesSansPointages(date);
			
			if(sansPointages == null || sansPointages.length == 0)
			{
				Keyboard.KEYBOARD.println("Toutes les equipes parties et n'ayant pas abandonne\n"
				    + " ont une liste de pointages pour le "+format.format(date)+".");
			}
			else
			{
				String msg = "Attention, les equipes suivantes sont parties et n'ont pas abandonne,\n"
				    + "pourtant elles n'ont pas de liste de pointages pour le "+format.format(date)+" : ";
				for(Equipe equipe : sansPointages)
				{
					msg+="\n"+equipe.dossard();
				}
				Keyboard.KEYBOARD.println(msg);
			}
			
			System.out.println();
		}
		
		
	}

}
