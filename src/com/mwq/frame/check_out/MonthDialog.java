package com.mwq.frame.check_out;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.mwq.dao.Dao;
import com.mwq.mwing.MTable;
import com.mwq.tool.Today;

public class MonthDialog extends JDialog {

	private JTable table;

	private Vector<String> tableColumnV;

	private Vector tableValueV;

	private DefaultTableModel tableModel;

	private JComboBox monthComboBox;

	private JComboBox yearComboBox;

	private Dao dao=Dao.getInstance();

	private int daysOfMonth[] = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31,
			30, 31 };

	/**
	 * Launch the application
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			MonthDialog dialog = new MonthDialog();
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
	public MonthDialog() {
		super();
		setModal(true);
		setTitle("月结账");
		setBounds(60, 60, 860, 620);

		final JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);

		int year = Today.getYEAR();
		int month = Today.getMONTH();

		yearComboBox = new JComboBox();
		yearComboBox.setMaximumRowCount(10);
		String minDatetime = dao.sOrderFormOfMinDatetime();
		if (minDatetime == null) {
			yearComboBox.addItem(year);
		} else {
			int minYear = Integer.valueOf(minDatetime.substring(0, 4));
			for (int y = minYear; y <= year; y++) {
				yearComboBox.addItem(y);
			}
		}
		yearComboBox.setSelectedItem(year);
		judgeLeapYear(year);
		yearComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int year = (Integer) yearComboBox.getSelectedItem();
				judgeLeapYear(year);
			}
		});
		panel.add(yearComboBox);

		final JLabel yearLabel = new JLabel();
		yearLabel.setText("年");
		panel.add(yearLabel);

		monthComboBox = new JComboBox();
		monthComboBox.setMaximumRowCount(12);
		for (int m = 1; m < 13; m++) {
			monthComboBox.addItem(m);
		}
		monthComboBox.setSelectedItem(month);
		panel.add(monthComboBox);

		final JLabel monthLabel = new JLabel();
		monthLabel.setText("月    ");
		panel.add(monthLabel);

		final JButton submitButton = new JButton();
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableValueV.removeAllElements();
				int year = (Integer) yearComboBox.getSelectedItem();
				int month = (Integer) monthComboBox.getSelectedItem();
				for (int day = 1; day <= daysOfMonth[month]; day++) {
					Vector rowV = new Vector();// 表格行对象
					rowV.add(day);
					String date = year + "";
					if (month < 10)
						date += "0" + month;
					else
						date += month;
					if (day < 10)
						date += "0" + day;
					else
						date += day;
					String[] values = dao.monthCheckOut(date);
					for (int i = 0; i < values.length; i++) {
						rowV.add(values[i]);
					}
					tableValueV.add(rowV);
				}
				Vector rowV = new Vector();// 表格行对象
				rowV.add("总计");
				String[] values = null;
				if (month < 10)
					values = dao.monthCheckOut(year + "0" + month);
				else
					values = dao.monthCheckOut(year + "" + month);
				for (int i = 0; i < values.length; i++) {
					rowV.add(values[i]);
				}
				tableValueV.add(rowV);
				tableModel.setDataVector(tableValueV, tableColumnV);
			}
		});
		submitButton.setText("确定");
		panel.add(submitButton);

		final JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		tableColumnV = new Vector<String>();
		tableColumnV.add("日期");
		tableColumnV.add("开台总数");
		tableColumnV.add("消费总额");
		tableColumnV.add("平均消费额");
		tableColumnV.add("最大消费额");
		tableColumnV.add("最小消费额");

		tableValueV = new Vector();

		tableModel = new DefaultTableModel(tableValueV, tableColumnV);

		table = new MTable(tableModel);
		scrollPane.setViewportView(table);
		//
	}

	private void judgeLeapYear(int year) {
		if (year % 100 == 0) {
			if (year % 400 == 0)
				daysOfMonth[2] = 29;
			else
				daysOfMonth[2] = 28;
		} else {
			if (year % 4 == 0)
				daysOfMonth[2] = 29;
			else
				daysOfMonth[2] = 28;
		}
	}

}
