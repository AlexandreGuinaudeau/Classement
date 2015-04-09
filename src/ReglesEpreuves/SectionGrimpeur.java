package ReglesEpreuves;

import BaseDeDonnees.BaseDeDonnees;

import Classements.*;
import Divers.AffichageDurees;
import Divers.Divers;

import java.text.SimpleDateFormat;
import java.util.*;

import org.jdom2.*;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import Divers.*;

public class SectionGrimpeur extends RegleEpreuve
{
	private double multiplicateur = 1;
	
	private Balise sectionStart;
	private Balise sectionFinish;

	private Double tempsVainqueur=null;
	private Double tempsDernier=null;
	
	public SectionGrimpeur(Epreuve epreuve, Element element)
	{
		super(epreuve,element);
		
		XPathFactory factory = XPathFactory.instance();
		XPathExpression<Attribute> expr = factory.compile("balise[@type='start']/@numero", Filters.attribute());
		List<Attribute> res = expr.evaluate(element);
		
		Attribute attr = BaseDeDonnees.firstOfList(res);
		
		if(attr != null)
		{
			int numeroBalise = 0;
			
			try
			{
				numeroBalise = attr.getIntValue();
			}
			catch(Exception e)
			{
				e.printStackTrace();
				throw new IllegalArgumentException("An error has occured : "+e.getLocalizedMessage());
			}
			
			sectionStart = epreuve.balise(numeroBalise);
		}

		expr = factory.compile("balise[@type='finish']/@numero", Filters.attribute());
		res = expr.evaluate(element);
		
		attr = BaseDeDonnees.firstOfList(res);
		
		if(attr != null)
		{
			int numeroBalise = 0;
			
			try
			{
				numeroBalise = attr.getIntValue();
			}
			catch(Exception e)
			{
				e.printStackTrace();
				throw new IllegalArgumentException("An error has occured : "+e.getLocalizedMessage());
			}
			
			sectionFinish = epreuve.balise(numeroBalise);
		}
		
		Element multiplicateurElem = element.getChild("multiplicateur");
		
		if(multiplicateurElem != null)
		{
			this.multiplicateur = Double.parseDouble(multiplicateurElem.getText());
		}
		else
		{
			this.multiplicateur = 1;
		}
		
		Element tempsVainqueurElem = element.getChild("tempsVainqueur");
		
		if(tempsVainqueurElem != null)
		{
			this.tempsVainqueur = Double.parseDouble(tempsVainqueurElem.getText());
		}
		else
		{
			this.tempsVainqueur = 1.0;
		}
		
		Element tempsDernierElem = element.getChild("tempsDernier");
		
		if(tempsDernierElem != null)
		{
			this.tempsDernier = Double.parseDouble(tempsDernierElem.getText());
		}
		else
		{
			this.tempsDernier = 1.0;
		}
	}
	
	@Override
	protected void appliquerAClassementOk(ResultatBrutEquipe brut,
			ResultatEquipeEpreuve resultat) {
		
		double temps = this.temps(brut);
		
		this.logStatusRegle("Temps reel de la "+this.epreuve.nom()+" : "+AffichageDurees.minutesSecondes(temps));
		this.logStatusRegle("Multiplicateur : "+this.multiplicateur);
		
		double malus = (this.multiplicateur-1)*temps;
		
		resultat.resultatEquipe().addToTempsReelTotal(malus);
		
		if(Divers.isSameDay(this.epreuve.circuit().date(), resultat.resultatEquipe().upToDate()))
		{
			resultat.resultatEquipe().addToCumuleSpecialesDuJour(malus);
		}
		resultat.resultatEquipe().addToCumuleSpecialeTotal(malus);
		this.logStatusRegle("Correction du temps reel de  "+this.epreuve.nom()+" : "+AffichageDurees.minutesSecondes((this.multiplicateur-1)*temps)+"\n");
	}

	public double multiplicateur()
	{
		return this.multiplicateur;
	}
	
	public void calculTemps(ResultatBrutEquipe brut, ResultatSpeciale resultat)
	{
		if(brut.equipe().dossard()==82)
		{
			this.logStatusRegle("Test");
		}
		
		this.resultatSpeciale = resultat;
		
		if(ResultatSpeciale.SpecialeACalculer!=null && !this.epreuve.nom().equals("Speciale "+ResultatSpeciale.SpecialeACalculer))
		{
			return;
		}
		
		Pointage startPointage = brut.pointage(sectionStart);
		
		if(startPointage == null)
		{
			this.logStatusRegle("La balise de depart de la section "+epreuve.nom()+" n'a pas ete pointee");
			resultat.setSuccessfullyCalculated(false);
			
			return;
		}
		
		Pointage finishPointage = brut.pointage(sectionFinish);
		
		if(finishPointage == null)
		{
			this.logStatusRegle("La balise de fin de la section "+epreuve.nom()+" n'a pas ete pointee");
			resultat.setSuccessfullyCalculated(false);
			
			return;
		}
		
		double temps = finishPointage.timeSince(startPointage);
		
		if(temps<0)
		{
			this.logStatusRegle("La balise de fin de la section "+epreuve.nom()+" a ete pointee avant la balise de depart.");
			resultat.setSuccessfullyCalculated(false);
			
			return;
		}
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String day = format.format(this.epreuve.circuit().date());
		
		String calculationDay = format.format(resultat.calculationDate());
		
		if(calculationDay.equals(day))
		{
			resultat.addToTempsDuJour(temps);
		}
		if(this.epreuve.circuit().date().compareTo(resultat.calculationDate())<=0)
		{
			resultat.addToTempsTotal(temps);
		}
		
		this.logStatusRegle("Duree de la speciale : "+AffichageDurees.minutesSecondes(temps)+" Temps du vainqueur : "+AffichageDurees.minutesSecondes(this.tempsVainqueur));
		
		resultat.addToCoeff(temps/this.tempsVainqueur);
	}
	
	public double temps(ResultatBrutEquipe brut)
	{
		Pointage startPointage = brut.pointage(sectionStart);
		
		if(startPointage == null)
		{
			this.logStatusRegle("La balise de depart de la section "+epreuve.nom()+" n'a pas ete pointee");
			this.logStatusRegle("Penalite de "+AffichageDurees.minutesSecondes(this.tempsDernier+900));
			
			return this.tempsDernier+900;
		}
		
		Pointage finishPointage = brut.pointage(sectionFinish);
		
		if(finishPointage == null)
		{
			this.logStatusRegle("La balise de fin de la section "+epreuve.nom()+" n'a pas ete pointee");
			this.logStatusRegle("Penalite de "+AffichageDurees.minutesSecondes(this.tempsDernier+900));
			
			return this.tempsDernier+900;
		}
		
		double temps = finishPointage.timeSince(startPointage);
		
		if(temps<0)
		{
			this.logStatusRegle("La balise de fin de la section "+epreuve.nom()+" a ete pointee avant la balise de depart.");
			this.logStatusRegle("Penalite de "+AffichageDurees.minutesSecondes(this.tempsDernier+900));
			
			return this.tempsDernier+900;
		}
		
		return temps;
	}
	

	
	public void logStatusRegle(String status)
	{
		if(resultatSpeciale != null && Divers.isSameDay(resultatSpeciale.calculationDate(), this.epreuve.circuit().date()))
		{
			ResultatSpeciale.writeToLogFile("Regle "+this.nom()+" : "+status+"\n");
		}
		else
		{
			super.logStatusRegle(status);
		}
	}
}
