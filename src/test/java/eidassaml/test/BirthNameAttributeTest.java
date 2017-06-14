package eidassaml.test;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import eidassaml.starterkit.person_attributes.natural_persons_attribute.BirthNameAttribute;
import eidassaml.starterkit.template.TemplateLoader;

public class BirthNameAttributeTest {
	
	@Test
	public void testGenerateBirthNameAttributeWithNonLatinScript() throws IOException {
		TemplateLoader.init();
		BirthNameAttribute attribute = new BirthNameAttribute("name", "\u03A9\u03BD\u03AC\u03C3\u03B7\u03C2");
		String xml = attribute.generate();
		System.out.println(xml);
		Assert.assertTrue(xml.contains("LatinScript=\"false\">"));
		Assert.assertTrue(xml.contains("\u03A9\u03BD\u03AC\u03C3\u03B7\u03C2"));
	}
	
	@Test
	public void testGenerateBirthNameAttributeEmptyNonLatin() throws IOException {
		TemplateLoader.init();
		BirthNameAttribute attribute = new BirthNameAttribute("name", "");
		String xml = attribute.generate();
		System.out.println(xml);
		Assert.assertTrue(xml.contains("name"));
		Assert.assertFalse(xml.contains("LatinScript=\"false\">"));
	}
	
	@Test
	public void testGenerateBirthNameAttributeNullNonLatin() throws IOException {
		TemplateLoader.init();
		BirthNameAttribute attribute = new BirthNameAttribute("name", null);
		String xml = attribute.generate();
		System.out.println(xml);
		Assert.assertTrue(xml.contains("name"));
		Assert.assertFalse(xml.contains("LatinScript=\"false\">"));
	}
	
	@Test
	public void testGenerateBirthNameAttribute() throws IOException {
		TemplateLoader.init();
		BirthNameAttribute attribute = new BirthNameAttribute("name");
		String xml = attribute.generate();
		System.out.println(xml);
		Assert.assertTrue(xml.contains("name"));
		Assert.assertFalse(xml.contains("LatinScript=\"false\">"));
	}

}
