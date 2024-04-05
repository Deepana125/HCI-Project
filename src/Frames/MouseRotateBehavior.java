package Frames;

import javax.media.j3d.*;
import java.awt.event.MouseEvent;
import java.awt.AWTEvent;
import java.util.Enumeration;
import javax.vecmath.Point3d;

public class MouseRotateBehavior extends Behavior {

    private TransformGroup targetTG;
    private Transform3D transform = new Transform3D();
    private Transform3D rotation = new Transform3D();
    private double angle = 0.0;
    private boolean isRotating = false;

    private WakeupCriterion[] mouseEvents = {
            new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED),
            new WakeupOnAWTEvent(MouseEvent.MOUSE_DRAGGED),
            new WakeupOnAWTEvent(MouseEvent.MOUSE_RELEASED)
    };

    public MouseRotateBehavior(TransformGroup targetTG) {
        this.targetTG = targetTG;
    }

    @Override
    public void initialize() {
        WakeupCondition mouseCriterion = new WakeupOr(mouseEvents);
        wakeupOn(mouseCriterion);
    }

    @Override
    public void processStimulus(Enumeration criteria) {
        while (criteria.hasMoreElements()) {
            WakeupCriterion wakeup = (WakeupCriterion) criteria.nextElement();
            if (wakeup instanceof WakeupOnAWTEvent) {
                AWTEvent[] events = ((WakeupOnAWTEvent) wakeup).getAWTEvent();
                for (AWTEvent event : events) {
                    if (event instanceof MouseEvent) {
                        MouseEvent mouseEvent = (MouseEvent) event;
                        processMouseEvent(mouseEvent);
                    }
                }
            }
        }
        wakeupOn(new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED)); // Reactivate the behavior
    }

    private void processMouseEvent(MouseEvent evt) {
        if (evt.getID() == MouseEvent.MOUSE_PRESSED) {
            angle = 0.0;
            isRotating = true;
        } else if (evt.getID() == MouseEvent.MOUSE_RELEASED) {
            isRotating = false;
        } else if (evt.getID() == MouseEvent.MOUSE_DRAGGED && isRotating) {
            int mouseX = evt.getX();
            int mouseY = evt.getY();

            transform.setIdentity();
            targetTG.getTransform(transform);

            // Calculate the center of the bounds
            BoundingSphere bounds = (BoundingSphere) targetTG.getBounds();
            Point3d center = new Point3d();
            bounds.getCenter(center);

            // Calculate the difference between mouse coordinates and the center
            double dx = mouseX - center.x;
            double dy = mouseY - center.y;

            angle = Math.atan2(dy, dx);
            rotation.rotY(angle);

            transform.mul(rotation);
            targetTG.setTransform(transform);
        }
    }
}
