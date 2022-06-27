package net.bncloud.bis.srm.doc.client;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.binding.xml.XMLFault;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.namespace.QName;

public class AddSoapHeader extends AbstractSoapInterceptor {
    private static final Log logger = LogFactory.getLog(AddSoapHeader.class);

    public AddSoapHeader() {
        super("write");
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        Document doc = DOMUtils.createDocument();

        Element root = doc.createElementNS("http://sys.webservice.client", "tns:RequestSOAPHeader");
        String userName = "";
        String password = "";
        if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password)) {
            Element userElement = doc.createElement("tns:user");
            Text userInfo = doc.createTextNode(userName);
            userElement.appendChild(userInfo);
            root.appendChild(userElement);
            Element pwdElement = doc.createElement("tns:password");
            Text passInfo = doc.createTextNode(password);
            pwdElement.appendChild(passInfo);
            root.appendChild(pwdElement);
        }

        QName qname = new QName("RequestSOAPHeader");
        SoapHeader head = new SoapHeader(qname, root);
        message.getHeaders().add(head);
    }
}
