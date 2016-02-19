package eidassaml.starterkit.person_attributes.legal_persons_attributes;

import eidassaml.starterkit.EidasAttribute;
import eidassaml.starterkit.EidasLegalPersonAttributes;
import eidassaml.starterkit.person_attributes.AbstractAttribute;
import eidassaml.starterkit.person_attributes.EidasPersonAttributes;

/**
 * Created by yuri on 2/12/2016.
 */
public class LegalNameAttribute extends AbstractAttribute {
    public LegalNameAttribute(String value) {
        super(value);
    }
    public LegalNameAttribute(){}

    @Override
    public String getTemplateName() {
        return "legalname";
    }

    @Override
    public String type() {
        return EidasAttribute.TYPE_LegalName;
    }

    @Override
    public EidasPersonAttributes getPersonAttributeType() {
        return EidasLegalPersonAttributes.LegalName;
    }
}
