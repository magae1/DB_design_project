import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;
public class CustomerPrinter {
    private Connection db_connection;
    private final String[] menu = {
            "내 계좌 조회", "일반 계좌 생성 요청", "대출 계좌 생성 요청", "펀드 계좌 생성 요청",
    };
    private String ID;

    CustomerPrinter(Connection connection, String customer_id) {
        this.db_connection = connection;
        this.ID = customer_id;
    }
    public void printMenu() {
        System.out.println("이용 가능한 업무들");
        for (int i = 0; i < menu.length; i++) {
            System.out.printf("[%d] %s\n", i + 1, menu[i]);
        }
    }

    public void printMyAccounts() {
        printAccountsNum();
        printAccountsList();
        printFooter();
    }

    public void accountRequestPage() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("새 계좌를 만듭니다.");

            System.out.print("요청번호: ");
            String task_id_for_str = scanner.nextLine();
            int task_id = Integer.parseInt(task_id_for_str);

            System.out.print("계좌번호: ");
            String account_id = scanner.nextLine();

            System.out.print("계좌 비밀번호: ");
            String account_pw = scanner.nextLine();

            System.out.printf("요청번호: %s\n", task_id_for_str);
            System.out.printf("계좌번호: %s\n", account_id);
            System.out.printf("계좌 비밀번호: %s\n", account_pw);
            System.out.println("으로 새 계좌 생성을 요청합니다.(Y/N)");
            String answer = scanner.nextLine();

            if (Objects.equals(answer, "Y") || Objects.equals(answer, "y")) {
                if (isValidTaskID(task_id)) {
                    insertIntoTask(task_id, "ACCOUNT");
                    insertIntoAccountRq(account_id, account_pw);
                    System.out.println("요청완료.");
                    break;
                }
            }
            System.out.println("다시 시도해주세요.");
        }
        printFooter();
    }

    public void loanRequestPage() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("새 대출을 신청합니다.");

            System.out.print("요청번호: ");
            String task_id_for_str = scanner.nextLine();
            int task_id = Integer.parseInt(task_id_for_str);

            System.out.print("대출 계좌번호: ");
            String loan_id_for_str = scanner.nextLine();
            int loan_id = Integer.parseInt(loan_id_for_str);

            System.out.print("대출액: ");
            String amount = scanner.nextLine();

            System.out.printf("요청번호: %s\n", task_id_for_str);
            System.out.printf("대출 계좌번호: %s\n", loan_id_for_str);
            System.out.printf("대출액: %s\n", amount);
            System.out.println("으로 새 대출을 요청합니다.(Y/N)");
            String answer = scanner.nextLine();

            if (Objects.equals(answer, "Y") || Objects.equals(answer, "y")) {
                if (isValidTaskID(task_id)) {
                    insertIntoTask(task_id, "LOAN");
                    insertIntoLoanRq(loan_id, amount);
                    System.out.println("요청완료.");
                    break;
                }
            }
            System.out.println("다시 시도해주세요.");
        }
        printFooter();
    }

    public void fundRequestPage() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("새 펀드계좌를 만듭니다.");

            System.out.print("요청번호: ");
            String task_id_for_str = scanner.nextLine();
            int task_id = Integer.parseInt(task_id_for_str);

            System.out.print("펀드 계좌번호: ");
            String fund_id_for_str = scanner.nextLine();
            int fund_id = Integer.parseInt(fund_id_for_str);

            System.out.print("펀드 입급액: ");
            String amount = scanner.nextLine();

            System.out.printf("요청번호: %s\n", task_id_for_str);
            System.out.printf("펀드 계좌번호: %s\n", fund_id_for_str);
            System.out.printf("펀드 입급액: %s\n", amount);
            System.out.println("으로 새 펀드를 요청합니다.(Y/N)");
            String answer = scanner.nextLine();

            if (Objects.equals(answer, "Y") || Objects.equals(answer, "y")) {
                if (isValidTaskID(task_id)) {
                    insertIntoTask(task_id, "FUND");
                    insertIntoFundRq(fund_id, amount);
                    System.out.println("요청완료.");
                    break;
                }
            }
            System.out.println("다시 시도해주세요.");
        }
        printFooter();
    }

    private void printAccountsList() {
        String query =
                "select ac.account_id, ac.date_created, ai.interest_rates, ac.password\n" +
                "from account as ac\n" +
                "JOIN account_interest as ai on ac.account_interest_id = ai.account_interest_id\n" +
                "where ac.account_id = ?";
        try {
            PreparedStatement preparedStatement = db_connection.prepareStatement(query);
            preparedStatement.setString(1, this.ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.printf("%s\t%s\t%s\t%s\n", "계좌번호", "생성일자", "이자율", "비밀번호");
            while (resultSet.next()) {
                System.out.printf("%s\t%s\t%s\t%s\n",
                        resultSet.getString(1), resultSet.getString(2),
                        resultSet.getString(3), resultSet.getString(4));
            }
        } catch (SQLException se) { se.printStackTrace(); }
    }
    private void printAccountsNum() {
        String query = "select count(*) from account where customer_id = ?";
        try {
            PreparedStatement preparedStatement = db_connection.prepareStatement(query);
            preparedStatement.setString(1, this.ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            System.out.printf("총 \"%d\"개의 계좌가 있습니다.\n", resultSet.getInt(1));
        } catch (SQLException se) { se.printStackTrace(); }
    }

    private void insertIntoTask(int task_id, String tasktype) {
        String query = "insert into task values(?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = db_connection.prepareStatement(query);
            preparedStatement.setInt(1, task_id);
            preparedStatement.setString(2, this.ID);
            preparedStatement.setString(3, tasktype);
            preparedStatement.setString(4, new Date().toString());

            preparedStatement.executeUpdate();
        } catch (SQLException se) { se.printStackTrace(); }
    }

    private void insertIntoAccountRq(String account_id, String account_pw) {
        String query = "insert into account_request values(?, ?, ?)";
        try {
            PreparedStatement preparedStatement = db_connection.prepareStatement(query);
            preparedStatement.setString(1, account_id);
            preparedStatement.setString(2, this.ID);
            preparedStatement.setString(3, account_pw);

            preparedStatement.executeUpdate();
        } catch (SQLException se) { se.printStackTrace(); }
    }

    private void insertIntoFundRq(int fund_id, String amount) {
        String query = "insert into fund_request values(?, ?, ?)";
        try {
            PreparedStatement preparedStatement = db_connection.prepareStatement(query);
            preparedStatement.setInt(1, fund_id);
            preparedStatement.setString(2, this.ID);
            preparedStatement.setString(3, amount);

            preparedStatement.executeUpdate();
        } catch (SQLException se) { se.printStackTrace(); }
    }
    private void insertIntoLoanRq(int loan_id, String amount) {
        String query = "insert into loan_request values(?, ?, ?)";
        try {
            PreparedStatement preparedStatement = db_connection.prepareStatement(query);
            preparedStatement.setInt(1, loan_id);
            preparedStatement.setString(2, this.ID);
            preparedStatement.setString(3, amount);

            preparedStatement.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
    private void printFooter() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("아무거나 입력해 뒤로 가기.");
        scanner.nextLine();
    }

    private boolean isValidTaskID(int task_id) {
        int count = -1;
        String query = "select count(*) from task where task_id = ?";
        try {
            PreparedStatement preparedStatement = db_connection.prepareStatement(query);
            preparedStatement.setInt(1, task_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            count = resultSet.getInt(1);
        } catch (SQLException se) { se.printStackTrace(); }
        return count == 0;

    }

    public String[] getMenu() {
        return this.menu;
    }
}
