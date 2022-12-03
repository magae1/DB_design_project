import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class MenuPrinter {
    private Connection db_connection;
    private final String[] clerkMenu = {
            "업무 확인", "일반 계좌 생성", "대출 계좌 생성",
            "고객 정보 조회", "고객 관리", "채무불이행자 조회",
            };

    MenuPrinter(Connection connection) {
        this.db_connection = connection;
    }

    public void printClerkMenu() {
        System.out.println("이용 가능한 업무들");
        for (int i = 0; i < clerkMenu.length; i++) {
            System.out.printf("[%d] %s\n", i + 1, clerkMenu[i]);
        }
    }

    public void printClerkTasks() {
        printClerkTasksNum();
        printClerkTasksList();
        printFooter();
    }

    public void printClerkCustomers() {
        printCustomersNum();
        printCustomersList();
        printFooter();
    }

    public void printClerkDefaulters() {

        printFooter();
    }

    private void printDefaulterNum() {
        String query = "select count(*) from customer";
        try {
            PreparedStatement preparedStatement = db_connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            System.out.printf("총 \"%d\"명의 고객이 있습니다.\n", resultSet.getInt(1));
        } catch (SQLException se) { se.printStackTrace(); }
    }


    private void printClerkTasksList() {
        String query = "select * from task";
        try {
            PreparedStatement preparedStatement = db_connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("업무ID" + "\t" + "고객ID" + "\t" + "업무유형" + "\t" + "요청일자");
            while (resultSet.next()) {
                System.out.printf("%s\t%s\t%s\t%s\n",
                        resultSet.getString(1), resultSet.getString(2),
                        resultSet.getString(3), resultSet.getString(4));
            }
        } catch (SQLException se) { se.printStackTrace(); }
    }

    private void printCustomersNum() {
        String query = "select count(*) from customer";
        try {
            PreparedStatement preparedStatement = db_connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            System.out.printf("총 \"%d\"명의 고객이 있습니다.\n", resultSet.getInt(1));
        } catch (SQLException se) { se.printStackTrace(); }
    }
    private void printCustomersList() {
        String query = "select * from customer";
        try {
            PreparedStatement preparedStatement = db_connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("고객ID" + "\t" + "비밀번호" + "\t" + "이름" + "\t" + "생일" + "\t" + "등급");
            while (resultSet.next()) {
                System.out.printf("%s\t%s\t%s\t%s\t%s\n",
                        resultSet.getString(1), resultSet.getString(2),
                        resultSet.getString(3), resultSet.getString(4),
                        resultSet.getString(5));
            }
        } catch (SQLException se) { se.printStackTrace(); }
    }
    private void printClerkTasksNum() {
        String query = "select count(*) from task";
        try {
            PreparedStatement preparedStatement = db_connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            System.out.printf("현재 총 \"%d\"건의 업무가 대기 중입니다.\n", resultSet.getInt(1));
        } catch (SQLException se) { se.printStackTrace(); }
    }
    private void printFooter() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("아무거나 입력해 뒤로 가기.");
        scanner.nextLine();
    }

    public String[] getClerkMenu() {
        return clerkMenu;
    }
}
