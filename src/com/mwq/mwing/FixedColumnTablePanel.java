package com.mwq.mwing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class FixedColumnTablePanel extends JPanel {

	private MTable fixedColumnTable;

	private FixedColumnTableModel fixedColumnTableModel;

	private MTable floatingColumnTable;

	private FloatingColumnTableModel floatingColumnTableModel;

	private Vector<String> tableColumnV;

	private Vector<Vector<Object>> tableValueV;

	private int fixedColumn = 2;

	public FixedColumnTablePanel(Vector<String> tableColumnV,
			Vector<Vector<Object>> tableValueV, int fixedColumn) {
		super();
		setLayout(new BorderLayout());

		this.tableColumnV = tableColumnV;
		this.tableValueV = tableValueV;
		this.fixedColumn = fixedColumn;

		fixedColumnTableModel = new FixedColumnTableModel();// 创建固定列表格模型对象

		fixedColumnTable = new MTable(fixedColumnTableModel);// 创建固定列表格对象
		ListSelectionModel fixed = fixedColumnTable.getSelectionModel();// 获得选择模型对象
		fixed.addListSelectionListener(new MListSelectionListener(true));// 添加行被选中的事件监听器

		floatingColumnTableModel = new FloatingColumnTableModel();// 创建可移动列表格模型对象

		floatingColumnTable = new MTable(floatingColumnTableModel);// 创建可移动列表格对象
		ListSelectionModel floating = floatingColumnTable.getSelectionModel();// 获得选择模型对象
		floating.addListSelectionListener(new MListSelectionListener(false));// 添加行被选中的事件监听器

		JScrollPane scrollPane = new JScrollPane();// 创建一个滚动面版对象
		scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, fixedColumnTable
				.getTableHeader()); // 将固定列表格头放到滚动面版的左上方

		JViewport viewport = new JViewport();// 创建一个用来显示基础信息的视口对象
		viewport.setView(fixedColumnTable);// 将固定列表格添加到视口中
		viewport.setPreferredSize(fixedColumnTable.getPreferredSize());// 设置视口的首选大小为固定列表格的首选大小
		scrollPane.setRowHeaderView(viewport);// 将视口添加到滚动面版的标题视口中

		scrollPane.setViewportView(floatingColumnTable);// 将可移动表格添加到默认视口

		add(scrollPane, BorderLayout.CENTER);
		//
	}

	class MTable extends JTable {

		public MTable(AbstractTableModel tableModel) {
			super(tableModel);
			setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		}

		// 表格列名信息
		@Override
		public JTableHeader getTableHeader() {
			// 获得表格头对象
			JTableHeader tableHeader = super.getTableHeader();
			tableHeader.setReorderingAllowed(false);// 设置表格列不可重排
			// 获得表格头的单元格对象
			DefaultTableCellRenderer defaultRenderer = (DefaultTableCellRenderer) tableHeader
					.getDefaultRenderer();
			// 设置单元格内容（即列名）居中显示
			defaultRenderer
					.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
			return tableHeader;
		}

		// 表格列值居中显示
		@Override
		public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
			DefaultTableCellRenderer defaultRenderer = (DefaultTableCellRenderer) super
					.getDefaultRenderer(columnClass);
			defaultRenderer
					.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
			return defaultRenderer;
		}

		// 表格行只可单选
		@Override
		public ListSelectionModel getSelectionModel() {
			ListSelectionModel selectionModel = super.getSelectionModel();
			selectionModel
					.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			return selectionModel;
		}

		// 用来设置表格的选中行
		@Override
		public void setRowSelectionInterval(int fromRow, int toRow) {// 重构父类的方法
			super.setRowSelectionInterval(fromRow, toRow);
		}

		// 用来设置表格的唯一选中行
		public void setRowSelectionInterval(int row) {// 通过重载实现自己的方法
			setRowSelectionInterval(row, row);
		}

	}

	class FixedColumnTableModel extends AbstractTableModel {

		public int getColumnCount() {// 返回固定列的数量
			return fixedColumn;
		}

		public int getRowCount() {// 返回行数
			return tableValueV.size();
		}

		public Object getValueAt(int rowIndex, int columnIndex) {// 返回指定单元格的值
			return tableValueV.get(rowIndex).get(columnIndex);
		}

		@Override
		public String getColumnName(int columnIndex) {// 返回指定列的名称
			return tableColumnV.get(columnIndex);
		}

	}

	class FloatingColumnTableModel extends AbstractTableModel {

		public int getColumnCount() {// 返回可移动列的数量
			return tableColumnV.size() - fixedColumn;// 需要扣除固定列的数量
		}

		public int getRowCount() {// 返回行数
			return tableValueV.size();
		}

		public Object getValueAt(int rowIndex, int columnIndex) {// 返回指定单元格的值
			return tableValueV.get(rowIndex).get(columnIndex + fixedColumn);// 需要为列索引加上固定列的数量
		}

		@Override
		public String getColumnName(int columnIndex) {// 返回指定列的名称
			return tableColumnV.get(columnIndex + fixedColumn);// 需要为列索引加上固定列的数量
		}

	}

	class MListSelectionListener implements ListSelectionListener {
		boolean isFixedColumnTable = true; // 默认由选中固定列表格中的行触发

		public MListSelectionListener(boolean isFixedColumnTable) {
			this.isFixedColumnTable = isFixedColumnTable;
		}

		public void valueChanged(ListSelectionEvent e) {
			if (isFixedColumnTable) { // 由选中固定列表格中的行触发
				int selectedRow = fixedColumnTable.getSelectedRow(); // 获得固定列表格中的选中行
				floatingColumnTable.setRowSelectionInterval(selectedRow); // 同时选中可移动列表格中的选中行
			} else { // 由选中可移动列表格中的行触发
				int selectedRow = floatingColumnTable.getSelectedRow(); // 获得可移动列表格中的选中行
				fixedColumnTable.setRowSelectionInterval(selectedRow); // 同时选中固定列表格中的选中行
			}
		}
	}

}
