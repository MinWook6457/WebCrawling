package crolling;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class db {
	private static final String jdbc ="jdbc:mariadb://localhost:3306/test_minwook"; // jdbc 드라이버 : db와 통신하는 자바 API
    private static final String username = "minwook";
    private static final String pw = "@alsdnr6457";
    
    public static Connection getConnection() throws SQLException{ // 데이터베이스와의 연결을 수립
    	System.out.println("===== 데이터베이스 연결 =====");
    	return DriverManager.getConnection(jdbc,username,pw); // Connection 객체 반환
    }
    
    public void insertArticle(info article) {
    	String sql = "INSERT INTO t_article_m (title, contents, publisher, upload, imgUrl) VALUES (?, ?, ?, ?, ?)";
   
    	try (Connection connection = getConnection(); // sql 연결
    			/* PreparedStatement : 동적으로 값을 바인딩하여 sql 인젝션 공격 방지*/
               PreparedStatement statement = connection.prepareStatement(sql)) {
    		   System.out.println("===== sql 동작 시작 =====");
    		   
               statement.setString(1, article.getTitle()); // 1번 인덱스로 제목 저장
               statement.setString(2, article.getContent()); // 2번 인덱스로 내용 저장
               statement.setString(3, article.getPublisher()); // 3번 인덱스로 출처 저장
               statement.setString(4, article.getUpload()); // 4번 인덱스로 업로드 시기 저장
               statement.setString(5, article.getUrl()); // 5번 인덱스로 url 저장
              
               statement.executeUpdate();  // insert , update, delete 등 쿼리 실행
           } catch (SQLException e) {
               e.printStackTrace();
           }
    }
}
