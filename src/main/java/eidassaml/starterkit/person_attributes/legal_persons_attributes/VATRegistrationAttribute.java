package eidassaml.starterkit.person_attributes.legal_persons_attributes;

import eidassaml.starterkit.EidasAttribute;
import eidassaml.starterkit.EidasLegalPersonAttributes;
import eidassaml.starterkit.person_attributes.AbstractLatinScriptAttribute;
import eidassaml.starterkit.person_attributes.EidasPersonAttributes;

/**
 * Created by yuri on 2/12/2016.
 */
public class VATRegistrationAttribute extends AbstractLatinScriptAttribute {
	
	public VATRegistrationAttribute(){}

    public VATRegistrationAttribute(String value) {
        super(value);
    }

    @Override
    public String getTemplateName() {
        return "vatregistration";
    }

    @Override
    public String type() {
        return EidasAttribute.TYPE_VATRegistration;
    }

    @Override
    public EidasPersonAttributes getPersonAttributeType() {
        return EidasLegalPersonAttributes.VATRegistration;
    }
}
