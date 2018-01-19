package com.mwq.frame.manage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

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

public class SortDialog extends JDialog {

	private JTable table;

	private JTextField sortNameTextField;

	private final Vector columnNameV = new Vector();

	private final DefaultTableModel tableModel = new DefaultTableModel();

	private final Dao dao = Dao.getInstance();

	/**
	 * Launch the application
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			SortDialog dialog = new SortDialog();
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
	public SortDialog() {
		super();
		setModal(true);
		getContentPane().setLayout(new BorderLayout());
		setResizable(false);
		setTitle("菜系管理");
		setBounds(100, 100, 500, 375);

		final JPanel operatePanel = new JPanel();
		getContentPane().add(operatePanel, BorderLayout.NORTH);

		final JLabel sortNameLabel = new JLabel();
		operatePanel.add(sortNameLabel);
		sortNameLabel.setText("菜系名称：");

		sortNameTextField = new JTextField();
		operatePanel.add(sortNameTextField);
		sortNameTextField.setColumns(20);

		final JLabel topPlaceholderLabel = new JLabel();
		topPlaceholderLabel.setPreferredSize(new Dimension(20, 40));
		operatePanel.add(topPlaceholderLabel);

		final JButton addButton = new JButton();// 创建添加菜系名称按钮对象
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sortName = sortNameTextField.getText().trim(); // 获得菜系名称，并去掉首尾空格
				if (sortName.equals("")) {// 查看是否输入了菜系名称
					JOptionPane.showMessageDialog(null, "请输入菜系名称！", "友情提示",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if (sortName.length() > 10) {// 查看菜系名称的长度是否超过了10个汉字
					JOptionPane.showMessageDialog(null, "菜系名称最多只能为10个汉字！",
							"友情提示", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if (dao.sSortByName(sortName).size() > 0) {// 查看该菜系名称是否已经存在
					JOptionPane.showMessageDialog(null, "该菜系已经存在！", "友情提示",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				int row = tableModel.getRowCount();// 获得当前拥有菜系名称的个数
				Vector newSortV = new Vector();// 创建一个代表新菜系名称的向量
				newSortV.add(new Integer(row + 1));// 添加序号
				newSortV.add(sortName);// 添加菜系名称
				tableModel.addRow(newSortV);// 将新菜系名称信息添加到表格中
				table.setRowSelectionInterval(row, row);// 设置新添加的菜系名称为选中的
				sortNameTextField.setText(null);// 将菜系名称文本框设置为空
				//
				dao.iSort(sortName);// 将新添加的菜系名称信息保存到数据库中
				JDBC.closeConnection();// 关闭数据库连接
			}
		});
		addButton.setText("添加");
		operatePanel.add(addButton);

		final JButton delButton = new JButton();// 创建删除菜系名称按钮对象
		delButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();// 获得选中的菜系
				String delSortName = (String) table.getValueAt(row, 1);// 获得选中的菜系名称
				int j = JOptionPane
						.showConfirmDialog(null, "确定要删除菜系“" + delSortName
								+ "”？", "友情提示", JOptionPane.YES_NO_OPTION);// 弹出确认提示
				if (j == 0) {// 确认删除
					tableModel.removeRow(row);// 从表格中移除菜系信息
					int rowCount = table.getRowCount();// 获得删除后拥有的菜系数
					if (rowCount > 0) {// 还拥有菜系
						if (row < table.getRowCount()) {// 删除的不是位于表格最后的菜系
							for (int i = row; i < table.getRowCount(); i++) {
								table.setValueAt(i + 1 + "", i, 0);// 修改位于删除菜系之后的序号
							}
							table.setRowSelectionInterval(row, row);// 设置上移到删除行索引的菜系为被选中
						} else {// 删除的是位于表格最后的菜系
							table.setRowSelectionInterval(row - 1, row - 1);// 设置当前位于表格最后的菜系被选中
						}
					}
					//
					dao.dSortByName(delSortName);// 从数据库中删除菜系
					JDBC.closeConnection();// 关闭数据库连接
				}
			}
		});
		delButton.setText("删除");
		operatePanel.add(delButton);

		final JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane);

		columnNameV.add("序    号");
		columnNameV.add("菜系名称");
		tableModel.setDataVector(dao.sSortName(), columnNameV);
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
