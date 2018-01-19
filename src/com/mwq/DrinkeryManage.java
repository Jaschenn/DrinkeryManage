package com.mwq;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.mwq.frame.LandFrame;

public class DrinkeryManage {
	public DrinkeryManage() {
		// Center the window
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		LandFrame landFrame = new LandFrame();
		Dimension frameSize = landFrame.getSize();
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		landFrame.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		landFrame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (Exception exception) {
					exception.printStackTrace();
				}
				new DrinkeryManage();
			}
		});
	}

}
