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

        private int fps;
        private int frames;

        private long fpsTime;

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

            g.setColor(0x000000);
            g.fillRect(0, 0, width, height);

            g.setColor(0xFFFFFF);

            g.drawString(
                "MMXF",
                2,
                2,
                Graphics.TOP | Graphics.LEFT
            );

            g.drawString(
                "FPS: " + fps,
                2,
                20,
                Graphics.TOP | Graphics.LEFT
            );

            g.drawString(
                "Java ME / CLDC 1.1 / MIDP 2.0",
                2,
                38,
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
                        // The loop checks running on the next iteration.
                    }
                }
            }
        }

        private void update() {
            frames++;

            long now = System.currentTimeMillis();

            if (now - fpsTime >= 1000L) {
                fps = frames;
                frames = 0;
                fpsTime = now;
            }
        }
    }
}
