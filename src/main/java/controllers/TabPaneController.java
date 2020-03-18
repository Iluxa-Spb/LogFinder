package controllers;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import model.FileTableView;
import model.TextFileView;
import utils.FileFinder;
import utils.TextFinder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TabPaneController {
    private Map<Tab, FileTableView> folders = new HashMap<>();
    private Map<Tab, TextFileView> files = new HashMap<>();

    public TabPaneController (){
    }

    public Tab createTableTab(TabPane tabPane, File[] files, String tabName, Map<File, TextFinder> searchFiles){
        Tab tab = new Tab(tabName);
        FileTableView tableView = new FileTableView();
        tableView.setRows(files, searchFiles);
        tab.setContent(tableView.getTable());
        tab.setClosable(true);
        tabPane.getTabs().add(tab);
        folders.put(tab, tableView);
        return tab;
    }

    public Tab createFileTab(TabPane tabPane, File file, TextFinder finder) throws IOException, InterruptedException {
        Tab tab = new Tab(file.getName());
        TextFileView textFileView = new TextFileView(file, finder);
        tabPane.getTabs().add(tab);
        tab.setContent(textFileView.getTextListView());
        tab.setClosable(true);
        files.put(tab, textFileView);
        return tab;
    }

    public FileTableView getFileTableView(Tab tab){
        return folders.get(tab);
    }

    public TextFileView getTextFileView(Tab tab){
        return files.get(tab);
    }

    public boolean isTableView(Tab tab){
        if (folders.containsKey(tab)) return true;
        else return false;
    }

    public boolean isTextFileView(Tab tab){
        if (files.containsKey(tab)) return true;
        else return false;
    }
}
