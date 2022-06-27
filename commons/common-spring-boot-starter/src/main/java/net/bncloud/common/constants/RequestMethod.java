package net.bncloud.common.constants;

public enum RequestMethod {

    GET,
    POST,
    PUT,
    DELETE,

    /**
     * 读，包括GET和POST，查询条件较复杂时使用POST
     */
    READ,

    /**
     * 写，包括POST和PUT
     */
    WRITE
}
