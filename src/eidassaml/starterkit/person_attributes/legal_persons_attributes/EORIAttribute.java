package eidassaml.starterkit.person_attributes.legal_persons_attributes;

import eidassaml.starterkit.EidasAttribute;
import eidassaml.starterkit.EidasLegalPersonAttributes;
import eidassaml.starterkit.person_attributes.AbstractAttribute;
import eidassaml.starterkit.person_attributes.EidasPersonAttributes;

/**
 * Created by yuri on 2/12/2016.
 */
public class EORIAttribute extends AbstractAttribute {


    public EORIAttribute(String value) {
        super(value);
    }
    public EORIAttribute() {    }

    @Override
    public String getTemplateName() {
        return "eori";
    }

    @Override
    public String type() {
        return EidasAttribute.TYPE_EORI;
    }

    @Override
    public EidasPersonAttributes getPersonAttributeType() {
        return EidasLegalPersonAttributes.EORI;
    }
}
