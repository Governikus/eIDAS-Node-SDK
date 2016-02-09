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
package de.governikus.eidassaml.starterkit;

public enum EidasNaturalPersonAttributes {
		
FirstName("http://eidas.europa.eu/attributes/naturalperson/CurrentGivenName", "FirstName"), 
BirthName("http://eidas.europa.eu/attributes/naturalperson/BirthName", "BirthName"),
FamilyName("http://eidas.europa.eu/attributes/naturalperson/CurrentFamilyName", "FamilyName"),
DateOfBirth("http://eidas.europa.eu/attributes/naturalperson/DateOfBirth", "DateOfBirth"),
PlaceOfBirth("http://eidas.europa.eu/attributes/naturalperson/PlaceOfBirth", "PlaceOfBirth"),
Gender("http://eidas.europa.eu/attributes/naturalperson/Gender", "Gender"),
CurrentAddress("http://eidas.europa.eu/attributes/naturalperson/CurrentAddress", "CurrentAddress"),
PersonIdentifier("http://eidas.europa.eu/attributes/naturalperson/PersonIdentifier", "PersonIdentifier");
	
	public final String NAME;
	public final String FRIENDLY_NAME;
	
	private EidasNaturalPersonAttributes(String name, String friendlyName){
		NAME = name;
		FRIENDLY_NAME = friendlyName;
	}
		
	public static EidasNaturalPersonAttributes GetValueOf(String s)
	{
		if(FirstName.NAME.equals(s))
		{
			return FirstName;
		}
		
		if(BirthName.NAME.equals(s))
		{
			return BirthName;
		}
		
		if(FamilyName.NAME.equals(s))
		{
			return FamilyName;
		}
		
		if(DateOfBirth.NAME.equals(s))
		{
			return DateOfBirth;
		}
		
		if(PlaceOfBirth.NAME.equals(s))
		{
			return PlaceOfBirth;
		}
		
		if(Gender.NAME.equals(s))
		{
			return Gender;
		}
		
		if(CurrentAddress.NAME.equals(s))
		{
			return CurrentAddress;
		}
		
		if(PersonIdentifier.NAME.equals(s))
		{
			return PersonIdentifier;
		}
		
		return null;
	}
	
}
