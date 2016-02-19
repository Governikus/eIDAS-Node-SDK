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

/**
 * 
 * @author hohnholt
 *
 */
public interface EidasAttribute {

	/**
	 * 
	 * @return An SAML XML attribute as String
	 */
	public String generate();
	
	/**
	 * 
	 * @return the type as string. Compare it with the Type consts of this class
	 */
	public String type();
	
	/**
	 * 
	 * @return the type as EidasNaturalPersonAttributes enum
	 */
	public EidasPersonAttributes getPersonAttributeType();

	/**
	 * Set the attribute value
	 */
	public void setValue(String value);

	/**
	 * Get a string-representation of the attribute value
	 * @return a String containing the attribute value
	 */
	public String getValue();


	/**
	 * Set the language for transliterated values
	 */
	default public void setTransliteratedLangId(String language){
		// Not implemented
	}


	public static final String TYPE_BirthName = "BirthNameAttribute";
	public static final String TYPE_CurrentAddress = "CurrentAddressAttribute";
	public static final String TYPE_DateOfBirth = "DateOfBirthAttribute";
	public static final String TYPE_FamilyName = "FamilyNameAttribute";
	public static final String TYPE_GivenName = "GivenNameAttribute";
	public static final String TYPE_PersonId = "PersonIdentifierAttribute";
	public static final String TYPE_PlaceOfBirth = "PlaceOfBirthAttribute";

	/** Legal Attributes **/
	public static final String TYPE_D_2012_17_EU_Identifier="D201217EUIdentifierAttribute";
	public static final String TYPE_EORI="EORIAttribute";
	public static final String TYPE_LegalEntityIdentifier="LegalEntityIdentifierAttribute";
	public static final String TYPE_LegalName="LegalNameAttribute";
	public static final String TYPE_LegalPersonAddress="LegalPersonAddressAttribute";
	public static final String TYPE_LegalPersonIdentifier="LegalPersonIdentifierAttribute";
	public static final String TYPE_SEED="SEEDAttribute";
	public static final String TYPE_SIC="SICAttribute";
	public static final String TYPE_TaxReference="TaxReferenceAttribute";
	public static final String TYPE_VATRegistration ="VatRegistrationAttribute";
	
	

	
}
