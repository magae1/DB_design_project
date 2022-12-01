import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

public class EnteringPage {
    private final String[] modes = {"은행원", "고객"};

    private final String[][] queryAttritues = {
            {"clerk", "clerk_id", "clerk_pw"},
            {"customer", "customer_id", "PW"}};
    private String ID;
    private String PW;
    private int inputMode = 0;

    private DBConnector dbConnector;
    EnteringPage(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public void enteringPageLoop() {
        enterModePage();
        enterLogInPage();
    }


    private void enterLogInPage() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("로그인 하시오(회원가입하려면 's'입력).");
            System.out.print("ID: ");
            this.ID = scanner.nextLine();
            if (Objects.equals(ID, "s")) {
                enterSignUpPage();
                continue;
            }

            System.out.print("PW: ");
            this.PW = scanner.nextLine();

            if (isIdPwExist()) {
                System.out.println("로그인에 성공했습니다.");
                break;
            }
            System.out.println("로그인 실패.");
        }
    }

    private void enterSignUpPage() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            //TODO clerk일 때와 customer일 때 분리 필요
            System.out.println("회원가입을 합니다.");
            System.out.print("ID: ");
            this.ID = scanner.nextLine();
            System.out.println();
            System.out.print("PW: ");
            this.PW = scanner.nextLine();

            if (!isIdExist()) {
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

    private boolean isIdExist() {
        String query = "SELECT COUNT(*) "
                + "FROM " + queryAttritues[inputMode - 1][0]
                + "WHERE " + queryAttritues[inputMode - 1][1] + "=" + this.ID;

        ResultSet results = dbConnector.getResultSet(query);
        int count = -1;
        try {
            if(results.next()) {
                count = results.getInt(1);
            }
            System.out.println(count);
        } catch (SQLException se) { se.printStackTrace(); }

        return count != 0;
    }

    private boolean isIdPwExist() {
        String query = "SELECT COUNT(*) "
            + "FROM " + queryAttritues[inputMode - 1][0]
            + "WHERE " + queryAttritues[inputMode - 1][1] + "=" + this.ID
            + "AND " + queryAttritues[inputMode - 1][2] + "=" + this.PW;

        ResultSet results = dbConnector.getResultSet(query);
        int count = -1;
        try {
            if(results.next()) {
                count = results.getInt(1);
            }
            System.out.println(count);
        } catch (SQLException se) { se.printStackTrace(); }

        return count != 0;
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
