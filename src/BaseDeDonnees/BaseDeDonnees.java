package BaseDeDonnees;

import Classements.*;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.*;
import org.jdom2.filter.*;
import org.jdom2.output.*;

import java.sql.*;

import java.io.*;
import java.util.*;
import java.text.*;

public class BaseDeDonnees
{	
	private Document configuration;
	private String configurationPath = "resources/configuration/configuration.xml";
	
	private Document raidSpecif;
	private String raidSpecifPath;
	
	private Connection conn; // Database connection
	
	private static BaseDeDonnees shared = null;
	
	private static Circuit[] circuits = null;
	
	public static BaseDeDonnees shared()
	{
		if(shared==null)
		{
			shared = new BaseDeDonnees();
		}
		return shared;
	}
	
	public static void prepare()
	{
		shared();
	}
	
	public static <E> E firstOfList(List<E> list)
	{
		if(list==null)
		{
			return null;
		}
		for(E s : list)
		{
			return s;
		}
		return null;
	}
	
	private BaseDeDonnees()
	{
		SAXBuilder sxb = new SAXBuilder();
		System.out.println("Ouverture du fichier de configuration...");
		try
		{
			configuration = sxb.build(new File(configurationPath));
			
			System.out.println("Le fichier de configuration a ete ouvert avec succes.");
		}
		catch(Exception e)
		{
			System.out.println("Impossible d'ouvrir le fichier de configuration : "+e.getLocalizedMessage());
			System.exit(1);
		}
		
		System.out.println("Ouverture du fichier de specification du raid...");
		try
		{
			XPathFactory factory = XPathFactory.instance();
			XPathExpression<Text> expr = factory.compile("//field[key='SpecificationRaid']/value/text()", Filters.text());
			List<Text> res = expr.evaluate(configuration);
			
			this.raidSpecifPath = firstOfList(res).getText();
			
			this.raidSpecif = sxb.build(new File(raidSpecifPath));
			
			System.out.println("Le fichier de specification du raid a ete ouvert avec succes.");
		}
		catch(Exception e)
		{
			System.out.println("Impossible d'ouvrir le fichier de specification du raid : "+e.getLocalizedMessage());
			System.exit(1);
		}
		
		try
		{
			XPathFactory factory = XPathFactory.instance();
			XPathExpression<Text> expr = factory.compile("//field[key='BaseDeDonnees']/value/text()", Filters.text());
			List<Text> res = expr.evaluate(configuration);
			
			String databasePath = firstOfList(res).getText();
			
			Class.forName("org.sqlite.JDBC");  
            conn = DriverManager  
                    .getConnection("jdbc:sqlite:"+databasePath);
		}
		catch(Exception e)
		{
			System.out.println("Impossible d'ouvrir la connection a la base de donnee : "+e.getLocalizedMessage());
			System.exit(1);
		}
	}
	
	public void setDoigtForEquipe(Doigt doigt,Equipe equipe)
	{
		PreparedStatement statement;
		try
		{
			statement = conn.prepareStatement("UPDATE Equipes SET doigt=? WHERE dossard=?");
			statement.setInt(1, doigt.id());
			statement.setInt(2, equipe.dossard());
			statement.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println("Une erreur s'est produite : "+e.getLocalizedMessage());
		}
		
	}
	
	public Equipe equipeAvecDossard(int dossard)
	{
		PreparedStatement statement;
		try
		{
			statement = conn.prepareStatement("SELECT nom_equipe,commentaires,sexe1,nom1,prenom1,ecole1,sexe2,nom2,prenom2,ecole2,doigt,carteOuverte,partie,abandon FROM Equipes WHERE dossard=?");
			statement.setInt(1, dossard);
			ResultSet result = statement.executeQuery();
			
			if(result.isClosed() || (result.isBeforeFirst() && result.isAfterLast()))
			{
				return null;
			}
			
			int numDoigt = result.getInt(11);
			Doigt doigt = null;
			if(!result.wasNull())
			{
				doigt = new Doigt(numDoigt);
			}
			
			Concurrent[] concurrents = new Concurrent[2];
			if(result.getString(3).equals("M."))
			{
				concurrents[0] = new Homme(result.getString(4),result.getString(5),result.getString(6));
			}
			else
			{
				concurrents[0] = new Femme(result.getString(4),result.getString(5),result.getString(6),result.getString(3));
			}
			if(result.getString(7).equals("M."))
			{
				concurrents[1] = new Homme(result.getString(8),result.getString(9),result.getString(10));
			}
			else
			{
				concurrents[1] = new Femme(result.getString(8),result.getString(9),result.getString(10),result.getString(7));
			}
			Equipe equipe = new Equipe(dossard,result.getString(1),result.getString(2),doigt,result.getBoolean(12),result.getBoolean(13),result.getBoolean(14),concurrents);
			result.close();
			return equipe;
		}
		catch(Exception e)
		{
			System.out.println("Une erreur s'est produite : "+e.getLocalizedMessage());
			return null;
		}
	}
	
