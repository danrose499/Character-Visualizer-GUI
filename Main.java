package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.chart.*;
import javafx.scene.Group;

import java.io.IOException;

public class Main extends Application {

    String filename = "alice.txt";
    TextField numSlices;
    TextField inputFile;
    PieChart charChart;
    Scene scene;
    BorderPane BP;
    HBox upperBox;
    HBox lowerBox;
    HistogramAlphaBet histogram;

    @Override public void start(Stage stage) throws IOException {
        /* Setup scene: */
        scene = new Scene(new Group());
        stage.setTitle("Character Frequency Pie Chart");
        stage.setWidth(500);
        stage.setHeight(500);
        BP = new BorderPane();

        /* Setup Upper HBox: */
        Text upperText = new Text("Pie Chart of file: ");
        inputFile = new TextField();
        inputFile.setPrefWidth(250);
        inputFile.setOnAction(event -> {
            try {
                processFileName(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        upperBox = new HBox(upperText, inputFile);
        upperBox.setAlignment(Pos.BASELINE_CENTER);
        upperBox.setStyle("-fx-padding: 10;");

        /* Setup Pie Chart */
        histogram = new HistogramAlphaBet("alice.txt");
        charChart = histogram.drawPieChart(26);

        /* Setup Lower HBox */
        Text lowerText = new Text("Enter # of Letter Frequencies to Display: ");
        numSlices = new TextField();
        numSlices.setPrefWidth(50);
        numSlices.setAlignment(Pos.CENTER);
        numSlices.setOnAction(this::processSliceInput);

        lowerBox = new HBox(lowerText, numSlices);
        lowerBox.setAlignment(Pos.BASELINE_CENTER);

        /* Finalize Scene */
        resetChart(charChart);
        stage.setScene(scene);
        stage.show();
    }

    private void resetChart(PieChart newChart) {
//        newChart.getData().forEach(data -> {
//            String percentage = String.format("%.2f%%", data.getPieValue());
//            Tooltip toolTip = new Tooltip(percentage);
//            Tooltip.install(data.getNode(), toolTip);
//        });
        newChart.setLegendVisible(false);
        newChart.setClockwise(false);
        ((Group) scene.getRoot()).getChildren().clear();
        BP.setTop(upperBox);
        BP.setBottom(lowerBox);
        BP.setCenter(newChart);
        ((Group) scene.getRoot()).getChildren().add(BP);
    }

    private void processFileName(ActionEvent event) throws IOException {
        histogram.updateFile(inputFile.getText());
        //charChart = histogram.drawPieChart(Integer.parseInt(numSlices.getText()));
        charChart = histogram.drawPieChart(26);
        resetChart(charChart);
    }

    private void processSliceInput(ActionEvent event) {
//        int charsToDisplay = Integer.parseInt(numSlices.getText());
//        CV.getGraphicsContext2D().clearRect(0, 0, CV.getWidth(), CV.getHeight());
//        if (charsToDisplay > 26) { charsToDisplay = 26; }
//        alice.drawPieChart(CV.getGraphicsContext2D(), charsToDisplay);

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Grapefruit: " + 13, 13),
                        new PieChart.Data("Apples: " + 30, 30));
        charChart = new PieChart(pieChartData);
        resetChart(charChart);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
