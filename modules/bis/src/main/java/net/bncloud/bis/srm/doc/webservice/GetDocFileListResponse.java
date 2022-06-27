
package net.bncloud.bis.srm.doc.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="out" type="{http://service.it.meishang.com}ArrayOfAnyType2anyTypeMap"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "out"
})
@XmlRootElement(name = "getDocFileListResponse")
public class GetDocFileListResponse {

    @XmlElement(required = true, nillable = true)
    protected ArrayOfAnyType2AnyTypeMap out;

    /**
     * 获取out属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAnyType2AnyTypeMap }
     *     
     */
    public ArrayOfAnyType2AnyTypeMap getOut() {
        return out;
    }

    /**
     * 设置out属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAnyType2AnyTypeMap }
     *     
     */
    public void setOut(ArrayOfAnyType2AnyTypeMap value) {
        this.out = value;
    }

}
