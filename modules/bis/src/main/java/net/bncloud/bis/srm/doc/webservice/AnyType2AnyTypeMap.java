
package net.bncloud.bis.srm.doc.webservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anyType2anyTypeMap complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="anyType2anyTypeMap">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *                   &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "anyType2anyTypeMap", propOrder = {
    "entry"
})
public class AnyType2AnyTypeMap {

    protected List<AnyType2AnyTypeMap.Entry> entry;

    /**
     * Gets the value of the entry property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entry property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntry().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AnyType2AnyTypeMap.Entry }
     *
     *
     */
    public List<AnyType2AnyTypeMap.Entry> getEntry() {
        if (entry == null) {
            entry = new ArrayList<AnyType2AnyTypeMap.Entry>();
        }
        return this.entry;
    }


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
     *         &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
     *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
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
        "key",
        "value"
    })
    public static class Entry {

        protected Object key;
        protected String value;

        /**
         * 获取key属性的值。
         *
         * @return
         *     possible object is
         *     {@link Object }
         *
         */
        public Object getKey() {
            return key;
        }

        /**
         * 设置key属性的值。
         *
         * @param value
         *     allowed object is
         *     {@link Object }
         *
         */
        public void setKey(Object value) {
            this.key = value;
        }

        /**
         * 获取value属性的值。
         *
         * @return
         *     possible object is
         *     {@link Object }
         *
         */
        public Object getValue() {
            return value;
        }

        /**
         * 设置value属性的值。
         *
         * @param value
         *     allowed object is
         *     {@link Object }
         *
         */
        public void setValue(String value) {
            this.value = value;
        }
    }

}
