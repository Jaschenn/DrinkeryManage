package com.mwq.dao;

import java.util.Vector;

public class Dao extends BaseDao {

	private static Dao dao;

	static {
		dao = new Dao();
	}

	public static Dao getInstance() {
		return dao;
	}

	// tb_user
	public Vector sUser() {
		return selectSomeNote("select name,sex,birthday,id_card,freeze from tb_user where freeze='正常'");
	}

	public Vector sUserNameOfNotFreeze() {
		return selectSomeValue("select name from tb_user where freeze='正常'");
	}

	public Vector sUserByName(String name) {
		return selectOnlyNote("select * from tb_user where name='" + name + "'");
	}

	public boolean uPasswordByName(String name, String password) {
		return super.longHaul("update tb_user set password='" + password
				+ "' where name='" + name + "'");
	}

	public boolean uFreezeByName(String name, String freeze) {
		return super.longHaul("update tb_user set freeze='" + freeze
				+ "' where name='" + name + "'");
	}

	public boolean iUser(String values[]) {
		String sql = "insert into tb_user(name,sex,birthday,id_card,password,freeze) values('"
				+ values[0]
				+ "','"
				+ values[1]
				+ "','"
				+ values[2]
				+ "','"
				+ values[3] + "','" + values[4] + "','" + values[5] + "')";
		System.out.println(sql);
		return longHaul(sql);
	}

	// tb_order_form
	public Vector<?> sOrderFormOfDay(String date) {
		return selectSomeNote("select * from tb_order_form where datetime between '"
				+ date + " 00:00:00' and '" + date + " 23:59:59'");
	}

	public String sOrderFormOfMaxId() {
		Object object = selectOnlyValue("select max(num) from tb_order_form");
		if (object == null) {
			return null;
		} else {
			return object.toString();
		}
	}

	public String sOrderFormOfMinDatetime() {
		Object object = selectOnlyValue("select min(datetime) from tb_order_form");
		if (object == null) {
			return null;
		} else {
			return object.toString();
		}
	}

	public String[] monthCheckOut(String num) {
		String values[] = { "——", "——", "——", "——", "——" };
		String sqls[] = {
				"select count(*) from tb_order_form where num like '" + num
						+ "%'",
				"select sum(money) from tb_order_form where num like '" + num
						+ "%'",
				"select avg(money) from tb_order_form where num like '" + num
						+ "%'",
				"select max(money) from tb_order_form where num like '" + num
						+ "%'",
				"select min(money) from tb_order_form where num like '" + num
						+ "%'" };
		for (int i = 0; i < sqls.length; i++) {
			Object value = super.selectOnlyValue(sqls[i]);
			if (value != null) {
				String v = value.toString();
				if (!v.equals("0"))
					values[i] = v;
			}
		}
		return values;
	}

	public Vector yearCheckOut(int year) {
		Vector<Vector> valueV = new Vector<Vector>();
		String sql = "";
		String formatMonth = "";
		String formatDay = "";
		for (int day = 1; day <= 31; day++) {
			Vector rowV = new Vector();// 统计行
			rowV.add(day);// 加入日期
			formatDay = (day < 10 ? "0" + day : "" + day);
			for (int month = 1; month <= 12; month++) {// 做统计
				formatMonth = (month < 10 ? "0" + month : "" + month);
				sql = "select sum(money) from tb_order_form where num like '"
						+ year + formatMonth + formatDay + "%'";
				yearCheckOut(rowV, sql);
			}
			sql = "select sum(money) from tb_order_form where num like '"
					+ year + "__" + formatDay + "%'";// 做列总计
			yearCheckOut(rowV, sql);
			valueV.add(rowV);
		}
		Vector rowV = new Vector();// 总计行
		rowV.add("总计");
		for (int month = 1; month <= 12; month++) {// 做月总计
			formatMonth = (month < 10 ? "0" + month : "" + month);
			sql = "select sum(money) from tb_order_form where num like '"
					+ year + formatMonth + "%'";
			yearCheckOut(rowV, sql);
		}
		sql = "select sum(money) from tb_order_form where num like '" + year
				+ "%'";// 做年总计
		yearCheckOut(rowV, sql);
		valueV.add(rowV);

		return valueV;
	}

	public void yearCheckOut(Vector rowV, String sql) {
		Object value = super.selectOnlyValue(sql);
		if (value == null)
			rowV.add("——");
		else
			rowV.add(value);
	}

