package uni.decor.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.*;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    @NotBlank(message = "Your email is required")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]+$", message = "Invalid email format")
    private String email;

    @Column(name = "password", nullable = false)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Invalid password format")
    private String password;

    @Column(name = "phone", nullable = false)
    @Pattern(regexp = "^0(86|96|97|98|32|33|34|35|36|37|38|39|91|94|83|84|85|81|82|90|93|70|79|77|76|78|92|56|58|99|59|55|87)\\d{7}$", message = "Invalid phone number format")
    private String phone;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Your name is required")
    private String name;

    @Column(name = "birthday", nullable = false)
    @Past(message = "Birthday must be a date in the past")
    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/((19|20)\\d{2})$", message = "Invalid date format. The format should be dd/MM/yyyy")
    private Date birthday;

    @Column(name = "gender", nullable = false)
    @Pattern(regexp = "^(Male|Female|Other)$", message = "Invalid gender. Valid values are Male, Female, or Other")
    private String gender;

    @Column(name="verified")
//    @AssertTrue(message = "User must be verified")
    private boolean verified;


    @ManyToMany
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ShippingAddress> shippingAddresses = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    public User() {
        this.birthday = new Date(); // Khởi tạo birthday với thời gian hiện tại
    }
}