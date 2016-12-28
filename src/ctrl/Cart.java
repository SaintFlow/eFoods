package ctrl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.CartBean;
import bean.CartItemBean;
import bean.CustomerBean;
import model.Orders;
import auth.EECSAuth;

/**
 * Servlet implementation class Cart
 */
@WebServlet("/Cart/*")
public class Cart extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Cart() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		HttpSession session = request.getSession();
		
		
		//Recording session time creation time 
		if (session.isNew())
		{
			long creationTime = session.getCreationTime();
			session.setAttribute("sessionID", session.getId());
			session.setAttribute("creationTime", creationTime);
		}
		
		List<Integer> foodCatIDList = new ArrayList<Integer>();
		List<String> foodNameList = new ArrayList<String>();
		List<Double> foodPriceList = new ArrayList<Double>();
		List<String> foodNumberList = new ArrayList<String>();
		
		//Get Food Item Information from request scope
		foodCatIDList = (List<Integer>) session.getAttribute("FOOD_CATID_LIST");
		foodNameList = (List<String>) session.getAttribute("FOOD_NAME_LIST");
		foodPriceList = (List<Double>) session.getAttribute("FOOD_PRICE_LIST");
		foodNumberList = (List<String>) session.getAttribute("FOOD_NUMBER_LIST");
		Enumeration<String> attributes = session.getAttributeNames();
		Enumeration<String> sattributes = session.getAttributeNames();
		String isloggedIn = (String) session.getAttribute("loggedIn");
		CustomerBean customer = (CustomerBean) session.getAttribute("customer");
		CartBean cart = (CartBean) session.getAttribute("cart");
		String name = (String) session.getAttribute("name");
		String accountNumber = (String) session.getAttribute("accountNumber");
		
		if (foodNumberList == null)
		{
			System.out.println("LIST IS NULL");
		}
		
		//Retrieving parameters
		String parameterAddToCart = request.getParameter("addToCart");
		String parameterQuantity = request.getParameter("quantityNumber");
		String parameterAction = request.getParameter("action");
		String parameterUpdateQ = request.getParameter("quantity");
		String parameteritemIndex = request.getParameter("itemIndex");
		String parameterLoginAttempt = request.getParameter("loginAttempt");
		String parameterCheckOut = request.getParameter("checkout");
		String parameterUser = request.getParameter("user");
		String parameterFullName = request.getParameter("fullname");
		String parameterHash = request.getParameter("hash");
		
		//Redirecting Traffic
		//Empty Cart
		if (parameterLoginAttempt != null)
		{
			String url = "https://www.eecs.yorku.ca/~randya25/auth/login.cgi?back=" + request.getRequestURL().toString();
			response.sendRedirect(url);
		} else if (parameterUser != null && parameterFullName != null && parameterHash != null)
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
				CustomerBean newCustomer =  new CustomerBean();
				newCustomer.setName(parameterFullName);
				newCustomer.setNumber(parameterUser);
				CartBean cartBean = (CartBean) session.getAttribute("cart");
				if (cartBean == null)
				{
					System.out.println("CART IS NULL");
					cartBean = new CartBean();
					//request.setAttribute("cart", cartBean);
				}
				cartBean.setCustomerBean(newCustomer);
				System.out.println("CORRECT LOG IN");
				session.getServletContext().getRequestDispatcher("/" + "LogIn.jspx").forward(request, response);
			} else
			{
				System.out.println("WRONG LOG IN");
				session.getServletContext().getRequestDispatcher("/" + "LogIn.jspx").forward(request, response);
			}
		}
		else if (parameterCheckOut != null)
		{
			//Hasn't logged in or incorrect log in
			if (isloggedIn == null)
			{
				System.out.println("Hasn't logged in yet but wants to check out");
				String url = "https://www.eecs.yorku.ca/~randya25/auth/login.cgi?back=" + request.getRequestURL().toString();
				response.sendRedirect(url);
			}
			//Logged in
			else if ( isloggedIn!= null)
			{
				System.out.println("loggedIN and attempting to checkout");
				//Logged in but cart doesn't exist
				if (cart == null)
				{
					session.getServletContext().getRequestDispatcher("/" + "Checkout.jspx").forward(request, response);
				} else
				{
					//Logged in but cart is empty
					if ( cart.getCartItems().isEmpty())
					{
						session.getServletContext().getRequestDispatcher("/" + "Checkout.jspx").forward(request, response);
					} 
					else
					{   //Proceed to valid checkout
						session.setAttribute("checkOutTime", System.currentTimeMillis());
						String fileName;
						Orders orderProcess = new Orders();
						try
						{
							String path = this.getServletContext().getRealPath("/xmlOrders");
							System.out.println(path);
							File folder = new File(path);
							File[] listOfFiles = folder.listFiles();
							
							int orderNum = 1;
							for (int i = 0; i < listOfFiles.length; i++)
							{
								if (listOfFiles[i].isFile() && listOfFiles[i].getName().contains(accountNumber))
								{
									orderNum++;
								}
							}
							if (orderNum < 10)
							{
								fileName = orderProcess.checkout((Integer) session.getAttribute("orderNumber"), cart, path + "/PO" + accountNumber + "_0" + orderNum + ".xml");
								System.out.println(path + "/PO" + accountNumber + "_0" + orderNum + ".xml");
								session.setAttribute("xmlLink", "/PO" + accountNumber + "_0" + orderNum + ".xml");
							}
							else
							{
								fileName = orderProcess.checkout((Integer) session.getAttribute("orderNumber"), cart, "PO" + accountNumber + "_" + orderNum + ".xml");
								System.out.println(path + "/PO" + accountNumber + "_0" + orderNum + ".xml");
								session.setAttribute("xmlLink", "/PO" + accountNumber + "_" + orderNum + ".xml");
							}
							session.getServletContext().getRequestDispatcher("/" + "Checkout.jspx").forward(request, response);
							cart.emptyCart();
							cart.setHstTotal(0);
							cart.setTotal(0);
							cart.setOrderTotal(0);
							cart.setShippingCost(0);
							session.setAttribute("cart", cart);
						} catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		//Somehow didn't obtain food from foodlist
		else if (parameterAddToCart != null && foodCatIDList.isEmpty())
		{
			session.getServletContext().getRequestDispatcher("/" + "Cart.jspx").forward(request, response);
		}
		//Adding item to cart
		else if (parameterAddToCart != null && parameterQuantity != null)
		{
			session.setAttribute("addToCartTime", System.currentTimeMillis());
			
			int index = Integer.parseInt(parameterAddToCart);
			session.setAttribute("selectedIndex", index);
			String foodName = foodNameList.get(index);
			int foodCatId = foodCatIDList.get(index);
			double foodPrice = foodPriceList.get(index);
			String foodNumber = foodNumberList.get(index);
			
			System.out.println("sREQUEST ATTRIBUTES");
//			while (sattributes.hasMoreElements())
//			{
//				
//				System.out.println(sattributes.nextElement());
//			}
			
			CartBean cartBean = (CartBean) session.getAttribute("cart");
			if (cartBean == null)
			{
				System.out.println("CART IS NULL");
				cartBean = new CartBean();
				//request.setAttribute("cart", cartBean);
			}
			if (isloggedIn != null && !cartBean.hasCustomerBean())
			{
				CustomerBean newCustomer =  new CustomerBean();
				newCustomer.setName((String)session.getAttribute("name"));
				newCustomer.setNumber((String)session.getAttribute("accountNumber"));
				cartBean.setCustomerBean(newCustomer);
			}
			cartBean.addCartItem(foodNumber, foodPrice + "",  parameterQuantity, foodName);
			session.setAttribute("cart", cartBean);
			
			session.getServletContext().getRequestDispatcher("/" + "Cart.jspx").forward(request, response);
		}
		//Updating the cart quantity number
		else if (parameterAction != null && parameterAction.equals("Update") && parameterUpdateQ != null)
		{
			System.out.println("Updating");
			CartBean cartBean = (CartBean) session.getAttribute("cart");
			if (cartBean == null)
			{
				System.out.println("CART IS NULL");
				cartBean = new CartBean();
				//request.setAttribute("cart", cartBean);
			}
			cartBean.updateCartItem(parameteritemIndex, parameterUpdateQ);
			session.setAttribute("cart", cartBean);
			
			session.getServletContext().getRequestDispatcher("/" + "Cart.jspx").forward(request, response);
		}
		else
		{
			System.out.println("Nothing else, redirecting to default Cart");
			request.getServletContext().getRequestDispatcher("/" + "Cart.jspx").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
