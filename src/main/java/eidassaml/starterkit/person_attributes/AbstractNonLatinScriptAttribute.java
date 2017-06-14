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

import eidassaml.starterkit.Utils;
import eidassaml.starterkit.template.TemplateLoader;

public abstract class AbstractNonLatinScriptAttribute extends AbstractLatinScriptAttribute {

	private String nonLatinScript;
	
	public AbstractNonLatinScriptAttribute() {
		
	}

	public AbstractNonLatinScriptAttribute(String latinScript) {
		super(latinScript);
		this.nonLatinScript = null;
	}

	public AbstractNonLatinScriptAttribute(String latinScript, String nonLatinScript) {
		super(latinScript);
		this.nonLatinScript = nonLatinScript;
	}

	public String getNonLatinScript() {
		return this.nonLatinScript;
	}

	public void setNonLatinScript(String nonLatinScript) {
		this.nonLatinScript = nonLatinScript;
	}

    /**
     * Return the template used for the generation of XML
     * @return The template of the NameAttribute, which will be used to create the XML representation of the attribute.
     */
    public abstract String getTemplateName();

	@Override
	public String generate() {
		String template = TemplateLoader.GetTemplateByName(getTemplateName());
        if (Utils.IsNullOrEmpty(this.nonLatinScript)){
            return template.replace("$latinScript", super.getLatinScript());
        }else{
            return template.replace("$latinScript", super.getLatinScript()).replace("$nonLatinScript", this.nonLatinScript);
        }
	}

}
