package Classements;

import BaseDeDonnees.BaseDeDonnees;

public class ClassementHommes extends Classement {

	@Override
	public String type() {
		return "Hommes";
	}

	@Override
	public TypeClassements typeClassement() {
		return TypeClassements.CLASSEMENT_HOMMES;
	}
	
	public Equipe[] equipes()
	{
		return BaseDeDonnees.shared().equipesHommes();
	}

}
