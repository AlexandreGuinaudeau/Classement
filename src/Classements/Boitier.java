package Classements;

public class Boitier
{
	private int id;
	
	public int id()
	{
		return id;
	}
	
	public Boitier(int id)
	{
		this.id = id;
	}
	
	public boolean equals(Boitier boitier)
	{
		return id == boitier.id;
	}
}