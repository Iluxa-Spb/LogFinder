package utils;

import java.io.*;
import java.util.*;

public class TextFinder {
    private List<Integer> numRowDetect = new ArrayList<>();
    private ListIterator iterator;
    private File file;
    private Boolean isDetected = false;
    private int count = 0;

    public TextFinder(File file) throws IOException {
        this.file = file;
    }

    public TextFinder(File file, String search) throws IOException {
        this.file = file;
        search(search);
    }

    public void search(String search) throws IOException {
        count = 0;
        isDetected = false;

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsoluteFile()),"cp1251"));

        String line = reader.readLine();
        if (line.contains(search))
            numRowDetect.add(count);
        count++;

        while (line != null){
            line = reader.readLine();
            if (line != null)
                if (line.contains(search))
                    numRowDetect.add(count);
            count++;
        }
        if (!numRowDetect.isEmpty()) isDetected = true;
        iterator = numRowDetect.listIterator();
    }

    public List<Integer> getNumRowDetect() {
        return numRowDetect;
    }

    public File getFile() {
        return file;
    }

    public Boolean isDetected() {
        return isDetected;
    }

    public int getCount() {
        return count;
    }

    public ListIterator getIterator() {
        return iterator;
    }
}
