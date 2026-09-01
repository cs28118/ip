package lumine;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lumine.ui.MainWindow;

/**
 * Launches Lumine's JavaFX graphical user interface.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Lumine lumine = new Lumine("data/lumine.txt");
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
        AnchorPane mainWindow = fxmlLoader.load();
        Scene scene = new Scene(mainWindow);

        stage.setTitle("Lumine");
        stage.setMinHeight(300);
        stage.setMinWidth(400);
        stage.setScene(scene);
        fxmlLoader.<MainWindow>getController().setLumine(lumine);
        stage.show();
    }
}
