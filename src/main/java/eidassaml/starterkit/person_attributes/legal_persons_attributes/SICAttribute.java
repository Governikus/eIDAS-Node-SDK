package eidassaml.starterkit.person_attributes.legal_persons_attributes;

import eidassaml.starterkit.EidasAttribute;
import eidassaml.starterkit.EidasLegalPersonAttributes;
import eidassaml.starterkit.person_attributes.AbstractLatinScriptAttribute;
import eidassaml.starterkit.person_attributes.EidasPersonAttributes;

/**
 * Created by yuri on 2/12/2016.
 */
public class SICAttribute extends AbstractLatinScriptAttribute {
	
	public SICAttribute() {}
	
    public SICAttribute(String value) {
        super(value);
    }

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
