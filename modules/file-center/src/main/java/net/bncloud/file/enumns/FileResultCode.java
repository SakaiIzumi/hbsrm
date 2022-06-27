package net.bncloud.file.enumns;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bncloud.common.api.IResultCode;

/**
 * @ClassName FileResultCode
 * @Author Administrator
 * @Date 2021/5/18
 * @Version V1.0
 **/
@Getter
@AllArgsConstructor
public enum FileResultCode implements IResultCode {
    FILE_NOT_EXIST(404,"文件不存在"),
    ;


    /**
     * code编码
     */
    final int code;
    /**
     * 中文信息描述
     */
    final String message;

}
