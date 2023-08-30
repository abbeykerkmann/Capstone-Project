package seg4910.group17.capstoneserver.api.model.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class PasswordChangeDetails {

    private PasswordChangeDetails() {}

    @JsonProperty("oldPassword")
    @NotBlank
    private String oldPassword;

    @JsonProperty("newPassword")
    @NotBlank
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

}
