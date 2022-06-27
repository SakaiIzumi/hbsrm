package net.bncloud.bis.srm.doc.client;

import net.bncloud.bis.config.OaConfiguration;
import net.bncloud.bis.srm.doc.webservice.AnyType2AnyTypeMap;
import net.bncloud.bis.srm.doc.webservice.ArrayOfAnyType2AnyTypeMap;
import net.bncloud.bis.srm.doc.webservice.ArrayOfInt;
import net.bncloud.bis.srm.doc.webservice.SRMDocInfoServicePortType;
import net.bncloud.common.util.ThrowableUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SrmDocSynchroDelegate implements ISrmDocSynchro{

    private static Logger LOGGER = LoggerFactory.getLogger(SrmDocSynchroDelegate.class);

    private String servicePath = "/services/SRMDocInfoService";

    @Autowired
    private OaConfiguration oaConfiguration;

    private SRMDocInfoServicePortType getService() throws Exception {
        String domain = oaConfiguration.getDomain();
        OaConfiguration.ProxyServer proxyServer = oaConfiguration.getProxyServer();
        if(StringUtils.isBlank(domain)){
            throw new RuntimeException("获取合同附件地址域名不能为空");
        }
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.getInInterceptors().add(new LoggingInInterceptor());
        factory.getOutInterceptors().add(new LoggingOutInterceptor());
        factory.getOutInterceptors().add(new AddSoapHeader());
        factory.setServiceClass(SRMDocInfoServicePortType.class);
        factory.setAddress(domain + this.servicePath);
        SRMDocInfoServicePortType service = (SRMDocInfoServicePortType)factory.create();
        Client proxy = ClientProxy.getClient(service);
        HTTPConduit conduit = (HTTPConduit)proxy.getConduit();
        HTTPClientPolicy policy = new HTTPClientPolicy();
        policy.setConnectionTimeout(10000L);
        policy.setReceiveTimeout(60000L);

        if(Objects.nonNull(proxyServer)){
            String ip = proxyServer.getIp();
            Integer port = proxyServer.getPort();
            if(StringUtils.isNotBlank(ip) && Objects.nonNull(port)){
                policy.setProxyServer(ip);
                policy.setProxyServerPort(port);
            }
        }
        conduit.setClient(policy);
        return service;
    }

    @Override
    public List<Map<String,Object>> getDocInfoList(Integer in0, ArrayOfInt in1) {
        try {
            ArrayOfAnyType2AnyTypeMap arrayOfAnyType2AnyTypeMap = getService().getDocInfoList(in0,in1);
            List<AnyType2AnyTypeMap> anyType2AnyTypeMap = arrayOfAnyType2AnyTypeMap.getAnyType2AnyTypeMap();
            List<Map<String,Object>> docFileList = new ArrayList<>();
            for (AnyType2AnyTypeMap anyTypeMap : anyType2AnyTypeMap){
                List<AnyType2AnyTypeMap.Entry> entryMap = anyTypeMap.getEntry();
                Map<String,Object> map = new HashMap<>();
                for (AnyType2AnyTypeMap.Entry entry : entryMap){
                    map.put(entry.getKey()+"",entry.getValue());
                }
                docFileList.add(map);
            }
            return docFileList;
        } catch (Exception e) {
            LOGGER.warn("获取合同信息异常："+ ThrowableUtils.toString(e));
        }
        return new ArrayList<>();
    }

    @Override
    public List<Map<String,Object>> getDocFileList(Integer in0, ArrayOfInt in1) {
        try {
            ArrayOfAnyType2AnyTypeMap arrayOfAnyType2AnyTypeMap = getService().getDocFileList(in0,in1);
            List<AnyType2AnyTypeMap> anyType2AnyTypeMap = arrayOfAnyType2AnyTypeMap.getAnyType2AnyTypeMap();

            List<Map<String,Object>> docFileList = new ArrayList<>();

            for (AnyType2AnyTypeMap anyTypeMap : anyType2AnyTypeMap){

                List<AnyType2AnyTypeMap.Entry> entryMap = anyTypeMap.getEntry();
                Map<String,Object> map = new HashMap<>();
                for (AnyType2AnyTypeMap.Entry entry : entryMap){
                    map.put(entry.getKey().toString(),entry.getValue());
                }
                docFileList.add(map);
            }
            return docFileList;
        } catch (Exception e) {
            LOGGER.warn("获取合同附件信息异常："+ ThrowableUtils.toString(e));
        }
        return new ArrayList<>();
    }


}
