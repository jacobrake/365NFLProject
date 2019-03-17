/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nflplayers;

import java.sql.*;

/**
 *
 * @author jaker
 */
public class NFLPlayer {
    int Pid;
    String Name;
    int Tid;
    String Position;
    double YPG;
    int TDS;
    int INTS;
    double Height;
    double Weight;
    double Speed;
    
    public static NFLPlayer GetById(Connection con, int id) throws SQLException{
        NFLPlayer player = new NFLPlayer();
        Statement stmt = null;

        try {
            PreparedStatement query = con.prepareStatement(
                "select p.pid, p.tid, p.name, " +
                "p.pos, p.ypg, p.td, p.int, p.heightIn, p.weightLb, p.speed "
                        + "from group15.NFLPlayer p WHERE p.pid = ?");
            query.setInt(1, id);
            ResultSet rs = (ResultSet) query.executeQuery();
            while (rs.next()) {
                player.Pid = rs.getInt("pid");
                player.Name = rs.getString("name");
                player.Tid = rs.getInt("tid");
                player.TDS = rs.getInt("td");
                player.INTS = rs.getInt("int");
                player.YPG = rs.getDouble("ypg");
                player.Height = rs.getDouble("heightIn");
                player.Weight = rs.getDouble("weightLb");
                player.Speed = rs.getDouble("speed");
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
        return player;
    }
    
        public static NFLPlayer GetByName(Connection con, String name) throws SQLException{
        NFLPlayer player = new NFLPlayer();
        Statement stmt = null;

        try {
            PreparedStatement query = con.prepareStatement(
                "select p.pid, p.tid, p.name, " +
                "p.pos, p.ypg, p.td, p.int, p.heightIn, p.weightLb, p.speed "
                        + "from group15.NFLPlayer p WHERE p.name = ?");
            query.setString(1, name);
            ResultSet rs = (ResultSet) query.executeQuery();
            while (rs.next()) {
                player.Pid = rs.getInt("pid");
                player.Name = rs.getString("name");
                player.Tid = rs.getInt("tid");
                player.TDS = rs.getInt("td");
                player.INTS = rs.getInt("int");
                player.YPG = rs.getDouble("ypg");
                player.Height = rs.getDouble("heightIn");
                player.Weight = rs.getDouble("weightLb");
                player.Speed = rs.getDouble("speed");
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
        return player;
    }

}