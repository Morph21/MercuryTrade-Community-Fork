package com.mercury.platform.ui.components.panel.grid;

import com.mercury.platform.ui.misc.AppThemeColor;

import java.awt.*;

import static java.lang.Math.round;

public class GridPanel extends Component {
   final int rows;
   final int cols;

   public GridPanel(int rows, int cols) {
       this.rows = rows;
       this.cols = cols;
   }

    @Override
    public void paint(Graphics g) {
        final int width = getWidth();
        final int height = getHeight();

        final float cell_width = ((float) width) / cols;
        final float cell_height = ((float) height) / rows;

        g.setColor(AppThemeColor.SCROLL_BAR);

        for (int i = 0; i < rows; i++) {
            final int horizontal_pos = round(cell_height * i);
            g.drawLine(0, horizontal_pos, width - 1, horizontal_pos);
        }

        for (int i = 0; i < cols; i++) {
            final int vertical_pos = round(cell_width * i);
            g.drawLine(vertical_pos, 0, vertical_pos, height - 1);
        }
    }
}
