package Classements;

import java.util.Comparator;

public class ComparaisonResultatsSpeciaux implements Comparator<ResultatSpeciale>
{
	public int compare(ResultatSpeciale res1, ResultatSpeciale res2)
	{
		Double coeff1 = res1.tempsTotal();//res1.coeff();
		Double coeff2 = res2.tempsTotal();//res2.coeff();
		
		return coeff1.compareTo(coeff2);
	}
}
