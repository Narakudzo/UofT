import java.sql.*;

public class Assignment2 {
    
    // A connection to the database  
    Connection connection;
  
    // Statement to run queries
    Statement sql;
  
    // Prepared Statement
    PreparedStatement ps;
  
    // Result set for the query
    ResultSet rs;
  
    //CONSTRUCTOR
    Assignment2(){
    	// Load JDBC Driver
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
    }
  
    //Using the input parameters, establish a connection to be used for this session. Returns true if connection is successful
    public boolean connectDB(String URL, String username, String password){
		try {
			connection = DriverManager.getConnection(URL, username, password);
		} catch (SQLException e) {
			return false;
		}
        return true;
    }
  
    //Closes the connection. Returns true if closure was successful
    public boolean disconnectDB(){
		try {
			connection.close();
		} catch (SQLException e) {
			return false;
		}
        return true;
    }
    
    //Helper method to set schema.
    public boolean setSchema(String schema){
    	try {
    		sql = connection.createStatement();
			String sqlText;
			sqlText = "SET search_path TO " + schema;
			sql.executeUpdate(sqlText);
		} catch (SQLException e) {
			return false;
		}
    	return true;
    }
    
    public boolean insertTeam(int eid, String ename, int cid) {
    	int i = 0;
    	try {
			sql = connection.createStatement();
			String sqlText;
			sqlText = "INSERT INTO team VALUES(" + eid + ", '" + ename + "', " + cid + ")";
			i = sql.executeUpdate(sqlText);	
		} catch (SQLException e) {
			return false;
		}
    	if(i > 0) { return true; }
    	return false;
    }
  
    public int getChampions(int eid) {
    	int numChampion = 0;
    	try {
			sql = connection.createStatement();
			String sqlText;
			sqlText = "SELECT count(*) as count FROM champion WHERE mid = " + eid;
			rs = sql.executeQuery(sqlText);	
			while(rs.next()) {
				numChampion = rs.getInt("count");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return numChampion;
    }
   
    public String getRinkInfo(int rid){
    	String result = "";
    	try {
			sql = connection.createStatement();
			String sqlText;
			sqlText = "SELECT rinkid, rinkname, capacity, tname FROM rink r, tournament t " +
						" WHERE rinkid = " + rid + "AND r.tid = t.tid ";
			rs = sql.executeQuery(sqlText);	
			while(rs.next()) {
				result += rs.getInt("rinkid") + ":";
				result += rs.getString("rinkname") + ":";
				result += rs.getInt("capacity") + ":";
				result += rs.getString("tname");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return result;
    }

    public boolean chgRecord(int pid, int rank){
    	int i = 0;
    	try {
			String sqlText;
			sqlText = "UPDATE player SET globalrank = ? WHERE pid = ?";
			ps = connection.prepareStatement(sqlText);
			ps.setInt(1, rank);
			ps.setInt(2, pid);
			i = ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			return false;
		}
    	if(i > 0) { return true; }
    	return false;
    }

    public boolean deleteMatcBetween(int e1id, int e2id){
    	int i = 0;
    	try {
			String sqlText;
			sqlText = "DELETE FROM event WHERE year >= 2011 AND year <= 2015 AND winid = ? AND lossid = ?";
			ps = connection.prepareStatement(sqlText);
			ps.setInt(1, e1id);
			ps.setInt(2, e2id);
			i = ps.executeUpdate();
			ps.setInt(1, e2id);
			ps.setInt(2, e1id);
			i += ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			return false;
		}
    	if(i > 0) { return true; }
    	return false;
    }
  
    public String listPlayerRanking(){
    	String result = "";
    	try {
			sql = connection.createStatement();
			String sqlText;
			sqlText = "SELECT pid, pname, globalrank FROM player ORDER BY pid ASC";
			rs = sql.executeQuery(sqlText);	
			while(rs.next()) {
				result += rs.getString("pname") + ":";
				result += rs.getInt("globalrank") + "\n";
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return result;
    }
  
    public int findTriCircle(){
    	int result = 0;
    	try {
    		sql = connection.createStatement();
    		String sqlText;
    		sqlText = "SELECT count(*) / 3 as count FROM " +
    			"(SELECT e1.winid,e1.lossid,e2.winid,e2.lossid,e3.winid,e3.lossid FROM event e1, event e2, event e3 " +
    			"WHERE e1.winid = e3.lossid AND e1.lossid = e2.winid AND e2.lossid = e3.winid " +
    			"GROUP BY e1.winid,e1.lossid,e2.winid,e2.lossid,e3.winid,e3.lossid) as foo";
    		rs = sql.executeQuery(sqlText);	
			while(rs.next()) {
				result += rs.getInt("count");
			}
			rs.close();
    	}catch (SQLException e) {
			e.printStackTrace();
		}
    	return result;
    }
    
    public boolean updateDB(){
    	int i = 0;
    	try {
			sql = connection.createStatement();
			String sqlText;
			sqlText = "DROP TABLE IF EXISTS allTimeHomeWinners";
			sql.executeUpdate(sqlText);
			sqlText = "CREATE TABLE allTimeHomeWinners( pid INTEGER )";
			sql.executeUpdate(sqlText);
			sqlText = "CREATE VIEW nonWinnersList(tid) AS " +
					"(SELECT mid FROM champion ch, tournament t, team te " +
					"WHERE ch.tid = t.tid AND ch.mid = te.gid AND t.cid != te.cid)";
			sql.executeUpdate(sqlText);
			sqlText = "INSERT INTO allTimeHomeWinners " +
					"(SELECT pid FROM player WHERE tid NOT IN (SELECT tid FROM nonWinnersList))";
			i += sql.executeUpdate(sqlText);
			sqlText = "DROP VIEW nonWinnersList";
			sql.executeUpdate(sqlText);
		} catch (SQLException e) {
			return false;
		}
    	if(i > 0) { return true; }
    	return false;  
    }  
}

