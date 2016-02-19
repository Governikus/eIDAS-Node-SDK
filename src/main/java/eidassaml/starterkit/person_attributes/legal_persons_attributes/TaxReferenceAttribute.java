package eidassaml.starterkit.person_attributes.legal_persons_attributes;

import eidassaml.starterkit.EidasAttribute;
import eidassaml.starterkit.EidasLegalPersonAttributes;
import eidassaml.starterkit.person_attributes.AbstractAttribute;
import eidassaml.starterkit.person_attributes.EidasPersonAttributes;

/**
 * Created by yuri on 2/12/2016.
 */
public class TaxReferenceAttribute extends AbstractAttribute {

    public TaxReferenceAttribute(){}
    public TaxReferenceAttribute(String value) {
        super(value);
    }

    @Override
    public String getTemplateName() {
        return "taxreference";
    }

    @Override
    public String type() {
        return EidasAttribute.TYPE_TaxReference;
    }

    @Override
    public EidasPersonAttributes getPersonAttributeType() {
        return EidasLegalPersonAttributes.TaxReference;
    }
}
