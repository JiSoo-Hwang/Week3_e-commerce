package kr.jsh.ecommerce.testdata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DummyInsert {
    private static final Logger logger = Logger.getLogger(DummyInsert.class.getName());
    private static final String URL = "jdbc:mysql://localhost:3306/hhplus?characterEncoding=UTF-8&serverTimezone=UTC";
    private static final String USER = "application";
    private static final String PASSWORD = "application";
    private static final int TOTAL_RECORDS = 2_000_000;  // 🔹 200만 개 삽입
    private static final int BATCH_SIZE = 50_000; // 🔹 5만 개씩 배치 처리

    public static void main(String[] args) {
        new CustomerInsert().run();
    }

    static class CustomerInsert {
        private final Random random = new Random();

        public void run() {
            String insertCustomerSQL = "INSERT INTO customer (customer_name, customer_address, customer_phone) VALUES (?, ?, ?)";
            String insertWalletSQL = "INSERT INTO wallet (balance, customer_id) VALUES (?, ?)";

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                conn.setAutoCommit(false);

                try (PreparedStatement pstmtCustomer = conn.prepareStatement(insertCustomerSQL, PreparedStatement.RETURN_GENERATED_KEYS);
                     PreparedStatement pstmtWallet = conn.prepareStatement(insertWalletSQL)) {

                    // 🔹 고객 데이터 대량 삽입
                    for (int i = 0; i < TOTAL_RECORDS; i++) {
                        String name = "고객" + i;
                        String address = "도시 " + (random.nextInt(100) + 1);
                        String phone = "010-" + (random.nextInt(9000) + 1000) + "-" + (random.nextInt(9000) + 1000);

                        pstmtCustomer.setString(1, name);
                        pstmtCustomer.setString(2, address);
                        pstmtCustomer.setString(3, phone);
                        pstmtCustomer.addBatch();

                        if (i % BATCH_SIZE == 0) {
                            pstmtCustomer.executeBatch();
                            conn.commit();
                            logger.log(Level.INFO, "{0}명의 고객 데이터가 삽입되었습니다.", i);

                            // 🔹 고객 ID 가져와서 Wallet 생성
                            try (var generatedKeys = pstmtCustomer.getGeneratedKeys()) {
                                while (generatedKeys.next()) {
                                    long customerId = generatedKeys.getLong(1);
                                    int balance = random.nextInt(1000000) + 1000; // 1,000원 ~ 1,000,000원

                                    pstmtWallet.setInt(1, balance);
                                    pstmtWallet.setLong(2, customerId);
                                    pstmtWallet.addBatch();
                                }
                            }
                            pstmtWallet.executeBatch();
                            conn.commit();
                        }
                    }

                    // 🔹 남은 배치 실행
                    pstmtCustomer.executeBatch();
                    pstmtWallet.executeBatch();
                    conn.commit();

                    logger.log(Level.INFO, "모든 데이터 삽입이 완료되었습니다! 총 {0}개의 고객 및 지갑 데이터가 추가되었습니다.", TOTAL_RECORDS);
                }
            } catch (SQLException e) {
                logger.log(Level.WARNING, "데이터 삽입 중 오류 발생", e);
            }
        }
    }
}
