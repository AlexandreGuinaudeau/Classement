package Classements;

import BaseDeDonnees.*;
import ReglesEpreuves.*;
import Divers.*;

public class ResultatEquipeEpreuve
{
	private Equipe equipe;
	private Epreuve epreuve;
	private Classement classement; // Le classement auquel est associé ce résultat intermédiaire
	
	private double tempsReel = 0; // en secondes
	private double bonus = 0; // en secondes
	private double malus = 0; // en secondes
	
	private DetailsResultatEpreuve details = null;
	
	private boolean shouldBeIgnored = false;
	
	private boolean wasSuccessfullyCalculated = false;
	
	private ResultatEquipe resultat;
	
	public ResultatEquipeEpreuve(Classement classement, Equipe equipe, Epreuve epreuve, ResultatEquipe resultat)
	{
		this.equipe = equipe;
		this.epreuve = epreuve;
		this.classement = classement;
		this.resultat = resultat;
	}
	
	public void calculerResultat()
	{
		ResultatBrutEquipe brut = new ResultatBrutEquipe(equipe);
		
		log("\nApplication de la règle "+epreuve.penalitesBalises().nom()+"\n");
		epreuve.penalitesBalises().appliquerA(brut, this);
		log("Malus : "+AffichageDurees.heuresMinutesSecondes(malus)+" Bonus : "+AffichageDurees.heuresMinutesSecondes(bonus)+"\n");
		
		
		
		log("\nDebut de l'application des règles");
		
		
		
		if(epreuve.regles() != null)
		{
			for(RegleEpreuve regle : epreuve.regles())
			{
				log("\nApplication de la règle "+regle.nom()+"\n");
				regle.appliquerA(brut, this);
				log("Malus : "+AffichageDurees.heuresMinutesSecondes(malus)+" Bonus : "+AffichageDurees.heuresMinutesSecondes(bonus)+"\n");
			}
		}
		
		log("Fin de l'application des règles\n\n");
		
		if(epreuve.checkValidity(brut))
		{
			tempsReel = epreuve.calculTempsReel(brut);
			//cas du trail de merde
			if (epreuve.nom.equals("TrailMerde")){
			if ((tempsReel-bonus)>(1266)){bonus=tempsReel-1266;}
			}
			if (epreuve.nom.equals("VTTO1V")){
				System.out.println(tempsReel);
				}
			log("Temps réel : "+AffichageDurees.heuresMinutesSecondes(tempsReel)+" Temps compensé : "+AffichageDurees.heuresMinutesSecondes(tempsReel+malus-bonus)+"\n");
			
			this.wasSuccessfullyCalculated = true;
		}
		else
		{
			this.logAbandon("La liste des pointages est invalide dans abandon(resultatEquipeEpreuve) \n");
			this.wasSuccessfullyCalculated = true;
		}
	}
	
	public Classement classement()
	{
		return classement;
	}
	
	public Epreuve epreuve()
	{
		return epreuve;
	}
	
	public void addToTempsReel(double secondes)
	{
		this.tempsReel += secondes;
	}
	
	public void addToBonus(double secondes)
	{
		this.bonus += secondes;
	}
	
	public void addToMalus(double secondes)
	{
		this.malus += secondes;
	}
	
	public double tempsReel()
	{
		return this.tempsReel;
	}
	
	public double bonus()
	{
		return this.bonus;
	}
	
	public double malus()
	{
		return this.malus;
	}
	
	public void log(String toLog)
	{
		if(Divers.isSameDay(this.epreuve.circuit().date(), this.resultat.upToDate()))
		{
			ResultatEquipe.writeToLogFile(toLog);
		}
	}
	
	public void logAbandon(String detail)
	{
		ResultatEquipe.writeToLogFile("Abandon du calcul de l'épreuve "+this.epreuve.nom()+" pour l'équipe "+equipe.dossard()+" : "+detail+"\n\n");
	}
	
	public boolean shouldBeIgnored()
	{
		return this.shouldBeIgnored;
	}
	
	public boolean wasSuccessfullyCalculated()
	{
		return wasSuccessfullyCalculated;
	}
	
	public void setSuccessfullyCalculated(boolean success)
	{
		wasSuccessfullyCalculated = success;
	}
	
	public ResultatEquipe resultatEquipe()
	{
		return this.resultat;
	}
}
