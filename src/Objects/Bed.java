package Objects;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.*;

public class Bed extends BranchGroup {

    public Bed() {
        // Create the bed frame
        Box bedFrame = new Box(0.4f, 0.02f, 0.6f, AppearanceUtils.createAppearance(0.6f, 0.4f, 0.2f));

        // Create the legs
        Cylinder leg1 = new Cylinder(0.02f, 0.3f, AppearanceUtils.createAppearance(0.6f, 0.4f, 0.2f));
        Cylinder leg2 = new Cylinder(0.02f, 0.3f, AppearanceUtils.createAppearance(0.6f, 0.4f, 0.2f));
        Cylinder leg3 = new Cylinder(0.02f, 0.3f, AppearanceUtils.createAppearance(0.6f, 0.4f, 0.2f));
        Cylinder leg4 = new Cylinder(0.02f, 0.3f, AppearanceUtils.createAppearance(0.6f, 0.4f, 0.2f));

        // Position the legs
        Transform3D legTransform = new Transform3D();
        legTransform.setTranslation(new Vector3f(-0.35f, -0.15f, 0.25f));
        TransformGroup leg1TG = new TransformGroup(legTransform);
        leg1TG.addChild(leg1);

        legTransform.setTranslation(new Vector3f(0.35f, -0.15f, 0.25f));
        TransformGroup leg2TG = new TransformGroup(legTransform);
        leg2TG.addChild(leg2);

        legTransform.setTranslation(new Vector3f(-0.35f, -0.15f, -0.25f));
        TransformGroup leg3TG = new TransformGroup(legTransform);
        leg3TG.addChild(leg3);

        legTransform.setTranslation(new Vector3f(0.35f, -0.15f, -0.25f));
        TransformGroup leg4TG = new TransformGroup(legTransform);
        leg4TG.addChild(leg4);

        // Create the headboard
        Box headboard = new Box(0.4f, 0.3f, 0.02f, AppearanceUtils.createAppearance(0.6f, 0.4f, 0.2f));

        // Position the headboard
        Transform3D headboardTransform = new Transform3D();
        headboardTransform.setTranslation(new Vector3f(0.0f, 0.15f, -0.3f));
        TransformGroup headboardTG = new TransformGroup(headboardTransform);
        headboardTG.addChild(headboard);

        // Create the footboard
        Box footboard = new Box(0.4f, 0.3f, 0.02f, AppearanceUtils.createAppearance(0.6f, 0.4f, 0.2f));

        // Position the footboard
        Transform3D footboardTransform = new Transform3D();
        footboardTransform.setTranslation(new Vector3f(0.0f, 0.15f, 0.3f));
        TransformGroup footboardTG = new TransformGroup(footboardTransform);
        footboardTG.addChild(footboard);

        // Create the mattress
        Box mattress = new Box(0.38f, 0.02f, 0.58f, AppearanceUtils.createAppearance(0.8f, 0.8f, 0.8f));

        // Position the mattress
        Transform3D mattressTransform = new Transform3D();
        mattressTransform.setTranslation(new Vector3f(0.0f, 0.32f, 0.0f));
        TransformGroup mattressTG = new TransformGroup(mattressTransform);
        mattressTG.addChild(mattress);

        // Create the pillows
        Box pillow1 = new Box(0.1f, 0.1f, 0.1f, AppearanceUtils.createAppearance(1.0f, 1.0f, 1.0f));
        Box pillow2 = new Box(0.1f, 0.1f, 0.1f, AppearanceUtils.createAppearance(1.0f, 1.0f, 1.0f));

        // Position the pillows
        Transform3D pillow1Transform = new Transform3D();
        pillow1Transform.setTranslation(new Vector3f(-0.3f, 0.3f, -0.25f));
        TransformGroup pillow1TG = new TransformGroup(pillow1Transform);
        pillow1TG.addChild(pillow1);

        Transform3D pillow2Transform = new Transform3D();
        pillow2Transform.setTranslation(new Vector3f(0.3f, 0.3f, -0.25f));
        TransformGroup pillow2TG = new TransformGroup(pillow2Transform);
        pillow2TG.addChild(pillow2);

        // Add all parts to the bed BranchGroup
        addChild(bedFrame);
        addChild(leg1TG);
        addChild(leg2TG);
        addChild(leg3TG);
        addChild(leg4TG);
        addChild(headboardTG);
        addChild(footboardTG);
        addChild(mattressTG);
        addChild(pillow1TG);
        addChild(pillow2TG);
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
