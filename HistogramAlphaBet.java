package sample;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;

import static java.util.stream.Collectors.toMap;

public class HistogramAlphaBet {
    private String filename;                     // file name of file to be searched
    private int totalChars;                      // Total characters in file to be searched
    private Map<String, Double> freq;            // Unsorted frequency map of characters in the file
    private Map<String, Double> sortedFrequency; // Sorted frequency map of characters in the file
    private Map<String, Double> alphaFrequency;  // Alphabetized frequency map of characters in the file

    HistogramAlphaBet(String filename) throws IOException {
        this.filename = filename;
        getMaps();
    }

    private void getMaps() throws IOException {
        Path fileName = Path.of(filename);
        String actual = Files.readString(fileName);
        String string = actual.replaceAll("[^a-zA-Z]", "").toUpperCase(); // Only keep letters and capitalize them
        totalChars = string.length();

        this.freq = new HashMap<>();
        for(int i = 0; i < totalChars; i++) {
            String Ch = Character.toString(string.charAt(i));
            incrementFrequency(freq, Ch);
        }

        this.sortedFrequency = freq
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        this.alphaFrequency = freq
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    private static <K> void incrementFrequency(Map<K, Double> map, K Key) {
        map.putIfAbsent(Key, 0.0);
        map.put(Key, map.get(Key) + 1.0);
    }

    public Chart drawChart(int CharsToPrint, Boolean alphabetize, Boolean displayFrequency, Boolean isPieChart) {
        Map<String, Double> temp;
        if(alphabetize)
            temp = alphaFrequency;
        else
            temp = sortedFrequency;
        /* When returning a PieChart */
        if(isPieChart){
            if(CharsToPrint == 0)
                return new PieChart();
            ObservableList pieChartData = FXCollections.observableArrayList();

            for (Map.Entry<String, Double> mapElement : temp.entrySet()) {
                if(CharsToPrint-- > 0){
                    if(displayFrequency)
                        pieChartData.add(new PieChart.Data(mapElement.getKey() + ": " + mapElement.getValue().intValue(), mapElement.getValue()));
                    else
                        pieChartData.add(new PieChart.Data(mapElement.getKey() + ": " + Math.round(100.0 * mapElement.getValue() / (double) totalChars) / 1.0 + "%", mapElement.getValue()));
                }
            }
            PieChart pieChart = new PieChart(pieChartData);
            pieChart.setClockwise(false);
            return pieChart;
        }
        
        /* When returning a BarChart */
        else{
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();
            if(CharsToPrint == 0)
                return new BarChart<>(xAxis, yAxis);
            if(displayFrequency)
                yAxis.setLabel("Frequency");
            else
                yAxis.setLabel("Percent");
            xAxis.setLabel("Letter");
            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            XYChart.Series series = new XYChart.Series();

            for (Map.Entry<String, Double> mapElement : temp.entrySet()) {
                if(CharsToPrint-- > 0){
                    if(displayFrequency)
                        series.getData().add(new XYChart.Data(mapElement.getKey(), mapElement.getValue()));
                    else
                        series.getData().add(new XYChart.Data(mapElement.getKey(), Math.round(100.0 * mapElement.getValue() / (double) totalChars) / 1.0));
                }
            }
            barChart.getData().addAll(series);
            return barChart;
        }
    }
}
