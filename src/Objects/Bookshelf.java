package Objects;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.*;

public class Bookshelf extends BranchGroup {

    public Bookshelf() {
        // Create the bookshelf body
        Box body = new Box(0.3f, 0.6f, 0.1f, AppearanceUtils.createAppearance(0.5f, 0.35f, 0.2f));

        // Create shelves
        float shelfHeight = 0.1f;
        float shelfPosition = -0.3f;
        for (int i = 0; i < 4; i++) {
            Box shelf = new Box(0.25f, 0.02f, 0.1f, AppearanceUtils.createAppearance(0.5f, 0.35f, 0.2f));
            Transform3D shelfTransform = new Transform3D();
            shelfTransform.setTranslation(new Vector3f(0.0f, shelfPosition, 0.0f));
            TransformGroup shelfTG = new TransformGroup(shelfTransform);
            shelfTG.addChild(shelf);
            addChild(shelfTG);
            shelfPosition += shelfHeight;
        }

        // Add all parts to the bookshelf BranchGroup
        addChild(body);
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
