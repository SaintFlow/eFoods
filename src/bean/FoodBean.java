package bean;

import java.security.InvalidParameterException;
import java.text.DecimalFormat;
import java.text.ParseException;

public class FoodBean
{

	private double price;
	private String name;
	private String number;
	private int catid;
	private int quantity;
	private double priceTotal;
	// private double check;

	/**
	 * @param price
	 * @param name
	 * @param number
	 * @param catid
	 */
	public FoodBean(double price, String name, String number, int catid) {
		super();
		this.price = price;
		this.name = name;
		this.number = number;
		this.catid = catid;
		this.quantity = 0; 
		this.priceTotal = 0.0;
	}
	
	public double formatDouble(double value)
	{
		DecimalFormat df=new DecimalFormat("#.00");
		String formate = df.format(value);
		double finalValue = 0.0;
		try
		{
			finalValue = (Double)df.parse(formate) ;
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return finalValue;
	}

	public void setPriceTotal(double priceTotal)
	{
		this.priceTotal = Math.round(priceTotal*100.0)/100.0;
	}

	/**
	 * @return the price
	 */
	public double getPrice()
	{

		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(double price)
	{
		this.price = Math.round(price * 100.0)/(100.0);
	}

	/**
	 * @return the name
	 */

	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the number
	 */
	public String getNumber()
	{
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(String number)
	{
		this.number = number;
	}

	/**
	 * @return the catid
	 */
	public int getCatid()
	{
		return catid;
	}

	/**
	 * @param catid
	 *            the catid to set
	 */
	public void setCatid(int catid)
	{
		this.catid = catid;
	}

	/**
	 * 
	 * @return the quantity of this item.
	 */
	public int getQuantity()
	{
		return this.quantity;
	}

	public void HardSetQuantity(int quantity)
	{
		this.quantity = quantity;
	}

	/**
	 * Set the quantity of the item ordered
	 */
	public void setQuantity(int quantity)
	{
		this.quantity += quantity;
	}

	public double getPriceTotal()
	{
		return priceTotal;
	}

	public double computePriceTotal()
	{
		this.priceTotal = this.getPrice() * this.quantity;
		return Math.round(this.priceTotal * 100.0)/100.0;
	}

	public String toString()
	{
		String item = "Price: " + this.getPrice() + " Name: " + this.getName() + " Number: " + this.getNumber()
		+ " CatId: " + this.catid + " Quantity: " + this.getQuantity() + " Total Price: "
		+ this.getPriceTotal();
		return item;
	}

}
