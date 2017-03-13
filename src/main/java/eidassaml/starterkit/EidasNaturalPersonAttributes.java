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
import eidassaml.starterkit.person_attributes.natural_persons_attribute.BirthNameAttribute;
import eidassaml.starterkit.person_attributes.natural_persons_attribute.CurrentAddressAttribute;
import eidassaml.starterkit.person_attributes.natural_persons_attribute.DateOfBirthAttribute;
import eidassaml.starterkit.person_attributes.natural_persons_attribute.FamilyNameAttribute;
import eidassaml.starterkit.person_attributes.natural_persons_attribute.GenderAttribute;
import eidassaml.starterkit.person_attributes.natural_persons_attribute.GivenNameAttribute;
import eidassaml.starterkit.person_attributes.natural_persons_attribute.PersonIdentifierAttribute;
import eidassaml.starterkit.person_attributes.natural_persons_attribute.PlaceOfBirthAttribute;

public enum EidasNaturalPersonAttributes implements EidasPersonAttributes{
	FirstName("http://eidas.europa.eu/attributes/naturalperson/CurrentGivenName", "FirstName", GivenNameAttribute.class),
	BirthName("http://eidas.europa.eu/attributes/naturalperson/BirthName", "BirthName", BirthNameAttribute.class),
	FamilyName("http://eidas.europa.eu/attributes/naturalperson/CurrentFamilyName", "FamilyName", FamilyNameAttribute.class),
	DateOfBirth("http://eidas.europa.eu/attributes/naturalperson/DateOfBirth", "DateOfBirth", DateOfBirthAttribute.class),
	PlaceOfBirth("http://eidas.europa.eu/attributes/naturalperson/PlaceOfBirth", "PlaceOfBirth", PlaceOfBirthAttribute.class),
	Gender("http://eidas.europa.eu/attributes/naturalperson/Gender", "Gender", GenderAttribute.class),
	CurrentAddress("http://eidas.europa.eu/attributes/naturalperson/CurrentAddress", "CurrentAddress", CurrentAddressAttribute.class),
	PersonIdentifier("http://eidas.europa.eu/attributes/naturalperson/PersonIdentifier", "PersonIdentifier", PersonIdentifierAttribute.class);
	public final String NAME;
	public final String FRIENDLY_NAME;
	public final Class<? extends EidasAttribute> ATTRIBUTE_CLASS;

	EidasNaturalPersonAttributes(String name, String friendlyName, Class<? extends EidasAttribute> attrClass){
		NAME = name;
		FRIENDLY_NAME = friendlyName;
		ATTRIBUTE_CLASS = attrClass;
	}

	public static EidasNaturalPersonAttributes GetValueOf(String s) throws ErrorCodeException {
		for (EidasNaturalPersonAttributes enpa : EidasNaturalPersonAttributes.values()) {
			if (enpa.NAME.equals(s)) {
				return enpa;
			}
		}
		throw new ErrorCodeException(ErrorCode.ILLEGAL_REQUEST_SYNTAX, "Unsupported EidasNaturalPersonAttributes value:" + s);
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
