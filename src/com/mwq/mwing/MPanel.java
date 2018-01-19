package com.mwq.mwing;

import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MPanel extends JPanel {

	private ImageIcon imageIcon;// 声明一个图片对象

	public MPanel(URL imgUrl) {
		super();// 继承父类的构造方法
		setLayout(new GridBagLayout());// 将布局管理器修改为网格组布局
		imageIcon = new ImageIcon(imgUrl);// 根据传入的URL创建ImageIcon对象
		setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());// 设置面板与图片等大
	}

	protected void paintComponent(Graphics g) {// 重写JPanel类的paintComponent()方法
		super.paintComponent(g);// 调用JPanel类的paintComponent()方法
		Image image = imageIcon.getImage();// 通过ImageIcon对象获得Image对象
		g.drawImage(image, 0, 0, null);// 绘制Image对象，即将图片绘制到面板中
	}

}
