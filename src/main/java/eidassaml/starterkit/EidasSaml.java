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
package eidassaml.starterkit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import eidassaml.starterkit.person_attributes.EidasPersonAttributes;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.opensaml.DefaultBootstrap;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.encryption.EncryptionException;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.signature.SignatureException;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.ValidatorSuite;
import org.xml.sax.SAXException;

import eidassaml.starterkit.Utils.X509KeyPair;
import eidassaml.starterkit.template.TemplateLoader;

/**
 * Put all method together for creating, validating and parsing of saml messages and make it easy.
 * Using the methods of this class will init opensaml automatecally
 * 
 * 
 * 
 * @author hohnholt
 *
 */
public class EidasSaml {

	private static boolean isInit = false;
	
	/**
	 * Inits the OpenSAML library and the EidasSaml Starterkit library.
	 * Set BouncyCastleProvider as Security Provider
	 * It is nessesary to call this method! 
	 * 
	 * @throws ConfigurationException if there is a problem to init the OpenSAML lib or the eidassaml templates
	 */
	public static synchronized void Init() throws ConfigurationException
	{
		if(!isInit)
		{
			DefaultBootstrap.bootstrap(); 
			try {
				TemplateLoader.init();
			} catch (IOException e) {
				throw new ConfigurationException("EidasSaml: Can not init Templateloader. SAML Message will not build correctly!",e);
			}
			Security.addProvider(new BouncyCastleProvider());
			org.apache.xml.security.algorithms.JCEMapper.setProviderId("BC");
			isInit=true;
		}
	}
	
	/**
	 * Creates a signed eidas saml request.
	 * Sets the Level of Assurance to http://eidas.europa.eu/LoA/high
	 * 
	 * 
	 * 
	 * @param _issuer the name of the requester
	 * @param _destination the response destination
	 * @param _providerName the response provider
	 * @param _signer the author of the message
	 * @param _requestedAttributes a list of the requestAttributes
	 * @return signed saml xml request as byte array
	 * @throws ConfigurationException thrown if the opensaml lib or eidas starterkit lib is not init
	 * @throws CertificateEncodingException thrown if the signer is not valid
	 * @throws IOException there multiple reason why this can be thrown
	 * @throws XMLParserException thrown if there is a problem in the saml request xml
	 * @throws UnmarshallingException thrown if there is a problem to create the saml request
	 * @throws MarshallingException thrown if there is a problem to create the saml request
	 * @throws SignatureException thrown if there is a problem to create the saml request (while signing)
	 * @throws TransformerFactoryConfigurationError thrown if there is a problem to create the saml request
	 * @throws TransformerException thrown if there is a problem to create the saml request
	 */
	public static byte[] CreateRequest(String _issuer, String _destination, String _providerName, EidasSigner _signer, Map<EidasPersonAttributes, Boolean> _requestedAttributes) throws ConfigurationException, CertificateEncodingException, IOException, XMLParserException, UnmarshallingException, MarshallingException, SignatureException, TransformerFactoryConfigurationError, TransformerException{
		Init();
		EidasRequest eidasRequest = new EidasRequest(_destination, _issuer,_providerName, _signer);
		return eidasRequest.generate(_requestedAttributes);
	}
	
	public static byte[] CreateRequest(String _issuer, String _destination, EidasSigner _signer, Map<EidasPersonAttributes, Boolean> _requestedAttributes) throws ConfigurationException, CertificateEncodingException, IOException, XMLParserException, UnmarshallingException, MarshallingException, SignatureException, TransformerFactoryConfigurationError, TransformerException{
		Init();
		EidasRequest eidasRequest = new EidasRequest(_destination, _issuer,Constants.DefaultProviderName, _signer);
		return eidasRequest.generate(_requestedAttributes);
	}
	
