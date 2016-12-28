package model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;

import bean.CategoryBean;
import dao.CategoryDAO;

public class Category
{
	private static CategoryDAO dao;
	public Category() throws ClassNotFoundException, SQLException, ServletException
	{
		try
		{
			dao = new CategoryDAO();
		} catch (NamingException e)
		{
			throw new ServletException(e.getMessage());
		}
	}
	
	public static  List<CategoryBean> getCategories() throws ServletException
	{
		try
		{
			return dao.retrieveAll();
		} catch (SQLException e)
		{
			throw new ServletException(e);
		}
	}
	
	public void getCategoryImages(ServletContext ctx) throws IOException, Base64DecodingException, SQLException, ServletException
	{
		try {
			List<CategoryBean> currCategories = getCategories();

			for (int i = 0; i < currCategories.size(); i++ )
			{
				// convert byte array back to BufferedImage and write to file
				InputStream in = new ByteArrayInputStream(currCategories.get(i).getPicture());
				BufferedImage bImageFromConvert = ImageIO.read(in);
				ImageIO.write(bImageFromConvert, "gif", new File(ctx.getRealPath("/pictures/" + i + ".gif")));
			}
			
 
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	
	}
}
