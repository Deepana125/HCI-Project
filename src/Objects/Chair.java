package Objects;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Chair extends BranchGroup {
    public Chair() {
        // Create materials
        Material material = new Material();
        material.setDiffuseColor(new Color3f(0.5f, 0.5f, 0.5f)); // Gray
        material.setSpecularColor(new Color3f(1.0f, 1.0f, 1.0f)); // White
        material.setShininess(100.0f);

        // Create geometry for chair parts
        Appearance appearance = new Appearance();
        appearance.setMaterial(material);

        // Create seat
        Box seat = new Box(0.5f, 0.05f, 0.5f, appearance);

        // Create backrest
        Box backrest = new Box(0.05f, 0.5f, 0.5f, appearance);

        // Create legs
        Box leg1 = new Box(0.05f, 0.5f, 0.05f, appearance);
        Box leg2 = new Box(0.05f, 0.5f, 0.05f, appearance);
        Box leg3 = new Box(0.05f, 0.5f, 0.05f, appearance);
        Box leg4 = new Box(0.05f, 0.5f, 0.05f, appearance);

        // Translate each part to its position
        Transform3D seatPosition = new Transform3D();
        Transform3D backrestPosition = new Transform3D();
        Transform3D leg1Position = new Transform3D();
        Transform3D leg2Position = new Transform3D();
        Transform3D leg3Position = new Transform3D();
        Transform3D leg4Position = new Transform3D();

        seatPosition.setTranslation(new Vector3f(0.0f, 0.5f, 0.0f));
        backrestPosition.setTranslation(new Vector3f(-0.45f, 0.75f, 0.0f));
        leg1Position.setTranslation(new Vector3f(-0.45f, 0.0f, -0.45f));
        leg2Position.setTranslation(new Vector3f(-0.45f, 0.0f, 0.45f));
        leg3Position.setTranslation(new Vector3f(0.45f, 0.0f, -0.45f));
        leg4Position.setTranslation(new Vector3f(0.45f, 0.0f, 0.45f));

        TransformGroup chairPartsTG = new TransformGroup();
        chairPartsTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        TransformGroup seatTG = new TransformGroup(seatPosition);
        TransformGroup backrestTG = new TransformGroup(backrestPosition);
        TransformGroup leg1TG = new TransformGroup(leg1Position);
        TransformGroup leg2TG = new TransformGroup(leg2Position);
        TransformGroup leg3TG = new TransformGroup(leg3Position);
        TransformGroup leg4TG = new TransformGroup(leg4Position);

        seatTG.addChild(seat);
        backrestTG.addChild(backrest);
        leg1TG.addChild(leg1);
        leg2TG.addChild(leg2);
        leg3TG.addChild(leg3);
        leg4TG.addChild(leg4);

        chairPartsTG.addChild(seatTG);
        chairPartsTG.addChild(backrestTG);
        chairPartsTG.addChild(leg1TG);
        chairPartsTG.addChild(leg2TG);
        chairPartsTG.addChild(leg3TG);
        chairPartsTG.addChild(leg4TG);

        addChild(chairPartsTG);

        // Add lighting
        addLighting();
    }

    private void addLighting() {
        // Directional light
        DirectionalLight light = new DirectionalLight(new Color3f(1.0f, 1.0f, 1.0f), new Vector3f(-1.0f, -1.0f, -1.0f));
        light.setInfluencingBounds(new BoundingSphere());
        addChild(light);

        // Ambient light
        AmbientLight ambientLight = new AmbientLight(new Color3f(0.5f, 0.5f, 0.5f));
        ambientLight.setInfluencingBounds(new BoundingSphere());
        addChild(ambientLight);
    }
}
