package kr.jsh.ecommerce.loadTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LoadTestFruitPriceUpdate {
    private static final Logger logger = LoggerFactory.getLogger(LoadTestFruitPriceUpdate.class);
    private static final String URL = "jdbc:mysql://localhost:3306/hhplus?characterEncoding=UTF-8&serverTimezone=UTC";
    private static final String USER = "application";
    private static final String PASSWORD = "application";
    private static final int BATCH_SIZE = 500;

    public static void main(String[] args) {
        new FruitPriceUpdater().run();
    }

    static class FruitPriceUpdater {
        private final Random random = new Random();

        public void run() {
            List<Long> fruitIds = new ArrayList<>();
            String selectSQL = "SELECT fruit_id FROM fruit";  // fruit_id: 과일 테이블의 기본키 컬럼명

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(selectSQL)) {

                while (rs.next()) {
                    fruitIds.add(rs.getLong("fruit_id"));
                }
                logger.info("총 {}개의 과일 데이터가 조회되었습니다.", fruitIds.size());

                String updateSQL = "UPDATE fruit SET fruit_price = ? WHERE fruit_id = ?";
                conn.setAutoCommit(false);

                try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                    int count = 0;
                    for (Long fruitId : fruitIds) {
                        // 100단위 가격 생성: 예를 들어 1000원부터 4900원 사이 (10~49 * 100)
                        int newPrice = (random.nextInt(40) + 10) * 100;
                        pstmt.setInt(1, newPrice);
                        pstmt.setLong(2, fruitId);
                        pstmt.addBatch();
                        count++;

                        if (count % BATCH_SIZE == 0) {
                            pstmt.executeBatch();
                            conn.commit();
                            logger.info("{}건의 과일 가격 업데이트 완료", count);
                        }
                    }
                    pstmt.executeBatch();
                    conn.commit();
                    logger.info("모든 과일 가격 업데이트 완료! 총 {}건의 업데이트가 이루어졌습니다.", count);
                }
            } catch (SQLException e) {
                logger.error("과일 가격 업데이트 중 오류 발생", e);
            }
        }
    }
}