package Classements;

import java.text.SimpleDateFormat;

import org.jdom2.*;

import ReglesEpreuves.Penalite;

public class Balise
{
	private Boitier boitier;
	private int numero;
	private Epreuve epreuve;
	
	public Balise(Epreuve epreuve, Element element)
	{
		this.epreuve = epreuve;
		
		numero = Integer.parseInt(element.getChildText("numero"));
		
		boitier = new Boitier(Integer.parseInt(element.getChildText("boitier")));
		
		String type = element.getAttributeValue("type");
		
		if(type!=null)
		{
			if(type.equals("start"))
			{
				epreuve.setStartBalise(this);
			}
			if(type.equals("finish"))
			{
				epreuve.setFinishBalise(this);
			}
		}
		
		Element bonus = element.getChild("bonus");
		
		if(bonus != null)
		{
			Penalite penalite = new Penalite(this,-Double.parseDouble(bonus.getText()));
			
			epreuve.penalitesBalises().ajouterPenalite(penalite);
		}
		
		Element malus = element.getChild("malus");
		
		if(malus != null)
		{
			Penalite penalite = new Penalite(this,Double.parseDouble(malus.getText()));
			
			epreuve.penalitesBalises().ajouterPenalite(penalite);
		}
	}
	
	public Balise(Boitier boitier, int numero, Epreuve epreuve)
	{
		this.boitier = boitier;
		this.numero = numero;
		this.epreuve = epreuve;
	}
	
	public int numero()
	{
		return numero;
	}
	
	public Epreuve epreuve()
	{
		return epreuve;
	}
	
	public Boitier boitier()
	{
		return boitier;
	}
}