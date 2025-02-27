package kr.jsh.ecommerce.loadTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Random;

public class LoadTestDummyDataInsert {
    private static final Logger logger = LoggerFactory.getLogger(LoadTestDummyDataInsert.class);
    private static final String URL = "jdbc:mysql://localhost:3306/hhplus?characterEncoding=UTF-8&serverTimezone=UTC";
    private static final String USER = "application";
    private static final String PASSWORD = "application";
    private static final int TOTAL_RECORDS = 10_000;  // 1만 건 삽입
    private static final int BATCH_SIZE = 500; // 500건씩 배치 처리

    public static void main(String[] args) {
        new CustomerInsert().run();
    }

    static class CustomerInsert {
        private final Random random = new Random();

        public void run() {
            String insertCustomerSQL = "INSERT INTO customer (customer_name, customer_address, customer_phone) VALUES (?, ?, ?)";
            String insertWalletSQL = "INSERT INTO wallet (balance, customer_id,version) VALUES (?, ?,0)";

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                conn.setAutoCommit(false);

                try (PreparedStatement pstmtCustomer = conn.prepareStatement(insertCustomerSQL, PreparedStatement.RETURN_GENERATED_KEYS);
                     PreparedStatement pstmtWallet = conn.prepareStatement(insertWalletSQL)) {

                    // 고객 데이터 대량 삽입
                    for (int i = 0; i < TOTAL_RECORDS; i++) {
                        String name = "고객" + i;
                        String address = "도시 " + (random.nextInt(100) + 1);
                        String phone = "010-" + (random.nextInt(9000) + 1000) + "-" + (random.nextInt(9000) + 1000);

                        pstmtCustomer.setString(1, name);
                        pstmtCustomer.setString(2, address);
                        pstmtCustomer.setString(3, phone);
                        pstmtCustomer.addBatch();

                        // 500건 단위로 배치 실행
                        if ((i + 1) % BATCH_SIZE == 0) {
                            pstmtCustomer.executeBatch();
                            conn.commit();
                            logger.info("{}명의 고객 데이터가 삽입되었습니다.", (i + 1));

                            // 삽입된 고객의 ID를 가져와서 Wallet 데이터 생성
                            try (ResultSet generatedKeys = pstmtCustomer.getGeneratedKeys()) {
                                while (generatedKeys.next()) {
                                    long customerId = generatedKeys.getLong(1);
                                    int balance = random.nextInt(1_000_000) + 1000; // 1,000원 ~ 1,000,000원

                                    pstmtWallet.setInt(1, balance);
                                    pstmtWallet.setLong(2, customerId);
                                    pstmtWallet.addBatch();
                                }
                            }
                            pstmtWallet.executeBatch();
                            conn.commit();
                        }
                    }

                    // 남은 데이터 처리 (배치 사이즈로 딱 떨어지지 않은 경우)
                    pstmtCustomer.executeBatch();
                    pstmtWallet.executeBatch();
                    conn.commit();

                    logger.info("모든 데이터 삽입이 완료되었습니다! 총 {}개의 고객 및 지갑 데이터가 추가되었습니다.", TOTAL_RECORDS);
                }
            } catch (SQLException e) {
                logger.warn("데이터 삽입 중 오류 발생", e);
            }
        }
    }
}
