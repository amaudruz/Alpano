package ch.epfl.alpano.gui;


import javafx.util.StringConverter;

import java.io.File;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import static javafx.geometry.Pos.*; 



public class Alpano extends Application {

    private PanoramaParametersBean parametersBean;
    private PanoramaComputerBean computerBean;

    public Alpano() throws Exception {
        List<Summit> summits = GazetteerParser.readSummitsFrom(new File("alps.txt"));  
        ContinuousElevationModel dem = createDem();
        parametersBean = new PanoramaParametersBean(PredefinedPanoramas.NIESEN);
        computerBean = new PanoramaComputerBean(PredefinedPanoramas.NIESEN, summits, dem);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        
        StackPane panoPane = panoPane();
        GridPane paramsGrid = paramsGrid();
        BorderPane root = new BorderPane(panoPane, null, null, paramsGrid, null);
        Scene scene = new Scene(root);

        primaryStage.setTitle("Alpano");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private ContinuousElevationModel createDem() throws Exception {
        DiscreteElevationModel dem1 = new HgtDiscreteElevationModel(new File("N45E006.hgt")).
                union(new HgtDiscreteElevationModel(new File("N45E007.hgt"))).
                union(new HgtDiscreteElevationModel(new File("N45E008.hgt"))).
                union(new HgtDiscreteElevationModel(new File("N45E009.hgt")));
        DiscreteElevationModel dem2 = new HgtDiscreteElevationModel(new File("N46E006.hgt")).
                union(new HgtDiscreteElevationModel(new File("N46E007.hgt"))).
                union(new HgtDiscreteElevationModel(new File("N46E008.hgt"))).
                union(new HgtDiscreteElevationModel(new File("N46E009.hgt")));
        
        return new ContinuousElevationModel(dem1.union(dem2));
    }

    private GridPane paramsGrid() {
        GridPane grid = new GridPane();
        
        labels(grid);
        
        TextField latitudeText = createWithColumn(7);
        TextField longitudeText = createWithColumn(7);
        TextField altitudeText = createWithColumn(4);
        TextField azimuthText = createWithColumn(3);
        TextField angleText = createWithColumn(3);
        TextField visibilityText = createWithColumn(3);
        TextField widthText = createWithColumn(4);
        TextField heightText = createWithColumn(4);
        
        StringConverter<Integer> stringConverter = new FixedPointStringConverter(4);
        
        TextFormatter<Integer> latFormatter = new TextFormatter<>(stringConverter);
        latFormatter.valueProperty().bindBidirectional(parametersBean.observerLatitudeProperty());
        latitudeText.setTextFormatter(latFormatter);
        
        TextFormatter<Integer> lonFormatter = new TextFormatter<>(stringConverter);
        lonFormatter.valueProperty().bindBidirectional(parametersBean.observerLongitudeProperty());
        longitudeText.setTextFormatter(lonFormatter);
        
        StringConverter<Integer> stringConverter2 = new FixedPointStringConverter(0);
        
        TextFormatter<Integer> altFormatter = new TextFormatter<>(stringConverter2);
        altFormatter.valueProperty().bindBidirectional(parametersBean.observerElevationProperty());
        altitudeText.setTextFormatter(altFormatter);
        
        TextFormatter<Integer> aziFormatter = new TextFormatter<>(stringConverter2);
        aziFormatter.valueProperty().bindBidirectional(parametersBean.centerAzimuthProperty());
        azimuthText.setTextFormatter(aziFormatter);
        
        TextFormatter<Integer> angFormatter = new TextFormatter<>(stringConverter2);
        angFormatter.valueProperty().bindBidirectional(parametersBean.horizontalFieldOfViewProperty());
        angleText.setTextFormatter(angFormatter);
        
        TextFormatter<Integer> visFormatter = new TextFormatter<>(stringConverter2);
        visFormatter.valueProperty().bindBidirectional(parametersBean.maxDistanceProperty());
        visibilityText.setTextFormatter(visFormatter);
        
        TextFormatter<Integer> widFormatter = new TextFormatter<>(stringConverter2);
        widFormatter.valueProperty().bindBidirectional(parametersBean.widthProperty());
        widthText.setTextFormatter(widFormatter);
        
        TextFormatter<Integer> heiFormatter = new TextFormatter<>(stringConverter2);
        heiFormatter.valueProperty().bindBidirectional(parametersBean.heightProperty());
        heightText.setTextFormatter(heiFormatter);
        
        ObservableList<Integer> list = FXCollections.observableArrayList(Arrays.asList(0,1,2));
        ChoiceBox<Integer> superSampling = new ChoiceBox<>(list);
        StringConverter<Integer> converter = new LabeledListStringConverter("non", "2x", "4x");
        //TextFormatter<Integer> supFormatter = new TextFormatter<>(converter);
        superSampling.setConverter(converter);
        superSampling.valueProperty().bindBidirectional(parametersBean.superSamplingExponentProperty());
        
        TextArea text = new TextArea();
        text.setEditable(false);
        text.setPrefRowCount(2);
        
        grid.addColumn(1, latitudeText, azimuthText, widthText);
        grid.addColumn(3, longitudeText, angleText, heightText);
        grid.addColumn(5, altitudeText, visibilityText, superSampling);
        grid.add(text, 7, 0, 1, 3);



        return grid;
    }

    private void labels(GridPane grid) {
        
        Map<UserParameter, Label> map = new EnumMap<>(UserParameter.class);
        
        Label latitudeLabel = new Label("Latitude (°) :");
        Label longitudeLabel = new Label("Longitude (°) :");
        Label altitudeLabel = new Label("Altitude (m) :");
        Label azimuthLabel = new Label("Azimuth (°) :");
        Label angleLabel = new Label("Angle de vue(°) :");
        Label visibilityLabel = new Label("Visibilité (km) :");
        Label widthLabel = new Label("Largeur (px) :");
        Label heightLabel = new Label("Hauteur (px) :");
        Label superSamplingLabel = new Label("Suréchantillonage :");
        
        GridPane.setHalignment(heightLabel, HPos.RIGHT);
        grid.addColumn(0, latitudeLabel, azimuthLabel, widthLabel);
        grid.addColumn(2, longitudeLabel, angleLabel, heightLabel);
        grid.addColumn(4, altitudeLabel, visibilityLabel, superSamplingLabel);
    }

    private TextField createWithColumn(int i) {
        TextField field = new TextField();
        field.setPrefColumnCount(i);
        field.setAlignment(CENTER_RIGHT);
        return field;
    }

    private StackPane panoPane() {
        StackPane panoGroup = new StackPane(panoView(), labelsPane());
        ScrollPane panoScrollPane = new ScrollPane(panoGroup);
        return new StackPane(panoScrollPane, updateNotice());
    }

    private StackPane updateNotice() {
        Font font = new Font(40);
        Text text = new Text("Les paramètres du panorama ont changé. Cliquez ici pour mettre le dessin à jour.");
        text.setFont(font);
        text.setTextAlignment(TextAlignment.CENTER);
        StackPane updateNotice = new StackPane(text);
        updateNotice.setBackground(new Background(new BackgroundFill(new Color(1, 1, 1, 0.9), CornerRadii.EMPTY, Insets.EMPTY)));
        updateNotice.visibleProperty().bind(computerBean.parametersProperty().isNotEqualTo(parametersBean.parametersProperty()));
        
        updateNotice.setOnMouseClicked(x -> {
            if(updateNotice.isVisible()) {
                computerBean.setParameters(parametersBean.getParameters());
            }
        });
        return updateNotice;
    }

    private Pane labelsPane() {
        Pane labelsPane = new Pane();
        labelsPane.prefWidthProperty().bind(parametersBean.widthProperty());
        labelsPane.prefHeightProperty().bind(parametersBean.heightProperty());
        Bindings.bindContent(labelsPane.getChildren(), computerBean.getLabels());
        
        return labelsPane;
    }

    private ImageView panoView() {
        ImageView imageView = new ImageView();
        imageView.imageProperty().bind(computerBean.imageProperty());
        imageView.fitWidthProperty().bind(parametersBean.widthProperty());
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        return imageView;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
