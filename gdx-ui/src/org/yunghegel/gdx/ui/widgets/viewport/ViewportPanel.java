package org.yunghegel.gdx.ui.widgets.viewport;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import lombok.Getter;
import org.yunghegel.gdx.ui.UI;
import org.yunghegel.gdx.ui.widgets.STable;

public class ViewportPanel extends STable {

    private ViewportWidget viewportWidget;
    private Stack stack;

    @Getter
    private STable uiBody;

    public ViewportPanel (ViewportWidget widget) {
        this.viewportWidget=widget;
        createStack();
    }

    private void createStack(){
        stack = new Stack();
        add(stack).grow();
        uiBody = new STable();
        uiBody.setTouchable(Touchable.enabled);
        stack.add(viewportWidget);
        stack.add(uiBody);
        TextButton  test = new TextButton("Test", UI.getSkin());
        uiBody.add(test);
    }


}
