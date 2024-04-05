package Objects;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.*;

public class Cupboard extends BranchGroup {

    public Cupboard() {
        // Create the cupboard body
        Box body = new Box(0.3f, 0.5f, 0.1f, AppearanceUtils.createAppearance(0.5f, 0.35f, 0.2f));

        // Create the left door
        Box leftDoor = new Box(0.15f, 0.5f, 0.02f, AppearanceUtils.createAppearance(0.5f, 0.35f, 0.2f));
        Transform3D leftDoorTransform = new Transform3D();
        leftDoorTransform.setTranslation(new Vector3f(-0.15f, 0.0f, 0.1f));
        TransformGroup leftDoorTG = new TransformGroup(leftDoorTransform);
        leftDoorTG.addChild(leftDoor);

        // Create the right door
        Box rightDoor = new Box(0.15f, 0.5f, 0.02f, AppearanceUtils.createAppearance(0.5f, 0.35f, 0.2f));
        Transform3D rightDoorTransform = new Transform3D();
        rightDoorTransform.setTranslation(new Vector3f(0.15f, 0.0f, 0.1f));
        TransformGroup rightDoorTG = new TransformGroup(rightDoorTransform);
        rightDoorTG.addChild(rightDoor);

        // Create the handle
        Cylinder handle = new Cylinder(0.01f, 0.1f, AppearanceUtils.createAppearance(0.7f, 0.7f, 0.7f));
        Transform3D handleTransform = new Transform3D();
        handleTransform.setTranslation(new Vector3f(0.12f, 0.2f, 0.12f));
        TransformGroup handleTG = new TransformGroup(handleTransform);
        handleTG.addChild(handle);

        // Add all parts to the cupboard BranchGroup
        addChild(body);
        addChild(leftDoorTG);
        addChild(rightDoorTG);
        addChild(handleTG);
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
