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

public class HomeController implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("HomeController -> initialize() ...");
        InitializeTasks();
        try {
            AnchorPane aboutPane = FXMLLoader.load(getClass().getResource("about.fxml"));
            aboutDialog = new JFXDialog(AP, aboutPane, JFXDialog.DialogTransition.TOP);
        } catch (IOException ex) {
            System.out.println("error in loading about.fxml file");
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
    private void searchEvent(KeyEvent event) {
        System.out.println("HomeController -> searchEvent() ...");
        if ((searchComBox.getText().trim().equals(""))) {
            InitializeTasks();
        } else {

            FP.getChildren().clear();
            List<TaskFX> taskList = select("where text like '%" + searchComBox.getText().trim() + "%'");
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
        createTasksTable();
        FP.getChildren().clear();
        List<TaskFX> taskList = select("");
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

    private void createTasksTable() {
        System.out.println("HomeController -> createTasksTable() ...");
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            con = DriverManager.getConnection("jdbc:hsqldb:file:db/TaskDatabase", "SA", "");
        } catch (Exception ex) {
            System.out.println(ex);
        }
        String tableCreateSQL = "CREATE TABLE if not EXISTS tasks ( id INTEGER IDENTITY PRIMARY KEY,"
                + " text LONGVARCHAR NOT NULL , color VARCHAR(25) NOT NULL )";
        try {
            con.prepareStatement(tableCreateSQL).execute();
        } catch (Exception ex) {
            System.out.println(ex);
            System.out.println("failed to create table");
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println("error in close connection");
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
        String sqlString = "INSERT INTO tasks (text,color) values (?,?)";
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            con = DriverManager.getConnection("jdbc:hsqldb:file:db/TaskDatabase", "SA", "");
        } catch (Exception ex) {
            System.out.println(ex);
        }
        try {
            PreparedStatement prepareStatement = con.prepareStatement(sqlString);

            prepareStatement.setString(1, text);
            prepareStatement.setString(2, color);
            prepareStatement.execute();
            System.out.println("insert Done ...");
        } catch (SQLException ex) {
            System.out.println(ex);
            System.out.println("failed to insert table");
        }

    }

    public static List select(String s) {
        System.out.println("HomeController -> select() ...");
        String sqlString;
        if ("".equals(s)) {
            sqlString = "SELECT * FROM tasks";
        } else {
            sqlString = "SELECT * FROM tasks " + s;
        }
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            con = DriverManager.getConnection("jdbc:hsqldb:file:db/TaskDatabase", "SA", "");
        } catch (Exception ex) {
            System.out.println(ex);
        }
        List listTasks = new ArrayList<TaskFX>();
        try {
            PreparedStatement prepareStatement = con.prepareStatement(sqlString);
            ResultSet resultSet = prepareStatement.executeQuery();
            while (resultSet.next()) {
                listTasks.add(new TaskFX(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3)));
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
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
