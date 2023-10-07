package ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import org.yunghegel.gdx.ui.widgets.SLabel;
import org.yunghegel.gdx.ui.widgets.STable;

public class Logo extends STable {

    private Texture spriteSheet;
    private AnimatedSprite animatedSprite;
    private Animation<TextureRegion> animation;
    private Image iconImage;

    public Logo () {
        spriteSheet = new Texture(Gdx.files.internal("sheet.png"));
        Array<TextureRegion> regions = new Array<TextureRegion>();
        for(int i = 0; i < 20; i++){
            regions.add(new TextureRegion(spriteSheet, i*104, 0, 104, 104));
        }
        animation = new Animation<TextureRegion>(0.1f, regions);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        animation.setFrameDuration(.3f);
        animatedSprite = new AnimatedSprite(animation);

        SpriteDrawable spriteDrawable = new SpriteDrawable(animatedSprite);
        iconImage = new Image(spriteDrawable);

        Table imageTable = new Table();
        imageTable.padTop(3).padBottom(3);

        SLabel title = new SLabel("Salient","default-alternative");
        add(iconImage).pad(5,0,5,8).pad(3).size(15,15);
        add(title).pad(0,5,0,5).pad(3);
    }

}
