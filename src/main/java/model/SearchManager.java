package model;

import controllers.OutController;
import controllers.TreeViewController;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import utils.Filter;
import utils.SearchTask;
import utils.TextFinder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SearchManager {
    private SearchTask searchTask;
    private OutController outController;

    public SearchManager(TreeViewController treeViewController, String ext, String textSearch, OutController outController) {
        long start = System.currentTimeMillis();
        searchTask = new SearchTask(treeViewController, ext, textSearch, start);

        searchTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                long finish = System.currentTimeMillis();
                long timeConsumedMillis = finish - start;
                System.out.println("Compile searchTask! Time: "+ timeConsumedMillis);
                outController.getInfoLabel().textProperty().unbind();
                outController.getInfoLabel().setText("Search complete in "+ timeConsumedMillis + " MilSec!");
                outController.getProgressIndicator().visibleProperty().setValue(false);
                outController.getButtonOk().setDisable(false);
            }
        });
        new Thread(searchTask).start();
    }

    public void runSearch(File dir, TreeItem<File> parent){
        searchTask.setRecursiveSearch(dir, parent);
    }

    public TextFinder getSearchFile(File file) {
        return searchTask.getSearchFile(file);
    }

    public Map<File, TextFinder> getSearchFiles() {
        return searchTask.getSearchFiles();
    }

    public SearchTask getSearchTask(){ return searchTask;}
}
