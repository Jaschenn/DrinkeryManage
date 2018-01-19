package com.mwq.frame.check_out;

import java.awt.BorderLayout;
import java.awt.Container;
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
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import sun.swing.SwingUtilities2;

import com.mwq.dao.Dao;
import com.mwq.mwing.FixedColumnTablePanel;
import com.mwq.mwing.MTable;
import com.mwq.tool.Today;

public class YearDialog extends JDialog {

	private JTable table;

	private Vector<String> tableColumnV;

	private Vector<Vector<Object>> tableValueV;

	private DefaultTableModel tableModel;

	private JComboBox yearComboBox;

	private Dao dao=Dao.getInstance();

	/**
	 * Launch the application
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			YearDialog dialog = new YearDialog();
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
	public YearDialog() {
		super();
		setModal(true);
		setTitle("年结账");
		setBounds(60, 60, 860, 620);

		final JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);

		yearComboBox = new JComboBox();
		yearComboBox.setMaximumRowCount(10);
		String minDatetime = dao.sOrderFormOfMinDatetime();
		int year = Today.getYEAR();
		if (minDatetime == null) {
			yearComboBox.addItem(year);
		} else {
			int minYear = Integer.valueOf(minDatetime.substring(0, 4));
			for (int y = minYear; y <= year; y++) {
				yearComboBox.addItem(y);
			}
		}
		yearComboBox.setSelectedItem(year);
		panel.add(yearComboBox);

		final JLabel yearLabel = new JLabel();
		yearLabel.setText("年    ");
		panel.add(yearLabel);

		final JButton submitButton = new JButton();
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableValueV.removeAllElements();
				int year = (Integer) yearComboBox.getSelectedItem();
				tableValueV.addAll(dao.yearCheckOut(year));
				Container contentPane = getContentPane();
				contentPane.remove(1);
				getContentPane()
						.add(
								new FixedColumnTablePanel(tableColumnV,
										tableValueV, 1), BorderLayout.CENTER);
				SwingUtilities.updateComponentTreeUI(contentPane);
			}
		});
		submitButton.setText("确定");
		panel.add(submitButton);

		tableColumnV = new Vector<String>();
		tableColumnV.add("日期");
		for (int i = 1; i <= 12; i++) {
			tableColumnV.add(i + " 月");
		}
		tableColumnV.add("总计");

		tableValueV = new Vector<Vector<Object>>();

		getContentPane().add(
				new FixedColumnTablePanel(tableColumnV, tableValueV, 1),
				BorderLayout.CENTER);
		//
	}

}
