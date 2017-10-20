/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mk.classes;

import com.mk.beans.UserLogin;
import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 

/*
 * THIS IS NOT USED CLASS KEPT FOR FURTHUR USE.
 */



//@WebFilter(filterName = "AuthFilter", urlPatterns = {"*.xhtml"})
public class AuthFilter implements Filter {
     
    public AuthFilter() {
    }
 
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
         
    }
 
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
         try {
 
            // check whether session variable is set
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;
            HttpSession ses = req.getSession(false);
            String reqURI = req.getRequestURI();
            System.out.println("URI:" + reqURI);
            //UserLogin userLogin = (UserLogin) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userLogin");
            UserLogin userLogin = null;//(UserLogin) ses.getAttribute( "userLogin" );
            System.out.println("SESSION:" + ses);
            UserLogin ul = null;
            if (ses!= null)
            {
                ul = (UserLogin) ses.getAttribute("userLogin");
                userLogin = (UserLogin) ses.getAttribute( "userLogin" );
                System.out.println("BEAN:" + userLogin);
            }
            /* if ( reqURI.indexOf("/MKRLogin.xhtml") >= 0 || (ses != null && ses.getAttribute("username") != null)
                                       || reqURI.indexOf("/public/") >= 0 || reqURI.contains("javax.faces.resource") ) */
            if ( reqURI.indexOf("/MKRLogin.xhtml") >= 0 
                    || reqURI.contains("javax.faces.resource")
                    || (ul != null && ul.isUserLogged())
                    )
                   chain.doFilter(request, response);
            else
            {
                res.sendRedirect(req.getContextPath() + "/faces/MKRLogin.xhtml");  // Anonymous user. Redirect to login page
            }
         }
     catch(Throwable t) {
         System.out.println( t.getMessage());
     }
    } //doFilter
 
    @Override
    public void destroy() {
         
    }
}