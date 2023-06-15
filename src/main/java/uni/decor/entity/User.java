package uni.decor.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import uni.decor.common.Enum.Provider;

import java.util.*;
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    @NotBlank(message = "Email is required")
    private String email;

    @Column(name = "password")
//    @NotBlank(message = "Password is required")
    private String password;

    @Column(name = "phone")
//    @NotBlank(message = "Phone Number is required")
    private String phone;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name is required")
    private String name;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "gender")
    private String gender;

    @Column(name="verified")
    private boolean verified;

    @Enumerated(EnumType.STRING)
    private Provider provider;


    @ManyToMany
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public Set<Role> getRoles() {
        return roles;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Set<Role> userRoles = this.getRoles();
//        return userRoles.stream()
//                .map(role -> new SimpleGrantedAuthority(role.getName()))
//                .toList();
//    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("user"));
        authorities.add(new SimpleGrantedAuthority("admin"));
        System.out.println("authorities nek: ");
        System.out.println(authorities);
//        String[] roles = this.getRoles();
//        for(String role : roles)
//        {
//            authorities.add(new SimpleGrantedAuthority(role));
//        }
        return authorities;
    }
    //    public User() {
//        this.birthday = new Date(); // Khởi tạo birthday với thời gian hiện tại
//    }
}