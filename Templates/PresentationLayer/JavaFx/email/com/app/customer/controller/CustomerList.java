@FXML
	public void email() {
		
		thisStage.close();
		AnchorPane root = null;
		EmailNotification controller = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					"/view/EmailNotificationview.fxml"));
			root = loader.load();
			controller = loader.getController();
			Stage stage = new Stage();
			EmailNotification.thisStage = stage;
			Scene scene = new Scene(root,581,504);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}