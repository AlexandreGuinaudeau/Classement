package Classements;

public abstract class Concurrent
{
	private String nom;
	private String prenom;
	private String origine;
	
	public Concurrent(String nom, String prenom, String origine)
	{
		this.nom = nom;
		this.prenom = prenom;
		this.origine = origine;
	}
	
	public String nom()
	{
		return nom;
	}
	
	public String prenom()
	{
		return prenom;
	}
	
	public String origine()
	{
		return origine;
	}
	
	public abstract boolean estHomme();
	
	public abstract boolean estFemme();
	
	public abstract String civilite();
}
