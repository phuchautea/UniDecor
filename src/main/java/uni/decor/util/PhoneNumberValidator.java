package uni.decor.util;

import java.util.regex.Pattern;

public class PhoneNumberValidator {
    public static boolean validateVNPhoneNumber(String phoneNumber) {
        phoneNumber = phoneNumber.replace("+84", "0");
        Pattern pattern = Pattern.compile("^(0)(86|96|97|98|32|33|34|35|36|37|38|39|91|94|83|84|85|81|82|90|93|70|79|77|76|78|92|56|58|99|59|55|87)\\d{7}$");
        return pattern.matcher(phoneNumber).matches();
    }
}
