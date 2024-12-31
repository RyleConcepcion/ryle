package com.newyear.storemanagement;

import Controllers.UserSessionController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage primaryStage; // To manage scenes globally

    private UserSessionController Datasource;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage; // Assign the primary stage
        setScene("/Fxml/login.fxml", "Store Management System", 1280, 800); // Load the initial scene
    }

    @Override
    public void init() throws Exception {
        super.init();
        if (!Model.Datasource.getInstance().open()) {
            System.out.println("FATAL ERROR: Couldn't connect to the database");
            Platform.exit();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Model.Datasource.getInstance().close();
    }

    public static void setScene(String fxmlPath, String title, int width, int height) {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource(fxmlPath));
            Scene scene = new Scene(root, width, height);
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
