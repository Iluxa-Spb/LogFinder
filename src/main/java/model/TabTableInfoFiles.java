package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import utils.TextFinder;

import java.io.File;

public class TabTableInfoFiles {
    private File file;

    private SimpleStringProperty name;
    private SimpleStringProperty folder;
    private SimpleIntegerProperty numOfCoincidences;
    private SimpleLongProperty size;

    public TabTableInfoFiles(File file, TextFinder textFinder) {
        this.file = file;
        this.name = new SimpleStringProperty(file.getName());
        this.folder = new SimpleStringProperty(file.getAbsolutePath());
        this.numOfCoincidences = new SimpleIntegerProperty(textFinder.getNumRowDetect().size());
        this.size = new SimpleLongProperty(file.length());
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getFolder() {
        return folder.get();
    }

    public SimpleStringProperty folderProperty() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder.set(folder);
    }

    public int getNumOfCoincidences() {
        return numOfCoincidences.get();
    }

    public SimpleIntegerProperty numOfCoincidencesProperty() {
        return numOfCoincidences;
    }

    public void setNumOfCoincidences(int numOfCoincidences) {
        this.numOfCoincidences.set(numOfCoincidences);
    }

    public File getFile() { return file; }

    public void setFile(File file) { this.file = file; }

    public long getSize() { return size.get(); }

    public SimpleLongProperty sizeProperty() { return size; }

    public void setSize(long size) { this.size.set(size); }
}
