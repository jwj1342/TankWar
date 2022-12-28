package henu.soft.tankwar;

import com.almasb.fxgl.scene.SubScene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SuccessScence extends SubScene {
    public SuccessScence() {
        Text text = new Text("You Win");
        text.setFont(Font.font(70));
        StackPane stackPane = new StackPane(text);
        stackPane.setPrefSize(800, 600);
        stackPane.setStyle("-fx-background-color: Green");

        getContentRoot().getChildren().add(stackPane);
    }

}
