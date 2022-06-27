package net.bncloud.saas.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRelateInfoUpdate {
    private Long userId;
    private String subjectType;
    private Long subjectId;
    private String position;
    private String jobNo;
}
