package Classements;

import BaseDeDonnees.BaseDeDonnees;

public class ClassementFemmes extends Classement
{
	public String type()
	{
		return "Femmes";
	}
	
	public TypeClassements typeClassement()
	{
		return TypeClassements.CLASSEMENT_FEMMES;
	}
	
	public Equipe[] equipes()
	{
		return BaseDeDonnees.shared().equipesFemmes();
	}
}
