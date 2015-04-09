package Classements;

import java.text.SimpleDateFormat;
import Divers.*;
import java.util.*;
import java.io.*;

import BaseDeDonnees.*;
import Divers.AffichageDurees;

public class ResultatEquipe
{
	private static FileWriter logFile = null;
	
	double tempsCompenseTotal=0;
	
	double tempsReelDuJour; // en secondes
	double bonusDuJour; // en secondes
	double malusDuJour; // en secondes
	double cumuleSpecialesDuJour; // en secondes
	
	double tempsReelTotal; // en secondes
	double bonusTotal; // en secondes
	double malusTotal; // en secondes
	
	private Equipe equipe;
	private Classement classement; // Le classement auquel est associé ce résultat intermédiaire
	private Date upToDate;
	
	private boolean abandon;
	private boolean nonpartie;
	
	private boolean wasSuccessfullyCalculated = true;
	
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
				throw new IllegalArgumentException("Impossible d'ouvrir le fichier log des résultats d'équipes : "+e.getLocalizedMessage());
			}
		}
		return logFile;
	}
	
	public ResultatEquipe(Equipe equipe, Classement classement, Date upToDate)
	{
		this.equipe = equipe;
		this.classement = classement;
		this.upToDate = upToDate;
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
			throw new IllegalArgumentException("Impossible d'écrire dans le fichier log des résultats d'équipes : "+e.getLocalizedMessage());
		}
	}
	
	public void writeToLogFileIfDateOk(String s,Circuit circuit)
	{
		if(Divers.isSameDay(upToDate, circuit.date()))
		{
			ResultatEquipe.writeToLogFile(s);
		}
	}
	
	public void calculerResultat(Epreuve epreuve)
	{
		this.writeToLogFileIfDateOk("Calcul du résultat pour l'épreuve "+epreuve.nom()+"\n",epreuve.circuit());
		
		ResultatEquipeEpreuve resultat = new ResultatEquipeEpreuve(this.classement,this.equipe,epreuve,this);
		
		resultat.calculerResultat();
		
		this.wasSuccessfullyCalculated &= resultat.wasSuccessfullyCalculated();
		
		if(Divers.isSameDay(epreuve.circuit().date(), upToDate))
		{
			this.addToBonusDuJour(resultat.bonus());
			this.addToMalusDuJour(resultat.malus());
		}
		
		this.addToBonusTotal(resultat.bonus());
		this.addToMalusTotal(resultat.malus());
	}
	
	public double calculerTempsReel(Circuit circuit)
	{
		Date depart = null;
		Date arrivee = null;
		ResultatBrutEquipe brut = new ResultatBrutEquipe(equipe);
		
		if(circuit.isDepartEnMasse())
		{
			depart = circuit.date();
			
			if(depart == null)
			{
				throw new RuntimeException("Le circuit débute par un départ en masse, mais aucune date de départ n'a été définie.");
			}
		}
		else
		{
			Pointage start = brut.start(circuit);
			
			depart = start.date();
		}
		
		Pointage finish = brut.finish(circuit);
		
		arrivee = finish.date();
		
		return ((double)(arrivee.getTime() - depart.getTime()))/1000;
	}
	
	public void calculerResultat(Circuit circuit)
	{
		SimpleDateFormat format = new SimpleDateFormat("EEEE");
		SimpleDateFormat format2 = new SimpleDateFormat("hh:mm:ss");
		
		writeToLogFile("Calcul du résultat sur le circuit du "+format.format(circuit.date())+"\n\n");
		
		if(circuit.isDepartEnMasse())
		{
			this.writeToLogFileIfDateOk("Départ en masse : heure de départ "+format2.format(circuit.date())+"\n\n",circuit);
		}
		
		double tempsReel = 0;
		
		try
		{
			tempsReel = this.calculerTempsReel(circuit);
		}
		catch(Exception e)
		{
			System.out.println("Impossible de calculer le temps réel de l'équipe "+this.equipe.dossard()+" sur le circuit !");
		}
		
		if(Divers.isSameDay(circuit.date(), upToDate))
		{
			this.addToTempsReelDuJour(tempsReel);
		}
		
		this.addToTempsReelTotal(tempsReel);
		
		
		this.writeToLogFileIfDateOk("Temps réel du jour : "+AffichageDurees.heuresMinutesSecondes(this.tempsReelDuJour)+"\n",circuit);
		this.writeToLogFileIfDateOk("Temps compensé du jour : "+AffichageDurees.heuresMinutesSecondes(this.tempsCumuleDuJour())+"\n",circuit);
		this.writeToLogFileIfDateOk("Temps réel total : "+AffichageDurees.heuresMinutesSecondes(this.tempsReelTotal)+"\n",circuit);
		this.writeToLogFileIfDateOk("Temps compensé total : "+AffichageDurees.heuresMinutesSecondes(this.tempsCumuleTotal())+"\n",circuit);
		
		
		/*
		 * modif pour débuggage, mise en commentaire
		writeToLogFile("Temps réel du jour : "+AffichageDurees.heuresMinutesSecondes(this.tempsReelDuJour)+"\n");
		writeToLogFile("Temps compensé du jour : "+AffichageDurees.heuresMinutesSecondes(this.tempsCumuleDuJour())+"\n");
		writeToLogFile("Temps réel total : "+AffichageDurees.heuresMinutesSecondes(this.tempsReelTotal)+"\n");
		writeToLogFile("Temps compensé total : "+AffichageDurees.heuresMinutesSecondes(this.tempsCumuleTotal())+"\n");
		*/
		if(Divers.isSameDay(circuit.date(), upToDate))
			writeToLogFile("Temps total "+AffichageDurees.heuresMinutesSecondes(this.tempsCompenseTotal));
		for(Epreuve epreuve : circuit.epreuves())
		{
			this.calculerResultat(epreuve);
			this.writeToLogFileIfDateOk("\n Fin du calcul de l'épreuve\n\n",circuit);
			
			this.writeToLogFileIfDateOk("Temps réel du jour : "+AffichageDurees.heuresMinutesSecondes(this.tempsReelDuJour)+"\n",circuit);
			this.writeToLogFileIfDateOk("Temps compensé du jour : "+AffichageDurees.heuresMinutesSecondes(this.tempsCumuleDuJour())+"\n",circuit);
			this.writeToLogFileIfDateOk("Temps réel total : "+AffichageDurees.heuresMinutesSecondes(this.tempsReelTotal)+"\n",circuit);
			this.writeToLogFileIfDateOk("Temps compensé total : "+AffichageDurees.heuresMinutesSecondes(this.tempsCumuleTotal())+"\n",circuit);
		}
		
		if(circuit.equals(BaseDeDonnees.shared().circuits()[0]) && equipe.dossard()==83){
			this.writeToLogFileIfDateOk("\n Bonus de 4 minutes pour compenser le temps perdu suite a un pb de velo\n\n",circuit);
			if(Divers.isSameDay(circuit.date(), upToDate))
			{
				this.addToBonusDuJour(4*60);
			}
			
			this.addToBonusTotal(4*60);
		}
		
		if(circuit.equals(BaseDeDonnees.shared().circuits()[0]) && equipe.dossard()==105){
			this.writeToLogFileIfDateOk("\n Bonus de 31 minutes pour compenser le temps perdu suite a un pb de velo\n\n",circuit);
			if(Divers.isSameDay(circuit.date(), upToDate))
			{
				this.addToBonusDuJour(31*60);
			}
			
			this.addToBonusTotal(31*60);
		}
		
		if(circuit.equals(BaseDeDonnees.shared().circuits()[0]) && equipe.dossard()==13){
			this.writeToLogFileIfDateOk("\n Bonus de 1 seconde pour aide au transport\n\n",circuit);
			if(Divers.isSameDay(circuit.date(), upToDate))
			{
				this.addToBonusDuJour(1);
			}
			
			this.addToBonusTotal(1);
		}
		
		if(circuit.equals(BaseDeDonnees.shared().circuits()[0]) && equipe.dossard()==100){
			this.writeToLogFileIfDateOk("\n Bonus de 27 minutes pour mauvais embranchement apres ravito\n\n",circuit);
			if(Divers.isSameDay(circuit.date(), upToDate))
			{
				this.addToBonusDuJour(27*60);
			}
			
			this.addToBonusTotal(27*60);
		}
		
		if(circuit.equals(BaseDeDonnees.shared().circuits()[0]) && equipe.dossard()==112){
			this.writeToLogFileIfDateOk("\n Bonus de 12 minutes pour compenser le temps perdu a reparer des velos (meritent un trophee)\n\n",circuit);
			if(Divers.isSameDay(circuit.date(), upToDate))
			{
				this.addToBonusDuJour(12*60);
			}
			
			this.addToBonusTotal(12*60);
		}
		
		if(circuit.equals(BaseDeDonnees.shared().circuits()[0]) && equipe.dossard()==83){
			this.writeToLogFileIfDateOk("\n Bonus de 4 minutes pour compenser le temps perdu suite a un pb de velo\n\n",circuit);
			if(Divers.isSameDay(circuit.date(), upToDate))
			{
				this.addToBonusDuJour(4*60);
			}
			
			this.addToBonusTotal(4*60);
		}
		
		if(circuit.equals(BaseDeDonnees.shared().circuits()[0]) && equipe.dossard()==11){
			this.writeToLogFileIfDateOk("\n Bonus de 30 minutes pour poiçonnage balise 38\n\n",circuit);
			if(Divers.isSameDay(circuit.date(), upToDate))
			{
				this.addToBonusDuJour(30*60);
			}
			
			this.addToBonusTotal(30*60);
		}
		if(circuit.equals(BaseDeDonnees.shared().circuits()[0]) && equipe.dossard()==31){
			this.writeToLogFileIfDateOk("\n Pour equilibrer... bonus de 3h\n\n",circuit);
			if(Divers.isSameDay(circuit.date(), upToDate))
			{
				this.addToBonusDuJour(3*3600);
			}
			
			this.addToBonusTotal(3*3600);
		}
		
		if(circuit.equals(BaseDeDonnees.shared().circuits()[0]) && equipe.dossard()==134){
			this.writeToLogFileIfDateOk("\n Bonus de 10, erreur pointage balises CO optionnelle + 60 VTTO\n\n",circuit);
			if(Divers.isSameDay(circuit.date(), upToDate))
			{
				this.addToBonusDuJour(10*60+60*60);
			}
			
			this.addToBonusTotal(10*60+60*60);
		}
		
		if(circuit.equals(BaseDeDonnees.shared().circuits()[1]) && equipe.dossard()==97){
			this.writeToLogFileIfDateOk("\n Bonus de 15, aide a l'equipe 97\n\n",circuit);
			if(Divers.isSameDay(circuit.date(), upToDate))
			{
				this.addToBonusDuJour(15*60);
			}
			
			this.addToBonusTotal(15*60);
		}
		
		if(circuit.equals(BaseDeDonnees.shared().circuits()[2]) && equipe.dossard()==44){
			this.writeToLogFileIfDateOk("\n Bonus de 5, pneu creve avant depart\n\n",circuit);
			if(Divers.isSameDay(circuit.date(), upToDate))
			{
				this.addToBonusDuJour(5*60);
			}
			
			this.addToBonusTotal(5*60);
		}
		
		if(circuit.equals(BaseDeDonnees.shared().circuits()[2]) && equipe.dossard()==77){
			this.writeToLogFileIfDateOk("\nGrosse looze pneu crevé\n\n",circuit);
			if(Divers.isSameDay(circuit.date(), upToDate))
			{
				this.addToBonusDuJour(60*60);
			}
			
			this.addToBonusTotal(60*60);
		}
		
		boolean aPasFaitCanoe[]=new boolean[148];
		aPasFaitCanoe[3]=true;
		aPasFaitCanoe[9]=true;
		aPasFaitCanoe[10]=true;
		aPasFaitCanoe[16]=true;
		aPasFaitCanoe[18]=true;
		aPasFaitCanoe[21]=true;
		aPasFaitCanoe[24]=true;
		aPasFaitCanoe[25]=true;
		aPasFaitCanoe[36]=true;
		aPasFaitCanoe[37]=true;
		aPasFaitCanoe[46]=true;
		aPasFaitCanoe[54]=true;
		aPasFaitCanoe[69]=true;
		aPasFaitCanoe[85]=true;
		aPasFaitCanoe[97]=true;
		aPasFaitCanoe[100]=true;
		aPasFaitCanoe[132]=true;
		aPasFaitCanoe[133]=true;
		aPasFaitCanoe[138]=true;
		
		if(circuit.equals(BaseDeDonnees.shared().circuits()[2]) && !aPasFaitCanoe[equipe.dossard()]){
			this.writeToLogFileIfDateOk("\n Bonus de 20 minutes car a fait le canoe\n\n",circuit);
			if(Divers.isSameDay(circuit.date(), upToDate))
			{
				this.addToBonusDuJour(20*60);
			}
			
			this.addToBonusTotal(20*60);
		}
		
		/*
		
		if(circuit.equals(BaseDeDonnees.shared().circuits()[1]) && equipe.dossard()==28)
		{
			this.writeToLogFileIfDateOk("\n Bonus de 15 minutes pour compenser le temps perdu suite au non pointage de la balise 67\n\n",circuit);
			if(Divers.isSameDay(circuit.date(), upToDate))
			{
				this.addToBonusDuJour(15*60);
			}
			
			this.addToBonusTotal(15*60);
		}
		if(circuit.equals(BaseDeDonnees.shared().circuits()[1]) && equipe.dossard()==54)
		{
			this.writeToLogFileIfDateOk("\n Bonus de 25 minutes pour compenser le temps perdu suite au non pointage de la balise 67\n\n",circuit);
			if(Divers.isSameDay(circuit.date(), upToDate))
			{
				this.addToBonusDuJour(25*60);
			}
			if(Divers.isSameDay(circuit.date(), upToDate))
			{
				this.addToBonusTotal(25*60);
			}
		}
		if(circuit.equals(BaseDeDonnees.shared().circuits()[2]) && equipe.dossard()==4)
		{
			this.writeToLogFileIfDateOk("\n Bonus de 7 minutes pour réparation vélo\n\n",circuit);
			if(Divers.isSameDay(circuit.date(), upToDate))
			{
				this.addToBonusDuJour(7*60);
			}
			
			this.addToBonusTotal(7*60);
		}
		if(circuit.equals(BaseDeDonnees.shared().circuits()[2]) && equipe.dossard()==14)
		{
			this.writeToLogFileIfDateOk("\n Bonus de 6 minutes pour réparation vélo\n\n",circuit);
			if(Divers.isSameDay(circuit.date(), upToDate))
			{
				this.addToBonusDuJour(6*60);
			}
			
			this.addToBonusTotal(6*60);
		}
		if(circuit.equals(BaseDeDonnees.shared().circuits()[2]) && equipe.dossard()==45)
		{
			this.writeToLogFileIfDateOk("\n Bonus de 21 minutes pour réparation vélo\n\n",circuit);
			if(Divers.isSameDay(circuit.date(), upToDate))
			{
				this.addToBonusDuJour(21*60);
			}
			
			this.addToBonusTotal(21*60);
		}
		if(circuit.equals(BaseDeDonnees.shared().circuits()[2]) && equipe.dossard()==22)
		{
			this.writeToLogFileIfDateOk("\n Bonus de 11 minutes pour réparation vélo\n\n",circuit);
			if(Divers.isSameDay(circuit.date(), upToDate))
			{
				this.addToBonusDuJour(11*60);
			}
			
			this.addToBonusTotal(11*60);
		}
		if(circuit.equals(BaseDeDonnees.shared().circuits()[2]) && equipe.dossard()==65)
		{
			this.writeToLogFileIfDateOk("\n Bonus de 16 minutes pour réparation vélo\n\n",circuit);
			if(Divers.isSameDay(circuit.date(), upToDate))
			{
				this.addToBonusDuJour(16*60);
			}
			
			this.addToBonusTotal(16*60);
		}
		
		*/
	}
	
	public void calculerResultat()
	{
		
		writeToLogFile("\n****************************************************\n");
		writeToLogFile("Calcul du résultat de l'équipe n°"+this.equipe.dossard()+"\n\n");
		
		Circuit[] circuits = BaseDeDonnees.shared().circuits();
		
		for(Circuit circuit : circuits)
		{
			if(circuit.date().compareTo(upToDate)<=0)
			{
				this.calculerResultat(circuit);
			}
		}

		writeToLogFile("\nFin du calcul du résultat de l'équipe n°"+this.equipe.dossard()+"\n");
		writeToLogFile("****************************************************\n");
	}
	
	public void addToTempsReelDuJour(double secondes)
	{
		this.tempsReelDuJour += secondes;
	}
	
	public void addToBonusDuJour(double secondes)
	{
		this.bonusDuJour += secondes;
	}
	
	public void addToMalusDuJour(double secondes)
	{
		this.malusDuJour += secondes;
	}
	
	public void addToCumuleSpecialesDuJour(double secondes)
	{
		this.cumuleSpecialesDuJour += secondes;
	}
	
	public double tempsReelDuJour()
	{
		return this.tempsReelDuJour;
	}
	
	public double bonusDuJour()
	{
		return this.bonusDuJour;
	}
	
	public double malusDuJour()
	{
		return this.malusDuJour;
	}
	
	public void addToTempsReelTotal(double secondes)
	{
		this.tempsReelTotal += secondes;
	}
	
	public void addToBonusTotal(double secondes)
	{
		this.bonusTotal += secondes;
	}
	
	public void addToMalusTotal(double secondes)
	{
		this.malusTotal += secondes;
	}
	
	public double tempsReelTotal()
	{
		return this.tempsReelTotal;
	}
	
	public double bonusTotal()
	{
		return this.bonusTotal;
	}
	
	public double malusTotal()
	{
		return this.malusTotal;
	}
	
	public double cumuleSpecialeDuJour()
	{
		return this.cumuleSpecialesDuJour;
	}
	
	public double tempsCumuleDuJour()
	{
		return this.tempsReelDuJour + this.malusDuJour - this.bonusDuJour + this.cumuleSpecialesDuJour;
	}
	
	public double tempsCumuleTotal()
	{
		return this.tempsReelTotal + this.malusTotal - this.bonusTotal;
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
			ResultatEquipe.logFile = null;
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
	
	public Date upToDate()
	{
		return this.upToDate;
	}
}
