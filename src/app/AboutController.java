package app;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AboutController implements Initializable {

    private double xOffset = 0;

    private double yOffset = 0;

    @FXML
    private AnchorPane AP;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void okAction(Event event) {
        HomeController.aboutDialog.close();
    }

    @FXML
    private void mailAction(MouseEvent event) throws URISyntaxException, IOException {
        Desktop desktop;
        if (Desktop.isDesktopSupported()
                && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {
            URI mailto = new URI("mailto:vssx04@gmail.com?");
            desktop.mail(mailto);
        } else {
            throw new RuntimeException("desktop doesn't support mailto");
        }
    }

    @FXML
    private void githubAction(MouseEvent event) {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/vladislavsergeev"));
        } catch (Exception e) {
            System.out.println("error in URL");
        }
    }
}
