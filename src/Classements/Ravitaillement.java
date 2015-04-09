package Classements;

import java.util.List;

import org.jdom2.Element;

import ReglesEpreuves.RegleEpreuve;

public class Ravitaillement extends Epreuve
{
	// Construction a partir d'un element XML
	protected Ravitaillement(Circuit circuit, Element elem)
	{
		this.circuit = circuit;
		this.nom = "Ravitaillement";
		
		Element balisesParent = elem.getChild("balises");
		
		List<Element> elemBalises = balisesParent.getChildren("balise");
		
		balises = new Balise[elemBalises.size()];
		
		int i=0;
		for(Element elemBalise : elemBalises)
		{
			balises[i] = new Balise(this,elemBalise);
			i++;
		}
		
		if(finish == null)
		{
			throw new IllegalArgumentException("Le ravitaillement ne comporte pas de balise de fin d'arret chrono.");
		}
		
		if(start == null)
		{
			throw new IllegalArgumentException("Le ravitaillement ne comporte pas de balise de debut d'arret chrono !");
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

}
