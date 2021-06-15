package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public void listAllPlayers(Map<Integer, Player>idMap ){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				idMap.put(player.getPlayerID(), player);
			}
			conn.close();
		
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Player> getVertici(double x, Map<Integer, Player> idMap) {
		
		String sql = "SELECT a.PlayerID as pID, AVG(a.Goals) AS media "
				+ "FROM actions a, matches m "
				+ "WHERE a.MatchID = m.MatchID "
				+ "GROUP BY a.PlayerID "
				+ "HAVING media > ?";
		List<Player> result = new ArrayList<Player>();
		
		Connection conn = DBConnect.getConnection();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, x);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				Player pTemp = idMap.get(res.getInt("pID"));
				result.add(pTemp);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Adiacenza> getArchi(Map<Integer, Player> idMap) {
		
		String sql = "SELECT a1.PlayerID as pID1, a2.PlayerID as pID2, (SUM(A1.TimePlayed) - SUM(A2.TimePlayed)) AS peso "
				+ "FROM actions a1, actions a2 "
				+ "WHERE a1.MatchID=a2.MatchID "
				+ "AND a1.`Starts`=a2.`Starts` "
				+ "AND a1.`Starts`=1 "
				+ "AND a1.PlayerID > a2.PlayerID "
				+ "AND a1.TeamID != a2.TeamID "
				+ "GROUP BY a1.PlayerID, a2.PlayerID "
				+ "HAVING peso!=0";
		
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		

		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				if(idMap.containsKey(res.getInt("pID1")) && idMap.containsKey(res.getInt("pID2"))) {
					int peso = res.getInt("peso");
					if (peso>0) {
						Player p1 = idMap.get(res.getInt("pID1"));
						Player p2 = idMap.get(res.getInt("pID2"));
						Adiacenza atemp = new Adiacenza(p1, p2, peso);
						
						result.add(atemp);
					} else if( peso<0) {
						Player p1 = idMap.get(res.getInt("pID2"));
						Player p2 = idMap.get(res.getInt("pID1"));
						Adiacenza atemp = new Adiacenza(p1, p2, (-1)*peso);
						result.add(atemp);
					}
				}
			
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
