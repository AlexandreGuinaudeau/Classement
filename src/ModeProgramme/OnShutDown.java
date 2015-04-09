package ModeProgramme;

import BaseDeDonnees.BaseDeDonnees;
import Classements.*;

public class OnShutDown extends Thread
{
	
	public void run() {
		System.out.println("Shutting down");
        BaseDeDonnees.shared().disconnectFromDatabase();
	}

}
