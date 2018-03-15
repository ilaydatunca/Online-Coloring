package edu.tr.mef.comp106.fxquickstart;





import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/edu/tr/mef/comp106/fxquickstart/fxml/Scene.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/edu/tr/mef/comp106/fxquickstart/styles/Styles.css");
        stage.setTitle("COMP106 Blob Coloring FX Quickstart");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
