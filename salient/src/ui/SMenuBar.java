package ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.kotcrab.vis.ui.widget.*;
import games.spooky.gdx.nativefilechooser.NativeFileChooserCallback;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.yunghegel.gdx.ui.widgets.SLabel;
import org.yunghegel.gdx.ui.widgets.STable;
import org.yunghegel.gdx.ui.widgets.viewport.SImageButton;
import app.Salient;
import events.proj.ProjectChangedEvent;
import events.proj.ProjectLoadedEvent;
import events.scene.SceneLoadedEvent;
import events.scene.SceneSelectionEvent;

import java.util.Objects;

public class SMenuBar extends MenuBar implements ProjectLoadedEvent.Listener, ProjectChangedEvent.Listener, SceneLoadedEvent.Listener, SceneSelectionEvent.Listener {

    Menu files, edit, view, help;
    SMenuItem newProject, openProject, saveProject, recentProjects, exit;

    public SMenuBar() {

        init();
        createWindowControls();
        createFileMenu();

        Salient.INSTANCE.registerListener(this);
    }

    SLabel projectName;
    SLabel sceneName;

    Skin skin;

    private void init() {

        files = new Menu("File");
        edit = new Menu("Edit");
        view = new Menu("View");
        help = new Menu("Help");

        files.setSkin(skin);
        edit.setSkin(skin);
        view.setSkin(skin);
        help.setSkin(skin);


        Table items = (Table) getTable().getChild(0);
        items.pad(0);
        var logo = new Logo();
//        items.add(logo).pad(3,0,3,5).padLeft(5).padRight(5).align(Align.left);


        addMenu(files);
        addMenu(edit);
        addMenu(view);
        addMenu(help);


        Table projectInfo = new Table();
        projectName = new SLabel("Project: ","default-gray");
        sceneName = new SLabel("Scene: ","default-gray");
        projectInfo.add(projectName).padRight(5);
        projectInfo.add(sceneName).padRight(5);

        getTable().add(projectInfo).padLeft(30);

        projectInfo.align(Align.center);




    }

    private void createWindowControls(){
        STable windowControls = new STable();
        var minimizeButton = new SImageButton("iconify-window");
        var maximizeButton = new SImageButton("maximize-window");
        var closeButton = new SImageButton("close-window");
//        windowControls.add(minimizeButton).size(22,22).padRight(5).padBottom(0).center().width(22);
//        windowControls.add(maximizeButton).size(22,22).padRight(5).center().width(22);
//        windowControls.add(closeButton).size(22,22).padRight(5).padBottom(0).center().width(22);
        windowControls.align(Align.right);
        getTable().add(windowControls).growX().right().padRight(5);
        getTable().setTouchable(Touchable.enabled);

        maximizeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Salient.INSTANCE.getNatives().setFullscreen();
            }
        });
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        minimizeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Salient.INSTANCE.getNatives().setIconified();
            }
        });

    }

    private void createFileMenu() {
        newProject = new SMenuItem("New Project");
        openProject = new SMenuItem("Open Project");
        saveProject = new SMenuItem("Save Project");
        exit = new SMenuItem("Exit");
        recentProjects = new SMenuItem("Recent Projects");



        files.addItem(newProject);
        files.addItem(openProject);
        files.addItem(saveProject);
        files.addItem(recentProjects);
        files.addItem(exit);


        openProject.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Salient.INSTANCE.getNatives().chooseFile(new NativeFileChooserCallback() {
                    @Override
                    public void onFileChosen(FileHandle file) {
                        Salient.INSTANCE.getProjectManager().initialize(file.nameWithoutExtension());
                    }

                    @Override
                    public void onCancellation() {

                    }

                    @Override
                    public void onError(Exception exception) {

                    }
                });
            }
        });
        newProject.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Salient.INSTANCE.getUi().getStage().addActor(new Dialogs.InputDialog("Create a new project: ", "Project name: ", true, null, new InputDialogListener() {
                    @Override
                    public void finished(String input) {
                        Salient.INSTANCE.getProjectManager().initialize(input);
                    }

                    @Override
                    public void canceled() {

                    }
                }));
            }
        });


    }

    private void createHelpMenu() {
    }

    private void createViewMenu() {
    }

    private void createEditMenu() {
    }


    @Override
    public void onProjectLoaded(@NotNull ProjectLoadedEvent event) {
        System.out.println("Project loaded: " + event.getProject().getName());
        val recentProjectsMenu = new PopupMenu();

        for (val project : Salient.INSTANCE.getProjectManager().getRecentProjects()) {
            val item = new SMenuItem(project);
            item.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Salient.INSTANCE.getProjectManager().initialize(project);
                }
            });
            recentProjectsMenu.addItem(item);
        }
        recentProjects.setSubMenu(recentProjectsMenu);

        projectName.setText('['+event.getProject().getName()+']');


    }

    @Override
    public void onProjectChanged(@NotNull ProjectChangedEvent event) {

    }

    @Override
    public void onSceneLoaded(@NotNull SceneLoadedEvent event) {
        sceneName.setText(' '+ Objects.requireNonNull(event.getScene()).name);
    }

    @Override
    public void onSceneSelection(@NotNull SceneSelectionEvent event) {
        sceneName.setText(' '+ Objects.requireNonNull(event.getScene()).name);

    }
}
