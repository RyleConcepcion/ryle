package com.newyear.storemanagement;

import Controllers.UserSessionController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {

    private UserSessionController Datasource;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/login.fxml"));
        primaryStage.setTitle("Store Management System");
        //primaryStage.getIcons().add(new Image("/view/resources/img/brand/fav.png"));
        primaryStage.setScene(new Scene(root, 1280, 800));
        primaryStage.show();

    }

    @Override
    public void init() throws Exception {
        super.init();
        if(!Model.Datasource.getInstance().open()) {
            System.out.println("FATAL ERROR: Couldn't connect to database");
            Platform.exit();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Model.Datasource.getInstance().close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
