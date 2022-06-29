package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import network.Network;

public class libera extends Application {
    static Network net;

    public static void send(int sended) {
        net.send(sended);
    }
    public static void send(String sended) {
        net.send(sended);
    }
    public static int waitForInt() {
        return net.waitForInt();
    }
    public static String waitForString() {
        return net.waitForString();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/libera-icon.png")));
        stage.setTitle("libera");
        stage.show();

        net = new Network();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
