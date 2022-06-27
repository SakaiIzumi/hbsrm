package net.bncloud.information.controller.vm;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class MessageBody {
    private String url;
    private String title;
    @NotBlank(message = "content不能为空")
    private String content;
    @NotBlank(message = "users不能为空")
    private String users;
}
