package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import utils.TextFinder;

import java.io.File;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class FileTableView {

    private ObservableList<TabTableInfoFiles> tabTableInfoFiles = FXCollections.observableArrayList();
    private TableView<TabTableInfoFiles> table = new TableView<TabTableInfoFiles>(tabTableInfoFiles);

    public FileTableView() {
        TableColumn<TabTableInfoFiles, String> nameColumn = new TableColumn<TabTableInfoFiles, String>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<TabTableInfoFiles, String>("name"));
        table.getColumns().add(nameColumn);

        TableColumn<TabTableInfoFiles, String> folderColumn = new TableColumn<TabTableInfoFiles, String>("Folder");
        folderColumn.setCellValueFactory(new PropertyValueFactory<TabTableInfoFiles, String>("folder"));
        table.getColumns().add(folderColumn);

        TableColumn<TabTableInfoFiles, Integer> numOfCoincidencesColumn = new TableColumn<TabTableInfoFiles, Integer>("Coincidences");
        numOfCoincidencesColumn.setCellValueFactory(new PropertyValueFactory<TabTableInfoFiles, Integer>("numOfCoincidences"));
        table.getColumns().add(numOfCoincidencesColumn);

        TableColumn<TabTableInfoFiles, String> sizeColumn = new TableColumn<TabTableInfoFiles, String>("Size");
        sizeColumn.setCellValueFactory(new PropertyValueFactory<TabTableInfoFiles, String>("size"));
        table.getColumns().add(sizeColumn);

        nameColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.4));
        folderColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.4));
        numOfCoincidencesColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        sizeColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.09));
    }

    public void setRows(File[] files, Map<File, TextFinder> searchFiles){
        for (File f: files){
            if (searchFiles.containsKey(f))
                tabTableInfoFiles.add(new TabTableInfoFiles(f,searchFiles.get(f)));
        }
    }

    public void addRow(TabTableInfoFiles tabTableInfoFiles){
        this.tabTableInfoFiles.add(tabTableInfoFiles);
    }

    public File getFileTable(){
        AtomicReference<File> file = null;
        table.setOnMouseClicked((MouseEvent e) -> {
            if(e.getClickCount() == 2)
                if (table.getSelectionModel().getSelectedItem() != null) {
                    {
                        System.out.println(table.getSelectionModel().getSelectedItem().getName());
                        file.set(table.getSelectionModel().getSelectedItem().getFile());
                    }
                }
        });
        return file.get();
    }

    public TableView<TabTableInfoFiles> getTable(){
        return table;
    }

    public ObservableList<TabTableInfoFiles> getTabTableInfoFiles() { return tabTableInfoFiles; }
}
