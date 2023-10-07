/* (C)2023 */
package scene;

import app.Salient;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import io.DirectoryMappings;
import io.Serializer;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;
import org.yunghegel.gdx.gizmo.core.utility.CompassGizmo;
import org.yunghegel.gdx.scenegraph.scene3d.BaseScene;
import org.yunghegel.gdx.ui.widgets.viewport.ViewportWidget;
import org.yunghegel.gdx.utils.graphics.Grid;
import project.Project;
import scene.context.env.EditorEnvironment;
import sys.profiling.Profile;
import util.SceneUtils;

public class Scene extends BaseScene implements ViewportWidget.Renderer {

    public String name;
    public String path;
    public String uid = "-1";

    private SceneGraph sceneGraph;
    public Project project;

    public EditorEnvironment env = new EditorEnvironment();
    public PerspectiveCamera camera = Salient.INSTANCE.getCamera();
    public Grid grid;
    public SceneSkybox skybox;
    public SceneManager sceneManager;
    public static CompassGizmo compassGizmo;

    public Scene(String name, Project project) {
        sceneGraph = new SceneGraph(this);
        this.name = name;
        this.project = project;
        path = DirectoryMappings.INSTANCE.getScenePath(project.getName(), name);
        float[] subdivisions = new float[] {0.25f, 0.125f};
        grid = new Grid(0.2f, 300, 0.5f, subdivisions);
        Cubemap customSkyboxCubemap = SceneUtils.createCubemapDirectionFormat("editor/", "editor/");
        skybox = new SceneSkybox(customSkyboxCubemap);
        sceneManager = new SceneManager();
        sceneManager.setCamera(Salient.INSTANCE.getCameraController().getPerspectiveCamera());

        IBLBuilder iblBuilder = IBLBuilder.createOutdoor(new DirectionalLightEx());
        Texture brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));
        Cubemap environmentCubemap = iblBuilder.buildEnvMap(1024);
        Cubemap diffuseCubemap = iblBuilder.buildIrradianceMap(256);
        Cubemap specularCubemap = iblBuilder.buildRadianceMap(10);

        sceneManager.setAmbientLight(.1f);
        sceneManager.environment.set(
                new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
        sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));

        sceneManager.setSkyBox(skybox);

        Gdx.gl.glEnable(Gdx.gl.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(Gdx.gl.GL_LEQUAL);
        Gdx.gl.glDepthMask(true);

        Gdx.gl.glEnable(Gdx.gl.GL_CULL_FACE);
        Gdx.gl.glCullFace(Gdx.gl.GL_BACK);

        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);

        compassGizmo =
                new CompassGizmo(
                        Salient.INSTANCE.getInputManager().getInputMultiplexer(),
                        Salient.INSTANCE.getModelBatch(),
                        Salient.INSTANCE.getCamera(),
                        Salient.INSTANCE.getViewport(),
                        0);
    }

    public Scene(Project project) {
        this("New Scene", project);
    }

    @Override
    public void update(float delta) {
        sceneGraph.update(delta);
        sceneManager.update(delta);
        Salient.INSTANCE.getCameraController().getPerspectiveCameraController().update(delta);
        camera.update();
        compassGizmo.update();
        //        Gdx.input.setInputProcessor(Salient.INSTANCE.getCameraController());

    }

    @Override
    public void render(float delta) {

        sceneManager.render();
        grid.render(camera);
        sceneGraph.render(delta);
        Salient.INSTANCE.getCameraController().getPerspectiveCameraController().render();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void dispose() {}

    public static Scene createDefault(Project proj) {
        return new Scene("New Scene", proj);
    }

    @Profile
    public void save() {
        Serializer.INSTANCE.serializeScene(this);
    }
}
