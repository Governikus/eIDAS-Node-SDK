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
package eidassaml.starterkit.natural_persons_attribute;

import eidassaml.starterkit.EidasAttribute;
import eidassaml.starterkit.EidasNaturalPersonAttributes;
import eidassaml.starterkit.Utils;
import eidassaml.starterkit.template.TemplateLoader;

public class GivenNameAttribute extends AbstractNameAttribute{

	public GivenNameAttribute(String value) {
		super(value);
	}
	
	public GivenNameAttribute(String value, String transliteratedValue) {
		super(value,transliteratedValue);
	}

	
	@Override
	public String type() {
		return EidasAttribute.TYPE_GivenName;
	}
	
	@Override
	public String toString() {
		return type() + " " + this.getValue();
	}

	@Override
	public EidasNaturalPersonAttributes getNaturalPersonAttributeType() {
		return EidasNaturalPersonAttributes.FirstName;
	}
}
