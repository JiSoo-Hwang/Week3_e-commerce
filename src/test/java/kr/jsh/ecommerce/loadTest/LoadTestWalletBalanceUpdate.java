package kr.jsh.ecommerce.loadTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LoadTestWalletBalanceUpdate {
    private static final Logger logger = LoggerFactory.getLogger(LoadTestWalletBalanceUpdate.class);
    private static final String URL = "jdbc:mysql://localhost:3306/hhplus?characterEncoding=UTF-8&serverTimezone=UTC";
    private static final String USER = "application";
    private static final String PASSWORD = "application";
    private static final int BATCH_SIZE = 500;

    public static void main(String[] args) {
        new WalletUpdater().run();
    }

    static class WalletUpdater {
        private final Random random = new Random();

        public void run() {
            List<Long> customerIds = new ArrayList<>();
            String selectSQL = "SELECT customer_id FROM wallet";

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(selectSQL)) {

                while (rs.next()) {
                    customerIds.add(rs.getLong("customer_id"));
                }
                logger.info("총 {}개의 지갑 데이터가 조회되었습니다.", customerIds.size());

                String updateSQL = "UPDATE wallet SET balance = ? WHERE customer_id = ?";
                conn.setAutoCommit(false);

                try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                    int count = 0;
                    for (Long customerId : customerIds) {
                        // 100단위 금액으로 설정: 예를 들어 100원 ~ 1,000,000원 범위
                        int newBalance = ((random.nextInt(9999)) + 1) * 100;
                        pstmt.setInt(1, newBalance);
                        pstmt.setLong(2, customerId);
                        pstmt.addBatch();
                        count++;

                        if (count % BATCH_SIZE == 0) {
                            pstmt.executeBatch();
                            conn.commit();
                            logger.info("{}건의 지갑 업데이트 완료", count);
                        }
                    }
                    pstmt.executeBatch();
                    conn.commit();
                    logger.info("모든 지갑 데이터 업데이트 완료! 총 {}건의 업데이트가 이루어졌습니다.", count);
                }
            } catch (SQLException e) {
                logger.error("지갑 데이터 업데이트 중 오류 발생", e);
            }
        }
    }
}
