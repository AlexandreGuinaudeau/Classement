package Classements;

import java.io.*;
import java.util.*;
import java.text.*;

import BaseDeDonnees.*;
import Interface.Keyboard;
import au.com.bytecode.opencsv.*;

public class FichierPointages
{
	private String path;
	
	private int doigtPos;
	private int dateLecturePos;
	private int startPos;
	private int finishPos;
	private int nbPointagesPos;
	private int debutPointages;
	
	public FichierPointages(String path)
	{
		this.path = path;
	}
	
	public void lirePointages() throws Exception
	{
		CSVReader reader = new CSVReader(new FileReader(path), ';');
		
		String[] nextLine = reader.readNext();
		
		for(int i=0 ; i<nextLine.length ; i++)
		{
			if(nextLine[i].equalsIgnoreCase("SI-Card"))
			{
				doigtPos = i;
			}
			else if(nextLine[i].equalsIgnoreCase("ST_CN"))
			{
				startPos = i;
			}
			else if(nextLine[i].equalsIgnoreCase("read at"))
			{
				dateLecturePos = i;
			}
			else if(nextLine[i].equalsIgnoreCase("FI_CN"))
			{
				finishPos = i;
			}
			else if(nextLine[i].equalsIgnoreCase("No. of punches"))
			{
				nbPointagesPos = i;
				debutPointages = i+1;
			}
		}
		
		while ((nextLine = reader.readNext()) != null)
		{
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			SimpleDateFormat dateLectureFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
			SimpleDateFormat dayFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date dateLecture = dateLectureFormat.parse(nextLine[this.dateLecturePos]);			
        	String jour = dayFormat.format(dateLecture);
			
			Doigt doigt = new Doigt(Integer.parseInt(nextLine[this.doigtPos]));
			
			// Depart
			if(!nextLine[this.startPos].isEmpty())
			{
				Boitier boitier = new Boitier(Integer.parseInt(nextLine[this.startPos]));
	        	
	        	String datePointage = nextLine[this.startPos+2];
	        	Date date = format.parse(jour+" "+datePointage);
	        	
	        	Pointage pointage = new Pointage(date,boitier,doigt,TypePointage.TYPE_START);
	        	
	        	BaseDeDonnees.shared().savePointage(pointage);
			}
			
			// Arrivee
			if(!nextLine[this.finishPos].isEmpty())
			{
				Boitier boitier = new Boitier(Integer.parseInt(nextLine[this.finishPos]));
	        	
	        	String datePointage = nextLine[this.finishPos+2];
	        	Date date = format.parse(jour+" "+datePointage);
	        	
	        	Pointage pointage = new Pointage(date,boitier,doigt,TypePointage.TYPE_FINISH);
	        	
	        	BaseDeDonnees.shared().savePointage(pointage);
			}
			
	        int nbPointages = Integer.parseInt(nextLine[this.nbPointagesPos]);
	        
	        for(int i=0 ; i<nbPointages ; i++)
	        {
	        	Boitier boitier = new Boitier(Integer.parseInt(nextLine[this.debutPointages+3*i]));
	        	
	        	String datePointage = nextLine[this.debutPointages+3*i+2];
	        	Date date = null;
	        	try{
	        		date = format.parse(jour+" "+datePointage);
	        	}
	        	catch(Exception e)
	        	{
	        		throw new IllegalArgumentException("Le pointage a une date invalide!");
	        	}
	        	
	        	Pointage pointage = new Pointage(date,boitier,doigt,TypePointage.TYPE_BALISE);
	        	
	        	BaseDeDonnees.shared().savePointage(pointage);
	        }
	    }
		
		Keyboard.KEYBOARD.println("L'acquisition du fichier de pointages a ete realisee avec succes.");
		System.out.println();
	}
}
