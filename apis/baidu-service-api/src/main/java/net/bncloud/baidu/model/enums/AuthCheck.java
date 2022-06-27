package net.bncloud.baidu.model.enums;

/**
 * desc: 认证方式
 *
 * @author Rao
 * @Date 2022/05/24
 **/
public enum AuthCheck {

    /**
     * ip 白名单，需要在百度控制台上添加过滤ip
     */
    ip {
        @Override
        public String desc() {
            return "ip白名单认证方式。";
        }
    },
    /**
     * 需要填写 sk配置 ，暂未实现，需要实现请参考 SnUtils,并且看文档。
     */
    @Deprecated
    sn{
        @Override
        public String desc() {
            return "sn 签名认证，需要配置sk。";
        }
    },

    ;

    public abstract String desc();

    public static boolean isIp( AuthCheck authCheck ){
        return ip == authCheck;
    }

    public static boolean isSn(AuthCheck authCheck){
        return sn == authCheck;
    }

}
