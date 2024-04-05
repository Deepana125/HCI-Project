package Objects;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.*;

public class Sofa extends BranchGroup {

    public Sofa() {
        // Create the seat
        Box seat = new Box(0.4f, 0.2f, 0.6f, AppearanceUtils.createAppearance(0.7f, 0.5f, 0.3f));

        // Create the backrest
        Box backrest = new Box(0.4f, 0.25f, 0.02f, AppearanceUtils.createAppearance(0.7f, 0.5f, 0.3f));
        Transform3D backrestTransform = new Transform3D();
        backrestTransform.setTranslation(new Vector3f(0.0f, 0.2f, 0.3f));
        TransformGroup backrestTG = new TransformGroup(backrestTransform);
        backrestTG.addChild(backrest);

        // Create the armrests
        Box leftArmrest = new Box(0.02f, 0.2f, 0.3f, AppearanceUtils.createAppearance(0.7f, 0.5f, 0.3f));
        Transform3D leftArmrestTransform = new Transform3D();
        leftArmrestTransform.setTranslation(new Vector3f(-0.38f, 0.1f, 0.3f));
        TransformGroup leftArmrestTG = new TransformGroup(leftArmrestTransform);
        leftArmrestTG.addChild(leftArmrest);

        Box rightArmrest = new Box(0.02f, 0.2f, 0.3f, AppearanceUtils.createAppearance(0.7f, 0.5f, 0.3f));
        Transform3D rightArmrestTransform = new Transform3D();
        rightArmrestTransform.setTranslation(new Vector3f(0.38f, 0.1f, 0.3f));
        TransformGroup rightArmrestTG = new TransformGroup(rightArmrestTransform);
        rightArmrestTG.addChild(rightArmrest);

        // Create the legs
        Cylinder leg1 = new Cylinder(0.02f, 0.2f, AppearanceUtils.createAppearance(0.7f, 0.5f, 0.3f));
        Cylinder leg2 = new Cylinder(0.02f, 0.2f, AppearanceUtils.createAppearance(0.7f, 0.5f, 0.3f));
        Cylinder leg3 = new Cylinder(0.02f, 0.2f, AppearanceUtils.createAppearance(0.7f, 0.5f, 0.3f));
        Cylinder leg4 = new Cylinder(0.02f, 0.2f, AppearanceUtils.createAppearance(0.7f, 0.5f, 0.3f));
        Transform3D leg1Transform = new Transform3D();
        leg1Transform.setTranslation(new Vector3f(-0.35f, -0.2f, 0.55f));
        TransformGroup leg1TG = new TransformGroup(leg1Transform);
        leg1TG.addChild(leg1);
        Transform3D leg2Transform = new Transform3D();
        leg2Transform.setTranslation(new Vector3f(0.35f, -0.2f, 0.55f));
        TransformGroup leg2TG = new TransformGroup(leg2Transform);
        leg2TG.addChild(leg2);
        Transform3D leg3Transform = new Transform3D();
        leg3Transform.setTranslation(new Vector3f(-0.35f, -0.2f, 0.05f));
        TransformGroup leg3TG = new TransformGroup(leg3Transform);
        leg3TG.addChild(leg3);
        Transform3D leg4Transform = new Transform3D();
        leg4Transform.setTranslation(new Vector3f(0.35f, -0.2f, 0.05f));
        TransformGroup leg4TG = new TransformGroup(leg4Transform);
        leg4TG.addChild(leg4);

        // Add all parts to the sofa BranchGroup
        addChild(seat);
        addChild(backrestTG);
        addChild(leftArmrestTG);
        addChild(rightArmrestTG);
        addChild(leg1TG);
        addChild(leg2TG);
        addChild(leg3TG);
        addChild(leg4TG);
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
