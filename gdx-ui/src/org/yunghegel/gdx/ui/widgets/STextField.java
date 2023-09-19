package org.yunghegel.gdx.ui.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import org.yunghegel.gdx.ui.UI;

public class STextField extends TextField {
    public STextField(String text) {
        super(text,UI.getSkin());
        setStyle(UI.getSkin().get("default",TextFieldStyle.class));

    }

    @Override
    public float getPrefWidth() {
        return 100f;
    }
}
