import java.sql.*;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

public class EnteringPage {
    private final String[] modes = {"은행원", "고객"};

    private String ID;
    private String PW;
    private int inputMode = 0;
    private Connection db_connection;
    EnteringPage(Connection connection) {
        this.db_connection = connection;
    }

    public void enteringPageLoop() {
        enterModePage();
        enterLogInPage();
    }

    private void enterLogInPage() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("로그인 하시오(회원가입하려면 's'입력).");

            System.out.print("아이디: ");
            this.ID = scanner.nextLine();
            if (Objects.equals(ID, "s")) {
                if (this.inputMode == 1) enterClerkSignUpPage();
                else enterCustomerSignUpPage();
                continue;
            }

            System.out.print("비밀번호: ");
            this.PW = scanner.nextLine();

            if (this.inputMode == 1) {
                if (checkClerkLogIn()) {
                    System.out.println("로그인에 성공했습니다.");
                    break;
                }
            } else {
                if (checkCustomerLogIn()) {
                    System.out.println("로그인에 성공했습니다.");
                    break;
                }
            }
            System.out.println("로그인 실패.");
        }
    }

    private void enterClerkSignUpPage() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("회원가입을 합니다.");
            System.out.print("아이디: ");
            this.ID = scanner.nextLine();

            System.out.print("비밀번호: ");
            this.PW = scanner.nextLine();

            System.out.print("이름: ");
            String name = scanner.nextLine();

            System.out.print("직위: ");
            String position = scanner.nextLine();

            if (checkClerkSignUp()) {
                insertClerkAccount(name, position);
                System.out.println("회원가입에 성공했습니다. 로그인 화면으로 돌아갑니다.");
                break;
            }
            System.out.println("회원가입 실패.");
        }
    }

    private void enterCustomerSignUpPage() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("회원가입을 합니다.");
            System.out.print("아이디: ");
            this.ID = scanner.nextLine();

            System.out.print("비밀번호: ");
            this.PW = scanner.nextLine();

            System.out.print("이름: ");
            String name = scanner.nextLine();

            System.out.print("생일: ");
            String birth = scanner.nextLine();

            if (checkCustomerSignUp()) {
                insertCustomerAccount(name, birth);
                System.out.println("회원가입에 성공했습니다. 로그인 화면으로 돌아갑니다.");
                break;
            }
            System.out.println("회원가입 실패.");
        }
    }



    private void enterModePage() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            printModeChoicePage();
            System.out.print("숫자로 입력: ");
            int input = scanner.nextInt();
            if (input >= 1 && input <= modes.length) {
                System.out.printf("[%d] %s 모드를 선택하셨습니다.\n", input, modes[input - 1]);
                inputMode = input;
                break;
            }
        }
    }


    private boolean checkClerkLogIn() {
        int count = -1;
        try {
            String query = "select count(*) from clerk where clerk_id = ? and password = ?";
            PreparedStatement preparedStatement = db_connection.prepareStatement(query);
            preparedStatement.setString(1, this.ID);
            preparedStatement.setString(2, this.PW);

            ResultSet results = preparedStatement.executeQuery();
            if(results.next()) {
                count = results.getInt(1);
            }
        } catch (SQLException se) { se.printStackTrace(); }

        return count != 0;
    }

    private boolean checkCustomerLogIn() {
        int count = -1;
        try {
            String query = "select count(*) from customer where customer_id = ? and password = ?";
            PreparedStatement preparedStatement = db_connection.prepareStatement(query);
            preparedStatement.setString(1, this.ID);
            preparedStatement.setString(2, this.PW);

            ResultSet results = preparedStatement.executeQuery();
            if(results.next()) {
                count = results.getInt(1);
            }
        } catch (SQLException se) { se.printStackTrace(); }

        return count != 0;
    }

    private boolean checkClerkSignUp() {
        int count = -1;
        try {
            String query = "select count(*) from clerk where clerk_id = ?";
            PreparedStatement preparedStatement = db_connection.prepareStatement(query);
            preparedStatement.setString(1, this.ID);

            ResultSet results = preparedStatement.executeQuery();
            if(results.next()) {
                count = results.getInt(1);
            }
        } catch (SQLException se) { se.printStackTrace(); }

        return count == 0;
    }

    private boolean checkCustomerSignUp() {
        int count = -1;
        try {
            String query = "select count(*) from customer where customer_id = ?";
            PreparedStatement preparedStatement = db_connection.prepareStatement(query);
            preparedStatement.setString(1, this.ID);

            ResultSet results = preparedStatement.executeQuery();
            if(results.next()) {
                count = results.getInt(1);
            }
        } catch (SQLException se) { se.printStackTrace(); }

        return count == 0;
    }

    private void insertClerkAccount(String name, String position) {
        try {
            String query = "insert into clerk values(?, ?, ?, ?)";
            PreparedStatement preparedStatement = db_connection.prepareStatement(query);
            preparedStatement.setString(1, this.ID);
            preparedStatement.setString(2, this.PW);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, position);

            preparedStatement.executeUpdate();
        } catch (SQLException se) { se.printStackTrace(); }
    }

    private void insertCustomerAccount(String name, String birth) {
        try {
            String query = "insert into customer values(?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = db_connection.prepareStatement(query);
            preparedStatement.setString(1, this.ID);
            preparedStatement.setString(2, this.PW);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, birth);
            preparedStatement.setString(5, "NORMAL");

            preparedStatement.executeUpdate();
        } catch (SQLException se) { se.printStackTrace(); }
    }

    private void printModeChoicePage() {
        System.out.println("모드를 선택하세요.");
        for (int i = 0; i < modes.length; i++) {
            System.out.printf("[%d] %s 모드\n", i + 1, modes[i]);
        }
    }

    public String getID() {
        return ID;
    }

    public String getPW() {
        return PW;
    }

    public int getInputMode() {
        return inputMode;
    }
    public String getChosenMode() {
        return modes[inputMode - 1];
    }
    public String[] getModes() {
        return modes;
    }
}
