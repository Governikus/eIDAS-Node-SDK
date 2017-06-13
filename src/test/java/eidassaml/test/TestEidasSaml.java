package eidassaml.test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.junit.Before;
import org.junit.Test;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.encryption.EncryptionException;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.signature.SignatureException;
import org.xml.sax.SAXException;

import eidassaml.starterkit.EidasAttribute;
import eidassaml.starterkit.EidasContactPerson;
import eidassaml.starterkit.EidasEncrypter;
import eidassaml.starterkit.EidasLegalPersonAttributes;
import eidassaml.starterkit.EidasLoA;
import eidassaml.starterkit.EidasMetadataService;
import eidassaml.starterkit.EidasNameId;
import eidassaml.starterkit.EidasNameIdType;
import eidassaml.starterkit.EidasNaturalPersonAttributes;
import eidassaml.starterkit.EidasOrganisation;
import eidassaml.starterkit.EidasPersistentNameId;
import eidassaml.starterkit.EidasRequest;
import eidassaml.starterkit.EidasRequestSectorType;
import eidassaml.starterkit.EidasResponse;
import eidassaml.starterkit.EidasSaml;
import eidassaml.starterkit.EidasSigner;
import eidassaml.starterkit.ErrorCodeException;
import eidassaml.starterkit.Utils;
import eidassaml.starterkit.Utils.X509KeyPair;
import eidassaml.starterkit.person_attributes.EidasPersonAttributes;
import eidassaml.starterkit.person_attributes.natural_persons_attribute.BirthNameAttribute;
import eidassaml.starterkit.person_attributes.natural_persons_attribute.CurrentAddressAttribute;
import eidassaml.starterkit.person_attributes.natural_persons_attribute.DateOfBirthAttribute;
import eidassaml.starterkit.person_attributes.natural_persons_attribute.FamilyNameAttribute;
import eidassaml.starterkit.person_attributes.natural_persons_attribute.GenderAttribute;
import eidassaml.starterkit.person_attributes.natural_persons_attribute.GenderType;
import eidassaml.starterkit.person_attributes.natural_persons_attribute.GivenNameAttribute;
import eidassaml.starterkit.person_attributes.natural_persons_attribute.PersonIdentifierAttribute;
import eidassaml.starterkit.person_attributes.natural_persons_attribute.PlaceOfBirthAttribute;

public class TestEidasSaml {

	@Before
	public void setUp() throws Exception {
		EidasSaml.Init();
	}

