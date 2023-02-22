import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/message")
public class MessageServlet extends HttpServlet {

    private ObjectMapper objectMapper = new ObjectMapper();

    //在用数据库之前，是把数据存放到messageLis中
    //private List<Message> messageList = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf8");
        //resp.getWriter().write(objectMapper.writeValueAsString(messageList));
        List<Message> messageList = null;
        try {
            messageList = load();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        resp.getWriter().write(objectMapper.writeValueAsString(messageList));

    }

     protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Message message = objectMapper.readValue(req.getInputStream(),Message.class);
        //messageList.add(message);
         try {
             save(message);
         } catch (SQLException e) {
             e.printStackTrace();
         }
         resp.setStatus(200);

        System.out.println("提交数据成功: from="+message.getFrom()+
                ",to="+message.getTo() + ",message="+message.getMessage());
    }

    //从数据库获取数据
    private List<Message> load() throws SQLException {
        //1，先有一个数据源
        DataSource dataSource = new MysqlDataSource();
        ((MysqlDataSource)dataSource).setUrl("jdbc:mysql://127.0.0.1:3306/java105?characteEncoding=utf8&useSSL=false");
        ((MysqlDataSource)dataSource).setUser("root");

        //2，建立连接
        Connection connection =dataSource.getConnection();

        //3，构造SQL 语句
        String sql = "select * from message";
        PreparedStatement statement = connection.prepareStatement(sql);


        //4，执行sql 进行插入操作
        ResultSet resultSet = statement.executeQuery();

        //5，遍历结果集合
        List<Message> messageList = new ArrayList<>();
        while (resultSet.next()){
            Message message = new Message();
            message.setFrom(resultSet.getString("from"));
            message.setTo(resultSet.getString("to"));
            message.setMessage(resultSet.getString("message"));
            messageList.add(message);
        }

        //6，关闭连接
        statement.close();
        connection.close();
        return messageList;


    }

    //把数据保存到数据库
    private void save(Message message) throws SQLException {

        //1，先有一个数据源
        DataSource dataSource = new MysqlDataSource();
        ((MysqlDataSource)dataSource).setUrl("jdbc:mysql://127.0.0.1:3306/java105?characteEncoding=utf8&useSSL");
        ((MysqlDataSource)dataSource).setUser("root");

        //2，建立连接
        Connection connection =dataSource.getConnection();

        //3，构造SQL 语句
        String sql = "insert into message Values(?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,message.getFrom());
        statement.setString(2,message.getTo());
        statement.setString(3,message.getMessage());

        //4，执行sql 进行插入操作
        int ret = statement.executeUpdate();
        System.out.println("ret = "+ret);

        //5，关闭连接
        statement.close();
        connection.close();




    }
}
