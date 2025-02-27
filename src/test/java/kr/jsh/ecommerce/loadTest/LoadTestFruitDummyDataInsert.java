package kr.jsh.ecommerce.loadTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class LoadTestFruitDummyDataInsert {
    private static final Logger logger = LoggerFactory.getLogger(LoadTestFruitDummyDataInsert.class);
    private static final String URL = "jdbc:mysql://localhost:3306/hhplus?characterEncoding=UTF-8&serverTimezone=UTC";
    private static final String USER = "application";
    private static final String PASSWORD = "application";
    private static final int TOTAL_FRUITS = 1000;   // 총 1,000건의 과일 데이터 삽입
    private static final int BATCH_SIZE = 500;      // 500건씩 배치 처리

    public static void main(String[] args) {
        new FruitInsert().run();
    }

    static class FruitInsert {
        private final Random random = new Random();

        public void run() {
            String insertFruitSQL = "INSERT INTO fruit (fruit_name, fruit_stock, fruit_price, status) VALUES (?, ?, ?, ?)";

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                conn.setAutoCommit(false);

                try (PreparedStatement pstmt = conn.prepareStatement(insertFruitSQL)) {
                    for (int i = 0; i < TOTAL_FRUITS; i++) {
                        String fruitName = "과일" + i;
                        int stock = 10000 + random.nextInt(50000); // 10,000 ~ 59,999
                        int price = 1000 + random.nextInt(4000);     // 1,000 ~ 4,999
                        String status = "IN_STOCK";

                        pstmt.setString(1, fruitName);
                        pstmt.setInt(2, stock);
                        pstmt.setInt(3, price);
                        pstmt.setString(4, status);
                        pstmt.addBatch();

                        if ((i + 1) % BATCH_SIZE == 0) {
                            pstmt.executeBatch();
                            conn.commit();
                            logger.info("{}개의 과일 데이터가 삽입되었습니다.", (i + 1));
                        }
                    }

                    // 남은 데이터 처리
                    pstmt.executeBatch();
                    conn.commit();
                    logger.info("모든 과일 데이터 삽입 완료! 총 {}개의 과일 데이터가 추가되었습니다.", TOTAL_FRUITS);
                }
            } catch (SQLException e) {
                logger.error("과일 데이터 삽입 중 오류 발생", e);
            }
        }
    }
}

