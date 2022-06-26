package com.mercury.platform.core;

import com.mercury.platform.shared.AsSubscriber;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.descriptor.TaskBarDescriptor;
import com.mercury.platform.shared.entity.message.MercuryError;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.DesktopWindow;
import com.sun.jna.platform.WindowUtils;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.ptr.IntByReference;
import org.apache.commons.lang3.SystemUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;


public class ChatHelper implements AsSubscriber {
    private Robot robot;
    private static boolean clipboardMessageOn = true;

    public ChatHelper() {
        subscribe();
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void executeClipboardMessage() {
        if (clipboardMessageOn && isGameOpen()) {
            this.gameToFront();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MercuryStoreCore.blockHotkeySubject.onNext(true);
            robot.keyRelease(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_A);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_A);

            robot.keyPress(KeyEvent.VK_BACK_SPACE);
            robot.keyRelease(KeyEvent.VK_BACK_SPACE);

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            MercuryStoreCore.blockHotkeySubject.onNext(false);
        }
    }

    private void executeMessage(String message) {
        clipboardMessageOn = false;
        this.gameToFront();
        StringSelection selection = new StringSelection(message);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);
        MercuryStoreCore.blockHotkeySubject.onNext(true);
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_A);

        robot.keyPress(KeyEvent.VK_BACK_SPACE);
        robot.keyRelease(KeyEvent.VK_BACK_SPACE);

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        MercuryStoreCore.blockHotkeySubject.onNext(false);
        clipboardMessageOn = true;
    }

    private void executeTradeMessage() {
        clipboardMessageOn = false;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();
        try {
            String result = (String) clipboard.getData(DataFlavor.stringFlavor);
            if (result != null && (result.contains("listed for") || result.contains("for my"))) {
                this.gameToFront();
                MercuryStoreCore.blockHotkeySubject.onNext(true);
                robot.keyRelease(KeyEvent.VK_ALT);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);

                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_A);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                robot.keyRelease(KeyEvent.VK_A);

                robot.keyPress(KeyEvent.VK_BACK_SPACE);
                robot.keyRelease(KeyEvent.VK_BACK_SPACE);

                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);

                Timer timer = new Timer(300, action -> {
                    StringSelection selection = new StringSelection("");
                    clipboard.setContents(selection, null);
                });
                timer.setRepeats(false);
                timer.start();

                MercuryStoreCore.blockHotkeySubject.onNext(false);
            }
        } catch (UnsupportedFlavorException | IOException e) {
            MercuryStoreCore.errorHandlerSubject.onNext(new MercuryError(e));
        }
        clipboardMessageOn = true;
    }

    private void openChat(String whisper) {
        clipboardMessageOn = false;
        this.gameToFront();
        StringSelection selection = new StringSelection("@" + whisper);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        MercuryStoreCore.blockHotkeySubject.onNext(true);
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_A);

        robot.keyPress(KeyEvent.VK_BACK_SPACE);
        robot.keyRelease(KeyEvent.VK_BACK_SPACE);

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_SPACE);
        robot.keyRelease(KeyEvent.VK_SPACE);
        MercuryStoreCore.blockHotkeySubject.onNext(false);
        clipboardMessageOn = true;
    }

    private void findInStashTab(String toBeFound) {
        clipboardMessageOn = false;
        this.gameToFront();
        StringSelection selection = new StringSelection(toBeFound);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);
        MercuryStoreCore.blockHotkeySubject.onNext(true);

        robot.keyRelease(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_F);

        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_F);


        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);

        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_V);

        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);

        MercuryStoreCore.blockHotkeySubject.onNext(false);
        clipboardMessageOn = true;
    }


    final WinDef.HWND HWND_TOPMOST = new WinDef.HWND(new Pointer(-1));
    final int SWP_NOSIZE = 0x0001;
    final int SWP_NOMOVE = 0x0002;
    final int SWP_SHOWWINDOW = 0x0040;

    static public ArrayList<DesktopWindow> getPoeWindows() {
        ArrayList<DesktopWindow> windows = new ArrayList<DesktopWindow>();
        WindowUtils.getAllWindows(false).forEach(window -> {
            char[] className = new char[512];
            User32.INSTANCE.GetClassName(window.getHWND(), className, 512);

            if (Native.toString(className).equals("POEWindowClass")) {
                windows.add(window);
            }
        });

        return windows;
    }

    static public int getWindowPid(DesktopWindow window) {
        IntByReference winpid = new IntByReference();
        User32.INSTANCE.GetWindowThreadProcessId(window.getHWND(), winpid);

        return winpid.getValue();
    }

    static public ArrayList<Integer> getPoeWindowPids() {
        ArrayList<DesktopWindow> windows = getPoeWindows();
        ArrayList<Integer> pids = new ArrayList<Integer>();

        windows.forEach(window -> { pids.add(getWindowPid(window)); });
        return pids;
    }

    private void gameToFront() {
        int pid = Configuration.get().applicationConfiguration().get().getGamePid();

        if (SystemUtils.IS_OS_WINDOWS) {
            getPoeWindows().forEach(window -> {
                int winpid = getWindowPid(window);

                boolean pidMatch = (pid == 0) || (pid == winpid);

                if (pidMatch) {
                    User32.INSTANCE.ShowWindow(window.getHWND(), 5);

                    boolean isAtFront = User32.INSTANCE.SetForegroundWindow(window.getHWND());
                    int counter = 0;
                    while (!isAtFront && counter < 10) {
                        isAtFront = User32.INSTANCE.SetForegroundWindow(window.getHWND());
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        counter++;
                    }

                    User32.INSTANCE.SetFocus(window.getHWND());
                }
            });

//            User32.INSTANCE.EnumWindows((hWnd, arg1) -> {
//                char[] className = new char[512];
//                User32.INSTANCE.GetClassName(hWnd, className, 512);
//                String wText = Native.toString(className);
//
//
//                if (wText.isEmpty()) {
//                    System.out.println("wText is empty");
//                    return true;
//                }
//                if (wText.equals("POEWindowClass")) {
//                    System.out.println("wText equals poe class");
//                    User32.INSTANCE.SetForegroundWindow(hWnd);
//                    User32.INSTANCE.SetFocus(hWnd);
//                    return false;
//                }
//                System.out.println("after if statement");
//                return true;
//            }, null);
        }

    }

    private boolean isGameOpen() {
        if (SystemUtils.IS_OS_WINDOWS) {
            WinDef.HWND poeWindowClass = WindowUtils.getAllWindows(false).stream().filter(window -> {
                char[] className = new char[512];
                User32.INSTANCE.GetClassName(window.getHWND(), className, 512);
                return Native.toString(className).equals("POEWindowClass");
            }).map(DesktopWindow::getHWND).findFirst().orElse(null);

            return poeWindowClass != null;
        } else {
            return true;
        }
    }

    @Override
    public void subscribe() {
        MercuryStoreCore.chatCommandSubject.subscribe(this::executeMessage);
        MercuryStoreCore.chatClipboardSubject.subscribe(state -> this.executeClipboardMessage());
        MercuryStoreCore.openChatSubject.subscribe(this::openChat);
        MercuryStoreCore.findInStashTab.subscribe(this::findInStashTab);
        MercuryStoreCore.tradeWhisperSubject.subscribe(state -> this.executeTradeMessage());
        MercuryStoreCore.dndSubject.subscribe(state -> {
            TaskBarDescriptor config = Configuration.get().taskBarConfiguration().get();
            if (config.isInGameDnd()) {
                if (state) {
                    executeMessage("/dnd " + config.getDndResponseText());
                } else {
                    executeMessage("/dnd");
                }
            }
        });
        MercuryStoreCore.pushbulletSubject.subscribe(state -> {
            TaskBarDescriptor config = Configuration.get().taskBarConfiguration().get();
            config.setPushbulletOn(state);
        });
    }
}
