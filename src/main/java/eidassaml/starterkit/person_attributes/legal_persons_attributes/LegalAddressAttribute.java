package eidassaml.starterkit.person_attributes.legal_persons_attributes;

import eidassaml.starterkit.EidasAttribute;
import eidassaml.starterkit.EidasLegalPersonAttributes;
import eidassaml.starterkit.EidasNaturalPersonAttributes;
import eidassaml.starterkit.Utils;
import eidassaml.starterkit.person_attributes.EidasPersonAttributes;
import eidassaml.starterkit.person_attributes.natural_persons_attribute.CurrentAddressAttribute;
import eidassaml.starterkit.template.TemplateLoader;
import org.xml.sax.SAXException;

/**
 * Created by yuri on 2/12/2016.
 */
public class LegalAddressAttribute extends CurrentAddressAttribute {
    public LegalAddressAttribute(String locatorDesignator, String thoroughfare, String postName, String postCode) {
        super(locatorDesignator, thoroughfare, postName, postCode);
    }

    public LegalAddressAttribute(String xmlString) throws SAXException {
        super(xmlString);
    }
    public LegalAddressAttribute(){}

    @Override
    public String generate() {
        String value = CVAddressTemp.replace("$locatorDesignator", getLocatorDesignator())
                .replace("$thoroughfare", getThoroughfare())
                .replace("$postName", getPostName())
                .replace("$postCode", getPostCode());
        return TemplateLoader.GetTemplateByName("legalpersonaddress").replace("$value", Utils.ToBase64(value));
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
