package com.app.customer.controller;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import com.hex.framework.mail.MailingImpl;
import com.hex.framework.mail.vo.MailVO;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EmailNotification {
	
	@FXML
	private Button Send;
	@FXML
	private TextField fromText;
	@FXML
	private TextArea toText;
	@FXML
	private TextArea ccText;
	@FXML
	private TextArea bccText;
	@FXML
	private TextField subjectText;
	@FXML
	private TextArea contentText;
	
	public static Stage thisStage;

	@FXML
	private void SendEmail() {
		ResourceBundle bundle = ResourceBundle.getBundle(
				"MailServerCridencials", Locale.ENGLISH);
		String[] to = new String[1];
		String[] cc = new String[1];
		String[] bc = new String[1];
		to[0] = toText.getText();
		cc[0] = ccText.getText();
		bc[0] = bccText.getText();
		String host = bundle.getString("SEVER_HOST");
		int port = Integer.parseInt(bundle.getString("SEVER_PORT"));
		MailVO mailVO = new MailVO();
		mailVO.setFrom(fromText.getText());
		mailVO.setSubject(subjectText.getText());
		mailVO.setBody(contentText.getText());
		mailVO.setHost(host);
		mailVO.setPort(port);
		mailVO.setBccs(bc);
		mailVO.setCcs(cc);
		try {
			new MailingImpl().sendEmail(mailVO);
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
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			
		}
	}

}
