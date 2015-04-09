package Classements;

import java.text.SimpleDateFormat;
import java.util.*;

import org.jdom2.*;

public class Circuit
{
	private Date date;
	private Epreuve[] epreuves;
	
	private boolean departEnMasse = false;
	
	private Balise depart;
	private Balise arrivee;
	
	// Construction à partir d'un élément XML
	public Circuit(Element element)
	{
		Element dateElem = element.getChild("date");
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		try
		{
			date = format.parse(dateElem.getText());
		}
		catch(Exception e)
		{
			System.out.println("Le format de la date dans le fichier de spécif du raid est incorrect : "+e.getLocalizedMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		
		List<Element> epreuvesElem = element.getChild("epreuves").getChildren();
		
		epreuves = new Epreuve[epreuvesElem.size()];
		int i=0;
		
		for(Element epreuveElem : epreuvesElem)
		{
			epreuves[i] = Epreuve.epreuveFromXML(this, epreuveElem);
			i++;
		}
	}
	
	public Circuit(Date date, Epreuve[] epreuves)
	{
		this.date = date;
		this.epreuves = epreuves;
	}
	
	public Date date()
	{
		return date;
	}
	
	public Epreuve[] epreuves()
	{
		return this.epreuves;
	}
	
	public void setDepartEnMasse(boolean departEnMasse)
	{
		this.departEnMasse = departEnMasse;
	}
	
	public boolean isDepartEnMasse()
	{
		return this.departEnMasse;
	}
	
	public double tempsDepuisDepartEnMasse(Date date)
	{
		if(!departEnMasse)
		{
			throw new IllegalArgumentException("Le circuit ne comporte pas de départ en masse.");
		}
		
		double res = ((double)(date.getTime()-this.date.getTime()))/1000;
		
		return res;
	}
}
