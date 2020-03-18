package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.SearchManager;
import model.TabTableInfoFiles;
import utils.Filter;

import java.io.File;
import java.io.IOException;

import static java.lang.Thread.MAX_PRIORITY;

public class AppController {

    @FXML
    private TreeView<String> fileTree;

    @FXML
    private TabPane tabPane;

    @FXML
    private ButtonBar buttonBar;

    @FXML
    private Button buttonAll;

    @FXML
    private Button buttonPrev;

    @FXML
    private Button buttonNext;

    @FXML
    private TextField textSearch;

    @FXML
    private Button buttonOk;

    @FXML
    private TextField textExtension;

    @FXML
    private Button buttonChooser;

    @FXML
    private TextField textFilePath;

    @FXML
    private Label infoLabel;

    @FXML
    private Label loadingInfoLabel;

    @FXML
    private ProgressIndicator loadingProgressIndicator;

    private TabPaneController tabPaneController;
    private TreeViewController treeViewController;
    private SearchManager searchManager;
    private OutController outController;

    @FXML
    private void initialize() {
        Thread.currentThread().setPriority(MAX_PRIORITY);
        tabPaneController = new TabPaneController();

        outController = new OutController(loadingProgressIndicator, loadingInfoLabel, buttonOk);
        buttonBar.setDisable(true);
        setListeners();
        loadingProgressIndicator.visibleProperty().setValue(false);
    }

    @FXML
    public void onClickChooser(javafx.event.ActionEvent actionEvent) {
        buttonOk.setDisable(false);
        if (searchManager != null)
        searchManager.getSearchTask().cancel(true);

        final DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage = (Stage) buttonChooser.getScene().getWindow();
        File file = directoryChooser.showDialog(stage);

        if (file != null){
            textFilePath.setText(file.getAbsolutePath());
        }
        loadingProgressIndicator.visibleProperty().setValue(false);
        loadingInfoLabel.setText("");
        textSearch.setDisable(false);
    }

    @FXML
    public void onClickOk(ActionEvent actionEvent) throws IOException {
        File file = new File(textFilePath.getText());
        if (file.isDirectory()) {
            treeViewController = new TreeViewController(fileTree, file);
            searchManager = new SearchManager(treeViewController, textExtension.getText(), textSearch.getText(), outController);

            searchManager.runSearch(file, null);

            buttonOk.setDisable(true);
            //textSearch.setDisable(true);
            fileTree.setDisable(false);
            loadingProgressIndicator.visibleProperty().setValue(true);

            loadingInfoLabel.textProperty().unbind();
            loadingInfoLabel.textProperty().bind(searchManager.getSearchTask().messageProperty());
            infoLabel.setText("");
        } else infoLabel.setText("Folder: "+ file.getAbsolutePath()+" not exist!");
    }

    private void setListeners(){
        tabPane.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                        if (tabPaneController.isTextFileView(tabPane.getSelectionModel().getSelectedItem())){
                            buttonBar.setDisable(false);
                            infoLabel.setText("Found: "+tabPaneController.getTextFileView
                                    (tabPane.getSelectionModel().getSelectedItem())
                                    .getFinder().getNumRowDetect().size()+" coincidences!");
                        } else {
                            buttonBar.setDisable(true);
                            if (tabPaneController.isTableView(tabPane.getSelectionModel().getSelectedItem())){
                                int sizeFounds = tabPaneController.getFileTableView(tabPane.getSelectionModel().getSelectedItem()).getTabTableInfoFiles().size();
                                infoLabel.setText("Found: "+ sizeFounds +" files!");
                            }
                            }
                        }
                }
        );

        fileTree.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                if(mouseEvent.getClickCount() == 2)
                {
                    if (fileTree.getSelectionModel().getSelectedItem() != null) {
                        TreeItem<String> item = fileTree.getSelectionModel().getSelectedItem();

                        if (treeViewController.isFolder(item)) {
                            File dir = new File(treeViewController.getFolder(item).getAbsolutePath());

                            File[] lst = dir.listFiles(new Filter(textExtension.getText()));
                            tabPane.getSelectionModel().select(tabPaneController.createTableTab(tabPane, lst, item.getValue(), searchManager.getSearchFiles()));
                            TableView<TabTableInfoFiles> table = tabPaneController.getFileTableView(
                                    tabPane.getSelectionModel().getSelectedItem())
                                    .getTable();
                            buttonBar.setDisable(true);
                            int sizeFounds = tabPaneController.getFileTableView(tabPane.getSelectionModel().getSelectedItem()).getTabTableInfoFiles().size();
                            infoLabel.setText("Found: "+ sizeFounds +" files!");
                            table.setOnMouseClicked((MouseEvent e) -> {
                                if (e.getClickCount() == 2)
                                    if (table.getSelectionModel().getSelectedItem() != null) {
                                        {
                                            File file = table.getSelectionModel().getSelectedItem().getFile();
                                            System.out.println(file.getName());
                                            try {
                                                tabPane.getSelectionModel().select(tabPaneController.createFileTab(tabPane, file, searchManager.getSearchFile(file)));

                                            } catch (IOException | InterruptedException ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                    }
                            });
                        }
                        if (treeViewController.isFile(item)) {
                            File file = treeViewController.getFile(item);
                            try {
                                tabPane.getSelectionModel().select(tabPaneController.createFileTab(tabPane, file, searchManager.getSearchFile(file)));
                                buttonBar.setDisable(false);
                                infoLabel.setText("Found: "+tabPaneController.getTextFileView
                                        (tabPane.getSelectionModel().getSelectedItem())
                                        .getFinder().getNumRowDetect().size()+" coincidences!");
                                tabPane.getSelectionModel().getSelectedItem().setOnCloseRequest(new EventHandler<Event>() {
                                    @Override
                                    public void handle(Event event) {
                                        tabPaneController.getTextFileView(tabPane.getSelectionModel().getSelectedItem()).getTask().cancel(true);
                                    }
                                });
                            } catch (IOException | InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

        buttonAll.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (tabPaneController.isTextFileView(tabPane.getSelectionModel().getSelectedItem())){
                    tabPaneController.getTextFileView(tabPane.getSelectionModel().getSelectedItem()).selectAll();
                }
            }
        });

        buttonNext.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (tabPaneController.isTextFileView(tabPane.getSelectionModel().getSelectedItem())){
                    tabPaneController.getTextFileView(tabPane.getSelectionModel().getSelectedItem()).selectNext();
                }
            }
        });

        buttonPrev.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (tabPaneController.isTextFileView(tabPane.getSelectionModel().getSelectedItem())){
                    tabPaneController.getTextFileView(tabPane.getSelectionModel().getSelectedItem()).selectPrevious();
                }
            }
        });
    }
}
