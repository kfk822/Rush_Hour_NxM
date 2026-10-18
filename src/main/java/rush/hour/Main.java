package rush.hour;

import rush.hour.model.MapVO;
import rush.hour.service.exceptions.MapReaderException;
import rush.hour.service.map.parser.MapParser;
import rush.hour.service.map.reader.ScannerMapReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Scanner mapScanner = new Scanner(Main.class.getClassLoader().getResourceAsStream("map.txt"));
        ScannerMapReader scannerMapReader = new ScannerMapReader(mapScanner);

        try {


            List<String> rawMap = scannerMapReader.readMap();

            MapParser mapParser = new MapParser(rawMap.size(), rawMap.get(0).length());

            MapVO map = mapParser.parseMap(rawMap);

            System.out.println(map);
        }
        catch (MapReaderException e){
            System.out.println(e.getMessage());
        }



    }
}
