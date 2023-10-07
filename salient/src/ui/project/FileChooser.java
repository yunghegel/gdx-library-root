package ui.project;

import app.Salient;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Selection;
import io.DirectoryMappings;
import lombok.val;
import net.dermetfan.gdx.scenes.scene2d.ui.ListFileChooser;
import net.dermetfan.gdx.scenes.scene2d.ui.TreeFileChooser;
import net.dermetfan.utils.Function;
import org.yunghegel.gdx.ui.UI;
import org.yunghegel.gdx.ui.widgets.EditableLabel;
import org.yunghegel.gdx.ui.widgets.EditableTextButton;
import org.yunghegel.gdx.ui.widgets.SLabel;
import org.yunghegel.gdx.ui.widgets.STable;
import org.yunghegel.gdx.utils.ui.fields.FileChooserField;
import project.ProjectManager;
import ui.STree;

import java.io.FileFilter;
import java.util.Map;

public class FileChooser extends TreeFileChooser {

    FileFilter filter;
    Listener listener;
    float prefWidth;
    STree tree;
    EditableTextButton path;
    SLabel title;
    public FileHandle selectedFile=null;
    Map nodeMap = new java.util.HashMap<FileHandle,Tree.Node>();

    @Override
    public Tree.Node add(FileHandle file) {
        Tree.Node node = TreeFileChooser.fileNode(file,filter,supplier,consumer);
        nodeMap.putIfAbsent(file,node);
        getTree().add(node);

        return node;
    }



    public interface FileSelectListener {
        void onFileSelected(FileHandle file);
    }

    public FileSelectListener fileSelectListener = new FileSelectListener() {
        @Override
        public void onFileSelected(FileHandle file) {
            DialogProvider.INSTANCE.setDialogType(DialogProvider.INSTANCE.matchPath(file));

        }
    };

    public void setFileSelectListener(FileSelectListener fileSelectListener) {
        this.fileSelectListener = fileSelectListener;
    }


    public FileChooser(Listener listener, FileFilter filter,float prefWidth) {
        super(UI.getSkin(), listener);

        this.listener = listener;
        this.filter = filter;
        tree = new STree(getSkin());
        configurePath();
        setTree(tree);
        buildWidgets();
        build();
        configureNodes();


    }

    public String getPath(){
        return path.label.label.getText().toString();
    }

    private void configurePath() {
        path = new EditableTextButton("path","default-medium");
        path.label.label.setText(DirectoryMappings.INSTANCE.getPROJECTS_DIR().substring(DirectoryMappings.INSTANCE.getPROJECTS_DIR().indexOf(".salient")));


    }
    public  void addIfNotPresent(FileHandle file,Tree.Node node) {
        if(!nodeMap.containsKey(file)) {
            nodeMap.put(file,node);
            getTree().add(node);
        }
    }



    void configureNodes(){
       getTree().clearChildren();






    Tree.Node node = TreeFileChooser.fileNode(Gdx.files.getFileHandle(DirectoryMappings.INSTANCE.getPROJECTS_DIR(), Files.FileType.Absolute),filter,supplier,consumer);
    node.expandAll();
//    node.setValue(Gdx.files.getFileHandle(DirectoryMappings.INSTANCE.getPROJECTS_DIR(), Files.FileType.Absolute));
    add(Gdx.files.getFileHandle(DirectoryMappings.INSTANCE.getPROJECTS_DIR(), Files.FileType.Absolute));

    }

    Function<FileHandle,Label> supplier = (FileHandle file)->{
        val label = new EditableTextButton(file.name());
        if(file.isDirectory()){
            label.label.label.getText().append("/");



            label.label.label.setStyle(getSkin().get("default-medium", Label.LabelStyle.class));
        }else{
            label.setColor(Color.WHITE);
            label.label.label.setStyle(getSkin().get("default-medium", Label.LabelStyle.class));
        }
        return label.label.label;
    };

    net.dermetfan.utils.Function<Tree.Node,Void> consumer = (Tree.Node node)->{
        val table = new STable() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
                tree.drawOver(node, getStage().getBatch(), getX(), getY() + node.getActor().getY() - tree.getYSpacing() / 2, getWidth(), node.getActor().getHeight() + tree.getYSpacing());
            }
        };

        FileHandle file = (FileHandle) node.getValue();
        if(file!=null){
            var edit = new EditableLabel(file.name()+"/","default-medium");
            if(file.name().equals("projects")) edit.label.setColor(Color.GOLD);
            else if (file.name().equals("assets")) edit.label.setColor(Color.SKY);
            else if (file.name().equals("scenes")) edit.label.setColor(Color.FOREST);
            node.setActor(edit);
            node.getActor().addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {

                    String filePath = file.path();
                    filePath = filePath.substring(filePath.indexOf(".salient"));
                    path.label.label.setText(filePath);
                    selectedFile = file;
                    fileSelectListener.onFileSelected(file);
                    DialogProvider.INSTANCE.setDialogType(DialogProvider.INSTANCE.matchPath(file));


                }
            });
        }







        return null;
    };




}
