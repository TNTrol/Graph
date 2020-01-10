/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphEdition;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class OrGraphArray<T, E> extends GraphArray<T, E> {

    public OrGraphArray() {
    }

    public OrGraphArray(JPanel paintPanel, int width, int height) {
        setPanel(paintPanel, width, height, null, null);
    }

    public OrGraphArray(JPanel paintPanel, int width, int height, Callback value) {
        setPanel(paintPanel, width, height, value, null);
        this.callback = value;
    }

    public OrGraphArray(JPanel paintPanel, int width, int height, Callback value, Show show) {
        setPanel(paintPanel, width, height, value, show);
    }

    @Override
    public void setPanel(JPanel paintPanel, int width, int height, Callback value, Show show) {
        w = width;
        h = height;
        this.show = show;
        this.callback = value;
        paintPanel.setLayout(new BorderLayout());
        paintJP = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                addToPanel(g);
                paintGraph(g);
            }
        };
        SwingUtils.setFixedSize(paintPanel, width, height);
        paintJP.setBackground(Color.WHITE);
        paintPanel.add(paintJP);

    }

    private Point LineOfIndex(int x, int y) {
        for (int i = 0; i < Edge.length; i++) {
            for (int j = 0; j < Edge[0].length; j++) {
                if (Edge[i][j] != null && checkEdge(Note.get(i).getX(), Note.get(i).getY(), Note.get(j).getX(), Note.get(j).getY(), x, y)) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }

    @Override
    public void mouseReleased(int x, int y) {
        if (State.AddLine == state && indexStart >= 0) {
            indexEnd = PointOfIndex(x, y);
            if (indexEnd >= 0 && indexEnd != indexStart && Edge[indexStart][indexEnd] == null && Edge[indexEnd][indexStart] == null) {
                Edge[indexStart][indexEnd] = new Node();
                paintJP.repaint();
                if (callback != null) {
                    E temp = (E) callback.addValueToEdge(indexStart, indexEnd);
                    Edge[indexStart][indexEnd] = temp == null ? null : new Node(temp);
                }
            }
            paintJP.repaint();
            indexEnd = -1;
        }
        if (callback != null) {
            if (State.ValueEdge == state) {
                Point p = LineOfIndex(x, y);
                if (p != null) {
                    Edge[p.ind1][p.ind2].value = (E) callback.addValueToEdge(p.ind1, p.ind2);
                    paintJP.repaint();
                }
            }
            if (State.ValueTop == state && indexStart >= 0) {
                Note.get(indexStart).setValue((T) callback.addValueToTop(indexStart));
                paintJP.repaint();
            }
        }
        if (show != null) {
            if (indexStart >= 0 && State.RemoveTop != state) {
                show.showValue(indexStart, -1);
            } else if (indexStart < 0 && State.RemoveLine != state) {
                Point p = LineOfIndex(x, y);
                if (p != null) {
                    show.showValue(p.ind1, p.ind2);
                }
            }
        }
        indexStart = -1;
    }

    private void paintGraph(Graphics g) {
        g.setColor(Color.BLACK);
        for (int i = 0; i < Note.size(); i++) {
            for (int j = 0; j < Note.size(); j++) {
                if (Edge[i][j] != null) {
                    drawEdge(Note.get(i).getX(), Note.get(i).getY(), Note.get(j).getX(), Note.get(j).getY(), i, j, g);
                }
            }
        }
        for (int i = 0; i < Note.size(); i++) {
            drawTop(Note.get(i).getX(), Note.get(i).getY(), i, g);
        }
        if (State.AddLine == state && indexStart >= 0 && indexEnd < 0) {
            drawEdge(Note.get(indexStart).getX(), Note.get(indexStart).getY(),
                    currentX, currentY, indexStart, -1, g);
        }
    }

    @Override
    public void drawEdge(int x1, int y1, int x2, int y2, int start, int end, Graphics g) {
        g.drawLine(x1, y1, x2, y2);
        double angle = Math.atan2(x2 - x1, y2 - y1);
        g.drawLine(x2, y2, Math.toIntExact(Math.round(x2 - 10 * Math.sin(angle - 45))), Math.toIntExact(Math.round(y2 - 10 * Math.cos(angle - 45))));
        g.drawLine(x2, y2, Math.toIntExact(Math.round(x2 - 10 * Math.sin(angle + 45))), Math.toIntExact(Math.round(y2 - 10 * Math.cos(angle + 45))));
    }

    @Override
    public boolean[][] getMatrixBool() {
        boolean[][] arr = new boolean[Note.size()][Note.size()];
        for (int i = 0; i < Note.size(); i++) {
            for (int j = 0; j < Note.size(); j++) {
                arr[i][j] = Edge[i][j] != null;
            }
        }
        return arr;
    }

}
