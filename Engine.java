
//required import statements
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.xml.stream.events.Characters;

public class Engine extends JPanel {

	static class rect {
		public int screencoords[];
		public int texturecoords[];
		public BufferedImage texture;
		public double distance;

		public rect(int s[], int t[], BufferedImage c, double d) {
			screencoords = s;
			texturecoords = t;
			texture = c;
			distance = d;
		}
	}

	static class sprite {
		public int screencoords[];
		public int texturecoords[];
		public BufferedImage texture;
		public double distance;

		public sprite(int s[], int t[], BufferedImage c, double d) {
			screencoords = s;
			texturecoords = t;
			texture = c;
			distance = d;
		}

		public double getDistance() {
			return distance;
		}
	}

	private static final int map[][] = {
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
					1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 0, 0, 0, 0, 3, 0, 3, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2,
					0, 0, 0, 0, 3, 0, 3, 0, 3, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2,
					0, 0, 0, 0, 3, 0, 3, 0, 3, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2,
					0, 0, 0, 0, 3, 0, 3, 0, 3, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 2, 2, 0, 2, 2, 0, 0, 0, 0, 3, 0, 3, 0, 3, 0, 0, 0, 1, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 4, 0, 4, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 4, 0, 0, 0, 0, 5, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 4, 0, 4, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 4, 0, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 1, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0,
					1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 0, 0, 0, 0, 3, 0, 3, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2,
					0, 0, 0, 0, 3, 0, 3, 0, 3, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2,
					0, 0, 0, 0, 3, 0, 3, 0, 3, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2,
					0, 0, 0, 0, 3, 0, 3, 0, 3, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 2, 2, 0, 2, 2, 0, 0, 0, 0, 3, 0, 3, 0, 3, 0, 0, 0, 1, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 4, 0, 4, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 4, 0, 0, 0, 0, 5, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 4, 0, 4, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 4, 0, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
					1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
	};

	private static double posX = 3;
	private static double posY = 3;
	private static double dirX = -1;
	private static double dirY = 0;
	private static double planeX = 0;
	private static double planeY = 0.65;

	private static int reload = 5;

	private static final double moveSpeed = 0.06;
	private static final double rotSpeed = 0.03;

	private static final boolean keys[] = new boolean[6];

	private static final int transparencyLimit = 5;

	private static final rect rects[] = new rect[800 * transparencyLimit];

	private class Keyboard implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {

