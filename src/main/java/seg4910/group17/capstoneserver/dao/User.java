package seg4910.group17.capstoneserver.dao;

import com.fasterxml.jackson.annotation.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="tbl_users")
public class User implements UserDetails {
    protected User(){};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Integer id;

    @Column(name="username")
    @JsonProperty("username")
    @NotBlank
    private String username;

    @Column(name="email")
    @JsonProperty("email")
    @NotBlank
    @Email
    private String email;

    @Column(name="address")
    @JsonProperty("address")
    private String address;

    @Column(name="first_name")
    @JsonProperty("firstName")
    @NotBlank
    private String firstName;

    @Column(name="last_name")
    @JsonProperty("lastName")
    @NotBlank
    private String lastName;

    @Column(name="password")
    @JsonProperty("password")
    private String password;

    @Column(name="phone")
    @JsonProperty("phone")
    private String phone;

    @OneToMany(mappedBy="user")
    @JsonIgnore
    private List<Listing> listings;

    public User(String username, String email, String firstName, String last_name, String password) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = last_name;
        this.password = password;
        this.listings = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public List<Listing> getListings() {
        return listings;
    }

    // used for hashing the passwords
    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

}
