
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.EmbedType;
import net.dv8tion.jda.core.entities.User;
	 
	public class SqlConnection {
	   
	    private Connection connection;
	    private String urlbase,host,database,user,pass;
	   
	    public SqlConnection(String urlbase, String host, String database, String user, String pass) {
	        this.urlbase = urlbase;
	        this.host = host;
	        this.database = database;
	        this.user = user;
	        this.pass = pass;
	    }
	   
	    public void connection(){
	        if(!isConnected()){
	            try {
	                connection = DriverManager.getConnection(urlbase + host + "/" + database, user, pass);
	                System.out.println("[BD]Request connexion accepted");
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	   
	    public void disconnect(){
	        if(isConnected()){
	            try {
	                connection.close();
	                System.out.println("[BD]Discoconected");
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    
		   
	    public boolean isConnected(){
	        return connection != null;
	    }
	    
	    public void createData(User user){
	    	if(!hasData(user)){
	    		try {
	    			//SQL Statement
					PreparedStatement querry = connection.prepareStatement("INSERT INTO account(ID, Rank) VALUES (?,?)");
					querry.setString(1, user.getId().toString());
					querry.setInt(2, 0);
					querry.execute();
					querry.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
	    	};
	    }
	    
	    public boolean hasData(User user){
	    	try {
				PreparedStatement querry = connection.prepareStatement("SELECT ID FROM account WHERE ID = ?");
				querry.setString(1, user.getId());
				ResultSet resultat = querry.executeQuery();
				Boolean hasData = resultat.next();
				querry.close();
				return hasData;
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return false;  	
	    }
	    
	    public int getRank(User user){

	    	try {
				PreparedStatement querry = connection.prepareStatement("SELECT Rank FROM account WHERE ID = ?");
				querry.setString(1, user.getId());
		    	int balance = 0;
		    	
				ResultSet rs = querry.executeQuery();
				while (rs.next()){
					balance = rs.getInt("Rank");					
				}
				querry.close();		
				return balance;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return 0;
	    }
	    
	    public void setRank(User user, int Rank){
	    	
	    	int balance = getRank(user);
	    	int amount = balance + Rank;
	    	
	    	
	    	try {
				PreparedStatement querry = connection.prepareStatement("UPDATE account SET Rank = ? WHERE ID = ?");
				querry.setInt(1, amount);
				querry.setString(2, user.getId().toString());
				querry.executeUpdate();
				querry.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
	    
	}
