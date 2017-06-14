package eidassaml.test;

import java.io.IOException;

import org.junit.Test;

import eidassaml.starterkit.person_attributes.legal_persons_attributes.LegalAddressAttribute;
import eidassaml.starterkit.person_attributes.natural_persons_attribute.CurrentAddressAttribute;
import eidassaml.starterkit.template.TemplateLoader;

public class LegalAddressAttributeTest {
	
	@Test
	public void testGenerateLegalAddressAttribute() throws IOException {
		String locatorDesignator = "locatorDesignator";
		String thoroughfare = "thoroughfare";
		String postName = "postName";
		String postCode = "postCode";
		String pOBOX = "pOBOX";
		String locatorName = "locatorName";
		String cvaddressArea = "cvaddressArea";
		String adminunitFirstline = "adminunitFirstline";
		String adminunitSecondline = "adminunitSecondline";
		
		LegalAddressAttribute attribute = new LegalAddressAttribute(locatorDesignator, thoroughfare, postName, postCode, pOBOX, locatorName, cvaddressArea, adminunitFirstline, adminunitSecondline);
		TemplateLoader.init();
		String xml = attribute.generate();
		System.out.println(xml);
	}
	
	@Test
	public void testGenerateCurrentAddressAttribute() throws IOException {
		String locatorDesignator = "locatorDesignator";
		String thoroughfare = "thoroughfare";
		String postName = "postName";
		String postCode = "postCode";
		String pOBOX = "pOBOX";
		String locatorName = "locatorName";
		String cvaddressArea = "cvaddressArea";
		String adminunitFirstline = "adminunitFirstline";
		String adminunitSecondline = "adminunitSecondline";
		
		CurrentAddressAttribute attribute = new CurrentAddressAttribute(locatorDesignator, thoroughfare, postName, postCode, pOBOX, locatorName, cvaddressArea, adminunitFirstline, adminunitSecondline);
		TemplateLoader.init();
		String xml = attribute.generate();
		System.out.println(xml);
	}

}
