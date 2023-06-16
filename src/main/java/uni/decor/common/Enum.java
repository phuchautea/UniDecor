package uni.decor.common;

public class Enum {
    public enum Provider {
        LOCAL, GOOGLE
    }
    public enum PaymentStatus {
        PAID("Đã thanh toán"),
        UNPAID("Chưa thanh toán");

        private final String name;

        PaymentStatus(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum OrderStatus {
        PENDING_CONFIRMATION("Chờ xác nhận"),
        PREPARING("Đang chuẩn bị"),
        SHIPPING("Đang giao"),
        COMPLETED("Hoàn thành"),
        CANCELED("Đã hủy");

        private final String name;

        OrderStatus(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
