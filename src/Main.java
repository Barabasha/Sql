import java.sql.*;
import java.util.List;
import com.google.common.collect.ImmutableList;

public class Main {
    private static List<String> students = ImmutableList.of("Ivanov", "Petrov", "Sidorov", "Pupkin");
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","Qwerty00")) {
            Statement statement = connection.createStatement();
            statement.execute("create table students (id int primary key auto_increment, firstname varchar(50), lastname varchar(50));");
            PreparedStatement preparedStatement = connection.prepareStatement("insert into students (firstname) values (?);");
            for (String student : students){
                preparedStatement.setString(1,student);
                preparedStatement.executeUpdate();
            }

            statement.execute("create table progress (student_id int, foreign key (student_id) references students (id), physics int, math int, programming int);");
            statement.execute("insert into progress (student_id, physics, math, programming) values (1,5,3,4);");
            statement.execute("insert into progress (student_id, physics, math, programming) values (2,4,5,3);");
            statement.execute("insert into progress (student_id, physics, math, programming) values (3,4,3,4);");
            statement.execute("insert into progress (student_id, physics, math, programming) values (4,5,3,4);");

            ResultSet resultSet = statement.executeQuery("select * from students where id in  (select student_id from progress where math = 3);");
            System.out.println("Next students have the mark=3 in math:");
            while (resultSet.next()){
                System.out.println(resultSet.getString("firstname"));
            }
            System.out.println();

            ResultSet resultSet2 = statement.executeQuery("select * from students where id in  (select student_id from progress where physics > programming);");
            System.out.println("Next students have the mark in physics is more than the mark in programming:");
            while (resultSet2.next()){
                System.out.println(resultSet2.getString("firstname"));
            }
            System.out.println();

            statement.close();

        } catch (SQLException e) {
             e.printStackTrace();
        }
    }
}

