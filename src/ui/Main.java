package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("PongGame.fxml"));
		Parent p = loader.load();
		Scene s = new Scene(p);
		
		stage.setScene(s);
		stage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}