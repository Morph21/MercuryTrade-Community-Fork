package com.mercury.platform.ui.components.panel.grid;

import com.mercury.platform.ui.misc.AppThemeColor;

import java.awt.*;

import static java.lang.Math.round;

public class CellPanel extends Component {
    private boolean isShow = false;
    private boolean isQuad = false;
    private int x = 0;
    private int y = 0;

    private final Stroke stroke = new BasicStroke(2);

    public void hideCell() {
        isShow = false;
    }

    public void showCell(int x, int y) {
        this.x = x - 1;
        this.y = y - 1;
        isShow = true;
        isQuad = x > 12 || y > 12;
    }

    public void setQuad(boolean isQuad) {
        this.isQuad = isQuad;
    }

    @Override
    public void paint(Graphics g) {
        if (isShow) {
            Graphics2D g2 = (Graphics2D) g;
            final int size = isQuad ? 24 : 12;

            final float cellWidth = ((float) getWidth()) / size;
            final float cellHeight = ((float) getHeight()) / size;

            final int x = round(cellWidth * this.x);
            final int y = round(cellHeight * this.y);

            final int width = round(cellWidth * (this.x + 1)) - x;
            final int height = round(cellHeight * (this.y + 1)) - y;

            g2.setColor(AppThemeColor.TEXT_DEFAULT);
            g2.setStroke(stroke);
            g2.drawRect(x, y, width, height);
        }
    }
}
