import java.sql.ResultSet;
import java.sql.SQLException;

public class MenuPrinter {

    private final String[] clerkMenu = {
            "업무 확인", "일반 계좌 생성", "대출 계좌 생성",
            "고객 정보 조회", "고객 관리", "채무불이행자 조회",
            };

    MenuPrinter() {}


    public void printClerkMenu() {
        System.out.println("이용 가능한 업무들");
        for (int i = 0; i < clerkMenu.length; i++) {
            System.out.printf("[%d] %s\n", i + 1, clerkMenu[i]);
        }
    }

    public void printClerckTaskList(int listCount, ResultSet resultSet) throws SQLException {
        System.out.printf("현재 총 %d건의 업무가 대기 중입니다.\n", listCount);
        System.out.println("업무ID" + "\t" + "고객ID" + "\t" + "업무유형");
        while(resultSet.next()) {
            for (int i = 1; i <= 2; i++) {
                System.out.print(resultSet.getString(i) + "\t");
            }
            System.out.println();
        }
    }


    public String[] getClerkMenu() {
        return clerkMenu;
    }
}
