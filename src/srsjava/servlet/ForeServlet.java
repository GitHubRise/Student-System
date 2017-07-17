package srsjava.servlet;
 
import java.util.List;
 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.springframework.web.util.HtmlUtils;
 
import srsjava.bean.Course;
import srsjava.bean.User;
import srsjava.dao.CourseDAO;
import srsjava.dao.UserDAO;
import srsjava.util.Page;
 
public class ForeServlet extends BaseForeServlet {
    public String home(HttpServletRequest request, HttpServletResponse response, Page page) {
        List<Course> cs= new CourseDAO().list();
        request.setAttribute("cs", cs);
        User user = (User) request.getSession().getAttribute("user");
        System.out.println(user.getId());
        List<Course> choosen= new CourseDAO().listChoosen(user.getId());
        request.setAttribute("choosen", choosen);
        List<Course> notice= new CourseDAO().listNotice(user.getId());
        request.setAttribute("notice", notice);
        return "home.jsp";
    }
    
    
    
    public String search(HttpServletRequest request, HttpServletResponse response, Page page) {
    	String keyword = request.getParameter("keyword");
        List<Course> ps= new CourseDAO().search(keyword,0,20);
        request.setAttribute("cs",ps);
        return "home.jsp";
    }
    public String asearch(HttpServletRequest request, HttpServletResponse response, Page page) {
    	String keyword = request.getParameter("keyword");
        List<Course> ps= new CourseDAO().search(keyword,0,20);
        request.setAttribute("cs",ps);
        return "ahome.jsp";
    }
    
    public String change(HttpServletRequest request, HttpServletResponse response, Page page) {
    	String fpassword = request.getParameter("fpassword");
    	String password1 = request.getParameter("password1");
    	String password2 = request.getParameter("password2");
    	User user = (User) request.getSession().getAttribute("user");
    	if(!user.getPassword().equals(fpassword)){
    		request.setAttribute("msg", "ԭ��������޸�ʧ�ܣ�");
    		System.out.println("�����޸�ʧ��");

    		
    	}else if(! password1.equals(password2)){
    		request.setAttribute("msg", "�������벻һ�£��޸�ʧ�ܣ�");
    		System.out.println("�����޸�ʧ��");

    		
    	}else {
    		userDAO.setPassword(user.getId(),password2);
    		
    		request.setAttribute("msg", "�޸ĳɹ�");
    		System.out.println("�����޸ĳɹ�");
    	}
    	if(user.getLevel().equals("admin"))
        	return "foreahome"; 
        if(user.getLevel().equals("teacher"))
        	return "forethome";
        return "forehome";
    }
    
    public String thome(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user = (User) request.getSession().getAttribute("user");
        List<Course> tcs = new CourseDAO().teacherList(user.getId());
        List<Course> tnotice= new CourseDAO().listTNotice(user.getId());
        System.out.println(user.getId());
        System.out.println(tnotice);
        
        request.setAttribute("tnotice", tnotice);
        request.setAttribute("tcs", tcs);
        return "thome.jsp";
    }
    public String ahome(HttpServletRequest request, HttpServletResponse response, Page page) {
        List<Course> cs= new CourseDAO().alist();
        request.setAttribute("cs", cs);
        List<Course> anotice= new CourseDAO().listANotice();
        request.setAttribute("anotice", anotice);
        return "ahome.jsp";
    }
 
   
    public String logout(HttpServletRequest request, HttpServletResponse response,Page page) {
    	request.getSession().removeAttribute("user");
    	return "@index.jsp";
    }
    public String register(HttpServletRequest request, HttpServletResponse response,Page page) {
    	System.out.println("ִ��ע��");
    	String rname = request.getParameter("rname");
    	String rpassword = request.getParameter("rpassword");
    	String ruser = request.getParameter("ruser");
    	System.out.println(rname);
    	System.out.println(rpassword);
    	System.out.println(ruser);
    	//ת���ֹע��
    	rname = HtmlUtils.htmlEscape(rname);
    	ruser = HtmlUtils.htmlEscape(ruser);
    	System.out.println("��Ҫע����û���Ϊ��" + rname);
    	boolean exist = userDAO.isExist(rname);
    	if(exist){
            request.setAttribute("msg", "�˺��Ѿ���ʹ��,����ע��");
            return "index.jsp";  
        }
    	boolean existName = userDAO.isExistName(ruser);
    	if(exist){
            request.setAttribute("msg", "�û����Ѿ���ʹ��,����ע��");
            return "index.jsp";  
        }
    	if(rname.length() > 10){
    		request.setAttribute("msg", "�û������ܳ���ʮλ");
            return "index.jsp"; 
    	}
    	User user = new User();
        user.setId(rname);
        user.setPassword(rpassword);
        user.setName(ruser);
        System.out.println(user.getName());
        System.out.println(user.getPassword());
        userDAO.add(user);
        request.setAttribute("msg", "ע��ɹ������¼");
    	return "index.jsp";
    }
    
    public String login(HttpServletRequest request, HttpServletResponse response, Page page) {
        String name = request.getParameter("name");
        name = HtmlUtils.htmlEscape(name);
        String password = request.getParameter("password");     
        System.out.println("ִ��DAO��ѯ");
        User user = userDAO.get(name);
        //���û����᷵��null���󣬲���ֱ�ӱȽ�getPassword()
        System.out.println("�û��������룺" + password);
        if(null==user){
			request.setAttribute("msg", "�˺��������");
			System.out.println("�˺Ų�����,��ע��");
			request.setAttribute("id", name);
			return "index.jsp";	
		}else if(! user.getPassword().equals(password) ){
			request.setAttribute("msg", "�˺��������");
			System.out.println("�˺��������");
			request.setAttribute("id", name);
			return "index.jsp";	
		}
        
        request.getSession().setAttribute("user", user);
        System.out.println(user.getLevel());
        if(user.getLevel().equals("admin"))
        	return "foreahome"; 
        if(user.getLevel().equals("teacher"))
        	return "forethome";
        return "forehome";
    }   
}