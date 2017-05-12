package ch.epfl.alpano.gui;


import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Alpano extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        PanoramaParametersBean parametersBean = new PanoramaParametersBean(PredefinedPanoramas.JURA_ALPS);
        List<Summit> summits = GazetteerParser.readSummitsFrom(new File("alps.txt"));
        ImagePainter painter;
        ContinuousElevationModel dem;
        //PanoramaComputerBean parnoramaComputerBean = new PanoramaComputerBean(parametersBean.getParameters(), summits, painter, dem);
        StackPane panoPane = panoPane();
        GridPane paramsGrid = paramsGrid(parametersBean);
        BorderPane root = new BorderPane(panoPane, null, null, paramsGrid, null);
        Scene scene = new Scene(root);

        primaryStage.setTitle("Alpano");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private GridPane paramsGrid(PanoramaParametersBean parametersBean) {
        // TODO Auto-generated method stub
        GridPane grid = new GridPane();
        Label latitudeLabel = new Label("Latitude (°) :");
        Label longitudeLabel = new Label("Longitude (°) :");
        Label altitudeLabel = new Label("Altitude (m) :");
        Label azimuthLabel = new Label("Azimuth (°) :");
        Label angleLabel = new Label("Angle de vue(°) :");
        Label visibilityLabel = new Label("Visibilité (km) :");
        Label widthLabel = new Label("Largeur (px) :");
        Label heightLabel = new Label("Hauteur (px) :");
        Label superSamplingLabel = new Label("Suréchantillonage :");
        
        grid.addColumn(0, latitudeLabel, azimuthLabel, widthLabel);
        grid.addColumn(2, longitudeLabel, angleLabel, heightLabel);
        grid.addColumn(4, altitudeLabel, visibilityLabel, superSamplingLabel);
        
        TextField latitudeText = new TextField();
        TextField longitudeText = new TextField();
        TextField altitudeText = new TextField();
        TextField azimuthText = new TextField();
        TextField angleText = new TextField();
        TextField visibilityText = new TextField();
        TextField widthText = new TextField();
        TextField heightText = new TextField();
        
        
        StringConverter<Integer> stringConverter = new FixedPointStringConverter(4);
        TextFormatter<Integer> latFormatter = new TextFormatter<>(stringConverter);
        latFormatter.valueProperty().bindBidirectional(parametersBean.observerLatitudeProperty());
        latitudeText.setTextFormatter(latFormatter);
        TextFormatter<Integer> lonFormatter = new TextFormatter<>(stringConverter);
        lonFormatter.valueProperty().bindBidirectional(parametersBean.observerLongitudeProperty());
        longitudeText.setTextFormatter(lonFormatter);
        /*
        StringConverter<Integer> stringConverter2 = new FixedPointStringConverter(0);
        TextFormatter<Integer> formatter2 = new TextFormatter<>(stringConverter2);
        altitudeText.setTextFormatter(formatter2);
        azimuthText.setTextFormatter(formatter2);
        angleText.setTextFormatter(formatter2);
        visibilityText.setTextFormatter(formatter2);
        widthText.setTextFormatter(formatter2);
        heightText.setTextFormatter(formatter2);*/
        
        grid.addColumn(1, latitudeText, azimuthText, widthText);
        grid.addColumn(3, longitudeText, angleText, heightText);
        grid.addColumn(5, altitudeText, visibilityText);



        return grid;
    }

    private StackPane panoPane() {
        // TODO Auto-generated method stub
        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
