package ReglesEpreuves;

import org.jdom2.Element;

import Classements.Epreuve;
import Classements.Pointage;
import Classements.ResultatBrutEquipe;
import Classements.ResultatEquipeEpreuve;
import Divers.AffichageDurees;

public class LimiteTemps extends RegleEpreuve {

	private double malus; // Une minute de retard = malus minutes de pénalités
	private double limite; // Limite en secondes
	
	public LimiteTemps(Epreuve epreuve, Element element)
	{
		super(epreuve,element);
		
		Element malusElem = element.getChild("malus");
		
		if(malusElem==null)
		{
			throw new IllegalArgumentException("La limite de temps ne définit pas un malus par minute de retard !");
		}
		
		malus = Double.parseDouble(malusElem.getText());
		
		Element limiteElem = element.getChild("limite");
		
		if(limiteElem==null)
		{
			throw new IllegalArgumentException("La limite de temps ne définit pas de limite de temps !");
		}
		
		limite = Double.parseDouble(limiteElem.getText());
		limite *= 60;
	}
	
	protected void appliquerAClassementOk(ResultatBrutEquipe brut,
			ResultatEquipeEpreuve resultat) {
		
		boolean startPointee = brut.estPointee(epreuve.start());
		boolean finishPointee = brut.estPointee(epreuve.finish());
		
		if(!startPointee)
		{
			this.logStatusRegle("Avertissement : la balise de départ de la section en temps limitée n'est pas pointée.");
		}
		
		if(!finishPointee)
		{
			this.logStatusRegle("Avertissement : la balise de fin de la section en temps limitée n'est pas pointée.");
		}
		
		if(!startPointee && !finishPointee)
		{
			this.logStatusRegle("Ok : l'épreuve est considéré comme non courue. Pénalité : "+(limite/60)+" min.");
			
			resultat.addToMalus(limite);
		}
		else if(startPointee && finishPointee)
		{
			this.logStatusRegle("Ok : le pointage des balises départ et arrivée est correct.");
			
			Pointage pointageStart = brut.pointage(epreuve.start());
			Pointage pointageFinish = brut.pointage(epreuve.finish());
			
			double temps = Math.abs(pointageFinish.timeSince(pointageStart));
			
			this.logStatusRegle("Temps limite : "+AffichageDurees.heuresMinutesSecondes(limite)+" min. Temps de l'équipe : "+AffichageDurees.heuresMinutesSecondes(temps)+" min.");
			
			if(temps > limite)
			{
				double malusTemps = 60*malus*(Math.floor((temps-limite)/60));
				
				this.logStatusRegle("Limite de temps dépassée : on applique une pénalité de "+malus+"xDépassement = "+(malusTemps/60)+" min.");;
			
				resultat.addToMalus(malusTemps);
			}
			else
			{
				this.logStatusRegle("La limite de temps est respecté, pas de pénalité.");;
			}
		}
		
		
	}

}
