package utils;

import controllers.TreeViewController;
import javafx.concurrent.Task;
import javafx.scene.control.TreeItem;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SearchTask extends Task<Void> {
    private Map<File, TextFinder> searchFiles = new HashMap<>();
    private Filter filter;
    private String textSearch;
    private long start;

    private TreeViewController treeViewController;

    private File dir;
    TreeItem<File> parent;


    public SearchTask(TreeViewController treeViewController, String ext, String textSearch, long start) {
        this.start = start;

        filter = new Filter(ext);
        this.textSearch = textSearch;

        this.treeViewController = treeViewController;
    }

    @Override
    protected Void call() throws Exception {
        recursiveSearch(dir, parent);

        return null;
    }

    private void recursiveSearch(File dir, TreeItem<File> parent) throws IOException {
        TreeItem<File> root = new TreeItem<>(dir);
        root.setExpanded(true);
        int i = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (filter.accept(file)) {
                TextFinder textFinder = new TextFinder(file, textSearch);
                if (textFinder.isDetected()) {
                    searchFiles.put(file, textFinder);
                    treeViewController.addFile(file);
                }
            }
            if (file.isDirectory()) {
                recursiveSearch(file, root);
            }
        }
        if (parent == null) {
            //fileTree.setRoot(root);
        } else {
            parent.getChildren().add(root);
        }
        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;
        this.updateMessage("Time: "+timeConsumedMillis+ " MilSec!");
    }

    public void setRecursiveSearch(File dir, TreeItem<File> parent){
        this.dir = dir;
        this.parent = parent;
    }

    public TextFinder getSearchFile(File file) {
        return searchFiles.get(file);
    }

    public Map<File, TextFinder> getSearchFiles() {
        return searchFiles;
    }
}
