package net.gazeplay;


import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import net.gazeplay.commons.configuration.Configuration;
import net.gazeplay.commons.utils.HomeButton;
import net.gazeplay.commons.utils.stats.Stats;

import java.io.IOException;

public class StaticAOI extends GraphicalContext<BorderPane>{

    int totalFixationsCount;
    private final Stats stats;
    private Configuration config;
    public class staticAOIBox{
        int x;
        int y;
        double width;
        double height;

        int countFixations;


    }



    @Override
    public ObservableList<Node> getChildren() {
        return  root.getChildren();
//        return null;
    }

    public static StaticAOI newInstance(GazePlay gazePlay, Stats stats) {
        BorderPane root = new BorderPane();
        return new StaticAOI(gazePlay, root, stats);
    }

    private StaticAOI(GazePlay gazePlay, BorderPane root, Stats stats){

        super(gazePlay, root);
        this.stats = stats;

        this.config = Configuration.getInstance();

        VBox pane = new VBox(1);
        HBox topPane;
        VBox centerPane = new VBox();
        centerPane.setAlignment(Pos.CENTER);
        StackPane stackPane = new StackPane();


        EventHandler<Event> backEvent = e -> {

            StatsContext statsContext = null;
            try {
                statsContext = StatsContext.newInstance(gazePlay, stats);
            } catch (IOException er) {
                er.printStackTrace();
            }
            this.clear();
            gazePlay.onDisplayStats(statsContext);
        };

        ImageView screenshot = new ImageView();
        screenshot.setPreserveRatio(true);
        screenshot.setImage(new Image(stats.getSavedStatsInfo().getScreenshotFile().toURI().toString()));
        screenshot.setFitHeight(500);
        stackPane.getChildren().add(screenshot);



        HomeButton backButton = new HomeButton("data/common/images/back_button.png");
        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, backEvent);
        pane.getChildren().add(backButton);
        pane.setAlignment(Pos.CENTER_RIGHT);

        topPane = new HBox();//(timeLabel, region1, screenTitleText, region2, buttonBox);
        topPane.setSpacing(10);
        pane.setStyle("-fx-background-color: transparent");
        root.setCenter(stackPane);
        root.setTop(topPane);
        root.setBottom(pane);
        pane.toFront();
        root.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 1); -fx-background-radius: 8px; -fx-border-radius: 8px; -fx-border-width: 5px; -fx-border-color: rgba(60, 63, 65, 0.7); -fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);");




    }


}
