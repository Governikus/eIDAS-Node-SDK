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
package eidassaml.starterkit.person_attributes.natural_persons_attribute;

import eidassaml.starterkit.EidasAttribute;
import eidassaml.starterkit.EidasNaturalPersonAttributes;
import eidassaml.starterkit.Utils;
import eidassaml.starterkit.person_attributes.AbstractNonLatinScriptAttribute;
import eidassaml.starterkit.person_attributes.EidasPersonAttributes;

public class BirthNameAttribute extends AbstractNonLatinScriptAttribute {
	
	public BirthNameAttribute() {}

	public BirthNameAttribute(String value) {
		super(value);
	}

	@Override
	public String getTemplateName() {
		return Utils.IsNullOrEmpty(getLatinScript()) ? "birthName" : "birthName_transliterated";
	}

	public BirthNameAttribute(String latinScript, String nonLatinScript) {
		super(latinScript, nonLatinScript);
	}

	@Override
	public String type() {
		return EidasAttribute.TYPE_BirthName;
	}

	@Override
	public String toString() {
		return type() + " " + getNonLatinScript();
	}

	@Override
	public EidasPersonAttributes getPersonAttributeType() {
		return EidasNaturalPersonAttributes.BirthName;
	}	
}
