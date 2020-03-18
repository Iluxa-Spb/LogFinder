package utils;

import javafx.scene.control.TreeItem;

import java.io.File;
import java.io.IOException;

public class FileFinder {
    public FileFinder() {
    }

    void find(File dir, TreeItem<File> parent) {
        TreeItem<File> root = new TreeItem<>(dir);
        root.setExpanded(true);
        try {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    System.out.println("directory:" + file.getCanonicalPath());
                    //treeViewController.addFolder(file);
                    find(file, root);
                } else {
                    System.out.println("     file:" + file.getCanonicalPath());
                    root.getChildren().add(new TreeItem<>(file));
                }

            }
            if (parent == null) {
                //fileTree.setRoot(root);
            } else {
                parent.getChildren().add(root);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}