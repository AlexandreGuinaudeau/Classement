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
	double cumuleSpecialesTotal;
	
	private Equipe equipe;
	private Classement classement; // Le classement auquel est associe ce resultat intermediaire
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
				throw new IllegalArgumentException("Impossible d'ouvrir le fichier log des resultats d'equipes : "+e.getLocalizedMessage());
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
			throw new IllegalArgumentException("Impossible d'ecrire dans le fichier log des resultats d'equipes : "+e.getLocalizedMessage());
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
		this.writeToLogFileIfDateOk("Calcul du resultat pour l'epreuve "+epreuve.nom()+"\n",epreuve.circuit());
		
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
				throw new RuntimeException("Le circuit debute par un depart en masse, mais aucune date de depart n'a ete definie.");
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
		
		writeToLogFile("Calcul du resultat sur le circuit du "+format.format(circuit.date())+"\n\n");
		
		if(circuit.isDepartEnMasse())
		{
			this.writeToLogFileIfDateOk("Depart en masse : heure de depart "+format2.format(circuit.date())+"\n\n",circuit);
		}
		
		double tempsReel = 0;
		
		try
		{
			tempsReel = this.calculerTempsReel(circuit);
		}
		catch(Exception e)
		{
			System.out.println("Impossible de calculer le temps reel de l'equipe "+this.equipe.dossard()+" sur le circuit !");
		}
		
		if(Divers.isSameDay(circuit.date(), upToDate))
		{
			this.addToTempsReelDuJour(tempsReel);
		}
		
		this.addToTempsReelTotal(tempsReel);
		
		
		this.writeToLogFileIfDateOk("Temps reel du jour : "+AffichageDurees.heuresMinutesSecondes(this.tempsReelDuJour)+"\n",circuit);
		this.writeToLogFileIfDateOk("Temps compense du jour : "+AffichageDurees.heuresMinutesSecondes(this.tempsCumuleDuJour())+"\n",circuit);
		this.writeToLogFileIfDateOk("Temps reel total : "+AffichageDurees.heuresMinutesSecondes(this.tempsReelTotal)+"\n",circuit);
		this.writeToLogFileIfDateOk("Temps compense total : "+AffichageDurees.heuresMinutesSecondes(this.tempsCumuleTotal())+"\n",circuit);
		
		
		/*
		 * modif pour debuggage, mise en commentaire
		writeToLogFile("Temps reel du jour : "+AffichageDurees.heuresMinutesSecondes(this.tempsReelDuJour)+"\n");
		writeToLogFile("Temps compense du jour : "+AffichageDurees.heuresMinutesSecondes(this.tempsCumuleDuJour())+"\n");
		writeToLogFile("Temps reel total : "+AffichageDurees.heuresMinutesSecondes(this.tempsReelTotal)+"\n");
		writeToLogFile("Temps compense total : "+AffichageDurees.heuresMinutesSecondes(this.tempsCumuleTotal())+"\n");
		*/
		if(Divers.isSameDay(circuit.date(), upToDate))
			writeToLogFile("Temps total "+AffichageDurees.heuresMinutesSecondes(this.tempsCompenseTotal));
		for(Epreuve epreuve : circuit.epreuves())
		{
			this.calculerResultat(epreuve);
			this.writeToLogFileIfDateOk("\n Fin du calcul de l'epreuve\n\n",circuit);
			
			this.writeToLogFileIfDateOk("Temps reel du jour : "+AffichageDurees.heuresMinutesSecondes(this.tempsReelDuJour)+"\n",circuit);
			this.writeToLogFileIfDateOk("Temps compense du jour : "+AffichageDurees.heuresMinutesSecondes(this.tempsCumuleDuJour())+"\n",circuit);
			this.writeToLogFileIfDateOk("Temps reel total : "+AffichageDurees.heuresMinutesSecondes(this.tempsReelTotal)+"\n",circuit);
			this.writeToLogFileIfDateOk("Temps compense total : "+AffichageDurees.heuresMinutesSecondes(this.tempsCumuleTotal())+"\n",circuit);
		}
		 //correctif lundi
		
		 if(circuit.equals(BaseDeDonnees.shared().circuits()[0]) && equipe.dossard()==113){
				this.writeToLogFileIfDateOk("\n pas de velo sur run and bike \n\n",circuit);
				if(Divers.isSameDay(circuit.date(), upToDate))
				{
					this.addToMalusDuJour(5*60);
				}
				
				this.addToMalusTotal(5*60);
		 }
		/*
		 //correctif Samedi
		
		
		//Samedi equipe 53
				if(circuit.equals(BaseDeDonnees.shared().circuits()[0]) && equipe.dossard()==53){
					this.writeToLogFileIfDateOk("\n pas de velo sur run and bike \n\n",circuit);
					if(Divers.isSameDay(circuit.date(), upToDate))
					{
						this.addToBonusDuJour(4*60+40);
					}
					
					this.addToBonusTotal(4*60+40);
				}
				
				 //Samedi equipe 48 betise sarah
				if(circuit.equals(BaseDeDonnees.shared().circuits()[0]) && equipe.dossard()==48){
					this.writeToLogFileIfDateOk("\n betise sarah\n\n",circuit);
					if(Divers.isSameDay(circuit.date(), upToDate))
					{
						this.addToBonusDuJour(12*60);
					}
					
					this.addToBonusTotal(12*60);
				}

				 //Samedi equipe 114 fleche
				if(circuit.equals(BaseDeDonnees.shared().circuits()[0]) && equipe.dossard()==114){
					this.writeToLogFileIfDateOk("\n fleche rate\n\n",circuit);
					if(Divers.isSameDay(circuit.date(), upToDate))
					{
						this.addToBonusDuJour(300);
					}
					
					this.addToBonusTotal(300);
				}
				
	 //Samedi equipe 95 intervention medicale sur ravito
				if(circuit.equals(BaseDeDonnees.shared().circuits()[0]) && equipe.dossard()==95){
					this.writeToLogFileIfDateOk("\n intervention medicale\n\n",circuit);
					if(Divers.isSameDay(circuit.date(), upToDate))
					{
						this.addToBonusDuJour(1328);
					}
					
					this.addToBonusTotal(1328);
				}
		//Samedi equipe 61 velo pdt pause
				if(circuit.equals(BaseDeDonnees.shared().circuits()[0]) && equipe.dossard()==61){
					this.writeToLogFileIfDateOk("\n repart velo ravito\n\n",circuit);
					if(Divers.isSameDay(circuit.date(), upToDate))
					{
						this.addToBonusDuJour(64);
					}
					
					this.addToBonusTotal(64);
				}
	*/
		//dimanche
		if(circuit.equals(BaseDeDonnees.shared().circuits()[0]) && ((equipe.dossard()==21)||(equipe.dossard()==89)||(equipe.dossard()==25)||(equipe.dossard()==65))){
			this.writeToLogFileIfDateOk("\n merci menou\n\n",circuit);
			if(Divers.isSameDay(circuit.date(), upToDate))
			{
				this.addToBonusDuJour(5*60);
			}
			
			this.addToBonusTotal(5*60);
		}
			if((circuit.equals(BaseDeDonnees.shared().circuits()[0]) && ((equipe.dossard()==3)||(equipe.dossard()==25)||(equipe.dossard()==47)||(equipe.dossard()==52)||(equipe.dossard()==56)||(equipe.dossard()==69)||(equipe.dossard()==113)))){
				this.writeToLogFileIfDateOk("\n malus detour\n\n",circuit);
				if(Divers.isSameDay(circuit.date(), upToDate))
				{
					this.addToMalusDuJour(45*60);
				}
				this.addToMalusTotal(45*60);}
			
		/*
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
			this.writeToLogFileIfDateOk("\n Bonus de 30 minutes pour poi√ßonnage balise 38\n\n",circuit);
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
	*/	
		
		/*
		if(circuit.equals(BaseDeDonnees.shared().circuits()[0]) && equipe.dossard()==44){
			this.writeToLogFileIfDateOk("\n Bonus de 5, pneu creve avant depart\n\n",circuit);
			if(Divers.isSameDay(circuit.date(), upToDate))
			{
				this.addToBonusDuJour(5*60);
			}
			
			this.addToBonusTotal(5*60);
		}
		
		if(circuit.equals(BaseDeDonnees.shared().circuits()[0]) && equipe.dossard()==77){
			this.writeToLogFileIfDateOk("\nGrosse looze pneu creve\n\n",circuit);
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
		
		if(circuit.equals(BaseDeDonnees.shared().circuits()[0]) && !aPasFaitCanoe[equipe.dossard()]){
			this.writeToLogFileIfDateOk("\n Bonus de 20 minutes car a fait le canoe\n\n",circuit);
			if(Divers.isSameDay(circuit.date(), upToDate))
			{
				this.addToBonusDuJour(20*60);
			}
			
			this.addToBonusTotal(20*60);
		}
		*/
		
	}
	
	public void calculerResultat()
	{
		
		writeToLogFile("\n****************************************************\n");
		writeToLogFile("Calcul du resultat de l'equipe n∞"+this.equipe.dossard()+"\n\n");
		
		Circuit[] circuits = BaseDeDonnees.shared().circuits();
		
		for(Circuit circuit : circuits)
		{
			if(circuit.date().compareTo(upToDate)<=0)
			{
				this.calculerResultat(circuit);
			}
		}

		writeToLogFile("\nFin du calcul du resultat de l'equipe n∞"+this.equipe.dossard()+"\n");
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
	
	public void addToCumuleSpecialeTotal(double secondes){
		cumuleSpecialesDuJour += secondes;
	}
	
	public double CumuleSpecialeTotal(){
		return cumuleSpecialesDuJour;
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
