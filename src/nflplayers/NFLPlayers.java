/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nflplayers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.sql.*;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;



/**
 *
 * @author jaker
 */
public class NFLPlayers extends Application {

    static Connection connect;
    
    @Override
    public void start(Stage primaryStage) {
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://ambari-head.csc.calpoly.edu/group15","group15", "group15");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Button btn = new Button();
        btn.setText("New Fantasy Team");
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                Scene fantasyTeamScene;
                fantasyTeamScene = GetFantasyTeamScene(primaryStage);
                primaryStage.setScene(fantasyTeamScene);
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 700, 700);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();

        Button viewPlayersBtn = new Button("View Players");
        viewPlayersBtn.setTranslateY(40);
        viewPlayersBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Scene NFLPlayerScene;
                NFLPlayerScene = GetNFLPlayerScene();
                primaryStage.setScene(NFLPlayerScene);
            }
        });

        root.getChildren().add(viewPlayersBtn);

    }
    public static void main(String[] args) {
        launch(args);
    }

    public static Scene GetNFLPlayerScene() {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(100, 100, 100, 100));
        Scene s = new Scene(pane, 900, 700);
        ArrayList<String> cols = new ArrayList<String>(){
            {
                add("Pid");
                add("Name");
                add("TeamName");
                add("Position");
                add("YPG");
                add("TDS");
                add("INTS");
            }
        };
        ArrayList<String> colnames = new ArrayList<String>(){
            {
                add("Pid");
                add("Name");
                add("Team");
                add("Pos");
                add("YPG");
                add("TDs");
                add("INTs");
            }
        };
        TableView playerTable = GetPlayerTable("%%", cols, colnames);
        pane.getChildren().addAll(playerTable);
        return s;
    }
    public static TableView GetPlayerTable(String pos, ArrayList<String> cols, ArrayList<String> colnames){
        TableView playerTable = new TableView();
        for(int i = 0; i < cols.size(); i++){
            TableColumn col = new TableColumn(colnames.get(i));
            col.setCellValueFactory(
                new PropertyValueFactory<NFLPlayer, Integer>(cols.get(i)));
            playerTable.getColumns().add(col);
        }
        Alert a = new Alert(AlertType.ERROR);
        ObservableList<NFLPlayer> playerData = null;
        try {
            playerData = NFLPlayer.ListAll(connect, pos);
        } catch(Exception e) {
            a.setContentText("Error loading players");
            System.err.println("Error loading players");
            a.show();
        }

        playerTable.setItems(playerData);
        return playerTable;
    }

    public static Scene GetFantasyTeamScene(Stage primaryStage){
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(100, 100, 100, 100));
        Scene s = new Scene(pane, 700, 700);
        
        final TextField name = new TextField();
        name.setPromptText("Enter your team name.");
        GridPane.setConstraints(name, 0, 1);
        pane.getChildren().add(name);
        
        final TextField owner = new TextField();
        owner.setPromptText("Enter team owner name.");
        GridPane.setConstraints(owner, 0, 2);
        pane.getChildren().add(owner);
        
        Button btn = new Button();
        btn.setText("Add Fantasy Team");
        Alert a = new Alert(AlertType.ERROR);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FantasyTeam team = new FantasyTeam();
                team.Name = name.getText();
                team.Owner = owner.getText();
                team.DefenseTid = 0;
                try{
                    team.CreateFantasyTeam(connect);
                }catch(Exception e){
                    a.setContentText("Error adding fantasy team");
                    System.err.println("Error adding fantasy team");
                    a.show();
                }
                a.setAlertType(AlertType.CONFIRMATION);
                a.setHeaderText("Success");
                a.setContentText("Successfully added team " + team.Name);
                a.show();
                owner.clear();
                name.clear();
            }
        });
        GridPane.setConstraints(btn, 1, 2);
        pane.getChildren().add(btn);
        
        final Label label = new Label("Fantasy Teams");
        label.setFont(new Font("Arial", 20));
        GridPane.setConstraints(label, 0, 4);
        
        TableView table = new TableView();
        table.setEditable(false);
        TableColumn teamNameCol = new TableColumn("Team Name");
        teamNameCol.setMinWidth(100);
        teamNameCol.setCellValueFactory(
                new PropertyValueFactory<FantasyTeam, String>("Name"));

        TableColumn ownerCol = new TableColumn("Owner Name");
        ownerCol.setMinWidth(100);
        ownerCol.setCellValueFactory(
                new PropertyValueFactory<FantasyTeam, String>("Owner"));
        ObservableList<FantasyTeam> ftData = null;
        try{
            ftData = FantasyTeam.ListAll(connect);
        }catch(Exception e){
            a.setContentText("Error loading fantasy teams");
            System.err.println("Error loading fantasy teams");
            a.show();
        }
        table.setRowFactory(tv -> {
        TableRow<FantasyTeam> row = new TableRow<>();
        final TextField playerName = new TextField();
        playerName.setPromptText("Search for player to add");
        GridPane.setConstraints(playerName, 1, 5);
        row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY 
                    && event.getClickCount() == 1) {

                    FantasyTeam clickedRow = row.getItem();
                    Button addPlayer = new Button();
                    GridPane.setConstraints(addPlayer, 2, 5);
                    Button comparePlayers = new Button();
                    GridPane.setConstraints(comparePlayers, 2, 6);
                    comparePlayers.setText("Compare players to add");
                    comparePlayers.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                             primaryStage.setScene(GetComparePlayersScene(clickedRow));
                        }
                    });
                    pane.getChildren().removeAll(addPlayer, playerName, comparePlayers);
                    pane.getChildren().addAll(playerName, addPlayer, comparePlayers);
                    addPlayer.setText("Add Player to team " + clickedRow.Name);
                    System.out.println("here");
                    addPlayer.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            try{
                                System.out.println("here2");
                                NFLPlayer player = NFLPlayer.GetByName(connect, playerName.getText());
                                if(player.Pid == 0){
                                    a.setHeaderText("Error");
                                    a.setContentText("Player Not Found");
                                    a.show();
                                }else{
                                    try{
                                        clickedRow.AddPlayer(connect, player.Pid);
                                        a.setAlertType(AlertType.CONFIRMATION);
                                        a.setContentText("Successfully added " + player.Name + " to team " + clickedRow.Name);
                                        a.show();
                                    }catch(Exception e1){
                                        a.setAlertType(AlertType.ERROR);
                                        a.setContentText("Error adding player");
                                        System.err.println("Error adding player");
                                        a.show();
                                    }
                                }
                            }catch(Exception e){
                                a.setContentText("Error searching database for player");
                                System.err.println("Error searching database for player");
                                a.show();
                            }
                            playerName.clear();
                        }
                    });
                }
            });
            return row ;
        });
        table.setItems(ftData);
        table.getColumns().addAll(teamNameCol, ownerCol);
        GridPane.setConstraints(table, 0, 5);
        pane.getChildren().addAll(label, table);
        
        return s;
    }
    
    public static Scene GetComparePlayersScene(FantasyTeam team){
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(100,10,100,10));
        Scene s = new Scene(pane, 700, 700);
        Alert a = new Alert(AlertType.ERROR);
        ObservableList<String> options = FXCollections.emptyObservableList();
        try{
            options = NFLPlayer.ListPositions(connect);
        }catch(Exception e){
            a.setAlertType(AlertType.ERROR);
            a.setContentText("Error getting positions");
            System.err.println("Error getting positions");
            a.show();
        }
        final ComboBox comboBox1 = new ComboBox(options);
        //final ComboBox comboBox2 = new ComboBox(options);

        ArrayList<String> cols = new ArrayList<String>(){
            {
                add("Pid");
                add("Name");
                add("TeamName");
                add("Position");
            }
        };
        ArrayList<String> colnames = new ArrayList<String>(){
            {
                add("Pid");
                add("Name");
                add("Team");
                add("Pos");
            }
        };
        final TableView playerTable1 = GetPlayerTable("%%", cols, colnames);
        final TableView playerTable2 = GetPlayerTable("%%", cols, colnames);
        GridPane.setConstraints(comboBox1, 1, 0);
        //GridPane.setConstraints(comboBox2, 2, 0);
        GridPane.setConstraints(playerTable1, 1, 1);
        GridPane.setConstraints(playerTable2, 2, 1);
        pane.setHgap(10);
        pane.setVgap(2);
        playerTable1.setRowFactory(tv -> {
            TableRow<NFLPlayer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY 
                    && event.getClickCount() == 1) {

                    NFLPlayer clickedRow = row.getItem();
                    Button addPlayer = new Button();
                    Text info = new Text();
                    String fullPlayer = "PID: " + clickedRow.Pid + "\n" +
                            "Name: " + clickedRow.Name + "\n" +
                            "Team: " + clickedRow.TeamName + "\n" +
                            "Position: " + clickedRow.Position + "\n" +
                            "Height: " + clickedRow.Height + "\n" +
                            "Weight: " + clickedRow.Weight + "\n" +
                            "Speed: " + clickedRow.Speed + "\n" +
                            "TDs: " + clickedRow.TDS + "\n" +
                            "INTs: " + clickedRow.INTS + "\n" +
                            "YPG: " + clickedRow.YPG + "\n";
                    info.setText(fullPlayer);
                    GridPane.setConstraints(info, 1, 3);
                    
                    GridPane.setConstraints(addPlayer, 1, 4);
                    pane.getChildren().removeAll(addPlayer, info);
                    pane.getChildren().addAll(addPlayer, info);
                    addPlayer.setText("Add Player to team " + clickedRow.Name);
                    addPlayer.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            try{
                                team.AddPlayer(connect, clickedRow.Pid);
                                a.setAlertType(AlertType.CONFIRMATION);
                                a.setContentText("Successfully added " + clickedRow.Name + " to team " + team.Name);
                                a.show();
                            }catch(Exception e1){
                                a.setAlertType(AlertType.ERROR);
                                a.setContentText("Error adding player");
                                System.err.println("Error adding player");
                                a.show();
                            }
                        }
                    });
                }
            });
            return row;
        });
        playerTable2.setRowFactory(tv -> {
            TableRow<NFLPlayer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY 
                    && event.getClickCount() == 1) {

                    NFLPlayer clickedRow = row.getItem();
                    Button addPlayer2 = new Button();
                    Text info2 = new Text();
                    String fullPlayer = "PID: " + clickedRow.Pid + "\n" +
                            "PID: " + clickedRow.Name + "\n" +
                            "Team: " + clickedRow.TeamName + "\n" +
                            "Position: " + clickedRow.Position + "\n" +
                            "Height: " + clickedRow.Height + "\n" +
                            "Weight: " + clickedRow.Weight + "\n" +
                            "Speed: " + clickedRow.Speed + "\n" +
                            "TDs: " + clickedRow.TDS + "\n" +
                            "INTs: " + clickedRow.INTS + "\n" +
                            "YPG: " + clickedRow.YPG + "\n";
                    info2.setText(fullPlayer);
                    GridPane.setConstraints(info2, 2, 3);
                    
                    GridPane.setConstraints(addPlayer2, 2, 4);
                    pane.getChildren().removeAll(addPlayer2, info2);
                    pane.getChildren().addAll(addPlayer2, info2);
                    addPlayer2.setText("Add Player to team " + clickedRow.Name);
                    addPlayer2.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            try{
                                team.AddPlayer(connect, clickedRow.Pid);
                                a.setAlertType(AlertType.CONFIRMATION);
                                a.setContentText("Successfully added " + clickedRow.Name + " to team " + team.Name);
                                a.show();
                            }catch(Exception e1){
                                a.setAlertType(AlertType.ERROR);
                                a.setContentText("Error adding player");
                                System.err.println("Error adding player");
                                a.show();
                            }
                        }
                    });
                }
            });
            return row;
        });
        comboBox1.valueProperty().addListener(new ChangeListener<String>() {
            @Override 
            public void changed(ObservableValue ov, String t, String t1) {
                String pos1 = "none";                
                pos1 = t1;
                try {
                    ObservableList<NFLPlayer> playerData = NFLPlayer.ListAll(connect, pos1);
                    playerTable1.setItems(playerData);
                    playerTable2.setItems(playerData);
                } catch(Exception e) {
                    a.setContentText("Error loading players");
                    System.err.println("Error loading players");
                    a.show();
                }       
                
            }    
        });
        //comboBox2.valueProperty().addListener(new ChangeListener<String>() {
        //    @Override 
        //    public void changed(ObservableValue ov, String t, String t1) {
        //        String pos2 = "none";                
        //        pos2 = t1;
        //        try {
        //            ObservableList<NFLPlayer> playerData = NFLPlayer.ListAll(connect, pos2);
        //            playerTable2.setItems(playerData);
        //        } catch(Exception e) {
        //           a.setContentText("Error loading players");
        //            System.err.println("Error loading players");
        //            a.show();
        //        }                
        //    }    
        //});
        pane.getChildren().addAll(playerTable1, playerTable2, comboBox1);//, comboBox2);
        
        return s;
    }
    
}
