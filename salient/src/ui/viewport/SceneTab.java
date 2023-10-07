package ui.viewport;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.OptionDialogListener;
import com.kotcrab.vis.ui.widget.Separator;
import org.jetbrains.annotations.NotNull;
import org.yunghegel.gdx.ui.widgets.EditableTextButton;
import org.yunghegel.gdx.ui.widgets.STable;
import org.yunghegel.gdx.ui.widgets.STextButton;
import org.yunghegel.gdx.utils.ui.ViewportWidget;
import app.Salient;
import events.scene.SceneClosedEvent;
import events.scene.SceneCreatedEvent;
import events.scene.SceneLoadedEvent;
import scene.Scene;

public class SceneTab extends STable implements SceneClosedEvent.Listener, SceneCreatedEvent.Listener, SceneLoadedEvent.Listener {

    public EditableTextButton tabButton;
    ViewportWidget viewportWidget;
    STextButton closeButton;
    STable tab;
    STable closeTab;
    ClickListener listener;
    Scene scene;

    public SceneTab(Scene scene) {
        this.scene = scene;
        tab = new STable();
        closeTab = new STable();
        tabButton = new EditableTextButton(scene.name, "default-medium");
        tabButton.label.setCallback(s -> {
            scene.name = s;
            scene.save();

        });
        closeButton = new STextButton(" x ");
        tab.add(tabButton);
        Separator separator = new Separator();
        add(tab).growY();

        add(closeButton);

        createDefaultListeners();


    }

    void createDefaultListeners(){
        closeButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                getStage().addActor(Dialogs.showOptionDialog(getStage(), "Confirm", "Exit without saving?", Dialogs.OptionDialogType.YES_NO, new OptionDialogListener() {

                    @Override
                    public void yes() {
                        Salient.INSTANCE.getProjectManager().currentProject.removeScene(scene);
                        Salient.INSTANCE.getProjectManager().save();
                    }

                    @Override
                    public void no() {
                        remove();

                    }

                    @Override
                    public void cancel() {

                    }
                }
                ));


                return super.touchDown(event, x, y, pointer, button);
            }
        });
        tab.setTouchable(Touchable.enabled);
        tab.addListener(listener=new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
            }


        });



    }

    public void addCloseListener(InputListener listener){
        closeButton.addListener(listener);
    }


    @Override
    protected void drawBackground(Batch batch, float parentAlpha, float x, float y) {
        super.drawBackground(batch, parentAlpha, x, y);

    }

    @Override
    public void onSceneClosed(@NotNull SceneClosedEvent event) {
            event.getScene().save();
            event.getScene().dispose();

    }

    @Override
    public void onSceneCreated(@NotNull SceneCreatedEvent event) {


    }

    @Override
    public void onSceneLoaded(@NotNull SceneLoadedEvent event) {

    }
}

