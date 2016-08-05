package com.app.customer.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.map.ObjectMapper;

import com.app.customer.controller.CustomerList;
import com.app.framework.delegate.HJSFBusinessDelegate;
import com.app.customer.vo.Customer;
import com.hexaware.framework.logger.LogFactory;
import com.hexaware.framework.logger.Logger;
${Validation_import}

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CustomerAdd {

	@FXML
	private TextField customerId;
	@FXML
	private TextField name;
	@FXML
	private TextField email;
	@FXML
	private TextField phone;
	@FXML
	private TextArea address;
	@FXML
	private TextField orders;
	@FXML
	private TextField action;
	@FXML
	private TextField createdBy;
	@FXML
	private TextField createdDate;
	@FXML
	private TextField modifiedBy;
	@FXML
	private TextField modifiedDate;
	@FXML
	private Button addToListbtn;
	@FXML
	private Button listbtn;
	@FXML
	private Button update;
	@FXML
	private Hyperlink listlink;
	@FXML
	private GridPane gridPane;
	@FXML
	private Label infoLabel;

	static Stage thisStage;

	static Logger log = LogFactory.getLogger(CustomerAdd.class);

	@FXML
	void initialize() {
		update.setVisible(false);
	}

	/*
	 * Clearing GripPane for add operation
	 */
	void clearGrid() {

		gridPane.getChildren().remove(modifiedDate);
		gridPane.getChildren().remove(17);

		gridPane.getChildren().remove(modifiedBy);
		gridPane.getChildren().remove(16);

		gridPane.getChildren().remove(createdDate);
		gridPane.getChildren().remove(15);

		gridPane.getChildren().remove(createdBy);
		gridPane.getChildren().remove(14);

		gridPane.getRowConstraints().clear();
	}

	/*
	 * Adding customer into DataBase
	 */
	@FXML
	private void addRowToList() {
		System.out.println("getting into add method");
		try {
			/*
			 * Logic
			 */
			Customer customer = new Customer();
			customer.setName(name.getText());
			customer.setEmail(email.getText());
			customer.setAction(action.getText());
			customer.setAddress(address.getText());
			customer.setOrders(orders.getText());
			String id = customerId.getText();
			int customerid = Integer.parseInt(id);
			customer.setCustomerid(customerid);
			customer.setPhone(phone.getText());
			customer.setCreateddate(new Date());
			customer.setModifieddate(new Date());
			customer.setCreatedby(InetAddress.getLocalHost().getHostName());
			customer.setModifiedby(InetAddress.getLocalHost().getHostName());

			 /*
		      * save the customer into DB
			  */
				if (insert(customer)) {
					infoBox("Record Inserted Successfully", "ALERT MESSAGE", "");

				   /*
					* Navigating to List page
					*/
					customerListlink();
					}
		}catch (Exception e) {
			System.err.print(e.getMessage());
			infoLabel.setText(e.getMessage());
		}
		System.out.println("Exiting add method");
	}

	/*
	 * Navigating to List page
	 */
	@FXML
	public void list() {
		customerListlink();
	}

	/*
	 * Navigating to List page
	 */
	@FXML
	public void customerListlink() {
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

	}

	/*
	 * Retrieving customer details from DB for edit operation
	 */
	public void editData(Customer customer) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		customerId.setEditable(false);
		createdDate.setEditable(false);
		createdBy.setEditable(false);
		modifiedBy.setEditable(false);
		customerId.setText(Integer.toString(customer.getCustomerid()));
		name.setText(customer.getName());
		email.setText(customer.getEmail());
		action.setText(customer.getAction());
		address.setText(customer.getAddress());
		phone.setText(customer.getPhone());
		orders.setText(customer.getOrders());
		createdBy.setText(customer.getCreatedby());
		createdDate.setText(sdf.format(customer.getCreateddate()));
		modifiedBy.setText(customer.getModifiedby());
		modifiedDate.setText(sdf.format(customer.getModifieddate()));
		update.setVisible(true);
		addToListbtn.setVisible(false);
		listbtn.setVisible(true);
	}

	/*
	 * Updating customer into DataBase
	 */
	@FXML
	public void update() {
		System.out.println("getting into update method");
		try {

			/*
			 * Logic
			 */
			Customer customer = new Customer();
			customer.setName(name.getText());
			customer.setEmail(email.getText());
			customer.setAction(action.getText());
			customer.setAddress(address.getText());
			customer.setOrders(orders.getText());
			String id = customerId.getText();
			int customerId = Integer.parseInt(id);
			customer.setCustomerid(customerId);
			customer.setPhone(phone.getText());
			customer.setCreateddate(new Date());
			customer.setModifieddate(new Date());
			customer.setCreatedby(createdBy.getText());
			customer.setModifiedby(modifiedBy.getText());

			/*
			 * update the customer into DB
			 */

			if (update(customer)) {
				infoBox("Record Updated Successfully", "ALERT MESSAGE", "");
				/*
				 * Navigating to List page
				 */
				customerListlink();
			}
		}catch (Exception e) {
			System.err.print(e.getMessage());
			infoLabel.setText(e.getMessage());
		}
		System.out.println("Exiting update method");
	}

	/*
	 * Alert message
	 */

	public static void infoBox(String infoMessage, String titleBar,
			String headerMessage) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(titleBar);
		alert.setHeaderText(headerMessage);
		alert.setContentText(infoMessage);
		alert.showAndWait();
	}

	public boolean insert(Customer customer) throws IOException {

		log.debug("Inside Method Insert..");
		HJSFBusinessDelegate delegate = new HJSFBusinessDelegate();
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(customer);
		try
		{
		${Validation_Call}
		String output = delegate.doBusiness("POJO",
				"com.app.customer.stub.CustomerStub", "insert",
				"com.app.customer.vo.Customer", jsonInString);
		log.debug("output : " + output);
		}${Validation_CatchAdd}
		catch (Exception e) {
			System.err.print(e.getMessage());
			infoLabel.setText(e.getMessage());
		}
		log.debug("End of Method Insert..");
		return true;
	}

	public boolean update(Customer customer) throws IOException {

		log.debug("Inside Method Update..");
		HJSFBusinessDelegate delegate = new HJSFBusinessDelegate();
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(customer);
		try
		{
		${Validation_Call}
		String output = delegate.doBusiness("POJO",
				"com.app.customer.stub.CustomerStub", "update",
				"com.app.customer.vo.Customer", jsonInString);
		log.debug("output : " + output);
		}${Validation_CatchAdd}
		catch (Exception e) {
			System.err.print(e.getMessage());
			infoLabel.setText(e.getMessage());
		}
		log.debug("End of Method Update..");
		return true;
	}
}
