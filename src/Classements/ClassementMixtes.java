package Classements;

import BaseDeDonnees.BaseDeDonnees;

public class ClassementMixtes extends Classement
{
	public String type()
	{
		return "Mixte";
	}
	
	public TypeClassements typeClassement()
	{
		return TypeClassements.CLASSEMENT_MIXTES;
	}
	
	public Equipe[] equipes()
	{
		return BaseDeDonnees.shared().equipesMixtes();
	}
}