	/**
	 * 
	 * Creates a signed eidas saml request
	 * 
	 * @param _issuer the name of the requester
	 * @param _destination the response destination
	 * @param _providerName the response provider
	 * @param _signer the author of the message
	 * @param _requestedAttributes a list of the requestAttributes
	 * @param _selectorType private sector or public sector SP
	 * @param _nameIdPolicy defines the treatment of identifiers to be used in a cross-border context
	 * @param _loa  the Level of Assurance 
	 * @return signed saml xml request as byte array
	 * @throws ConfigurationException thrown if the opensaml lib or eidas starterkit lib is not init
	 * @throws CertificateEncodingException thrown if the signer is not valid
	 * @throws IOException there multiple reason why this can be thrown
	 * @throws XMLParserException thrown if there is a problem in the saml request xml
	 * @throws UnmarshallingException thrown if there is a problem to create the saml request
	 * @throws MarshallingException thrown if there is a problem to create the saml request
	 * @throws SignatureException thrown if there is a problem to create the saml request (while signing)
	 * @throws TransformerFactoryConfigurationError thrown if there is a problem to create the saml request
	 * @throws TransformerException thrown if there is a problem to create the saml request
	 */
	public static byte[] CreateRequest(String _issuer, String _destination,String _providerName, EidasSigner _signer, Map<EidasPersonAttributes, Boolean> _requestedAttributes,EidasRequestSectorType _selectorType, EidasNameIdType _nameIdPolicy, EidasLoA _loa) throws ConfigurationException, CertificateEncodingException, IOException, XMLParserException, UnmarshallingException, MarshallingException, SignatureException, TransformerFactoryConfigurationError, TransformerException{
		Init();
		EidasRequest eidasRequest = new EidasRequest(_destination,_selectorType,_nameIdPolicy,_loa, _issuer,_providerName, _signer);
		return eidasRequest.generate(_requestedAttributes);
	}
	
	public static byte[] CreateRequest(String _issuer, String _destination, EidasSigner _signer, Map<EidasPersonAttributes, Boolean> _requestedAttributes,EidasRequestSectorType _selectorType, EidasNameIdType _nameIdPolicy, EidasLoA _loa) throws ConfigurationException, CertificateEncodingException, IOException, XMLParserException, UnmarshallingException, MarshallingException, SignatureException, TransformerFactoryConfigurationError, TransformerException{
		Init();
		EidasRequest eidasRequest = new EidasRequest(_destination,_selectorType,_nameIdPolicy,_loa, _issuer,Constants.DefaultProviderName, _signer);
		return eidasRequest.generate(_requestedAttributes);
	}
	
	/**
	 * Read a eidas saml request xml and creats a EidasRequest object
	 * 
	 * @param is the eidas saml request
	 * @return a representation of the eidas saml request
	 * @throws ConfigurationException thrown if the opensaml lib or eidas starterkit lib is not init
	 * @throws XMLParserException thrown if there is a problem in the saml request xml
	 * @throws UnmarshallingException thrown if there is a problem in the saml request xml
	 * @throws ErrorCodeException thrown if there is a problem in the saml request xml
	 * @throws IOException 
	 */
	public static EidasRequest ParseRequest(InputStream is) throws ConfigurationException, XMLParserException, UnmarshallingException, ErrorCodeException, IOException{
		Init();
		return EidasRequest.Parse(is);
	}
	

	/**
	 * Read a eidas saml request xml and checks the signatures
	 * 
	 * @param is the eidas saml request
	 * @param authors a list of author certificates to check the signaures
	 * @return a representation of the eidas saml request
	 * @throws ConfigurationException thrown if the opensaml lib or eidas starterkit lib is not init
	 * @throws XMLParserException thrown if there is a problem in the saml request xml
	 * @throws UnmarshallingException thrown if there is a problem in the saml request xml
	 * @throws ErrorCodeException thrown if there is a problem in the saml request xml
	 * @throws IOException 
	 */
	public static EidasRequest ParseRequest(InputStream is, List<X509Certificate> authors) throws ConfigurationException, XMLParserException, UnmarshallingException, ErrorCodeException, IOException{
		Init();
		return EidasRequest.Parse(is,authors);
	}
	
