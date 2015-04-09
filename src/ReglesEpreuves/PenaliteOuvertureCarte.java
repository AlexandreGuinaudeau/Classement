package ReglesEpreuves;

import org.jdom2.*;

import Classements.*;
import Divers.AffichageDurees;

public class PenaliteOuvertureCarte extends RegleEpreuve
{
	private double malus; // Malus en secondes
	
	public PenaliteOuvertureCarte(Epreuve epreuve, Element element)
	{
		super(epreuve,element);
		
		Element malusElem = element.getChild("malus");
		if(malusElem == null)
		{
			throw new IllegalArgumentException("Une penalite doit etre definie pour l'ouverture de la carte Roadbook !");
		}
		
		malus = Double.parseDouble(malusElem.getText());
		malus *= 60;
	}
	
	protected void appliquerAClassementOk(ResultatBrutEquipe brut,
			ResultatEquipeEpreuve resultat) {
		
		if(brut.carteOuverte())
		{
			resultat.addToMalus(malus);
			this.logStatusRegle("La carte du roadbook a ete ouverte, penalite de "+AffichageDurees.heuresMinutesSecondes(malus));
		}
		this.logStatusRegle("La carte du roadbook n'a pas ete ouverte, pas de penalite");
	}

}
