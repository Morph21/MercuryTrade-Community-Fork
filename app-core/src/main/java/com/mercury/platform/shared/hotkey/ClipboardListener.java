package com.mercury.platform.shared.hotkey;

import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.descriptor.NotificationSettingsDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorListener;

public class ClipboardListener {
    private final static Logger logger = LogManager.getLogger(ClipboardListener.class);
    public static boolean enabled;
    private static ClipboardListener instance = null;
    private static String lastData;
    private static Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    FlavorListener flavorListener;

    private boolean secondRun = false;

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
                            if (isBuyMessage(message)){
                                MercuryStoreCore.chatClipboardSubject.onNext(true);
                            }
                        }
                    }
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }
        }).start();

    }

    private boolean isBuyMessage(String message){
        if(!message.contains("@")) {
            return false;
        }

        String messageLowercase = message.toLowerCase();

        if(messageLowercase.contains("hi, i would like")
            || messageLowercase.contains("hi, i'd like")
            || messageLowercase.contains("i'd like")
            || (messageLowercase.contains("wtb") && messageLowercase.contains("(stash"))
            || messageLowercase.contains("구매하고 싶습니다") /* Korean: I would like to buy */
            || messageLowercase.contains("хочу купить у вас") /* Russian: I would like to buy */
            || messageLowercase.contains("eu gostaria de comprar") /* Brazilian Portuguese: I would like to buy */
            || messageLowercase.contains("เราต้องการชื้อ") /* Thai: I would like to buy */
            || (messageLowercase.contains("ich möchte") && messageLowercase.contains("kaufen")) /* German: I would like to buy */
            || messageLowercase.contains("je souhaiterais t'acheter") /* French: I would like to buy */
            || messageLowercase.contains("を購入したいです") /* Japanese: I would like to buy */) {
            return true;
        }

        return false;
    }

    public static void createListener() {
        if (instance == null) {
            instance = new ClipboardListener();
        }
    }

}
