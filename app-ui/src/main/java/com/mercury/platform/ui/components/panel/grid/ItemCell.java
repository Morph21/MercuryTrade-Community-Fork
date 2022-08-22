package com.mercury.platform.ui.components.panel.grid;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemCell {
    private int x;
    private int y;
    private CellPanel cellPanel;
}
