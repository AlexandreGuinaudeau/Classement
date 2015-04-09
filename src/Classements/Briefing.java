package Classements;

import java.util.List;

import org.jdom2.Element;

public class Briefing extends Ravitaillement
{
	// Construction à partir d'un élément XML
	protected Briefing(Circuit circuit, Element elem)
	{
		super(circuit,elem);
		this.nom = "Briefing";
	}
}