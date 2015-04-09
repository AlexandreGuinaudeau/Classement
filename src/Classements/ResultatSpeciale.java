package Classements;

import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

import BaseDeDonnees.*;
import Divers.AffichageDurees;

public class ResultatSpeciale
{
	private static FileWriter logFile = null;
	
	private double tempsDuJour = 0; // en secondes
	private double tempsTotal = 0; // en secondes
	private double coeff = 0;
	
	private Equipe equipe;
	private Classement classement; // Le classement auquel est associe ce resultat intermediaire
	private Date upToDate;
	
	private boolean abandon;
	private boolean nonpartie;
	
	private boolean wasSuccessfullyCalculated = true;
	
	private ResultatBrutEquipe brut;
	
	public static Integer SpecialeACalculer = null;
	
	public static FileWriter logFile()
	{
		if(logFile == null)
		{
			try
			{
				logFile = new FileWriter("resultats_equipe.log");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				throw new IllegalArgumentException("Impossible d'ouvrir le fichier log des resultats d'equipes : "+e.getLocalizedMessage());
			}
		}
		return logFile;
	}
	
	public ResultatSpeciale(Equipe equipe, Classement classement, Date upToDate)
	{
		this.equipe = equipe;
		this.classement = classement;
		this.upToDate = upToDate;
		
		this.brut = new ResultatBrutEquipe(equipe);
	}
	
	public static void writeToLogFile(String s)
	{
		try
		{
			logFile().write(s);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new IllegalArgumentException("Impossible d'ecrire dans le fichier log des resultats d'equipes : "+e.getLocalizedMessage());
		}
	}
	
	public void calculerResultat(Epreuve epreuve)
	{
		if(((classement.typeClassement() == TypeClassements.CLASSEMENT_GRIMPEUR) && epreuve.hasSectionGrimpeur()) || ((classement.typeClassement() == TypeClassements.CLASSEMENT_SPRINTEUR) && epreuve.hasSectionSprinteur()))
		{
			writeToLogFile("Calcul du resultat pour l'epreuve "+epreuve.nom()+"\n");
			writeToLogFile("Coeff avant calcul "+this.coeff+"\n");
			
			if(epreuve.checkValidity(brut))
			{
				if(classement.typeClassement() == TypeClassements.CLASSEMENT_GRIMPEUR)
				{
					epreuve.calculTempsSpecialeGrimpeur(brut, this);
				}
				else
				{
					epreuve.calculTempsSpecialeSprinteur(brut, this);
				}
			}
			else
			{
				writeToLogFile("La liste des pointages est invalide mais heu c'est faux \n");
				this.wasSuccessfullyCalculated = false;
			}
			

			writeToLogFile("Coeff apres calcul "+this.coeff+"\n\n");
		}
	}
	
	public void calculerResultat(Circuit circuit)
	{
		SimpleDateFormat format = new SimpleDateFormat("EEEE");
		
		writeToLogFile("Calcul du resultat sur le circuit du "+format.format(circuit.date())+"\n\n");
		
		if(circuit.isDepartEnMasse())
		{
			writeToLogFile("Depart en masse : heure de depart "+format.format(circuit.date())+"\n\n");
		}
		
		for(Epreuve epreuve : circuit.epreuves())
		{
			this.calculerResultat(epreuve);
		}
	}
	
	public void calculerResultat()
	{
		writeToLogFile("\n****************************************************\n");
		writeToLogFile("Calcul du resultat de l'equipe n°"+this.equipe.dossard()+"\n\n");
		
		Circuit[] circuits = BaseDeDonnees.shared().circuits();
		
		for(Circuit circuit : circuits)
		{
			if(circuit.date().compareTo(upToDate)<=0) 
			{
				this.calculerResultat(circuit);
			}
		}

		writeToLogFile("\nFin du calcul du resultat de l'equipe n°"+this.equipe.dossard()+"\n");
		writeToLogFile("****************************************************\n");
	}
	
	public void addToTempsDuJour(double secondes)
	{
		this.tempsDuJour += secondes;
	}
	public void addToTempsTotal(double secondes)
	{
		this.tempsTotal += secondes;
	}
	
	public void addToCoeff(double value)
	{
		this.coeff += value;
	}
	
	public double tempsDuJour()
	{
		return this.tempsDuJour;
	}
	public double tempsTotal()
	{
		return this.tempsTotal;
	}
	
	public double coeff()
	{
		return this.coeff;
	}
	
	public Equipe equipe()
	{
		return equipe;
	}
	
	public Classement classement()
	{
		return classement;
	}
	
	public static void closeLogFile()
	{
		try
		{
			logFile().close();
			ResultatSpeciale.logFile = null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Une erreur s'est produite : "+e.getLocalizedMessage());
		}
	}
	
	public boolean wasSuccessfullyCalculated()
	{
		return wasSuccessfullyCalculated;
	}
	
	public void setSuccessfullyCalculated(boolean success)
	{
		wasSuccessfullyCalculated = success;
	}
	
	public Date calculationDate()
	{
		return upToDate;
	}
}
