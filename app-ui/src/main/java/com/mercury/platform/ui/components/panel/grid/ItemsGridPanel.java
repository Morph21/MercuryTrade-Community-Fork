package com.mercury.platform.ui.components.panel.grid;

import com.mercury.platform.shared.config.descriptor.StashTabDescriptor;
import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.misc.ViewInit;
import com.mercury.platform.ui.frame.movable.ItemsGridFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import lombok.NonNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;


public class ItemsGridPanel extends JPanel implements ViewInit {
    private ComponentsFactory componentsFactory;
    private Map<String, ItemInfoPanel> tabButtons;
    private StashTabsContainer stashTabsContainer;
    private JPanel navBar;

    private CellPanel cellPanel;

    public ItemsGridPanel() {
        super(new BorderLayout());
        componentsFactory = new ComponentsFactory();
        tabButtons = new HashMap<>();
        stashTabsContainer = new StashTabsContainer();
        onViewInit();
    }

    public ItemsGridPanel(@NonNull ComponentsFactory factory) {
        super(new BorderLayout());
        this.componentsFactory = factory;
        tabButtons = new HashMap<>();
        stashTabsContainer = new StashTabsContainer();
        onViewInit();
    }

    @Override
    public void onViewInit() {
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.setBorder(null);

        JPanel rightPanel = componentsFactory.getTransparentPanel(new BorderLayout());
        rightPanel.setBackground(AppThemeColor.TRANSPARENT);
        rightPanel.setPreferredSize(new Dimension(18, 668));
        JPanel downPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        downPanel.setBorder(BorderFactory.createEmptyBorder(-10, 0, 0, 0));
        downPanel.setBackground(AppThemeColor.TRANSPARENT);
        downPanel.setPreferredSize(new Dimension(661, 16));
        cellPanel = new CellPanel();

        this.add(getHeaderPanel(), BorderLayout.PAGE_START);
        this.add(cellPanel, BorderLayout.CENTER);
        this.add(rightPanel, BorderLayout.LINE_END);
        this.add(downPanel, BorderLayout.PAGE_END);
        this.setPreferredSize(this.getMaximumSize());
    }

    public void add(@NonNull ItemTradeNotificationDescriptor message, ItemInfoPanelController controller) {
        String nickname = message.getWhisperNickname();
        if (!tabButtons.containsKey(nickname + message.getTabName())) {
            int x = message.getLeft();
            int y = message.getTop();
            StashTabDescriptor stashTabDescriptor;
            if (stashTabsContainer.containsTab(message.getTabName())) {
                stashTabDescriptor = stashTabsContainer.getStashTab(message.getTabName());
            } else {
                stashTabDescriptor = new StashTabDescriptor(message.getTabName(), false, true);
            }
            ItemInfoPanel cellHeader = createGridItem(message, new ItemCell(x, y, cellPanel), stashTabDescriptor);
            if (controller != null) {
                cellHeader.setController(controller);
            }
            navBar.add(cellHeader);
            tabButtons.put(nickname + message.getTabName(), cellHeader);
            MercuryStoreUI.repaintSubject.onNext(ItemsGridFrame.class);
        }
    }

    public void remove(@NonNull ItemTradeNotificationDescriptor message) {
        closeGridItem(message);
    }

    public void changeTabType(@NonNull ItemInfoPanel itemInfoPanel) {
        cellPanel.setQuad(itemInfoPanel.getStashTabDescriptor().isQuad());
        MercuryStoreUI.repaintSubject.onNext(ItemsGridFrame.class);
    }

    public int getActiveTabsCount() {
        return navBar.getComponentCount();
    }

    private JPanel getHeaderPanel() {
        JPanel root = componentsFactory.getTransparentPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        navBar = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        navBar.setBorder(BorderFactory.createEmptyBorder(20, 0, 26, 0));
        root.add(navBar);
        return root;
    }

    private ItemInfoPanel createGridItem(@NonNull ItemTradeNotificationDescriptor message, @NonNull ItemCell cell, @NonNull StashTabDescriptor stashTabDescriptor) {
        ItemInfoPanel itemInfoPanel = new ItemInfoPanel(message, cell, stashTabDescriptor, componentsFactory);
        itemInfoPanel.setAlignmentY(SwingConstants.CENTER);
        itemInfoPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ItemsGridPanel.this.cellPanel.setQuad(stashTabDescriptor.isQuad());
                MercuryStoreUI.repaintSubject.onNext(ItemsGridFrame.class);
            }
        });
        return itemInfoPanel;
    }

    private void closeGridItem(@NonNull ItemTradeNotificationDescriptor message) {
        String nickname = message.getWhisperNickname();
        ItemInfoPanel itemInfoPanel = tabButtons.get(nickname + message.getTabName());
        if (itemInfoPanel != null) {
            if (itemInfoPanel.getStashTabDescriptor().isUndefined()) {
                itemInfoPanel.getStashTabDescriptor().setUndefined(false);
                stashTabsContainer.addTab(itemInfoPanel.getStashTabDescriptor());
                stashTabsContainer.save();
            }

            navBar.remove(itemInfoPanel);
            cellPanel.hideCell();
            repaint();
            tabButtons.remove(nickname + message.getTabName());
            MercuryStoreUI.repaintSubject.onNext(ItemsGridFrame.class);
        }
    }
}
