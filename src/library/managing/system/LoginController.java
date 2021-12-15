/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.managing.system;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author engmohammed
 */
public class LoginController implements Initializable {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private ComboBox<String> chosseUser;

    public static int idM = -1;

    public void login(Event e) {

        if (!username.getText().equals("") && !password.getText().equals("") && chosseUser.getSelectionModel().getSelectedIndex() != -1) {

            if (chosseUser.getSelectionModel().getSelectedIndex() == 0) {

                if (username.getText().trim().matches("[aA]dmin") && password.getText().equals("123456")) {
                    OpenStage("AdminInterface", "Admin", e);
                } else {
                    JOptionPane.showMessageDialog(null, "Invaild Username or Password");
                }

            } else if (chosseUser.getSelectionModel().getSelectedIndex() == 1) {

                idM = Connect.loginMemeber(username.getText(), password.getText());
                if (idM != -1) {

                    OpenStage("MemberInterface", "Member", e);

                } else {
                    JOptionPane.showMessageDialog(null, "Invaild Username or Password");
                }

            }

        } else {
            JOptionPane.showMessageDialog(null, "The Field Empty");
        }
    }

    public void OpenStage(String nameStage, String title, Event e) {
        try {

            Parent parent = FXMLLoader.load(getClass().getResource(nameStage + ".fxml"));
            Scene scene = new Scene(parent);
            Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            appStage.setScene(scene);
            appStage.setTitle(title);
            appStage.setResizable(false);
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            appStage.setX((primScreenBounds.getWidth() - appStage.getWidth()) / 2);
            appStage.setY((primScreenBounds.getHeight() - appStage.getHeight()) / 2);

        } catch (Exception ex) {
        }
    }

    public void exit() {
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        chosseUser.setItems(FXCollections.observableArrayList("Admin", "Member"));
    }

}
