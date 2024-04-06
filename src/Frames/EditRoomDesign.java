package Frames;

import Objects.Bed;
import Objects.Chair;
import Objects.Cupboard;
import Objects.Desk;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

import javax.imageio.ImageIO;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class EditRoomDesign extends JFrame {
    private String roomNumber;
    private List<Furniture2DItem> furnitureItems = new ArrayList<>();
    private Canvas3D canvas3D;
    private SimpleUniverse universe;
    private BranchGroup scene;
    private JTextField roomNumberField = new JTextField(10);
    private JTextField widthField = new JTextField(5);
    private JTextField heightField = new JTextField(5);
    private JPanel designAreaPanel;

    public EditRoomDesign(String roomNumber) {
        this.roomNumber = roomNumber;
        initializeUI();
        initializeCanvas3D(); // Ensure the canvas is initialized before setting up the scene
        initializeDesignArea(); // Make sure this is done before you load room details
        loadRoomDetails(); // Now it's safe to load room details because the design area panel has been initialized
        getContentPane().add(createToolbarPanel(), BorderLayout.WEST); // Add the toolbar after all initializations
    }


    private void initializeUI() {
        setTitle("Edit Room Design: " + roomNumber);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Room Number:"));
        topPanel.add(roomNumberField);
        topPanel.add(new JLabel("Width:"));
        topPanel.add(widthField);
        topPanel.add(new JLabel("Height:"));
        topPanel.add(heightField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveRoomDesign();
            }
        });

        add(topPanel, BorderLayout.NORTH);
        add(saveButton, BorderLayout.SOUTH);

        canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        add(canvas3D, BorderLayout.CENTER);
    }

    private void initializeCanvas3D() {
        canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        universe = new SimpleUniverse(canvas3D);
        universe.getViewingPlatform().setNominalViewingTransform();
    }

    private void initializeDesignArea() {
        designAreaPanel = createDesignAreaPanel(); // This line must correctly initialize designAreaPanel
        add(designAreaPanel, BorderLayout.CENTER);
        setupSceneGraph(); // Make sure scene graph is also initialized
    }

    private void setupSceneGraph() {
        scene = new BranchGroup();
        scene.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        scene.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);

        for (Furniture2DItem item : furnitureItems) {
            addFurnitureToScene(item);
            TransformGroup tg = createTransformGroupForItem(item);
            scene.addChild(tg);
        }

        universe.addBranchGraph(scene);
    }

    private void addFurnitureToScene(Furniture2DItem item) {
        TransformGroup tg = createTransformGroupForItem(item);
        scene.addChild(tg);
    }

    private TransformGroup createTransformGroupForItem(Furniture2DItem item) {
        String type = item.getType();
        Point position = item.getPosition();

        TransformGroup tg = new TransformGroup();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        Transform3D transform = new Transform3D();
        double scaleFactor = getScaleFactor(type);
        transform.setScale(new Vector3d(scaleFactor, scaleFactor, scaleFactor));

        Vector3f position3D = translate2DPositionTo3D(position);
        position3D.y = getBaseHeight(type) / 2; // Set Y to half the base height
        transform.setTranslation(position3D);

        tg.setTransform(transform);

        Node furniture3D = get3DModelForType(type);
        tg.addChild(furniture3D);

        return tg;
    }

    private void loadRoomDetails() {
        File file = new File("room_info.txt");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("Room Number: " + roomNumber)) {
                    roomNumberField.setText(line.split(": ")[1]);
                    widthField.setText(scanner.nextLine().split(": ")[1]);
                    heightField.setText(scanner.nextLine().split(": ")[1]);
                    scanner.nextLine(); // Skip the "Furniture Details:" line

                    designAreaPanel.removeAll(); // Clear the panel before adding new items

                    while (scanner.hasNextLine()) {
                        line = scanner.nextLine();
                        if (line.isEmpty()) break;
                        String[] parts = line.split(": ");
                        if (parts.length < 2 || !"Type".equals(parts[0])) continue;
                        String type = parts[1];
                        line = scanner.nextLine();
                        parts = line.split(": ");
                        if (parts.length < 2 || !"Position".equals(parts[0])) continue;
                        String[] positionParts = parts[1].split(", ");
                        if (positionParts.length < 2) {
                            System.err.println("Invalid position data for furniture item");
                            continue;
                        }
                        try {
                            int x = Integer.parseInt(positionParts[0].trim());
                            int y = Integer.parseInt(positionParts[1].trim());
                            Image scaledImage = getScaledImageForType(type, 100, 100);
                            ImageIcon icon = new ImageIcon(scaledImage);
                            JLabel label = new JLabel(icon);
                            label.setBounds(x, y, 100, 100); // Scaled image size
                            makeLabelDraggable(label);
                            designAreaPanel.add(label);
                            furnitureItems.add(new Furniture2DItem(type, new Point(x, y)));
                        } catch (NumberFormatException ex) {
                            System.err.println("Error parsing position for type: " + type);
                        }
                    }
                    designAreaPanel.revalidate();
                    designAreaPanel.repaint();
                    break; // Room loaded, break the loop
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Image getScaledImageForType(String type, int width, int height) throws IOException {
        String imagePath = getImagePathForType(type);
        BufferedImage originalImage = ImageIO.read(new File(imagePath));
        return originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }


    private void makeLabelDraggable(JLabel label) {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            Point lastPoint;

            @Override
            public void mousePressed(MouseEvent e) {
                lastPoint = e.getPoint(); // Remember where the mouse was clicked
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // Calculate the distance moved since last event
                int deltaX = e.getX() - lastPoint.x;
                int deltaY = e.getY() - lastPoint.y;

                // Move the label by this distance
                Point newLocation = label.getLocation();
                newLocation.translate(deltaX, deltaY);
                label.setLocation(newLocation);

                // Save the current point for the next call
                lastPoint = e.getPoint();
            }
        };

        // Add the listeners to the label
        label.addMouseListener(mouseAdapter);
        label.addMouseMotionListener(mouseAdapter);
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

        JButton view3DButton = new JButton("View in 3D");
        view3DButton.addActionListener(e -> show3DView());
        toolbar.add(view3DButton);  // Adding the button to the toolbar
        return toolbar;
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

        setupLighting(scene);

        universe.addBranchGraph(scene);

        setupInteraction(canvas3D);

        configureViewPlatform(universe.getViewingPlatform().getViewPlatformTransform());

        frame3D.setVisible(true);

        System.out.println("3D view displayed");
    }

    private void setupInteraction(Canvas3D canvas) {
        CustomOrbitBehavior orbit = new CustomOrbitBehavior(canvas, CustomOrbitBehavior.REVERSE_ROTATE);
        orbit.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
        orbit.setRotXFactor(1.0);
        orbit.setRotYFactor(1.0);
        orbit.setTransFactors(1.0, 1.0);
        ViewingPlatform viewingPlatform = universe.getViewingPlatform();
        viewingPlatform.setViewPlatformBehavior(orbit);
    }

    private void configureViewPlatform(TransformGroup viewTransformGroup) {
        Transform3D viewTransform = new Transform3D();

        Point3d cameraEye = new Point3d(0, 10, 10);
        Point3d cameraLookAt = new Point3d(0, 0, 0);
        Vector3d cameraUp = new Vector3d(0, 1, 0);

        viewTransform.lookAt(cameraEye, cameraLookAt, cameraUp);
        viewTransform.invert();

        viewTransformGroup.setTransform(viewTransform);
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

    private void setupLighting(BranchGroup sceneRoot) {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);

        Color3f light1Color = new Color3f(1.0f, 1.0f, 1.0f);
        Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);

        DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds);

        sceneRoot.addChild(light1);
    }

    private String getImagePathForType(String type) {
        switch (type) {
            case "Cupboard":
                return "src/Resources/cupboard.jpg"; // Replace with actual image path
            // Add cases for other types
            default:
                return null;
        }
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

            label.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    JComponent jc = (JComponent) e.getSource();
                    TransferHandler th = jc.getTransferHandler();
                    th.exportAsDrag(jc, e, TransferHandler.COPY);
                }
            });

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

    private Node createFloor() {
        // The color and texture for the floor
        Appearance floorAppearance = new Appearance();
        Color3f color = new Color3f(0.5f, 0.5f, 0.5f);
        ColoringAttributes ca = new ColoringAttributes(color, ColoringAttributes.NICEST);
        floorAppearance.setColoringAttributes(ca);

        // Creating a box representing the floor with the correct size
        com.sun.j3d.utils.geometry.Box floor = new Box(6f, 0.01f, 6f, Primitive.GENERATE_NORMALS, floorAppearance);

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

    private void saveRoomDesign() {
        try (FileWriter writer = new FileWriter("room_info.txt", true)) { // Append mode
            writer.write("Room Number: " + roomNumberField.getText() + "\n");
            writer.write("Width: " + widthField.getText() + "\n");
            writer.write("Height: " + heightField.getText() + "\n");
            writer.write("Furniture Details:\n");
            for (Furniture2DItem item : furnitureItems) {
                writer.write("Type: " + item.getType() + "\n");
                writer.write("Position: " + item.getPosition().x + ", " + item.getPosition().y + "\n\n");
            }
            JOptionPane.showMessageDialog(this, "Room design saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to save room design.", "Error", JOptionPane.ERROR_MESSAGE);
        }
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
        SwingUtilities.invokeLater(() -> {
            EditRoomDesign editor = new EditRoomDesign("12"); // Example room number
            editor.setVisible(true);
        });
    }
}
