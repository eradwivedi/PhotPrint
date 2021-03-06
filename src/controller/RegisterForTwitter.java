package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.Model;
import model.UserDAO;

import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import databeans.User;
import formbeans.RegisterForm;

/*
 * Processes the parameters from the form in register.jsp.
 * If successful:
 *   (1) creates a new User bean
 *   (2) sets the "user" session attribute to the new User bean
 *   (3) redirects to view the originally requested photo.
 * If there was no photo originally requested to be viewed
 * (as specified by the "redirect" hidden form value),
 * just redirect to manage.do to allow the user to add some
 * photos.
 */
public class RegisterForTwitter extends Action {
	private FormBeanFactory<RegisterForm> formBeanFactory = FormBeanFactory.getInstance(RegisterForm.class);

	private UserDAO userDAO;
	
	public RegisterForTwitter(Model model) {
		userDAO = model.getUserDAO();
	}

	public String getName() { return "registerForTwitter.do"; }

    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);
        try{

        Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
	        // create a new customer
	        // Create the user bean
	        	User user;
	        	user= userDAO.read(twitter.getScreenName());
	        	if (user==null) {
	        		user = new User();
	        		user.setUserName(twitter.getScreenName());
	        		user.setFirstName("hello, ");
	        		user.setLastName(twitter.getScreenName());
	        		user.setPassword("twitter");
	        		userDAO.create(user);
	        	}
	    
			// Attach (this copy of) the user bean to the session
	        HttpSession session = request.getSession(false);
	        session.setAttribute("user",user);
	        
	        
			return "manage.do";
        } catch (RollbackException e) {
        	errors.add(e.getMessage());
        	return "register.jsp";
      
        } catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "manage.do";
    }
}
