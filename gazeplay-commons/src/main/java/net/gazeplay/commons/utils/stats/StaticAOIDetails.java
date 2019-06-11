package net.gazeplay.commons.utils.stats;

import javafx.scene.paint.Color;
import lombok.Getter;

import java.util.ArrayList;

public class StaticAOIDetails {

    //Original dimensions of the game
    @Getter
    private final double originalImgWidth;
    @Getter
    private final double originalImgHeight;

    @Getter
    private final String gameName;

    private ArrayList<staticAOIBox> staticAOIList;

    public class staticAOIBox{
        //AOI Box to be drawn on the screen
        int x;
        int y;
        double width;
        double height;

        Color borderColor;

        int countFixations;


    }

//    public static StaticAOIDetails newInstance(String name, double width, double height) {
//        return new StaticAOIDetails(name, width, height);
//    }
//
//    private StaticAOIDetails(String name, double width, double height){
//        originalImgHeight = height;
//        originalImgWidth = width;
//        gameName = name;
//    }

    public StaticAOIDetails(String name, double width, double height){
        originalImgHeight = height;
        originalImgWidth = width;
        gameName = name;
        staticAOIList = new ArrayList<staticAOIBox>();
    }




}
