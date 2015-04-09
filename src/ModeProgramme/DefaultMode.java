package ModeProgramme;

import Divers.KeyboardInput;

public class DefaultMode extends ModeProgramme
{
	public DefaultMode()
	{
		parent = null;
		children = new ModesProgramme[3];
		
		children[0] = ModesProgramme.MODE_EQUIPES;
		children[1] = ModesProgramme.MODE_IMPORTATION;
		children[2] = ModesProgramme.MODE_CALCUL_CLASSEMENTS;
	}
	
	public String description() {
		return "Menu principal";
	}
	
	public ModesProgramme mode()
	{
		return ModesProgramme.MODE_DEFAULT;
	}

}
