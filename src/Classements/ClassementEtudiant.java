package Classements;

import BaseDeDonnees.BaseDeDonnees;

public class ClassementEtudiant extends Classement
{
	public String type()
	{
		return "Etudiant";
	}
	
	public TypeClassements typeClassement()
	{
		return TypeClassements.CLASSEMENT_ETUDIANT;
	}
	
	public Equipe[] equipes()
	{
		return BaseDeDonnees.shared().equipesEtudiant();
	}
}
