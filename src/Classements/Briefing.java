package Classements;

import java.util.List;

import org.jdom2.Element;

public class Briefing extends Ravitaillement
{
	// Construction a partir d'un element XML
	protected Briefing(Circuit circuit, Element elem)
	{
		super(circuit,elem);
		this.nom = "Briefing";
	}
}