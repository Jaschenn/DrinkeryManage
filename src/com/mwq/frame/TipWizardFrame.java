package com.mwq.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import com.mwq.dao.Dao;
import com.mwq.frame.check_out.DayDialog;
import com.mwq.frame.check_out.MonthDialog;
import com.mwq.frame.check_out.YearDialog;
import com.mwq.frame.manage.DeskNumDialog;
import com.mwq.frame.manage.MenuDialog;
import com.mwq.frame.manage.SortDialog;
import com.mwq.frame.user.UpdatePasswordDialog;
import com.mwq.frame.user.UserManagerDialog;
import com.mwq.mwing.MButton;
import com.mwq.mwing.MTable;
import com.mwq.tool.Today;
import com.mwq.tool.Validate;

public class TipWizardFrame extends JFrame {

	private JLabel timeLabel;

	private JTextField amountTextField;

	private JTextField unitTextField;

	private JTextField nameTextField;

	private JTextField codeTextField;

	private JComboBox numComboBox;

	private JTextField changeTextField;

	private JTextField realWagesTextField;

	private JTextField expenditureTextField;

	private ButtonGroup buttonGroup = new ButtonGroup();

	private MTable rightTable;

	private Vector<String> rightTableColumnV;

	private Vector<Vector<Object>> rightTableValueV;

	private DefaultTableModel rightTableModel;

	private MTable leftTable;

	private Vector<String> leftTableColumnV;

	private Vector<Vector<Object>> leftTableValueV;

	private DefaultTableModel leftTableModel;

	private Vector<Vector<Vector<Object>>> menuOfDeskV;

	private Dimension screenSize;

	private final Dao dao = Dao.getInstance();

