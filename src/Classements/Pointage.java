package Classements;
import java.util.*;

public class Pointage
{
	private Date date;
	private Boitier boitier;
	private Doigt doigt;
	private TypePointage type;
	
	public Date date()
	{
		return date;
	}
	
	public Boitier boitier()
	{
		return boitier;
	}
	
	public Doigt doigt()
	{
		return doigt;
	}
	
	public int type()
	{
		switch(type)
		{
			case TYPE_BALISE:
				return 0;
			case TYPE_START:
				return 1;
			case TYPE_FINISH:
				return 2;
		}
		return 0;
	}
	
	public boolean isStart()
	{
		return type == TypePointage.TYPE_START;
	}
	
	public boolean isFinish()
	{
		return type == TypePointage.TYPE_FINISH;
	}
	
	public String stringType()
	{
		switch(type)
		{
			case TYPE_BALISE:
				return "Balise";
			case TYPE_START:
				return "Depart";
			case TYPE_FINISH:
				return "Arrivee";
		}
		return null;
	}
	
	public Pointage(Date date, Boitier boitier, Doigt doigt, TypePointage type)
	{
		this.date = date;
		this.boitier = boitier;
		this.doigt = doigt;
		this.type = type;
	}
	
	public Pointage(Date date, Boitier boitier, Doigt doigt, int type)
	{
		this.date = date;
		this.boitier = boitier;
		this.doigt = doigt;
		switch(type)
		{
			case 0:
				this.type = TypePointage.TYPE_BALISE;
				break;
			case 1:
				this.type = TypePointage.TYPE_START;
				break;
			case 2:
				this.type = TypePointage.TYPE_FINISH;
				break;
		}
	}
	
	public boolean equals(Pointage p2)
	{
		return date.equals(p2.date) && boitier.equals(p2.boitier) && doigt.equals(p2.doigt) && type.equals(p2.type);
	}
	
	// timeSince : renvoie l'ecart en secondes entre le pointage en cours et le pointage p2
	// positif si le pointage en cours a eu lieu apres p2
	public double timeSince(Pointage p2)
	{
		return ((double)(date.getTime() - p2.date.getTime()))/1000;
	}
	
	public boolean isAfter(Pointage p2)
	{
		return date.after(p2.date);
	}
	
	public boolean isBefore(Pointage p2)
	{
		return date.before(p2.date);
	}
}