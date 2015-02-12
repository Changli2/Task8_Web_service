package controller;
/*
 * Name- Team Maven(Team-8)
 * Date- 17 Jan 2015
 * Course- Task-7
*/
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Controller extends HttpServlet{

	/**
	 * version Id
	 */
	private static final long serialVersionUID = -1951358628804251994L;
	
	public void init() throws ServletException {
		
		Action.add(new HomePageAction());
		Action.add(new GeoMapAction());
		Action.add(new GoogleChartAction());
		Action.add(new WordCloudAction());
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String nextPage = performTheAction(req);
        sendToNextPage(nextPage,req,resp);
	}
	


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	
	private String performTheAction(HttpServletRequest req) {
		//HttpSession session     = req.getSession(true);
        String      servletPath = req.getServletPath();
        String      action = getActionName(servletPath);

        if (action.equals("register.do") || action.equals("login.do")) {
        	// Allow these actions without logging in
			return Action.perform(action,req);
        }
        
		return Action.perform(action,req);
	}
	

	private void sendToNextPage(String nextPage, HttpServletRequest req,
			HttpServletResponse resp) throws IOException, ServletException {
		if(nextPage ==  null ) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND,req.getServletPath());
			return;
		}
		if (nextPage.endsWith(".do")) {
			resp.sendRedirect(nextPage);
			return;
    	}
    	
    	if (nextPage.endsWith(".jsp")) {
	   		RequestDispatcher d = req.getRequestDispatcher("WEB-INF/" + nextPage);
	   		d.forward(req,resp);
	   		return;
    	}
    	
    	throw new ServletException(Controller.class.getName()+".sendToNextPage(\"" + nextPage + "\"): invalid extension.");
	}
	
	private String getActionName(String path) {
    	// We're guaranteed that the path will start with a slash
        int slash = path.lastIndexOf('/');
        return path.substring(slash+1);
    }
}
