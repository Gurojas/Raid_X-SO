/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raidso;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Gustavo Rojas
 */
public class ContentView extends Stage{
    
    private final int gap = 10;
    
    public ContentView(String originalContent){
        
        BorderPane mainPane = new BorderPane();
        mainPane.setPadding(new Insets(this.gap));
        
        GridPane grid = new GridPane();
        grid.setGridLinesVisible(false);
        grid.setHgap(this.gap);
        grid.setVgap(this.gap);
        
        Label labelTitle = new Label("Contenido original");
        labelTitle.setFont(Font.font("Verdana",14));
        
        TextArea textAreaContent = new TextArea();
        textAreaContent.setText(originalContent);
        textAreaContent.setEditable(false);
        
        Button closeButton = new Button("Close");
        closeButton.setPrefWidth(100);
        
        grid.add(labelTitle,0,0);
        grid.add(textAreaContent,0,1);
        grid.add(closeButton,0,2);
        
        GridPane.setHalignment(closeButton, HPos.RIGHT);
        
        mainPane.setCenter(grid);
        
        Scene scene = new Scene(mainPane,400,300);
        
        this.setTitle(labelTitle.getText());
        this.setResizable(false);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setScene(scene);
        
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ContentView.this.close();
            }
        });
        
        
        
        
    }
    
    
    
}
