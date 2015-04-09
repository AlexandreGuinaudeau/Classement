package Classements;

import java.util.*;

public class DetailBalisesManquees extends DetailsResultatEpreuve
{
	private Set<Balise> balises;
	private ResultatEquipeEpreuve resultat;
	
	public DetailBalisesManquees(ResultatEquipeEpreuve resultat)
	{
		this.resultat = resultat;
		balises = new HashSet<Balise>();
	}
	
	public void addBaliseManquee(Balise balise)
	{
		balises.add(balise);
	}
	
	public String toString()
	{
		String res;
		
		res = "\\balisesmanquees{";
		
		int nbBalises = 0;
		
		for(Balise balise : resultat.epreuve().balises())
		{
			if(balises.contains(balise))
			{
				if(nbBalises>0)
				{
					res += ", ";
				}
				res += balise.numero();
				nbBalises++;
			}
		}
		
		res += "}\n";
		
		if(nbBalises>0)
		{
			return res;
		}
		return "";
	}
}
