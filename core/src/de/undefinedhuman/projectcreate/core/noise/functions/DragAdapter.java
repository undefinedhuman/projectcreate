package de.undefinedhuman.projectcreate.core.noise.functions;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.engine.utils.math.Vector2i;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DragSource;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Objects;

public class DragAdapter extends MouseAdapter {

    private final Rectangle TEMP_RECTANGLE_1 = new Rectangle();
    private final Rectangle TEMP_RECTANGLE_2 = new Rectangle();
    private Rectangle prevRect;
    private JWindow window = null;
    private Component componentThatIsDragged;
    private int index = -1;
    private Component gap;
    private Vector2 startMousePosition;
    private Vector2i dragOffset = new Vector2i();
    private final int gestureMotionThreshold = DragSource.getDragThreshold();

    @Override
    public void mousePressed(MouseEvent event) {
        JComponent componentClicked = (JComponent) event.getComponent();
        startMousePosition = componentClicked.getComponentCount() <= 1 ? null : new Vector2(event.getX(), event.getY());
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        JComponent parent = (JComponent) event.getComponent();

        if (componentThatIsDragged == null && startMousePosition != null && startMousePosition.dst(event.getX(), event.getY()) > gestureMotionThreshold) {
            startDragging(parent, event.getPoint());
            return;
        }
        if (window == null || componentThatIsDragged == null)
            return;

        updateWindowLocation(event.getPoint(), parent);
        if (prevRect != null && prevRect.contains(event.getPoint()))
            return;

        for (int i = 0; i < parent.getComponentCount(); i++) {
            Component c = parent.getComponent(i);
            if (Objects.equals(c, gap) && c.getBounds().contains(event.getPoint()))
                return;
            if(swapTargetIndex(parent, gap, gap, c, event.getPoint(), i))
                return;
        }
        parent.remove(gap);
        parent.revalidate();
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        startMousePosition = null;

        if (window == null || componentThatIsDragged == null)
            return;

        JComponent parent = (JComponent) event.getComponent();

        Component tempComponent = componentThatIsDragged;
        componentThatIsDragged = null;
        prevRect = null;
        window.dispose();
        window = null;

        for (int i = 0; i < parent.getComponentCount(); i++) {
            Component c = parent.getComponent(i);
            if (Objects.equals(c, gap)) {
                swapComponentLocation(parent, gap, tempComponent, i);
                return;
            }
            if(swapTargetIndex(parent, gap, tempComponent, c, event.getPoint(), i))
                return;
        }
        swapComponentLocation(parent, gap, tempComponent, parent.getParent().getBounds().contains(event.getPoint()) ? parent.getComponentCount() : index);
    }

    private boolean swapTargetIndex(JComponent parent, Component remove, Component add, Component component, Point point, int index) {
        int tgt = getTargetIndex(component.getBounds(), point, index);
        if(tgt < 0)
            return false;
        swapComponentLocation(parent, remove, add, tgt);
        return true;
    }

    private void startDragging(JComponent parent, Point mousePosition) {
        Component clickedComponent = parent.getComponentAt(mousePosition);
        index = parent.getComponentZOrder(clickedComponent);
        if (Objects.equals(clickedComponent, parent) || index < 0)
            return;

        componentThatIsDragged = clickedComponent;
        dragOffset.set(mousePosition.x, mousePosition.y).sub(componentThatIsDragged.getX(), componentThatIsDragged.getY());
        dragOffset = new Vector2i(mousePosition.x, mousePosition.y).sub(componentThatIsDragged.getX(), componentThatIsDragged.getY());

        gap = Box.createRigidArea(componentThatIsDragged.getSize());
        swapComponentLocation(parent, clickedComponent, gap, index);

        componentThatIsDragged.setLocation(0, 0);
        window = new JWindow();
        window.setBackground(new Color(0, true));
        window.add(componentThatIsDragged);
        window.setSize(componentThatIsDragged.getSize());

        updateWindowLocation(mousePosition, parent);
        window.setVisible(true);
    }

    private void updateWindowLocation(Point pt, JComponent parent) {
        Point p = new Point(pt.x - dragOffset.x, pt.y - dragOffset.y);
        SwingUtilities.convertPointToScreen(p, parent);
        window.setLocation(p);
    }

    private int getTargetIndex(Rectangle r, Point pt, int i) {
        int ht2 = (int) (0.5f + r.height * 0.5f);
        TEMP_RECTANGLE_1.setBounds(r.x, r.y, r.width, ht2);
        TEMP_RECTANGLE_2.setBounds(r.x, r.y + ht2, r.width, ht2);
        if (TEMP_RECTANGLE_1.contains(pt)) {
            prevRect = TEMP_RECTANGLE_1;
            return i - 1 > 0 ? i : 0;
        } else if (TEMP_RECTANGLE_2.contains(pt)) {
            prevRect = TEMP_RECTANGLE_2;
            return i;
        }
        return -1;
    }

    private void swapComponentLocation(Container parent, Component remove, Component add, int index) {
        parent.remove(remove);
        parent.add(add, index);
        parent.revalidate();
        parent.repaint();
    }

}

class Test {

    public JComponent makeUI() {
        Box box = Box.createVerticalBox();
        DragAdapter dh = new DragAdapter();
        box.addMouseListener(dh);
        box.addMouseMotionListener(dh);

        int idx = 0;
        for (JComponent c : Arrays.asList(
                new JLabel("<html>111<br>11<br>11"),
                new JButton("2"), new JCheckBox("3"), new JTextField(5))) {
            box.add(createToolbarButton(idx++, c));
        }
        JPanel p = new JPanel(new BorderLayout());
        p.add(box, BorderLayout.NORTH);
        return p;
    }

    private static JComponent createToolbarButton(int i, JComponent c) {
        JLabel l = new JLabel(String.format(" %04d ", i));
        l.setOpaque(true);
        l.setBackground(Color.RED);
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createLineBorder(Color.BLUE, 2)));
        p.add(l, BorderLayout.WEST);
        p.add(c);
        p.setOpaque(false);
        return p;
    }

    public static void main(String... args) {
        EventQueue.invokeLater(Test::createAndShowGUI);
    }

    public static void createAndShowGUI() {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.getContentPane().add(new Test().makeUI());
        f.setSize(320, 240);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

}
