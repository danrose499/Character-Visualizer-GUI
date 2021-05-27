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
    String filename; // file name of file to be searched
    int totalChars;  // Total characters in file to be searched
    Map<String, Double> sortedFrequency; // Sorted frequency map of characters in the file

    HistogramAlphaBet(String filename) throws IOException {
        this.filename = filename;
        getMap();
    }

    public void getMap() throws IOException {
        Path fileName = Path.of(filename);
        String actual = Files.readString(fileName);
        String s = actual.replaceAll("[^a-zA-Z]", "").toLowerCase();
        totalChars = s.length();

        Map<String, Double> freq2 = new HashMap<>();
        for(int i = 0; i < totalChars; i++) {
            String Ch = Character.toString(s.charAt(i));
            incrementFrequency(freq2, Ch);
        }

        this.sortedFrequency = freq2
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    public static <K> void incrementFrequency(Map<K, Double> m, K Key) {
        m.putIfAbsent(Key, 0.00);
        m.put(Key, m.get(Key) + 1.00);
    }

    public PieChart drawPieChart(int SlicesToPrint) {
        //ObservableList<PieChart.Data> pieChartData = null;
        ObservableList pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Double> mapElement : sortedFrequency.entrySet()) {
            if(SlicesToPrint-- > 0)
                pieChartData.add(new PieChart.Data(mapElement.getKey(), mapElement.getValue()));
        }
        return new PieChart(pieChartData);

    }

    public void updateFile(String newFile) throws IOException {
        this.filename = newFile;
        getMap();
    }
}
