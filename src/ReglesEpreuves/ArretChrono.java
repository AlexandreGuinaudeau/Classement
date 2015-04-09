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
	private Double tempsLimite = null; // Temps limite en secondes de l'arret chrono
	
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
			throw new IllegalArgumentException("L'arret chrono n'a pas de balises de depart et d'arrivee.");
		}
		
		if(!brut.estPointee(arretChronoStart))
		{
			this.logStatusRegle("Erreur de pointage : la balise de depart de l'arret chrono n'a pas ete pointee.");
			return;
		}
		
		if(!brut.estPointee(arretChronoFinish))
		{
			this.logStatusRegle("Erreur de pointage : la balise de fin de l'arret chrono n'a pas ete pointee.");
			return;
		}
		
		int startIndex = brut.positionDansListePointages(arretChronoStart);
		int finishIndex = brut.positionDansListePointages(arretChronoFinish);
		
		if(finishIndex <= startIndex)
		{
			this.logStatusRegle("Erreur de pointage : la balise de fin de l'arret chrono a ete pointee avant la balise de debut.");
			return;
		}
		
		Pointage pointageStart = brut.pointage(arretChronoStart);
		Pointage pointageFinish = brut.pointage(arretChronoFinish);
		
		double tempsArret = pointageFinish.timeSince(pointageStart);
		
		this.logStatusRegle("Duree de l'arret chrono : "+AffichageDurees.minutesSecondes(tempsArret));
		
		double bonus;
		
		if(tempsLimite != null)
		{
			if(tempsArret <= tempsLimite)
			{
				this.logStatusRegle("La duree d'arret est inferieure a la duree maximale de "+AffichageDurees.minutesSecondes(tempsLimite));
			}
			else
			{
				this.logStatusRegle("La duree d'arret excede la duree maximale de "+AffichageDurees.minutesSecondes(tempsLimite));
			}
			
			bonus = Math.min(tempsLimite, tempsArret);
		}
		else
		{
			this.logStatusRegle("La duree de l'arret chrono n'est pas limitee.");
			bonus = tempsArret;
		}
		
		this.logStatusRegle("Correction du temps reel de "+AffichageDurees.minutesSecondes(bonus));
		
		if(Divers.isSameDay(this.epreuve.circuit().date(), resultat.resultatEquipe().upToDate()))
		{
			resultat.resultatEquipe().addToTempsReelDuJour(-bonus);
		}
		
		resultat.resultatEquipe().addToTempsReelTotal(-bonus);
	}

}
