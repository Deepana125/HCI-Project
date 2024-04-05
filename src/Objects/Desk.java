package Objects;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.*;

public class Desk extends BranchGroup {

    public Desk() {
        // Create the tabletop
        Box tabletop = new Box(0.4f, 0.02f, 0.3f, AppearanceUtils.createAppearance(0.5f, 0.35f, 0.05f));

        // Create the legs
        Cylinder leg1 = new Cylinder(0.02f, 0.3f, AppearanceUtils.createAppearance(0.5f, 0.35f, 0.05f));
        Cylinder leg2 = new Cylinder(0.02f, 0.3f, AppearanceUtils.createAppearance(0.5f, 0.35f, 0.05f));
        Cylinder leg3 = new Cylinder(0.02f, 0.3f, AppearanceUtils.createAppearance(0.5f, 0.35f, 0.05f));
        Cylinder leg4 = new Cylinder(0.02f, 0.3f, AppearanceUtils.createAppearance(0.5f, 0.35f, 0.05f));

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

        // Create the central support beam
        Box supportBeam = new Box(0.02f, 0.3f, 0.02f, AppearanceUtils.createAppearance(0.5f, 0.35f, 0.05f));

        // Position the support beam
        Transform3D supportBeamTransform = new Transform3D();
        supportBeamTransform.setTranslation(new Vector3f(0.0f, -0.15f, 0.0f));
        TransformGroup supportBeamTG = new TransformGroup(supportBeamTransform);
        supportBeamTG.addChild(supportBeam);

        // Add all parts to the desk BranchGroup
        addChild(tabletop);
        addChild(leg1TG);
        addChild(leg2TG);
        addChild(leg3TG);
        addChild(leg4TG);
        addChild(supportBeamTG);
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
