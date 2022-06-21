package cocktail.dto;

import cocktail.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

@Getter
public class SessionDto implements Serializable {

    private String name;
    private String email;
    private String picture;

    public SessionDto(User user) {
        this.name = user.getUsername();
        this.email = user.getUsername();
    }


    @Data
    @AllArgsConstructor
    public static class SessionInfoResponse {
        private String userId;
        private String sessionId;
        private Date createTime;
        private Date lastAccessTime;

    }
}
