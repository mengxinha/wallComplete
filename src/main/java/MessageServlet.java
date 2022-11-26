import com.fasterxml.jackson.databind.ObjectMapper;

import javax.rmi.CORBA.ValueHandler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageServlet extends HttpServlet {

    private ObjectMapper objectMapper = new ObjectMapper();

    private List<Message> messageList = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Message message = objectMapper.readValue(req.getInputStream(),Message.class);
        messageList.add(message);
        resp.setStatus(200);
        System.out.println("提交数据成功");
    }
}
