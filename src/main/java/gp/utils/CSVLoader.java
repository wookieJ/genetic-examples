package gp.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CSVLoader {
    public static List<List<Double>> loadFromFile(String path) {
        List<List<Double>> records = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(path))) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.stream(values)
                        .map((value) -> {
                            value = value.replaceAll(",", ".");
                            return Double.valueOf(value);
                        })
                        .collect(Collectors.toList())
                );
            }
        } catch (CsvValidationException | IOException e) {
            e.printStackTrace();
        }
        return records;
    }
}
