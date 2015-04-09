package Classements;

public class Doigt
{
	private int id;
	
	public Doigt(int id)
	{
		this.id = id;
	}
	
	public int id()
	{
		return id;
	}
	
	public boolean equals(Doigt doigt)
	{
		return id == doigt.id;
	}
}
