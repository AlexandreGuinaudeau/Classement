package Classements;

public class Homme extends Concurrent
{
	public Homme(String nom, String prenom, String origine)
	{
		super(nom,prenom,origine);
	}
	
	public String civilite()
	{
		return "M.";
	}
	
	public boolean estHomme() {
		return true;
	}

	public boolean estFemme() {
		return false;
	}

}
