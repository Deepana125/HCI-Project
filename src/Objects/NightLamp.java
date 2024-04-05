package Objects;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.*;

public class NightLamp extends BranchGroup {

    public NightLamp() {
        // Create the lampshade
        Cone lampshade = new Cone(0.1f, 0.2f, AppearanceUtils.createAppearance(0.8f, 0.8f, 0.2f));

        // Create the base
        Cylinder base = new Cylinder(0.1f, 0.02f, AppearanceUtils.createAppearance(0.4f, 0.4f, 0.4f));

        // Position the lampshade
        Transform3D lampshadeTransform = new Transform3D();
        lampshadeTransform.setTranslation(new Vector3f(0.0f, 0.1f, 0.0f));
        TransformGroup lampshadeTG = new TransformGroup(lampshadeTransform);
        lampshadeTG.addChild(lampshade);

        // Add all parts to the lamp BranchGroup
        addChild(lampshadeTG);
        addChild(base);
    }
    public class AppearanceUtils {

        // Utility method to create a simple appearance with a specified color
        public static Appearance createAppearance(float r, float g, float b) {
            Color3f color = new Color3f(r, g, b);
            ColoringAttributes coloringAttributes = new ColoringAttributes(color, ColoringAttributes.SHADE_GOURAUD);
            Appearance appearance = new Appearance();
            appearance.setColoringAttributes(coloringAttributes);
            return appearance;
        }
    }
}
