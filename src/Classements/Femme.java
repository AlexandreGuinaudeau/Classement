package Classements;

public class Femme extends Concurrent {

	private String civilite;
	
	public Femme(String nom, String prenom, String origine, String civilite)
	{
		super(nom,prenom,origine);
		this.civilite = civilite;
	}
	
	public boolean estHomme() {
		return false;
	}

	public boolean estFemme() {
		return true;
	}

	public String civilite() {
		return civilite;
	}

}
