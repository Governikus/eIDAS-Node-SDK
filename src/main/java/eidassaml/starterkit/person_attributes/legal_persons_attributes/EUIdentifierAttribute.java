package eidassaml.starterkit.person_attributes.legal_persons_attributes;

import eidassaml.starterkit.EidasAttribute;
import eidassaml.starterkit.EidasLegalPersonAttributes;
import eidassaml.starterkit.person_attributes.AbstractLatinScriptAttribute;
import eidassaml.starterkit.person_attributes.EidasPersonAttributes;

/**
 * Created by yuri on 2/12/2016.
 */
public class EUIdentifierAttribute extends AbstractLatinScriptAttribute {

	public EUIdentifierAttribute() {}
	
    public EUIdentifierAttribute(String value) {
        super(value);
    }

    @Override
    public String getTemplateName() {
        return "d201217euidentifier";
    }

    @Override
    public String type() {
        return EidasAttribute.TYPE_D_2012_17_EU_Identifier;
    }

    @Override
    public EidasPersonAttributes getPersonAttributeType() {
        return EidasLegalPersonAttributes.D2012_17_EUIdentifier;
    }
}
