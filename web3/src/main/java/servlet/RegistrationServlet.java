package servlet;

import exception.DBException;
import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Map<String, Object> emptyGet = new HashMap<>();
        //emptyGet.put("message", "");
        response.getWriter().println(PageGenerator.getInstance().getPage("registrationPage.html", new HashMap<>()));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        Long money = Long.valueOf(request.getParameter("money"));
        BankClient bankClient = new BankClient(name, password, money);

        Map<String, Object> page = new HashMap<>();
        if ((name == null | password == null)) {
            page.put("message", "Enter login or pass");
            response.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", page));
        } else if (money < 0) {
            page.put("message", "Money > 0");
            response.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", page));
        }
        if (new BankClientService().addClient(bankClient)) {
            page.put("message", "Add client successful");
            response.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", page));
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            page.put("message", "Client not add");
            response.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", page));
        }
    }
}