	public void savePointage(Pointage pointage)
	{		
		PreparedStatement statement;
		try
		{
			statement = conn.prepareStatement("INSERT OR IGNORE INTO Pointage (doigt,boitier,date,type) VALUES (?,?,?,?)");
			statement.setInt(1, pointage.doigt().id());
			statement.setInt(2, pointage.boitier().id());
			statement.setLong(3, pointage.date().getTime());
			statement.setInt(4, pointage.type());
			statement.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println("Une erreur s'est produite : "+e.getLocalizedMessage());
		}
	}
	
	public void disconnectFromDatabase()
	{
		try
		{
			conn.close();
		}
		catch(Exception e)
		{
			System.out.println("Une erreur s'est produite : "+e.getLocalizedMessage());
		}
	}
	
	public Circuit[] circuits()
	{
		if(circuits == null)
		{
			XPathFactory factory = XPathFactory.instance();
			XPathExpression<Element> expr = factory.compile("//raid/circuit", Filters.fclass(Element.class));
			List<Element> elemCircuits = expr.evaluate(this.raidSpecif);
			
			circuits = new Circuit[elemCircuits.size()];
			int i=0;
			
			for(Element elemCircuit : elemCircuits)
			{
				circuits[i] = new Circuit(elemCircuit);
				i++;
			}
		}
		
		return circuits;
	}
	