	@Test
	public void createParseRequest() throws CertificateException, IOException, UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, NoSuchProviderException, ConfigurationException,
			XMLParserException, UnmarshallingException, MarshallingException, SignatureException,
			TransformerFactoryConfigurationError, TransformerException, ErrorCodeException {
		String _issuer = "https://test/";
		String _destination = "test destination";
		String _providerName = "test providername";
		Map<EidasPersonAttributes, Boolean> _requestedAttributes = new HashMap<EidasPersonAttributes, Boolean>();
		_requestedAttributes.put(EidasNaturalPersonAttributes.BirthName, true);
		_requestedAttributes.put(EidasNaturalPersonAttributes.CurrentAddress, false);
		_requestedAttributes.put(EidasNaturalPersonAttributes.DateOfBirth, true);
		_requestedAttributes.put(EidasNaturalPersonAttributes.FamilyName, false);
		_requestedAttributes.put(EidasNaturalPersonAttributes.FirstName, true);
		_requestedAttributes.put(EidasNaturalPersonAttributes.Gender, true);
		_requestedAttributes.put(EidasNaturalPersonAttributes.PersonIdentifier, true);
		_requestedAttributes.put(EidasNaturalPersonAttributes.PlaceOfBirth, false);
		_requestedAttributes.put(EidasLegalPersonAttributes.LegalPersonIdentifier, true);
		_requestedAttributes.put(EidasLegalPersonAttributes.LegalName, true);
		_requestedAttributes.put(EidasLegalPersonAttributes.LegalAddress, true);
		_requestedAttributes.put(EidasLegalPersonAttributes.VATRegistration, true);
		_requestedAttributes.put(EidasLegalPersonAttributes.TaxReference, true);
		_requestedAttributes.put(EidasLegalPersonAttributes.D2012_17_EUIdentifier, true);
		_requestedAttributes.put(EidasLegalPersonAttributes.LEI, true);
		_requestedAttributes.put(EidasLegalPersonAttributes.EORI, true);
		_requestedAttributes.put(EidasLegalPersonAttributes.SEED, true);
		_requestedAttributes.put(EidasLegalPersonAttributes.SIC, true);
		EidasRequestSectorType _selectorType = EidasRequestSectorType.Public;
		EidasNameIdType _nameIdPolicy = EidasNameIdType.Persistent;
		EidasLoA _loa = EidasLoA.Low;
		List<X509Certificate> authors = new ArrayList<X509Certificate>();

		X509Certificate cert = Utils
				.readX509Certificate(TestEidasSaml.class.getResourceAsStream("/EidasSignerTest_x509.cer"));
		authors.add(cert);
		PrivateKey pk = (Utils.ReadPKCS12(TestEidasSaml.class.getResourceAsStream("/eidassignertest.p12"),
				"123456".toCharArray())).getKey();
		EidasSigner _signer = new EidasSigner(pk, cert);

		byte[] request = EidasSaml.CreateRequest(_issuer, _destination, _providerName, _signer, _requestedAttributes,
				_selectorType, _nameIdPolicy, _loa);
		String resultStr = new String(org.bouncycastle.util.encoders.Base64.encode(request), StandardCharsets.UTF_8);
		System.out.println("--->" + resultStr);
		EidasRequest result = EidasSaml.ParseRequest(new ByteArrayInputStream(request), authors);
		assertEquals(_issuer, result.getIssuer());
		assertEquals(_destination, result.getDestination());
		assertEquals(_providerName, result.getProviderName());
		assertEquals(_destination, result.getDestination());
		assertEquals(_requestedAttributes.size(), result.getRequestedAttributes().size());
		for (Map.Entry<EidasPersonAttributes, Boolean> entry : result.getRequestedAttributes()) {
			assertEquals(_requestedAttributes.get(entry.getKey()), entry.getValue());
		}
	}

	@Test
	public void requestFromXMLfile() throws IOException, CertificateException, ConfigurationException,
			XMLParserException, UnmarshallingException, ErrorCodeException {
		String _issuer = "https://test/";
		String _destination = "test destination";
		String _providerName = "test providername";
		Map<EidasPersonAttributes, Boolean> _requestedAttributes = new HashMap<EidasPersonAttributes, Boolean>();
		_requestedAttributes.put(EidasNaturalPersonAttributes.BirthName, true);
		_requestedAttributes.put(EidasNaturalPersonAttributes.CurrentAddress, false);
		_requestedAttributes.put(EidasNaturalPersonAttributes.DateOfBirth, true);
		_requestedAttributes.put(EidasNaturalPersonAttributes.FamilyName, false);
		_requestedAttributes.put(EidasNaturalPersonAttributes.FirstName, true);
		_requestedAttributes.put(EidasNaturalPersonAttributes.Gender, true);
		_requestedAttributes.put(EidasNaturalPersonAttributes.PersonIdentifier, true);
		_requestedAttributes.put(EidasNaturalPersonAttributes.PlaceOfBirth, false);
		_requestedAttributes.put(EidasLegalPersonAttributes.LegalPersonIdentifier, true);
		_requestedAttributes.put(EidasLegalPersonAttributes.LegalName, true);
		_requestedAttributes.put(EidasLegalPersonAttributes.LegalAddress, true);
		_requestedAttributes.put(EidasLegalPersonAttributes.VATRegistration, true);
		_requestedAttributes.put(EidasLegalPersonAttributes.TaxReference, true);
		_requestedAttributes.put(EidasLegalPersonAttributes.D2012_17_EUIdentifier, true);
		_requestedAttributes.put(EidasLegalPersonAttributes.LEI, true);
		_requestedAttributes.put(EidasLegalPersonAttributes.EORI, true);
		_requestedAttributes.put(EidasLegalPersonAttributes.SEED, true);
		_requestedAttributes.put(EidasLegalPersonAttributes.SIC, true);
		X509Certificate cert = Utils
				.readX509Certificate(TestEidasSaml.class.getResourceAsStream("/EidasSignerTest_x509.cer"));
		List<X509Certificate> authors = new ArrayList<X509Certificate>();
		authors.add(cert);


		byte[] request = Files.readAllBytes(Paths.get("src/test/resources/EidasSamlRequest_13062017.xml"));


		EidasRequest result = EidasSaml.ParseRequest(new ByteArrayInputStream(request), authors);
		assertEquals(_issuer, result.getIssuer());
		assertEquals(_destination, result.getDestination());
		assertEquals(_providerName, result.getProviderName());
		assertEquals(_destination, result.getDestination());
		assertEquals(_requestedAttributes.size(), result.getRequestedAttributes().size());
		for (Map.Entry<EidasPersonAttributes, Boolean> entry : result.getRequestedAttributes()) {
			assertEquals(_requestedAttributes.get(entry.getKey()), entry.getValue());
		}
	}

