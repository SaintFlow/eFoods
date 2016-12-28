package bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartBean
{

	private List<CartItemBean> allCartItems = new ArrayList<CartItemBean>();
	private double dlbOrderTotal ;
	private double hst = 0.13;
	private double shippingCost = 5.00;
	private double total;
	private double hstTotal;
	private CustomerBean customerBean;

	public int getLineItemCount()
	{
		return allCartItems.size();
	}
	
	public boolean hasCustomerBean()
	{
		if (this.customerBean == null)
			return false;
		else
			return true;
	}
	
	public void emptyCart()
	{
		this.allCartItems.clear();
	}
	
	public void calculateTotal()
	{
		double dblTotal = 0;
		for (int counter = 0; counter < allCartItems.size(); counter++)
		{

			dblTotal += allCartItems.get(counter).getTotalCost();

		}
		
		this.setTotal(Math.round(dblTotal* 100.0)/100.0);
	}

	public void deleteCartItem(String strItemIndex)
	{
		int iItemIndex = 0;
		try
		{
			iItemIndex = Integer.parseInt(strItemIndex);
			allCartItems.remove(iItemIndex - 1);
			calculateOrderTotal();
		} catch (NumberFormatException nfe)
		{
			System.out.println("Error while deleting cart item: " + nfe.getMessage());
			nfe.printStackTrace();
		}
	}

	public void updateCartItem(String strItemIndex, String strQuantity)
	{
		double dblTotalCost = 0.0;
		double dblUnitCost = 0.0;
		int iQuantity = 0;
		int iItemIndex = 0;
		CartItemBean cartItem = null;
		try
		{
			iItemIndex = Integer.parseInt(strItemIndex);
			iQuantity = Integer.parseInt(strQuantity);
			if (iQuantity > 0)
			{
				cartItem = (CartItemBean) allCartItems.get(iItemIndex - 1);
				dblUnitCost = cartItem.getUnitCost();
				dblTotalCost = dblUnitCost * iQuantity;
				cartItem.setQuantity(iQuantity);
				cartItem.setTotalCost(dblTotalCost);
				calculateOrderTotal();
			} else if (iQuantity <= 0)
			{
				this.deleteCartItem(strItemIndex);
			}
		} catch (NumberFormatException nfe)
		{
			System.out.println("Error while updating cart: " + nfe.getMessage());
			nfe.printStackTrace();
		}

	}

	public void addCartItem(String strModelNo, String strUnitCost, String strQuantity, String strName)
	{
		double dblTotalCost = 0.0;
		double dblUnitCost = 0.0;
		int iQuantity = 0;
		CartItemBean cartItem = new CartItemBean();
		try
		{
			dblUnitCost = Double.parseDouble(strUnitCost);
			iQuantity = Integer.parseInt(strQuantity);
			if (iQuantity > 0)
			{
				dblTotalCost = dblUnitCost * iQuantity;
				cartItem.setPartNumber(strModelNo);
				cartItem.setStrName(strName);
				// cartItem.setModelDescription(strDescription);
				cartItem.setUnitCost(dblUnitCost);
				cartItem.setQuantity(iQuantity);
				cartItem.setTotalCost(dblTotalCost);
				allCartItems.add(cartItem);
				calculateOrderTotal();
			} 

		} catch (NumberFormatException nfe)
		{
			System.out.println("Error while parsing from String to primitive types: " + nfe.getMessage());
			nfe.printStackTrace();
		}
	}

	public void addCartItem(CartItemBean cartItem)
	{
		allCartItems.add(cartItem);
	}

	public CartItemBean getCartItem(int iItemIndex)
	{
		CartItemBean cartItem = null;
		if (allCartItems.size() > iItemIndex)
		{
			cartItem = (CartItemBean) allCartItems.get(iItemIndex);
		}
		return cartItem;
	}

	public List<CartItemBean> getCartItems()
	{
		
		return allCartItems;
	}

	public void setCartItems(ArrayList alCartItems)
	{
		this.allCartItems = alCartItems;
	}

	public double getOrderTotal()
	{
		return dlbOrderTotal;
	}

	public void setOrderTotal(double dblOrderTotal)
	{
		this.dlbOrderTotal = dblOrderTotal;
	}

	protected void calculateOrderTotal()
	{
		double dblTotal = 0;
		for (int counter = 0; counter < allCartItems.size(); counter++)
		{

			dblTotal += allCartItems.get(counter).getTotalCost();

		}
		this.setHstTotal(Math.round((dblTotal * hst*100.00))/100.00);
		if (dblTotal < 100)
		{
			this.setShippingCost(5);
			dblTotal = (dblTotal + shippingCost);
		} else
		{
			this.setShippingCost(0);
		}
		this.calculateTotal();
		
		setOrderTotal(Math.round(((dblTotal + this.getHstTotal()) * 1000.00))/ 1000.00);
	}

	public double getShippingCost()
	{
		return shippingCost;
	}

	public void setShippingCost(double shippingCost)
	{
		this.shippingCost = shippingCost;
	}

	public double getHst()
	{
		return hst;
	}

	public void setHst(double hst)
	{
		this.hst = hst;
	}

	public double getTotal()
	{
		return total;
	}

	public void setTotal(double total)
	{
		this.total = total;
	}

	public double getHstTotal()
	{
		return hstTotal;
	}

	public void setHstTotal(double hstTotal)
	{
		this.hstTotal = hstTotal;
	}

	public CustomerBean getCustomerBean()
	{
		return customerBean;
	}

	public void setCustomerBean(CustomerBean customerBean)
	{
		this.customerBean = customerBean;
	}
}
