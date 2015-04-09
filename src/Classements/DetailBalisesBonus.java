package Classements;

import java.util.HashSet;
import java.util.Set;

public class DetailBalisesBonus extends DetailsResultatEpreuve
{
	private Set<Balise> balises;
	private ResultatEquipeEpreuve resultat;
	
	public DetailBalisesBonus(ResultatEquipeEpreuve resultat)
	{
		this.resultat = resultat;
		balises = new HashSet<Balise>();
	}
	
	public void addBaliseBonus(Balise balise)
	{
		balises.add(balise);
	}
	
	public String toString()
	{
		String res;
		
		res = "\\balisesbonus{";
		
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
