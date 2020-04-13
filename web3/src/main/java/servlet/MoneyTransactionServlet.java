package servlet;

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

public class MoneyTransactionServlet extends HttpServlet {

    BankClientService bankClientService = new BankClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("moneyTransactionPage.html", new HashMap<>()));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        String senderName = request.getParameter("senderName");
        String senderPass = request.getParameter("senderPass");
        Long count = Long.valueOf(request.getParameter("count"));
        //count++;
        String nameTo = request.getParameter("nameTo");

        BankClient bankClient = new BankClient(senderName, senderPass, count);
        Map<String, Object> page = new HashMap<>();

        if (senderName != null && senderPass != null && count != null && nameTo != null) {
            if (bankClientService.sendMoneyToClient(bankClient, nameTo, count) & count > 0) {
                page.put("message", "The transaction was successful");
                response.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", page));
                response.setStatus(HttpServletResponse.SC_OK);
            }
            else{
                page.put("message", "transaction rejected");
                response.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", page));
            }
        }
        else {
            page.put("message", "Enter password login or money");
            response.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", page));
        }

        //response.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", page));
//
//        //bankClientService.sendMoneyToClient();

//        if ((senderName == null | senderPass == null | nameTo == null) ||
//                (senderName.isEmpty() | senderPass.isEmpty() | nameTo.isEmpty())) {
//            page.put("message", "Field Sender Name or Sender Password or Name To can not be empty!");
//            response.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", page));
//        } else if (count < 0) {
//            page.put("message", "Field Count can not be less than zero!");
//            response.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", page));
//        }
//        if (new BankClientService().sendMoneyToClient(bankClient, nameTo, count)) {
//            page.put("message", "The transaction was successful");
//            response.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", page));
//            response.setStatus(HttpServletResponse.SC_OK);
//        } else {
//            page.put("message", "transaction rejected");
//            response.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", page));
//        }
    }
}
