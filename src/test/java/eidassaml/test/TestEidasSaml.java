package eidassaml.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.bouncycastle.jcajce.provider.keystore.pkcs12.PKCS12KeyStoreSpi;
import org.junit.Before;
import org.junit.Test;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.encryption.EncryptionException;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.signature.SignatureException;

import eidassaml.starterkit.EidasAttribute;
import eidassaml.starterkit.EidasContactPerson;
import eidassaml.starterkit.EidasEncrypter;
import eidassaml.starterkit.EidasLoA;
import eidassaml.starterkit.EidasMetadataService;
import eidassaml.starterkit.EidasNameId;
import eidassaml.starterkit.EidasNameIdType;
import eidassaml.starterkit.EidasNaturalPersonAttributes;
import eidassaml.starterkit.EidasOrganisation;
import eidassaml.starterkit.EidasRequest;
import eidassaml.starterkit.EidasRequestSectorType;
import eidassaml.starterkit.EidasSaml;
import eidassaml.starterkit.EidasSigner;
import eidassaml.starterkit.ErrorCodeException;
import eidassaml.starterkit.Utils;
import eidassaml.starterkit.Utils.X509KeyPair;
import eidassaml.starterkit.person_attributes.EidasPersonAttributes;

public class TestEidasSaml {
	
	@Before
	public void setUp() throws Exception {		
		EidasSaml.Init();    
	}
	
	

	@Test
	public void testCreateRequest() throws CertificateException, IOException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, NoSuchProviderException, ConfigurationException, XMLParserException, UnmarshallingException, MarshallingException, SignatureException, TransformerFactoryConfigurationError, TransformerException, ErrorCodeException {
		String _issuer = "test issuer";
		String _destination= "test destination";
		String _providerName= "test providername";
		Map<EidasPersonAttributes, Boolean> _requestedAttributes= new HashMap<EidasPersonAttributes, Boolean>();
		_requestedAttributes.put(EidasNaturalPersonAttributes.FirstName, true);
		_requestedAttributes.put(EidasNaturalPersonAttributes.DateOfBirth, true);
		_requestedAttributes.put(EidasNaturalPersonAttributes.PersonIdentifier, true);
		_requestedAttributes.put(EidasNaturalPersonAttributes.Gender, true);
		EidasRequestSectorType _selectorType= EidasRequestSectorType.Public;
		EidasNameIdType _nameIdPolicy= EidasNameIdType.Persistent; 
		EidasLoA _loa= EidasLoA.Low;		
		List<X509Certificate> authors = new ArrayList<X509Certificate>();
		
		X509Certificate cert = Utils.readX509Certificate(TestEidasSaml.class.getResourceAsStream("/EidasSignerTest_x509.cer"));
		authors.add(cert);
		PrivateKey pk = (Utils.ReadPKCS12(TestEidasSaml.class.getResourceAsStream("/eidassignertest.p12"), "123456".toCharArray())).getKey();
		EidasSigner _signer= new EidasSigner(pk,cert);

		byte[] request = EidasSaml.CreateRequest(_issuer, _destination, _providerName, _signer, _requestedAttributes, _selectorType,_nameIdPolicy,_loa);
		EidasRequest result = EidasSaml.ParseRequest(new ByteArrayInputStream(request), authors);
		assertEquals(_issuer, result.getIssuer());
		assertEquals(_destination, result.getDestination());
		assertEquals(_providerName, result.getProviderName());
		assertEquals(_destination, result.getDestination());
		assertEquals(4,result.getRequestedAttributes().size());
	}

	//todo
	public void testParseRequestInputStreamListOfX509Certificate() {
		InputStream is = null;
		List<X509Certificate> authors = null;
		try {
			EidasSaml.ParseRequest(is, authors);
		} catch (ConfigurationException | XMLParserException | UnmarshallingException | ErrorCodeException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//todo
	public void testCreateResponse() {
		ArrayList<EidasAttribute> _att = null;
		String _destination = null;
		EidasNameId _nameid = null;
		String _issuer = null;
		String _inResponseTo = null;
		EidasEncrypter _encrypter = null; 
		EidasSigner _signer = null;
		try {
			EidasSaml.CreateResponse(_att, _destination, _nameid, _issuer, _inResponseTo, _encrypter,_signer);
		} catch (CertificateEncodingException | ConfigurationException | XMLParserException | IOException
				| UnmarshallingException | EncryptionException | MarshallingException | SignatureException
				| TransformerFactoryConfigurationError | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//todo
	public void testParseResponse() {
		InputStream is = null;
		X509KeyPair[] decryptionKeyPairs =null;  
		X509Certificate[] signatureAuthors = null;
		try {
			EidasSaml.ParseResponse(is,decryptionKeyPairs,signatureAuthors);
		} catch (ConfigurationException | XMLParserException | UnmarshallingException | ErrorCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	//todo
	public void testCreateMetaDataService() {
		String id= null;
		String entityId= null;
		Date validUntil= null;
		X509Certificate sigCert= null;
		X509Certificate encCert= null;
		EidasOrganisation organisation= null;
		EidasContactPerson technicalcontact= null;
		EidasContactPerson supportContact= null;
		String postEndpoint= null;
		String redirectEndpoint= null;
		List<EidasNameIdType> supportedNameIdTypes= null;
		List<EidasPersonAttributes> attributes= null;
		EidasSigner signer= null;
		try {
			byte[] mds = EidasSaml.CreateMetaDataService(id, entityId,validUntil,sigCert,encCert,organisation,technicalcontact,supportContact,postEndpoint,redirectEndpoint,supportedNameIdTypes,attributes,signer);
		} catch (CertificateEncodingException | ConfigurationException | IOException | XMLParserException
				| UnmarshallingException | MarshallingException | SignatureException
				| TransformerFactoryConfigurationError | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

}
