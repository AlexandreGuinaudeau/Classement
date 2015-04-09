package ReglesEpreuves;

import BaseDeDonnees.BaseDeDonnees;
import Classements.*;
import Divers.*;

import java.util.*;

import org.jdom2.*;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public class ArretChrono extends RegleEpreuve
{
	private Balise arretChronoStart = null;
	private Balise arretChronoFinish = null;
	private Double tempsLimite = null; // Temps limite en secondes de l'arrêt chrono
	
	public ArretChrono(Epreuve epreuve, Element element)
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
			
			arretChronoStart = epreuve.balise(numeroBalise);
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
			
			arretChronoFinish = epreuve.balise(numeroBalise);
		}
		
		Element tempsLimiteElem = element.getChild("tempsLimite");
		
		if(tempsLimiteElem != null)
		{
			String tempsLimiteStr = tempsLimiteElem.getText();
			
			try
			{
				tempsLimite = Double.parseDouble(tempsLimiteStr);
				tempsLimite *= 60;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				throw new IllegalArgumentException("An error has occured : "+e.getLocalizedMessage());
			}
		}
	}

	@Override
	protected void appliquerAClassementOk(ResultatBrutEquipe brut,
			ResultatEquipeEpreuve resultat) {
		
		if(arretChronoStart==null || arretChronoFinish==null)
		{
			throw new IllegalArgumentException("L'arrêt chrono n'a pas de balises de départ et d'arrivée.");
		}
		
		if(!brut.estPointee(arretChronoStart))
		{
			this.logStatusRegle("Erreur de pointage : la balise de départ de l'arrêt chrono n'a pas été pointée.");
			return;
		}
		
		if(!brut.estPointee(arretChronoFinish))
		{
			this.logStatusRegle("Erreur de pointage : la balise de fin de l'arrêt chrono n'a pas été pointée.");
			return;
		}
		
		int startIndex = brut.positionDansListePointages(arretChronoStart);
		int finishIndex = brut.positionDansListePointages(arretChronoFinish);
		
		if(finishIndex <= startIndex)
		{
			this.logStatusRegle("Erreur de pointage : la balise de fin de l'arrêt chrono a été pointée avant la balise de début.");
			return;
		}
		
		Pointage pointageStart = brut.pointage(arretChronoStart);
		Pointage pointageFinish = brut.pointage(arretChronoFinish);
		
		double tempsArret = pointageFinish.timeSince(pointageStart);
		
		this.logStatusRegle("Durée de l'arrêt chrono : "+AffichageDurees.minutesSecondes(tempsArret));
		
		double bonus;
		
		if(tempsLimite != null)
		{
			if(tempsArret <= tempsLimite)
			{
				this.logStatusRegle("La durée d'arrêt est inférieure à la durée maximale de "+AffichageDurees.minutesSecondes(tempsLimite));
			}
			else
			{
				this.logStatusRegle("La durée d'arrêt excède la durée maximale de "+AffichageDurees.minutesSecondes(tempsLimite));
			}
			
			bonus = Math.min(tempsLimite, tempsArret);
		}
		else
		{
			this.logStatusRegle("La durée de l'arrêt chrono n'est pas limitée.");
			bonus = tempsArret;
		}
		
		this.logStatusRegle("Correction du temps réel de "+AffichageDurees.minutesSecondes(bonus));
		
		if(Divers.isSameDay(this.epreuve.circuit().date(), resultat.resultatEquipe().upToDate()))
		{
			resultat.resultatEquipe().addToTempsReelDuJour(-bonus);
		}
		
		resultat.resultatEquipe().addToTempsReelTotal(-bonus);
	}

}
