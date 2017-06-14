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
import eidassaml.starterkit.person_attributes.EidasPersonAttributes;
import eidassaml.starterkit.template.TemplateLoader;

public class GenderAttribute implements EidasAttribute{
	
	private GenderType value;
	
	public GenderAttribute(GenderType value) {
		super();
		this.value = value;
	}

	public GenderAttribute(){}

	public String getNonLatinScript() {
		return value.NAME;
	}

	public void setValue(GenderType value) {
		this.value = value;
	}
	
	@Override
	public String generate() {
		return TemplateLoader.GetTemplateByName("gender").replace("$value", value.NAME);
	}

	@Override
	public String type() {
		return "GenderAttribute";
	}
	
	@Override
	public EidasPersonAttributes getPersonAttributeType() {
		return EidasNaturalPersonAttributes.Gender;
	}

	@Override
	public void setLatinScript(String value) {
		this.value = GenderType.GetValueOf(value);
	}

	@Override
	public String getLatinScript() {
		return this.value.name();
	}


}
