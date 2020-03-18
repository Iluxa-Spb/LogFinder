package model;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import utils.ReadFileTask;
import utils.TextFinder;

import java.io.*;
import java.util.Iterator;
import java.util.ListIterator;

public class TextFileView {
    private ListView<String> textListView;
    private TextFinder finder;

    private ReadFileTask task;

    public TextFileView(File file,TextFinder finder) throws InterruptedException {
        long start = System.currentTimeMillis();
        this.finder = finder;
        task = new ReadFileTask(file);

        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                long finish = System.currentTimeMillis();
                long timeConsumedMillis = finish - start;
                System.out.println("Compile Task! Time: "+ timeConsumedMillis);
            }
        });
        new Thread(task).start();

        textListView = new ListView<>(task.getRowList());
        textListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public ListView<String> getTextListView() {
        return textListView;
    }

    public TextFinder getFinder() { return finder; }

    public void selectAll(){
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Iterator iterator = finder.getNumRowDetect().iterator();
                textListView.getSelectionModel().clearSelection();
                while (iterator.hasNext()) {
                    textListView.getSelectionModel().select((Integer) iterator.next());
                }

                return null;
            }
        };
        new Thread(task).start();
    }

    public void selectNext(){
        ListIterator iterator = finder.getIterator();
        if (iterator.hasNext())
            textListView.getSelectionModel().clearAndSelect((Integer) iterator.next());
        textListView.scrollTo(textListView.getSelectionModel().getSelectedIndex());
    }

    public void selectPrevious(){
        ListIterator iterator = finder.getIterator();
        if (iterator.hasPrevious())
            textListView.getSelectionModel().clearAndSelect((Integer) iterator.previous());
        textListView.scrollTo(textListView.getSelectionModel().getSelectedIndex());
    }

    public ReadFileTask getTask(){
        return task;
    }
}
