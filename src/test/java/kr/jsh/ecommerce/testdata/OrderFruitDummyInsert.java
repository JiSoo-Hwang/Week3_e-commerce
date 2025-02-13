package kr.jsh.ecommerce.testdata;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderFruitDummyInsert {
    private static final Logger logger = Logger.getLogger(OrderFruitDummyInsert.class.getName());
    private static final String URL = "jdbc:mysql://localhost:3306/hhplus?characterEncoding=UTF-8&serverTimezone=UTC";
    private static final String USER = "application";
    private static final String PASSWORD = "application";

    private static final int MAX_FRUITS_PER_ORDER = 5;
    private static final int MIN_FRUITS_PER_ORDER = 3;
    private static final int BATCH_SIZE = 50_000; // 5만 개씩 배치 처리
    private static final long RUNNING_TIME_MS = 15 * 60 * 1000; // 15분

    public static void main(String[] args) {
        new OrderFruitDummyInsert().run();
    }

    public void run() {
        long startTime = System.currentTimeMillis();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);

            // 1️⃣ 현재 개별 주문 데이터 개수 확인
            int currentOrderFruitCount = countExistingRecords(conn, "order_fruit");
            logger.info("현재 개별 주문 개수: " + currentOrderFruitCount);

            // 2️⃣ 주문 및 과일 ID 목록 가져오기
            List<Long> orderIds = fetchOrderIds(conn);
            List<Long> fruitIds = fetchFruitIds(conn);

            // 3️⃣ 개별 주문 데이터 추가 삽입
            insertOrderFruits(conn, orderIds, fruitIds, currentOrderFruitCount, startTime);

            conn.commit();
            logger.info("개별 주문 데이터 추가 삽입 완료!");

        } catch (SQLException e) {
            logger.log(Level.WARNING, "데이터 삽입 중 오류 발생", e);
        }
    }

    // 🔹 현재 데이터 개수 조회
    private int countExistingRecords(Connection conn, String tableName) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + tableName;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // 🔹 주문 ID 가져오기
    private List<Long> fetchOrderIds(Connection conn) throws SQLException {
        List<Long> orderIds = new ArrayList<>();
        String query = "SELECT order_id FROM `order`";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                orderIds.add(rs.getLong("order_id"));
            }
        }
        return orderIds;
    }

    // 🔹 과일 ID 가져오기
    private List<Long> fetchFruitIds(Connection conn) throws SQLException {
        List<Long> fruitIds = new ArrayList<>();
        String query = "SELECT fruit_id FROM fruit";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                fruitIds.add(rs.getLong("fruit_id"));
            }
        }
        return fruitIds;
    }

    // 🔹 개별 주문 데이터 추가 삽입 (15분 동안 실행)
    private void insertOrderFruits(Connection conn, List<Long> orderIds, List<Long> fruitIds, int currentCount, long startTime) throws SQLException {
        String insertSQL = "INSERT INTO order_fruit (order_id, fruit_id, fruit_price, quantity, sub_total) VALUES (?, ?, ?, ?, ?)";
        Random random = new Random();
        int insertedCount = 0;

        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            while (System.currentTimeMillis() - startTime < RUNNING_TIME_MS) {
                for (int i = 0; i < BATCH_SIZE; i++) {
                    long orderId = orderIds.get(random.nextInt(orderIds.size())); // 랜덤한 주문 선택
                    long fruitId = fruitIds.get(random.nextInt(fruitIds.size()));
                    int quantity = random.nextInt(10) + 1;
                    int price = 1000 + random.nextInt(4000);
                    int subTotal = price * quantity;

                    pstmt.setLong(1, orderId);
                    pstmt.setLong(2, fruitId);
                    pstmt.setInt(3, price);
                    pstmt.setInt(4, quantity);
                    pstmt.setInt(5, subTotal);
                    pstmt.addBatch();
                    insertedCount++;

                    if (insertedCount % BATCH_SIZE == 0) {
                        pstmt.executeBatch();
                        conn.commit();
                        logger.info("현재 추가된 개별 주문 수: " + insertedCount);
                    }
                }
                pstmt.executeBatch();
                conn.commit();
            }
        }
        logger.info("15분 경과. 총 추가된 개별 주문 개수: " + insertedCount);
    }
}

