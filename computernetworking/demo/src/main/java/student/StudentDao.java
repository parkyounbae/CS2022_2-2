package student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentDao {
    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;

    public StudentDao ()
    {
        try{
            String dbURL = "jdbc:mysql://localhost:3307/bbs?serverTimezone=Asia/Seoul";
            String dbID = "root";
            String dbPassword = "";
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public int login(String id_from_web, String password_from_web)
    {
        String SQL = "SELECT password FROM Student WHERE student_id = ?";
        try{
            pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, id_from_web);
            rs = pstmt.executeQuery();
            if(rs.next())
            {
                if(rs.getString(1).equals(password_from_web))
                {
                    return 1; //로그인 성공
                }
                else
                {
                    return 0; //비밀번호 불일치
                }
            }
            return -1; // 아이디가 없음
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -2; // 데이터베이스 오류
    }


}
