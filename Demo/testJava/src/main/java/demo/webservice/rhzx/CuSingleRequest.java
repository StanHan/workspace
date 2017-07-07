
package demo.webservice.rhzx;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cuSingleRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cuSingleRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="certno" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="certtype" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="channelid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="clientip" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cstmsysid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="qryreason" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="qrytype" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="qtimelimit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="querymode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="resulttype" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cuSingleRequest", propOrder = {
    "certno",
    "certtype",
    "channelid",
    "clientip",
    "cstmsysid",
    "name",
    "qryreason",
    "qrytype",
    "qtimelimit",
    "querymode",
    "resulttype",
    "userid"
})
public class CuSingleRequest {

    protected String certno;
    protected String certtype;
    protected String channelid;
    protected String clientip;
    protected String cstmsysid;
    protected String name;
    protected String qryreason;
    protected String qrytype;
    protected String qtimelimit;
    protected String querymode;
    protected String resulttype;
    protected String userid;

    /**
     * Gets the value of the certno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCertno() {
        return certno;
    }

    /**
     * Sets the value of the certno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCertno(String value) {
        this.certno = value;
    }

    /**
     * Gets the value of the certtype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCerttype() {
        return certtype;
    }

    /**
     * Sets the value of the certtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCerttype(String value) {
        this.certtype = value;
    }

    /**
     * Gets the value of the channelid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChannelid() {
        return channelid;
    }

    /**
     * Sets the value of the channelid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChannelid(String value) {
        this.channelid = value;
    }

    /**
     * Gets the value of the clientip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientip() {
        return clientip;
    }

    /**
     * Sets the value of the clientip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientip(String value) {
        this.clientip = value;
    }

    /**
     * Gets the value of the cstmsysid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCstmsysid() {
        return cstmsysid;
    }

    /**
     * Sets the value of the cstmsysid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCstmsysid(String value) {
        this.cstmsysid = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the qryreason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQryreason() {
        return qryreason;
    }

    /**
     * Sets the value of the qryreason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQryreason(String value) {
        this.qryreason = value;
    }

    /**
     * Gets the value of the qrytype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQrytype() {
        return qrytype;
    }

    /**
     * Sets the value of the qrytype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQrytype(String value) {
        this.qrytype = value;
    }

    /**
     * Gets the value of the qtimelimit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQtimelimit() {
        return qtimelimit;
    }

    /**
     * Sets the value of the qtimelimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQtimelimit(String value) {
        this.qtimelimit = value;
    }

    /**
     * Gets the value of the querymode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuerymode() {
        return querymode;
    }

    /**
     * Sets the value of the querymode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuerymode(String value) {
        this.querymode = value;
    }

    /**
     * Gets the value of the resulttype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResulttype() {
        return resulttype;
    }

    /**
     * Sets the value of the resulttype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResulttype(String value) {
        this.resulttype = value;
    }

    /**
     * Gets the value of the userid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserid() {
        return userid;
    }

    /**
     * Sets the value of the userid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserid(String value) {
        this.userid = value;
    }

}
