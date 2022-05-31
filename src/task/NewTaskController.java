package task;

import app.HomeController;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class NewTaskController implements Initializable {

    @FXML
    private JFXTextArea text;

    private double xOffset = 0;
    private double yOffset = 0;

    private String hexaColor = "#bed9d4";
    @FXML
    private AnchorPane AP;
    @FXML
    private JFXButton closeBTN;
    @FXML
    private JFXButton addBTN;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("NewTaskController -> initialize()  ...");
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
    private void closeAction(Event event) {
        System.out.println("NewTaskController -> closeAction()  ...");
        ((Stage) (text.getScene().getWindow())).close();
    }

    @FXML
    private void AddAction(Event event) {
        try {
            System.out.println("NewTaskController -> AddAction()  ...");
            HomeController.insert(text.getText(), hexaColor);
            closeAction(event);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @FXML
    private void green(ActionEvent event) {
        hexaColor = "#bed9d4";
        AP.setStyle("-fx-background-color: " + hexaColor + ";");
        addBTN.setStyle("-fx-background-color: #bed9d4");
        closeBTN.setStyle("-fx-background-color: #bed9d4");
    }

    @FXML
    private void blue(ActionEvent event) {
        hexaColor = "#bad5f0";
        AP.setStyle("-fx-background-color: " + hexaColor);
        addBTN.setStyle("-fx-background-color: #bad5f0");
        closeBTN.setStyle("-fx-background-color: #bad5f0");
    }

    @FXML
    private void yellow(ActionEvent event) {
        hexaColor = "fae4cd";
        AP.setStyle("-fx-background-color: " + hexaColor + ";");
        addBTN.setStyle("-fx-background-color: #fae4cd" + ";");
        closeBTN.setStyle("-fx-background-color: #fae4cd" + ";");
    }

    @FXML
    private void purple(ActionEvent event) {
        hexaColor = "#efdff5";
        AP.setStyle("-fx-background-color: " + hexaColor);
        addBTN.setStyle("-fx-background-color: #efdff5");
        closeBTN.setStyle("-fx-background-color: #efdff5");
    }

}
