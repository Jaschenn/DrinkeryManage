package com.mwq.frame.manage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.mwq.dao.Dao;
import com.mwq.dao.JDBC;
import com.mwq.mwing.MTable;
import com.mwq.tool.Validate;

public class DeskNumDialog extends JDialog {

	private JTable table;

	private JTextField seatingTextField;

	private JTextField numTextField;

	private final Vector columnNameV = new Vector();

	private final DefaultTableModel tableModel = new DefaultTableModel();

	private final Dao dao = Dao.getInstance();

	private JTable openedDeskTable;

	/**
	 * Launch the application
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			DeskNumDialog dialog = new DeskNumDialog(null);
			dialog.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog
	 */
	public DeskNumDialog(JTable rightTable) {
		super();
		setModal(true);
		getContentPane().setLayout(new BorderLayout());
		setResizable(false);
		setTitle("台号管理");
		setBounds(100, 100, 500, 375);

		this.openedDeskTable = rightTable;

		final JPanel operatePanel = new JPanel();
		getContentPane().add(operatePanel, BorderLayout.NORTH);

		final JLabel numLabel = new JLabel();
		operatePanel.add(numLabel);
		numLabel.setText("台  号：");

		numTextField = new JTextField();
		numTextField.setColumns(6);
		operatePanel.add(numTextField);

		final JLabel seatingLabel = new JLabel();
		operatePanel.add(seatingLabel);
		seatingLabel.setText("  座位数：");

		seatingTextField = new JTextField();
		seatingTextField.setColumns(5);
		operatePanel.add(seatingTextField);

		final JLabel topPlaceholderLabel = new JLabel();
		topPlaceholderLabel.setPreferredSize(new Dimension(20, 40));
		operatePanel.add(topPlaceholderLabel);

		final JButton addButton = new JButton();//创建添加台号按钮对象
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String num = numTextField.getText().trim();// 获取台号，并去掉首尾空格
				String seating = seatingTextField.getText().trim();// 获取座位数，并去掉首尾空格
				if (num.equals("") || seating.equals("")) {// 查看用户是否输入了台号和座位数
					JOptionPane.showMessageDialog(null, "请输入台号和座位数！", "友情提示",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if (num.length() > 5) {// 查看台号的长度是否超过了5位
					JOptionPane.showMessageDialog(null, "台号最多只能为5个字符！", "友情提示",
							JOptionPane.INFORMATION_MESSAGE);
					numTextField.requestFocus();// 为台号文本框请求获得焦点
					return;
				}
				if (!Validate.execute("[1-9]{1}([0-9]{0,1})", seating)) {// 验证座位数是否在1——19之间
					String[] infos = { "座位数输入错误！", "座位数必须在 1——99 之间！" };
					JOptionPane.showMessageDialog(null, infos, "友情提示",
							JOptionPane.INFORMATION_MESSAGE);
					seatingTextField.requestFocus();// 为座位数文本框请求获得焦点
					return;
				}
				if (dao.sDeskByNum(num) != null) {// 查看该台号是否已经存在
					JOptionPane.showMessageDialog(null, "该台号已经存在！", "友情提示",
							JOptionPane.INFORMATION_MESSAGE);
					numTextField.requestFocus();// 为台号文本框请求获得焦点
					return;
				}
				int row = table.getRowCount();// 获得当前拥有台号的个数
				Vector newDeskNumV = new Vector();// 创建一个代表新台号的向量
				newDeskNumV.add(new Integer(row + 1));// 添加添加序号
				newDeskNumV.add(num);// 添加台号
				newDeskNumV.add(seating);// 添加座位数
				tableModel.addRow(newDeskNumV);// 将新台号信息添加到表格中
				table.setRowSelectionInterval(row, row);// 设置新添加的台号为选中的
				numTextField.setText(null);// 将台号文本框设置为空
				seatingTextField.setText(null);// 将座位数文本框设置为空
				//
				dao.iDesk(num, seating);// 将新添加的台号信息保存到数据库中
				JDBC.closeConnection();// 关闭数据库连接
			}
		});
		addButton.setText("添加");
		operatePanel.add(addButton);

		final JButton delButton = new JButton();//创建删除台号按钮对象
		delButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();// 获得选中的餐台
				if (selectedRow == -1) {// 未选中任何餐台
					JOptionPane.showMessageDialog(null, "请选择要删除的餐台！", "友情提示",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					String deskNum = table.getValueAt(selectedRow, 1)
							.toString();// 获得选中餐台的编号
					for (int row = 0; row < openedDeskTable.getRowCount(); row++) {// 查看该餐台是否正在被使用
						if (deskNum.equals(openedDeskTable.getValueAt(row, 1))) {
							JOptionPane.showMessageDialog(null,
									"该餐台正在使用，不能删除！", "友情提示",
									JOptionPane.INFORMATION_MESSAGE);
							return;// 该餐台正在被使用，不能删除，返回
						}
					}
					String infos[] = new String[] {// 组织确认信息
							"确定要删除餐台：",
							"    台  号：" + deskNum,
							"    座位数："
									+ table.getValueAt(selectedRow, 2)
											.toString() };
					int i = JOptionPane.showConfirmDialog(null, infos, "友情提示",
							JOptionPane.YES_NO_OPTION);// 弹出确认提示
					if (i == 0) {// 确认删除
						dao.dDeskByNum(deskNum);// 从数据库中删除
						tableModel.setDataVector(dao.sDesk(), columnNameV);// 刷新表格
						int rowCount = table.getRowCount();// 获得删除后拥有的餐台数
						if (rowCount > 0) {// 还拥有餐台
							if (selectedRow == rowCount)// 删除的为最后一个餐台
								selectedRow -= 1;// 将选中的餐台前移一行
							table.setRowSelectionInterval(selectedRow,
									selectedRow);// 设置当前选中的餐台
						}
						JDBC.closeConnection();// 关闭数据库连接
					}
				}
			}
		});
		delButton.setText("删除");
		operatePanel.add(delButton);

		final JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane);

		String columnNames[] = new String[] { "序  号", "台  号", "座位数" };
		for (int i = 0; i < columnNames.length; i++) {
			columnNameV.add(columnNames[i]);
		}

		tableModel.setDataVector(dao.sDesk(), columnNameV);
		JDBC.closeConnection();

		table = new MTable(tableModel);
		if (table.getRowCount() > 0)
			table.setRowSelectionInterval(0, 0);
		scrollPane.setViewportView(table);

		final JLabel leftPlaceholderLabel = new JLabel();
		leftPlaceholderLabel.setPreferredSize(new Dimension(20, 20));
		getContentPane().add(leftPlaceholderLabel, BorderLayout.WEST);

		final JPanel exitPanel = new JPanel();
		final FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		exitPanel.setLayout(flowLayout);
		getContentPane().add(exitPanel, BorderLayout.SOUTH);

		final JButton exitButton = new JButton();
		exitPanel.add(exitButton);
		exitButton.setText("退出");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		final JLabel bottomPlaceholderLabel = new JLabel();
		bottomPlaceholderLabel.setPreferredSize(new Dimension(10, 40));
		exitPanel.add(bottomPlaceholderLabel);

		final JLabel rightPlaceholderLabel = new JLabel();
		rightPlaceholderLabel.setPreferredSize(new Dimension(20, 20));
		getContentPane().add(rightPlaceholderLabel, BorderLayout.EAST);
		//
	}
}
