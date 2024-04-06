package Frames;

import Objects.Bed;
import Objects.Chair;
import Objects.Cupboard;
import Objects.Desk;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.ViewingPlatform;

import javax.media.j3d.Canvas3D;
import javax.vecmath.Vector3d;
import javax.imageio.ImageIO;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;



public class RoomDesigner extends JPanel {
    private List<Furniture2DItem> furnitureItems = new ArrayList<>();
    private Canvas3D canvas3D;
    private SimpleUniverse universe;
    private ImageIcon bedIcon = new ImageIcon("src/Resources/bed.jpg");
    private JTextField roomNumberField = new JTextField(10); // Initialize with appropriate columns
    private JTextField widthField = new JTextField(5); // Initialize with appropriate columns
    private JTextField heightField = new JTextField(5); // Initialize with appropriate columns


//    private static final String DEFAULT_FILE_PATH = "design.txt";

    private void saveDesign() {
        String filePath = "room_info.txt"; // File path for saving design information
        String imageFilePath = "room_design.png"; // File path for saving the screenshot
        initializeCanvas3D();
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // Write design information
            writer.println("Room Number: " + roomNumberField.getText());
            writer.println("Width: " + widthField.getText());
            writer.println("Height: " + heightField.getText());
            writer.println("Furniture Details:");

            // Write details of each furniture item
            for (Furniture2DItem item : furnitureItems) {
                writer.println("Type: " + item.getType());
                writer.println("Position: " + item.getPosition().x + ", " + item.getPosition().y);
                writer.println();
            }

            System.out.println("Design saved successfully to: " + filePath);
            JOptionPane.showMessageDialog(this, "Design saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Capture and save a screenshot of the designed canvas
            captureAndSaveScreenshot(imageFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving design: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void captureAndSaveScreenshot(String filePath) {
        try {
            // Get the location and size of the canvas component
            Rectangle canvasBounds = canvas3D.getBounds();

            // Create a Robot object
            Robot robot = new Robot();

            // Capture the screen area occupied by the canvas using the Robot object
            BufferedImage image = robot.createScreenCapture(canvasBounds);

            // Save the captured screenshot as a PNG file
            ImageIO.write(image, "png", new File(filePath));

            System.out.println("Screenshot saved successfully to: " + filePath);
        } catch (AWTException | IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeCanvas3D() {
        canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
    }

    public RoomDesigner() {
        setLayout(new BorderLayout());

        // Toolbar on the top
        JPanel topToolbarPanel = createTopToolbar();
        add(topToolbarPanel, BorderLayout.NORTH);

        // Toolbar on the left
        JPanel toolbarPanel = createToolbarPanel();
        add(toolbarPanel, BorderLayout.WEST);

        // Main content area where the room is designed
        JPanel designAreaPanel = createDesignAreaPanel();
        add(designAreaPanel, BorderLayout.CENTER);

        // Bottom buttons
        JPanel bottomButtonsPanel = createBottomButtonsPanel();
        add(bottomButtonsPanel, BorderLayout.SOUTH);
    }

    private JPanel createDesignAreaPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawRect(50, 50, 600, 400); // Draw the design canvas
            }
        };
        panel.setLayout(null);
        panel.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    Transferable t = evt.getTransferable();
                    String type = (String) t.getTransferData(DataFlavor.stringFlavor);
                    Image image = (Image) t.getTransferData(DataFlavor.imageFlavor);
                    if (image != null) {
                        Point location = evt.getLocation();
                        // Calculate the width and height from the image
                        int width = image.getWidth(null);
                        int height = image.getHeight(null);
                        // Create and add a new JLabel for the furniture item
                        JLabel furnitureLabel = new JLabel(new ImageIcon(image));
                        furnitureLabel.setBounds(location.x - width / 2, location.y - height / 2, width, height);
                        panel.add(furnitureLabel);
                        furnitureItems.add(new Furniture2DItem(type, location));
                        System.out.println("Added " + type + " at: " + location);
                        panel.revalidate();
                        panel.repaint();
                        // Add mouse listeners for moving the furniture label
                        addMouseListenerToLabel(furnitureLabel);
                        addMouseMotionListenerToLabel(furnitureLabel);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        return panel;
    }

    // Method to add mouse listener for moving the furniture label
    private void addMouseListenerToLabel(JLabel label) {
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Record the initial position of the label when mouse is pressed
                label.putClientProperty("startPoint", e.getPoint());
            }
        });
    }

