package com.mwq.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.URL;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import com.mwq.dao.Dao;
import com.mwq.mwing.MPanel;

public class LandFrame extends JFrame {

	private JPasswordField passwordField;// 密码框

	private JComboBox usernameComboBox;// 用户名下拉菜单

	public static void main(String args[]) {
		try {
			LandFrame frame = new LandFrame();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public LandFrame() {
		// 首先设置窗口的相关信息
		super();// 调用父类的构造方法
		setTitle(" 皇家铁道学院餐饮管理系统 ");// 设置窗口的标题
		setResizable(false);// 设置窗口不可以改变大小
		setAlwaysOnTop(false);// 设置窗口总在最前方
		setBounds(100, 100, 428,292);// 设置窗口的大小
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 设置当关闭窗口时执行的动作

		// 下面将创建一个面板对象并添加到窗口的容器中
		final MPanel panel = new MPanel(this.getClass().getResource(
				"/img/land_background.jpg"));// 创建一个面板对象
		panel.setLayout(new GridBagLayout());// 设置面板的布局管理器为网格组布局
		getContentPane().add(panel, BorderLayout.CENTER);// 将面板添加到窗体中

		final JLabel topLabel = new JLabel();
		topLabel.setPreferredSize(new Dimension(0, 126));
		final GridBagConstraints gridBagConstraints_5 = new GridBagConstraints();
		gridBagConstraints_5.gridx = 0;
		gridBagConstraints_5.gridy = 0;
		panel.add(topLabel, gridBagConstraints_5);

		final JLabel leftLabel = new JLabel();
		leftLabel.setPreferredSize(new Dimension(140, 0));
		final GridBagConstraints gridBagConstraints_3 = new GridBagConstraints();
		gridBagConstraints_3.gridy = 1;
		gridBagConstraints_3.gridx = 0;
		panel.add(leftLabel, gridBagConstraints_3);

		final JLabel rightLabel = new JLabel();
		rightLabel.setPreferredSize(new Dimension(55, 0));
		final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
		gridBagConstraints_2.gridy = 1;
		gridBagConstraints_2.gridx = 1;
		panel.add(rightLabel, gridBagConstraints_2);

		// 创建并设置用户名下拉菜单
		usernameComboBox = new JComboBox();// 创建用户名下拉菜单组件对象
		usernameComboBox.setMaximumRowCount(5);// 设置下拉菜单最多可显示的选项数
		usernameComboBox.addItem("请选择");// 为下拉菜单添加提示项
		usernameComboBox
				.addActionListener(new UsernameComboBoxActionListener());// 为下拉菜单添加事件监听器
		final GridBagConstraints gridBagConstraints = new GridBagConstraints();// 创建网格组布局管理器对象
		gridBagConstraints.anchor = GridBagConstraints.WEST;// 设置为靠左侧显示
		gridBagConstraints.gridy = 1;// 设置行索引为1
		gridBagConstraints.gridx = 2;// 设置列索引为2
		panel.add(usernameComboBox, gridBagConstraints);// 将组件按指定的布局管理器添加到面板中

		// 创建并设置密码框
		passwordField = new JPasswordField();// 创建密码框组件对象
		passwordField.setColumns(20);// 设置密码框可显示的字符数
		passwordField.setText("      ");// 设置密码框默认显示6个空格
		passwordField.addFocusListener(new PasswordFieldFocusListener());// 为密码框添加焦点监听器
		final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();// 创建网格组布局管理器对象
		gridBagConstraints_1.insets = new Insets(5, 0, 0, 0);// 设置组件外部上方的填充量为5像素
		gridBagConstraints_1.anchor = GridBagConstraints.WEST;// 设置为靠左侧显示
		gridBagConstraints_1.gridy = 2;// 设置行索引为2
		gridBagConstraints_1.gridx = 2;// 设置列索引为2
		panel.add(passwordField, gridBagConstraints_1);// 将组件按指定的布局管理器添加到面板中

		// 创建并设置一个用来添加三个按钮的面板
		final JPanel buttonPanel = new JPanel();// 创建一个用来添加按钮的面板
		buttonPanel.setOpaque(false);// 设置面板的背景为透明
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));// 设置面板采用水平箱布局
		final GridBagConstraints gridBagConstraints_4 = new GridBagConstraints();// 创建网格组布局管理器对象
		gridBagConstraints_4.insets = new Insets(10, 0, 0, 0);// 设置组件外部上方的填充量为10像素
		gridBagConstraints_4.gridwidth = 2;// 设置其占两列
		gridBagConstraints_4.gridy = 3;// 设置行索引为3
		gridBagConstraints_4.gridx = 1;// 设置列索引为1
		panel.add(buttonPanel, gridBagConstraints_4);// 将组件按指定的布局管理器添加到面板中

		// 创建并设置一个登录按钮，并将其添加到用来添加按钮的面板中
		final JButton landButton = new JButton();// 创建登录按钮组件对象
		landButton.setMargin(new Insets(0, 0, 0, 0));// 设置按钮边框和标签之间的间隔
		landButton.setContentAreaFilled(false);// 设置不绘制按钮的内容区域
		landButton.setBorderPainted(false);// 设置不绘制按钮的边框
		URL landUrl = this.getClass().getResource("/img/land_submit.png");// 获得默认情况下登录按钮显示图片的URL
		landButton.setIcon(new ImageIcon(landUrl));// 设置默认情况下登录按钮显示的图片
		URL landOverUrl = this.getClass().getResource(
				"/img/land_submit_over.png");// 获得当鼠标经过登录按钮时显示图片的URL
		landButton.setRolloverIcon(new ImageIcon(landOverUrl));// 设置当鼠标经过登录按钮时显示的图片
		URL landPressedUrl = this.getClass().getResource(
				"/img/land_submit_pressed.png");// 获得当登录按钮被按下时显示图片的URL
		landButton.setPressedIcon(new ImageIcon(landPressedUrl));// 设置当登录按钮被按下时显示的图片
		landButton.addActionListener(new LandButtonActionListener());// 为登录按钮添加事件监听器
		buttonPanel.add(landButton);// 将登录按钮添加到用来添加按钮的面板中

		final JButton resetButton = new JButton();
		resetButton.setMargin(new Insets(0, 0, 0, 0));
		resetButton.setContentAreaFilled(false);
		resetButton.setBorderPainted(false);
		URL resetUrl = this.getClass().getResource("/img/land_reset.png");
		resetButton.setIcon(new ImageIcon(resetUrl));
		URL resetOverUrl = this.getClass().getResource(
				"/img/land_reset_over.png");
		resetButton.setRolloverIcon(new ImageIcon(resetOverUrl));
		URL resetPressedUrl = this.getClass().getResource(
				"/img/land_reset_pressed.png");
		resetButton.setPressedIcon(new ImageIcon(resetPressedUrl));
		resetButton.addActionListener(new ResetButtonActionListener());
		buttonPanel.add(resetButton);

		final JButton exitButton = new JButton();
		exitButton.setMargin(new Insets(0, 0, 0, 0));
		exitButton.setContentAreaFilled(false);
		exitButton.setBorderPainted(false);
		URL exitUrl = this.getClass().getResource("/img/land_exit.png");
		exitButton.setIcon(new ImageIcon(exitUrl));
		URL exitOverUrl = this.getClass()
				.getResource("/img/land_exit_over.png");
		exitButton.setRolloverIcon(new ImageIcon(exitOverUrl));
		URL exitPressedUrl = this.getClass().getResource(
				"/img/land_exit_pressed.png");
		exitButton.setPressedIcon(new ImageIcon(exitPressedUrl));
		exitButton.addActionListener(new ExitButtonActionListener());
		buttonPanel.add(exitButton);
		//

		// 初始化用户名下拉菜单
		Vector userNameV = Dao.getInstance().sUserNameOfNotFreeze();
		if (userNameV.size() == 0) {
			usernameComboBox.addItem("TSoft");
		} else {
			for (int i = 0; i < userNameV.size(); i++) {
				usernameComboBox.addItem(userNameV.get(i));
			}
		}

	}

