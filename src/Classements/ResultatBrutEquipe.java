package Classements;

import java.text.SimpleDateFormat;

import BaseDeDonnees.*;
import Interface.Keyboard;

public class ResultatBrutEquipe
{
	private Equipe equipe;
	private Pointage[] pointages;
	private boolean carteOuverte;
	
	public Pointage[] pointages()
	{
		return pointages;
	}
	
	public ResultatBrutEquipe(Equipe equipe)
	{
		this.equipe = equipe;
		
		pointages = BaseDeDonnees.shared().pointagesEquipe(equipe);
		
		carteOuverte = equipe.carteOuverte();
	}
	
	public Equipe equipe()
	{
		return equipe;
	}
	
	public boolean carteOuverte()
	{
		return carteOuverte;
	}
	
	public void affichage()
	{
	  String msg;
		msg = "Resultat de l'equipe "+equipe.nomEquipe()+" n°"+equipe.dossard()+"\n";
		
		if(equipe.doigt() == null)
		{
			msg+="L'equipe n°"+equipe.dossard()+" n'est pour le moment pas associee a un doigt electronique. Affectez-lui un doigt electronique et faites l'acquisition des donnees du doigt avant de verifier la liste des pointages.\n";
			Keyboard.KEYBOARD.println(msg);
			return;
		}
		
		msg+="Numero de doigt : "+equipe.doigt().id()+"\n";
		
		if(pointages.length == 0)
		{
			msg+="L'equipe n°"+equipe.dossard()+" n'a pour le moment aucun pointages enregistres.\n";
			Keyboard.KEYBOARD.println(msg);
			return;
		}
		
		SimpleDateFormat jourFormatter = new SimpleDateFormat("EEEE");
		
		SimpleDateFormat pointageFormatter = new SimpleDateFormat("HH:mm:ss");
		
		String jour = null;
		
		for(Pointage pointage : pointages)
		{
			String jourPointage = jourFormatter.format(pointage.date());
			
			if(!jourPointage.equals(jour))
			{
				jour = jourPointage;
				msg+="Pointages du "+jour+"\n";
				
				msg+="Boitier\tHeure de pointage\tType\n";
			}
			
			msg+=pointage.boitier().id()+"\t"+pointageFormatter.format(pointage.date())+"\t\t"+pointage.stringType()+"\n";
		}

		msg+="\n";
		Keyboard.KEYBOARD.println(msg);
	}
	
	public boolean estPointee(Balise balise)
	{
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		
		String jour = format.format(balise.epreuve().circuit().date());
		
		for(Pointage pointage : pointages)
		{
			String jourPointage = format.format(pointage.date());
			
			if(jour.equals(jourPointage) && pointage.boitier().equals(balise.boitier()))
			{
				return true;
			}
		}
		return false;
	}
	
	public Pointage pointage(Balise balise)
	{
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String jour = null;
		try{
			jour = format.format(balise.epreuve().circuit().date());
		}
		catch(Exception e)
		{
			throw new IllegalArgumentException("Erreur");
		}
		
		for(int i=pointages.length-1 ; i>=0 ; i--)
		{
			Pointage pointage = pointages[i];
			String jourPointage = format.format(pointage.date());
			
			if(jour.equals(jourPointage) && pointage.boitier().equals(balise.boitier()))
			{
				return pointage;
			}
		}
		return null;
		
	}
	
	public Pointage start(Circuit circuit)
	{
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		
		String jour = format.format(circuit.date());
		
		for(Pointage pointage : pointages)
		{
			String jourPointage = format.format(pointage.date());
			
			if(jour.equals(jourPointage) && pointage.isStart())
			{
				return pointage;
			}
		}
		return null;
	}
	
	public Pointage finish(Circuit circuit)
	{
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		
		String jour = format.format(circuit.date());
		
		for(Pointage pointage : pointages)
		{
			String jourPointage = format.format(pointage.date());
			if(jour.equals(jourPointage) && pointage.isFinish())
			{
				return pointage;
			}
		}
		return null;
	}
	
	public int positionDansListePointages(Balise balise)
	{
		Pointage resPointage = null;
		int res = 0;
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		
		String jour = format.format(balise.epreuve().circuit().date());
		
		int i=0;
		
		for(Pointage pointage : pointages)
		{
			String jourPointage = format.format(pointage.date());
			
			if(jour.equals(jourPointage) && pointage.boitier().equals(balise.boitier()))
			{
				resPointage = pointage;
				res = i;
				break;
			}
			i++;
		}
		
		if(resPointage == null)
		{
			throw new IllegalArgumentException("La balise n°"+balise.numero()+" n'est pas pointee dans la liste de pointages de l'equipe n°"+this.equipe().dossard());
		}
		
		return res;
	}
}