	public Pointage[] pointagesEquipe(Equipe equipe)
	{
		Doigt doigt = equipe.doigt();
		
		if(doigt==null)
		{
			return null;
		}
		
		PreparedStatement statement;
		try
		{
			statement = conn.prepareStatement("SELECT boitier,date,type FROM Pointage WHERE doigt=? ORDER BY date ASC");
			statement.setInt(1, doigt.id());
			ResultSet result = statement.executeQuery();
			
			List<Pointage> liste = new LinkedList<Pointage>();
			
			while(result.next())
			{
				Pointage pointage = new Pointage(result.getDate(2),new Boitier(result.getInt(1)),doigt,result.getInt(3));
				
				liste.add(pointage);
			}
			
			Pointage[] res = new Pointage[liste.size()];
			int i=0;
			for(Pointage pointage : liste)
			{
				res[i] = pointage;
				i++;
			}

			return res;
		}
		catch(Exception e)
		{
			System.out.println("Une erreur s'est produite : "+e.getLocalizedMessage());
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}
	
	public Equipe[] equipesSansDoigt()
	{
		List<Equipe> liste = new LinkedList<Equipe>();
		
		PreparedStatement statement;
		try
		{
			statement = conn.prepareStatement("SELECT dossard FROM Equipes WHERE partie = 1 AND abandon = 0 AND doigt IS NULL ORDER BY dossard ASC");
			ResultSet result = statement.executeQuery();
			
			while(result.next())
			{
				liste.add(this.equipeAvecDossard(result.getInt(1)));
			}
			
			Equipe[] res = new Equipe[liste.size()];
			int i=0;
			for(Equipe equipe : liste)
			{
				res[i] = equipe;
				i++;
			}

			return res;
		}
		catch(Exception e)
		{
			System.out.println("Une erreur s'est produite : "+e.getLocalizedMessage());
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}
	
	
	public Equipe[] equipesHommes()
	{
		List<Equipe> liste = new LinkedList<Equipe>();
		
		PreparedStatement statement;
		try
		{
			statement = conn.prepareStatement("SELECT dossard FROM Equipes WHERE categorie='masculin' AND partie = 1 AND abandon = 0 ORDER BY dossard ASC");
			ResultSet result = statement.executeQuery();
			
			while(result.next())
			{
				liste.add(this.equipeAvecDossard(result.getInt(1)));
			}
			
			Equipe[] res = new Equipe[liste.size()];
			int i=0;
			for(Equipe equipe : liste)
			{
				res[i] = equipe;
				i++;
			}

			return res;
		}
		catch(Exception e)
		{
			System.out.println("Une erreur s'est produite : "+e.getLocalizedMessage());
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}
	
	
	public Equipe[] equipesFemmes()
	{
		List<Equipe> liste = new LinkedList<Equipe>();
		
		PreparedStatement statement;
		try
		{
			statement = conn.prepareStatement("SELECT dossard FROM Equipes WHERE categorie='feminin' AND partie = 1 AND abandon = 0 ORDER BY dossard ASC");
			ResultSet result = statement.executeQuery();
			
			while(result.next())
			{
				liste.add(this.equipeAvecDossard(result.getInt(1)));
			}
			
			Equipe[] res = new Equipe[liste.size()];
			int i=0;
			for(Equipe equipe : liste)
			{
				res[i] = equipe;
				i++;
			}

			return res;
		}
		catch(Exception e)
		{
			System.out.println("Une erreur s'est produite : "+e.getLocalizedMessage());
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}
	
	
	public Equipe[] equipesMixtes()
	{
		List<Equipe> liste = new LinkedList<Equipe>();
		
		PreparedStatement statement;
		try
		{
			statement = conn.prepareStatement("SELECT dossard FROM Equipes WHERE categorie='mixte' AND partie = 1 AND abandon = 0 ORDER BY dossard ASC");
			ResultSet result = statement.executeQuery();
			
			while(result.next())
			{
				liste.add(this.equipeAvecDossard(result.getInt(1)));
			}
			
			Equipe[] res = new Equipe[liste.size()];
			int i=0;
			for(Equipe equipe : liste)
			{
				res[i] = equipe;
				i++;
			}

			return res;
		}
		catch(Exception e)
		{
			System.out.println("Une erreur s'est produite : "+e.getLocalizedMessage());
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}
	
	
	public Equipe[] equipesAreva()
	{
		List<Equipe> liste = new LinkedList<Equipe>();
		
		PreparedStatement statement;
		try
		{
			statement = conn.prepareStatement("SELECT dossard FROM Equipes WHERE categorie_liste!= 'Etudiant' AND partie = 1 AND abandon = 0 ORDER BY dossard ASC");
			ResultSet result = statement.executeQuery();
			
			while(result.next())
			{
				liste.add(this.equipeAvecDossard(result.getInt(1)));
			}
			
			Equipe[] res = new Equipe[liste.size()];
			int i=0;
			for(Equipe equipe : liste)
			{
				res[i] = equipe;
				i++;
			}

			return res;
		}
		catch(Exception e)
		{
			System.out.println("Une erreur s'est produite : "+e.getLocalizedMessage());
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}
	public Equipe[] equipesEtudiant()
	{
		List<Equipe> liste = new LinkedList<Equipe>();
		
		PreparedStatement statement;
		try
		{
			statement = conn.prepareStatement("SELECT dossard FROM Equipes WHERE categorie_liste= 'Etudiant' AND partie = 1 AND abandon = 0 ORDER BY dossard ASC");
			ResultSet result = statement.executeQuery();
			
			while(result.next())
			{
				liste.add(this.equipeAvecDossard(result.getInt(1)));
			}
			
			Equipe[] res = new Equipe[liste.size()];
			int i=0;
			for(Equipe equipe : liste)
			{
				res[i] = equipe;
				i++;
			}

			return res;
		}
		catch(Exception e)
		{
			System.out.println("Une erreur s'est produite : "+e.getLocalizedMessage());
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}
	
	
	public Equipe[] equipesEnCourse()
	{
		List<Equipe> liste = new LinkedList<Equipe>();
		
		PreparedStatement statement;
		try
		{
			statement = conn.prepareStatement("SELECT dossard FROM Equipes WHERE partie = 1 AND abandon = 0 ORDER BY dossard ASC");
			ResultSet result = statement.executeQuery();
			
			while(result.next())
			{
				liste.add(this.equipeAvecDossard(result.getInt(1)));
			}
			
			Equipe[] res = new Equipe[liste.size()];
			int i=0;
			for(Equipe equipe : liste)
			{
				res[i] = equipe;
				i++;
			}

			return res;
		}
		catch(Exception e)
		{
			System.out.println("Une erreur s'est produite : "+e.getLocalizedMessage());
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}
	
	public Equipe[] equipesAbandon()
	{
		List<Equipe> liste = new LinkedList<Equipe>();
		
		PreparedStatement statement;
		try
		{
			statement = conn.prepareStatement("SELECT dossard FROM Equipes WHERE abandon = 1 AND partie=1 ORDER BY dossard ASC");
			ResultSet result = statement.executeQuery();
			
			while(result.next())
			{
				liste.add(this.equipeAvecDossard(result.getInt(1)));
			}
			
			Equipe[] res = new Equipe[liste.size()];
			int i=0;
			for(Equipe equipe : liste)
			{
				res[i] = equipe;
				i++;
			}

			return res;
		}
		catch(Exception e)
		{
			System.out.println("Une erreur s'est produite : "+e.getLocalizedMessage());
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}
	
	public Equipe[] equipesNonParties()
	{
		List<Equipe> liste = new LinkedList<Equipe>();
		
		PreparedStatement statement;
		try
		{
			statement = conn.prepareStatement("SELECT dossard FROM Equipes WHERE partie = 0 ORDER BY dossard ASC");
			ResultSet result = statement.executeQuery();
			
			while(result.next())
			{
				liste.add(this.equipeAvecDossard(result.getInt(1)));
			}
			
			Equipe[] res = new Equipe[liste.size()];
			int i=0;
			for(Equipe equipe : liste)
			{
				res[i] = equipe;
				i++;
			}

			return res;
		}
		catch(Exception e)
		{
			System.out.println("Une erreur s'est produite : "+e.getLocalizedMessage());
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}
	
	public Equipe[] equipesSansPointages(java.util.Date date)
	{
		List<Equipe> liste = new LinkedList<Equipe>();
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		
		int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
		String day = format.format(date);
		
		java.util.Date start, end;
		
		try
		{
			start = format.parse(day);
			end = new java.util.Date(start.getTime()+MILLIS_IN_DAY);
		}
		catch(Exception e)
		{
			System.out.println("Une erreur s'est produite : "+e.getLocalizedMessage());
			e.printStackTrace();
			System.exit(0);
			return null;
		}
		
		PreparedStatement statement;
		try
		{
			statement = conn.prepareStatement("SELECT dossard FROM Equipes WHERE partie = 1 AND abandon = 0 AND Equipes.doigt IS NULL OR NOT EXISTS (SELECT * FROM Pointage WHERE Pointage.doigt=Equipes.doigt AND Pointage.date >= ? AND Pointage.date < ?) ORDER BY dossard ASC");
			statement.setLong(1,start.getTime());
			statement.setLong(2,end.getTime());
			
			ResultSet result = statement.executeQuery();
			
			while(result.next())
			{
				Equipe equipe = this.equipeAvecDossard(result.getInt(1));
				if(!equipe.abandon() && equipe.partie())
				{
					liste.add(this.equipeAvecDossard(result.getInt(1)));
				}
			}
			
			Equipe[] res = new Equipe[liste.size()];
			int i=0;
			for(Equipe equipe : liste)
			{
				res[i] = equipe;
				i++;
			}
			
			return res;
		}
		catch(Exception e)
		{
			System.out.println("Une erreur s'est produite : "+e.getLocalizedMessage());
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}
	
	public int numeroCircuitAvecDate(java.util.Date date)
	{
		Circuit[] circuits = this.circuits();
		
		for(int i=0 ; i<circuits.length ; i++)
		{
			if(circuits[i].date().equals(date))
			{
				return i;
			}
		}
		return -1;
	}
}
