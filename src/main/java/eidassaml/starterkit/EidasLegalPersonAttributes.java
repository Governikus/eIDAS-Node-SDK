/* 
 * 
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * 
 * Date: 09 Feb 2016
 * Authors: Governikus GmbH & Co. KG
 * 
*/
package eidassaml.starterkit;

import eidassaml.starterkit.person_attributes.EidasPersonAttributes;
import eidassaml.starterkit.person_attributes.legal_persons_attributes.EORIAttribute;
import eidassaml.starterkit.person_attributes.legal_persons_attributes.EUIdentifierAttribute;
import eidassaml.starterkit.person_attributes.legal_persons_attributes.LegalAddressAttribute;
import eidassaml.starterkit.person_attributes.legal_persons_attributes.LegalEntityIdentifierAttribute;
import eidassaml.starterkit.person_attributes.legal_persons_attributes.LegalNameAttribute;
import eidassaml.starterkit.person_attributes.legal_persons_attributes.LegalPersonIdentifierAttribute;
import eidassaml.starterkit.person_attributes.legal_persons_attributes.SEEDAttribute;
import eidassaml.starterkit.person_attributes.legal_persons_attributes.SICAttribute;
import eidassaml.starterkit.person_attributes.legal_persons_attributes.TaxReferenceAttribute;
import eidassaml.starterkit.person_attributes.legal_persons_attributes.VATRegistrationAttribute;

public enum EidasLegalPersonAttributes implements EidasPersonAttributes {

    LegalPersonIdentifier("http://eidas.europa.eu/attributes/legalperson/LegalPersonIdentifier", "LegalPersonIdentifier", LegalPersonIdentifierAttribute.class),
    LegalName("http://eidas.europa.eu/attributes/legalperson/LegalName", "LegalName", LegalNameAttribute.class),
    LegalAddress("http://eidas.europa.eu/attributes/legalperson/LegalPersonAddress", "LegalAddress", LegalAddressAttribute.class),
    VATRegistration("http://eidas.europa.eu/attributes/legalperson/VATRegistrationNumber", "VATRegistration", VATRegistrationAttribute.class),
    TaxReference("http://eidas.europa.eu/attributes/legalperson/TaxReferenceType", "TaxReference", TaxReferenceAttribute.class),
    D2012_17_EUIdentifier("http://eidas.europa.eu/attributes/legalperson/D-2012-17-EUIdentifier", "D-2012-17-EUIdentifier", EUIdentifierAttribute.class),
    LEI("http://eidas.europa.eu/attributes/legalperson/LEI", "LEI", LegalEntityIdentifierAttribute.class),
    EORI("http://eidas.europa.eu/attributes/legalperson/EORI", "EORI", EORIAttribute.class),
    SEED("http://eidas.europa.eu/attributes/legalperson/SEED", "SEED", SEEDAttribute.class),
    SIC("http://eidas.europa.eu/attributes/legalperson/SIC", "SIC", SICAttribute.class);

    public final String NAME;
    public final String FRIENDLY_NAME;
    public final Class<? extends EidasAttribute> ATTRIBUTE_CLASS;

    EidasLegalPersonAttributes(String name, String friendlyName, Class<? extends EidasAttribute> attrClazz) {
        NAME = name;
        FRIENDLY_NAME = friendlyName;
        ATTRIBUTE_CLASS = attrClazz;
    }

    public static EidasLegalPersonAttributes GetValueOf(String s) {
        for (EidasLegalPersonAttributes elpa : EidasLegalPersonAttributes.values()) {
            if (elpa.NAME.equals(s)) {
                return elpa;
            }
        }
        return null;
    }

    /**
     * Create an instance of the EidasAttribute corresponding to the NaturalPersonAttribute type
     * @return instance of EidasAttribute corresponding to the NaturalPersonAttribute type
     */
    public EidasAttribute getInstance(){
        try {
            return ATTRIBUTE_CLASS.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("Unable to instantiate attribute type.", e);
        }
    }

    public String getName(){
        return NAME;
    }

    public String getFriendlyName(){
        return FRIENDLY_NAME;
    }

}
