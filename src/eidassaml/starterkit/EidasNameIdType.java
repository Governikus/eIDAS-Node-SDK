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

public enum EidasNameIdType {
	Persistent("urn:oasis:names:tc:SAML:2.0:nameid-format:persistent"),
	Transient("urn:oasis:names:tc:SAML:2.0:nameid-format:transient"),
	Unspecified("urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified");

	public final String NAME;
	
	private EidasNameIdType(String name)
	{
		NAME = name;
	}
	
	public static EidasNameIdType GetValueOf(String s)
	{
		if(Persistent.NAME.equals(s)){
			return Persistent;
		}
				
		if(Transient.NAME.equals(s)){
			return Transient;
		}
		
		if(Unspecified.NAME.equals(s)){
			return Unspecified;
		}
		
		return null;
	}
	
}
