package com.mercury.platform.shared.hotkey;

import com.mercury.platform.shared.HistoryManager;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.descriptor.NotificationSettingsDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;

public class ClipboardListener {
    private final static Logger logger = LogManager.getLogger(ClipboardListener.class);
    public static boolean enabled;
    private static ClipboardListener instance = null;
    private static String lastData;

    private ClipboardListener() {
        NotificationSettingsDescriptor config = Configuration.get().notificationConfiguration().get();
        enabled = config.isWhisperHelperEnable();
        new Timer(200, e -> {
            if (enabled) {
                try {
                    Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
                    DataFlavor df = DataFlavor.stringFlavor;
                    if (c.isDataFlavorAvailable(df)) {
                        String message = c.getData(df).toString();
                        if (!message.equals(lastData)) {
                            lastData = message;
                            if (message.toLowerCase().contains("@") &&
                                (message.toLowerCase().contains("hi, i would like") ||
                                 message.toLowerCase().contains("hi, i'd like") ||
                                 message.toLowerCase().contains("i'd like") ||
                                 (message.toLowerCase().contains("wtb") && message.toLowerCase().contains("(stash")))) {

                                if (!HistoryManager.INSTANCE.matchesRecent(message, 10)) {
                                    MercuryStoreCore.chatClipboardSubject.onNext(true);
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }
        }).start();
    }

    public static void createListener() {
        if (instance == null) {
            instance = new ClipboardListener();
        }
    }
}
