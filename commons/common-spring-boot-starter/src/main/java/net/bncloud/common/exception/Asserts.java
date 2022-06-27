package net.bncloud.common.exception;

import net.bncloud.common.api.IResultCode;
import net.bncloud.common.api.ResultCode;

/**
 * @author Rao
 * @Date 2021/12/25
 **/
public class Asserts {

    /**
     * 非null
     * @param obj
     * @param message
     */
    public static void notNull(Object obj,String message){
        isTrue( obj!= null,message );
    }

    /**
     * 是null
     * @param obj
     * @param message
     */
    public static void isNull(Object obj,String message){
        isTrue( obj == null,message );
    }

    /**
     * 为假
     */
    public static void isFalse(boolean flag,String message){
        isTrue( !flag, message );
    }

    /**
     * 为真
     * @param flag
     * @param message
     */
    public static void isTrue(boolean flag,String message){
        if(! flag){
            throw new BizException( new IResultCode() {
                private static final long serialVersionUID = 617512691330928570L;
                @Override
                public String getMessage() {
                    return message;
                }
                @Override
                public int getCode() {
                    return ResultCode.FAILURE.getCode();
                }
            });
        }
    }


}
