package Classements;

public class ClassementSprinteur extends ClassementGrimpeur {

	@Override
	public String type() {
		return Classement.nomClassement(TypeClassements.CLASSEMENT_SPRINTEUR);
	}
	
	public TypeClassements typeClassement()
	{
		return TypeClassements.CLASSEMENT_SPRINTEUR;
	}

}