    // Method to add mouse motion listener for dragging the furniture label
    private void addMouseMotionListenerToLabel(JLabel label) {
        label.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // Get the initial position of the label
                Point startPoint = (Point) label.getClientProperty("startPoint");
                if (startPoint != null) {
                    // Calculate the new position based on the mouse movement
                    int deltaX = e.getX() - startPoint.x;
                    int deltaY = e.getY() - startPoint.y;
                    Point labelLocation = label.getLocation();
                    int newX = labelLocation.x + deltaX;
                    int newY = labelLocation.y + deltaY;
                    // Set the new position of the label
                    label.setLocation(newX, newY);
                }
            }
        });
    }


    // Example method to create the top toolbar with room number, width, height inputs, and buttons.
    private JPanel createTopToolbar() {
        JPanel topToolbar = new JPanel();
        topToolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
        topToolbar.setBackground(new Color(245, 245, 245)); // Example color

        // Add Room Number input field and labels
        topToolbar.add(new JLabel("Room Number"));
        topToolbar.add(roomNumberField); // Set appropriate size

        // Add Width and Height input fields and labels
        topToolbar.add(new JLabel("Width"));
        topToolbar.add(widthField);
        topToolbar.add(new JLabel("Height"));
        topToolbar.add(heightField); // Set appropriate size

        // Add Create and Reset buttons
        JButton createButton = new JButton("Create");
        JButton resetButton = new JButton("Reset");
        // Set button colors and styles
        createButton.setBackground(new Color(0, 0, 255)); // Example color
        resetButton.setBackground(new Color(169, 169, 169)); // Example color

        topToolbar.add(createButton);
        topToolbar.add(resetButton);

        return topToolbar;
    }
    private void resetCanvas() {
//        // Clear the list of furniture items
//        furnitureItems.clear();
//
//        // Repaint the design area panel to remove all added pictures
//        JPanel designAreaPanel = (JPanel) getContentPane().getComponent(2); // Assuming designAreaPanel is the third component
//        designAreaPanel.removeAll();
//        designAreaPanel.revalidate();
//        designAreaPanel.repaint();
    }


    // Example method to create bottom buttons.
    private JPanel createBottomButtonsPanel() {
        JPanel bottomButtonsPanel = new JPanel();
        bottomButtonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        bottomButtonsPanel.setBackground(new Color(245, 245, 245)); // Example color

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveDesign()); // Attach action listener to save button

        JButton deleteButton = new JButton("Delete");
        JButton resetButton = new JButton("Reset"); // Add reset button

        // Set button colors and styles
        saveButton.setBackground(new Color(50, 205, 50)); // Example color
        deleteButton.setBackground(new Color(255, 69, 0)); // Example color

        // Attach action listener to the reset button
        resetButton.addActionListener(e -> resetCanvas());

        bottomButtonsPanel.add(saveButton);
        bottomButtonsPanel.add(deleteButton);
        bottomButtonsPanel.add(resetButton); // Add reset button

        return bottomButtonsPanel;
    }



    private JPanel createToolbarPanel() {
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.Y_AXIS));

        JLabel bedLabel = createDraggableLabel("src/Resources/bed.jpg", 100, 100, "Bed");
        toolbar.add(bedLabel);

        JLabel chairLabel = createDraggableLabel("src/Resources/chair.jpg", 50, 50, "Chair");
        toolbar.add(chairLabel);

        JLabel cupboardLabel = createDraggableLabel("src/Resources/cupboard.jpg", 100, 150, "Cupboard");
        toolbar.add(cupboardLabel);

        JLabel deskLabel = createDraggableLabel("src/Resources/bookshelf1.jpg", 150, 100, "Bookshelf");
        toolbar.add(deskLabel);

        // Add the 3D view button
        JButton view3DButton = new JButton("View in 3D");
        view3DButton.addActionListener(e -> show3DView());
        toolbar.add(view3DButton);  // Adding the button to the toolbar
        return toolbar;
    }

    private JLabel createDraggableLabel(String imagePath, int width, int height, String type) {
        try {
            ImageIcon icon = new ImageIcon(ImageIO.read(new File(imagePath)).getScaledInstance(width, height, Image.SCALE_SMOOTH));
            JLabel label = new JLabel(icon);
            label.setTransferHandler(new TransferHandler("icon") {
                @Override
                protected Transferable createTransferable(JComponent c) {
                    ImageIcon icon = (ImageIcon) ((JLabel) c).getIcon();
                    return new FurnitureTransferable(type, icon.getImage());
                }

                @Override
                public int getSourceActions(JComponent c) {
                    return COPY;
                }
            });

            // Add mouse listeners for dragging
            label.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    JComponent jc = (JComponent) e.getSource();
                    TransferHandler th = jc.getTransferHandler();
                    th.exportAsDrag(jc, e, TransferHandler.COPY);
                }
            });

            // Add mouse listener for moving
            label.addMouseMotionListener(new MouseAdapter() {
                public void mouseDragged(MouseEvent e) {
                    JComponent jc = (JComponent) e.getSource();
                    TransferHandler th = jc.getTransferHandler();
                    th.exportAsDrag(jc, e, TransferHandler.MOVE);
                }
            });

            return label;
        } catch (IOException ex) {
            ex.printStackTrace();
            return new JLabel("Image not found");
        }
    }


    // Add a light source to the scene
    private void setupLighting(BranchGroup sceneRoot) {
        // Define a bounding sphere for the light
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);

        // Define light properties
        Color3f light1Color = new Color3f(1.0f, 1.0f, 1.0f);
        Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);

        // Create directional light
        DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds);

        // Add light to the scene
        sceneRoot.addChild(light1);
    }

    private void setupInteraction(Canvas3D canvas) {
        CustomOrbitBehavior orbit = new CustomOrbitBehavior(canvas, CustomOrbitBehavior.REVERSE_ROTATE);
        orbit.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
        orbit.setRotXFactor(1.0); // Allow rotation around the X-axis
        orbit.setRotYFactor(1.0); // Allow rotation around the Y-axis
        orbit.setTransFactors(1.0, 1.0); // Allow translation along X and Y axis if needed
        // Optionally set zoom factor if you want to allow zooming:
        // orbit.setZoomFactor(1.0);

        ViewingPlatform viewingPlatform = universe.getViewingPlatform();
        viewingPlatform.setViewPlatformBehavior(orbit);
    }



    // Add method to configure initial camera position
    private void configureViewPlatform(TransformGroup viewTransformGroup) {
        Transform3D viewTransform = new Transform3D();

        // Camera slightly above and looking down at an angle
        Point3d cameraEye = new Point3d(0, 10, 10); // adjust Y for height, Z for distance from center
        Point3d cameraLookAt = new Point3d(0, 0, 0); // center of the room
        Vector3d cameraUp = new Vector3d(0, 1, 0); // Y-axis is up

        viewTransform.lookAt(cameraEye, cameraLookAt, cameraUp);
        viewTransform.invert(); // Invert the transform as lookAt creates a view matrix, not a camera matrix

        viewTransformGroup.setTransform(viewTransform);
    }


    private void show3DView() {
        JFrame frame3D = new JFrame("3D View");
        frame3D.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame3D.setSize(800, 600);

        canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        frame3D.add(canvas3D, BorderLayout.CENTER);

        universe = new SimpleUniverse(canvas3D);
        universe.getViewingPlatform().setNominalViewingTransform();

        BranchGroup scene = createSceneGraph();

        // Setup lighting in the scene
        setupLighting(scene);

        // Add scene graph to universe
        universe.addBranchGraph(scene);

        // Setup interaction for mouse control
        setupInteraction(canvas3D);

        // Configure initial camera position
        configureViewPlatform(universe.getViewingPlatform().getViewPlatformTransform());

        // Make frame visible
        frame3D.setVisible(true);

        System.out.println("3D view displayed");
    }


    private Node createFloor() {
        // The color and texture for the floor
        Appearance floorAppearance = new Appearance();
        Color3f color = new Color3f(0.5f, 0.5f, 0.5f);
        ColoringAttributes ca = new ColoringAttributes(color, ColoringAttributes.NICEST);
        floorAppearance.setColoringAttributes(ca);

        // Creating a box representing the floor with the correct size
        Box floor = new Box(6f, 0.01f, 6f, Primitive.GENERATE_NORMALS, floorAppearance);

        // Ensure the floor is not pickable
        floor.setPickable(false);

        // Positioning the floor correctly in the scene
        TransformGroup floorTG = new TransformGroup();
        Transform3D transform = new Transform3D();
        Transform3D floorTransform = new Transform3D();
        Transform3D floorTrans = new Transform3D();
        floorTrans.setIdentity(); // Resets the transform to identity
        floorTrans.setTranslation(new Vector3f(0f, -0.01f, 0f)); // Apply translation to lower the floor slightly below the origin along the Y-axis
        floorTG.setTransform(floorTrans);
        floorTG.addChild(floor);

        return floorTG;
    }



    private BranchGroup createSceneGraph() {
        BranchGroup objRoot = new BranchGroup();
        objRoot.addChild(createFloor());
        for (Furniture2DItem item : furnitureItems) {
            System.out.println("Creating 3D object for type: " + item.getType());
            TransformGroup tg = createTransformGroupForItem(item);
            objRoot.addChild(tg);
        }
        return objRoot;
    }

    private TransformGroup createTransformGroupForItem(Furniture2DItem item) {
        String type = item.getType();
        Point position = item.getPosition();

        // Create the transform group and set its capabilities to allow runtime changes
        TransformGroup tg = new TransformGroup();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        // Create the transformation object and apply the scale
        Transform3D transform = new Transform3D();
        double scaleFactor = getScaleFactor(type);
        transform.setScale(new Vector3d(scaleFactor, scaleFactor, scaleFactor));

        // Translate the position, adjusting for the furniture's base height
        Vector3f position3D = translate2DPositionTo3D(position);
        position3D.y = getBaseHeight(type) / 2; // Set Y to half the base height
        transform.setTranslation(position3D);

        // Set the transformation to the transform group
        tg.setTransform(transform);

        // Add the actual 3D model of the furniture
        Node furniture3D = get3DModelForType(type);
        tg.addChild(furniture3D);

        return tg;
    }

    private double getScaleFactor(String type) {
        return switch (type) {
            case "Chair" -> 0.8;
            case "Bed" -> 1.5;
            case "Cupboard" -> 1.3;
            case "Desk" -> 1.25;
            default -> 1.0; // Default scale factor if type is unknown
        };
    }

    private Node get3DModelForType(String type) {
        switch (type) {
            case "Chair":
                return new Chair();
            case "Bed":
                return new Bed();
            case "Cupboard":
                return new Cupboard();
            case "Desk":
                return new Desk();
            default:
                return new BranchGroup(); // Placeholder for unknown types
        }
    }

    private float getBaseHeight(String type) {
        // You'll need to provide the actual base height for each furniture type
        return switch (type) {
            case "Chair" -> 0.5f;
            case "Bed" -> 0.8f;
            case "Cupboard" -> 1.0f;
            case "Desk" -> 0.7f;
            default -> 0.5f; // Default base height
        };
    }


    // Translate the 2D screen position to a 3D world position
    private Vector3f translate2DPositionTo3D(Point position) {
        // These ratios convert 2D positions to the 3D world size
        float xRatio = 12f / 600f; // Width of the room in 3D / width of the panel in 2D
        float zRatio = 12f / 400f; // Depth of the room in 3D / height of the panel in 2D

        float x3d = position.x * xRatio - 6f; // Adjust to center
        float z3d = position.y * zRatio - 6f; // Adjust to center
        // The y3d will be set based on the furniture's base height in the method above

        return new Vector3f(x3d, 0f, z3d); // Initially setting y to 0
    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                RoomDesigner designer = new RoomDesigner();
                designer.setVisible(true);
            }
        });
    }
}

