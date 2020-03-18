package exceptions;

import javafx.scene.control.TextInputControl;

public class InputException {
    private TextInputControl control;

    public InputException(String msg, TextInputControl control) {
        super();
        this.control = control;
    }

    public TextInputControl getControl() {
        return control;
    }
}
