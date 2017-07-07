
package demo.webservice.rhzx;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cuResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cuResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="flowid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="retCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="retInfo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cuResult", propOrder = {
    "flowid",
    "retCode",
    "retInfo"
})
public class CuResult {

    protected String flowid;
    protected String retCode;
    protected String retInfo;

    /**
     * Gets the value of the flowid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlowid() {
        return flowid;
    }

    /**
     * Sets the value of the flowid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlowid(String value) {
        this.flowid = value;
    }

    /**
     * Gets the value of the retCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRetCode() {
        return retCode;
    }

    /**
     * Sets the value of the retCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRetCode(String value) {
        this.retCode = value;
    }

    /**
     * Gets the value of the retInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRetInfo() {
        return retInfo;
    }

    /**
     * Sets the value of the retInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRetInfo(String value) {
        this.retInfo = value;
    }

}
