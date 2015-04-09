package Classements;

import java.util.*;
import java.text.SimpleDateFormat;

import org.jdom2.*;

import ReglesEpreuves.*;

public class Epreuve
{
	protected Circuit circuit;
	protected String nom;
	protected double distance;
	protected double denivele;
	protected Balise[] balises;
	protected RegleEpreuve[] regles;
	
	protected Balise start;
	protected Balise finish;
	
	protected boolean isArrivee = false;
	
	private boolean departEnMasse = false;
	
	protected PenalitesBalises penalites;
	
	public static Epreuve epreuveFromXML(Circuit circuit, Element elem)
	{
		String name = elem.getName();
		
		if(name.equals("epreuve"))
		{
			return new Epreuve(circuit,elem);
		}
		if(name.equals("ravitaillement"))
		{
			return new Ravitaillement(circuit,elem);
		}
		if(name.equals("briefing"))
		{
			return new Briefing(circuit,elem);
		}
		
		System.out.println("Type d'épreuve non supporté : "+name+" !");
		System.exit(-1);
		return null;
	}
	
	protected Epreuve()
	{
		
	}
	
	// Construction à partir d'un élément XML
	protected Epreuve(Circuit circuit, Element elem)
	{
		this.circuit = circuit;
		
		nom = elem.getChildText("nom");
		distance = Double.parseDouble(elem.getChildText("distance"));
		denivele = Double.parseDouble(elem.getChildText("denivele"));
		
		Element balisesParent = elem.getChild("balises");
		
		if(balisesParent.getChild("departEnMasse") != null)
		{
			this.departEnMasse = true;
			this.circuit.setDepartEnMasse(true);
		}
		
		List<Element> elemBalises = balisesParent.getChildren("balise");
		
		balises = new Balise[elemBalises.size()];
		
		int i=0;
		for(Element elemBalise : elemBalises)
		{
			balises[i] = new Balise(this,elemBalise);
			i++;
		}
		
		Element arrivee = balisesParent.getChild("arrivee");
		
		if(arrivee != null)
		{
			this.isArrivee = true;
		}
		
		if(finish == null && !isArrivee)
		{
			throw new IllegalArgumentException("L'épreuve "+nom()+" ne comporte pas de balise arrivée");
		}
		
		if(start == null && !departEnMasse)
		{
			throw new IllegalArgumentException("L'épreuve "+nom()+" ne comporte pas de balise départ, et ne correspond pas à un départ en masse");
		}
		
		Element reglesParents = elem.getChild("regles");
		
		List<Element> elemRegles = reglesParents.getChildren();
		
		regles = new RegleEpreuve[elemRegles.size()];
		
		i=0;
		for(Element elemRegle : elemRegles)
		{
			regles[i] = RegleEpreuve.regleFromXML(this, elemRegle);
			i++;
		}
	}
	
	public Epreuve(Circuit circuit, Balise[] balises, RegleEpreuve[] regles)
	{
		this.circuit = circuit;
		this.balises = balises;
		this.regles = regles;
	}
	
	public String nom()
	{
		return nom;
	}
	
	public void setStartBalise(Balise balise)
	{
		start = balise;
	}
	
	public void setFinishBalise(Balise balise)
	{
		finish = balise;
	}
	
	public PenalitesBalises penalitesBalises()
	{
		if(penalites == null)
		{
			penalites = new PenalitesBalises(this);
		}
		return this.penalites;
	}
	
	public Balise balise(int numero)
	{
		for(Balise balise : balises)
		{
			if(balise.numero() == numero)
			{
				return balise;
			}
		}
		return null;
	}
	
	public Balise[] balises()
	{
		return balises;
	}
	
	public Circuit circuit()
	{
		return this.circuit;
	}
	
	public boolean checkValidity(ResultatBrutEquipe brut)
	{
		Pointage[] pointages = brut.pointages();
		
		boolean res = true;
		
		Pointage pointageStart = null;
		Pointage pointageFinish = null;
		
		if(start != null)
		{
			pointageStart = brut.pointage(start);
		}
		
		if(finish != null)
		{
			pointageFinish = brut.pointage(finish);
		}
		
		if(start != null && pointageStart == null)
		{
			res = false;
			logPointagesInvalides(brut.equipe(), "Balise départ "+start.numero()+" non pointée");
		}
		
		if(finish != null && pointageFinish == null)
		{
			res = false;
			logPointagesInvalides(brut.equipe(), "Balise arrivée "+finish.numero()+" non pointée");
		}
		
		if(pointageStart != null && pointageFinish != null)
		{
			if(pointageFinish.timeSince(pointageStart)<0)
			{
				res = false;
				logPointagesInvalides(brut.equipe(), "La balise arrivée est pointée avant la balise départ");
			}
		}
		
		return res;
	}
	
