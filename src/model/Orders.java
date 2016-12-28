package model;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.stream.StreamResult;

import bean.FoodBean;
import bean.CartBean;
import bean.CartItemBean;
import order.CustomerType;
import order.ItemType;
import order.ItemsListType;
import order.OrderType;

public class Orders
{
	List<FoodBean> cart_items_final;
	private final String xslt = "../res/xsl/PO.xsl";

	public Orders()
	{
		
	}

	
	private OrderType createOrder(int orderid, Date now, CartBean cart) throws Exception {
		OrderType lw = new OrderType();
		// attributes of <order>
		lw.setId(new BigInteger("" + orderid));

		GregorianCalendar c = new GregorianCalendar();
		c.setTime(now);
		XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		lw.setSubmitted(date);

		// customer information
		CustomerType customer = new CustomerType();
		customer.setAccount(cart.getCustomerBean().getNumber() + "");
		customer.setName(cart.getCustomerBean().getName());
		lw.setCustomer(customer);

		// items
		ItemsListType items = new ItemsListType();
		List<ItemType> listItems = new ArrayList<ItemType>();
		for (CartItemBean tmp : cart.getCartItems()) {
			ItemType item = new ItemType();

			item.setNumber(tmp.getPartNumber());
			item.setName(tmp.getStrName());
			item.setPrice(tmp.getUnitCost());
			item.setQuantity(tmp.getQuantity());
			item.setExtended(tmp.getTotalCost());

			listItems.add(item);
		}
		items.setItem(listItems);
		lw.setItems(items);

		lw.setTotal(new Double("" + cart.getTotal()));
		lw.setShipping(new Double("" + cart.getShippingCost()));
		lw.setHst(new Double("" + cart.getHstTotal()));
		lw.setGrandTotal(new Double("" + cart.getOrderTotal()));
		return lw;
	}
	
	
	public String checkout(int orderNum, CartBean cart, String fileLocation) throws Exception {
		Date now = new Date();

		OrderType lw = createOrder(orderNum, now, cart);
		JAXBContext jc = JAXBContext.newInstance(lw.getClass());
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

		  //String writer for writing to file
		  StringWriter sw = new StringWriter();
		  sw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		  sw.write("<?xml-stylesheet type=\"text/xsl\" href=\"" + xslt + "\"?>\n");
		  String xmlTitle;
		  
		 // OutputStream os = new FileOutputStream( xmlTitle );
		  marshaller.marshal(lw, new StreamResult(sw));
		  
		  System.out.println(sw.toString()); // for debugging
		  FileWriter fw = new FileWriter(fileLocation);
		  fw.write(sw.toString());
		  fw.close();
		  return fileLocation;
	}

}
