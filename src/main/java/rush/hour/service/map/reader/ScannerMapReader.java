package rush.hour.service.map.reader;

import rush.hour.service.exceptions.MapReaderException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ScannerMapReader implements MapReaderInterface{
    private final Scanner scanner;

    public ScannerMapReader(Scanner scanner){
        this.scanner = scanner;
    }

    @Override
    public List<String> readMap() throws MapReaderException {
        scanner.useDelimiter("\\n");
        List<String> result = new ArrayList<>();
        while (scanner.hasNext()){
            result.add(scanner.nextLine());
        }

        return  result;
    }
}
