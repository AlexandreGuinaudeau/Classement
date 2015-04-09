package Classements;

import java.util.*;
import java.io.*;
import java.text.*;

import BaseDeDonnees.BaseDeDonnees;
import Divers.AffichageDurees;
import Divers.StringManipulation;

public abstract class Classement
{
	private static Map<TypeClassements,Classement> classements;
	
	public static void init()
	{
		if(classements == null)
		{
			classements = new HashMap<TypeClassements,Classement>();
			
			classements.put(TypeClassements.CLASSEMENT_SCRATCH, new ClassementScratch());
			classements.put(TypeClassements.CLASSEMENT_AREVA, new ClassementAreva());
			classements.put(TypeClassements.CLASSEMENT_FEMMES, new ClassementFemmes());
			classements.put(TypeClassements.CLASSEMENT_MIXTES, new ClassementMixtes());
			classements.put(TypeClassements.CLASSEMENT_SPRINTEUR, new ClassementSprinteur());
			classements.put(TypeClassements.CLASSEMENT_GRIMPEUR, new ClassementGrimpeur());
			classements.put(TypeClassements.CLASSEMENT_ECOLES, new ClassementEcoles());
			classements.put(TypeClassements.CLASSEMENT_HOMMES, new ClassementHommes());
			classements.put(TypeClassements.CLASSEMENT_ETUDIANT, new ClassementEtudiant());
		}
	}
	
	public static String nomClassement(TypeClassements type)
	{
		switch(type)
		{
			case CLASSEMENT_SCRATCH:
				return "Scratch";
			case CLASSEMENT_AREVA:
				return "Entreprise";
			case CLASSEMENT_FEMMES:
				return "Femmes";
			case CLASSEMENT_MIXTES:
				return "Mixtes";
			case CLASSEMENT_SPRINTEUR:
				return "Meilleur Sprinteur";
			case CLASSEMENT_GRIMPEUR:
				return "Meilleur Grimpeur";
			case CLASSEMENT_ECOLES:
				return "Ecoles";
			case CLASSEMENT_HOMMES:
				return "Hommes";
			case CLASSEMENT_ETUDIANT:
				return "Etudiant";
			
			default:
				throw new IllegalArgumentException("Type de classement non supporte");
		}
	}
	
	public static String nomFichier(TypeClassements type)
	{
		switch(type)
		{
			case CLASSEMENT_SCRATCH:
				return "Scratch";
			case CLASSEMENT_AREVA:
				return "Entreprise";
			case CLASSEMENT_FEMMES:
				return "Femmes";
			case CLASSEMENT_MIXTES:
				return "Mixtes";
			case CLASSEMENT_SPRINTEUR:
				return "Meilleur_Sprinteur";
			case CLASSEMENT_GRIMPEUR:
				return "Meilleur_Grimpeur";
			case CLASSEMENT_ECOLES:
				return "Ecoles";
			case CLASSEMENT_HOMMES:
				return "Hommes";
			case CLASSEMENT_ETUDIANT:
				return "Etudiant";
			
			default:
				throw new IllegalArgumentException("Type de classement non supporte");
		}
	}
	
	public static Classement classementAvecNom(String nom)
	{
		init();
		if(nom.equals("scratch"))
		{
			return classements.get(TypeClassements.CLASSEMENT_SCRATCH);
		}
		if(nom.equals("entreprise"))
		{
			return classements.get(TypeClassements.CLASSEMENT_AREVA);
		}
		if(nom.equals("femmes"))
		{
			return classements.get(TypeClassements.CLASSEMENT_FEMMES);
		}
		if(nom.equals("mixtes"))
		{
			return classements.get(TypeClassements.CLASSEMENT_MIXTES);
		}
		if(nom.equals("sprinteur"))
		{
			return classements.get(TypeClassements.CLASSEMENT_SPRINTEUR);
		}
		if(nom.equals("grimpeur"))
		{
			return classements.get(TypeClassements.CLASSEMENT_GRIMPEUR);
		}
		if(nom.equals("ecoles"))
		{
			return classements.get(TypeClassements.CLASSEMENT_ECOLES);
		}
		if(nom.equals("hommes"))
		{
			return classements.get(TypeClassements.CLASSEMENT_HOMMES);
		}
		if(nom.equals("etudiant"))
		{
			return classements.get(TypeClassements.CLASSEMENT_ETUDIANT);
		}
		throw new IllegalArgumentException("Type de classement non supporte");
	}
	
