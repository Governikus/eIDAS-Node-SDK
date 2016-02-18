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
import eidassaml.starterkit.person_attributes.AbstractAttribute;
import eidassaml.starterkit.person_attributes.EidasPersonAttributes;
import eidassaml.starterkit.template.TemplateLoader;

public class BirthNameAttribute extends AbstractAttribute {

	public BirthNameAttribute(){}
	public BirthNameAttribute(String value) {
		super(value);
	}

	@Override
	public String getTemplateName() {
		return "birthName";
	}

	public BirthNameAttribute(String value, String transliteratedValue) {
		super(value,transliteratedValue);
	}

	@Override
	public String type() {
		// TODO Auto-generated method stub
		return EidasAttribute.TYPE_BirthName;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return type() + " " + getValue();
	}

	@Override
	public EidasPersonAttributes getPersonAttributeType() {
		// TODO Auto-generated method stub
		return EidasNaturalPersonAttributes.BirthName;
	}	
}
