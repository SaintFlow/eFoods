package model;

import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.ServletException;

import bean.FoodBean;
import dao.FoodDAO;

public class Food
{
	private FoodDAO foodDao;
	
	public Food() throws NamingException
	{
		
		this.foodDao = new FoodDAO();
	}
	
	public List<FoodBean> getFoodList(String section) throws ServletException
	{
		try
		{
			List<FoodBean> foodList = foodDao.getFoodByCategory(section);
			return foodList;
		} catch (SQLException e)
		{
			throw new ServletException(e);
		}
		
	}
	
	public List<FoodBean> getFoodCatIDList(int catID) throws ServletException
	{
		try
		{
			List<FoodBean> foodList = foodDao.getFoodByCategoryID(catID);
			return foodList;
		} catch (SQLException e)
		{
			throw new ServletException(e);
		}
		
	}
}
