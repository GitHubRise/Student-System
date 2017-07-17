package srsjava.servlet;
 
import java.lang.reflect.Method;
 
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import srsjava.dao.*;
import srsjava.util.Page;
 
public class BaseForeServlet extends HttpServlet{
 
    protected CourseDAO courseDAO = new CourseDAO();
    
    protected UserDAO userDAO = new UserDAO();
     
    public void service(HttpServletRequest request, HttpServletResponse response) {
        try {
             
            int start= 0;
            int count = 10;
            try {
                start = Integer.parseInt(request.getParameter("page.start"));
            } catch (Exception e) {
                 
            }
             
            try {
                count = Integer.parseInt(request.getParameter("page.count"));
            } catch (Exception e) {
            }
            System.out.println("����ת���ɹ�");
            Page page = new Page(start,count);
             
            String method = (String) request.getAttribute("method");
            
            System.out.println(method);
 
            Method m = this.getClass().getMethod(method, javax.servlet.http.HttpServletRequest.class,
                    javax.servlet.http.HttpServletResponse.class,Page.class);
             //m�Ƿ��� �ҳ����������
            //����request��method������������response,page ת�����ַ���
            String redirect = m.invoke(this,request, response,page).toString();
             System.out.println(redirect);
            if(redirect.startsWith("@"))
                response.sendRedirect(redirect.substring(1));
            else if(redirect.startsWith("%"))
                response.getWriter().print(redirect.substring(1));
            else
                request.getRequestDispatcher(redirect).forward(request, response);
             
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}