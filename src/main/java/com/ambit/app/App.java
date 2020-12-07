package com.ambit.app;

import com.ambit.app.utils.JMetroThemeManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class App extends Application {

    private Stage appStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/view/MainWindow.fxml"));
        AnchorPane root = fxmlLoader.load();
        Scene scene = new Scene(root);
        // 设置主题
//        JMetroThemeManager.setSceneStyle(scene);
        primaryStage.setScene(scene);
        primaryStage.show();
        appStage = primaryStage;
    }

    @Override
    public void stop(){
        appStage.close();
        System.exit(0);
    }
}
