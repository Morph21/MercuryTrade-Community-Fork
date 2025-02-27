package com.mercury.platform.shared.hotkey;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HotKeysInterceptor {
    public HotKeysInterceptor() {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.FINE);

        logger.setUseParentHandlers(false);
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }

        GlobalScreen.addNativeKeyListener(new MercuryNativeKeyListener());
        GlobalScreen.addNativeMouseListener(new MercuryNativeMouseListener());
    }
}
