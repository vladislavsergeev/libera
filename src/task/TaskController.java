package task;

import com.jfoenix.controls.JFXTextArea;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import utils.Const;

public class TaskController implements Initializable {

    public static JFXTextArea textStatic;
    @FXML
    private JFXTextArea text;
    @FXML
    private Label id;
    public static Label idStaitc;

    static Connection con = null;
    @FXML
    private AnchorPane AP;

    public static AnchorPane APStatic;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("TaskController -> initialize() ...");
        textStatic = text;
        idStaitc = id;
        APStatic = AP;
    }

    @FXML
    private void editAction(KeyEvent event) {
        System.out.println("TaskController -> editAction() ...");

        app.libera.send(Const.EDIT);
        app.libera.waitForInt();

        app.libera.send(text.getText());
        app.libera.waitForInt();

        app.libera.send(Integer.parseInt(id.getText()));
    }

    @FXML
    private void delete(ActionEvent event) {
        System.out.println("TaskController -> delete() ...");

        app.libera.send(Const.REMOVE);
        app.libera.waitForInt();
        app.libera.send(Integer.parseInt(id.getText()));
        app.libera.waitForInt();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/app/Home.fxml"));
            id.getScene().setRoot(root);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

}