	public static void printChoixClassements()
	{
		int i=1;
		for(TypeClassements type : TypeClassements.values())
		{
			System.out.println(i+" : "+nomClassement(type));
			i++;
		}
	}
	
	public static Classement classementAvecChoix(int choix)
	{
		if(choix<1 || choix > TypeClassements.values().length)
		{
			throw new IllegalArgumentException("Choix invalide");
		}
		
		return Classement.classementAvecType(TypeClassements.values()[choix-1]);
	}
	
	public static Classement classementAvecType(TypeClassements type)
	{
		Classement.init();
		
		if(classements.containsKey(type))
		{
			return classements.get(type);
		}
		else
		{
			throw new IllegalArgumentException("Type de classement non supporte");
		}
	}
	
	public abstract String type();
	public abstract TypeClassements typeClassement();
	
	public String ligneResultat(ResultatEquipe resultat, int rang)
	{
		String res = "";
		
		res += rang;
		res += "&";
		
		res += resultat.equipe().dossard();
		res += "&";
		
		res += resultat.equipe().nomEquipe();
		res += "&";
		
		/*
		res += AffichageDurees.heuresMinutesSecondes(resultat.tempsReelTotal());
		res += "&";
		
		res += AffichageDurees.heuresMinutesSecondes(resultat.bonusTotal());
		res += "&";
		
		res += AffichageDurees.heuresMinutesSecondes(resultat.malusTotal());
		res += "&";
		
		res += AffichageDurees.heuresMinutesSecondes(resultat.CumuleSpecialeTotal());
		res += "&";
		
		res += AffichageDurees.heuresMinutesSecondes(resultat.tempsCumuleTotal());
		*/
		//res += "\\tabularnewline\n\\hline\n";
		
		
		res += AffichageDurees.heuresMinutesSecondes(resultat.tempsReelDuJour());
		res += "&";
		
		res += AffichageDurees.heuresMinutesSecondes(resultat.bonusTotal());
		res += "&";
		
		res += AffichageDurees.heuresMinutesSecondes(resultat.malusTotal());
		res += "&";
		
		res += AffichageDurees.heuresMinutesSecondes(resultat.cumuleSpecialeDuJour());
		res += "&";
		
		res += AffichageDurees.heuresMinutesSecondes(resultat.tempsCumuleDuJour());
		
		res += "\\tabularnewline\n\\hline\n";
		
		return res;
	}
	
	public String ligneResultat(ResultatSpeciale resultat, int rang)
	{
		String res = "";
		
		res += rang;
		res += "&";
		
		res += resultat.equipe().dossard();
		res += "&";
		
		res += resultat.equipe().nomEquipe();
		res += "&";
		
		res += AffichageDurees.minutesSecondes(resultat.tempsDuJour());
		res += "&";
		
		res += AffichageDurees.heuresMinutesSecondes(resultat.tempsTotal());
		
		res += "\\tabularnewline\n\\hline\n";
		
		return res;
	}
	
	public void writeInFile(FileWriter file, String s)
	{
		try
		{
			file.write(s);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Impossible d'ecrire dans le fichier tex du classement.");
			System.exit(-1);
		}
	}
	
	public void enregistrerInfosClassement(FileWriter file, Date date)
	{
		
		String dateRaid = "\\newcommand{\\dateraid}{";
		
		SimpleDateFormat format = new SimpleDateFormat("EEEE dd MMMM yyyy");
		
		String jour = format.format(date);
		
		jour = StringManipulation.uppercaseFirstLetter(jour);
		
		int numCircuit = BaseDeDonnees.shared().numeroCircuitAvecDate(date);
		
		dateRaid += jour;
		//TODO =Modifier le jour pour classement journee
		//dateRaid += "\\\\Jour "+(numCircuit+1)+"}\n";
		dateRaid += "\\\\Jour "+3+"}\n";
		this.writeInFile(file, dateRaid);
		
		boolean classementFinal = (numCircuit == BaseDeDonnees.shared().circuits().length-1);
		String typeClassement;
		
		if(classementFinal)
		{
			typeClassement = "Classement final";
		}
		else
		{
			typeClassement = "Classement provisoire";
		}
		
		this.writeInFile(file, "\\newcommand{\\typeclassement}{"+typeClassement+"}\n");
		this.writeInFile(file, "\\newcommand{\\nomclassement}{"+this.type()+"}\n");
		
		this.writeInFile(file, "\n");
	}
	
