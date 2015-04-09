package ModeProgramme;

import BaseDeDonnees.BaseDeDonnees;
import Divers.KeyboardInput;
import Interface.Keyboard;

import java.io.*;

import Classements.*;

public class AcquisitionPointages extends ModeProgramme {

  @Override
	public String description() {
		return "Acquisition des listes de pointages a partir d'un fichier CSV SI-Config";
	}

	@Override
	public ModesProgramme mode() {
		return ModesProgramme.MODE_ACQUISITION_POINTAGES;
	}
	
	public void acquisition(File file)
	{
		System.out.println();
		Keyboard.KEYBOARD.println("Debut de l'acquistion du fichier "+file.getName());
		System.out.println();
		
		FichierPointages fichier = new FichierPointages(file.getAbsolutePath());
		
		try
		{
			fichier.lirePointages();
		}
		catch(Exception e)
		{
		  Keyboard.KEYBOARD.println("Une erreur s'est produite : "+e.getLocalizedMessage());
			e.printStackTrace();
			System.exit(-1);
			return;
		}
	}
	
	public void toggle()
	{
		super.toggle();
		
		System.out.println();
		System.out.println("A tout moment, taper r pour revenir au menu precedent, ou q pour quitter.");
		System.out.println();
		
		while(true)
		{
			File file = null;
			int i=0;
			boolean getAcquisition = false;
			
			while(file==null)
			{
				try
				{
				  i++;
				  System.out.println("i = "+i+" et file = "+file);
					System.out.print("Chemin d'acces au fichier a importer (glissez-deposez le fichier sur cette fenetre) : ");
					String path = Keyboard.KEYBOARD.myTake();
					//String path = KeyboardInput.getInput();
					if(path.equals("q"))
					{
						System.exit(0);
					}
					
					if(path.equals("r"))
					{
					  ModeProgramme.mode(ModesProgramme.MODE_DEFAULT).toggle();
						return;
					}
				
					file = new File(path);
					
					if(!file.exists())
					{
					  Keyboard.KEYBOARD.println("Le fichier indique n'existe pas !");
						file = null;
					} else {
					  getAcquisition = true;
					}
					break;
				}
				catch(Exception e)
				{
					throw new IllegalArgumentException("Chemin d'acces invalide");
				}
			}
			if (getAcquisition){
			  this.acquisition(file);
			  getAcquisition = false;
			}
		}
	}
}
