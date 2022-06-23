package cocktail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;


public class SessionDto {

    @Data
    @AllArgsConstructor
    public static class SessionInfoResponse {
        private String userId;
        private String sessionId;
        private Date createTime;
        private Date lastAccessTime;

    }
}
