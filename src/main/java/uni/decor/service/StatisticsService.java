package uni.decor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.decor.dto.BestSellingProductData;
import uni.decor.dto.PaymentMethodData;
import uni.decor.dto.SalesData;
import uni.decor.repository.IOrderRepository;

import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    @Autowired
    private IOrderRepository orderRepository;

//    private static final Map<String, String> monthMap = new HashMap<String, String>() {{
//        put("January", "Tháng 1");
//        put("February", "Tháng 2");
//        put("March", "Tháng 3");
//        put("April", "Tháng 4");
//        put("May", "Tháng 5");
//        put("June", "Tháng 6");
//        put("July", "Tháng 7");
//        put("August", "Tháng 8");
//        put("September", "Tháng 9");
//        put("October", "Tháng 10");
//        put("November", "Tháng 11");
//        put("December", "Tháng 12");
//    }};
//
//    public List<SalesData> getSalesStatistics() {
//        int currentYear = Year.now().getValue();
//        List<SalesData> sales = orderRepository.getSalesData(currentYear);
//
//        return sales.stream()
//                .map(data -> new SalesData(monthMap.get(data.getMonth()), data.getSales()))
//                .collect(Collectors.toList());
//    }

    public List<PaymentMethodData> getPaymentMethodStatistics() {
        return orderRepository.getPaymentMethodData();
    }

    public List<BestSellingProductData> getBestSellingProducts() {
        return orderRepository.getBestSellingProducts();
    }
}
