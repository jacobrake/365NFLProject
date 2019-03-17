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
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;



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
                fantasyTeamScene = GetFantasyTeamScene();
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
        Scene s = new Scene(pane, 700, 700);

        TableView playerTable = new TableView();

        TableColumn pidCol = new TableColumn("PID");
        pidCol.setCellValueFactory(
                new PropertyValueFactory<NFLPlayer, Integer>("Pid"));

        TableColumn nameCol = new TableColumn("Name");
        nameCol.setCellValueFactory(
                new PropertyValueFactory<NFLPlayer, String>("Name"));

        TableColumn teamCol = new TableColumn("Team");
        teamCol.setCellValueFactory(
                new PropertyValueFactory<NFLPlayer, Integer>("Tid"));

        TableColumn posCol = new TableColumn("Position");
        posCol.setCellValueFactory(
                new PropertyValueFactory<NFLPlayer, String>("Position"));

        TableColumn ypgCol = new TableColumn("YPG");
        ypgCol.setCellValueFactory(
                new PropertyValueFactory<NFLPlayer, Double>("YPG"));

        TableColumn tdCol = new TableColumn("TDs");
        tdCol.setCellValueFactory(
                new PropertyValueFactory<NFLPlayer, Integer>("TDS"));

        TableColumn intCol = new TableColumn("INTs");
        intCol.setCellValueFactory(
                new PropertyValueFactory<NFLPlayer, Integer>("INTS"));

        playerTable.getColumns().addAll(pidCol, nameCol, teamCol, posCol, ypgCol, tdCol);
        GridPane.setConstraints(playerTable, 0, 5);
        pane.getChildren().addAll(playerTable);
        return s;
    }

    public static Scene GetFantasyTeamScene(){
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
                    
                    pane.getChildren().addAll(playerName, addPlayer);
                    addPlayer.setText("Add Player to team " + clickedRow.Name);
                    System.out.println("here");
                    btn.setOnAction(new EventHandler<ActionEvent>() {
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
                                    }catch(Exception e1){
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
    
}
