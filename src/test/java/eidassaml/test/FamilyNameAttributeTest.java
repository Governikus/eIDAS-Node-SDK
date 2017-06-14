package eidassaml.test;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import eidassaml.starterkit.person_attributes.natural_persons_attribute.FamilyNameAttribute;
import eidassaml.starterkit.template.TemplateLoader;

public class FamilyNameAttributeTest {

	@Test
	public void testGenerateFamilyNameAttributeWithNonLatinScript() throws IOException {
		TemplateLoader.init();
		FamilyNameAttribute attribute = new FamilyNameAttribute("name", "\u03A9\u03BD\u03AC\u03C3\u03B7\u03C2");
		String xml = attribute.generate();
		System.out.println(xml);
		Assert.assertTrue(xml.contains("LatinScript=\"false\">"));
		Assert.assertTrue(xml.contains("\u03A9\u03BD\u03AC\u03C3\u03B7\u03C2"));
	}
	
	@Test
	public void testGenerateFamilyNameAttributeEmptyNonLatin() throws IOException {
		TemplateLoader.init();
		FamilyNameAttribute attribute = new FamilyNameAttribute("name", "");
		String xml = attribute.generate();
		System.out.println(xml);
		Assert.assertTrue(xml.contains("name"));
		Assert.assertFalse(xml.contains("LatinScript=\"false\">"));
	}
	
	@Test
	public void testGenerateFamilyNameAttributeNullNonLatin() throws IOException {
		TemplateLoader.init();
		FamilyNameAttribute attribute = new FamilyNameAttribute("name", null);
		String xml = attribute.generate();
		System.out.println(xml);
		Assert.assertTrue(xml.contains("name"));
		Assert.assertFalse(xml.contains("LatinScript=\"false\">"));
	}
	
	@Test
	public void testGenerateFamilyNameAttribute() throws IOException {
		TemplateLoader.init();
		FamilyNameAttribute attribute = new FamilyNameAttribute("name");
		String xml = attribute.generate();
		System.out.println(xml);
		Assert.assertTrue(xml.contains("name"));
		Assert.assertFalse(xml.contains("LatinScript=\"false\">"));
	}
}
