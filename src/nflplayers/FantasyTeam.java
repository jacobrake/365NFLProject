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
public class FantasyTeam {
    public int DefenseTid;
    public String Name;
    public int FTid;
    public String Owner;
    public ArrayList<NFLPlayer> Players;
    
    public void CreateFantasyTeam(Connection con) throws SQLException{
        PreparedStatement createStatement = null;
        try{
            con.setAutoCommit(false);
            createStatement = con.prepareStatement(
                    "insert into group15.FantasyTeam (name, owner, defenseTid)"
                            + "values (?, ?, ?)"
            );
            createStatement.setString(1, Name);
            createStatement.setString(2, Owner);
            createStatement.setInt(3, DefenseTid);
            createStatement.execute();
            con.commit();
        }catch(SQLException e ) {
            System.err.println("SQLException information");
            while(e != null){
                System.err.println("Error message: " + e.getMessage());
                System.err.println("SQLSTATE: " + e.getSQLState());
                System.err.println("Error Code: " + e.getErrorCode());
                e.printStackTrace();
                e = e.getNextException();
            }
            if (con != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    con.rollback();
                } catch(SQLException excep) {
                    System.err.println("SQLException information");
                    while(excep != null){
                        System.err.println("Error message: " + excep.getMessage());
                        System.err.println("SQLSTATE: " + excep.getSQLState());
                        System.err.println("Error Code: " + excep.getErrorCode());
                        excep.printStackTrace();
                        excep = excep.getNextException();
                    }
                }
            }
        } finally {
            if (createStatement != null) { createStatement.close(); }
            con.setAutoCommit(true);
        }
    }
    public void AddPlayer(Connection con, int pid) throws SQLException{
        PreparedStatement createStatement = null;
        if(Players == null){
            Players = new ArrayList<NFLPlayer>();
        }
        try{
            NFLPlayer player = NFLPlayer.GetById(con, pid);
            Players.add(player);
            con.setAutoCommit(false);
            createStatement = con.prepareStatement(
                    "insert into group15.FantasyTeamNFLPlayer (pid, ftid)"
                            + "values(?,?)"
            );
            createStatement.setInt(1, pid);
            createStatement.setInt(2, FTid);
            createStatement.execute();
            con.commit();
        }catch (SQLException e ) {
            System.err.println("SQLException information");
            while(e != null){
                System.err.println("Error message: " + e.getMessage());
                System.err.println("SQLSTATE: " + e.getSQLState());
                System.err.println("Error Code: " + e.getErrorCode());
                e.printStackTrace();
                e = e.getNextException();
            }
            if (con != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    con.rollback();
                } catch(SQLException excep) {
                    System.err.println("SQLException information");
                    while(excep != null){
                        System.err.println("Error message: " + excep.getMessage());
                        System.err.println("SQLSTATE: " + excep.getSQLState());
                        System.err.println("Error Code: " + excep.getErrorCode());
                        excep.printStackTrace();
                        excep = excep.getNextException();
                    }
                    throw excep;
                }
            }
        } finally {
            if (createStatement != null) { createStatement.close(); }
            con.setAutoCommit(true);
        }
        
    }
    
    public static FantasyTeam GetById(Connection con, int id) throws SQLException{
        FantasyTeam team = new FantasyTeam();
        PreparedStatement query = null;
        PreparedStatement query2 = null;

        try {
            query = con.prepareStatement(
                "select ft.ftid, ft.name, ft.owner, ft.defenseTid "
                        + "from group15.FantasyTeam ft WHERE ft.ftid = ?");
            query.setInt(1, id);
            ResultSet rs = (ResultSet) query.executeQuery();
            while (rs.next()) {
                team.Name = rs.getString("name");
                team.FTid = rs.getInt("ftid");
                team.Owner = rs.getString("owner");
                team.DefenseTid = rs.getInt("DefenseTid");
            }
            query2 = con.prepareStatement(
                "select p.pid, p.tid, p.name, " +
                "p.pos, p.ypg, p.td, p.int, p.heightIn, p.weightLb, p.speed "
                        + "from group15.NFLPlayer p left join "
                        + "group15.FantasyTeamNFLPlayer fttp on fttp.pid = p.pid"
                        + " where fttp.ftid = ?");
            query2.setInt(1, id);
            ResultSet rs2 = (ResultSet) query2.executeQuery();
            while (rs2.next()) {
                NFLPlayer player = new NFLPlayer();
                player.Pid = rs.getInt("pid");
                player.Name = rs.getString("name");
                player.Tid = rs.getInt("tid");
                player.TDS = rs.getInt("td");
                player.INTS = rs.getInt("int");
                player.YPG = rs.getDouble("ypg");
                player.Height = rs.getDouble("heightIn");
                player.Weight = rs.getDouble("weightLb");
                player.Speed = rs.getDouble("speed");
                team.Players.add(player);
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
            if (query2 != null) { query2.close(); }
        }
        return team;
    }
    
    public static FantasyTeam GetByName(Connection con, String name) throws SQLException{
        FantasyTeam team = new FantasyTeam();
        PreparedStatement query = null;
        PreparedStatement query2 = null;

        try {
            query = con.prepareStatement(
                "select ft.ftid, ft.name, ft.owner, ft.defenseTid "
                        + "from group15.FantasyTeam ft WHERE ft.name = ?");
            query.setString(1, name);
            ResultSet rs = (ResultSet) query.executeQuery();
            while (rs.next()) {
                team.Name = rs.getString("name");
                team.FTid = rs.getInt("ftid");
                team.Owner = rs.getString("owner");
                team.DefenseTid = rs.getInt("DefenseTid");
            }
            query2 = con.prepareStatement(
                "select p.pid, p.tid, p.name, " +
                "p.pos, p.ypg, p.td, p.int, p.heightIn, p.weightLb, p.speed "
                        + "from group15.NFLPlayer p left join "
                        + "group15.FantasyTeamNFLPlayer fttp on fttp.pid = p.pid"
                        + " where fttp.ftid = ?");
            query2.setInt(1, team.FTid);
            ResultSet rs2 = (ResultSet) query2.executeQuery();
            while (rs2.next()) {
                NFLPlayer player = new NFLPlayer();
                player.Pid = rs.getInt("pid");
                player.Name = rs.getString("name");
                player.Tid = rs.getInt("tid");
                player.TDS = rs.getInt("td");
                player.INTS = rs.getInt("int");
                player.YPG = rs.getDouble("ypg");
                player.Height = rs.getDouble("heightIn");
                player.Weight = rs.getDouble("weightLb");
                player.Speed = rs.getDouble("speed");
                team.Players.add(player);
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
            if (query2 != null) { query2.close(); }
        }
        return team;
    }
    public static ObservableList<FantasyTeam> ListAll(Connection con) throws SQLException{
        ObservableList<FantasyTeam> teams = FXCollections.observableArrayList();
        PreparedStatement query = null;
        PreparedStatement query2 = null;

        try {
            query = con.prepareStatement(
                "select ft.ftid, ft.name, ft.owner, ft.defenseTid "
                        + "from group15.FantasyTeam ft");
            ResultSet rs = (ResultSet) query.executeQuery();
            while (rs.next()) {
                FantasyTeam team = new FantasyTeam();
                team.Name = rs.getString("name");
                team.FTid = rs.getInt("ftid");
                team.Owner = rs.getString("owner");
                team.DefenseTid = rs.getInt("DefenseTid");
                query2 = con.prepareStatement(
                    "select p.pid, p.tid, p.name, " +
                    "p.pos, p.ypg, p.td, p.int, p.heightIn, p.weightLb, p.speed "
                            + "from group15.NFLPlayer p left join "
                            + "group15.FantasyTeamNFLPlayer fttp on fttp.pid = p.pid"
                            + " where fttp.ftid = ?");
                query2.setInt(1, team.FTid);
                ResultSet rs2 = (ResultSet) query2.executeQuery();
                if(team.Players == null){
                    team.Players = new ArrayList<NFLPlayer>();
                }
                while (rs2.next()) {
                    NFLPlayer player = new NFLPlayer();
                    player.Pid = rs2.getInt("pid");
                    player.Name = rs2.getString("name");
                    player.Tid = rs2.getInt("tid");
                    player.TDS = rs2.getInt("td");
                    player.INTS = rs2.getInt("int");
                    player.YPG = rs2.getDouble("ypg");
                    player.Height = rs2.getDouble("heightIn");
                    player.Weight = rs2.getDouble("weightLb");
                    player.Speed = rs2.getDouble("speed");
                    team.Players.add(player);
                }
                teams.add(team);
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
            if (query2 != null) { query2.close(); }
        }
        System.out.println(teams.size());
        return teams;
    }
    public String getName(){
        return Name;
    }
    public String getOwner(){
        return Owner;
    }
}
