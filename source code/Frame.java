package mandelbrot8;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;

public class Frame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4367351581352929222L;
	private JPanel contentPane;
	private JPanel paintPane;
	private JPanel helpPane;
	private JPanel aboutPane;

	private int image_width = 400;
	private int image_height = 400;
	private int image_xcenter = 300;
	private int image_ycenter = 200;

	private static Double xcenter = 0.0;
	private static Double ycenter = 0.0;

	private static Double XMIN = -2.0;
	private static Double YMIN = -2.0;
	private static Double XMAX = 2.0;
	private static Double YMAX = 2.0;
	private static Double s = 2.0;

	private int max_iteration = 32;
	private int iteration_mod = 0;

	/*
	 * 加载图片
	 */
	public void paintPaneinit() {
		contentPane.setEnabled(false);
		contentPane.setVisible(false);
		paintPane = new JPanel();
		paintPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(paintPane);
		paintPane.setLayout(null);

		BufferedImage image = draw(image_width, image_height);
		JLabel lblPaint = new JLabel();
		lblPaint.setBounds(100, 0, image_width, image_height);
		paintPane.add(lblPaint);

		/*
		 * back 按钮
		 */
		JButton btnBack = new JButton("back");
		btnBack.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnBack.setBounds(10, 400, 80, 20);
		paintPane.add(btnBack);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					turnback(paintPane);
				} catch (Exception e2) {
					e2.printStackTrace();
					System.exit(0);
				}
			}
		});

		JLabel lblScale = new JLabel("Scale");
		lblScale.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		lblScale.setHorizontalAlignment(SwingConstants.CENTER);
		lblScale.setBounds(350, 400, 200, 20);
		paintPane.add(lblScale);

		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setMaximumRowCount(4);
		comboBox.setBounds(10, 50, 80, 30);
		comboBox.addItem("0 (128)");
		comboBox.addItem("1 (auto)");
		comboBox.addItem("2 (256)");
		comboBox.addItem("3 (512)");
		paintPane.add(comboBox);
		ItemListener itemListener = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getItem()=="3 (512)"){
					iteration_mod=3;
				}else if(e.getItem()=="1 (auto)"){
					iteration_mod=1;
				}else if(e.getItem()=="2 (256)"){
					iteration_mod=2;
				}else {
					iteration_mod=0;
				}

			}
		};
		comboBox.addItemListener(itemListener);

		JLabel lblIteration = new JLabel("iterations");
		lblIteration.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		lblIteration.setHorizontalAlignment(SwingConstants.CENTER);
		lblIteration.setBounds(10, 20, 80, 20);
		paintPane.add(lblIteration);
		
		JButton btnRestart = new JButton("restart");
		btnRestart.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnRestart.setBounds(10, 330, 80, 20);
		paintPane.add(btnRestart);
		btnRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					xcenter = 0.0;
					ycenter = 0.0;

					XMIN = -2.0;
					YMIN = -2.0;
					XMAX = 2.0;
					YMAX = 2.0;
					s = 2.0;
					BufferedImage image = draw(image_width, image_height);
					Icon icon = new ImageIcon(image);
					lblPaint.setIcon(icon);
				} catch (Exception e2) {
					e2.printStackTrace();
					System.exit(0);
				}
			}
		});

		Icon icon = new ImageIcon(image);
		lblPaint.setIcon(icon);

		addMouseListener(new MouseListener() { // 为窗口添加鼠标事件监听器
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) { // 判断获取的按钮是否为鼠标的右击
					int xi = e.getX();
					int yj = e.getY();
					if (xi > 500 || xi < 100 || yj > 400 || s > 20) {
						return;
					}
					adjust(s);
					xcenter = 2.0 * s * (xi - image_xcenter) / image_width + xcenter;
					ycenter = 2.0 * s * (yj - image_ycenter) / image_height + ycenter;
					s = s * 1.1;
					XMIN = xcenter - s;
					XMAX = xcenter + s;
					YMIN = ycenter - s;
					YMAX = ycenter + s;
					BufferedImage image = draw(image_width, image_height);
					Icon icon = new ImageIcon(image);
					lblPaint.setIcon(icon);
					lblScale.setText(s.toString());
				}
				if (e.getButton() == MouseEvent.BUTTON1) { // 判断获取的按钮是否为鼠标的左击
					int xi = e.getX();
					int yj = e.getY();
					if (xi > 500 || xi < 100 || yj > 400 || s < 2.5e-14) {
						return;
					}
					adjust(s);
					xcenter = 2.0 * s * (xi - image_xcenter) / image_width + xcenter;
					ycenter = 2.0 * s * (yj - image_ycenter) / image_height + ycenter;
					s = s / 1.1;
					XMIN = xcenter - s;
					XMAX = xcenter + s;
					YMIN = ycenter - s;
					YMAX = ycenter + s;
					BufferedImage image = draw(image_width, image_height);
					Icon icon = new ImageIcon(image);
					lblPaint.setIcon(icon);
					lblScale.setText(s.toString());
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

	/*
	 * 返回主页面
	 */
	public void turnback(JPanel jpanel) {
		jpanel.setEnabled(false);
		jpanel.setVisible(false);
		contentPane.setEnabled(true);
		contentPane.setVisible(true);
		setContentPane(contentPane);
	}

	/*
	 * 帮助页面初始化
	 */
	public void helpPaneinit() {
		contentPane.setEnabled(false);
		contentPane.setVisible(false);
		helpPane = new JPanel();
		helpPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(helpPane);
		helpPane.setLayout(null);

		JLabel lblHelp = new JLabel("Help");
		lblHelp.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lblHelp.setHorizontalAlignment(SwingConstants.CENTER);
		lblHelp.setBounds(219, 16, 162, 30);
		helpPane.add(lblHelp);

		JLabel lblContent = new JLabel("Help");
		lblContent.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lblContent.setHorizontalAlignment(SwingConstants.CENTER);
		lblContent.setBounds(219, 16, 162, 30);
		helpPane.add(lblContent);

		JTextPane txtpnclickstartTo = new JTextPane();
		txtpnclickstartTo.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		txtpnclickstartTo.setBackground(SystemColor.window);
		txtpnclickstartTo.setText(
				"1.click \"start\" to play\n2.left click to zoom in\n3.right click to zoom out\n4.click \"back\" to return\n5.click \"about\".there's a wonderful land for you......\n6.have fun!");
		txtpnclickstartTo.setBounds(118, 114, 415, 216);
		helpPane.add(txtpnclickstartTo);

		/*
		 * back 按钮
		 */
		JButton btnBack = new JButton("back");
		btnBack.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnBack.setBounds(10, 400, 80, 20);
		helpPane.add(btnBack);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					turnback(helpPane);
				} catch (Exception e2) {
					e2.printStackTrace();
					System.exit(0);
				}
			}
		});
	}

	/*
	 * 关于页面初始化
	 */
	public void aboutPaneinit() {
		contentPane.setEnabled(false);
		contentPane.setVisible(false);
		aboutPane = new JPanel();
		aboutPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(aboutPane);
		aboutPane.setLayout(null);

		JLabel lblContent = new JLabel("About");
		lblContent.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lblContent.setHorizontalAlignment(SwingConstants.CENTER);
		lblContent.setBounds(219, 16, 162, 30);
		aboutPane.add(lblContent);

		JTextPane txtpnclickstartTo = new JTextPane();
		txtpnclickstartTo.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		txtpnclickstartTo.setBackground(SystemColor.window);
		txtpnclickstartTo.setText(
				"Mandelbrot Set v1.1\n\ncopyright yuchenxi\n2018.8.17\n\nany requests please contact me by email.\nemail:2763057708@qq.com");
		txtpnclickstartTo.setBounds(118, 114, 415, 216);
		aboutPane.add(txtpnclickstartTo);

		/*
		 * back 按钮
		 */
		JButton btnBack = new JButton("back");
		btnBack.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnBack.setBounds(10, 400, 80, 20);
		aboutPane.add(btnBack);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					turnback(aboutPane);
				} catch (Exception e2) {
					e2.printStackTrace();
					System.exit(0);
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Frame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Mandelbrot Set");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(219, 16, 162, 30);
		contentPane.add(lblNewLabel);

		/*
		 * 开始按钮
		 */
		JButton btnStart = new JButton("start");
		btnStart.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnStart.setBounds(219, 90, 162, 40);
		contentPane.add(btnStart);
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					paintPaneinit();
				} catch (Exception e2) {
					e2.printStackTrace();
					System.exit(0);
				}
			}
		});

		/*
		 * 帮助
		 */
		JButton btnHelp = new JButton("help");
		btnHelp.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnHelp.setBounds(219, 190, 162, 40);
		contentPane.add(btnHelp);
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					helpPaneinit();
				} catch (Exception e2) {
					e2.printStackTrace();
					System.exit(0);
				}
			}
		});

		/*
		 * 作者信息
		 */
		JButton btnAbout = new JButton("about");
		btnAbout.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		btnAbout.setBounds(219, 290, 162, 40);
		contentPane.add(btnAbout);
		btnAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					aboutPaneinit();
				} catch (Exception e2) {
					e2.printStackTrace();
					System.exit(0);
				}
			}
		});
	}
	
	/*
	 * 计算图片
	 */
	public BufferedImage draw(int width, int height) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		WritableRaster raster = image.getRaster();
		ColorModel model = image.getColorModel();
	
		Color color = Color.red;
		int argb = color.getRGB();
		Object colordata = model.getDataElements(argb, null);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Double a = XMIN + i * (XMAX - XMIN) / width;
				Double b = YMIN + j * (YMAX - YMIN) / height;
				if (!infinity(a, b)) {
					raster.setDataElements(i, j, colordata);
				}
			}
		}
		return image;
	}
	
	/*
	 * 判断序列是否发散
	 */
	public Boolean infinity(Double a, Double b) {
		Double x = 0.0;
		Double y = 0.0;
		int iteration = 1;
		while (x <= 2.0 && y <= 2.0 && iteration < max_iteration) {
			Double x0 = x * x - y * y + a;
			Double y0 = 2 * x * y + b;
			x = x0;
			y = y0;
			iteration++;
		}
		return x > 2.0 || y > 2.0;
	}

	public void adjust(Double s) {
		if (iteration_mod == 1) {
			if (s < 0.001) {
				max_iteration = (int) Math.floor(-4.5 * Math.log(s)) + 1;
			} else {
				max_iteration = 32;
			}
		} else if (iteration_mod == 0) {
			max_iteration = 128;
		} else if (iteration_mod == 2) {
			max_iteration = 256;
		} else {
			max_iteration = 512;
		}
	}
}