	@Test
	public void createParseResponse() throws SAXException, CertificateException, IOException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, NoSuchProviderException, KeyException, ConfigurationException, XMLParserException, UnmarshallingException, EncryptionException, MarshallingException, SignatureException, TransformerFactoryConfigurationError, TransformerException, ErrorCodeException {
		BirthNameAttribute birthName = new BirthNameAttribute("Meyer");
		CurrentAddressAttribute currentAddress = new CurrentAddressAttribute("Am Fallturm","33","Bremen","28207", "100", "bla", "bla", "bla", "bla");
		DateOfBirthAttribute dao = new DateOfBirthAttribute("1982-02-11");
		FamilyNameAttribute familyName =  new FamilyNameAttribute("Müller");
		GenderAttribute gender = new GenderAttribute(GenderType.Male);
		GivenNameAttribute givenName = new GivenNameAttribute("Bjørn");
		PersonIdentifierAttribute pi = new PersonIdentifierAttribute("test12321");
		PlaceOfBirthAttribute pob = new PlaceOfBirthAttribute("Saint-Étienne, France");		
		ArrayList<EidasAttribute> _att = new ArrayList<EidasAttribute>();
		_att.add(birthName);
		_att.add(currentAddress);
		_att.add(dao);
		_att.add(familyName);
		_att.add(gender);
		_att.add(givenName);
		_att.add(pi);
		_att.add(pob);		
		
		String _destination = "test destination";
		String destinationMetadata = "test_destination_metadata_url";
		EidasNameId _nameid = new EidasPersistentNameId("eidasnameidTest");
		String _issuer = "test issuer";
		String _inResponseTo = "test inResponseTo";
		X509Certificate[] cert = {Utils
				.readX509Certificate(TestEidasSaml.class.getResourceAsStream("/EidasSignerTest_x509.cer"))};
		X509KeyPair[] keypair = {Utils.ReadPKCS12(TestEidasSaml.class.getResourceAsStream("/eidassignertest.p12"),
				"123456".toCharArray())};
		PrivateKey pk = keypair[0].getKey();
		EidasEncrypter _encrypter = new EidasEncrypter(true, cert[0]);
		EidasSigner _signer = new EidasSigner(true, pk, cert[0]);
		
		byte[] response = EidasSaml.CreateResponse(_att, _destination, destinationMetadata, _nameid, _issuer, _inResponseTo, _encrypter, _signer);
		System.out.println("-->>Response-->>" + new String(response));
		
		EidasResponse result = EidasSaml.ParseResponse(new ByteArrayInputStream(response), keypair, cert);
		
		assertEquals(result.getDestination(),_destination);
		assertEquals(result.getNameId().getValue(),_nameid.getValue());
		assertEquals(result.getIssuer(),_issuer);
		assertEquals(result.getInResponseTo(),_inResponseTo);
		for (int i = 0; i < _att.size(); i++){
			assertEquals(result.getAttributes().get(i).getValue().replaceAll("\\s+",""),_att.get(i).getValue().replaceAll("\\s+",""));
		}
		
	}


