package srsjava.filter;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.lang.StringUtils;

import srsjava.bean.*;
import srsjava.dao.*;


public class ForeServletFilter implements Filter {
	
	@Override
    public void destroy() {
 
    }
	
	@Override
	public void doFilter(ServletRequest req,ServletResponse res,FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String contextPath = request.getServletContext().getContextPath();
		request.getServletContext().setAttribute("contextPath", contextPath);
		
		String[] noNeedAuthPage = new String[]{
				"login",
				"register"
        };

		String uri = request.getRequestURI();
		uri = StringUtils.remove(uri, contextPath);
		if(uri.startsWith("/fore") && !uri.startsWith("/foreServlet")){
			String method = StringUtils.substringAfterLast(uri,"/fore" );
            if(!Arrays.asList(noNeedAuthPage).contains(method)){
                
                if(null==(User) request.getSession().getAttribute("user")){
                	System.out.println((User) request.getSession().getAttribute("user"));
                    response.sendRedirect("index.jsp");
                    return;
                }
            }
			request.setAttribute("method", method);
			req.getRequestDispatcher("/foreServlet").forward(request, response);
			return;
		}
		if(uri.startsWith("/admin_")){
			System.out.println("����/admin�ɹ�");
			//��ȡCategory + Servlet
			String servletPath = StringUtils.substringBetween(uri,"_","_") + "Servlet";
			//��ȡlist
			String method = StringUtils.substringAfterLast(uri,"_");
			//��list���ݸ�servlet,���ö�Ӧ����
			System.out.println(method);
			request.setAttribute("method", method);
			//����ת����CategoryServlet
			System.out.println("/" + servletPath);
			req.getRequestDispatcher("/" + servletPath).forward(request, response);
			
			return;
			//�жϹ��������������ת/categoryServlet�͵���CategoryServlet�������
		}
		chain.doFilter(request, response);
		
		
		
		
		
	}
	@Override
	public void init (FilterConfig arg0) throws ServletException {
		System.out.println("First Filter init()");
		
	}
	
	
	
}
