package kr.jsh.ecommerce.testdata;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderDummyInsert {
    private static final Logger logger = Logger.getLogger(OrderDummyInsert.class.getName());
    private static final String URL = "jdbc:mysql://localhost:3306/hhplus?characterEncoding=UTF-8&serverTimezone=UTC";
    private static final String USER = "application";
    private static final String PASSWORD = "application";

    private static final int TARGET_FRUITS = 1000;  // 현실적인 과일 품종 개수
    private static final int TARGET_ORDERS = 10_000_000;  // 1,000만 개 주문 추가
    private static final int MAX_FRUITS_PER_ORDER = 5;
    private static final int MIN_FRUITS_PER_ORDER = 3;
    private static final int BATCH_SIZE = 50_000;  // 5만 건씩 배치 처리

    private static final List<String> ORDER_STATUSES = Arrays.asList("PENDING", "PAID", "CANCELLED");

    public static void main(String[] args) {
        new OrderDummyInsert().run();
    }

    public void run() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);

            // 1️⃣ 현재 데이터 개수 확인
            int currentFruitCount = countExistingRecords(conn, "fruit");
            int currentOrderCount = countExistingRecords(conn, "`order`");
            int currentOrderFruitCount = countExistingRecords(conn, "order_fruit");

            logger.info("기존 데이터 개수 확인 완료");
            logger.info("현재 과일 개수: " + currentFruitCount);
            logger.info("현재 주문 개수: " + currentOrderCount);
            logger.info("현재 개별 주문 개수: " + currentOrderFruitCount);

            // 2️⃣ 과일 추가 삽입
            List<Long> fruitIds = insertFruits(conn, currentFruitCount, TARGET_FRUITS);

            // 3️⃣ 고객 ID 목록 가져오기
            List<Long> customerIds = fetchCustomerIds(conn);

            // 4️⃣ 주문 데이터 삽입
            List<Long> orderIds = insertOrders(conn, customerIds, currentOrderCount, TARGET_ORDERS);

            // 5️⃣ 개별 주문 데이터 삽입
            insertOrderFruits(conn, orderIds, fruitIds, currentOrderFruitCount);

            conn.commit();
            logger.info("대량 데이터 삽입 완료!");

        } catch (SQLException e) {
            logger.log(Level.WARNING, "데이터 삽입 중 오류 발생", e);
        }
    }

    // 🔹 1️⃣ 기존 데이터 개수 조회
    private int countExistingRecords(Connection conn, String tableName) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + tableName;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // 🔹 2️⃣ 과일 데이터 추가 삽입
    private List<Long> insertFruits(Connection conn, int currentCount, int targetCount) throws SQLException {
        String insertSQL = "INSERT INTO fruit (fruit_name, fruit_stock, fruit_price, status) VALUES (?, ?, ?, ?)";
        List<Long> fruitIds = new ArrayList<>();
        Random random = new Random();

        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = currentCount; i < targetCount; i++) {
                String fruitName = "과일" + i;
                int stock = 10000 + random.nextInt(50000);
                int price = 1000 + random.nextInt(4000);
                pstmt.setString(1, fruitName);
                pstmt.setInt(2, stock);
                pstmt.setInt(3, price);
                pstmt.setString(4, "IN_STOCK");
                pstmt.addBatch();

                if (i % BATCH_SIZE == 0) {
                    pstmt.executeBatch();
                    conn.commit();
                }
            }
            pstmt.executeBatch();
        }
        logger.info("과일 데이터 삽입 완료");
        return fruitIds;
    }

    // 🔹 3️⃣ 고객 ID 가져오기
    private List<Long> fetchCustomerIds(Connection conn) throws SQLException {
        List<Long> customerIds = new ArrayList<>();
        String query = "SELECT customer_id FROM customer";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                customerIds.add(rs.getLong("customer_id"));
            }
        }
        return customerIds;
    }

    // 🔹 4️⃣ 주문 데이터 추가 삽입
    private List<Long> insertOrders(Connection conn, List<Long> customerIds, int currentCount, int targetCount) throws SQLException {
        String insertSQL = "INSERT INTO `order` (customer_id, order_date, total_amount, order_status) VALUES (?, ?, ?, ?)";
        List<Long> orderIds = new ArrayList<>();
        Random random = new Random();

        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = currentCount; i < targetCount; i++) {
                long customerId = customerIds.get(random.nextInt(customerIds.size()));
                LocalDateTime orderDate = LocalDateTime.now().minusDays(random.nextInt(30));
                String orderStatus = ORDER_STATUSES.get(random.nextInt(ORDER_STATUSES.size()));

                pstmt.setLong(1, customerId);
                pstmt.setTimestamp(2, Timestamp.valueOf(orderDate));
                pstmt.setInt(3, 0);
                pstmt.setString(4, orderStatus);
                pstmt.addBatch();

                if (i % BATCH_SIZE == 0) {
                    pstmt.executeBatch();
                    conn.commit();
                }
            }
            pstmt.executeBatch();
        }
        logger.info("✅ 주문 데이터 삽입 완료");
        return orderIds;
    }

    // 🔹 5️⃣ 개별 주문 데이터 추가 삽입
    private void insertOrderFruits(Connection conn, List<Long> orderIds, List<Long> fruitIds, int currentCount) throws SQLException {
        String insertSQL = "INSERT INTO order_fruit (order_id, fruit_id, fruit_price, quantity, sub_total) VALUES (?, ?, ?, ?, ?)";
        Random random = new Random();

        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            for (Long orderId : orderIds) {
                int fruitCount = random.nextInt(MAX_FRUITS_PER_ORDER - MIN_FRUITS_PER_ORDER + 1) + MIN_FRUITS_PER_ORDER;

                for (int i = 0; i < fruitCount; i++) {
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
                }
            }
            pstmt.executeBatch();
        }
        logger.info("개별 주문 데이터 삽입 완료");
    }
}
