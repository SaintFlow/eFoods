package adhoc;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

//Specialized Http response with additional methods. Replaces the original response in chain(request, response).
//Must be made exactly like this.
public class MyResponse extends HttpServletResponseWrapper
{
	
	private static final Object BUFFER_SIZE = null;
	private PrintWriter writer;
	private StringWriter sw = new StringWriter();
	
	public MyResponse(HttpServletResponse response) throws IOException
	{
		super(response);
		// TODO Auto-generated constructor stub
		
	}
	
	public PrintWriter getWriter()
	{
		return new PrintWriter(sw);
	}
	
	public String getContent() throws IOException
	{
		return sw.toString();
	}

	
	
}
