package app;

import task.TaskFX;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.Const;

public class HomeController implements Initializable {
    private static int user_id;

    private double xOffset = 0;
    private double yOffset = 0;

    public static JFXDialog aboutDialog;

    @FXML
    public FlowPane FP;

    static Connection con = null;
    @FXML
    private JFXTextField searchComBox;
    @FXML
    private StackPane AP;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu file;
    @FXML
    private MenuItem About;
    @FXML
    private MenuItem close;

    public static void setUserId(int id) {
        user_id = id;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("HomeController -> initialize() ...");
        user_id = -1;
        InitializeTasks();
        try {
            AnchorPane aboutPane = FXMLLoader.load(getClass().getResource("about.fxml"));
            aboutDialog = new JFXDialog(AP, aboutPane, JFXDialog.DialogTransition.TOP);
        } catch (IOException ex) {
            System.out.println("error in loading about.fxml file");
        }

    }

    @FXML
    private void logout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));

            Scene scene = new Scene(root);
            ((Stage) (searchComBox.getScene().getWindow())).setScene(scene);
            ((Stage) (searchComBox.getScene().getWindow())).initStyle(StageStyle.UNDECORATED);
            ((Stage) (searchComBox.getScene().getWindow())).show();
        }
        catch (IOException e) {

        }
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
    private void closeWindow(Event event) {
        System.exit(0);
    }

    @FXML
    private void AddTaskAction(ActionEvent event) {
        System.out.println("HomeController -> AddTaskAction() ...");
        try {
            Parent newTaskFXML = FXMLLoader.load(getClass().getResource("/task/newTask.fxml"));
            Scene sc = new Scene(newTaskFXML);
            Stage stage = new Stage();
            stage.setScene(sc);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Создание задачи");
            stage.showAndWait();
            InitializeTasks();
            System.gc();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    @FXML
    private void searchEvent() {
        System.out.println("HomeController -> searchEvent() ...");
        if ((searchComBox.getText().trim().equals(""))) {
            InitializeTasks();
        } else {
            FP.getChildren().clear();
            List<TaskFX> taskList = search();
            int count = taskList.size();
            for (int i = 0; i < count; i++) {
                try {
                    Parent TaskFXML = FXMLLoader.load(getClass().getResource("/task/Task.fxml"));
                    task.TaskController.textStatic.setText(taskList.get(i).getText());
                    task.TaskController.idStaitc.setText(taskList.get(i).getid() + "");
                    task.TaskController.APStatic.setStyle("-fx-background-color : " + taskList.get(i).getHexaColor() + ";");
                    FP.getChildren().add(TaskFXML);
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
        }
    }

    public void InitializeTasks() {
        System.out.println("HomeController -> InitializeTasks() ...");
        FP.getChildren().clear();
        List<TaskFX> taskList = select();
        int count = taskList.size();
        for (int i = 0; i < count; i++) {
            try {
                Parent TaskFXML = FXMLLoader.load(getClass().getResource("/task/Task.fxml"));
                task.TaskController.textStatic.setText(taskList.get(i).getText());
                task.TaskController.idStaitc.setText(taskList.get(i).getid() + "");
                task.TaskController.APStatic.setStyle("-fx-background-color : " + taskList.get(i).getHexaColor() + ";");

                FP.getChildren().add(TaskFXML);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }

    public static int getNumOfTasks() {
        System.out.println("HomeController -> getNumOfTasks() ...");
        int n = 0;
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            con = DriverManager.getConnection("jdbc:hsqldb:file:db/TaskDatabase", "SA", "");
        } catch (Exception ex) {
            System.out.println(ex);
        }
        String sqlString = "SELECT COUNT(*) AS total FROM tasks";
        try {
            PreparedStatement prepareStatement = con.prepareStatement(sqlString);
            ResultSet resultSet = prepareStatement.executeQuery();
            resultSet.next();
            n = resultSet.getInt("total");
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
        System.out.println("n = " + n);
        return n;
    }

    public static void insert(String text, String color) {
        System.out.println("HomeController -> insert() ...");
        app.libera.send(Const.ADD);
        app.libera.waitForInt();

        app.libera.send(text);
        app.libera.waitForInt();

        app.libera.send(color);
        app.libera.waitForInt();
    }

    public static List select() {
        System.out.println("HomeController -> select() ...");

        app.libera.send(Const.GET);

        boolean stop = false;
        List listTasks = new ArrayList<TaskFX>();

        do {
            int id = app.libera.waitForInt();
            if (id < 0) stop = true;
            else {
                app.libera.send(1);

                String text = app.libera.waitForString();
                app.libera.send(1);

                String color = app.libera.waitForString();
                app.libera.send(1);

                listTasks.add(new TaskFX(id, text, color));
            }
        } while (!stop);

        printList(listTasks);

        return listTasks;
    }

    public List search() {
        System.out.println("HomeController -> select() ...");

        app.libera.send(Const.SEARCH);
        app.libera.waitForInt();
        app.libera.send(searchComBox.getText().trim());

        boolean stop = false;
        List listTasks = new ArrayList<TaskFX>();

        do {
            int id = app.libera.waitForInt();
            if (id < 0) stop = true;
            else {
                app.libera.send(1);

                String text = app.libera.waitForString();
                app.libera.send(1);

                String color = app.libera.waitForString();
                app.libera.send(1);

                listTasks.add(new TaskFX(id, text, color));
            }
        } while (!stop);

        printList(listTasks);

        return listTasks;
    }

    @FXML
    public void refresh(ActionEvent event) {
        InitializeTasks();
        System.gc();
    }

    private static void printList(List list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

    @FXML
    private void aboutWindow(ActionEvent event) {
        if (aboutDialog.isVisible()) {
            return;
        }
        aboutDialog.show();
    }
}