	public boolean iOrderForm(String[] values) {
		String sql = "insert into tb_order_form(num,desk_num,datetime, money, user_id) values('"
				+ values[0]
				+ "','"
				+ values[1]
				+ "','"
				+ values[2]
				+ "',"
				+ values[3] + "," + values[4] + ")";
		return longHaul(sql);
	}

	// tb_order_item
	public boolean iOrderItem(String[] values) {
		String sql = "insert into tb_order_item(order_form_num,menu_num,amount, total) values('"
				+ values[0]
				+ "','"
				+ values[1]
				+ "',"
				+ values[2]
				+ ","
				+ values[3] + ")";
		return longHaul(sql);
	}

	// v_order_item_and_menu
	public Vector sOrderItemAndMenuByOrderFormNum(String num) {
		return selectSomeNote("select * from v_order_item_and_menu where order_form_num='"
				+ num + "'");
	}

	// Desk
	public Vector sDesk() {
		return selectSomeNote("select * from tb_desk");
	}

	public Vector sDeskByNum(String num) {
		return selectOnlyNote("select * from tb_desk where num='" + num + "'");
	}

	public boolean iDesk(String num, String seating) {
		String sql = "insert into tb_desk values('" + num + "'," + seating
				+ ")";
		return longHaul(sql);
	}

	public boolean dDeskByNum(String num) {
		String sql = "delete from tb_desk where num='" + num + "'";
		return longHaul(sql);
	}

	// Sort
	public Vector sSortName() {
		return selectSomeNote("select name from tb_sort");
	}

	public Vector sSortById(String id) {
		return selectOnlyNote("select * from tb_sort where id=" + id);
	}

	public Vector sSortByName(String name) {
		return selectSomeNote("select * from tb_sort where name='" + name + "'");
	}

	public boolean iSort(String name) {
		String sql = "insert into tb_sort values('" + name + "')";
		return longHaul(sql);
	}

	public boolean dSortByName(String name) {
		String sql = "delete from tb_sort where name='" + name + "'";
		return longHaul(sql);
	}

	// Menu
	public Vector sMenu() {
		String hql = "select num, name, code, sort_id, unit, unit_price from tb_menu";
		Vector vector = selectSomeNote(hql);
		for (int i = 0; i < vector.size(); i++) {
			Vector menuV = (Vector) vector.get(i);
			Vector sortV = sSortById(menuV.get(4).toString());
			menuV.set(4, sortV.get(1));
		}
		return vector;
	}

	public Vector sMenuOfSell() {
		String hql = "select num, name, code, sort_id, unit, unit_price from tb_menu where state='销售'";
		Vector vector = selectSomeNote(hql);
		for (int i = 0; i < vector.size(); i++) {
			Vector menuV = (Vector) vector.get(i);
			Vector sortV = sSortById(menuV.get(4).toString());
			menuV.set(4, sortV.get(1));
		}
		return vector;
	}

	public Vector sMenuById(String num) {
		return selectSomeNote("select * from tb_menu where num like '" + num
				+ "%'");
	}

	public Vector sMenuByCode(String code) {
		return selectSomeNote("select * from tb_menu where code like '" + code
				+ "%'");
	}

	// public Vector sMenuByName(String name) {
	// return selectOnlyNote("select * from tb_menu where name='" + name
	// + "' and state='销售'");
	// }

	public Vector sMenuByNameAndState(String name, String state) {
		return selectOnlyNote("select * from tb_menu where name='" + name
				+ "' and state='" + state + "'");
	}

	public String sMenuOfMaxId() {
		Object object = selectOnlyValue("select max(num) from tb_menu");
		if (object == null) {
			return null;
		} else {
			return object.toString();
		}
	}

	public boolean iMenu(String[] values) {
		String sql = "insert into tb_menu(num,name, code, sort_id, unit, unit_price, state) values('"
				+ values[0]
				+ "','"
				+ values[1]
				+ "','"
				+ values[2]
				+ "',"
				+ values[3]
				+ ",'"
				+ values[4]
				+ "','"
				+ values[5]
				+ "','"
				+ values[6] + "')";
		return longHaul(sql);
	}

	public boolean uMenu(String[] values) {
		String sql = "update tb_menu set code='" + values[2] + "', sort_id="
				+ values[3] + ", unit='" + values[4] + "', unit_price="
				+ values[5] + ", state='" + values[6] + "' where name='"
				+ values[1] + "'";
		return longHaul(sql);
	}

	public boolean uMenuStateByName(String name, String state) {
		return longHaul("update tb_menu set state='" + state + "' where name='"
				+ name + "'");
	}

	public boolean dMenuByName(String name) {
		return longHaul("delete from tb_menu where name='" + name + "'");
	}

}
