package eidassaml.starterkit.person_attributes;

import eidassaml.starterkit.EidasAttribute;
import eidassaml.starterkit.template.TemplateLoader;

public abstract class AbstractLatinScriptAttribute implements EidasAttribute {
	
	public final static String IS_LATIN_SCRIPT_ATTRIBUTENAME = "LatinScript";
	
	public AbstractLatinScriptAttribute() {
		
	}
	
	private String latinScript;
	
	public AbstractLatinScriptAttribute(String latinScript) {
		this.latinScript = latinScript;
	}
	
	public String getLatinScript() {
		return this.latinScript;
	}

	public void setLatinScript(String latinScript) {
		this.latinScript = latinScript;
	}

	/**
     * Return the template used for the generation of XML
     * @return The template of the NameAttribute, which will be used to create the XML representation of the attribute.
     */
    public abstract String getTemplateName();
	
	@Override
	public String generate() {
		String template = TemplateLoader.GetTemplateByName(getTemplateName());
		return template.replace("$latinScript", this.latinScript);
	}

}
