package controllers;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;

public class OutController {
    private ProgressIndicator progressIndicator;
    private Label infoLabel;
    private Button buttonOk;

    public OutController(ProgressIndicator progressIndicator, Label label, Button buttonOk) {
        this.infoLabel = label;
        this.progressIndicator = progressIndicator;
        this.buttonOk = buttonOk;
    }

    public ProgressIndicator getProgressIndicator() {
        return progressIndicator;
    }

    public void setProgressIndicator(ProgressIndicator progressIndicator) {
        this.progressIndicator = progressIndicator;
    }

    public Label getInfoLabel() {
        return infoLabel;
    }

    public void setInfoLabel(Label infoLabel) {
        this.infoLabel = infoLabel;
    }

    public Button getButtonOk() {
        return buttonOk;
    }
}
