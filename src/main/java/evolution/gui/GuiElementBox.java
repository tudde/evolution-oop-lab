package evolution.gui;

import evolution.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


public class GuiElementBox {
    private VBox vbox = new VBox(4);


    public GuiElementBox(AbstractWorldMap map, Vector2d pos, int tileSize) {

        StackPane cell = new StackPane();
        cell.setAlignment(Pos.CENTER);
        Rectangle bg = new Rectangle();
        if (map.isBiased(pos)) {
            bg.setWidth(tileSize);
            bg.setHeight(tileSize);
            bg.setFill(Color.rgb(0, 100, 20, 0.3));
        }
        cell.getChildren().add(bg);
        IMapElement el = (IMapElement) map.objectAt(pos);
        if (el != null) {
            if (el instanceof Animal && ((Animal) el).getEnergy() > 0) {

                Color animalColor;
                animalColor = new Color(0, 0, 0, 1.0);

                if (((Animal) el).getEnergy() < 100) {
                    animalColor = new Color(0, 0, 0, ((double) (((Animal) el).getEnergy()) / 100));
                }

                Circle circle = new Circle();
                circle.setCenterX(100.0f);
                circle.setCenterY(100.0f);
                circle.setRadius(tileSize / 4);
                circle.setFill(animalColor);
                cell.getChildren().add(circle);
            } else if (el instanceof Plant) {
                Circle circle = new Circle();
                circle.setCenterX(100.0f);
                circle.setCenterY(100.0f);
                circle.setRadius(tileSize / 8);
                circle.setFill(Color.rgb(0, 100, 20, 0.5));
                cell.getChildren().add(circle);


            }

        }
        vbox.getChildren().add(cell);
        vbox.setAlignment(Pos.CENTER);
    }

    public VBox getVBox() {
        return vbox;
    }
}
