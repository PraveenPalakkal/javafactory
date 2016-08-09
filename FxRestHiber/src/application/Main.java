package application;

import com.app.customer.controller.CustomerList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {

	/*Start application with a stage and scene.
	 * (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource(
					"/view/CustomerListview.fxml"));
			Scene scene = new Scene(root,1090,706);
			scene.getStylesheets().add(
					getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			CustomerList.thisStage=primaryStage;
			primaryStage.show();
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
