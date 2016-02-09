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
package de.governikus.eidassaml.starterkit.natural_persons_attribute;

import de.governikus.eidassaml.starterkit.EidasAttribute;
import de.governikus.eidassaml.starterkit.EidasNaturalPersonAttributes;
import de.governikus.eidassaml.starterkit.Utils;
import de.governikus.eidassaml.starterkit.template.TemplateLoader;

public abstract class AbstractNameAttribute implements EidasAttribute{

	public final static String IS_LATIIN_SCRIPT_ATTRIBUTENAME= "LatinScript";
	
	private String value;
	private String transliteratedValue;
	private boolean isLatinScript = true;
		
	public AbstractNameAttribute(String value, String transliteratedValue) {
		super();
		this.value = value;
		this.transliteratedValue = transliteratedValue;
	}
	
	public AbstractNameAttribute(String value) {
		super();
		this.value = value;
		this.transliteratedValue = null;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTransliteratedValue() {
		return transliteratedValue;
	}

	public void setTransliteratedValue(String transliteratedValue) {
		this.transliteratedValue = transliteratedValue;
	}

	public boolean isLatinScript() {
		return isLatinScript;
	}

	public void setLatinScript(boolean isLatinScript) {
		this.isLatinScript = isLatinScript;
	}

	@Override
	public String generate() {
		String template = "";
		if(Utils.IsNullOrEmpty(transliteratedValue)){			
			if(this.getNaturalPersonAttributeType() == EidasNaturalPersonAttributes.FamilyName){
				template = TemplateLoader.GetTemplateByName("familyname");
			}else if(this.getNaturalPersonAttributeType() == EidasNaturalPersonAttributes.FirstName){
				template = TemplateLoader.GetTemplateByName("givenname");
			}else{
				template = TemplateLoader.GetTemplateByName("birthName");
			}
			return template.replace("$value", value);
		}else{
			
			if(this.getNaturalPersonAttributeType() == EidasNaturalPersonAttributes.FamilyName){
				template = TemplateLoader.GetTemplateByName("familyname_transliterated");
			}else if(this.getNaturalPersonAttributeType() == EidasNaturalPersonAttributes.FirstName){
				template = TemplateLoader.GetTemplateByName("givenname_transliterated");
			}else{
				template = TemplateLoader.GetTemplateByName("birthName_transliterated");
			}
			return template.replace("$value", value).replace("$transliteratedvalue", transliteratedValue)
					.replace("$isLatinScript", String.valueOf(isLatinScript));
		}
	}
	
	
	
}
