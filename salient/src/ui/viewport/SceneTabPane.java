package ui.viewport;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisTable;
import org.jetbrains.annotations.NotNull;
import org.yunghegel.gdx.ui.widgets.STable;
import org.yunghegel.gdx.ui.widgets.STextButton;
import org.yunghegel.gdx.ui.widgets.viewport.ViewportPanel;
import app.Salient;
import events.scene.SceneClosedEvent;
import events.scene.SceneCreatedEvent;
import events.scene.SceneLoadedEvent;
import scene.Scene;

public class SceneTabPane extends STable implements SceneLoadedEvent.Listener, SceneClosedEvent.Listener,SceneCreatedEvent.Listener {

    ViewportPanel viewportPanel;
    HorizontalGroup tabs = new HorizontalGroup();
    Array<SceneTab> tabArray = new Array<>();
    ButtonGroup<TextButton> buttonGroup = new ButtonGroup<>();
    TextButton addTabButton = new STextButton(" + ", "round");
    STable addButtonContainer = new STable();
    public SceneTabPane(ViewportPanel viewportPanel) {
        super();
        pad(1);
        align(Align.left);
        this.viewportPanel = viewportPanel;
        tabs.space(3);
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setMinCheckCount(1);
        buttonGroup.setUncheckLast(true);
        VisTable container = new VisTable();
        container.add(tabs);


        addButtonContainer.add(addTabButton).width(20).height(20).padLeft(5);

        container.add(addButtonContainer).width(20).height(20);

        container.align(Align.left);
        container.setColor(0.2f,0.2f,0.1f,1);
        add(container).left().growX().row();
        Separator seperator = new Separator();
//        add(seperator).growX().row();
//        add(viewportPanel).grow().
        left();
        addDefaultListeners();
        Salient.INSTANCE.registerListener(this);
    }

    void addDefaultListeners() {
        addTabButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                var scene =Salient.INSTANCE.getProjectManager().sceneManager.initDefaultScene();
                Salient.INSTANCE.getEventBus().post(new SceneCreatedEvent(scene));
                Salient.INSTANCE.getProjectManager().currentProject.loadScene(scene);


                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    public void addTab(Scene scene){
        SceneTab tab = new SceneTab(scene);

        tabs.addActor(tab);
        tabArray.add(tab);
//            buttonGroup.add(tab.tabButton);
        tab.addCloseListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                tabs.removeActor(tab);
                tabArray.removeValue(tab, true);
//                    buttonGroup.remove(tab.tabButton);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        layout();
        tab.tabButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Salient.INSTANCE.getProjectManager().currentProject.setScene(scene);

            }
        });
    }

    public void removeTab(Scene scene){
        for(SceneTab tab : tabArray){
            if(tab.scene == scene){
                scene.save();
                scene.dispose();
                tabs.removeActor(tab);
                tabArray.removeValue(tab, true);
    }
        }
    }

    public void addTabListener(InputListener listener){
        for(SceneTab tab : tabArray){
            tab.tabButton.addListener(listener);
        }
    }

    @Override
    public void onSceneLoaded(@NotNull SceneLoadedEvent event) {
        addTab(event.getScene());
        viewportPanel.viewportWidget.setRenderer(event.getScene());
    }


    @Override
    public void onSceneClosed(@NotNull SceneClosedEvent event) {
        removeTab(event.getScene());
    }

    @Override
    public void onSceneCreated(@NotNull SceneCreatedEvent event) {



    }
}
