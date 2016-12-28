package dao;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.tomcat.jdbc.pool.DataSource;

import bean.FoodBean;

public class FoodDAO
{
	private DataSource ds;
	private static Connection conn = null;
	private static Statement stmt = null;

	public FoodDAO() throws NamingException
	{
		ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/EECS");
		// TODO Auto-generated constructor stub
	}

	private void createConnection() throws SQLException
	{
		conn = this.ds.getConnection();
		// conn.createStatement().executeUpdate("set schema "+SCHEMA_NAME);
		// System.out.println(ds.is);
		stmt = conn.createStatement();
	}
	
	private void closeConnection() throws SQLException
	{
    	conn.close();
	}
	
	public List<FoodBean> getFoodByCategoryID(int catID) throws SQLException
	{
		List<FoodBean> resultList = new ArrayList<FoodBean>();
		createConnection();
		ResultSet r = stmt.executeQuery("select * from ROUMANI.ITEM where CATID = "+catID);
		//System.out.println("Going to add %");
		while (r.next())
		{
			//System.out.println("Adding stuff");
			int catid = r.getInt("CATID");
			double price = r.getDouble("PRICE");
			String name = r.getString("NAME");
			String number = r.getString("NUMBER");

			FoodBean temp = new FoodBean(price, name, number, catid);
			resultList.add(temp);
		}
		closeConnection();
		return resultList;

	}

	public List<FoodBean> getFoodByCategory(String section) throws SQLException
	{
		List<FoodBean> resultList = new ArrayList<FoodBean>();
		createConnection();
		ResultSet r = stmt.executeQuery("select * from ROUMANI.ITEM where NAME like '%" + section + "%'");
		//System.out.println("Going to add %");
		while (r.next())
		{
			//System.out.println("Adding stuff");
			int catid = r.getInt("CATID");
			double price = r.getDouble("PRICE");
			String name = r.getString("NAME");
			String number = r.getString("NUMBER");

			FoodBean temp = new FoodBean(price, name, number, catid);
			resultList.add(temp);
		}
		closeConnection();
		return resultList;

	}
}
