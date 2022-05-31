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
        String sqlString = "Update tasks set text = ? where id = ?";
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            con = DriverManager.getConnection("jdbc:hsqldb:file:db/TaskDatabase", "SA", "");
        } catch (Exception ex) {
            System.out.println(ex);
        }
        try {
            PreparedStatement prepareStatement = con.prepareStatement(sqlString);
            prepareStatement.setInt(2, Integer.parseInt(id.getText()));
            prepareStatement.setString(1, text.getText());
            prepareStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
            System.out.println("failed to insert in table");
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }

    }

    @FXML
    private void delete(ActionEvent event) {
        System.out.println("TaskController -> delete() ...");
        String sqlString = "DELETE FROM tasks WHERE id =  ?";
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            con = DriverManager.getConnection("jdbc:hsqldb:file:db/TaskDatabase", "SA", "");
        } catch (Exception ex) {
            System.out.println(ex);
        }
        try {
            PreparedStatement prepareStatement = con.prepareStatement(sqlString);
            prepareStatement.setInt(1, Integer.parseInt(id.getText()));
            prepareStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("failed to delete the table");
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/app/Home.fxml"));
            id.getScene().setRoot(root);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

}
