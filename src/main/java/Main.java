import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        DatabaseAuthInfo db_info = new DatabaseAuthInfo();

        if (!db_info.parse_auth_info("db.auth")) {
            System.out.println("Failed to connect with DB. Check \".auth\" file.");
            return;
        }

        String db_connection_url = String.format("jdbc:mysql://%s:%s/%s?user=%s&password=%s",
                db_info.getHost(),
                db_info.getPort(),
                db_info.getDatabase_name(),
                db_info.getUsername(),
                db_info.getPassword());
        Connection connection =  null;
        try {
            connection = DriverManager.getConnection(db_connection_url);
        } catch (SQLException se) { se.printStackTrace(); }



        //모드 선택 및 로그인 화면 진입
        EnteringPage enteringPage = new EnteringPage(connection);
        enteringPage.enteringPageLoop();

        String userType  = enteringPage.getChosenMode();
        String ID = enteringPage.getID();

        //메인 메뉴 진입
        MenuPrinter menuPrinter = new MenuPrinter(connection);
        while(true) {
            System.out.printf("\n%s\t%s\t(으)로 접속 중입니다.\n", userType, ID);
            Scanner scanner = new Scanner(System.in);
            menuPrinter.printClerkMenu();
            System.out.println("원하시는 업무를 선택하세요('q' 또는 'Q'로 프로그램 종료)");
            System.out.print("숫자로 입력: ");
            String selectedMenu = scanner.next();

            if (Objects.equals(selectedMenu, "q") || Objects.equals(selectedMenu, "Q")) {
                System.out.println("프로그램을 종료합니다.");
                break;
            }

            int selectedNum = Integer.parseInt(selectedMenu);
            System.out.printf("[%d] %s 으로 진입합니다.\n", selectedNum, menuPrinter.getClerkMenu()[selectedNum - 1]);
            switch (selectedNum) {
                case 1 -> menuPrinter.printClerkTasks();
                case 4 -> menuPrinter.printClerkCustomers();
            }
        }

    }
}