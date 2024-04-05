package Frames;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageDragAndDrop extends JFrame {

    private JPanel canvasPanel;

    public ImageDragAndDrop() {
        setTitle("Image Drag and Drop");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Canvas panel
        canvasPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(Color.BLACK);
                g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g2d.dispose();
            }
        };
        canvasPanel.setLayout(null);
        canvasPanel.setBackground(Color.WHITE);
        canvasPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border

        // Set transfer handler and drop target
        canvasPanel.setTransferHandler(new ImageTransferHandler());

        canvasPanel.setDropTarget(new DropTarget() {
            @Override
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    Transferable transferable = evt.getTransferable();
                    if (transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                        evt.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                        ImageIcon icon = (ImageIcon) transferable.getTransferData(DataFlavor.imageFlavor);
                        Point dropPoint = evt.getLocation();
                        createImageLabel(icon, dropPoint.x, dropPoint.y);
                        evt.dropComplete(true);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                evt.rejectDrop();
            }
        });

        JScrollPane scrollPane = new JScrollPane(canvasPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Load and set image
        try {
            BufferedImage image = ImageIO.read(getClass().getResource("/Resources/bed.jpg"));
            Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(scaledImage);
            JLabel imageLabel = new JLabel(imageIcon);
            imageLabel.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
            imageLabel.setLocation(50, 50); // Initial location
            imageLabel.setTransferHandler(new ImageTransferHandler());
            canvasPanel.add(imageLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createImageLabel(ImageIcon icon, int x, int y) {
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setBounds(x - icon.getIconWidth() / 2, y - icon.getIconHeight() / 2,
                icon.getIconWidth(), icon.getIconHeight());
        imageLabel.setTransferHandler(new ImageTransferHandler());
        canvasPanel.add(imageLabel);
        canvasPanel.revalidate();
        canvasPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ImageDragAndDrop app = new ImageDragAndDrop();
            app.setVisible(true);
        });
    }
}

class ImageTransferHandler extends TransferHandler {
    @Override
    public boolean canImport(TransferSupport support) {
        return support.isDataFlavorSupported(DataFlavor.imageFlavor);
    }

    @Override
    public boolean importData(TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }

        Transferable transferable = support.getTransferable();
        try {
            ImageIcon icon = (ImageIcon) transferable.getTransferData(DataFlavor.imageFlavor);
            JComponent component = (JComponent) support.getComponent();
            Point dropPoint = support.getDropLocation().getDropPoint();
            SwingUtilities.convertPointFromScreen(dropPoint, component);
            int x = (int) dropPoint.getX();
            int y = (int) dropPoint.getY();
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setBounds(x - icon.getIconWidth() / 2, y - icon.getIconHeight() / 2,
                    icon.getIconWidth(), icon.getIconHeight());
            imageLabel.setTransferHandler(new ImageTransferHandler());
            component.add(imageLabel);
            component.revalidate();
            component.repaint();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
