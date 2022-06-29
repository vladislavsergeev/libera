package app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("LoginController -> initialize() ...");
    }

    @FXML
    private void doLogin() {
        System.out.println("LoginController -> doLogin() ...");

        if (loginField.getText().equals("") || passField.getText().equals("")) {
            err.setText("Пустое поле для логина или пароля");
        }
        else {
            try {
                app.libera.send(Const.LOGIN);
                app.libera.waitForInt();

                app.libera.send(loginField.getText());
                app.libera.waitForInt();

                app.libera.send(passField.getText());

                int res = app.libera.waitForInt();

                if (res == Const.LOGIN_DENIED) err.setText("Пользователя с таким именем не существует");
                else if (res == Const.WRONG_PASSWORD) err.setText("Неверное имя пользователя или пароль");
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
    private void doReg() {
        System.out.println("LoginController -> doReg() ...");

        if (loginField.getText().equals("") || passField.getText().equals("")) {
            err.setText("Пустое поле для логина или пароля");
        }
        else {
            try {
                app.libera.send(Const.REGISTRATION);
                app.libera.waitForInt();

                app.libera.send(loginField.getText());
                app.libera.waitForInt();

                app.libera.send(passField.getText());

                int res = app.libera.waitForInt();

                if (res == Const.LOGIN_DENIED) err.setText("Пользователь с таким именем уже существует");
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
