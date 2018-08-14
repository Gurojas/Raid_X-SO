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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
 * @author Gustavo Rojas
 */
public class RaidSO extends Application {
    
    public final int gap = 10;
    public File selectedFile;
    
    public RaidGenerator raidGenerator;
    public FileOperations fileOperations;
   
    @Override
    public void start(Stage primaryStage) {
        
        
        this.raidGenerator = new RaidGenerator();
        this.fileOperations = new FileOperations();
        
     
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
                "Raid 0","Raid 1","Raid 3","Raid 4","Raid 5","Raid 6"
        );
        ComboBox<String> raidsComboBox = new ComboBox<>(raidOptions);
        raidsComboBox.setPrefWidth(200);
        raidsComboBox.setValue("Raid 0");
        
        Button generateRaidButton = new Button("Generate");
        generateRaidButton.setPrefWidth(100);
            
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
        
        gridPane.add(generateRaidButton, 0, 4);
        
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(this.gap));
        
        root.setCenter(gridPane);
        root.setTop(labelTitle);
        BorderPane.setAlignment(labelTitle, Pos.CENTER);
        BorderPane.setMargin(labelTitle, new Insets(this.gap));
        
        
        Scene scene = new Scene(root, 600, 300);
        
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
        
        generateRaidButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (radioSaveButton.isSelected()){
                    if (raidsComboBox.getSelectionModel().getSelectedItem().equals("Raid 0")){
                        RaidGenerator raidGenerator = new RaidGenerator();
                        FileOperations fileOperations = new FileOperations();
                        String fileContent = fileOperations.getFileContent(selectedFile);
                        boolean result = raidGenerator.raid0(fileContent, selectedFile.getName());
                        if (result){
                            showAlert("Raid 0", AlertType.INFORMATION);
                        }
                        else{
                            showAlert("Raid 0", AlertType.ERROR);
                        }
                    }
                    if (raidsComboBox.getSelectionModel().getSelectedItem().equals("Raid 1")){
                        RaidGenerator raidGenerator = new RaidGenerator();
                        FileOperations fileOperations = new FileOperations();
                        String fileContent = fileOperations.getFileContent(selectedFile);
                        boolean result = raidGenerator.raid1(fileContent, selectedFile.getName());
                        if (result){
                            showAlert("Raid 1", AlertType.INFORMATION);
                        }
                        else{
                            showAlert("Raid 1", AlertType.ERROR);
                        }
                    }
                    if (raidsComboBox.getSelectionModel().getSelectedItem().equals("Raid 3")){
                        RaidGenerator raidGenerator = new RaidGenerator();
                        FileOperations fileOperations = new FileOperations();
                        String fileContent = fileOperations.getFileContent(selectedFile);
                        boolean result = raidGenerator.raid3(fileContent, selectedFile.getName());
                        if (result){
                            showAlert("Raid 3", AlertType.INFORMATION);
                        }
                        else{
                            showAlert("Raid 3", AlertType.ERROR);
                        }
                    }
                    if (raidsComboBox.getSelectionModel().getSelectedItem().equals("Raid 4")){
                        RaidGenerator raidGenerator = new RaidGenerator();
                        FileOperations fileOperations = new FileOperations();
                        String fileContent = fileOperations.getFileContent(selectedFile);
                        boolean result = raidGenerator.raid4(fileContent, selectedFile.getName());
                        if (result){
                            showAlert("Raid 4", AlertType.INFORMATION);
                        }
                        else{
                            showAlert("Raid 5", AlertType.ERROR);
                        }
                    }
                    if (raidsComboBox.getSelectionModel().getSelectedItem().equals("Raid 5")){
                        RaidGenerator raidGenerator = new RaidGenerator();
                        FileOperations fileOperations = new FileOperations();
                        String fileContent = fileOperations.getFileContent(selectedFile);
                        boolean result = raidGenerator.raid5(fileContent, selectedFile.getName());
                        if (result){
                            showAlert("Raid 5", AlertType.INFORMATION);
                        }
                        else{
                            showAlert("Raid 5", AlertType.ERROR);
                        }
                    }
                    if (raidsComboBox.getSelectionModel().getSelectedItem().equals("Raid 6")){
                        RaidGenerator raidGenerator = new RaidGenerator();
                        FileOperations fileOperations = new FileOperations();
                        String fileContent = fileOperations.getFileContent(selectedFile);
                        boolean result = raidGenerator.raid6(fileContent, selectedFile.getName());
                        if (result){
                            showAlert("Raid 6", AlertType.INFORMATION);
                        }
                        else{
                            showAlert("Raid 6", AlertType.ERROR);
                        }
                    }
                      
                }

                if (radioReadButton.isSelected()){
                    RaidGenerator raidGenerator = new RaidGenerator();
                    
                    if (selectedFile != null){
                        if (fileOperations.isRaidFolder(selectedFile)){
                            String selectedFileFolder = selectedFile.getName();
                            if (selectedFileFolder.equals("RAID_0")){
                                String originalContent = raidGenerator.readRaid0(selectedFile.getAbsolutePath());
                                ContentView contentView = new ContentView(originalContent);
                                contentView.show();
                            }
                            else if (selectedFileFolder.equals("RAID_1")){
                                String originalContent = raidGenerator.readRaid1(selectedFile.getAbsolutePath());
                                ContentView contentView = new ContentView(originalContent);
                                contentView.show();
                            }
                            else if(selectedFileFolder.equals("RAID_3")){
                                String originalContent = raidGenerator.readRaid3(selectedFile.getAbsolutePath());
                                ContentView contentView = new ContentView(originalContent);
                                contentView.show();
                            }
                            else if(selectedFileFolder.equals("RAID_4")){
                                String originalContent = raidGenerator.readRaid4(selectedFile.getAbsolutePath());
                                ContentView contentView = new ContentView(originalContent);
                                contentView.show();
                            }
                            else if(selectedFileFolder.equals("RAID_5")){
                                String originalContent = raidGenerator.readRaid5(selectedFile.getAbsolutePath());
                                ContentView contentView = new ContentView(originalContent);
                                contentView.show();
                            }
                            else if(selectedFileFolder.equals("RAID_6")){
                                String originalContent = raidGenerator.readRaid6(selectedFile.getAbsolutePath());
                                ContentView contentView = new ContentView(originalContent);
                                contentView.show();
                            }
                        }
                        else{
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Ventana de error");
                            alert.setHeaderText("Carpeta con nombre incorrecto");
                            alert.setContentText("Nombre correcto: RAID_X");
                            alert.showAndWait();
                        }
                    }
                    
                    //String originalContent = raidGenerator.readRaid0(selectedFile.getAbsolutePath());
                    //ContentView contentView = new ContentView(originalContent);
                    //contentView.show();
                }
            }
        });
        

    }
    
    public void showAlert(String raid, AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle("Ventana de informacion");
        if (alertType == AlertType.INFORMATION){
            alert.setHeaderText(raid+" creado con exito !!");
        }
        else if (alertType == AlertType.ERROR){
            alert.setHeaderText(raid+" no pudo ser creado :(");
        }
        alert.setContentText("Puede cerrar esta ventana");
        alert.showAndWait();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        

        
    }
    
}
