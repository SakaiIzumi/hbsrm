
package net.bncloud.bis.srm.doc.webservice;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "SRMDocInfoService", targetNamespace = "http://service.it.meishang.com", wsdlLocation = "file:/C:/Users/17603/Desktop/SRMDocInfoService.wsdl")
public class SRMDocInfoService
    extends Service
{

    private final static URL SRMDOCINFOSERVICE_WSDL_LOCATION;
    private final static WebServiceException SRMDOCINFOSERVICE_EXCEPTION;
    private final static QName SRMDOCINFOSERVICE_QNAME = new QName("http://service.it.meishang.com", "SRMDocInfoService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("file:/C:/Users/17603/Desktop/SRMDocInfoService.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        SRMDOCINFOSERVICE_WSDL_LOCATION = url;
        SRMDOCINFOSERVICE_EXCEPTION = e;
    }

    public SRMDocInfoService() {
        super(__getWsdlLocation(), SRMDOCINFOSERVICE_QNAME);
    }

    public SRMDocInfoService(WebServiceFeature... features) {
        super(__getWsdlLocation(), SRMDOCINFOSERVICE_QNAME, features);
    }

    public SRMDocInfoService(URL wsdlLocation) {
        super(wsdlLocation, SRMDOCINFOSERVICE_QNAME);
    }

    public SRMDocInfoService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SRMDOCINFOSERVICE_QNAME, features);
    }

    public SRMDocInfoService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SRMDocInfoService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns SRMDocInfoServicePortType
     */
    @WebEndpoint(name = "SRMDocInfoServiceHttpPort")
    public SRMDocInfoServicePortType getSRMDocInfoServiceHttpPort() {
        return super.getPort(new QName("http://service.it.meishang.com", "SRMDocInfoServiceHttpPort"), SRMDocInfoServicePortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SRMDocInfoServicePortType
     */
    @WebEndpoint(name = "SRMDocInfoServiceHttpPort")
    public SRMDocInfoServicePortType getSRMDocInfoServiceHttpPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://service.it.meishang.com", "SRMDocInfoServiceHttpPort"), SRMDocInfoServicePortType.class, features);
    }

    private static URL __getWsdlLocation() {
        if (SRMDOCINFOSERVICE_EXCEPTION!= null) {
            throw SRMDOCINFOSERVICE_EXCEPTION;
        }
        return SRMDOCINFOSERVICE_WSDL_LOCATION;
    }

}
