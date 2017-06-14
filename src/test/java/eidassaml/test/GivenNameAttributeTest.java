package eidassaml.test;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import eidassaml.starterkit.person_attributes.natural_persons_attribute.GivenNameAttribute;
import eidassaml.starterkit.template.TemplateLoader;

public class GivenNameAttributeTest {
	
	@Test
	public void testGenerateGivenNameAttributeWithNonLatinScript() throws IOException {
		TemplateLoader.init();
		GivenNameAttribute attribute = new GivenNameAttribute("name", "\u03A9\u03BD\u03AC\u03C3\u03B7\u03C2");
		String xml = attribute.generate();
		System.out.println(xml);
		Assert.assertTrue(xml.contains("name"));
		Assert.assertTrue(xml.contains("LatinScript=\"false\">"));
		Assert.assertTrue(xml.contains("\u03A9\u03BD\u03AC\u03C3\u03B7\u03C2"));
	}
	
	@Test
	public void testGenerateGivenNameAttributeEmptyNonLatin() throws IOException {
		TemplateLoader.init();
		GivenNameAttribute attribute = new GivenNameAttribute("name", "");
		String xml = attribute.generate();
		System.out.println(xml);
		Assert.assertTrue(xml.contains("name"));
		Assert.assertFalse(xml.contains("LatinScript=\"false\">"));
	}
	
	@Test
	public void testGenerateGivenNameAttributeNullNonLatin() throws IOException {
		TemplateLoader.init();
		GivenNameAttribute attribute = new GivenNameAttribute("name", null);
		String xml = attribute.generate();
		System.out.println(xml);
		Assert.assertTrue(xml.contains("name"));
		Assert.assertFalse(xml.contains("LatinScript=\"false\">"));
	}
	
	@Test
	public void testGenerateGivenNameAttribute() throws IOException {
		TemplateLoader.init();
		GivenNameAttribute attribute = new GivenNameAttribute("name");
		String xml = attribute.generate();
		System.out.println(xml);
		Assert.assertTrue(xml.contains("name"));
		Assert.assertFalse(xml.contains("LatinScript=\"false\">"));
	}

}
