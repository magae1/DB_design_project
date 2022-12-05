import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

public class ClerkPrinter {
    private Connection db_connection;
    private final String[] menu = {
        "업무 확인", "일반 계좌 생성", "대출 계좌 생성", "펀드 계좌 생성",
        "고객 정보 조회",
    };

    private String ID;

    ClerkPrinter(Connection connection, String clerk_id) {
        this.db_connection = connection;
        this.ID = clerk_id;
    }

    public void printMenu() {
        System.out.println("이용 가능한 업무들");
        for (int i = 0; i < menu.length; i++) {
            System.out.printf("[%d] %s\n", i + 1, menu[i]);
        }
    }

    public void printTasks() {
        printTasksNum();
        printTasksList();
        printFooter();
    }

    public void makeAccountPage() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("새 계좌를 만듭니다.");

            System.out.print("요청번호: ");
            String task_id_for_str = scanner.nextLine();
            int task_id = Integer.parseInt(task_id_for_str);

            System.out.print("계좌번호: ");
            String account_id = scanner.nextLine();
            System.out.print("고객ID: ");
            String customer_id = scanner.nextLine();
            System.out.print("계좌 비밀번호: ");
            String account_pw = scanner.nextLine();

            System.out.printf("요청번호: %s\n", task_id_for_str);
            System.out.printf("계좌번호: %s\n", account_id);
            System.out.printf("고객ID: %s\n", customer_id);
            System.out.printf("계좌 비밀번호: %s\n", account_pw);
            System.out.println("으로 새 계좌를 생성합니다.(Y/N)");
            String answer = scanner.nextLine();

