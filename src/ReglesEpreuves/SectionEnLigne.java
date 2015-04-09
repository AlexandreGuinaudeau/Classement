package ReglesEpreuves;

import Classements.*;
import org.jdom2.*;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import BaseDeDonnees.*;

import java.util.*;

public class SectionEnLigne extends RegleEpreuve
{
	private Balise start;
	private Balise finish;
	private double malus; // Penalite si l'ordre n'est pas respecte, en secondes
	
	public SectionEnLigne(Epreuve epreuve, Element element)
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
			
			start = epreuve.balise(numeroBalise);
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
			
			finish = epreuve.balise(numeroBalise);
		}
		
		Element malusElem = element.getChild("malus");
		if(malusElem == null)
		{
			throw new IllegalArgumentException("Une section en ligne doit definir un malus dans le cas ou l'ordre n'est pas respecte !");
		}
		
		malus = 60*Double.parseDouble(malusElem.getText());
	}
	
	protected void appliquerAClassementOk(ResultatBrutEquipe brut,
			ResultatEquipeEpreuve resultat)
	{
		int indexStart, indexFinish;
		
		if(start != null)
		{
			indexStart = resultat.epreuve().positionBalise(start);
		}
		else
		{
			indexStart = 0;
		}
		
		if(finish != null)
		{
			indexFinish = resultat.epreuve().positionBalise(finish)+1;
		}
		else
		{
			indexFinish = resultat.epreuve().balises().length;
		}
		
		Balise[] section = Arrays.copyOfRange(resultat.epreuve().balises(), indexStart, indexFinish);
		
		List<Integer> ordresPointage = new LinkedList<Integer>();
		
		for(Balise balise : section)
		{
			if(brut.estPointee(balise))
			{
				ordresPointage.add(brut.positionDansListePointages(balise));
			}
		}
		
		boolean isSorted = true;
		Integer tmp = null;
		
		for(Integer pos : ordresPointage)
		{
			if(tmp != null)
			{
				isSorted &= (tmp<=pos);
			}
			tmp = pos;
		}
		
		if(isSorted)
		{
			this.logStatusRegle("La section en ligne de l'epreuve "+resultat.epreuve().nom()+" est respectee");
		}
		else
		{
			this.logStatusRegle("la section en ligne de l'epreuve "+resultat.epreuve().nom()+" n'est pas respectee. Malus de "+(malus/60)+" minutes.");
			resultat.addToMalus(malus);
		}
	}

}
