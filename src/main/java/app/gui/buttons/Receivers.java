package app.gui.buttons;

import app.gui.commands.InitiateHandshakeCommand;
import engine.button.radioButton.CommandRadioButton;
import engine.button.radioButton.RadioButton;
import engine.button.radioButton.RadioButtonBundle;
import engine.display.Drawable;
import engine.display.DrawableComposition;
import engine.input.inputCombination.InputCombination;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static app.Constants.MAX_CLIENTS;
import static engine.display.DisplayBean.getDisplay;
import static engine.input.InputBean.getInput;
import static engine.scene.SceneBean.getScene;

public class Receivers {

    private RadioButtonBundle receiversBundle;
    private final RadioButton[] receivers;
    private final Integer[] receiversIds;
    private final Drawable background;

    public Receivers(Drawable background) {
        this.receivers = new RadioButton[MAX_CLIENTS - 1];
        this.receiversIds = new Integer[MAX_CLIENTS - 1];
        this.receiversBundle = new RadioButtonBundle(List.of(), true);
        this.background = background;
    }

    public void addReceiver(int receiverId) {
        int index = 0;
        for (int i = 0; i < MAX_CLIENTS - 1; i++) {
            if (this.receiversIds[i] == null) {
                index = i;
                break;
            }
        }
        Drawable offDrawable = getDisplay().getDrawableFactory().makeFramedRectangle(
                this.background.getX() + 2,
                this.background.getY() + 2 + (30 * index),
                196,
                30,
                2,
                (index % 2 == 0) ? "gray" : "lightBlue",
                "lighterBlue"
        );
        Drawable onDrawable = getDisplay().getDrawableFactory().makeFramedRectangle(
                this.background.getX() + 2,
                this.background.getY() + 2 + (30 * index),
                196,
                30,
                2,
                (index % 2 == 0) ? "gray" : "lightBlue",
                "yellow"
        );
        Drawable userName = getDisplay().getDrawableFactory().makeText(
                "User " + receiverId,
                this.background.getX() + 5,
                this.background.getY() + 5 + (30 * index),
                "HBE24",
                "black"
        );
        offDrawable = new DrawableComposition(offDrawable, userName);
        onDrawable = new DrawableComposition(onDrawable, userName);
        InputCombination activationCombination = getInput().getInputCombinationFactory().makeLmbCombination();
        InitiateHandshakeCommand command = new InitiateHandshakeCommand(receiverId);
        CommandRadioButton receiver = new CommandRadioButton(offDrawable, onDrawable, activationCombination, command);
        getScene().addOnHighest(receiver);
        this.receivers[index] = receiver;
        this.receiversIds[index] = receiverId;
        buildBundle();
    }

    public void removeReceiver(int receiverId) {
        int index = 0;
        for (int i = 0; i < MAX_CLIENTS - 1; i++) {
            if (this.receiversIds[i] == receiverId) {
                index = i;
                break;
            }
        }
        getScene().removeObject(this.receivers[index]);
        this.receivers[index] = null;
        this.receiversIds[index] = null;
        buildBundle();
    }

    public Integer getSelectedReceiverId() {
        if (this.receiversBundle.getSelectedRadioButtonIndex() >= 0) {
            return this.receiversIds[this.receiversBundle.getSelectedRadioButtonIndex()];
        }
        return null;
    }

    private void buildBundle() {
        int selectedReceiver = this.receiversBundle.getSelectedRadioButtonIndex();
        List<RadioButton> activeReceivers = Arrays.stream(this.receivers)
                .filter(Objects::nonNull)
                .toList();
        this.receiversBundle = new RadioButtonBundle(activeReceivers, selectedReceiver);
    }

}
