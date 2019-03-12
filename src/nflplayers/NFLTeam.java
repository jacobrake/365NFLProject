/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nflplayers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author jaker
 */
public class NFLTeam {
    public int Tid;
    public String Name;
    public int Ties;
    public int Wins;
    public int Losses;
    public static NFLTeam GetById(Connection con, int id) throws SQLException{
        NFLTeam team = new NFLTeam();
        Statement stmt = null;

        try {
            PreparedStatement query = con.prepareStatement(
                "select t.tid, t.name, t.wins, t.losses, t.ties "
                        + "from group15.NFLTeam t WHERE t.tid = ?");
            query.setInt(1, id);
            ResultSet rs = (ResultSet) query.executeQuery();
            while (rs.next()) {
                team.Name = rs.getString("name");
                team.Tid = rs.getInt("tid");
                team.Ties = rs.getInt("ties");
                team.Wins = rs.getInt("wins");
                team.Losses = rs.getInt("losses");
            }
        } catch (SQLException e ) {
            System.err.println("SQLException information");
            while(e != null){
                System.err.println("Error message: " + e.getMessage());
                System.err.println("SQLSTATE: " + e.getSQLState());
                System.err.println("Error Code: " + e.getErrorCode());
                e.printStackTrace();
                e = e.getNextException();
            }
        } finally {
            if (stmt != null) { stmt.close(); }
        }
        return team;
    }
}
