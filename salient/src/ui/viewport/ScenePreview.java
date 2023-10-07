package ui.viewport;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;
import org.yunghegel.gdx.ui.widgets.viewport.ViewportPanel;
import org.yunghegel.gdx.ui.widgets.viewport.ViewportWidget;
import org.yunghegel.gdx.ui.widgets.viewport.events.ViewportEvent;
import org.yunghegel.gdx.ui.widgets.viewport.events.ViewportEventListener;
import org.yunghegel.gdx.utils.graphics.ScreenUtil;
import app.Salient;
import scene.Scene;

public class ScenePreview extends ViewportPanel {

    public ScenePreview(ViewportWidget widget) {
        super(widget);
        createListener();

    }

    void createListener(){
        var viewportListener = new ViewportEventListener() {

            @Override
            public boolean exited(ViewportEvent event) {


                return super.exited(event);
            }

            @Override
            public boolean entered(ViewportEvent event) {


                return super.entered(event);
            }

            @Override
            public boolean gameViewportApplied(ViewportEvent event) {
                return super.gameViewportApplied(event);
            }

            @Override
            public boolean stageViewportApplied(ViewportEvent event) {
                Scene.compassGizmo.render(Salient.INSTANCE.getModelBatch());
                return super.stageViewportApplied(event);
            }
            @Override
            public boolean layout(ViewportEvent event, float width, float height, float x, float y) {
                if (Scene.compassGizmo == null) return false;

                BoundingBox box = new BoundingBox();
                Scene.compassGizmo.compass.calculateBoundingBox(box);
                Vector2 pos= ScreenUtil.toOpenGLCoords(width + MathUtils.round(x)+viewportWidget.viewport.getScreenWidth(), height+ MathUtils.round(Gdx.graphics.getHeight() - y)+viewportWidget.viewport.getScreenHeight());
                Scene.compassGizmo.setPosition(pos.x-box.getWidth()-0.01f,pos.y-box.getHeight()-0.01f);
                return super.layout(event, width, height, x, y);
            }
        };

        this.viewportWidget.addViewportListener(viewportListener);
    }


    @Override
    public void act(float delta) {
        if(!viewportWidget.isInsideWidget(Gdx.input.getX(),Gdx.graphics.getHeight()- Gdx.input.getY())) {
            Salient.INSTANCE.getInputManager().setProcessor(Salient.INSTANCE.getUi().getStage());
        } else {
            Salient.INSTANCE.getInputManager().setProcessor(Salient.INSTANCE.getCameraController().getPerspectiveCameraController());

        }
    }


}