	class UsernameComboBoxActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String userName = (String) usernameComboBox.getSelectedItem();
			if (userName.equals("TSoft"))
				passwordField.setText("111");
		}
	}

	class PasswordFieldFocusListener implements FocusListener {
		public void focusGained(FocusEvent e) {
			passwordField.setText("");
		}

		public void focusLost(FocusEvent e) {
			char[] passwords = passwordField.getPassword();
			String password = turnCharsToString(passwords);
			if (password.length() == 0) {
				passwordField.setText("      ");
			}
		}
	}

	class ExitButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			System.exit(0);
		}
	}

	class ResetButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			usernameComboBox.setSelectedIndex(0);
			passwordField.setText("      ");
		}
	}

	class LandButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String username = usernameComboBox.getSelectedItem().toString();// 获得登录用户的名称
			if (username.equals("请选择")) {// 查看是否选择了登录用户
				JOptionPane.showMessageDialog(null, "请选择登录用户！", "友情提示",
						JOptionPane.INFORMATION_MESSAGE);// 弹出提示
				resetUsernameAndPassword();// 恢复登录用户和登录密码
			}
			char[] passwords = passwordField.getPassword();// 获得登录用户的密码
			String inputPassword = turnCharsToString(passwords);// 将密码从char型数组转换成字符串
			if (username.equals("root")) {// 查看是否为默认用户登录
				if (inputPassword.equals("root")) {// 查看密码是否为默认密码
					land(null);// 登录成功
					String infos[] = { "请立刻单击“用户管理”按钮添加用户！",
							"添加用户后需要重新登录，本系统才能正常使用！" };// 组织提示信息
					JOptionPane.showMessageDialog(null, infos, "友情提示",
							JOptionPane.INFORMATION_MESSAGE);// 弹出提示
				} else {// 密码错误
					JOptionPane.showMessageDialog(null,
							"默认用户“root”的登录密码为“root”！", "友情提示",
							JOptionPane.INFORMATION_MESSAGE);// 弹出提示
					passwordField.setText("111");// 将密码设置为默认密码
				}
			} else {
				if (inputPassword.length() == 0) {// 用户未输入登录密码
					JOptionPane.showMessageDialog(null, "请输入登录密码！", "友情提示",
							JOptionPane.INFORMATION_MESSAGE);// 弹出提示
					resetUsernameAndPassword();// 恢复登录用户和登录密码
					
				}
				Vector user = Dao.getInstance().sUserByName(username);// 查询登录用户
				String password = user.get(5).toString();// 获得登录用户的密码
				if (inputPassword.equals(password)) {// 查看登录密码是否正确
					land(user);// 登录成功
				} else {// 登录密码错误
					JOptionPane.showMessageDialog(null, "登录密码错误，请确认后重新登录！",
							"友情提示", JOptionPane.INFORMATION_MESSAGE);// 弹出提示
					resetUsernameAndPassword();// 恢复登录用户和登录密码
					return;
				}
			}
		}

		private void resetUsernameAndPassword() {// 恢复登录用户和登录密码
			usernameComboBox.setSelectedIndex(0);// 恢复选中的登录用户为“请选择”项
			passwordField.setText("      ");// 恢复密码框的默认值为6个空格
			return;// 直接返回
		}

		private void land(Vector user) {// 登录成功
			TipWizardFrame tipWizard = new TipWizardFrame(user);// 创建主窗体对象
			tipWizard.setVisible(true);// 设置主窗体可见
			setVisible(false);// 设置登录窗口不可见
		}

	}

	private String turnCharsToString(char[] chars) {
		StringBuffer strBuf = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			strBuf.append(chars[i]);
		}
		return strBuf.toString().trim();
	}
}
