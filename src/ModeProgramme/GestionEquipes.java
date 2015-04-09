package ModeProgramme;

public class GestionEquipes extends ModeProgramme {

	public GestionEquipes()
	{
		parent = ModeProgramme.mode(ModesProgramme.MODE_DEFAULT);
		
		children = new ModesProgramme[1];
		
		children[0] = ModesProgramme.MODE_AFFECTATION_DOIGTS;
	}
	
	@Override
	public String description() {
		return "Gestion des equipes";
	}

	@Override
	public ModesProgramme mode() {
		return ModesProgramme.MODE_EQUIPES;
	}

}
