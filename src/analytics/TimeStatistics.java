package analytics;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 * Application Lifecycle Listener implementation class TimeStatistics
 *
 */
@WebListener
public class TimeStatistics implements HttpSessionAttributeListener {

    /**
     * Default constructor. 
     */
    public TimeStatistics() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see HttpSessionAttributeListener#attributeAdded(HttpSessionBindingEvent)
     */
    public void attributeAdded(HttpSessionBindingEvent se)  { 
    	HttpSession session;
		session = se.getSession();
		
		//Logging time it takes to add a food item to the cart
		if (session.getAttribute("addToCartTime") != null && session.getAttribute("creationTime") != null
				&& session.getAttribute("loggedTimeCart") == null)
		{
			long addToCartTime =  (long) session.getAttribute("addToCartTime") - (long) session.getAttribute("creationTime"); 
			session.setAttribute("testTime", addToCartTime);
			session.setAttribute("loggedTimeCart", "true");
			addToCartTime = addToCartTime / 1000;
			//session.getServletContext().setAttribute("addToCartTotal", 23);
			
			//If it's the first session ever
			if (session.getServletContext().getAttribute("sessionCountCart") == null)
			{				
				session.getServletContext().setAttribute("sessionCountCart", 1);
				session.getServletContext().setAttribute("addToCartTotal", addToCartTime);
				session.getServletContext().setAttribute("averageCartTime", addToCartTime);
				System.out.println("Current Cart time: " + addToCartTime + " seconds");
			} else
			{
				int sessionCount = (int)session.getServletContext().getAttribute("sessionCountCart");
				sessionCount += 1;
				long addToCartTotal = (long) session.getServletContext().getAttribute("addToCartTotal");
				addToCartTotal = addToCartTotal + addToCartTime;
				long averageTotal = addToCartTotal / sessionCount;
				session.getServletContext().setAttribute("addToCartTotal", addToCartTime);
				session.getServletContext().setAttribute("averageCartTime", averageTotal);
				System.out.println("Total Cart Time: " + addToCartTotal);
				System.out.println("Average Cart Time: " + averageTotal);
				
			}
			
		}
		
		//Logging time it takes to checkout, whether the user can successfully check out or not
		if (session.getAttribute("checkOutTime") != null && session.getAttribute("creationTime") != null
				&& session.getAttribute("loggedTimeCheck") == null)
		{
			long checkOutTime =  (long) session.getAttribute("checkOutTime") - (long) session.getAttribute("creationTime"); 
			//session.setAttribute("testTime", addToCartTime);
			session.setAttribute("loggedTimeCheck", "true");
			checkOutTime = checkOutTime / 1000;
			//session.getServletContext().setAttribute("addToCartTotal", 23);
			
			//If it's the first session ever
			if (session.getServletContext().getAttribute("sessionCountCheck") == null)
			{				
				session.getServletContext().setAttribute("sessionCountCheck", 1);
				session.getServletContext().setAttribute("checkOutTotal", checkOutTime);
				session.getServletContext().setAttribute("averageCheckTime", checkOutTime);
				System.out.println("Current CheckOut time: " + checkOutTime + " seconds");
			} else
			{
				int sessionCount = (int)session.getServletContext().getAttribute("sessionCountCheck");
				sessionCount += 1;
				long checkOutTotal = (long) session.getServletContext().getAttribute("checkOutTotal");
				checkOutTotal = checkOutTotal + checkOutTime;
				long averageTotal = checkOutTotal / sessionCount;
				session.getServletContext().setAttribute("checkOutTotal", checkOutTime);
				session.getServletContext().setAttribute("averageCheckTime", averageTotal);
				System.out.println("Total CheckOut Time: " + checkOutTotal);
				System.out.println("Average CheckOut Time: " + averageTotal);
				
			}
			
		}
    }

	/**
     * @see HttpSessionAttributeListener#attributeRemoved(HttpSessionBindingEvent)
     */
    public void attributeRemoved(HttpSessionBindingEvent se)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see HttpSessionAttributeListener#attributeReplaced(HttpSessionBindingEvent)
     */
    public void attributeReplaced(HttpSessionBindingEvent se)  { 
         // TODO Auto-generated method stub
    }
	
}
