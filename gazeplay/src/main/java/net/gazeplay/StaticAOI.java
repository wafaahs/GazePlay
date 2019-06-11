package net.gazeplay;


import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import lombok.NonNull;
import lombok.Setter;
import net.gazeplay.commons.configuration.Configuration;
import net.gazeplay.commons.ui.I18NText;
import net.gazeplay.commons.ui.Translator;
import net.gazeplay.commons.utils.HomeButton;
import net.gazeplay.commons.utils.multilinguism.Multilinguism;
import net.gazeplay.commons.utils.stats.*;
import net.gazeplay.games.bubbles.BubblesGamesStats;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class StaticAOI extends GraphicalContext<BorderPane>{


    private static final String COLON = "Colon";
    private static Boolean ALIGN_LEFT = true;
    private static String currentLanguage;


    int totalFixationsCount;
    private final Stats stats;
    private Configuration config;

    private ImageView screenshot;

//    private ArrayList<>;



    private void addToGrid(GridPane grid, AtomicInteger currentFormRow, I18NText label, Text value) {

        final int COLUMN_INDEX_LABEL_LEFT = 0;
        final int COLUMN_INDEX_INPUT_LEFT = 1;
        final int COLUMN_INDEX_LABEL_RIGHT = 1;
        final int COLUMN_INDEX_INPUT_RIGHT = 0;

        final int currentRowIndex = currentFormRow.incrementAndGet();

        label.setId("item");
        // label.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14)); in CSS

        value.setId("item");
        // value.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14)); in CSS
        if (ALIGN_LEFT) {
            grid.add(label, COLUMN_INDEX_LABEL_LEFT, currentRowIndex);
            grid.add(value, COLUMN_INDEX_INPUT_LEFT, currentRowIndex);

            GridPane.setHalignment(label, HPos.LEFT);
            GridPane.setHalignment(value, HPos.LEFT);
        } else {
            grid.add(value, COLUMN_INDEX_INPUT_RIGHT, currentRowIndex);
            grid.add(label, COLUMN_INDEX_LABEL_RIGHT, currentRowIndex);

            GridPane.setHalignment(label, HPos.RIGHT);
            GridPane.setHalignment(value, HPos.RIGHT);
        }
    }


    @Override
    public ObservableList<Node> getChildren() {
        return  root.getChildren();
    }

    public static StaticAOI newInstance(@NonNull GazePlay gazePlay, @NonNull Stats stats) {
        BorderPane root = new BorderPane();
        return new StaticAOI(gazePlay, root, stats);
    }

    private StaticAOI(GazePlay gazePlay, BorderPane root, Stats stats){

        super(gazePlay, root);
        this.stats = stats;

        final Translator translator = gazePlay.getTranslator();

        currentLanguage = translator.currentLanguage();

        // Align right for Arabic Language
        if (currentLanguage.equals("ara")) {
            ALIGN_LEFT = false;
        }


        this.config = Configuration.getInstance();

        Multilinguism multilinguism = Multilinguism.getSingleton();

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(70);
        grid.setVgap(18);
        grid.setPadding(new Insets(50, 50, 50, 50));

        AtomicInteger currentFormRow = new AtomicInteger(1);
        {
            I18NText label = new I18NText(translator, "TotalLength", COLON);
            Text value = new Text(StatsDisplay.convert(stats.computeTotalElapsedDuration()));
            addToGrid(grid, currentFormRow, label, value);
        }

        {
            final I18NText label;
            if (stats instanceof ShootGamesStats || stats instanceof BubblesGamesStats) {
                label = new I18NText(translator, "HitRate", COLON);
                Text value = new Text(String.valueOf(stats.getShotRatio() + "%"));
                if (!(stats instanceof ExplorationGamesStats)) {
                    addToGrid(grid, currentFormRow, label, value);
                }
            }
        }
        {
            I18NText label = new I18NText(translator, "Length", COLON);

            Text value = new Text(StatsDisplay.convert(stats.getRoundsTotalAdditiveDuration()));

            if (!(stats instanceof ExplorationGamesStats)) {
                addToGrid(grid, currentFormRow, label, value);
            }
        }
        {
            if (stats instanceof ShootGamesStats && !(stats instanceof BubblesGamesStats)
                    && ((ShootGamesStats) stats).getNbUnCountedShots() != 0) {

                final I18NText label = new I18NText(translator, "UncountedShot", COLON);

                final Text value = new Text(String.valueOf(((ShootGamesStats) stats).getNbUnCountedShots()));
                if (!(stats instanceof ExplorationGamesStats)) {
                    addToGrid(grid, currentFormRow, label, value);
                }
            }
        }
        {
            I18NText label = new I18NText(translator, "Rounds Played", COLON);
            Text value = new Text(""+(stats.getRoundsCounter()));
            addToGrid(grid, currentFormRow, label, value);
        }
        {
            final I18NText label;
            if (stats instanceof BubblesGamesStats) {
                label = new I18NText(translator, "BubbleShot", COLON);
            } else if (stats instanceof ShootGamesStats) {
                label = new I18NText(translator, "Shots", COLON);
            } else if (stats instanceof HiddenItemsGamesStats) {
                label = new I18NText(translator, "HiddenItemsShot", COLON);
            } else {
                label = new I18NText(translator, "Score", COLON);
            }
            Text value;
            if (stats instanceof BubblesGamesStats) {
                value = new Text(String.valueOf(stats.getNbGoals() / 2));

            } else {
                value = new Text(String.valueOf(stats.getNbGoals()));
            }
            if (!(stats instanceof ExplorationGamesStats)) {
                addToGrid(grid, currentFormRow, label, value);
            }
        }
        {
            I18NText label = new I18NText(translator, "Number of AOIs", COLON);

            Text value = new Text("10");

            if (!(stats instanceof ExplorationGamesStats)) {
                addToGrid(grid, currentFormRow, label, value);
            }
        }





        VBox pane = new VBox(1);


//        HBox topPane;
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

        screenshot = new ImageView();
        screenshot.setPreserveRatio(true);
        screenshot.setImage(new Image(stats.getSavedStatsInfo().getScreenshotFile().toURI().toString()));

        root.widthProperty().addListener((observable, oldValue, newValue) -> {
            screenshot.setFitWidth(newValue.doubleValue() * 0.55);
            //NEED TO UPDATE THEM BOXES HERE

        });

        stackPane.getChildren().add(screenshot);

        StackPane centerStackPane = new StackPane();
        centerStackPane.getChildren().add(stackPane);








        HomeButton backButton = new HomeButton("data/common/images/back_button.png");
        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, backEvent);
        pane.getChildren().add(backButton);
        pane.setAlignment(Pos.CENTER_RIGHT);

//        topPane = new HBox();//(timeLabel, region1, screenTitleText, region2, buttonBox);
//        topPane.setSpacing(10);

        Text screenTitleText = new Text(multilinguism.getTrad("Static Area Of Interest" , config.getLanguage()));
        // screenTitleText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        screenTitleText.setId("title");
        // screenTitleText.setTextAlignment(TextAlignment.CENTER);

        StackPane topPane = new StackPane();
        topPane.getChildren().add(screenTitleText);

        VBox statsPane = new VBox(1);

        Text statsTitleText = new Text(multilinguism.getTrad("Rounds Stats" , config.getLanguage()));
        statsTitleText.setId("subTitle");

        Text currentAOITitleText = new Text(multilinguism.getTrad("Selected AOI: " , config.getLanguage()));
        currentAOITitleText.setId("subTitle");

        statsPane.getChildren().add(statsTitleText);
        statsPane.getChildren().add(grid);
        statsPane.getChildren().add(currentAOITitleText);
        statsPane.setAlignment(Pos.CENTER);


        pane.setStyle("-fx-background-color: transparent");
//        root.setCenter(stackPane);
        root.setTop(topPane);

        if (ALIGN_LEFT) {
            root.setLeft(statsPane);
        } else { // Arabic alignment
            root.setRight(statsPane);
        }

        root.setCenter(centerStackPane);
        root.setBottom(pane);
        pane.toFront();
        root.setStyle(
                "-fx-background-color: rgba(128, 128, 128, 1); -fx-background-radius: 8px; -fx-border-radius: 8px; -fx-border-width: 5px; -fx-border-color: rgba(60, 63, 65, 0.7); -fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);");




    }


}
