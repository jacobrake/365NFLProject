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
public class NFLDefense {
    public int Tid;
    public double Pya;
    public double Rya;
    public int Sacks;
    public int Ints;
    public int Turnovers;

    public static NFLDefense GetById(Connection con, int id) throws SQLException{
        NFLDefense defense = new NFLDefense();
        Statement stmt = null;

        try {
            PreparedStatement query = con.prepareStatement(
                "select d.tid, d.pya, d.rya, d.sacks, d.ints, d.turnovers"
                        + "from group15.NFLDefense d WHERE d.pid = ?");
            query.setInt(1, id);
            ResultSet rs = (ResultSet) query.executeQuery();
            while (rs.next()) {
                defense.Tid = rs.getInt("tid");
                defense.Turnovers = rs.getInt("turnovers");
                defense.Pya = rs.getInt("sacks");
                defense.Ints = rs.getInt("ints");
                defense.Pya = rs.getDouble("pya");
                defense.Rya = rs.getDouble("rya");
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
        return defense;
    }
}
