package ModeProgramme;

import java.util.*;

import Interface.*;
import Divers.KeyboardInput;

public abstract class ModeProgramme
{
	protected static Map<ModesProgramme,ModeProgramme> modes = null;
	
	protected ModeProgramme parent;
	protected ModesProgramme[] children;
	
	public abstract String description();
	
	public abstract ModesProgramme mode();
	
	private boolean shouldStop;
	
	protected static ModeProgramme mode(ModesProgramme aMode)
	{
		if(modes==null)
		{
			modes = new HashMap<ModesProgramme,ModeProgramme>();
		}
		if(modes.containsKey(aMode))
		{
			return modes.get(aMode);
		}
		ModeProgramme res;
		
		switch(aMode)
		{
			case MODE_DEFAULT:
			{
				res = new DefaultMode();
				break;
			}
			case MODE_IMPORTATION:
			{
				res = new ImportationMode();
				break;
			}
			case MODE_EQUIPES:
			{
				res = new GestionEquipes();
				break;
			}
			case MODE_AFFECTATION_DOIGTS:
			{
				res = new AffectationsDoigts();
				break;
			}
			case MODE_EQUIPESSANSPOINTAGES:
			{
				res = new EquipesSansPointages();
				break;
			}
			case MODE_CALCUL_CLASSEMENTS:
			{
				res = new CalculClassements();
				break;
			}
			case MODE_LECTURE_POINTAGES:
			{
				res = new LecturePointages();
				break;
			}
			case MODE_ACQUISITION_POINTAGES:
			{
				res = new AcquisitionPointages();
				break;
			}
			default:
			{
				System.out.println("Mode non supporte");
				res = null;
				break;
			}
		}
		
		modes.put(aMode, res);
		return res;
	}
	
	public void printHeader()
	{
		System.out.println();
		System.out.println("*************************************************************");
		System.out.println(description());
		System.out.println("*************************************************************");
		System.out.println();
    Keyboard.KEYBOARD.modeActuel = mode().name();
    Keyboard.KEYBOARD.editOutput("Menu : "+description());
	}
	
	public void toggle()
	{		
		if(children != null)
		{
			shouldStop = false;
			
			while(!shouldStop)
			{
				printHeader();
				
				printSousMenu();
				System.out.println();
				
				try
				{
					choixSousMenu();
				}
				catch(Exception e)
				{
					e.printStackTrace();
					System.out.println("Erreur lors du choix du mode. "+e.getLocalizedMessage());
				}
			}
		}
		else
		{
			printHeader();
		}
	}
	
	public static void startDefault()
	{
		ModeProgramme.mode(ModesProgramme.MODE_DEFAULT).toggle();
	}
	
	public void printSousMenu()
	{
		for(int i=0 ; i< children.length ; i++)
		{
			System.out.println((i+1)+" : "+ModeProgramme.mode(children[i]).description());
		}
		if(parent!=null)
		{
			System.out.println("r : Menu precedent - "+parent.description());
		}
		System.out.println("q : Quitter");
	}
	
	public void choixSousMenu() throws InterruptedException
	{
		System.out.print("Choix de mode : ");
//		try {
			String mode = Keyboard.KEYBOARD.myTake();
			//String mode = KeyboardInput.getInput();
	
			if(mode.equals("q"))
			{
				System.exit(0);
			}
	
			System.out.println();
	
			if(parent != null && mode.equals("r"))
			{
				shouldStop = true;
				ModeProgramme.mode(ModesProgramme.MODE_DEFAULT).toggle();
				return;
			}
	
			int choix;
	
			try
			{
				choix = Integer.parseInt(mode);
			}
			catch(Exception e)
			{
				throw new IllegalArgumentException("Mode non reconnu : "+mode);
			}
	
			assert (choix>=1 && choix<=children.length) : "le mode choisi n'existe pas !";
	
			ModeProgramme.mode(children[choix-1]).toggle();
//		} catch (InterruptedException e){
//			throw new IllegalArgumentException("InterruptedException : "+e.getLocalizedMessage());
//		}
	}
}
