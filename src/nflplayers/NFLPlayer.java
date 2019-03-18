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
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author jaker
 */
public class NFLPlayer {
    int Pid;
    String Name;
    int Tid;
    String TeamName;
    String Position;
    double YPG;
    int TDS;
    int INTS;
    double Height;
    double Weight;
    double Speed;

    public Integer getPid() {
        return Pid;
    }
    public String getName() {
        return this.Name;
    }
    public Integer getTid() {
        return this.Tid;
    }
    public String getPosition() {
        return this.Position;
    }
    public Double getYPG() {
        return this.YPG;
    }
    public Integer getTDS() {
        return this.TDS;
    }
    public Integer getINTS() {
        return this.INTS;
    }
    public String getTeamName(){
        return this.TeamName;
    }
    
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
                PreparedStatement query2 = con.prepareStatement(
                        "select t.name from group15.NFLTeam t where t.tid = ?");
                query2.setInt(1, player.Tid);
                ResultSet rs2 = (ResultSet) query2.executeQuery();
                while (rs2.next()) {
                    player.TeamName = rs2.getString("name");
                }
                        
                player.Position = rs.getString("pos");
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
                PreparedStatement query2 = con.prepareStatement(
                        "select t.name from group15.NFLTeam t where t.tid = ?");
                query2.setInt(1, player.Tid);
                ResultSet rs2 = (ResultSet) query2.executeQuery();
                while (rs2.next()) {
                    player.TeamName = rs2.getString("name");
                }
                player.Position = rs.getString("pos");
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
    
        public static ObservableList<NFLPlayer> ListAll(Connection con) throws SQLException{

            ObservableList<NFLPlayer> players = FXCollections.observableArrayList();
            PreparedStatement query = null;

            try {
                query = con.prepareStatement(
                    "select p.pid, p.tid, p.name, " +
                    "p.pos, p.ypg, p.td, p.int, p.heightIn, p.weightLb, p.speed "
                            + "from group15.NFLPlayer p");
                ResultSet rs = (ResultSet) query.executeQuery();
                while (rs.next()) {
                    NFLPlayer player = new NFLPlayer();
                    player.Pid = rs.getInt("pid");
                    player.Name = rs.getString("name");
                    player.Tid = rs.getInt("tid");
                    PreparedStatement query2 = con.prepareStatement(
                            "select t.name from group15.NFLTeam t where t.tid = ?");
                    query2.setInt(1, player.Tid);
                    ResultSet rs2 = (ResultSet) query2.executeQuery();
                    while (rs2.next()) {
                        player.TeamName = rs2.getString("name");
                    }
                    player.Position = rs.getString("pos");
                    player.TDS = rs.getInt("td");
                    player.INTS = rs.getInt("int");
                    player.YPG = rs.getDouble("ypg");
                    player.Height = rs.getDouble("heightIn");
                    player.Weight = rs.getDouble("weightLb");
                    player.Speed = rs.getDouble("speed");
                    System.out.println(player.Pid + " " + player.Name + " " + player.Tid);
                    players.add(player);
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
                if (query != null) { query.close(); }
            }
            System.out.println(players.size());
            return players;
        }
}
