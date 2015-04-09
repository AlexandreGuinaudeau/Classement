package ReglesEpreuves;

import org.jdom2.Element;

import Classements.Epreuve;
import Classements.Pointage;
import Classements.ResultatBrutEquipe;
import Classements.ResultatEquipeEpreuve;
import Divers.AffichageDurees;

public class LimiteTemps extends RegleEpreuve {

	private double malus; // Une minute de retard = malus minutes de penalites
	private double limite; // Limite en secondes
	
	public LimiteTemps(Epreuve epreuve, Element element)
	{
		super(epreuve,element);
		
		Element malusElem = element.getChild("malus");
		
		if(malusElem==null)
		{
			throw new IllegalArgumentException("La limite de temps ne definit pas un malus par minute de retard !");
		}
		
		malus = Double.parseDouble(malusElem.getText());
		
		Element limiteElem = element.getChild("limite");
		
		if(limiteElem==null)
		{
			throw new IllegalArgumentException("La limite de temps ne definit pas de limite de temps !");
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
			this.logStatusRegle("Avertissement : la balise de depart de la section en temps limitee n'est pas pointee.");
		}
		
		if(!finishPointee)
		{
			this.logStatusRegle("Avertissement : la balise de fin de la section en temps limitee n'est pas pointee.");
		}
		
		if(!startPointee && !finishPointee)
		{
			this.logStatusRegle("Ok : l'epreuve est considere comme non courue. Penalite : "+(limite/60)+" min.");
			
			resultat.addToMalus(limite);
		}
		else if(startPointee && finishPointee)
		{
			this.logStatusRegle("Ok : le pointage des balises depart et arrivee est correct.");
			
			Pointage pointageStart = brut.pointage(epreuve.start());
			Pointage pointageFinish = brut.pointage(epreuve.finish());
			
			double temps = Math.abs(pointageFinish.timeSince(pointageStart));
			
			this.logStatusRegle("Temps limite : "+AffichageDurees.heuresMinutesSecondes(limite)+" min. Temps de l'equipe : "+AffichageDurees.heuresMinutesSecondes(temps)+" min.");
			
			if(temps > limite)
			{
				double malusTemps = 60*malus*(Math.floor((temps-limite)/60));
				
				this.logStatusRegle("Limite de temps depassee : on applique une penalite de "+malus+"xDepassement = "+(malusTemps/60)+" min.");;
			
				resultat.addToMalus(malusTemps);
			}
			else
			{
				this.logStatusRegle("La limite de temps est respecte, pas de penalite.");;
			}
		}
		
		
	}

}
