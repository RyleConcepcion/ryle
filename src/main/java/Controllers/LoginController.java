package Controllers;

import Model.User;
import Utils.PasswordUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;

    Stage dialogStage = new Stage();
    Scene scene;

    public void handleLoginButtonAction(ActionEvent event) throws SQLException, IOException {
        String username = usernameField.getText();
        String providedPassword = passwordField.getText();

        if ((username == null || username.isEmpty()) || (providedPassword == null || providedPassword.isEmpty())) {
            Utils.HelperMethods.alertBox("Please enter the Username and Password", null, "Login Failed!");
        } else if (!Utils.HelperMethods.validateUsername(username)) {
            Utils.HelperMethods.alertBox("Please enter a valid Username!", null, "Login Failed!");
        } else {

            User user = Model.Datasource.getInstance().getUserByUsername(username);
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                Utils.HelperMethods.alertBox("There is no user registered with that username!", null, "Login Failed!");
            } else {
                boolean passwordMatch = PasswordUtils.verifyUserPassword(providedPassword, user.getPassword(), user.getSalt());

                if (passwordMatch) {
                    UserSessionController.setUserId(user.getId());
                    UserSessionController.setUserFullName(user.getFullname());
                    UserSessionController.setUserName(user.getUsername());
                    UserSessionController.setUserEmail(user.getEmail());
                    UserSessionController.setUserAdmin(user.getAdmin());
                    UserSessionController.setUserStatus(user.getStatus());

                    Node node = (Node) event.getSource();
                    dialogStage = (Stage) node.getScene().getWindow();
                    dialogStage.close();
                    if (user.getAdmin() == 0) {
                        scene = new Scene(FXMLLoader.load(getClass().getResource("resources/Fxml/main-dashboard.fxml")));
                    } else if (user.getAdmin() == 1) {
                        scene = new Scene(FXMLLoader.load(getClass().getResource("resources/Fxml/main-dashboard.fxml")));
                    }
                    dialogStage.setScene(scene);
                    dialogStage.show();
                } else {
                    Utils.HelperMethods.alertBox("Please enter correct Email and Password", null, "Login Failed!");
                }
            }
        }
    }

    public void handleRegisterButtonAction(ActionEvent actionEvent) throws IOException {
        Stage dialogStage;
        Node node = (Node) actionEvent.getSource();
        dialogStage = (Stage) node.getScene().getWindow();
        dialogStage.close();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("resources/Fxml/register.fxml")));
        dialogStage.setScene(scene);
        dialogStage.show();
    }
}
