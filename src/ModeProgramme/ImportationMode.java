package ModeProgramme;

public class ImportationMode extends ModeProgramme {

	public ImportationMode()
	{
		parent = ModeProgramme.mode(ModesProgramme.MODE_DEFAULT);
		
		children = new ModesProgramme[3];
		children[0] = ModesProgramme.MODE_ACQUISITION_POINTAGES;
		children[1] = ModesProgramme.MODE_LECTURE_POINTAGES;
		children[2] = ModesProgramme.MODE_EQUIPESSANSPOINTAGES;
	}
	
	public String description() {
		return "Gestion des donnees des doigts electroniques";
	}

	public ModesProgramme mode()
	{
		return ModesProgramme.MODE_IMPORTATION;
	}

	public void toggle()
	{
		super.toggle();
		
	}
}
