package Classements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import BaseDeDonnees.BaseDeDonnees;
import Divers.StringManipulation;

public class ClassementGrimpeur extends Classement {

	@Override
	public String type() {
		return Classement.nomClassement(TypeClassements.CLASSEMENT_GRIMPEUR);
	}
	
	public TypeClassements typeClassement()
	{
		return TypeClassements.CLASSEMENT_GRIMPEUR;
	}
	
	public void calculClassement(Date upToDate)
	{
		Equipe[] equipes = this.equipes();
		
		List<ResultatSpeciale> resultats = new LinkedList<ResultatSpeciale>();
		
		for(int i=0 ; i<equipes.length ; i++)
		{
			if(equipes[i].doigt()!=null)
			{
				Pointage[] pointages = BaseDeDonnees.shared().pointagesEquipe(equipes[i]);
				
				if(pointages!=null && pointages.length>0)
				{
					ResultatSpeciale tmp = new ResultatSpeciale(equipes[i], this, upToDate);
					tmp.calculerResultat();
					
					if(tmp.wasSuccessfullyCalculated())
					{
						resultats.add(tmp);
					}
				}
			}
			else
			{
				ResultatSpeciale.writeToLogFile("\nL'equipe n°"+equipes[i].dossard()+" n'a pas de numero de doigt associe. Abandon du calcul de leur resultat au raid.\n");
			}
		}
		
		Collections.sort(resultats, new ComparaisonResultatsSpeciaux());
		
		this.sauvegarderResultatsSpeciaux(resultats, upToDate);
		
		ResultatSpeciale.closeLogFile();
	}

}
