package ReglesEpreuves;

import Classements.Balise;

public class Penalite
{
	private Balise balise;
	private double penalite; // en minutes
	
	public Penalite(Balise balise, double minutes)
	{
		this.balise = balise;
		this.penalite = minutes;
	}
	
	public Balise balise()
	{
		return balise;
	}
	
	public double minutesDePenalites()
	{
		return penalite;
	}
}