//class Furniture2DItem {
//    private String type;
//    private Point position;
//
//    public Furniture2DItem(String type, Point position) {
//        this.type = type;
//        this.position = position;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public Point getPosition() {
//        return position;
//    }
//}

class FurnitureTransferable implements Transferable {
    private String type;
    private Image image;

    public FurnitureTransferable(String type, Image image) {
        this.type = type;
        this.image = image;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{DataFlavor.stringFlavor, DataFlavor.imageFlavor};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return DataFlavor.stringFlavor.equals(flavor) || DataFlavor.imageFlavor.equals(flavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) {
        if (DataFlavor.stringFlavor.equals(flavor)) {
            return type;
        }
        if (DataFlavor.imageFlavor.equals(flavor)) {
            return image;
        }
        return null;
    }
}
class RestrictedOrbitBehavior extends OrbitBehavior {
    public RestrictedOrbitBehavior(javax.media.j3d.Canvas3D canvas, int flags) {
        super(canvas, flags);
        setRotFactors(1.0, 0.0); // Disallow vertical rotation.
    }
}

interface OrbitBehaviorInterface {
    void updateViewPlatform();
}

class CustomOrbitBehavior extends OrbitBehavior implements OrbitBehaviorInterface {
    public static final int REVERSE_ROTATE = 2; // Define custom constant for reverse rotate

    private int rotationAxis;

    public CustomOrbitBehavior(Canvas3D canvas, int rotationAxis) {
        super(canvas, OrbitBehavior.REVERSE_ALL);
        this.rotationAxis = rotationAxis;
    }

    @Override
    public void updateViewPlatform() {
        // Get the current viewing transform
        TransformGroup viewingTG = getViewingPlatform().getViewPlatformTransform();
        Transform3D currentRotation = new Transform3D();
        viewingTG.getTransform(currentRotation);

        viewingTG.setTransform(currentRotation);
    }
}