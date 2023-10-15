import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import io.github.fourlastor.scope.Group;
import io.github.fourlastor.scope.ObjectScope;
import io.github.fourlastor.scope.ScopeRenderer;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;
import org.yunghegel.gdx.shaders.Providers;
import org.yunghegel.gdx.ui.UI;
import org.yunghegel.gdx.utils.graphics.CameraController;
import org.yunghegel.gdx.utils.graphics.model.PrimitiveSupplier;

public class ShaderTest extends ApplicationAdapter {



    private PerspectiveCamera camera;
    private EditorCamera cameraController;
    private FirstPersonCameraController firstPersonCameraController;
    private Array<RenderableProvider> instances = new Array<RenderableProvider>();
    private ModelBatch modelBatch;

    private float time;
    private Group group;
    private ScopeRenderer scopeRenderer;
    private Stage stage;
    private Window window;
    private Table table;




    @Override
    public void create() {
        UI.load();
        VisUI.load(UI.getSkin());
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        stage = new Stage(new ScreenViewport());
        table = new Table();

        camera = new PerspectiveCamera(60f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.near = 0.1f;
        camera.far = 200;
        camera.position.set(0,0.5f, 4f);
        camera.lookAt(0,0.5f,0);
        camera.update();



        cameraController = new EditorCamera(camera,new Vector3(0,0,0),200f);
        cameraController.getPerspectiveCameraController().getConfig().setHomeTarget(new Vector3(0,0.5f,0));
        cameraController.getPerspectiveCameraController().setHomeDistance(200f);
//        Gdx.input.setInputProcessor(cameraController.getPerspectiveCameraController());
        inputMultiplexer.addProcessor(cameraController.getPerspectiveCameraController());
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
        group= new Group(new ObjectScope("lerp",cameraController.getPerspectiveCameraController().getConfig()));
        scopeRenderer = new ScopeRenderer(48);

        modelBatch = new ModelBatch(new Providers.DepthProvider());

        for (int i = -5; i < 5; i++) {
            for (int j = -5; j < 5; j++) {
                for (int k = -5; k < 5; k++) {
                    ModelInstance primitive = PrimitiveSupplier.randomPrimitive();
                    primitive.transform.setToTranslation(i, j, k);
                    primitive.transform.scale(0.5f, 0.5f, 0.5f);
                    instances.add(primitive);
                }
            }

        }

        var slerpSlider = UI.slider(50,300,5f,false, UI.getSkin(),200f,(f)->{
            camera.far = f;
        });
        var velocitySlider = UI.slider(0,1,0.05f,false, UI.getSkin(),0.5f,(f)->{
            camera.near = f;
        });

        table.add(slerpSlider).row();

        table.add(velocitySlider).row();

        window = new Window("Camera",UI.getSkin());
        window.add(table);

        window.addListener(new InputListener() {

//            @Override
//            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
//                Gdx.input.setInputProcessor(stage);
//            }
//
//            @Override
//            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
//                Gdx.input.setInputProcessor(inputMultiplexer);
//            }
        });

        stage.addActor(window);



    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        time += deltaTime;

        for (RenderableProvider instance : instances) {
            ModelInstance modelInstance = (ModelInstance) instance;
            modelInstance.transform.rotate(Vector3.Y, deltaTime * 10);
        }
        camera.update();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        modelBatch.begin(camera);
        modelBatch.render(instances);
        modelBatch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {

    }
}
