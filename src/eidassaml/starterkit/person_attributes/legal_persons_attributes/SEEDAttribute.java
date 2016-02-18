package eidassaml.starterkit.person_attributes.legal_persons_attributes;

import eidassaml.starterkit.EidasAttribute;
import eidassaml.starterkit.EidasLegalPersonAttributes;
import eidassaml.starterkit.person_attributes.AbstractAttribute;
import eidassaml.starterkit.person_attributes.EidasPersonAttributes;

/**
 * Created by yuri on 2/12/2016.
 */
public class SEEDAttribute extends AbstractAttribute {
    public SEEDAttribute(String value) {
        super(value);
    }
    public SEEDAttribute(){}

    @Override
    public String getTemplateName() {
        return "seed";
    }

    @Override
    public String type() {
        return EidasAttribute.TYPE_SEED;
    }

    @Override
    public EidasPersonAttributes getPersonAttributeType() {
        return EidasLegalPersonAttributes.SEED;
    }
}
