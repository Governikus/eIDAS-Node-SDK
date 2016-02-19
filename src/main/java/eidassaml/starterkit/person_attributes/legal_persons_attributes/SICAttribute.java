package eidassaml.starterkit.person_attributes.legal_persons_attributes;

import eidassaml.starterkit.EidasAttribute;
import eidassaml.starterkit.EidasLegalPersonAttributes;
import eidassaml.starterkit.person_attributes.AbstractAttribute;
import eidassaml.starterkit.person_attributes.EidasPersonAttributes;

/**
 * Created by yuri on 2/12/2016.
 */
public class SICAttribute extends AbstractAttribute {
    public SICAttribute(String value) {
        super(value);
    }
    public SICAttribute(){}

    @Override
    public String getTemplateName() {
        return "sic";
    }

    @Override
    public String type() {
        return EidasAttribute.TYPE_SIC;
    }

    @Override
    public EidasPersonAttributes getPersonAttributeType() {
        return EidasLegalPersonAttributes.SIC;
    }
}
