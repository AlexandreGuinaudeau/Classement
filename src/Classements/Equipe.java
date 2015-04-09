package Classements;

import BaseDeDonnees.*;

public class Equipe
{
	private int dossard;
	private String nomEquipe;
	private String commentaires;
	private Concurrent[] membres;
	private Doigt doigt;
	private boolean carteOuverte;
	private boolean partie;
	private boolean abandon;
	
	public Equipe(int dossard, String nomEquipe, String commentaires, Doigt doigt, boolean carteOuverte, boolean partie, boolean abandon, Concurrent[] membres)
	{
		this.dossard = dossard;
		this.nomEquipe = nomEquipe;
		this.commentaires = commentaires;
		this.membres = membres;
		this.doigt = doigt;
		this.carteOuverte = carteOuverte;
		this.partie = partie;
		this.abandon = abandon;
	}
	
	public static Equipe equipeAvecDossard(int dossard)
	{
		return BaseDeDonnees.shared().equipeAvecDossard(dossard);
	}
	
	public int dossard()
	{
		return dossard;
	}
	
	public int nbMembres()
	{
		return membres.length;
	}
	
	public Concurrent[] membres()
	{
		return membres;
	}
	
	public String nomEquipe()
	{
		return this.nomEquipe;
	}
	
	public Doigt doigt()
	{
		return doigt;
	}
	
	public boolean carteOuverte()
	{
		return this.carteOuverte;
	}
	
	public boolean partie()
	{
		return this.partie;
	}
	
	public boolean abandon()
	{
		return this.abandon;
	}
}
