package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.chart.*;
import javafx.scene.Group;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main extends Application {

    String filename;
    int slices = 26;
    Boolean alphabetize = false;
    Boolean isPieChart = true;
    Boolean freqDisplay = true;
    Text errorMSG;
    TextField numSlices;
    TextField inputFile;
    Chart charChart;
    Scene scene;
    BorderPane BP;
    HBox upperHBox;
    HBox lowerHBox;
    HBox radioHBox;
    VBox lowerBox;
    VBox upperBox;
    VBox sortSelect;
    VBox graphSelect;
    VBox labelSelect;
    HistogramAlphaBet histogram;

    @Override public void start(Stage stage) {
        /* Setup scene: */
        scene = new Scene(new Group());
        stage.setTitle("Character Frequency Pie Chart");
        stage.setWidth(500);
        stage.setHeight(580);
        BP = new BorderPane();

        /* Setup Upper Pane (upperBox): */
        Text upperText = new Text("Pie Chart of file: ");
        inputFile = new TextField();
        inputFile.setPrefWidth(250);
        inputFile.setOnAction(event -> {
            try {
                processFileName();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        upperHBox = new HBox(upperText, inputFile);
        upperHBox.setAlignment(Pos.BASELINE_CENTER);
        upperHBox.setStyle("-fx-padding: 10;");
        errorMSG = new Text();
        errorMSG.setFill(Color.RED);
        upperBox = new VBox(upperHBox, errorMSG);
        upperBox.setAlignment(Pos.TOP_CENTER);

        /* Setup Middle Pane (Pie charChart) */
        charChart = new PieChart();

        /* Setup lowerBox HBox */
        Text lowerText = new Text("Enter # of Letter Frequencies to Display: ");
        numSlices = new TextField();
        numSlices.setPrefWidth(50);
        numSlices.setAlignment(Pos.CENTER);
        numSlices.setOnAction(this::processSliceInput);

        lowerHBox = new HBox(lowerText, numSlices);
        lowerHBox.setAlignment(Pos.BASELINE_CENTER);

        /* Setup graphSelect RadioButton */
        ToggleGroup graphTG = new ToggleGroup();
        RadioButton pie = new RadioButton("Pie Chart ");
        pie.setToggleGroup(graphTG);
        pie.setSelected(true);
        RadioButton bar = new RadioButton("Bar Chart ");
        bar.setToggleGroup(graphTG);
        graphSelect = new VBox(pie, bar);
        pie.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            isPieChart = isNowSelected;
            charChart = histogram.drawPieChart(slices, alphabetize, freqDisplay, isPieChart);
            resetChart(charChart);
        });

        /* Setup sortSelect RadioButton */
        ToggleGroup sortTG = new ToggleGroup();
        RadioButton freqSort = new RadioButton("Sort By Frequency ");
        freqSort.setToggleGroup(sortTG);
        freqSort.setSelected(true);
        RadioButton alphaSort = new RadioButton("Sort Alphabetically ");
        alphaSort.setToggleGroup(sortTG);
        sortSelect = new VBox(freqSort, alphaSort);
        alphaSort.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            alphabetize = isNowSelected;
            charChart = histogram.drawPieChart(slices, alphabetize, freqDisplay, isPieChart);
            resetChart(charChart);
        });

        /* Setup LabelSelect RadioButton  */
        ToggleGroup labelTG = new ToggleGroup();
        RadioButton displayFrequency = new RadioButton("Display Frequency ");
        displayFrequency.setToggleGroup(labelTG);
        displayFrequency.setSelected(true);
        RadioButton displayPercent = new RadioButton("Display Percent ");
        displayPercent.setToggleGroup(labelTG);
        labelSelect = new VBox(displayFrequency, displayPercent);
        displayFrequency.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            freqDisplay = isNowSelected;
            charChart = histogram.drawPieChart(slices, alphabetize, freqDisplay, isPieChart);
            resetChart(charChart);
        });

        /* Setup Bottom Pane (lowerBox) */
        radioHBox = new HBox(graphSelect, sortSelect, labelSelect);
        radioHBox.setAlignment(Pos.BASELINE_CENTER);
        radioHBox.setStyle("-fx-padding: 10;");
        lowerBox = new VBox(lowerHBox, radioHBox);

        /* Finalize Scene */
        resetChart(charChart);
        stage.setScene(scene);
        stage.show();
    }

    private void resetChart(Chart newChart) {
//        newChart.getData().forEach(data -> {
//            String percentage = String.format("%.2f%%", data.getPieValue());
//            Tooltip toolTip = new Tooltip(percentage);
//            Tooltip.install(data.getNode(), toolTip);
//        });

        newChart.setLegendVisible(false);
        ((Group) scene.getRoot()).getChildren().clear();
        BP.setTop(upperBox);
        BP.setBottom(lowerBox);
        BP.setCenter(newChart);
        ((Group) scene.getRoot()).getChildren().add(BP);
    }

    private void processFileName() throws IOException {
        filename = inputFile.getText();
        Path path = Path.of(filename);
        if(Files.notExists(path)) {
            errorMSG.setText("Error: \"" + filename + "\" does not exist!");
            errorMSG.setVisible(true);
        }
        else {
            errorMSG.setVisible(false);
            histogram = new HistogramAlphaBet(filename);
            charChart = histogram.drawPieChart(slices, alphabetize, freqDisplay, isPieChart);
        }
        resetChart(charChart);
    }

    private void processSliceInput(ActionEvent event) {
        slices = Integer.parseInt(numSlices.getText());
        charChart = histogram.drawPieChart(slices, alphabetize, freqDisplay, isPieChart);
        resetChart(charChart);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
