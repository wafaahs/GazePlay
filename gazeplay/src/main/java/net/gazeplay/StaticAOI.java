package net.gazeplay;


import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.*;

public class StaticAOI extends GraphicalContext<BorderPane>{

    int totalFixationsCount;

    public class staticAOIBox{
        int x;
        int y;
        double width;
        double height;

        int countFixations;


    }



    @Override
    public ObservableList<Node> getChildren() {
        return null;
    }
}
