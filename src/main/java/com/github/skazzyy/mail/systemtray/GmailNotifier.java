package com.github.skazzyy.mail.systemtray;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.net.URL;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

import com.github.skazzyy.gmail.GetGmailCount;

public class GmailNotifier {

    protected TrayIcon getTrayIcon() {
        SystemTray systemTray = SystemTray.getSystemTray();
        Dimension trayIconSize = systemTray.getTrayIconSize();
        String imagePath = "images/e-mail-icone-8968-16.png"; // relative to the classpath (hopefully)
        if (trayIconSize.getWidth() > 31) {
            imagePath = "images/e-mail-icone-8968-32.png";
        }
        URL imageURL = getClass().getClassLoader().getResource(imagePath);
        ImageIcon imageIcon = new ImageIcon(imageURL, "New message(s)");
        Image image = imageIcon.getImage();

        TrayIcon trayIcon = new TrayIcon(image);
        return trayIcon;
    }

    public static void main(String[] args) throws AWTException {
        Timer inboxPoller = new Timer("Gmail Inbox Poller", false);
        GetGmailCount gmailApi = new GetGmailCount();

        // Get an image to use
        SystemTray systemTray = SystemTray.getSystemTray();
        GmailNotifier notifier = new GmailNotifier();
        TrayIcon trayIcon = notifier.getTrayIcon();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Timer fired!");
                try {
                    int unreadCount = gmailApi.getUnreadCount();
                    if (unreadCount > 0) {
                        if (!Arrays.asList(systemTray.getTrayIcons()).contains(trayIcon)) {
                            systemTray.add(trayIcon);
                        }
                    } else {
                        systemTray.remove(trayIcon);
                    }
                    System.out.println("Unread messages: " + unreadCount);
                } catch (Throwable t) {
                    t.printStackTrace();
                    System.exit(-1);
                }
            }
        };
        trayIcon.addActionListener(e -> {
            System.err.println("Implement a better action, right now it just refreshes!");
            task.run();
        });
        inboxPoller.scheduleAtFixedRate(task, 0, 1000 * 60);
    }
}
