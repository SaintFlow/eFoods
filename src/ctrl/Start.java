package ctrl;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;

import auth.EECSAuth;
import bean.CategoryBean;
import bean.CustomerBean;
import bean.FoodBean;
import model.Category;
import model.Food;
//import model.Store;

/**
 * Servlet implementation class Start
 */
@WebServlet(
{ "/Startup" })
public class Start extends HttpServlet
{
	@Override
	public void init() throws ServletException
	{
		try
		{
			Category category = new Category();
			Food food = new Food();
			this.getServletContext().setAttribute("category", category);
			this.getServletContext().setAttribute("food", food);
			((Category) this.getServletContext().getAttribute("category")).getCategoryImages(this.getServletContext());
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Base64DecodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String errormessage = "";

		this.getServletContext().setAttribute("error", errormessage);
	}

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Start()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		// TODO Auto-generated method stub
		response.setContentType("text/plain");
		HttpSession session = request.getSession();
		if (session.isNew())
		{
			long creationTime = session.getCreationTime();
			session.setAttribute("sessionID", session.getId());
			session.setAttribute("creationTime", creationTime);
		}
		session.setAttribute("startTime", System.currentTimeMillis());
		PrintWriter pw = response.getWriter();
		this.getServletContext().setAttribute("error", "");
		// response.getWriter().append("Served at:
		// ").append(request.getContextPath());
		// System.out.println("Logging message on console");

		// List of category information
		List<CategoryBean> categoryList = Category.getCategories();

		this.getServletContext().setAttribute("categoryList", categoryList);
		
		//Get all category information
		List<Integer> Category_ID_list = new ArrayList<Integer>();
		List<String> Category_name_list = new ArrayList<String>();
		List<String> Category_description_list = new ArrayList<String>();
		List<byte[]> Category_picture_list = new ArrayList<byte[]>();

		for (int i = 0; i < categoryList.size(); i++)
		{
			Category_ID_list.add(categoryList.get(i).getId());
			Category_name_list.add(categoryList.get(i).getName());
			Category_description_list.add(categoryList.get(i).getDescription());
			Category_picture_list.add(categoryList.get(i).getPicture());
		}
		
		//Store all category information into the request scope
		request.setAttribute("CATEGORY_ID_LIST", Category_ID_list);
		request.setAttribute("CATEGORY_NAME_LIST", Category_name_list);
		request.setAttribute("CATEGORY_DESCRIPTION_LIST", Category_description_list);
		request.setAttribute("CATEGORY_PICTURE_LIST", Category_picture_list);
		request.setAttribute("SUCCESS", true);

		//Store all category information into the session scope
		session.setAttribute("CATEGORY_ID_LIST", Category_ID_list);
		session.setAttribute("CATEGORY_NAME_LIST", Category_name_list);
		session.setAttribute("CATEGORY_DESCRIPTION_LIST", Category_description_list);
		session.setAttribute("CATEGORY_PICTURE_LIST", Category_picture_list);
		session.setAttribute("SUCCESS", true);
		
		//Get cart parameters
		String parameterAddToCart = request.getParameter("addToCart");
		String parameterCat = request.getParameter("selectCat");
		String parameterQuantity = request.getParameter("quantityNumber");
		String parameterAction = request.getParameter("action");
		String parameterUpdateQ = request.getParameter("quantity");
		String parameteritemIndex = request.getParameter("itemIndex");
		String parameterSearch = request.getParameter("search");
		String parameterLoginAttempt = request.getParameter("loginAttempt");
		String parameterFullName = request.getParameter("fullname");
		String parameterHash = request.getParameter("hash");
		String parameterUser = request.getParameter("user");
		
		try
		{
					
			//Login attempt
			if (parameterLoginAttempt != null)
			{
				String url = "https://www.eecs.yorku.ca/~randya25/auth/login.cgi?back=" + request.getRequestURL().toString();
				response.sendRedirect(url);
			} 
			//Response from CGI
			else if (parameterUser != null && parameterFullName != null && parameterHash != null)
			{
				EECSAuth auth = new EECSAuth();
				Boolean correct = auth.authenticate(parameterUser, parameterHash);
				if (correct)
				{
					//Confirmed user
					session.setAttribute("name", parameterFullName);
					session.setAttribute("accountNumber", parameterUser);
					session.setAttribute("loggedIn", "true");
					session.setAttribute("orderNumber", 1);
					System.out.println("CORRECT LOG IN");
					session.getServletContext().getRequestDispatcher("/" + "LogIn.jspx").forward(request, response);
				} else
				{
					System.out.println("WRONG LOG IN");
					session.getServletContext().getRequestDispatcher("/" + "LogIn.jspx").forward(request, response);
				}
			}
			else if (parameterCat != null)
			{
				//Get all food information
				System.out.println("parameterCat is " + parameterCat);
				session.setAttribute("paramCat", parameterCat);
				List<FoodBean> foodBeans = ((Food) this.getServletContext().getAttribute("food")).getFoodCatIDList(Integer.parseInt(parameterCat));
				
				List<Integer> foodCatIDList = new ArrayList<Integer>();
				List<String> foodNameList = new ArrayList<String>();
				List<Double> foodPriceList = new ArrayList<Double>();
				List<String> foodNumberList = new ArrayList<String>();

				for (int i = 0; i < foodBeans.size(); i++)
				{
					foodCatIDList.add(foodBeans.get(i).getCatid());
					foodNameList.add(foodBeans.get(i).getName());
					foodPriceList.add(foodBeans.get(i).getPrice());
					foodNumberList.add(foodBeans.get(i).getNumber());
				}
				
				session.setAttribute("FOOD_CATID_LIST", foodCatIDList);
				session.setAttribute("FOOD_NAME_LIST", foodNameList);
				session.setAttribute("FOOD_PRICE_LIST", foodPriceList);
				session.setAttribute("FOOD_NUMBER_LIST", foodNumberList);
				//request.setAttribute("FoodList", ((Food) this.getServletContext().getAttribute("food")).getFoodList(parameterCat));
				request.getServletContext().getRequestDispatcher("/" + "FoodList.jspx").forward(request, response);
			}else if(parameterSearch != null && !parameterSearch.isEmpty())
			{
				System.out.println("Searching...");
				List<FoodBean> foodBeans = ((Food) this.getServletContext().getAttribute("food")).getFoodList(parameterSearch);
				
				List<Integer> foodCatIDList = new ArrayList<Integer>();
				List<String> foodNameList = new ArrayList<String>();
				List<Double> foodPriceList = new ArrayList<Double>();
				List<String> foodNumberList = new ArrayList<String>();

				for (int i = 0; i < foodBeans.size(); i++)
				{
					foodCatIDList.add(foodBeans.get(i).getCatid());
					foodNameList.add(foodBeans.get(i).getName());
					foodPriceList.add(foodBeans.get(i).getPrice());
					foodNumberList.add(foodBeans.get(i).getNumber());
				}
				
				session.setAttribute("FOOD_CATID_LIST", foodCatIDList);
				session.setAttribute("FOOD_NAME_LIST", foodNameList);
				session.setAttribute("FOOD_PRICE_LIST", foodPriceList);
				session.setAttribute("FOOD_NUMBER_LIST", foodNumberList);
				request.getServletContext().getRequestDispatcher("/" + "FoodList.jspx").forward(request, response);
			}
			else if(parameterAddToCart != null)
			{
				System.out.println("Dispatching to cart servlet");
				String newcat = (String)session.getAttribute("paramCat");
				if (newcat != null)
				{
					List<FoodBean> foodBeans = ((Food) this.getServletContext().getAttribute("food")).getFoodCatIDList(Integer.parseInt(newcat));
					
					List<Integer> foodCatIDList = new ArrayList<Integer>();
					List<String> foodNameList = new ArrayList<String>();
					List<Double> foodPriceList = new ArrayList<Double>();
					List<String> foodNumberList = new ArrayList<String>();

					for (int i = 0; i < foodBeans.size(); i++)
					{
						foodCatIDList.add(foodBeans.get(i).getCatid());
						foodNameList.add(foodBeans.get(i).getName());
						foodPriceList.add(foodBeans.get(i).getPrice());
						foodNumberList.add(foodBeans.get(i).getNumber());
					}
					
					session.setAttribute("FOOD_CATID_LIST", foodCatIDList);
					session.setAttribute("FOOD_NAME_LIST", foodNameList);
					session.setAttribute("FOOD_PRICE_LIST", foodPriceList);
					session.setAttribute("FOOD_NUMBER_LIST", foodNumberList);
				}
				request.getServletContext().getRequestDispatcher("/" + "Cart").forward(request, response);
			}
			else if (parameterAction != null && parameterAction == "Update")
			{
				session.getServletContext().getRequestDispatcher("/" + "Cart").forward(request, response);
			}
			else
			{
				System.out.println("Nothing else to do in Start Servlet");
				request.getServletContext().getRequestDispatcher("/" + "UI.jspx").forward(request, response);
			}
			
		}finally
		{
			
		}		
		// System.out.println(parameterS);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