	public void sauvegarderResultats(List<ResultatEquipe> resultatsTries, Date upToDate)
	{
		String path = "classements/";
		
		SimpleDateFormat format = new SimpleDateFormat("EEEE");
		
		String jour = format.format(upToDate);
		
		jour = StringManipulation.uppercaseFirstLetter(jour);
		
		path += "Classement_"+this.nomFichier(this.typeClassement())+"_"+jour+".tex";
		
		File output = new File(path);
		
		FileWriter file = null;
		
		try
		{
			file = new FileWriter(output);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Impossible de creer le fichier tex du classement.");
			System.exit(-1);
		}
		
		this.enregistrerInfosClassement(file,upToDate);
		
		this.writeInFile(file, "\n\\newcommand{\\lignes}{\n\n");
		
		int rang = 1;
		
		for(ResultatEquipe resultat : resultatsTries)
		{
			this.writeInFile(file, this.ligneResultat(resultat, rang));
			rang++;
		}
		
		this.writeInFile(file, "\n}");
		
		try {
			file.close();
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("Impossible de fermer le fichier tex du classement.");
			System.exit(-1);
		}
	}
	
	public void sauvegarderResultatsSpeciaux(List<ResultatSpeciale> resultatsTries, Date upToDate)
	{
		String path = "classements/";
		
		SimpleDateFormat format = new SimpleDateFormat("EEEE");
		
		String jour = format.format(upToDate);
		
		jour = StringManipulation.uppercaseFirstLetter(jour);
		
		path += "Classement_"+this.nomFichier(this.typeClassement())+"_"+jour+".tex";
		
		File output = new File(path);
		
		FileWriter file = null;
		
		try
		{
			file = new FileWriter(output);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Impossible de creer le fichier tex du classement.");
			System.exit(-1);
		}
		
		this.enregistrerInfosClassement(file,upToDate);
		
		this.writeInFile(file, "\n\\newcommand{\\lignes}{\n\n");
		
		int rang = 1;
		
		for(ResultatSpeciale resultat : resultatsTries)
		{
			this.writeInFile(file, this.ligneResultat(resultat, rang));
			rang++;
		}
		
		this.writeInFile(file, "\n}");
		
		try {
			file.close();
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("Impossible de fermer le fichier tex du classement.");
			System.exit(-1);
		}
	}
	
	public Equipe[] equipes()
	{
		return BaseDeDonnees.shared().equipesEnCourse();
	}
	
	public void calculClassement(Date upToDate)
	{
		Equipe[] equipes = this.equipes();
		
		List<ResultatEquipe> resultats = new LinkedList<ResultatEquipe>();
		
		for(int i=0 ; i<equipes.length ; i++)
		{
			if(equipes[i].doigt()!=null)
			{
				Pointage[] pointages = BaseDeDonnees.shared().pointagesEquipe(equipes[i]);
				
				if(pointages!=null && pointages.length>0)
				{
					ResultatEquipe tmp = new ResultatEquipe(equipes[i], this, upToDate);
					tmp.calculerResultat();
					
					if(tmp.wasSuccessfullyCalculated())
					{
						resultats.add(tmp);
					}
				}
			}
			else
			{
				ResultatEquipe.writeToLogFile("\nL'equipe n°"+equipes[i].dossard()+" n'a pas de numero de doigt associe. Abandon du calcul de leur resultat au raid.\n");
			}
		}
		
		Collections.sort(resultats, new ComparaisonResultatsEquipes());
		
		this.sauvegarderResultats(resultats, upToDate);
		
		ResultatEquipe.closeLogFile();
	}
}