			switch (e.getKeyChar()) {
				case 'w' -> {
					keys[0] = true;
				}
				case 'a' -> {
					keys[1] = true;
				}
				case 's' -> {
					keys[2] = true;
				}
				case 'd' -> {
					keys[3] = true;
				}
			}

			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				keys[4] = true;
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				keys[5] = true;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {

			switch (e.getKeyChar()) {
				case 'w' -> {
					keys[0] = false;
				}
				case 'a' -> {
					keys[1] = false;
				}
				case 's' -> {
					keys[2] = false;
				}
				case 'd' -> {
					keys[3] = false;
				}
			}

			switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					keys[4] = false;
					break;
				case KeyEvent.VK_RIGHT:
					keys[5] = false;
					break;
				case KeyEvent.VK_SPACE:
					shoot();
					System.out.println(projectiles.size());
					break;
				default:
					break;
			}
		}

	}

	private final BufferedImage image;
	private final Graphics g;
	private final Timer timer;

	private static final ExecutorService executor = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	private static final Future<?> futures[] = new Future<?>[800 * transparencyLimit];

	private static final ArrayList<sprite> sprites = new ArrayList<>();
	private static final ArrayList<Character> characters = new ArrayList<>();

	private static ArrayList<Projectile> projectiles = new ArrayList<>();

	private static final int drawn[] = new int[800 * transparencyLimit];

	public Engine() throws IOException {

		image = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
		g = image.getGraphics();
		TextureLoader.init();
		timer = new Timer(10, new TimerListener());
		timer.start();
		addKeyListener(new Keyboard());
		setFocusable(true);

		characters.add(new Character(3, 3, TextureLoader.spriteTexture));
	}

	private class TimerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			g.setColor(Color.black);
			g.fillRect(0, 0, 800, 800);

			move();
			moveThings();

			drawFloor();
			renderWalls();
			renderSprites();
			renderProjectiles();
			Collections.sort(sprites, Comparator.comparingDouble(sprite::getDistance).reversed());
			int size = 800 * transparencyLimit;
			Arrays.setAll(drawn, i -> 0);

			sprite S = null;
			for (sprite s : sprites) {
				S = s;
				for (int j = 0; j < size; j++) {
					rect r = rects[j];
					if (r == null || drawn[j] == 1)
						continue;
					if (r.distance > s.distance) {
						Future<?> future = executor.submit(() -> {
							return g.drawImage(TextureLoader.wallTexture, r.screencoords[0],
									r.screencoords[1], r.screencoords[2], r.screencoords[3], r.texturecoords[0],
									r.texturecoords[1],
									r.texturecoords[2], r.texturecoords[3], null);
						});
						futures[j] = future;
						drawn[j] = 1;
					}
				}
				for (Future<?> future : futures) {
					try {
						if (future != null)
							future.get();
					} catch (InterruptedException | ExecutionException f) {
					}
				}

				g.drawImage(s.texture, s.screencoords[0],
						s.screencoords[1], s.screencoords[2], s.screencoords[3], s.texturecoords[0],
						s.texturecoords[1],
						s.texturecoords[2], s.texturecoords[3], null);

			}
			for (int j = 0; j < size; j++) {
				rect r = rects[j];
				if (r == null || drawn[j] == 1)
					continue;
				if (S == null || r.distance < S.distance) {
					Future<?> future = executor.submit(() -> {
						return g.drawImage(TextureLoader.wallTexture, r.screencoords[0],
								r.screencoords[1], r.screencoords[2], r.screencoords[3], r.texturecoords[0],
								r.texturecoords[1],
								r.texturecoords[2], r.texturecoords[3], null);
					});
					futures[j] = future;
				}
			}
			for (Future<?> future : futures) {
				try {
					if (future != null)
						future.get();
				} catch (InterruptedException | ExecutionException f) {
				}
			}

			Arrays.setAll(rects, i -> null);

			repaint();
		}

	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}

	public void shoot() {
		if (reload > 0) {
			return;
		}
		reload = 10;
		Projectile p = new Projectile(posX, posY, dirX, dirY, true);
		projectiles.add(p);
	}

	public void moveThings() {
		reload -= 1;
		for (int i = 0; i < characters.size(); i++){
			int a = (int)(Math.random()*361);
			projectiles = characters.get(i).move(posX, posY, characters, projectiles, map, a);
			if (characters.get(i).dead){
				characters.remove(i);
			} 
		}
		for (int i = 0; i < projectiles.size(); i++) {
			Projectile p = projectiles.get(i);
			p.move(map);
			if (p.isdead()) {
				projectiles.remove(p);
			} else if (p.outOfBounds(48, 48, map)) {
				projectiles.remove(p);
			}
		}
	}

	public static int wallHit(int mapX, int mapY, double posX, double posY, int side) {
		int type;
		if ((type = map[mapX][mapY]) != 0)
			return type;
		if (side == 0) {
			if ((type = map[mapX - 1][mapY]) != 0 && posX < mapX)
				return type;
			if ((type = map[mapX + 1][mapY]) != 0 && posX > mapX + 1) {
				return type;
			}
		} else {
			if ((type = map[mapX][mapY + 1]) != 0 && posY > mapY + 1)
				return type;
			else if ((type = map[mapX][mapY - 1]) != 0 && posY < mapY)
				return type;
		}
		type = 0;
		return type;
	}

	public static void renderWalls() {
		for (int x = 0; x < 800; x++) {
			double c = x / 400.0 - 1;
			double raydirX = dirX + c * planeX;
			double raydirY = dirY + c * planeY;
			double dx = (raydirX == 0) ? 1e20 : Math.abs(1 / raydirX);
			double dy = (raydirY == 0) ? 1e20 : Math.abs(1 / raydirY);

			int side = 0;

			int mapX = (int) posX;
			int mapY = (int) posY;

			double lenX;
			double lenY;
			double stepX;
			double stepY;

			if (raydirX < 0) {
				lenX = (posX - mapX) * dx;
				stepX = -1;
			} else {
				lenX = (1 - posX + mapX) * dx;
				stepX = 1;
			}
			if (raydirY < 0) {
				lenY = (posY - mapY) * dy;
				stepY = -1;
			} else {
				lenY = (1 + mapY - posY) * dy;
				stepY = 1;
			}

			double wallcoord;
			double dist;

			int hits = 0;
			for (int i = 0; i < 200; i++) {
				if ((mapX < 0 || mapX >= 48) || (mapY < 0 || mapY >= 48))
					break;

				int walltype;
				if ((walltype = wallHit(mapX, mapY, posX, posY, side)) != 0) {
					hits++;
					boolean b = false;
					if (hits >= transparencyLimit) {
						hits = transparencyLimit;
						b = true;
					}

					int height = 800;

					if (side == 0) {
						dist = (lenX - dx);
						double texturedist = dist * raydirY + posY;
						wallcoord = texturedist - (int) texturedist;
					} else {
						dist = (lenY - dy);
						double texturedist = dist * raydirX + posX;
						wallcoord = texturedist - (int) texturedist;
					}

					addWallStrip(dist, wallcoord, hits, x, height);

					if (b)
						break;

				}

				if (lenY < lenX) {
					mapY += stepY;
					lenY += dy;
					side = 1;
				} else {
					mapX += stepX;
					lenX += dx;
					side = 0;
				}
			}
		}
	}

	public static void addWallStrip(double dist, double wallcoord, int hits, int x, int h) {
		int lh = (int) (800 / dist);
		int lineHeight = (int) (h / dist);
		int screencoords[] = { (int) x, (int) (400 + lh / 2 - lineHeight), (int) x + 1,
				(int) (400 + lh / 2) };
		int texturecoords[] = { (int) (wallcoord * 100), 0, (int) (wallcoord * 100) + 1, 99 };
		rects[800 * (transparencyLimit - hits) + x] = new rect(screencoords, texturecoords,
				TextureLoader.wallTexture, dist);
	}

	public void drawFloor() {
		DataBufferInt imageBuffer = (DataBufferInt) image.getRaster().getDataBuffer();
		DataBufferInt textureBuffer = (DataBufferInt) TextureLoader.floorTexture.getRaster().getDataBuffer();
		int textureX = TextureLoader.floorTexture.getWidth();
		int textureY = TextureLoader.floorTexture.getHeight();

		int imageData[] = imageBuffer.getData();
		int textureData[] = textureBuffer.getData();

		for (int x = 0; x < 800; x++) {
			double c = x / 400.0 - 1;
			double raydirX = dirX + c * planeX;
			double raydirY = dirY + c * planeY;
			for (int y = 0; y < 400; y++) {
				double perpDist = 400.0 / y;
				double pixelx = (raydirX * perpDist + posX);
				double pixely = (raydirY * perpDist + posY);
				if ((pixelx >= 0 && pixelx < 48) && (pixely >= 0 && pixely < 48)
						&& map[(int) pixelx][(int) pixely] != 2) {
					int imagex = (int) ((pixelx - (int) pixelx) * textureX);
					int imagey = (int) ((pixely - (int) pixely) * textureY);
					int pixelColor = textureData[textureX * imagey + imagex];
					imageData[800 * (y + 400) + x] = pixelColor;
				}
			}
		}
	}

	public void renderProjectiles() {
		double sx;
		double sd;
		double sy;
		for (int i = 0; i < projectiles.size(); i++) {
			double x = projectiles.get(i).getX();
			double y = projectiles.get(i).getY();
			double dist = Math.sqrt((x - posX) * (x - posX) + (y - posY) * (y - posY));
			double angleC = Math.atan(Math.abs(dirY) / Math.abs(dirX));
			if (dirX < 0)
				angleC = Math.PI - angleC;
			if (dirY < 0)
				angleC *= -1;
			if (angleC < 0)
				angleC += 2 * Math.PI;
			double angleE = Math.atan(Math.abs(posY - y) / Math.abs(posX - x));
			if (x - posX < 0)
				angleE = Math.PI - angleE;
			if (y - posY < 0)
				angleE *= -1;
			if (angleE < 0)
				angleE += 2 * Math.PI;
			double angledif = angleE - angleC;
			if (angledif > Math.PI)
				angledif -= Math.PI * 2;
			if (angledif < -Math.PI)
				angledif += Math.PI * 2;
			if (Math.abs(angledif) < 0.78) {

				sd = dist * Math.abs(Math.cos(angledif));
				sx = (dist * Math.sin(-angledif) / sd) / 0.65 * 400 + 400;
				sy = 400 + 120 / sd;
				double dim = 100 / sd;
				double cornerX = sx - 50 / sd;
				double cornerY = sy - 50 / sd;
				int[] screenCoords = { (int) cornerX, (int) cornerY, (int) (cornerX + dim), (int) (cornerY + dim) };
				int[] textureCoords = { 0, 0, 225, 225 };
				sprite p = new sprite(screenCoords, textureCoords, image, sd);
				sprites.add(p);
			}
		}
	}

	public static void renderSprites() {
		sprites.clear();
		double sx;
		double sd;
		for (int i = 0; i < characters.size(); i++) {
			double x = characters.get(i).getX();
			double y = characters.get(i).getY();
			double dist = Math.sqrt((x - posX) * (x - posX) + (y - posY) * (y - posY));
			double angleC = Math.atan(Math.abs(dirY) / Math.abs(dirX));
			if (dirX < 0)
				angleC = Math.PI - angleC;
			if (dirY < 0)
				angleC *= -1;
			if (angleC < 0)
				angleC += 2 * Math.PI;
			double angleE = Math.atan(Math.abs(posY - y) / Math.abs(posX - x));
			if (x - posX < 0)
				angleE = Math.PI - angleE;
			if (y - posY < 0)
				angleE *= -1;
			if (angleE < 0)
				angleE += 2 * Math.PI;
			double angledif = angleE - angleC;
			if (angledif > Math.PI)
				angledif -= Math.PI * 2;
			if (angledif < -Math.PI)
				angledif += Math.PI * 2;
			if (Math.abs(angledif) < 0.78) {
				Character e = characters.get(i);
				sd = dist * Math.abs(Math.cos(angledif));
				sx = (int) ((dist * Math.sin(-angledif) / sd) / 0.65 * 400 + 400);
				int spriteDim = (int) (350 / sd);
				int screenCoords[] = { (int) sx - spriteDim, 400 - spriteDim, (int) sx + spriteDim,
						400 + spriteDim };
				int textureCoords[] = { 0, 0, e.getSprite().getWidth(), e.getSprite().getHeight() };
				sprite r = new sprite(screenCoords, textureCoords, e.getSprite(), sd);
				sprites.add(r);
			}
		}
	}

	public static void move() {

		double oldX = posX;
		double oldY = posY;

		if (keys[0]) {
			posX += dirX * moveSpeed;
			posY += dirY * moveSpeed;
		}
		if (keys[1]) {
			posX -= planeX * moveSpeed;
			posY -= planeY * moveSpeed;
		}
		if (keys[2]) {
			posX -= dirX * moveSpeed;
			posY -= dirY * moveSpeed;
		}
		if (keys[3]) {
			posX += planeX * moveSpeed;
			posY += planeY * moveSpeed;
		}

		if (keys[4]) {
			double oldDirX = dirX;
			dirX = dirX * Math.cos(rotSpeed) - dirY * Math.sin(rotSpeed);
			dirY = oldDirX * Math.sin(rotSpeed) + dirY * Math.cos(rotSpeed);
			double oldPlaneX = planeX;
			planeX = planeX * Math.cos(rotSpeed) - planeY * Math.sin(rotSpeed);
			planeY = oldPlaneX * Math.sin(rotSpeed) + planeY * Math.cos(rotSpeed);
		} else if (keys[5]) {
			double oldDirX = dirX;
			dirX = dirX * Math.cos(-rotSpeed) - dirY * Math.sin(-rotSpeed);
			dirY = oldDirX * Math.sin(-rotSpeed) + dirY * Math.cos(-rotSpeed);
			double oldPlaneX = planeX;
			planeX = planeX * Math.cos(-rotSpeed) - planeY * Math.sin(-rotSpeed);
			planeY = oldPlaneX * Math.sin(-rotSpeed) + planeY * Math.cos(-rotSpeed);
		}

		if (map[(int) posX][(int) posY] != 0) {
			posX = oldX;
			posY = oldY;
		}
	}

	public static void main(String[] args) throws IOException {
		System.setProperty("sun.java2d.opengl", "true");
		JFrame frame = new JFrame("thing");
		frame.setSize(800, 800);
		frame.setLocation(0, 0);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new Engine());
		frame.setVisible(true);
	}

}