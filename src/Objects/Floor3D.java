package Objects;

import javax.swing.*;
import java.awt.event.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;

public class Floor3D extends JFrame implements ActionListener {
    private JButton showFloorButton;
    private BranchGroup scene;
    private Switch floorSwitch;

    public Floor3D() {
        super("3D Floor Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create button to show the floor
        showFloorButton = new JButton("Show Floor");
        showFloorButton.addActionListener(this);

        // Add button to frame
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(showFloorButton);
        getContentPane().add(buttonPanel, "South");

        // Create 3D scene
        scene = createSceneGraph();
        SimpleUniverse universe = new SimpleUniverse();
        universe.getViewingPlatform().setNominalViewingTransform();
        universe.addBranchGraph(scene);

        pack();
        setVisible(true);
    }

    private BranchGroup createSceneGraph() {
        BranchGroup root = new BranchGroup();

        // Create a switch node to control the visibility of the floor
        floorSwitch = new Switch();
        floorSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);

        // Add floor to the switch node
        Node floor = createFloor();
        floorSwitch.addChild(floor);

        // Set the switch node to be visible initially
        floorSwitch.setWhichChild(Switch.CHILD_ALL);

        // Add the switch node to the scene
        root.addChild(floorSwitch);

        return root;
    }

    private Node createFloor() {
        // Create a simple appearance with a solid color texture
        Appearance floorAppearance = new Appearance();
        Color3f color = new Color3f(0.7f, 0.7f, 0.7f); // Light gray
        ColoringAttributes ca = new ColoringAttributes(color, ColoringAttributes.NICEST);
        floorAppearance.setColoringAttributes(ca);

        // Create a box to represent the floor
        Box floorBox = new Box(10f, 0.01f, 10f, Primitive.GENERATE_NORMALS, floorAppearance);
        floorBox.setPickable(false);

        // Transform group to position the floor in the scene
        TransformGroup tg = new TransformGroup();
        Transform3D t3d = new Transform3D();
        t3d.setTranslation(new Vector3d(0.0, -0.01, 0.0)); // Lower the floor slightly to avoid z-fighting
        tg.setTransform(t3d);
        tg.addChild(floorBox); // Add the floor Box directly to the TransformGroup

        return tg; // Return the TransformGroup with the floor
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == showFloorButton) {
            // Toggle floor visibility
            showFloor();
        }
    }

    private void showFloor() {
        // Toggle the visibility of the switch node
        if (floorSwitch.getWhichChild() == Switch.CHILD_NONE) {
            floorSwitch.setWhichChild(Switch.CHILD_ALL);
        } else {
            floorSwitch.setWhichChild(Switch.CHILD_NONE);
        }
    }

    public static void main(String[] args) {
        new Floor3D();
    }
}