            if (Objects.equals(answer, "Y") || Objects.equals(answer, "y")) {
                if (isExistTask(task_id) && isValidRqAccountID(account_id)) {
                    insertAccount(account_id, customer_id, account_pw);
                    moveToCompletedTask(task_id);
                    System.out.println("생성완료.");
                    break;
                }
            }
            System.out.println("다시 시도해주세요.");
        }
        printFooter();
    }

    public void makeLoanPage() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("새 대출계좌를 만듭니다.");

            System.out.print("요청번호: ");
            String task_id_for_str = scanner.nextLine();
            int task_id = Integer.parseInt(task_id_for_str);

            System.out.print("대출계좌번호: ");
            String loan_id_for_str = scanner.nextLine();
            int lona_id = Integer.parseInt(loan_id_for_str);
            System.out.print("고객ID: ");
            String customer_id = scanner.nextLine();
            System.out.print("대출액: ");
            String amount = scanner.nextLine();
            System.out.print("계좌 비밀번호: ");
            String loan_pw = scanner.nextLine();

            System.out.printf("요청번호: %s\n", task_id_for_str);
            System.out.printf("대출계좌번호: %s\n", loan_id_for_str);
            System.out.printf("고객ID: %s\n", customer_id);
            System.out.printf("대출액: %s\n", amount);
            System.out.printf("계좌 비밀번호: %s\n", loan_pw);
            System.out.println("으로 새 대출계좌를 생성합니다.(Y/N)");
            String answer = scanner.nextLine();

            if (Objects.equals(answer, "Y") || Objects.equals(answer, "y")) {
                if (isExistTask(task_id) && isValidRqLoanID(lona_id)) {
                    insertLoan(lona_id, customer_id, amount, loan_pw);
                    moveToCompletedTask(task_id);
                    System.out.println("생성완료.");
                    break;
                }
            }
            System.out.println("다시 시도해주세요.");
        }
        printFooter();
    }

    public void makeFundPage() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("새 펀드계좌를 만듭니다.");

            System.out.print("요청번호: ");
            String task_id_for_str = scanner.nextLine();
            int task_id = Integer.parseInt(task_id_for_str);

            System.out.print("계좌번호: ");
            String fund_id_for_str = scanner.nextLine();
            int fund_id = Integer.parseInt(fund_id_for_str);
            System.out.print("고객ID: ");
            String customer_id = scanner.nextLine();
            System.out.print("펀드 입급액: ");
            String amount = scanner.nextLine();

            System.out.printf("요청번호: %s\n", task_id_for_str);
            System.out.printf("계좌번호: %s\n", fund_id_for_str);
            System.out.printf("고객ID: %s\n", customer_id);
            System.out.printf("펀드 입급액: %s\n", amount);
            System.out.println("으로 새 펀드계좌를 생성합니다.(Y/N)");
            String answer = scanner.nextLine();

            if (Objects.equals(answer, "Y") || Objects.equals(answer, "y")) {
                if (isExistTask(task_id) && isValidRqFundID(fund_id)) {
                    insertFund(task_id, customer_id, amount);
                    moveToCompletedTask(task_id);
                    System.out.println("생성완료.");
                    break;
                }
            }
            System.out.println("다시 시도해주세요.");
        }
        printFooter();
    }

    public void printCustomers() {
        printCustomersNum();
        printCustomersList();
        printFooter();
    }

    private void printTasksList() {
        String query = "select * from task";
        try {
            PreparedStatement preparedStatement = db_connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.printf("%s\t%s\t%s\t%s\n", "업무ID", "고객ID", "업무 유형", "요청 일자");
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
            System.out.printf("%s\t%s\t%s\t%s\t%s\n", "고객ID", "비밀번호", "이름", "생일", "등급");
            while (resultSet.next()) {
                System.out.printf("%s\t%s\t%s\t%s\t%s\n",
                        resultSet.getString(1), resultSet.getString(2),
                        resultSet.getString(3), resultSet.getString(4),
                        resultSet.getString(5));
            }
        } catch (SQLException se) { se.printStackTrace(); }
    }
    private void printTasksNum() {
        String query = "select count(*) from task";
        try {
            PreparedStatement preparedStatement = db_connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            System.out.printf("현재 총 \"%d\"건의 업무가 대기 중입니다.\n", resultSet.getInt(1));
        } catch (SQLException se) { se.printStackTrace(); }
    }

    private void insertAccount(String account_id, String customer_id, String password) {
        String query = "insert into account values(?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = db_connection.prepareStatement(query);
            preparedStatement.setString(1, account_id);
            preparedStatement.setString(2, customer_id);
            preparedStatement.setString(3, new Date().toString());
            preparedStatement.setString(4, "1");
            preparedStatement.setInt(5, 0);
            preparedStatement.setString(6, password);

            preparedStatement.executeUpdate();
        } catch (SQLException se) { se.printStackTrace(); }
    }
    private void insertLoan(int loan_id, String customer_id, String loan_amount, String password) {
        String query = "insert into loan values(?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = db_connection.prepareStatement(query);
            preparedStatement.setInt(1, loan_id);
            preparedStatement.setString(2, customer_id);
            preparedStatement.setString(3, new Date().toString());
            preparedStatement.setString(4, loan_amount);
            preparedStatement.setString(5, "0");
            preparedStatement.setString(6, password);

            preparedStatement.executeUpdate();
        } catch (SQLException se) { se.printStackTrace(); }
    }

    private void insertFund(int fund_id, String customer_id, String fund_amount) {
        String query = "insert into fund values(?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = db_connection.prepareStatement(query);
            preparedStatement.setInt(1, fund_id);
            preparedStatement.setString(2, customer_id);
            preparedStatement.setString(3, new Date().toString());
            preparedStatement.setString(4, fund_amount);
            preparedStatement.setString(5, fund_amount);

            preparedStatement.executeUpdate();
        } catch (SQLException se) { se.printStackTrace(); }
    }

    private boolean isValidRqAccountID(String account_id) {
        int count = -1;
        String query = "select count(*) from account where account_id = ?";
        try {
            PreparedStatement preparedStatement = db_connection.prepareStatement(query);
            preparedStatement.setString(1, account_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            count = resultSet.getInt(1);
        } catch (SQLException se) { se.printStackTrace(); }
        return count == 0;
    }

    private boolean isValidRqFundID(int fund_id) {
        int count = -1;
        String query = "select count(*) from fund where fund_id = ?";
        try {
            PreparedStatement preparedStatement = db_connection.prepareStatement(query);
            preparedStatement.setInt(1, fund_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            count = resultSet.getInt(1);
        } catch (SQLException se) { se.printStackTrace(); }
        return count == 0;
    }

    private boolean isValidRqLoanID(int loan_id) {
        int count = -1;
        String query = "select count(*) from loan where loan_id = ?";
        try {
            PreparedStatement preparedStatement = db_connection.prepareStatement(query);
            preparedStatement.setInt(1, loan_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            count = resultSet.getInt(1);
        } catch (SQLException se) { se.printStackTrace(); }
        return count == 0;
    }


    private void printFooter() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("아무거나 입력해 뒤로 가기.");
        scanner.nextLine();
    }

    private void moveToCompletedTask(int task_id) {
        String removeQuery = "delete from task where task_id = ?";
        String insertQuery = "insert into completed_task values(?, ?, ?)";
        try {
            PreparedStatement preparedStatement = db_connection.prepareStatement(removeQuery);
            preparedStatement.setInt(1, task_id);
            preparedStatement.executeUpdate();

            preparedStatement = db_connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, task_id);
            preparedStatement.setString(2, ID);
            preparedStatement.setString(3, new Date().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException se) { se.printStackTrace(); }

    }
    private boolean isExistTask(int task_id) {
        int count = -1;
        String query = "select count(*) from task where task_id = ?";
        try {
            PreparedStatement preparedStatement = db_connection.prepareStatement(query);
            preparedStatement.setInt(1, task_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            count = resultSet.getInt(1);
        } catch (SQLException se) { se.printStackTrace(); }
        return count > 0;
    }

    public String[] getMenu() {
        return this.menu;
    }
}