	@Test
	public void createParseMetaDataService() throws ConfigurationException, IOException, XMLParserException, UnmarshallingException, MarshallingException, SignatureException, TransformerFactoryConfigurationError, TransformerException, CertificateException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, NoSuchProviderException, ParseException {
		String id = "test id";
		String entityId = "test entityid";
		Date validUntil = new SimpleDateFormat("dd.MM.yyyy").parse("01.01.2025");

		X509Certificate sigCert = Utils
				.readX509Certificate(TestEidasSaml.class.getResourceAsStream("/EidasSignerTest_x509.cer"));
		X509Certificate encCert = Utils
				.readX509Certificate(TestEidasSaml.class.getResourceAsStream("/EidasSignerTest_x509.cer"));
		EidasOrganisation organisation = new EidasOrganisation("eidas orga", "EIDAS LLC", "http://www.example.com", "en");
		EidasContactPerson technicalcontact = new EidasContactPerson("technical company", "Michelle", "Obama", "0123456789", "technical@example.com");
		EidasContactPerson supportContact = new EidasContactPerson("support  company", "Barack", "Obama",  "789", "support@example.com");;
		String postEndpoint = "post.eu/endpoint";
		String redirectEndpoint = "redirect.eu/endpoint";
		List<EidasNameIdType> supportedNameIdTypes = new ArrayList<EidasNameIdType>();
		supportedNameIdTypes.add(EidasNameIdType.Persistent);
		supportedNameIdTypes.add(EidasNameIdType.Transient);
		List<EidasPersonAttributes> attributes = new ArrayList<EidasPersonAttributes>();
		attributes.add(EidasNaturalPersonAttributes.BirthName);
		attributes.add(EidasNaturalPersonAttributes.CurrentAddress);
		attributes.add(EidasNaturalPersonAttributes.DateOfBirth);
		attributes.add(EidasNaturalPersonAttributes.FamilyName);
		attributes.add(EidasNaturalPersonAttributes.FirstName);
		attributes.add(EidasNaturalPersonAttributes.Gender);
		attributes.add(EidasNaturalPersonAttributes.PersonIdentifier);
		attributes.add(EidasNaturalPersonAttributes.PlaceOfBirth);
		
		PrivateKey pk = (Utils.ReadPKCS12(TestEidasSaml.class.getResourceAsStream("/eidassignertest.p12"),
				"123456".toCharArray())).getKey();
		EidasSigner signer = new EidasSigner(pk, sigCert);


		byte[] mds = EidasSaml.CreateMetaDataService(id, entityId, validUntil, EidasLoA.Substantial, sigCert, encCert, organisation,
					technicalcontact, supportContact, postEndpoint, redirectEndpoint, supportedNameIdTypes, attributes,
					signer);
		EidasMetadataService emds = EidasSaml.ParseMetaDataService(new ByteArrayInputStream(mds));
		assertEquals(emds.getEncCert(),encCert);
		assertEquals(emds.getEntityId(),entityId);
		assertEquals(emds.getId(),id);
		assertEquals(emds.getOrganisation().getName(),organisation.getName());
		assertEquals(emds.getOrganisation().getDisplayName(),organisation.getDisplayName());
		assertEquals(emds.getOrganisation().getLangId(),organisation.getLangId());
		assertEquals(emds.getOrganisation().getUrl(),organisation.getUrl());
		assertEquals(emds.getPostEndpoint(),postEndpoint);
		assertEquals(emds.getRedirectEndpoint(),redirectEndpoint);
		assertEquals(emds.getSigCert(),sigCert);		
		assertEquals(emds.getSupportcontact().getCompany(),supportContact.getCompany());
		assertEquals(emds.getSupportcontact().getEmail(),supportContact.getEmail());
		assertEquals(emds.getSupportcontact().getGivenName(), supportContact.getGivenName());
		assertEquals(emds.getSupportcontact().getSurName(), supportContact.getSurName());
		assertEquals(emds.getSupportcontact().getTel(),supportContact.getTel());
		assertEquals(emds.getTechnicalContact().getCompany(), technicalcontact.getCompany());
		assertEquals(emds.getTechnicalContact().getEmail(), technicalcontact.getEmail());
		assertEquals(emds.getTechnicalContact().getGivenName(), technicalcontact.getGivenName());
		assertEquals(emds.getTechnicalContact().getSurName(),technicalcontact.getSurName());
		assertEquals(emds.getTechnicalContact().getTel(),technicalcontact.getTel());
		assertEquals(emds.getValidUntil(),validUntil);
		assertEquals(emds.getAttributes().size(), attributes.size());
	}
}
