package Classements;

import BaseDeDonnees.BaseDeDonnees;

public class ClassementAreva extends Classement
{
	public String type()
	{
		return "Entreprise";
	}
	
	public TypeClassements typeClassement()
	{
		return TypeClassements.CLASSEMENT_AREVA;
	}
	
	public Equipe[] equipes()
	{
		return BaseDeDonnees.shared().equipesAreva();
	}
}
