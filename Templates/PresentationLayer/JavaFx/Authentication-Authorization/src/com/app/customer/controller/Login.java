package com.app.customer.controller;

import java.io.IOException;

import com.app.customer.util.LdapAuthentication;
import com.hexaware.framework.logger.LogFactory;
import com.hexaware.framework.logger.Logger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Login {
	@FXML
	private Button loginBtn;
	@FXML
	private TextField userNameText;
	@FXML
	private PasswordField passwordText;

	public static Stage thisStage;

	public static Logger log = LogFactory.getLogger(Login.class);

	@FXML
	private void loginPage() {

		LdapAuthentication authentication = new LdapAuthentication();
		try {

			if (!"".equalsIgnoreCase(userNameText.getText())&&!"".equalsIgnoreCase(passwordText.getText())&& authentication.checkAuthentication(userNameText.getText(),
					passwordText.getText())) {
				log.info("Authentication success for user");
				infoBox("Login Successfull", "ALERT MESSAGE", "");
				thisStage.close();
				Parent root;
				try {
					root = FXMLLoader.load(getClass().getResource(
							"/view/CustomerListview.fxml"));
					Scene scene = new Scene(root, 1090, 706);
					Stage primaryStage = new Stage();
					primaryStage.setScene(scene);
					CustomerList.thisStage = primaryStage;
					primaryStage.show();

				} catch (IOException exp) {
					log.debug("Login failed");
				}

			} else {
				log.info("Authentication faiiled for user");
				infoBox("Login Failed", "ALERT MESSAGE", "");
				userNameText.setText("");
				passwordText.setText("");
			}

		} catch (Exception exp) {
			log.debug("Authentication faiiled for user");
			log.info(exp.getMessage());
			infoBox("Login Failed", "ALERT MESSAGE", "");
		}

	}

	public static void infoBox(String infoMessage, String titleBar,
			String headerMessage) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(titleBar);
		alert.setHeaderText(headerMessage);
		alert.setContentText(infoMessage);
		alert.showAndWait();
	}

}
