package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class ReadFileTask extends Task<ObservableList<String>> {
    private File file;
    private ObservableList<String> rowList;

    public ReadFileTask(File file) {
        this.file = file;
        this.rowList = FXCollections.observableArrayList();
    }

    @Override
    protected ObservableList<String> call() throws Exception {
        //ObservableList<String> rowList = FXCollections.observableArrayList();
        int count = (int) file.length();
        int i = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader
                (new FileInputStream(file.getAbsoluteFile()),"cp1251"));

        String line = reader.readLine();
        while (line != null){
                line = reader.readLine();
                if (line != null)
                    rowList.add(line);
        }
        //rowList.remove(null);

        this.updateProgress(i, count);

        return rowList;
    }


    private void copy(File file) throws Exception {
        this.updateMessage("Copying: " + file.getAbsolutePath());
        //Thread.sleep(500);
    }

    public ObservableList<String> getRowList(){
        return rowList;
    }
}
