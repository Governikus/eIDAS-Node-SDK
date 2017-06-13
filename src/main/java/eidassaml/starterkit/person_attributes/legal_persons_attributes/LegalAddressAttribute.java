package eidassaml.starterkit.person_attributes.legal_persons_attributes;

import org.xml.sax.SAXException;

import eidassaml.starterkit.EidasAttribute;
import eidassaml.starterkit.EidasLegalPersonAttributes;
import eidassaml.starterkit.Utils;
import eidassaml.starterkit.person_attributes.EidasPersonAttributes;
import eidassaml.starterkit.person_attributes.natural_persons_attribute.CurrentAddressAttribute;
import eidassaml.starterkit.template.TemplateLoader;

/**
 * Created by yuri on 2/12/2016.
 */
public class LegalAddressAttribute extends CurrentAddressAttribute {
    public LegalAddressAttribute(String locatorDesignator, String thoroughfare, String postName, String postCode
    		, String pOBOX, String locatorName, String cvaddressArea,
			String adminunitFirstline, String adminunitSecondline) {
        super(locatorDesignator, thoroughfare, postName, postCode, pOBOX, locatorName, cvaddressArea, adminunitFirstline, adminunitSecondline);
    }

    public LegalAddressAttribute(String xmlString) throws SAXException {
        super(xmlString);
    }
    public LegalAddressAttribute(){}

    @Override
    public String generate() {
        String value = super.getValue();
        return TemplateLoader.GetTemplateByName("legalpersonaddress").replace("$base64Value", Utils.ToBase64(value));
    }

    @Override
    public String type() {
        return EidasAttribute.TYPE_LegalPersonAddress;
    }

    @Override
    public String toString() {
        return type() + " " + getLocatorDesignator() + " " + getThoroughfare() + " , " + getPostCode() + " " + getPostName();
    }

    @Override
    public EidasPersonAttributes getPersonAttributeType() {
        return EidasLegalPersonAttributes.LegalAddress;
    }


}
