package org.yunghegel.gdx.ui.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import org.yunghegel.gdx.ui.UI;

public class EditableTextButton extends Button {

    EditableLabel label;

    public EditableTextButton(String text) {
        label=new EditableLabel(text);
        setSkin(UI.getSkin());
        setStyle(UI.getSkin().get("default", ButtonStyle.class));
        add(label).expand().fill();
        label.textField.getStyle().focusedBackground=null;
        label.textField.getStyle().background=null;
    }
}
