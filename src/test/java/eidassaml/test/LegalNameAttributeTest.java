package eidassaml.test;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import eidassaml.starterkit.person_attributes.legal_persons_attributes.LegalNameAttribute;
import eidassaml.starterkit.person_attributes.natural_persons_attribute.BirthNameAttribute;
import eidassaml.starterkit.person_attributes.natural_persons_attribute.FamilyNameAttribute;
import eidassaml.starterkit.person_attributes.natural_persons_attribute.GivenNameAttribute;
import eidassaml.starterkit.template.TemplateLoader;


public class LegalNameAttributeTest {

	@Test
	public void testGenerateLegalNameAttribute() throws IOException {
		TemplateLoader.init();
		LegalNameAttribute attribute = new LegalNameAttribute("name", "\u03A9\u03BD\u03AC\u03C3\u03B7\u03C2");
		String xml = attribute.generate();
		System.out.println(xml);
		Assert.assertTrue(xml.contains("LatinScript=\"false\">"));
		Assert.assertTrue(xml.contains("\u03A9\u03BD\u03AC\u03C3\u03B7\u03C2"));
	}
	
	@Test
	public void testGenerateBirthNameAttribute() throws IOException {
		TemplateLoader.init();
		BirthNameAttribute attribute = new BirthNameAttribute("name", "\u03A9\u03BD\u03AC\u03C3\u03B7\u03C2");
		String xml = attribute.generate();
		System.out.println(xml);
		Assert.assertTrue(xml.contains("LatinScript=\"false\">"));
		Assert.assertTrue(xml.contains("\u03A9\u03BD\u03AC\u03C3\u03B7\u03C2"));
	}
	
	@Test
	public void testGenerateFamilyNameAttribute() throws IOException {
		TemplateLoader.init();
		FamilyNameAttribute attribute = new FamilyNameAttribute("name", "\u03A9\u03BD\u03AC\u03C3\u03B7\u03C2");
		String xml = attribute.generate();
		System.out.println(xml);
		Assert.assertTrue(xml.contains("LatinScript=\"false\">"));
		Assert.assertTrue(xml.contains("\u03A9\u03BD\u03AC\u03C3\u03B7\u03C2"));
	}
	
	@Test
	public void testGenerateGivenNameAttribute() throws IOException {
		TemplateLoader.init();
		GivenNameAttribute attribute = new GivenNameAttribute("name", "\u03A9\u03BD\u03AC\u03C3\u03B7\u03C2");
		String xml = attribute.generate();
		System.out.println(xml);
		Assert.assertTrue(xml.contains("LatinScript=\"false\">"));
		Assert.assertTrue(xml.contains("\u03A9\u03BD\u03AC\u03C3\u03B7\u03C2"));
	}
}
