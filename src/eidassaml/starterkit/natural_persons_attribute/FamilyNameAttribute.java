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

public class FamilyNameAttribute extends AbstractNameAttribute{

	public FamilyNameAttribute(String value, String transliteratedValue) {
		super(value,transliteratedValue);
	}
	
	public FamilyNameAttribute(String value) {
		super(value);
	}
	
	@Override
	public String type() {
		// TODO Auto-generated method stub
		return EidasAttribute.TYPE_FamilyName;
	}

	@Override
	public EidasNaturalPersonAttributes getNaturalPersonAttributeType() {
		// TODO Auto-generated method stub
		return EidasNaturalPersonAttributes.FamilyName;
	}
	
	@Override
	public String toString() {
		return type() + " " + this.getValue();
	}

	/*
	private String langId = "";
	private String value;
	private String transliteratedLangId = null;
	private String transliteratedValue = null;
	
	public FamilyNameAttribute(String langId, String value) {
		super();
		this.langId = langId;
		this.value = value;
	}
	
	public FamilyNameAttribute(String langId, String value,
			String transliteratedLangId, String transliteratedValue) {
		super();
		this.langId = langId;
		this.value = value;
		this.transliteratedLangId = transliteratedLangId;
		this.transliteratedValue = transliteratedValue;
	}
	
	public FamilyNameAttribute(String value) {
		super();
		this.value = value;
	}
	
	public FamilyNameAttribute( String value,
			String transliteratedLangId, String transliteratedValue) {
		super();
		this.value = value;
		this.transliteratedLangId = transliteratedLangId;
		this.transliteratedValue = transliteratedValue;
	}

	@Override
	public String generate() {
		
		if(!Utils.IsNullOrEmpty(this.transliteratedLangId) && !Utils.IsNullOrEmpty(this.transliteratedValue)){
			return TemplateLoader.GetTemplateByName("familyname_transliterated").replace("$languageID", langId).replace("$value", value).replace("$transliteratedlang", transliteratedLangId).replace("$transliteratedvalue", transliteratedValue);
		}
		
		return TemplateLoader.GetTemplateByName("familyname").replace("$languageID", langId).replace("$value", value);
	}

	@Override
	public String type() {
		return EidasAttribute.TYPE_FamilyName;
	}
	
	@Override
	public String toString() {
		return type() + " " + langId + " " + value;
	}

	public String getTransliteratedLangId() {
		return transliteratedLangId;
	}

	public void setTransliteratedLangId(String transliteratedLangId) {
		this.transliteratedLangId = transliteratedLangId;
	}

	public String getTransliteratedValue() {
		return transliteratedValue;
	}

	public void setTransliteratedValue(String transliteratedValue) {
		this.transliteratedValue = transliteratedValue;
	}
	
	
	
	public String getLangId() {
		return langId;
	}

	public void setLangId(String langId) {
		this.langId = langId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public EidasNaturalPersonAttributes getNaturalPersonAttributeType() {
		// TODO Auto-generated method stub
		return EidasNaturalPersonAttributes.FamilyName;
	}*/

}
