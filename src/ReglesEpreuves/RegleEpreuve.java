package ReglesEpreuves;

import java.util.*;

import org.jdom2.*;

import Classements.*;

import Divers.*;

public abstract class RegleEpreuve
{
	protected Set<TypeClassements> toIgnore = new HashSet<TypeClassements>(); // Les classements sur lesquels la regle ne s'applique pas.
	protected Epreuve epreuve;
	protected String nom;
	protected ResultatEquipeEpreuve resultat;
	protected ResultatSpeciale resultatSpeciale;
	
	static public RegleEpreuve regleFromXML(Epreuve epreuve, Element element)
	{
		String nomRegle = element.getName();
		
		if(nomRegle.equals("arretchrono"))
		{
			return new ArretChrono(epreuve,element);
		}
		if(nomRegle.equals("sectionGrimpeur"))
		{
			return new SectionGrimpeur(epreuve,element);
		}
		if(nomRegle.equals("penaliteOuvertureCarte"))
		{
			return new PenaliteOuvertureCarte(epreuve,element);
		}
		if(nomRegle.equals("sectionSprinteur"))
		{
			return new SectionSprinteur(epreuve,element);
		}
		if(nomRegle.equals("sectionEnLigne"))
		{
			return new SectionEnLigne(epreuve,element);
		}
		if(nomRegle.equals("limiteTemps"))
		{
			return new LimiteTemps(epreuve,element);
		}
		
		throw new IllegalArgumentException("Nom de regle non reconnu : "+nomRegle);
	}
	
	protected RegleEpreuve(Epreuve epreuve)
	{
		this.epreuve = epreuve;
	}
	
	public RegleEpreuve(Epreuve epreuve, Element element)
	{
		this.epreuve = epreuve;
		this.nom = element.getName();
		this.toIgnore = new HashSet<TypeClassements>();
	}
	
	// La fonction appliquerAClassementOk est appelee si la regle s'applique sur le classement du resultat de l'epreuve
	protected abstract void appliquerAClassementOk(ResultatBrutEquipe brut, ResultatEquipeEpreuve resultat);
	
	public void appliquerA(ResultatBrutEquipe brut, ResultatEquipeEpreuve resultat)
	{
		if(!toIgnore.contains(resultat.classement().typeClassement()))
		{
			this.resultat = resultat;
			this.appliquerAClassementOk(brut, resultat);
		}
	}
	
	public String nom()
	{
		return nom;
	}
	
	public void logStatusRegle(String status)
	{
		if(resultat!=null && Divers.isSameDay(resultat.resultatEquipe().upToDate(), this.epreuve.circuit().date()))
		{
			ResultatEquipe.writeToLogFile("Regle "+this.nom()+" : "+status+"\n");
		}
	}
}
