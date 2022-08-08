package com.mercury.platform.ui.components.panel.taskbar;

import com.mercury.platform.core.ProdStarter;
import com.mercury.platform.shared.FrameVisibleState;
import com.mercury.platform.shared.IconConst;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.TaskBarDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.misc.ViewInit;
import com.mercury.platform.ui.frame.movable.TaskBarFrame;
import com.mercury.platform.ui.manager.FramesManager;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;
import lombok.NonNull;

import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TaskBarPanel extends JPanel implements ViewInit {
    private ComponentsFactory componentsFactory;
    private TaskBarController controller;
    private PlainConfigurationService<TaskBarDescriptor> taskBarService;
    private JButton toHideout;
    private JButton showHelpIG;
    private MouseListener taskBarFrameMouseListener;

    public TaskBarPanel(@NonNull TaskBarController controller, @NonNull ComponentsFactory factory, MouseListener taskBarFrameMouseListener) {
        this.controller = controller;
        this.componentsFactory = factory;
        this.taskBarFrameMouseListener = taskBarFrameMouseListener;
        this.onViewInit();

        MercuryStoreCore.hotKeySubject.subscribe(hotkeyDescriptor -> {
            SwingUtilities.invokeLater(() -> {
                if (ProdStarter.APP_STATUS.equals(FrameVisibleState.SHOW)) {
                    if (this.taskBarService.get().getHideoutHotkey().equals(hotkeyDescriptor)) {
                        this.toHideout.doClick();
                    } else if (this.taskBarService.get().getHelpIGHotkey().equals(hotkeyDescriptor)) {
                        this.showHelpIG.doClick();
                    }
                }
            });
        });
    }

    @Override
    public void onViewInit() {
        this.taskBarService = Configuration.get().taskBarConfiguration();

        this.setBackground(AppThemeColor.FRAME);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        JButton visibleMode = componentsFactory.getIconButton(
                IconConst.VISIBLE_ALWAYS_MODE,
                24,
                AppThemeColor.FRAME,
                TooltipConstants.VISIBLE_MODE);
        componentsFactory.setUpToggleCallbacks(visibleMode,
                () -> {
                    visibleMode.setIcon(componentsFactory.getIcon(IconConst.VISIBLE_DND_MODE, 24));
                    controller.enableDND();
                },
                () -> {
                    visibleMode.setIcon(componentsFactory.getIcon(IconConst.VISIBLE_ALWAYS_MODE, 24));
                    controller.disableDND();
                },
                true
        );
        visibleMode.addMouseListener(taskBarFrameMouseListener);

        JButton pushbulletNotification = componentsFactory.getIconButton(
                taskBarService.get().isPushbulletOn() ? IconConst.PUSHBULLET_NOTIFICATION : IconConst.PUSHBULLET_NOTIFICATION_OFF,
                24,
                AppThemeColor.FRAME,
                TooltipConstants.PUSHBULLET_NOTIFICATION_ACTIVE);
        componentsFactory.setUpToggleCallbacks(pushbulletNotification,
                                               () -> {
                                                   getPushbullet(taskBarService.get().isPushbulletOn(), pushbulletNotification);
                                               },
                                               () -> {
                                                   getPushbullet(taskBarService.get().isPushbulletOn(), pushbulletNotification);
                                               },
                                               true
                                              );
        pushbulletNotification.addMouseListener(taskBarFrameMouseListener);

        JButton itemGrid = componentsFactory.getIconButton(
                IconConst.ITEM_GRID_ENABLE,
                24,
                AppThemeColor.FRAME,
                TooltipConstants.ITEM_GRID);
        itemGrid.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    controller.showITH();
                }
            }
        });
        itemGrid.addMouseListener(taskBarFrameMouseListener);

        this.toHideout = componentsFactory.getIconButton(
                IconConst.HIDEOUT,
                24,
                AppThemeColor.FRAME,
                TooltipConstants.HIDEOUT);
        this.toHideout.addActionListener(action -> {
            this.controller.performHideout();
        });
        this.toHideout.addMouseListener(taskBarFrameMouseListener);

        this.showHelpIG = componentsFactory.getIconButton(
                IconConst.HELP_IG,
                24,
                AppThemeColor.FRAME,
                TooltipConstants.HELPIG);
        this.showHelpIG.addActionListener(action -> {
            this.controller.showHelpIG();
        });
        this.showHelpIG.addMouseListener(taskBarFrameMouseListener);

        JButton adr = componentsFactory.getIconButton(
                IconConst.OVERSEER,
                24,
                AppThemeColor.FRAME,
                TooltipConstants.ADR_SETTINGS);
        adr.addActionListener(action -> {
            FramesManager.INSTANCE.performAdr();
            TaskBarFrame windowAncestor = (TaskBarFrame) SwingUtilities.getWindowAncestor(TaskBarPanel.this);
            windowAncestor.setSize(new Dimension(windowAncestor.getMIN_WIDTH(), windowAncestor.getHeight()));
            windowAncestor.pack();
        });
        adr.addMouseListener(taskBarFrameMouseListener);

        JButton chatFilter = componentsFactory.getIconButton(
                IconConst.CHAT_FILTER,
                24,
                AppThemeColor.FRAME,
                TooltipConstants.CHAT_FILTER);
        chatFilter.addActionListener(action -> {
            this.controller.showChatFiler();
        });
        chatFilter.addMouseListener(taskBarFrameMouseListener);

        JButton historyButton = componentsFactory.getIconButton(
                IconConst.HISTORY,
                24,
                AppThemeColor.FRAME,
                TooltipConstants.HISTORY);
        historyButton.addActionListener(action -> {
            this.controller.showHistory();
        });
        historyButton.addMouseListener(taskBarFrameMouseListener);

        JButton pinButton = componentsFactory.getIconButton(
                IconConst.DRAG_AND_DROP,
                24,
                AppThemeColor.FRAME,
                TooltipConstants.SETUP_FRAMES_LOCATION);
        pinButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    controller.openPINSettings();
                }
            }
        });
        pinButton.addMouseListener(taskBarFrameMouseListener);

        JButton scaleButton = componentsFactory.getIconButton(
                IconConst.SCALE_SETTINGS,
                24,
                AppThemeColor.FRAME,
                TooltipConstants.SCALE_SETTINGS);
        scaleButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    controller.openScaleSettings();
                }
            }
        });
        scaleButton.addMouseListener(taskBarFrameMouseListener);

        JButton settingsButton = componentsFactory.getIconButton(
                IconConst.SETTINGS,
                26,
                AppThemeColor.FRAME,
                TooltipConstants.SETTINGS);
        settingsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    controller.showSettings();
                    TaskBarFrame windowAncestor = (TaskBarFrame) SwingUtilities.getWindowAncestor(TaskBarPanel.this);
                    windowAncestor.setSize(new Dimension(windowAncestor.getMIN_WIDTH(), windowAncestor.getHeight()));
                    windowAncestor.pack();
                }
            }
        });
        settingsButton.addMouseListener(taskBarFrameMouseListener);

        JButton exitButton = componentsFactory.getIconButton(
                IconConst.EXIT,
                24,
                AppThemeColor.FRAME,
                TooltipConstants.EXIT);
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    controller.exit();
                }
            }
        });
        exitButton.addMouseListener(taskBarFrameMouseListener);

        this.add(this.toHideout);
        this.add(Box.createRigidArea(new Dimension(3, 4)));
        this.add(adr);
        this.add(Box.createRigidArea(new Dimension(3, 4)));
        this.add(chatFilter);
        this.add(Box.createRigidArea(new Dimension(3, 4)));
        this.add(visibleMode);
        this.add(Box.createRigidArea(new Dimension(2, 4)));
        this.add(pushbulletNotification);
        this.add(Box.createRigidArea(new Dimension(2, 4)));
        this.add(this.showHelpIG);
        this.add(Box.createRigidArea(new Dimension(2, 4)));
        this.add(historyButton);
        this.add(Box.createRigidArea(new Dimension(3, 4)));
        this.add(itemGrid);
        this.add(Box.createRigidArea(new Dimension(3, 4)));
        this.add(pinButton);
        this.add(Box.createRigidArea(new Dimension(3, 4)));
        this.add(scaleButton);
        this.add(Box.createRigidArea(new Dimension(3, 4)));
        this.add(settingsButton);
        this.add(Box.createRigidArea(new Dimension(3, 4)));
        this.add(exitButton);
        this.add(Box.createRigidArea(new Dimension(3, 4)));
    }

    private void getPushbullet(boolean pushbulletEnabled, JButton pushbulletNotification) {
        if (!pushbulletEnabled) {
            pushbulletNotification.setIcon(componentsFactory.getIcon(IconConst.PUSHBULLET_NOTIFICATION, 24));
            controller.enablePushbullet();
        } else {
            pushbulletNotification.setIcon(componentsFactory.getIcon(IconConst.PUSHBULLET_NOTIFICATION_OFF, 24));
            controller.disablePushbullet();
        }
    }

    public int getWidthOf(int elementCount) {
        int size = this.getPreferredSize().width / (this.getComponentCount() / 2);
        return size * elementCount + 3;
    }
}
