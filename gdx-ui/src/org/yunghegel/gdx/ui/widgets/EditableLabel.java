package org.yunghegel.gdx.ui.widgets;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class EditableLabel extends ToggleWidget {

    private String text;
    protected STextField textField;
    private SLabel label;
    private ClickListener listener;
    private ChangeListener changeListener;
    private ButtonClicked buttonClicked;

    public EditableLabel(String text) {
        this.text = text;
        label = new SLabel(text);
        firstTable.add(label).grow();

        textField = new STextField(text);
        secondTable.add(textField);

        label.setTouchable(Touchable.enabled);

        initListeners();
    }

    private void initListeners(){
        label.setTouchable(Touchable.enabled);
        label.addListener(listener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (getTapCount()==2){
                    toggle();
                    getStage().setKeyboardFocus(textField);
                    textField.selectAll();
                }

            }
        });
        firstTable.setTouchable(Touchable.enabled);
        firstTable.addListener(listener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if(buttonClicked != null){
                    buttonClicked.clicked();
                }
            }


        });

        textField.addListener(changeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                label.setText(textField.getText());
                if(label.getText().isEmpty()) label.setText(". . .");
                EditableLabel.this.text = textField.getText();
            }
        });

    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if (stage != null) {
            var listener = new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (getStage() != null && event.getTarget() != textField) {
                        if (stage.getKeyboardFocus() == textField) stage.setKeyboardFocus(null);
                        showFirstTable();

                    }
                    return false;
                }
            };
            stage.addListener(listener);
        }
    }


    public interface ButtonClicked {
        void clicked();
    }
}
