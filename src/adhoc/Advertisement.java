package adhoc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class Advertisement
 */
@WebFilter(
		dispatcherTypes = {DispatcherType.FORWARD }
					, 
		urlPatterns = { 
				"/Advertisement", 
				"/Cart.jspx"
		})
public class Advertisement implements Filter {

    /**
     * Default constructor. 
     * 
     */
	
	private String adFoodNumber = "1409S413";
    public Advertisement() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here

		// pass the request along the filter chain
		System.out.println("FILTERING");
		MyResponse res = new MyResponse((HttpServletResponse) response);
		String parameterAddToCart = request.getParameter("addToCart");
		String parameterQuantity = request.getParameter("quantityNumber");
		HttpServletRequest request2 = (HttpServletRequest) request;
		HttpSession session = request2.getSession();
		
		if (parameterAddToCart != null && parameterQuantity != null)
		{
			int index = Integer.parseInt(parameterAddToCart);
			session.setAttribute("selectedIndex", index);
			
			List<Integer> foodCatIDList = new ArrayList<Integer>();
			List<String> foodNameList = new ArrayList<String>();
			List<Double> foodPriceList = new ArrayList<Double>();
			List<String> foodNumberList = new ArrayList<String>();
			
			foodCatIDList = (List<Integer>) session.getAttribute("FOOD_CATID_LIST");
			foodNameList = (List<String>) session.getAttribute("FOOD_NAME_LIST");
			foodPriceList = (List<Double>) session.getAttribute("FOOD_PRICE_LIST");
			foodNumberList = (List<String>) session.getAttribute("FOOD_NUMBER_LIST");
			
			String foodName = foodNameList.get(index);
			int foodCatId = foodCatIDList.get(index);
			double foodPrice = foodPriceList.get(index);
			String foodNumber = foodNumberList.get(index);
			
			if ( foodNumber.equals(adFoodNumber))
			{
				chain.doFilter(request, res);
				String payload = res.getContent();
				payload = payload.replaceAll("Advertisement", "SOME ADVERTISEMENT FOR " + adFoodNumber + "!!");
				response.getWriter().println(payload);
				
			} else
			{
				chain.doFilter(request, response);
			}
		} else
		{
		chain.doFilter(request, response);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
