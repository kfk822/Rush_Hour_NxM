package rush.hour.service.ui.reader;

import java.util.Scanner;

public class ScannerConsoleReader implements ConsoleReader{
    Scanner scanner;

    public ScannerConsoleReader(Scanner scanner){
        this.scanner = scanner;
    }

    @Override
    public String read(){
        return scanner.nextLine();

    }

}
