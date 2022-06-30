package app;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import utils.Const;

public class LoginController implements Initializable {
    @FXML
    private TextField loginField;

    @FXML
    private TextField passField;

    @FXML
    private Label err;

    private double xOffset = 0;

    private double yOffset = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("LoginController -> initialize() ...");
    }


    @FXML
    public void RootMousePressed(Event event) {
        MouseEvent e = (MouseEvent) event;
        xOffset = e.getSceneX();
        yOffset = e.getSceneY();
    }

    @FXML
    public void RootMouseDragged(Event event) {
        MouseEvent e = (MouseEvent) event;
        ((Stage) (((Node) (event.getSource())).getScene().getWindow())).setX(e.getScreenX() - xOffset);
        ((Stage) (((Node) (event.getSource())).getScene().getWindow())).setY(e.getScreenY() - yOffset);
    }
    @FXML
    private void doLogin() {
        System.out.println("LoginController -> doLogin() ...");

        if (loginField.getText().equals("") || passField.getText().equals("")) {
            err.setText("Заполнены не все поля для входа");
        }
        else {
            try {
                app.libera.send(Const.LOGIN);
                app.libera.waitForInt();

                app.libera.send(loginField.getText());
                app.libera.waitForInt();

                app.libera.send(passField.getText());

                int res = app.libera.waitForInt();

                if (res == Const.LOGIN_DENIED) err.setText("Такого аккаунта нет");
                else if (res == Const.WRONG_PASSWORD) err.setText("Неверный пароль");
                else {
                    Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));

                    Scene scene = new Scene(root);
                    ((Stage) (loginField.getScene().getWindow())).setScene(scene);
                }
            } catch (IOException e) {

            }
        }
    }

    @FXML
    private void closeWindow(Event event) {
        System.exit(0);
    }
    @FXML
    private void doReg() {
        System.out.println("LoginController -> doReg() ...");

        if (loginField.getText().equals("") || passField.getText().equals("")) {
            err.setText("Заполнены не все поля для входа");
        }
        else {
            try {
                app.libera.send(Const.REGISTRATION);
                app.libera.waitForInt();

                app.libera.send(loginField.getText());
                app.libera.waitForInt();

                app.libera.send(passField.getText());

                int res = app.libera.waitForInt();

                if (res == Const.LOGIN_DENIED) err.setText("Логин уже используется");
                else {
                    Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));

                    Scene scene = new Scene(root);
                    ((Stage) (loginField.getScene().getWindow())).setScene(scene);
                }
            }
            catch (IOException e) {

            }
        }
    }
}
