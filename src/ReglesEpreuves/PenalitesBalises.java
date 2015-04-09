package ReglesEpreuves;

import java.util.*;

import org.jdom2.Element;

import Classements.*;

public class PenalitesBalises extends RegleEpreuve
{
	private List<Penalite> penalites;
	
	public PenalitesBalises(Epreuve epreuve, Element element)
	{
		super(epreuve,element);
		
		penalites = new LinkedList<Penalite>();
	}
	
	public PenalitesBalises(Epreuve epreuve)
	{
		super(epreuve);
		
		this.nom = "Penalités des balises";
		
		penalites = new LinkedList<Penalite>();
	}

	@Override
	protected void appliquerAClassementOk(ResultatBrutEquipe brut,
			ResultatEquipeEpreuve resultat)
	{
		Integer start = null;
		Integer finish = null;
		
		Balise startBalise = resultat.epreuve().start();
		Balise finishBalise = resultat.epreuve().finish();
		
		for(Penalite penalite : penalites)
		{
			boolean pointee = brut.estPointee(penalite.balise());
			
			if(penalite.minutesDePenalites()>0)
			{
				if(!pointee)
				{
					this.logStatusRegle("la balise n°"+penalite.balise().numero()+" n'a pas été pointée. Malus de "+penalite.minutesDePenalites()+" minutes");
					resultat.addToMalus(60*penalite.minutesDePenalites());
				}
				else
				{
					this.logStatusRegle("la balise n°"+penalite.balise().numero()+" a été correctement pointée.");
				}
			}
			else
			{
				if(pointee)
				{
					this.logStatusRegle("la balise bonus n°"+penalite.balise().numero()+" a été pointée. Bonus de "+(-penalite.minutesDePenalites())+" minutes");
					resultat.addToBonus(-60*penalite.minutesDePenalites());
				}
				else
				{
					this.logStatusRegle("la balise bonus n°"+penalite.balise().numero()+" n'a pas été pointée.");
				}
			}
		}
	}

	public void ajouterPenalite(Penalite penalite)
	{
		penalites.add(penalite);
	}
}
