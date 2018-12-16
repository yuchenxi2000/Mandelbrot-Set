package mandelbrot8;

import java.awt.Dimension;
import java.awt.EventQueue;

public class Launcher {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame frame = new Frame();
					frame.setMaximumSize(new Dimension(600, 450));//设置最大值
					frame.setMinimumSize(new Dimension(600, 450));//设置最小值
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(0);
				}
			}
		});
	}
}
