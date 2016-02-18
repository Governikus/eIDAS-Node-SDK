package eidassaml.starterkit.person_attributes.legal_persons_attributes;

import eidassaml.starterkit.EidasAttribute;
import eidassaml.starterkit.EidasLegalPersonAttributes;
import eidassaml.starterkit.person_attributes.AbstractAttribute;
import eidassaml.starterkit.person_attributes.EidasPersonAttributes;

/**
 * Created by yuri on 2/12/2016.
 */
public class LegalEntityIdentifierAttribute extends AbstractAttribute {


    public LegalEntityIdentifierAttribute(String value) {
        super(value);
    }
    public LegalEntityIdentifierAttribute(){}

    @Override
    public String getTemplateName() {
        return "legalentityidentifier";
    }

    @Override
    public String type() {
        return EidasAttribute.TYPE_LegalEntityIdentifier;
    }

    @Override
    public EidasPersonAttributes getPersonAttributeType() {
        return EidasLegalPersonAttributes.LEI;
    }
}