	/**
	 * Creates a signed eidas saml response. the Assertion is encrypted
	 * 
	 * 
	 * 
	 * @param _att the values of the requested attributes
	 * @param _destination the response destination
	 * @param _nameid defines the treatment of identifiers to be used in a cross-border context
	 * @param _issuer the name of the response sender 
	 * @param _inResponseTo the responceTo id
	 * @param _encrypter the reader of the requested attributes
	 * @param _signer the author of this message 
	 * @return signed encrypted saml xml response as byte array
	 * @throws ConfigurationException thrown if the opensaml lib or eidas starterkit lib is not init
	 * @throws CertificateEncodingException thrown if there any problems with the used certificates
	 * @throws XMLParserException thrown if there any problem to create the message
	 * @throws IOException there multiple reason why this can be thrown
	 * @throws UnmarshallingException thrown if there any problem to create the message
	 * @throws EncryptionException 
	 * @throws MarshallingException thrown if there any problem to create the message
	 * @throws SignatureException 
	 * @throws TransformerFactoryConfigurationError thrown if there any problem to create the message
	 * @throws TransformerException thrown if there any problem to create the message
	 */
	public static byte[] CreateResponse(ArrayList<EidasAttribute> _att, String _destination, EidasNameId _nameid, String _issuer, String _inResponseTo, EidasEncrypter _encrypter,EidasSigner _signer) throws ConfigurationException, CertificateEncodingException, XMLParserException, IOException, UnmarshallingException, EncryptionException, MarshallingException, SignatureException, TransformerFactoryConfigurationError, TransformerException
	{
		Init();
		EidasResponse response = new EidasResponse(_att, _destination, _nameid,_inResponseTo, _issuer, _signer, _encrypter);
		return response.generate();	
	}
	
	/**
	 * 
	 * @param is
	 * @param decryptionKeyPairs
	 * @param signatureAuthors
	 * @return
	 * @throws ConfigurationException
	 * @throws XMLParserException
	 * @throws UnmarshallingException
	 * @throws ErrorCodeException
	 */
	public static EidasResponse ParseResponse(InputStream is,X509KeyPair[] decryptionKeyPairs, X509Certificate[] signatureAuthors) throws ConfigurationException, XMLParserException, UnmarshallingException, ErrorCodeException
	{
		Init();
		return EidasResponse.Parse(is, decryptionKeyPairs, signatureAuthors);
	}
	
	/**
	 * 
	 * @param id
	 * @param entityId
	 * @param validUntil
	 * @param sigCert
	 * @param encCert
	 * @param organisation
	 * @param technicalcontact
	 * @param postEndpoint
	 * @param redirectEndpoint
	 * @param supportedNameIdTypes
	 * @param attributes
	 * @param signer
	 * @return
	 * @throws ConfigurationException
	 * @throws CertificateEncodingException
	 * @throws IOException
	 * @throws XMLParserException
	 * @throws UnmarshallingException
	 * @throws MarshallingException
	 * @throws SignatureException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	public static byte[] CreateMetaDataService(String id, String entityId, Date validUntil, X509Certificate sigCert, X509Certificate encCert, EidasOrganisation organisation, EidasContactPerson technicalcontact, EidasContactPerson supportContact, String postEndpoint, String redirectEndpoint, List<EidasNameIdType> supportedNameIdTypes, List<EidasPersonAttributes> attributes, EidasSigner signer) throws ConfigurationException, CertificateEncodingException, IOException, XMLParserException, UnmarshallingException, MarshallingException, SignatureException, TransformerFactoryConfigurationError, TransformerException{
		Init();
		EidasMetadataService meta = new EidasMetadataService(id, entityId, validUntil, sigCert, encCert, organisation, technicalcontact, supportContact, postEndpoint, redirectEndpoint, supportedNameIdTypes);
		return meta.generate(attributes, signer);
	}
	
	/**
	 * 
	 * @param is
	 * @return
	 * @throws ConfigurationException
	 * @throws CertificateException
	 * @throws XMLParserException
	 * @throws UnmarshallingException
	 * @throws IOException
	 */
	public static EidasMetadataService ParseMetaDataService(InputStream is) throws ConfigurationException, CertificateException, XMLParserException, UnmarshallingException, IOException
	{
		Init();
		return EidasMetadataService.Parse(is);
	}
	
