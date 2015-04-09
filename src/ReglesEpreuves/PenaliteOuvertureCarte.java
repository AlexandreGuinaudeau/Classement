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
			throw new IllegalArgumentException("Une pénalité doit être définie pour l'ouverture de la carte Roadbook !");
		}
		
		malus = Double.parseDouble(malusElem.getText());
		malus *= 60;
	}
	
	protected void appliquerAClassementOk(ResultatBrutEquipe brut,
			ResultatEquipeEpreuve resultat) {
		
		if(brut.carteOuverte())
		{
			resultat.addToMalus(malus);
			this.logStatusRegle("La carte du roadbook a été ouverte, pénalité de "+AffichageDurees.heuresMinutesSecondes(malus));
		}
		this.logStatusRegle("La carte du roadbook n'a pas été ouverte, pas de pénalité");
	}

}
