package controllers;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utils.TreeItemComparator;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TreeViewController {
    private Map<File, TreeItem<String>> folders = new HashMap<>();
    private Map<TreeItem<String>, File> files = new HashMap<>();
    private TreeItemComparator comparator = new TreeItemComparator();

    private Image iconFolders = new Image(getClass().getResourceAsStream("/pic/cloud.png"));
    private Image iconFilesDone = new Image(getClass().getResourceAsStream("/pic/checked.png"));

    public TreeViewController(TreeView<String> treeView, File mainFolder) {
        treeView.setRoot(new TreeItem<>(mainFolder.getName()));
        folders.put(mainFolder, treeView.getRoot());
    }

    public void addFile(File f) {
        File parent = f.getParentFile();
        TreeItem<String> newNode = new TreeItem<>(f.getName(), new ImageView(iconFilesDone));
        TreeItem<String> parentNode;
        if (folders.containsKey(parent)) {
            parentNode = folders.get(parent);
            parentNode.getChildren().add(newNode);
        } else parentNode = addFolder(parent, newNode);
        parentNode.getChildren().sort(comparator);
        files.put(newNode, f);
    }

    private TreeItem<String> addFolder(File f, TreeItem<String> nextNode) {
        File parent = f.getParentFile();
        TreeItem<String> newNode = new TreeItem<>(f.getName(), new ImageView(iconFolders));
        newNode.getChildren().add(nextNode);
        TreeItem<String> parentNode;
        if (folders.containsKey(parent)) {
            parentNode = folders.get(parent);
            parentNode.getChildren().add(newNode);
        } else parentNode = addFolder(parent, newNode);
        parentNode.getChildren().sort(comparator);
        folders.put(f, newNode);
        return newNode;
    }

    public TreeItem<String> addFolder(File f) {
        File parent = f.getParentFile();
        TreeItem<String> newNode = new TreeItem<>(f.getName(), new ImageView(iconFolders));
        TreeItem<String> parentNode;
        if (folders.containsKey(parent)) {
            parentNode = folders.get(parent);
            parentNode.getChildren().add(newNode);
        } else parentNode = addFolder(parent, newNode);
        parentNode.getChildren().sort(comparator);
        folders.put(f, newNode);
        return newNode;
    }

    public File getFile(TreeItem<String> item) {
        return files.get(item);
    }

    public File getFolder(TreeItem<String> item){

        for (Map.Entry<File, TreeItem<String>> entry : folders.entrySet()) {
            if (entry.getValue() == item) {
                return (File) entry.getKey();
            }
        }
        return null;
    }

    public boolean isFile(TreeItem<String> item){
        if (files.containsKey(item)) return true;
            else return false;
    }

    public boolean isFolder(TreeItem<String> item){
        if (folders.containsValue(item)) return true;
        else return false;
    }
}