	/**
	 * Launch the application
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			TipWizardFrame frame = new TipWizardFrame(null);
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame
	 */
	public TipWizardFrame(final Vector user) {
		super();
		setTitle("  皇家铁道学院餐饮管理系统 ");
		setResizable(true);
		setBounds(0, 0, 1024, 768);
		setExtendedState(TipWizardFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		final JLabel topLabel = new JLabel();
		topLabel.setPreferredSize(new Dimension(0, 100));
		topLabel.setHorizontalAlignment(SwingConstants.CENTER);
		URL topUrl = this.getClass().getResource("/img/top.jpg");
		ImageIcon topIcon = new ImageIcon(topUrl);
		topLabel.setIcon(topIcon);
		getContentPane().add(topLabel, BorderLayout.NORTH);

		final JSplitPane splitPane = new JSplitPane();// 创建分割面版对象
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);// 设置为水平分割
		splitPane.setDividerLocation(755);// 设置面版默认的分割位置
		splitPane.setDividerSize(10);// 设置分割条的宽度
		splitPane.setOneTouchExpandable(true);// 设置为支持快速展开/折叠分割条
		splitPane.setBorder(new TitledBorder(null, "",
				TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, null, null));// 设置面版的边框
		getContentPane().add(splitPane, BorderLayout.CENTER);// 将分割面版添加到上级容器中

		final JPanel leftPanel = new JPanel();// 创建放于分割面版左侧的普通面版对象
		leftPanel.setLayout(new BorderLayout());// 设置面板的布局管理器
		splitPane.setLeftComponent(leftPanel);// 将普通面版对象添加到分割面版的左侧

		final JLabel leftTitleLabel = new JLabel();
		leftTitleLabel.setFont(new Font("", Font.BOLD, 14));
		leftTitleLabel.setPreferredSize(new Dimension(0, 25));
		leftTitleLabel.setText(" 已点菜     /  待确定列表：");
		leftPanel.add(leftTitleLabel, BorderLayout.NORTH);

		final JScrollPane leftScrollPane = new JScrollPane();
		leftPanel.add(leftScrollPane);

		menuOfDeskV = new Vector<Vector<Vector<Object>>>();

		leftTableColumnV = new Vector<String>();
		String leftTableColumns[] = { "状  态", "序    号", "商品编号", "商品名称", "单    位",
				"数    量", "单    价", "金    额" };
		for (int i = 0; i < leftTableColumns.length; i++) {
			leftTableColumnV.add(leftTableColumns[i]);
		}

		leftTableValueV = new Vector<Vector<Object>>();

		leftTableModel = new DefaultTableModel(leftTableValueV,
				leftTableColumnV);
		leftTableModel.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {// 通过表格模型监听器实现自动结账
				int rowCount = leftTable.getRowCount();// 获得签单列表中的行数
				float expenditure = 0.0f;// 默认消费 0 元
				for (int row = 0; row < rowCount; row++) {// 通过循环计算消费金额
					expenditure += Float.valueOf(leftTable.getValueAt(row, 7)
							.toString());// 累加消费金额
				}
				expenditureTextField.setText(expenditure + "0");// 更新“消费金额”文本框
			}
		});

		leftTable = new MTable(leftTableModel);
		leftScrollPane.setViewportView(leftTable);

		final JPanel rightPanel = new JPanel();// 创建放于分割面版右侧的普通面版对象
		rightPanel.setLayout(new BorderLayout());
		splitPane.setRightComponent(rightPanel);// 将普通面版对象添加到分割面版的右侧

		final JLabel rightTitleLabel = new JLabel();
		rightTitleLabel.setFont(new Font("", Font.BOLD, 14));
		rightTitleLabel.setPreferredSize(new Dimension(0, 25));
		rightTitleLabel.setText(" 餐桌列表：");
		rightPanel.add(rightTitleLabel, BorderLayout.NORTH);

		final JScrollPane rightScrollPane = new JScrollPane();
		rightPanel.add(rightScrollPane);

		rightTableColumnV = new Vector<String>();
		rightTableColumnV.add("序    号");
		rightTableColumnV.add("桌    号");
		rightTableColumnV.add("点菜时间");

		rightTableValueV = new Vector<Vector<Object>>();

		rightTableModel = new DefaultTableModel(rightTableValueV,
				rightTableColumnV);

		rightTable = new MTable(rightTableModel);
		rightTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int rSelectedRow = rightTable.getSelectedRow();// 获得“餐桌列表”中的选中行
				leftTableValueV.removeAllElements();// 清空“签单列表”中的所有行
				leftTableValueV.addAll(menuOfDeskV.get(rSelectedRow));// 将选中桌号的签单列表添加到“签单列表”中
				leftTableModel.setDataVector(leftTableValueV, leftTableColumnV);// 刷新“签单列表”
				leftTable.setRowSelectionInterval(0);// 选中“签单列表”中的第一行
				numComboBox.setSelectedItem(rightTable.getValueAt(rSelectedRow,
						1));// 同步选中“桌号”下拉菜单中的相应台号
			}
		});
		rightScrollPane.setViewportView(rightTable);

		final JPanel bottomPanel = new JPanel();
		bottomPanel.setPreferredSize(new Dimension(0, 230));
		bottomPanel.setLayout(new BorderLayout());
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);

		final JPanel orderDishesPanel = new JPanel();
		orderDishesPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		bottomPanel.add(orderDishesPanel, BorderLayout.NORTH);

		final JLabel numLabel = new JLabel();
		numLabel.setFont(new Font("", Font.BOLD, 12));
		numLabel.setText("桌号：");
		orderDishesPanel.add(numLabel);

		numComboBox = new JComboBox();
		numComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowCount = rightTable.getRowCount();// 获得开桌列表中的行数，即已开桌数
				if (rowCount > 0) {// 已经有开桌
					String selectedDeskNum = numComboBox.getSelectedItem()
							.toString();// 获得“桌号”下拉菜单中选中的桌号
					int needSelectedRow = -1;// 默认选中的桌号未开桌
					opened: for (int row = 0; row < rowCount; row++) {// 通过循环查看选中的桌号是否已经开桌
						String openedDeskNum = rightTable.getValueAt(row, 1)
								.toString();// 获得已开台的台号
						if (selectedDeskNum.equals(openedDeskNum)) {// 查看选中的台号是否已经开台
							needSelectedRow = row;// 选中的台号已经开台
							break opened;// 跳出循环
						}
					}
					if (needSelectedRow == -1) {// 选中的台号尚未开台，即新开台
						rightTable.clearSelection();// 取消选择“开台列表”中的选中行
						leftTableValueV.removeAllElements();// 清空“签单列表”中的所有行
						leftTableModel.setDataVector(leftTableValueV,
								leftTableColumnV);// 刷新“签单列表”
					} else {// 选中的台号已经开台，即添加菜品
						if (needSelectedRow != rightTable.getSelectedRow()) {
							// “台号”下拉菜单中选中的台号在“开台列表”中未被选中
							rightTable.setRowSelectionInterval(needSelectedRow);// 在“开台列表”中选中该台号
							leftTableValueV.removeAllElements();// 清空“签单列表”中的所有行
							leftTableValueV.addAll(menuOfDeskV
									.get(needSelectedRow));// 将选中台号的签单列表添加到“签单列表”中
							leftTableModel.setDataVector(leftTableValueV,
									leftTableColumnV);// 刷新“签单列表”
							leftTable.setRowSelectionInterval(0);// 选中“签单列表”中的第一行
						}
					}
				}
			}
		});
		initNumComboBox();
		orderDishesPanel.add(numComboBox);

		final JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		orderDishesPanel.add(panel);

		final JLabel codeALabel = new JLabel();
		codeALabel.setFont(new Font("", Font.BOLD, 12));
		codeALabel.setText("  商品（");
		final GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridx = 0;
		panel.add(codeALabel, gridBagConstraints);

		final JRadioButton numRadioButton = new JRadioButton();
		numRadioButton.setFont(new Font("", Font.BOLD, 12));
		buttonGroup.add(numRadioButton);
		numRadioButton.setText("编号");
		final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
		gridBagConstraints_1.gridy = 0;
		gridBagConstraints_1.gridx = 1;
		panel.add(numRadioButton, gridBagConstraints_1);

		final JLabel codeBLabel = new JLabel();
		codeBLabel.setFont(new Font("", Font.BOLD, 12));
		codeBLabel.setText("/");
		final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
		gridBagConstraints_2.gridy = 0;
		gridBagConstraints_2.gridx = 2;
		panel.add(codeBLabel, gridBagConstraints_2);

		final JRadioButton codeRadioButton = new JRadioButton();
		codeRadioButton.setFont(new Font("", Font.BOLD, 12));
		buttonGroup.add(codeRadioButton);
		codeRadioButton.setSelected(true);
		codeRadioButton.setText("助记码");
		final GridBagConstraints gridBagConstraints_3 = new GridBagConstraints();
		gridBagConstraints_3.gridy = 0;
		gridBagConstraints_3.gridx = 3;
		panel.add(codeRadioButton, gridBagConstraints_3);

		final JLabel codeCLabel = new JLabel();
		codeCLabel.setText("）：");
		final GridBagConstraints gridBagConstraints_4 = new GridBagConstraints();
		gridBagConstraints_4.gridy = 0;
		gridBagConstraints_4.gridx = 4;
		panel.add(codeCLabel, gridBagConstraints_4);

		codeTextField = new JTextField();
		codeTextField.addKeyListener(new KeyAdapter() {

			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == ' ')// 判断用户输入的是否为空格
					e.consume();// 如果是空格则销毁此次按键事件
			}

			public void keyReleased(KeyEvent e) {// 通过键盘监听器实现智能获取菜品
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {// 按下回车键
					makeOutAnInvoice();// 开单
				} else {
					String input = codeTextField.getText().trim();// 获得输入内容
					Vector vector = null;// 符合条件的菜品集合
					if (input.length() > 0) {// 输入内容不为空
						if (codeRadioButton.isSelected()) {// 按助记码查询
							vector = dao.sMenuByCode(input);// 查询符合条件的菜品
							if (vector.size() > 0)// 存在符合条件的菜品
								vector = (Vector) vector.get(0);// 获得第一个符合条件的菜品
							else
								// 不存在符合条件的菜品
								vector = null;
						} else {// 按编号查询
							vector = dao.sMenuById(input);// 查询符合条件的菜品
							if (vector.size() > 0)// 存在符合条件的菜品
								vector = (Vector) vector.get(0);// 获得第一个符合条件的菜品
							else
								// 不存在符合条件的菜品
								vector = null;
						}
					}
					if (vector == null) {// 不存在符合条件的菜品
						nameTextField.setText(null);// 设置“商品名称”文本框为空
						unitTextField.setText(null);// 设置“单位”文本框为空
					} else {// 存在符合条件的菜品
						nameTextField.setText(vector.get(3).toString());// 设置“商品名称”文本框为符合条件的菜品名称
						unitTextField.setText(vector.get(5).toString());// 设置“单位”文本框为符合条件的菜品单位
					}
				}
			}
		});
		codeTextField.setColumns(10);
		final GridBagConstraints gridBagConstraints_5 = new GridBagConstraints();
		gridBagConstraints_5.gridy = 0;
		gridBagConstraints_5.gridx = 5;
		panel.add(codeTextField, gridBagConstraints_5);

		final JLabel nameLabel = new JLabel();
		nameLabel.setFont(new Font("", Font.BOLD, 12));
		nameLabel.setText("  商品名称：");
		orderDishesPanel.add(nameLabel);

		nameTextField = new JTextField();
		nameTextField.setHorizontalAlignment(SwingConstants.CENTER);
		nameTextField.setEditable(true);
		nameTextField.setFocusable(true);
		nameTextField.setColumns(20);
		orderDishesPanel.add(nameTextField);

		final JLabel unitLabel = new JLabel();
		unitLabel.setFont(new Font("", Font.BOLD, 12));
		unitLabel.setText("  单位：");
		orderDishesPanel.add(unitLabel);

		unitTextField = new JTextField();
		unitTextField.setHorizontalAlignment(SwingConstants.CENTER);
		unitTextField.setEditable(true);
		unitTextField.setFocusable(true);
		unitTextField.setColumns(5);
		orderDishesPanel.add(unitTextField);

		final JLabel amountLabel = new JLabel();
		amountLabel.setFont(new Font("", Font.BOLD, 12));
		amountLabel.setText("  数量：");
		orderDishesPanel.add(amountLabel);

		amountTextField = new JTextField();// 创建“数量”文本框
		amountTextField.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {// 当文本框获得焦点时执行
				amountTextField.setText(null);// 设置“数量”文本框为空
			}

			public void focusLost(FocusEvent e) {// 当文本框失去焦点时执行
				String amount = amountTextField.getText();// 获得输入的数量
				if (amount.length() == 0)// 未输入数量
					amountTextField.setText("1"); // 恢复为默认数量1
			}
		});
		amountTextField.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				int length = amountTextField.getText().length();// 获取当前数量的位数
				if (length < 2) {// 位数小于两位
					String num = (length == 0 ? "123456789" : "0123456789"); // 将允许输入的字符定义成字符串
					if (num.indexOf(e.getKeyChar()) < 0)// 查看按键字符是否包含在允许输入的字符中
						e.consume(); // 如果不包含在允许输入的字符中则销毁此次按键事件
				} else {
					e.consume(); // 如果不小于数量允许的最大位数则销毁此次按键事件
				}
			}
		});
		amountTextField.setText("1");// 默认数量为1
		amountTextField.setHorizontalAlignment(SwingConstants.CENTER);
		amountTextField.setColumns(3);
		orderDishesPanel.add(amountTextField);

		final JLabel emptyLabel = new JLabel();
		emptyLabel.setText(null);
		emptyLabel.setPreferredSize(new Dimension(10, 20));
		orderDishesPanel.add(emptyLabel);

		final JButton addButton = new JButton();
		addButton.setFont(new Font("", Font.BOLD, 12));
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				makeOutAnInvoice();
				codeTextField.requestFocus();
			}
		});
		addButton.setText("点  菜");
		orderDishesPanel.add(addButton);
		
		final JButton delButton = new JButton();
		delButton.setFont(new Font("", Font.BOLD, 12));
		delButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int lSelectedRow = leftTable.getSelectedRow();// 获得“结账列表”中的选中行，即要取消的菜品
				if (lSelectedRow == -1) {// 未选中任何行
					JOptionPane.showMessageDialog(null, "请选择要取消的商品！", "友情提示",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				} else {
					String NEW = leftTable.getValueAt(lSelectedRow, 0)
							.toString();// 获得选中菜品的新点菜标记
					if (NEW.equals("")) {// 没有新点菜标记，不允许取消
						JOptionPane.showMessageDialog(null, "很抱歉，该商品已经不能取消！",
								"友情提示", JOptionPane.INFORMATION_MESSAGE);
						return;
					} else {
						int rSelectedRow = rightTable.getSelectedRow();// 获得“桌号列表”中的选中行，即取消菜品的台号
						int i = JOptionPane.showConfirmDialog(null, "确定要取消“"
								+ rightTable.getValueAt(rSelectedRow, 1)
								+ "”中的商品“"
								+ leftTable.getValueAt(lSelectedRow, 3) + "”？",
								"友情提示", JOptionPane.YES_NO_OPTION);// 弹出提示信息确认是否取消
						if (i == 0) {// 确认取消
							leftTableModel.removeRow(lSelectedRow);// 从“签单列表”中取消菜品
							int rowCount = leftTable.getRowCount();// 获得取消后的点菜数量
							if (rowCount == 0) {// 未点任何菜品
								rightTableModel.removeRow(rSelectedRow);// 取消开桌
								menuOfDeskV.remove(rSelectedRow);// 移除签单列表
							} else {
								if (lSelectedRow == rowCount) {// 取消菜品为最后一个
									lSelectedRow -= 1;// 设置最后一个菜品为选中的
								} else {// 取消菜品不是最后一个
									Vector<Vector<Object>> menus = menuOfDeskV
											.get(rSelectedRow);
									for (int row = lSelectedRow; row < rowCount; row++) {// 修改点菜序号
										leftTable.setValueAt(row + 1, row, 1);
										menus.get(row).set(1, row + 1);
									}
								}
								leftTable.setRowSelectionInterval(lSelectedRow);// 设置选中行
							}
						}
					}
				}
			}
		});
		delButton.setText("取消点菜");
		orderDishesPanel.add(delButton);

		final JButton subButton = new JButton();
		subButton.setFont(new Font("", Font.BOLD, 12));
		subButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = rightTable.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(null, "请选择要结账的桌号！", "友情提示",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				} else {
					int row = leftTable.getRowCount() - 1;
					String NEW = leftTable.getValueAt(row, 0).toString();
					if (NEW.equals("待确定")) {
						leftTableValueV.removeAllElements();
						leftTableValueV.addAll(menuOfDeskV.get(selectedRow));
						for (; row >= 0; row--) {
							leftTableValueV.get(row).set(0, "");
						}
						leftTableModel.setDataVector(leftTableValueV,
								leftTableColumnV);
						leftTable.setRowSelectionInterval(0, 0);
					}
				}
			}
		});
		subButton.setText("打印账单");
		orderDishesPanel.add(subButton);



		final JPanel clueOnPanel = new JPanel();
		clueOnPanel.setPreferredSize(new Dimension(220, 0));
		clueOnPanel.setBorder(new TitledBorder(null, "",
				TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, null, null));
		clueOnPanel.setLayout(new GridLayout(0, 1));
		bottomPanel.add(clueOnPanel, BorderLayout.WEST);

		final JLabel aClueOnLabel = new JLabel();
		clueOnPanel.add(aClueOnLabel);
		aClueOnLabel.setFont(new Font("", Font.BOLD, 12));
		aClueOnLabel.setText("  今天是：");

		final JLabel bClueOnLabel = new JLabel();
		bClueOnLabel.setFont(new Font("", Font.BOLD, 12));
		clueOnPanel.add(bClueOnLabel);
		bClueOnLabel.setHorizontalAlignment(SwingConstants.CENTER);
		bClueOnLabel.setText(Today.getDateOfShow());

		final JLabel cClueOnLabel = new JLabel();
		cClueOnLabel.setFont(new Font("", Font.BOLD, 12));
		clueOnPanel.add(cClueOnLabel);
		cClueOnLabel.setHorizontalAlignment(SwingConstants.CENTER);
		cClueOnLabel.setText(Today.getDayOfWeek());

		timeLabel = new JLabel();// 创建用于显示时间的标签对象
		timeLabel.setFont(new Font("宋体", Font.BOLD, 14));// 设置标签中的文字为宋体、粗体、14号
		timeLabel.setForeground(new Color(255, 0, 0));// 设置标签中的文字为红色
		timeLabel.setHorizontalAlignment(SwingConstants.CENTER);// 设置标签中的文字居中显示
		clueOnPanel.add(timeLabel);// 将标签添加到上级容器中
		new Time().start();// 开启线程

		final JLabel eClueOnLabel = new JLabel();
		clueOnPanel.add(eClueOnLabel);
		eClueOnLabel.setFont(new Font("", Font.BOLD, 12));
		eClueOnLabel.setText("  当前操作员：");

		final JLabel fClueOnLabel = new JLabel();
		fClueOnLabel.setFont(new Font("", Font.BOLD, 12));
		clueOnPanel.add(fClueOnLabel);
		fClueOnLabel.setHorizontalAlignment(SwingConstants.CENTER);
		if (user == null)
			fClueOnLabel.setText("系统默认用户");
		else
			fClueOnLabel.setText(user.get(1).toString());

		final JPanel checkOutPanel = new JPanel();
		checkOutPanel.setPreferredSize(new Dimension(500, 0));
		bottomPanel.add(checkOutPanel);
		checkOutPanel.setBorder(new TitledBorder(null, "",
				TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, null, null));
		checkOutPanel.setLayout(new GridBagLayout());

		final JLabel label = new JLabel();
		label.setPreferredSize(new Dimension(72, 70));
		URL rmbUrl = this.getClass().getResource("/img/rmb.jpg");
		ImageIcon rmbIcon = new ImageIcon(rmbUrl);
		label.setIcon(rmbIcon);
		final GridBagConstraints gridBagConstraints_9 = new GridBagConstraints();
		gridBagConstraints_9.insets = new Insets(0, 0, 0, 15);
		gridBagConstraints_9.gridheight = 4;
		gridBagConstraints_9.gridy = 0;
		gridBagConstraints_9.gridx = 0;
		checkOutPanel.add(label, gridBagConstraints_9);

		final JLabel expenditureLabel = new JLabel();
		expenditureLabel.setFont(new Font("", Font.BOLD, 16));
		expenditureLabel.setText("消费金额：");
		final GridBagConstraints gridBagConstraints_13 = new GridBagConstraints();
		gridBagConstraints_13.gridx = 1;
		gridBagConstraints_13.gridy = 0;
		gridBagConstraints_13.insets = new Insets(0, 10, 0, 0);
		checkOutPanel.add(expenditureLabel, gridBagConstraints_13);

		expenditureTextField = new JTextField();
		expenditureTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		expenditureTextField.setText("0.00");
		expenditureTextField.setForeground(new Color(255, 0, 0));
		expenditureTextField.setFont(new Font("", Font.BOLD, 15));
		expenditureTextField.setColumns(7);
		expenditureTextField.setEditable(false);
		final GridBagConstraints gridBagConstraints_6 = new GridBagConstraints();
		gridBagConstraints_6.gridy = 0;
		gridBagConstraints_6.gridx = 2;
		checkOutPanel.add(expenditureTextField, gridBagConstraints_6);

		final JLabel expenditureUnitLabel = new JLabel();
		expenditureUnitLabel.setForeground(new Color(255, 0, 0));
		expenditureUnitLabel.setFont(new Font("", Font.BOLD, 15));
		expenditureUnitLabel.setText(" 元");
		final GridBagConstraints gridBagConstraints_14 = new GridBagConstraints();
		gridBagConstraints_14.gridy = 0;
		gridBagConstraints_14.gridx = 3;
		checkOutPanel.add(expenditureUnitLabel, gridBagConstraints_14);

		final JLabel realWagesLabel = new JLabel();
		realWagesLabel.setFont(new Font("", Font.BOLD, 16));
		realWagesLabel.setText("实收金额：");
		final GridBagConstraints gridBagConstraints_7 = new GridBagConstraints();
		gridBagConstraints_7.insets = new Insets(10, 10, 0, 0);
		gridBagConstraints_7.gridy = 1;
		gridBagConstraints_7.gridx = 1;
		checkOutPanel.add(realWagesLabel, gridBagConstraints_7);

		realWagesTextField = new JTextField();
		realWagesTextField.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				// TODO 自动生成方法存根

			}

			public void keyReleased(KeyEvent e) {
				// TODO 自动生成方法存根

			}

			public void keyTyped(KeyEvent e) {
				int length = realWagesTextField.getText().length();// 获取当前数量的位数
				if (length < 8) {// 位数小于两位
					String num = (length == 4 ? "123456789" : "0123456789"); // 将允许输入的字符定义成字符串
					if (num.indexOf(e.getKeyChar()) < 0)// 查看按键字符是否包含在允许输入的字符中
						e.consume(); // 如果不包含在允许输入的字符中则销毁此次按键事件
				} else {
					e.consume(); // 如果不小于数量允许的最大位数则销毁此次按键事件
				}
			}
		});
		realWagesTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		realWagesTextField.setText("0.00");
		realWagesTextField.setForeground(new Color(0, 128, 0));
		realWagesTextField.setFont(new Font("", Font.BOLD, 15));
		realWagesTextField.setColumns(7);
		final GridBagConstraints gridBagConstraints_8 = new GridBagConstraints();
		gridBagConstraints_8.insets = new Insets(10, 0, 0, 0);
		gridBagConstraints_8.gridy = 1;
		gridBagConstraints_8.gridx = 2;
		checkOutPanel.add(realWagesTextField, gridBagConstraints_8);

		final JLabel realWagesUnitLabel = new JLabel();
		realWagesUnitLabel.setForeground(new Color(0, 128, 0));
		realWagesUnitLabel.setFont(new Font("", Font.BOLD, 15));
		realWagesUnitLabel.setText(" 元");
		final GridBagConstraints gridBagConstraints_15 = new GridBagConstraints();
		gridBagConstraints_15.insets = new Insets(10, 0, 0, 0);
		gridBagConstraints_15.gridy = 1;
		gridBagConstraints_15.gridx = 3;
		checkOutPanel.add(realWagesUnitLabel, gridBagConstraints_15);

		final JButton checkOutButton = new JButton();
		checkOutButton.setFont(new Font("", Font.BOLD, 12));
		checkOutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = rightTable.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(null, "请选择要结账的台号！", "友情提示",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					int rowCount = leftTable.getRowCount();// 获得结账餐台的点菜数量
					String NEW = leftTable.getValueAt(rowCount - 1, 0)
							.toString();// 获得最后点菜的标记
					if (NEW.equals("待确定")) {// 如果最后点菜被标记为“待确定”,则弹出提示
						JOptionPane.showMessageDialog(null, "请先确定未签单商品的处理方式！",
								"友情提示", JOptionPane.INFORMATION_MESSAGE);
					} else {
						float expenditure = Float.valueOf(expenditureTextField
								.getText());// 获得消费金额
						float realWages = Float.valueOf(realWagesTextField
								.getText());// 获得实收金额
						if (realWages < expenditure) {// 如果实收金额小于消费金额，则弹出提示
							if (realWages == 0.0f)
								JOptionPane
										.showMessageDialog(null, "请输入实收金额！",
												"友情提示",
												JOptionPane.INFORMATION_MESSAGE);
							else
								JOptionPane.showMessageDialog(null,
										"实收金额不能小于消费金额！", "友情提示",
										JOptionPane.INFORMATION_MESSAGE);
							realWagesTextField.requestFocus();// 并令“实收金额”文本框获得焦点
						} else {
							changeTextField.setText(realWages - expenditure
									+ "0");// 计算并设置“找零金额”
							String[] values = {
									getNum(),
									rightTable.getValueAt(selectedRow, 1)
											.toString(),
									Today.getDate()
											+ " "
											+ rightTable.getValueAt(
													selectedRow, 2),
									expenditureTextField.getText(),
									user.get(0).toString() };// 组织消费单信息
							dao.iOrderForm(values);// 持久化到数据库
							values[0] = dao.sOrderFormOfMaxId();// 获得消费单编号
							for (int i = 0; i < rowCount; i++) {// 通过循环获得各个消费项目的信息
								values[1] = leftTable.getValueAt(i, 2)
										.toString();// 获得商品编号
								values[2] = leftTable.getValueAt(i, 5)
										.toString();// 获得商品数量
								values[3] = leftTable.getValueAt(i, 7)
										.toString();// 获得商品消费金额
								dao.iOrderItem(values);// 持久化到数据库
							}
							JOptionPane.showMessageDialog(null, rightTable
									.getValueAt(selectedRow, 1)
									+ " 结账完成！", "友情提示",
									JOptionPane.INFORMATION_MESSAGE);// 弹出结账完成提示
							rightTableModel.removeRow(selectedRow);// 从“开台列表”中取消开台
							leftTableValueV.removeAllElements();// 清空“签单列表”
							leftTableModel.setDataVector(leftTableValueV,
									leftTableColumnV);// 刷新“签单列表”
							realWagesTextField.setText("0.00");// 清空“实收金额”文本框
							changeTextField.setText("0.00");// 清空“找零金额”文本框
							menuOfDeskV.remove(selectedRow);// 从“签单列表”集合中移除已结账的签单列表
						}
					}
				}
			}
		});
		checkOutButton.setMargin(new Insets(2, 14, 2, 14));
		checkOutButton.setText("结 账");
		final GridBagConstraints gridBagConstraints_10 = new GridBagConstraints();
		gridBagConstraints_10.anchor = GridBagConstraints.EAST;
		gridBagConstraints_10.gridwidth = 2;
		gridBagConstraints_10.insets = new Insets(10, 0, 0, 0);
		gridBagConstraints_10.gridy = 2;
		gridBagConstraints_10.gridx = 2;
		checkOutPanel.add(checkOutButton, gridBagConstraints_10);

		final JLabel changeLabel = new JLabel();
		changeLabel.setFont(new Font("", Font.BOLD, 16));
		changeLabel.setText("找零金额：");
		final GridBagConstraints gridBagConstraints_11 = new GridBagConstraints();
		gridBagConstraints_11.insets = new Insets(10, 10, 0, 0);
		gridBagConstraints_11.gridy = 3;
		gridBagConstraints_11.gridx = 1;
		checkOutPanel.add(changeLabel, gridBagConstraints_11);

		changeTextField = new JTextField();
		changeTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		changeTextField.setText("0.00");
		changeTextField.setForeground(new Color(255, 0, 255));
		changeTextField.setFont(new Font("", Font.BOLD, 15));
		changeTextField.setEditable(false);
		changeTextField.setColumns(7);
		final GridBagConstraints gridBagConstraints_12 = new GridBagConstraints();
		gridBagConstraints_12.insets = new Insets(10, 0, 0, 0);
		gridBagConstraints_12.gridy = 3;
		gridBagConstraints_12.gridx = 2;
		checkOutPanel.add(changeTextField, gridBagConstraints_12);

		final JLabel changeUnitLabel = new JLabel();
		changeUnitLabel.setForeground(new Color(255, 0, 255));
		changeUnitLabel.setFont(new Font("", Font.BOLD, 15));
		changeUnitLabel.setText(" 元");
		final GridBagConstraints gridBagConstraints_16 = new GridBagConstraints();
		gridBagConstraints_16.insets = new Insets(10, 0, 0, 0);
		gridBagConstraints_16.gridy = 3;
		gridBagConstraints_16.gridx = 3;
		checkOutPanel.add(changeUnitLabel, gridBagConstraints_16);

		final JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 0));
		bottomPanel.add(buttonPanel, BorderLayout.EAST);

		final JPanel aButtonPanel = new JPanel();
		aButtonPanel.setBorder(new TitledBorder(null, "",
				TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, null, null));
		aButtonPanel.setLayout(new GridLayout(0, 1));
		buttonPanel.add(aButtonPanel);

		final JButton aTopButton = new MButton();
		aTopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MenuDialog menuDialog = new MenuDialog();
				menuDialog.setLocation((screenSize.width - menuDialog
						.getWidth()) / 2, (screenSize.height - menuDialog
						.getHeight()) / 2);
				menuDialog.setVisible(true);
			}
		});
		URL menuUrl = this.getClass().getResource("/img/menu.jpg");
		ImageIcon menuIcon = new ImageIcon(menuUrl);
		aTopButton.setIcon(menuIcon);
		aButtonPanel.add(aTopButton);

		final JButton aCenterButton = new MButton();
		aCenterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SortDialog sortDialog = new SortDialog();
				sortDialog.setLocation((screenSize.width - sortDialog
						.getWidth()) / 2, (screenSize.height - sortDialog
						.getHeight()) / 2);
				sortDialog.setVisible(true);
			}
		});
		URL sortUrl = this.getClass().getResource("/img/sort.jpg");
		ImageIcon sortIcon = new ImageIcon(sortUrl);
		aCenterButton.setIcon(sortIcon);
		aButtonPanel.add(aCenterButton);

		final JButton aBottomButton = new MButton();
		aBottomButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DeskNumDialog deskNumDialog = new DeskNumDialog(rightTable);
				deskNumDialog.setLocation((screenSize.width - deskNumDialog
						.getWidth()) / 2, (screenSize.height - deskNumDialog
						.getHeight()) / 2);
				deskNumDialog.setVisible(true);
				initNumComboBox();
			}
		});
		URL deskUrl = this.getClass().getResource("/img/desk.jpg");
		ImageIcon deskIcon = new ImageIcon(deskUrl);
		aBottomButton.setIcon(deskIcon);
		aButtonPanel.add(aBottomButton);

		final JPanel dButtonPanel = new JPanel();
		dButtonPanel.setPreferredSize(new Dimension(0, 0));
		dButtonPanel.setBorder(new TitledBorder(null, "",
				TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, null, null));
		dButtonPanel.setLayout(new GridLayout(0, 1));
		buttonPanel.add(dButtonPanel);

		final JButton dTopButton = new MButton();
		dTopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DayDialog dayDialog = new DayDialog();
				dayDialog.setVisible(true);
			}
		});
		URL dayUrl = this.getClass().getResource("/img/day.png");
		ImageIcon dayIcon = new ImageIcon(dayUrl);
		dTopButton.setIcon(dayIcon);
		dButtonPanel.add(dTopButton);

		final JButton dCenterButton = new MButton();
		dCenterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MonthDialog monthDialog = new MonthDialog();
				monthDialog.setVisible(true);
			}
		});
		URL monthUrl = this.getClass().getResource("/img/month.png");
		ImageIcon monthIcon = new ImageIcon(monthUrl);
		dCenterButton.setIcon(monthIcon);
		dButtonPanel.add(dCenterButton);

		final JButton dBottomButton = new MButton();
		dBottomButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				YearDialog yearDialog = new YearDialog();
				yearDialog.setVisible(true);
			}
		});
		URL yearUrl = this.getClass().getResource("/img/year.png");
		ImageIcon yearIcon = new ImageIcon(yearUrl);
		dBottomButton.setIcon(yearIcon);

		dButtonPanel.add(dBottomButton);
		final JPanel cButtonPanel = new JPanel();
		cButtonPanel.setBorder(new TitledBorder(null, "",
				TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, null, null));
		cButtonPanel.setLayout(new GridLayout(0, 1));
		buttonPanel.add(cButtonPanel);

		final JButton cTopButton = new MButton();
		cTopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UpdatePasswordDialog upDialog = new UpdatePasswordDialog();
				upDialog.setLocation(
						(screenSize.width - upDialog.getWidth()) / 2,
						(screenSize.height - upDialog.getHeight()) / 2);
				upDialog.setUser(user);
				upDialog.setVisible(true);
			}
		});
		URL passwordUrl = this.getClass().getResource("/img/password.jpg");
		ImageIcon passwordIcon = new ImageIcon(passwordUrl);
		cTopButton.setIcon(passwordIcon);
		cButtonPanel.add(cTopButton);

		final JButton cCenterButton = new MButton();
		cCenterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserManagerDialog umDialog = new UserManagerDialog();
				umDialog.setLocation(
						(screenSize.width - umDialog.getWidth()) / 2,
						(screenSize.height - umDialog.getHeight()) / 2);
				umDialog.setVisible(true);
			}
		});
		URL userUrl = this.getClass().getResource("/img/user.jpg");
		ImageIcon userIcon = new ImageIcon(userUrl);
		cCenterButton.setIcon(userIcon);
		cButtonPanel.add(cCenterButton);

		final JButton cBottomButton = new MButton();
		cBottomButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		URL exitUrl = this.getClass().getResource("/img/exit.jpg");
		ImageIcon exitIcon = new ImageIcon(exitUrl);
		cBottomButton.setIcon(exitIcon);
		cButtonPanel.add(cBottomButton);

		if (user == null) {
			numComboBox.setEnabled(false);
			numRadioButton.setEnabled(false);
			codeRadioButton.setEnabled(false);
			codeTextField.setEnabled(false);
			amountTextField.setEnabled(false);
			addButton.setEnabled(false);
			subButton.setEnabled(false);
			delButton.setEnabled(false);
			realWagesTextField.setEnabled(false);
			checkOutButton.setEnabled(false);
			aTopButton.setEnabled(false);
			aCenterButton.setEnabled(false);
			aBottomButton.setEnabled(false);
			cTopButton.setEnabled(false);
			dTopButton.setEnabled(false);
			dCenterButton.setEnabled(false);
			dBottomButton.setEnabled(false);
		}
	}

	private void makeOutAnInvoice() {
		String deskNum = numComboBox.getSelectedItem().toString();// 获得台号
		String menuName = nameTextField.getText();// 获得商品名称
		String menuAmount = amountTextField.getText();// 获得数量
		// 验证
		if (deskNum.equals("请选择")) {// 验证是否已经选择桌号
			JOptionPane.showMessageDialog(null, "请选择桌号！", "友情提示",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		if (menuName.length() == 0) {// 验证是否已经确定商品
			JOptionPane.showMessageDialog(null, "请录入商品名称！", "友情提示",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		if (!Validate.execute("[1-9]{1}([0-9]{0,1})", menuAmount)) {// 验证数量是否有效，数量必须在1-99之间
			String info[] = new String[] { "您输入的数量错误！", "数量必须在1-99之间！" };
			JOptionPane.showMessageDialog(null, info, "友情提示",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		// 处理开台信息
		int rightSelectedRow = rightTable.getSelectedRow();// 获得被选中的桌号
		int leftRowCount = 0;// 默认点菜数量为0
		if (rightSelectedRow == -1) {// 没有被选中的台号，即新开台
			rightSelectedRow = rightTable.getRowCount();// 被选中的台号为新开的台
			Vector deskV = new Vector();// 创建一个代表新开台的向量对象
			deskV.add(rightSelectedRow + 1);// 添加开台序号
			deskV.add(deskNum);// 添加开台号
			deskV.add(Today.getTime());// 添加开台时间
			rightTableModel.addRow(deskV);// 将开台信息添加到“开台列表”中
			rightTable.setRowSelectionInterval(rightSelectedRow);// 选中新开的台
			menuOfDeskV.add(new Vector());// 添加一个对应的签单列表
		} else { // 选中的台号已经开台，即添加菜品
			leftRowCount = leftTable.getRowCount();// 获得已点菜的数量
		}
		// 处理点菜信息
		Vector vector = dao.sMenuByNameAndState(menuName, "销售");// 获得被点菜品
		int amount = Integer.valueOf(menuAmount);// 将菜品数量转为int型
		int unitPrice = Integer.valueOf(vector.get(5).toString()); // 将菜品单价转为int型
		int money = unitPrice * amount;// 计算菜品消费额
		Vector<Object> menuV = new Vector<Object>();
		menuV.add("待确定");// 添加新点菜标记
		menuV.add(leftRowCount + 1);// 添加点菜序号
		menuV.add(vector.get(0));// 添加菜品编号
		menuV.add(menuName);// 添加菜品名称
		menuV.add(vector.get(4));// 添加菜品单位
		menuV.add(amount);// 添加菜品数量
		menuV.add(unitPrice);// 添加菜品单价
		menuV.add(money);// 添加菜品消费额
		leftTableModel.addRow(menuV);// 将点菜信息添加到“签单列表”中
		leftTable.setRowSelectionInterval(leftRowCount);// 将新点菜设置为选中行
		menuOfDeskV.get(rightSelectedRow).add(menuV);// 将新点菜信息添加到对应的签单列表
		//
		codeTextField.setText(null);
		nameTextField.setText(null);
		unitTextField.setText(null);
		amountTextField.setText("1");
	}

	private String getNum() {
		String maxNum = dao.sOrderFormOfMaxId();
		String date = Today.getDateOfNum();
		if (maxNum == null) {
			maxNum = date + "001";
		} else {
			if (maxNum.subSequence(0, 8).equals(date)) {
				maxNum = maxNum.substring(8);
				int nextNum = Integer.valueOf(maxNum) + 1;
				if (nextNum < 10)
					maxNum = date + "00" + nextNum;
				else if (nextNum < 100)
					maxNum = date + "0" + nextNum;
				else
					maxNum = date + nextNum;
			} else {
				maxNum = date + "001";
			}
		}
		return maxNum;
	}

	private void initNumComboBox() {
		numComboBox.removeAllItems();
		numComboBox.addItem("请选择");
		Vector allSortV = dao.sDesk();
		for (int i = 0; i < allSortV.size(); i++) {
			Vector sortV = (Vector) allSortV.get(i);
			numComboBox.addItem(sortV.get(1));
		}
	}

	private void a(JLabel dClueOnLabel) {
		Calendar now;
		int hour;
		int minute;
		int second;
		while (true) {
			now = Calendar.getInstance();
			hour = now.get(Calendar.HOUR_OF_DAY);
			minute = now.get(Calendar.MINUTE);
			second = now.get(Calendar.SECOND);
			dClueOnLabel.setText(hour + ":" + minute + ":" + second);
		}
	}

	class Time extends Thread {// 创建内部类
		public void run() {// 重构父类的方法
			while (true) {
				Date date = new Date();// 创建日期对象
				timeLabel.setText(date.toString().substring(11, 19));// 获取当前时间，并显示到时间标签中
				try {
					Thread.sleep(1000);// 令线程休眠1秒
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}