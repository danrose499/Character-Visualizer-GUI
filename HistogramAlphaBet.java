package sample;

import javafx.scene.canvas.GraphicsContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import java.util.Map;
import static java.util.stream.Collectors.toMap;


public class HistogramAlphaBet {
    String filename; // file name of file to be searched
    int totalChars;  // Total characters in file to be searched
    Map<Character, Integer> sortedFrequency; // Sorted frequency map of characters in the file

    HistogramAlphaBet(String filename) throws IOException {
        this.filename = filename;
        getMap();
    }

    public void getMap() throws IOException {
        Path fileName = Path.of(filename);
        String actual = Files.readString(fileName);
        String s = actual.replaceAll("[^a-zA-Z]", "").toLowerCase();
        totalChars = s.length();

        Map<Character, Integer> frequency = new HashMap<>();

        for(int i = 0; i < totalChars; i++) {
            Character Ch = s.charAt(i);
            incrementFrequency(frequency, Ch);
        }

        this.sortedFrequency = frequency
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));


    }

    public static <K> void incrementFrequency(Map<K, Integer> m, K Key) {
        m.putIfAbsent(Key, 0);
        m.put(Key, m.get(Key) + 1);
    }

    public PieChart drawPieChart(int SlicesToPrint) {
//        int cWidth = (int) GC.getCanvas().getWidth();
//        int cHeight = (int) GC.getCanvas().getHeight();
//        double d = (3.0 * (Math.min(cWidth, cHeight) / 4.0))-50;
        //MyPoint arcPoint = new MyPoint(cWidth / 2 - (int) d / 2, cHeight / 2 - (int) d / 2);
        //PieChart charChart = new PieChart(arcPoint, d);
        //charChart.draw(GC, totalChars, SlicesToPrint, sortedFrequency);


        ObservableList<PieChart.Data> pieChartData = null;
        for (Map.Entry<Character, Integer> mapElement : sortedFrequency.entrySet()) {
            //if(SlicesToPrint-- > 0)
                pieChartData.add(new PieChart.Data(String.valueOf(mapElement.getKey()), mapElement.getValue()));
        }
        return new PieChart(pieChartData);
    }

//    public void recount(String newFile) throws IOException {
//        this.filename = newFile;
//        getMap();
//    }
}
