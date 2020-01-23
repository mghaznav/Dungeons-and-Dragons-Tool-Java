package monsters;

import java.util.ArrayList;
import java.sql.*;


public class DBConnection {
	
	private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    private String username;
    private String password;
	

	
	public DBConnection(){
		this(DBDetails.username, DBDetails.password);

		//connect(); // test our connection
	}
	
	public DBConnection(String u, String p){
		username = u;
		password = p; 
	}
	
	/**
	 * NOT FOR GENERAL USE. Placed in a separate method to ensure opening and closing of the connection is handled
	 * every time a request is made, rather than having a connection always open and relying 
	 * on students to close it.
	 */
	public void connect(){
		//Initialize the connection
		try{
			Class.forName(DBDetails.JDBC_DRIVER);			
		    conn = DriverManager.getConnection(DBDetails.DB_URL,username,password);
		}
		catch (Exception e){
			System.out.println(e);
		}
    }
    
    
	
	/**
	 * Returns a list of strings, where each string represents a full course. You will need to parse this string to rebuild the course
	 * 
	 * @return List of Strings representing every available course
	 */
	public ArrayList<Monster> getAllMonsters(){
		ArrayList<Monster> mList = new ArrayList<Monster>();		
        String sql = "SELECT * FROM Monsters;";
		connect();
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
			while(rs.next()){
				mList.add(new Monster(rs.getString("name"), rs.getString("upper"), rs.getString("lower"), rs.getString("description")));
			}

		}
        
        //catch any issues along the way
		catch (Exception e){
			System.out.println(e);
		}
		//close any/all connections
		finally{
			try{
				stmt.close();
				conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		return mList;
	}
	
	


	public void addMonster(String name, String upper, String lower, String description){
		String sql = "INSERT INTO Monsters(name,upper,lower,description) VALUES(" + "'" + name + "','" + upper + "','" + lower + "','" + description + "');";

		dbUpdate(sql);
	}
	
	/**
	 * Saves information to the database. Accepts a Monster object only. 
	 * 
	 * @param s A Monster object. Its contents will be saved to the database.
	 */
	public void addMonster(Monster monster){
		String name = monster.getName();
		String upper = monster.getUpper();
		String lower = monster.getLower();
		String description = monster.getDescription();
			
		String sql = "INSERT INTO Monsters(name,upper,lower,description) VALUES(" + "'" + name + "','" + upper + "','" + lower + "','" + description + "');";
		dbUpdate(sql);
     }
	

	public void updateMonster(Monster monster){

		String name = monster.getName();
		String upper = monster.getUpper();
		String lower = monster.getLower();
		String description = monster.getDescription();
		String sql = String.format("UPDATE Monsters set name = \"%s\", upper = \"%s\", lower = \"%s\", description = \"%s\" where name= \"%s\"", name, upper, lower, description, name);
 		System.out.println(sql);
 		dbUpdate(sql);


	}
	
	/**
	 * Loads and returns a Monster object, containing any available information.
	 * 
	 * @param id The name of the monster
	 *
	 * @return Monster object 
	 */

	public Monster findMonster(String name){
		Monster monster = new Monster(); //what will be returned       
		String sql = "SELECT * FROM Monsters WHERE name = '" + name + "';";		
        connect();
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

			while(rs.next()){
				monster.setName(rs.getString("name"));
				monster.setUpperBound(rs.getString("upper"));
				monster.setLowerBound(rs.getString("lower"));
				monster.setDescription(rs.getString("description"));
			}

			conn.close();
			stmt.close();
			rs.close();
		}
        
        //catch any issues along the way
		catch (Exception e){
			System.out.println(e);
		}
		//close any/all connections
		finally{
			try{
				stmt.close();
				conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		return monster;
	}
	
	/**
	 * Delete a monster
	 * 
	 * @param name 
	 */
	public void deleteMonster(String name){

		String sql = "DELETE FROM Monsters WHERE name = '" + name + "';" ;
		dbUpdate(sql);
	}

	
    /**
	 * NOT FOR GENERAL USE. Completely deletes all Monster information from the database.
	 * 
	 */
	public void deleteAllMonsters(){
	
		String sql = "DELETE FROM Monsters;" ;
		dbUpdate(sql);
	}
	
	/**
	 * NOT FOR GENERAL USE. Issues a custom UPDATE command to the mysql DB. This must be a mutator command (INSERT, DROP, UPDATE...)
	 * Use at your own risk. TA's not liable for databases broken by use of this command.
	 * 
	 * @param command the string command you wish to execute.
	 */
	public void customUpdate(String command){
		System.out.println("Executing Custom Update Command: " + command);
        
        dbUpdate(command);
    }
	
/** 
	 * NOT FOR GENERAL USE. Master mutator method for db updates
	 */
    public void dbUpdate(String sql){
        connect();
        try {
            stmt = conn.createStatement();
			stmt.executeUpdate(sql);
        }
		//catch any issues along the way
		catch (Exception e){
			System.out.println(e);
		}
		//close any/all connections
		finally{
			try{
				stmt.close();
				conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
    }


	/**
	 * NOT FOR GENERAL USE. Issues a custom EXECUTE command to the mysql DB. This must be an accessor command (SELECT .. )
	 * Prints the returned results to the screen.
	 * Use at your own risk. TA's not liable for databases broken by use of this command.
	 * 
	 * @param command the string command you wish to execute.
	 */
    public void customExecute(String command){
		System.out.println("Executing Custom Execute Command: " + command);
        connect();
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(command);

			while (rs.next()){
				System.out.println(rs.toString());
			}
        }

        //catch any issues along the way
		catch (Exception e){
			System.out.println(e);
		}
		//close any/all connections
		finally{
			try{
				stmt.close();
				conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}