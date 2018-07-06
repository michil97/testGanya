package ru.indriver;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Write {
    private static Write ourInstance = new Write();

    public static Write getInstance() {
        return ourInstance;
    }

    private Write() {
    }

    void Write(String csv){
        try (PrintStream out = new PrintStream(new FileOutputStream("output.csv"))) {
            out.print(csv);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
