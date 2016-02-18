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
package eidassaml.starterkit.person_attributes;

import eidassaml.starterkit.EidasAttribute;
import eidassaml.starterkit.Utils;
import eidassaml.starterkit.template.TemplateLoader;

public abstract class AbstractAttribute implements EidasAttribute{

	public final static String IS_LATIN_SCRIPT_ATTRIBUTENAME = "LatinScript";

	private String value;
	private String transliteratedValue;
	private boolean isLatinScript = true;

	public AbstractAttribute(String value, String transliteratedValue) {
		super();
		this.value = value;
		this.transliteratedValue = transliteratedValue;
	}

	public AbstractAttribute(String value) {
		super();
		this.value = value;
		this.transliteratedValue = null;
	}

	public AbstractAttribute(){}

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

    /**
     * Return the template used for the generation of XML
     * @return The template of the NameAttribute, which will be used to create the XML representation of the attribute.
     */
    public abstract String getTemplateName();

	@Override
	public String generate() {
		String template = TemplateLoader.GetTemplateByName(getTemplateName());
        if (Utils.IsNullOrEmpty(transliteratedValue)){
            return template.replace("$value", value);
        }else{
            return template.replace("$value", value).replace("$transliteratedvalue", transliteratedValue)
                    .replace("$isLatinScript", String.valueOf(isLatinScript));
        }
	}

}
