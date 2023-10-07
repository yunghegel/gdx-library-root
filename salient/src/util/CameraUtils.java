package util;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import app.Salient;

public class CameraUtils {

    public static Vector3 cameraRayXZPlaneIntersection(Vector3 cameraPosition, Vector3 cameraDirection) {
//        Plane level on horizon
        Plane plane = new Plane(new Vector3(0, 1, 0), 0);
        Vector3 intersection = new Vector3();
//        pick ray from center of screen
        Ray ray = Salient.INSTANCE.getViewport().getPickRay(Salient.INSTANCE.getViewport().getScreenWidth() / 2, Salient.INSTANCE.getViewport().getScreenHeight() / 2);
//        intersect ray with plane
        if (Intersector.intersectRayPlane(ray, plane, intersection)) {
            return intersection;
        } else {
            return new Vector3();
    }

    }

}