	// calculTempsReel : on suppose que la liste des pointages est valide
	public double calculTempsReel(ResultatBrutEquipe brut)
	{
		Pointage[] pointages = brut.pointages();
		
		double tempsReel;
		
		Pointage pointageStart = null;
		Pointage pointageFinish = null;
		
		if(start != null)
		{
			pointageStart = brut.pointage(start);
		}
		
		if(start == null && !this.departEnMasse)
		{
			this.logPointagesInvalides(brut.equipe(), "La balise de début d'épreuve n'a pas été pointée");
			
			return 0;
		}
		
		if(finish == null && !this.isArrivee)
		{
			this.logPointagesInvalides(brut.equipe(), "La balise de fin d'épreuve n'a pas été pointée");
			
			return 0;
		}
		
		if(isArrivee)
		{
			pointageFinish = brut.finish(this.circuit);
		}
		else
		{
			pointageFinish = brut.pointage(finish);
		}
		
		if(!departEnMasse)
		{
			if(pointageFinish == null)
			{
				throw new RuntimeException("L'équipe "+brut.equipe().dossard()+" n'a pas pointé la balise arrivée pour l'épreuve "+this.nom());
			}
			tempsReel = pointageFinish.timeSince(pointageStart);
		}
		else
		{
			tempsReel = this.circuit.tempsDepuisDepartEnMasse(pointageFinish.date());
		}
		
		return tempsReel;
	}
	
	public RegleEpreuve[] regles()
	{
		return this.regles;
	}
	
	public void logPointagesInvalides(Equipe equipe, String detail)
	{
		ResultatEquipe.writeToLogFile("Pointages invalides de l'équipe n°"+equipe.dossard()+" sur l'épreuve "+this.nom+" : "+detail+"\n");
	}
	
	public int positionBalise(Balise balise)
	{
		for(int i=0 ; i<balises.length ; i++)
		{
			if(balises[i].equals(balise))
			{
				return i;
			}
		}
		
		throw new IllegalArgumentException("La balise n°"+balise.numero()+" ne fait pas partie de l'épreuve "+this.nom());
	}
	
	public Balise start()
	{
		return start;
	}
	
	public Balise finish()
	{
		return finish;
	}
	
	public boolean isArrivee()
	{
		return this.isArrivee;
	}
	
	public boolean hasSectionGrimpeur()
	{
		for(RegleEpreuve regle : regles)
		{
			if(regle.nom().equals("sectionGrimpeur"))
			{
				return true;
			}
		}
		return false;
	}
	
	public SectionGrimpeur sectionGrimpeur()
	{
		for(RegleEpreuve regle : regles)
		{
			if(regle.nom().equals("sectionGrimpeur"))
			{
				return (SectionGrimpeur)regle;
			}
		}
		return null;
	}
	
	public boolean hasSectionSprinteur()
	{
		for(RegleEpreuve regle : regles)
		{
			if(regle.nom().equals("sectionSprinteur"))
			{
				return true;
			}
		}
		return false;
	}
	
	public SectionSprinteur sectionSprinteur()
	{
		for(RegleEpreuve regle : regles)
		{
			if(regle.nom().equals("sectionSprinteur"))
			{
				return (SectionSprinteur)regle;
			}
		}
		return null;
	}
	
	public void calculTempsSpecialeGrimpeur(ResultatBrutEquipe brut, ResultatSpeciale resultat)
	{
		if(this.hasSectionGrimpeur())
		{
			SectionGrimpeur section = this.sectionGrimpeur();
			
			section.calculTemps(brut,resultat);
		}
	}
	
	public void calculTempsSpecialeSprinteur(ResultatBrutEquipe brut, ResultatSpeciale resultat)
	{
		if(this.hasSectionSprinteur())
		{
			SectionSprinteur section = this.sectionSprinteur();
			
			section.calculTemps(brut,resultat);
		}
	}
}
