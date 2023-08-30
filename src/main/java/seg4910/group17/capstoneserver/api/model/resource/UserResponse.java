package seg4910.group17.capstoneserver.api.model.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserResponse {
    private UserResponse() {}

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("address")
    private String address;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("phone")
    private String phone;

    public UserResponse(Integer id, String username, String email, String address, String firstName, String lastName, String phone) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.address = address;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }
}
