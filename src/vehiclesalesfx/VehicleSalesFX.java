/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehiclesalesfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Ricky Thain
 */
public class VehicleSalesFX extends Application {
    
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        
        /*
        Button1 = new Button("Run Service");
        Button1.setStyle("-fx-font: 12 'MV Boli");
        Button1.disableProperty().bind(dashService.runningProperty());
        Button1.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent e) {
                dashService.restart();
            }
            
        });
        
        ProgressIndicator1 = new ProgressIndicator();
        ProgressIndicator1.visibleProperty().bind(dashService.runningProperty());
        
        VBox VBox1 = new VBox();
        VBox1.setPadding(new Insets(10, 10, 10, 10));
        VBox1.setSpacing(10);
        VBox1.setAlignment(Pos.CENTER);
        
        VBox1.getChildren().addAll(Button1, ProgressIndicator1);
        */
        System.out.println("inside vehicle sales fx.java");
        Parent parent = FXMLLoader.load(getClass().getResource("VehicleSalesFXML.fxml"));
        System.out.println("after loading statement of fxml file");
        Scene scene = new Scene(parent);
        scene.getStylesheets().add(VehicleSalesFX.class.getResource("Style.css").toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.setTitle("Vehicle Sales FX");
        primaryStage.show();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    
    
}
