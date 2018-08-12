/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raidso;

import java.io.File;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 *
 * @author Gustavo
 */
public class RaidSO extends Application {
    
    public final int gap = 10;
    public File selectedFile;
   
    @Override
    public void start(Stage primaryStage) {
        
        RaidGenerator rg = new RaidGenerator();
        System.out.println(rg.charToBin('d'));
        

        Label labelTitle = new Label("Tarea RAID SO");
        labelTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        
        ToggleGroup toggleGroup = new ToggleGroup();
        
        RadioButton radioSaveButton = new RadioButton("Save");
        radioSaveButton.setSelected(true); // selected by default
        radioSaveButton.setToggleGroup(toggleGroup);
        
        RadioButton radioReadButton = new RadioButton("Read");
        radioReadButton.setToggleGroup(toggleGroup);

        Label saveLabel = new Label("Save file");
        Label readLabel = new Label("Read file");
        
        TextField saveField = new TextField();
        saveField.setEditable(false);
        saveField.setPrefWidth(400);
        
        TextField readField = new TextField();
        readField.setEditable(false);
        readField.setPrefWidth(400);
        
        Button browseSaveButton = new Button("Browse");
        browseSaveButton.setPrefWidth(100);
        
        Button browseReadButton = new Button("Browse");
        browseReadButton.setPrefWidth(100);
        
        Label raidsOptionLabel = new Label("Options: ");
        
        ObservableList<String> raidOptions = FXCollections.observableArrayList(
                "Raid 0","Raid 1","Raid 2","Raid 3","Raid 4","Raid 5","Raid 6"
        );
        ComboBox<String> raidsComboBox = new ComboBox<>(raidOptions);
        raidsComboBox.setPrefWidth(200);
        raidsComboBox.setValue("Raid 0");
        
        Button aceptarButton = new Button("Aceptar");
        aceptarButton.setPrefWidth(100);
            
        GridPane gridPane = new GridPane();
        gridPane.setVgap(this.gap);
        gridPane.setHgap(this.gap);
        gridPane.setGridLinesVisible(false);
        
        gridPane.add(radioSaveButton, 0, 0);
        gridPane.add(radioReadButton ,1, 0);
        
        gridPane.add(saveLabel, 0, 1);
        gridPane.add(saveField, 1, 1);
        gridPane.add(browseSaveButton, 2, 1);
        
        gridPane.add(readLabel,0,2);
        gridPane.add(readField,1,2);
        gridPane.add(browseReadButton,2,2);
        
        gridPane.add(raidsOptionLabel,0,3);
        gridPane.add(raidsComboBox,1,3);
        
        gridPane.add(aceptarButton, 0, 4);
        
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(this.gap));
        
        root.setCenter(gridPane);
        root.setTop(labelTitle);
        BorderPane.setAlignment(labelTitle, Pos.CENTER);
        BorderPane.setMargin(labelTitle, new Insets(this.gap));
        
        
        Scene scene = new Scene(root, 600, 400);
        
        primaryStage.setTitle(labelTitle.getText());
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // by default read options are disabled
        readField.setDisable(true);
        browseReadButton.setDisable(true);
        
        // Events methods
        
        radioSaveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (radioSaveButton.isSelected()){
                    // disable read options
                    readField.setDisable(true);
                    browseReadButton.setDisable(true);
                                      
                    saveField.setDisable(false);
                    browseSaveButton.setDisable(false);
                    raidsComboBox.setDisable(false);
                }
            }
        });
        
        radioReadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (radioReadButton.isSelected()){
                    saveField.setDisable(true);
                    browseSaveButton.setDisable(true);
                    raidsComboBox.setDisable(true);
                    
                    readField.setDisable(false);
                    browseReadButton.setDisable(false);
                }
            }
        });
        
        browseSaveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
                selectedFile = fileChooser.showOpenDialog(primaryStage);
                if (selectedFile != null){
                    saveField.setText(selectedFile.getAbsolutePath());
                }
            }
        });
        
        browseReadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Open Resource folder");
                selectedFile = directoryChooser.showDialog(primaryStage);
                if (selectedFile != null){
                    readField.setText(selectedFile.getAbsolutePath());
                }
            }
        });
        
        aceptarButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (radioSaveButton.isSelected()){
                    if (raidsComboBox.getSelectionModel().getSelectedItem().equals("Raid 0")){
                        RaidGenerator raidGenerator = new RaidGenerator();
                        FileOperations fileOperations = new FileOperations();
                        String fileContent = fileOperations.getFileContent(selectedFile);
                        raidGenerator.raid0(fileContent, selectedFile.getName());
                    }
                    if (raidsComboBox.getSelectionModel().getSelectedItem().equals("Raid 1")){
                        RaidGenerator raidGenerator = new RaidGenerator();
                        FileOperations fileOperations = new FileOperations();
                        String fileContent = fileOperations.getFileContent(selectedFile);
                        raidGenerator.raid1(fileContent, selectedFile.getName());
                    }
                    if (raidsComboBox.getSelectionModel().getSelectedItem().equals("Raid 2")){
                        
                    }
                    if (raidsComboBox.getSelectionModel().getSelectedItem().equals("Raid 3")){
                        RaidGenerator raidGenerator = new RaidGenerator();
                        FileOperations fileOperations = new FileOperations();
                        String fileContent = fileOperations.getFileContent(selectedFile);
                        raidGenerator.raid3(fileContent, selectedFile.getName());
                    }
                    if (raidsComboBox.getSelectionModel().getSelectedItem().equals("Raid 4")){
                        RaidGenerator raidGenerator = new RaidGenerator();
                        FileOperations fileOperations = new FileOperations();
                        String fileContent = fileOperations.getFileContent(selectedFile);
                        raidGenerator.raid4(fileContent, selectedFile.getName());
                    }
                    
                }

                if (radioReadButton.isSelected()){
                    RaidGenerator raidGenerator = new RaidGenerator();
                    String originaContent = raidGenerator.readRaid4(selectedFile.getAbsolutePath());
                    ContentView contentView = new ContentView(originaContent);
                    contentView.show();
                }
            }
        });
        

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        

        
    }
    
}
