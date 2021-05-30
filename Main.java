package sample;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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

public class Main extends Application {

    Scene scene;
    BorderPane BP;               // BorderPane to be added to scene
    VBox lowerBox, upperBox;     // Top and Bottom panes of BP BorderPane
    Chart charChart;             // Chart displaying letter frequencies
    String filename;             // Stores name of file to read
    TextField inputFile;         // TextField to get filename
    Text errorMSG;               // Error message that displays when filename does not appear in the directory
    int lettersToDisplay = 26;   // Number of letters to display in Chart (default is to display all letters)
    TextField numLetters;        // TextField to get the number of letters to display
    Boolean alphabetize = false; // Determines whether Chart is sorted alphabetically or by frequency
    Boolean isPieChart  = true;  // Determines whether Chart is returned as a PieChart or a BarChart
    Boolean freqDisplay = true;  // Determines if labels should be letter frequency or percent
    HistogramAlphaBet histogram; // Class to get PieChart of BarChart of filename

    @Override public void start(Stage stage) {
        /* Setup scene: */
        scene = new Scene(new Group());
        stage.setTitle("Letter Frequency Chart");
        stage.setWidth(500);
        stage.setHeight(580);
        BP = new BorderPane();

        /* Setup Upper Pane (upperBox): */
        Text upperText = new Text("Enter file name: ");
        inputFile = new TextField();
        inputFile.setPrefWidth(250);
        inputFile.setOnAction(event -> {
            try {
                processFileName();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        HBox upperHBox = new HBox(upperText, inputFile);
        upperHBox.setAlignment(Pos.BASELINE_CENTER);
        upperHBox.setStyle("-fx-padding: 10;");

        errorMSG = new Text(); // errorMSG text is set in processFilename
        errorMSG.setFill(Color.RED);

        upperBox = new VBox(upperHBox, errorMSG);
        upperBox.setAlignment(Pos.TOP_CENTER);

        /* Setup Middle Pane (Pie charChart) */
        charChart = new PieChart(); // charChart is empty until user set inputFile

        /* Setup lowerBox HBox */
        Text lowerText = new Text("Enter number of Letter Frequencies to Display: ");
        numLetters = new TextField();
        numLetters.setPrefWidth(50);
        numLetters.setAlignment(Pos.CENTER);
        numLetters.setOnAction(this::processNumberInput);

        HBox lowerHBox = new HBox(lowerText, numLetters);
        lowerHBox.setAlignment(Pos.BASELINE_CENTER);

        /* Setup graphSelect RadioButton */
        ToggleGroup graphTG = new ToggleGroup();
        RadioButton pie = new RadioButton("Pie Chart ");
        pie.setToggleGroup(graphTG);
        pie.setSelected(true);
        RadioButton bar = new RadioButton("Bar Chart ");
        bar.setToggleGroup(graphTG);
        VBox graphSelect = new VBox(pie, bar);
        pie.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            isPieChart = isNowSelected;
            charChart = histogram.drawChart(lettersToDisplay, alphabetize, freqDisplay, isPieChart);
            resetChart(charChart);
        });

        /* Setup sortSelect RadioButton */
        ToggleGroup sortTG = new ToggleGroup();
        RadioButton freqSort = new RadioButton("Sort By Frequency ");
        freqSort.setToggleGroup(sortTG);
        freqSort.setSelected(true);
        RadioButton alphaSort = new RadioButton("Sort Alphabetically ");
        alphaSort.setToggleGroup(sortTG);
        VBox sortSelect = new VBox(freqSort, alphaSort);
        alphaSort.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            alphabetize = isNowSelected;
            charChart = histogram.drawChart(lettersToDisplay, alphabetize, freqDisplay, isPieChart);
            resetChart(charChart);
        });

        /* Setup LabelSelect RadioButton  */
        ToggleGroup labelTG = new ToggleGroup();
        RadioButton displayFrequency = new RadioButton("Display Frequency ");
        displayFrequency.setToggleGroup(labelTG);
        displayFrequency.setSelected(true);
        RadioButton displayPercent = new RadioButton("Display Percent ");
        displayPercent.setToggleGroup(labelTG);
        VBox labelSelect = new VBox(displayFrequency, displayPercent);
        displayFrequency.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            freqDisplay = isNowSelected;
            charChart = histogram.drawChart(lettersToDisplay, alphabetize, freqDisplay, isPieChart);
            resetChart(charChart);
        });

        /* Setup Bottom Pane (lowerBox) */
        HBox radioHBox = new HBox(graphSelect, sortSelect, labelSelect);
        radioHBox.setAlignment(Pos.BASELINE_CENTER);
        radioHBox.setStyle("-fx-padding: 10;");
        lowerBox = new VBox(lowerHBox, radioHBox);

        /* Finalize Scene */
        resetChart(charChart);
        stage.setScene(scene);
        stage.show();
    }

    private void resetChart(Chart newChart) {
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
        if(Files.notExists(path)) { // errorMSG is only displayed when file can't be found in directory
            errorMSG.setText("Error: \"" + filename + "\" does not exist!");
            errorMSG.setVisible(true);
        }
        else {
            errorMSG.setVisible(false);
            histogram = new HistogramAlphaBet(filename);
            charChart = histogram.drawChart(lettersToDisplay, alphabetize, freqDisplay, isPieChart);
        }
        resetChart(charChart);
    }

    private void processNumberInput(ActionEvent event) {
        lettersToDisplay = Integer.parseInt(numLetters.getText());
        charChart = histogram.drawChart(lettersToDisplay, alphabetize, freqDisplay, isPieChart);
        resetChart(charChart);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