	/**
	 * 
	 * @param id
	 * @param entityId
	 * @param validUntil
	 * @param sigCert
	 * @param encCert
	 * @param organisation
	 * @param postEndpoint
	 * @param _spType
	 * @param supportedNameIdTypes
	 * @param signer
	 * @return
	 * @throws ConfigurationException
	 * @throws CertificateEncodingException
	 * @throws IOException
	 * @throws XMLParserException
	 * @throws UnmarshallingException
	 * @throws MarshallingException
	 * @throws SignatureException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	public static byte[] CreateMetaDataNode(String id, String entityId, Date validUntil, X509Certificate sigCert, X509Certificate encCert, EidasOrganisation organisation, EidasContactPerson techcontact, EidasContactPerson supportcontact,String postEndpoint, EidasRequestSectorType _spType, List<EidasNameIdType> supportedNameIdTypes, EidasSigner signer) throws ConfigurationException, CertificateEncodingException, IOException, XMLParserException, UnmarshallingException, MarshallingException, SignatureException, TransformerFactoryConfigurationError, TransformerException
	{
		Init();
		EidasMetadataNode meta = new EidasMetadataNode(id, entityId, validUntil, sigCert, encCert, organisation, techcontact, supportcontact, postEndpoint, _spType, supportedNameIdTypes);
		return meta.generate( signer);
	}
	
	/**
	 * 
	 * @param is
	 * @return
	 * @throws ConfigurationException
	 * @throws CertificateException
	 * @throws XMLParserException
	 * @throws UnmarshallingException
	 * @throws IOException
	 */
	public static EidasMetadataNode ParseMetaDataNode(InputStream is) throws ConfigurationException, CertificateException, XMLParserException, UnmarshallingException, IOException
	{
		Init();
		return EidasMetadataNode.Parse(is);
	}
	
	/**
	 * Raise an Exception if the eidasrequest is not saml valid
	 * 
	 * @param request
	 * @throws ValidationException
	 * @throws ConfigurationException 
	 */
	public static void ValidateEidasRequest(EidasRequest request) throws ValidationException, ConfigurationException{
		Init();
		ValidatorSuite schemaValidators=org.opensaml.Configuration.getValidatorSuite("saml2-core-schema-validator");
		schemaValidators.validate(request.getAuthnRequest());
	}
	

	/**
	 * Validates a saml message with the saml-schema-protocol-2_0.xsd, saml-schema-assertion-2_0.xsd, xenc-schema.xsd, xmldsig-core-schema.xsd,NaturalPersonShema.xsd
	 * If the message is not valid a SAXException will be thrown
	 * 
	 * @param is the saml message as stream
	 * @param resetStreamAfterValidation if u like to parse the given stream later u have to reset the stream
	 * @throws SAXException if the given saml message is not vaild
	 * @throws IOException if there is a problem to read the stream
	 */
	public static void ValidateXMLRequest(InputStream is,boolean resetStreamAfterValidation) throws SAXException, IOException{
		
		SchemaFactory sf = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
		
		StreamSource s2 = new StreamSource(EidasSaml.class.getResourceAsStream("/saml-schema-protocol-2_0.xsd"));
		StreamSource s1 = new StreamSource(EidasSaml.class.getResourceAsStream("/saml-schema-assertion-2_0.xsd"));
		StreamSource s3 = new StreamSource(EidasSaml.class.getResourceAsStream("/xenc-schema.xsd"));
		StreamSource s4 = new StreamSource(EidasSaml.class.getResourceAsStream("/xmldsig-core-schema.xsd"));
		StreamSource s5 = new StreamSource(EidasSaml.class.getResourceAsStream("/NaturalPersonShema.xsd"));
		
		Schema schema = sf.newSchema(new StreamSource[]{s5,s4,s3,s1,s2});
		Validator validator = schema.newValidator();
		validator.validate(new StreamSource(is));
		if(resetStreamAfterValidation)
			is.reset();//this is imported if u try to parse the stream later
	}
	
}
