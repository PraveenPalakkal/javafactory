package com.app.customer.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.app.framework.delegate.HJSFBusinessDelegate;
import com.app.customer.vo.Customer;
import com.hexaware.framework.logger.LogFactory;
import com.hexaware.framework.logger.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
${Cache_import}

public class CustomerList {

	@FXML
	private Button Addbtn;
	@FXML
	private Button search;
	${emailButtonInJavaFx}
	@FXML
	private ComboBox<String> comboField;
	@FXML
	private TextField fieldValue;
	@FXML
	public TableView<Customer> customers;

	public static Stage thisStage;
	
	static Logger log = LogFactory.getLogger(CustomerList.class);
	
	public CustomerList() {

	}

	@FXML
	public void initialize() {
		comboField.getItems().clear();
		comboField.getItems().addAll("customerId", "name", "email", "phone",
				"address", "action", "createddate", "createdby",
				"modifieddate", "modifiedby");
		List<Customer> customerList = initialiseList();

		for (Customer customer : customerList) {
			customers.getItems().add(customer);
		}
	}

	/*
	 * Email Notification
	 */
	${emailCodeInJavaFx}
	
	
	/*
	 * Navigating from CustomerList page to CustomerAddListPage for Add
	 * Operation
	 */
	@FXML
	public void addNewrow() {
		thisStage.close();
		AnchorPane root = null;
		CustomerAdd controller = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					"/view/CustomerAddview.fxml"));
			root = loader.load();
			controller = loader.getController();
			controller.clearGrid();
			Stage stage = new Stage();
			CustomerAdd.thisStage = stage;
			Scene scene = new Scene(root,1020,706);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Navigating from CustomerList page to CustomerAddListPage for Edit
	 * Operation
	 */
	@FXML
	public void edit() {
		thisStage.close();
		AnchorPane root = null;
		CustomerAdd controller;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					"/view/CustomerAddview.fxml"));
			root = loader.load();
			controller = loader.getController();
			controller
					.editData(customers.getSelectionModel().getSelectedItem());
			Scene scene = new Scene(root,1020,706);
			Stage primaryStage = new Stage();
			CustomerAdd.thisStage = primaryStage;
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*
	 *Delete customer from DataBase
	 */
	@FXML
	public void delete() throws ParseException {
		System.out.println("Entering into Delete of BEAN********");
		List deleteList = new ArrayList();
		Customer customerVo = customers.getSelectionModel().getSelectedItem();
		deleteList.add(customerVo);
		HJSFBusinessDelegate delegate = new HJSFBusinessDelegate();
		JSONParser parser = new JSONParser();
		JSONArray array = (JSONArray) parser.parse(deleteList.toString());
		log.debug("Total Customer to be deleted : " + array.size());
		String output = delegate.doBusiness("POJO",
				"com.app.customer.stub.CustomerStub", "deleteAll",
				"java.util.List<com.app.customer.vo.Customer>",
				array.toJSONString());
		log.debug("output : " + output);
		this.customers.getItems().remove(customerVo);
		log.debug("End of Method Delete..");
		infoBox("Record Deleted Successfully", "ALERT MESSAGE", "");
	}

	/*
	 *Search customer from DataBase
	 */
	@FXML
	public void search() {
		System.out.println("Entering into Search of BEAN********");
		String searchValue = fieldValue.getText();
		String columnValue = comboField.getValue();

		JSONObject object = new JSONObject();
		object.put("searchValue", searchValue);
		object.put("searchColumn", columnValue);

		HJSFBusinessDelegate delegate = new HJSFBusinessDelegate();
		String output = delegate.doBusiness("POJO",
				"com.app.customer.stub.CustomerStub", "search",
				"String,String",object.toJSONString());

		JSONParser parser = new JSONParser();
		List<Customer> customerList = new ArrayList<>();
		try {
			JSONArray customers = (JSONArray) parser.parse(output);
			JSONObject customer = null;
			Customer customerVO = null;
			ObjectMapper mapper = new ObjectMapper();

			for (int i = 0; i < customers.size(); i++) {
				customer = (JSONObject) customers.get(i);
				customerVO = mapper.convertValue(customer, Customer.class);
				customerList.add(customerVO);
			}

			this.customers.getItems().clear();
			this.customers.getItems().addAll(customerList);
			log.debug("End of Method Search..");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

	}

	/*
	 *Clear the textField and referesh table view.
	 */
	@FXML
	public void clear(ActionEvent event) {
		fieldValue.setText("");
		customers.getItems().clear();
		initialize();
	}
	
	/*
	 * Alert Message Box
	 */
	public static void infoBox(String infoMessage, String titleBar, String headerMessage)
    {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titleBar);
        alert.setHeaderText(headerMessage);
        alert.setContentText(infoMessage);
        alert.showAndWait();
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
					"/view/CustomerAddview.fxml"));
			Scene scene = new Scene(root, 1020, 706);
			Stage primaryStage = new Stage();
			primaryStage.setScene(scene);
			CustomerAdd.thisStage = primaryStage;
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public List<Customer> initialiseList() {
		List<Customer> customerList = null;
		try {
			log.debug("Inside Method initialiseList..");
			HJSFBusinessDelegate delegate = new HJSFBusinessDelegate();
			${Cache_ImplStart}
			String output = delegate.doBusiness("POJO",
					"com.app.customer.stub.CustomerStub", "getAllCustomer",
					"", "{}");
			log.debug("output : " + output);
			JSONParser parser = new JSONParser();
			JSONArray customers = (JSONArray) parser.parse(output);
			log.debug("Customer Array : " + customers);
			customerList = new ArrayList<>();
			for (int i = 0; i < customers.size(); i++) {
				JSONObject customer = (JSONObject) customers.get(i);
				ObjectMapper mapper = new ObjectMapper();
				customerList.add(mapper.convertValue(customer, Customer.class));
			}
			${Cache_ImplEnd}
			log.debug("List Size : " + customerList.size());
			log.debug("End of Method initialiseList..");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return customerList;

	}
}
