package br.guiroshix.mmxf;

import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;

/**
 * Minimal Java ME MIDlet for MMXF.
 *
 * Targets CLDC 1.1 / MIDP 2.0.
 */
public final class Main extends MIDlet {

    private GameCanvas game;

    public void startApp() {
        if (game == null) {
            game = new GameCanvas();
        }

        Display.getDisplay(this).setCurrent(game);
        game.start();
    }

    public void pauseApp() {
        if (game != null) {
            game.stop();
        }
    }

    public void destroyApp(boolean unconditional) {
        if (game != null) {
            game.stop();
        }
    }

    private static final class GameCanvas extends Canvas implements Runnable {

        private volatile boolean running;
        private Thread thread;

        // FPS
        private int fps;
        private int frames;
        private long fpsTime;

        // Memory
        private long memoryUsed;
        private long memoryFree;
        private long memoryTotal;

        GameCanvas() {
            setFullScreenMode(true);
        }

        void start() {
            if (running) {
                return;
            }

            running = true;

            fpsTime = System.currentTimeMillis();

            thread = new Thread(this);
            thread.start();
        }

        void stop() {
            running = false;
            thread = null;
        }

        protected void paint(Graphics g) {
            int width = getWidth();
            int height = getHeight();

            // Background
            g.setColor(0x000000);
            g.fillRect(0, 0, width, height);

            // Debug text
            g.setColor(0xFFFFFF);

            int y = 2;

            g.drawString(
                "MMXF DEBUG",
                2,
                y,
                Graphics.TOP | Graphics.LEFT
            );

            y += 18;

            g.drawString(
                "FPS: " + fps,
                2,
                y,
                Graphics.TOP | Graphics.LEFT
            );

            y += 18;

            g.drawString(
                "Mem Used: " + formatMemory(memoryUsed),
                2,
                y,
                Graphics.TOP | Graphics.LEFT
            );

            y += 18;

            g.drawString(
                "Mem Free: " + formatMemory(memoryFree),
                2,
                y,
                Graphics.TOP | Graphics.LEFT
            );

            y += 18;

            g.drawString(
                "Mem Total: " + formatMemory(memoryTotal),
                2,
                y,
                Graphics.TOP | Graphics.LEFT
            );
        }

        public void run() {
            final long frameTime = 16L;

            while (running) {
                long start = System.currentTimeMillis();

                update();

                repaint();
                serviceRepaints();

                long elapsed = System.currentTimeMillis() - start;
                long sleep = frameTime - elapsed;

                if (sleep > 0) {
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        // Loop checks running on next iteration.
                    }
                }
            }
        }

        private void update() {
            frames++;

            long now = System.currentTimeMillis();

            // FPS
            if (now - fpsTime >= 1000L) {
                fps = frames;
                frames = 0;
                fpsTime = now;
            }

            // Memory
            Runtime runtime = Runtime.getRuntime();

            memoryTotal = runtime.totalMemory();
            memoryFree = runtime.freeMemory();
            memoryUsed = memoryTotal - memoryFree;
        }

        /**
         * Converts bytes to a more readable unit.
         *
         * Examples:
         * 512     -> 512 B
         * 2048    -> 2 KB
         * 1048576 -> 1 MB
         */
        private String formatMemory(long bytes) {
            if (bytes >= 1024L * 1024L) {
                return (bytes / (1024L * 1024L)) + " MB";
            }

            if (bytes >= 1024L) {
                return (bytes / 1024L) + " KB";
            }

            return bytes + " B";
        }
    }
}