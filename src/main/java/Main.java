import java.util.Objects;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        DatabaseAuthInfo db_info = new DatabaseAuthInfo();

        if (!db_info.parse_auth_info("db.auth")) {
            System.out.println("Failed to connect with DB. Check \".auth\" file.");
            return;
        }
        DBConnector dbConnector = new DBConnector(db_info);
        MenuPrinter menuPrinter = new MenuPrinter();

        //모드 선택 및 로그인 화면 진입
        EnteringPage enteringPage = new EnteringPage(dbConnector);
        enteringPage.enteringPageLoop();

        String userType  = enteringPage.getChosenMode();
        String ID = enteringPage.getID();
        String PW = enteringPage.getPW();
        System.out.printf("%s\t%s\t%s\n", userType, ID, PW);

        //메인 메뉴 진입
        while(true) {
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
            System.out.println(selectedNum);
        }

    }
}