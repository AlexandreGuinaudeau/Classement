package Classements;

import java.util.Comparator;

public class ComparaisonResultatsEquipes implements Comparator<ResultatEquipe>
{
	public int compare(ResultatEquipe res1, ResultatEquipe res2)
	{
		Double temps1 = res1.tempsCumuleDuJour();
		Double temps2 = res2.tempsCumuleDuJour();
		
		return temps1.compareTo(temps2);
	}
}
