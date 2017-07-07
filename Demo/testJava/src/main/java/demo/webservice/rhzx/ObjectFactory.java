
package demo.webservice.rhzx;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.cfcc.icrqs.webservice package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.cfcc.icrqs.webservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CuSingleResult }
     * 
     */
    public CuSingleResult createCuSingleResult() {
        return new CuSingleResult();
    }

    /**
     * Create an instance of {@link CuGetResult }
     * 
     */
    public CuGetResult createCuGetResult() {
        return new CuGetResult();
    }

    /**
     * Create an instance of {@link CuSingleRequest }
     * 
     */
    public CuSingleRequest createCuSingleRequest() {
        return new CuSingleRequest();
    }

    /**
     * Create an instance of {@link CuResult }
     * 
     */
    public CuResult createCuResult() {
        return new CuResult();
    }

}
