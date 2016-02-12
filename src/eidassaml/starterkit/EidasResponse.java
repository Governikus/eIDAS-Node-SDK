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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.opensaml.Configuration;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.EncryptedAssertion;
import org.opensaml.saml2.core.Status;
import org.opensaml.saml2.core.StatusCode;
import org.opensaml.saml2.core.impl.StatusResponseTypeImpl;
import org.opensaml.saml2.encryption.Decrypter;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.encryption.DecryptionException;
import org.opensaml.xml.encryption.EncryptionException;
import org.opensaml.xml.encryption.InlineEncryptedKeyResolver;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.keyinfo.StaticKeyInfoCredentialResolver;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureException;
import org.opensaml.xml.signature.Signer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import eidassaml.starterkit.Constants;
import eidassaml.starterkit.EidasEncrypter;
import eidassaml.starterkit.EidasSigner;
import eidassaml.starterkit.ErrorCode;
import eidassaml.starterkit.ErrorCodeException;
import eidassaml.starterkit.Utils;
import eidassaml.starterkit.XMLSignatureHandler;
import eidassaml.starterkit.natural_persons_attribute.AbstractNameAttribute;
import eidassaml.starterkit.natural_persons_attribute.BirthNameAttribute;
import eidassaml.starterkit.natural_persons_attribute.CurrentAddressAttribute;
import eidassaml.starterkit.natural_persons_attribute.DateOfBirthAttribute;
import eidassaml.starterkit.natural_persons_attribute.FamilyNameAttribute;
import eidassaml.starterkit.natural_persons_attribute.GivenNameAttribute;
import eidassaml.starterkit.natural_persons_attribute.PersonIdentifyerAttribute;
import eidassaml.starterkit.natural_persons_attribute.PlaceOfBirthAttribute;
import eidassaml.starterkit.template.TemplateLoader;

/**
 * 
 * @author hohnholt
 *
 */
public class EidasResponse {

	static final long ONE_MINUTE_IN_MILLIS=60000;//millisecs
	public final static SimpleDateFormat SimpleDf = Constants.SimpleSamlDf;
	
	private String id;
	private String destination;
	private String issuer;
	private String inResponseTo;
	private String issueInstant;
	
	private EidasEncrypter encrypter;
	private EidasSigner signer;
	private ArrayList<EidasAttribute> attributes;
	private EidasNameId nameId = null;
	//private EidasNameIdType nameIdPolicy = EidasNameIdType.Transient;
	
	private org.opensaml.saml2.core.Response openSamlResp = null;
	
	private EidasResponse(){
		attributes = new ArrayList<EidasAttribute>();
	}
	
	public EidasResponse(String _destination, EidasNameId _nameid, 
			String _inResponseTo, 
			String _issuer, 
			EidasSigner _signer,
			EidasEncrypter _encrypter){
		id = "_" + Utils.GenerateUniqueID();
		nameId = _nameid;
		destination = _destination;
		inResponseTo = _inResponseTo;
		issuer = _issuer;
		issueInstant = SimpleDf.format(new Date());
		encrypter = _encrypter;
		signer = _signer;
		attributes = new ArrayList<EidasAttribute>();
	}
	
	public EidasResponse(ArrayList<EidasAttribute> att, String _destination, EidasNameId _nameid,
			String _inResponseTo, 
			String _issuer, 
			EidasSigner _signer,
			EidasEncrypter _encrypter){
		id = "_" + Utils.GenerateUniqueID();
		nameId = _nameid;
		destination = _destination;
		inResponseTo = _inResponseTo;
		issuer = _issuer;
		issueInstant = SimpleDf.format(new Date());
		encrypter = _encrypter;
		signer = _signer;
		attributes = att;
	}
	
	public byte[] generateErrorRsp(ErrorCode code, String... msg) throws IOException, XMLParserException, UnmarshallingException, CertificateEncodingException, MarshallingException, SignatureException, TransformerFactoryConfigurationError, TransformerException
	{
		BasicParserPool ppMgr = new BasicParserPool();
		ppMgr.setNamespaceAware(true);
		
		byte[] returnValue;
		
		String respTemp = TemplateLoader.GetTemplateByName("failresp");
		
		respTemp = respTemp.replace("$InResponseTo", inResponseTo);
		respTemp =respTemp.replace("$IssueInstant", issueInstant);
		respTemp =respTemp.replace("$Issuer", issuer);
		respTemp =respTemp.replace("$Id",id);
		respTemp =respTemp.replace("$Destination",destination);
		respTemp =respTemp.replace("$Code",code.getSamlStatus());
		if(msg == null){
			respTemp =respTemp.replace("$ErrMsg",code.toDescription());
		}else{
			respTemp =respTemp.replace("$ErrMsg",code.toDescription(msg));
		}
		
		List<Signature> sigs = new ArrayList<Signature>();
		
		try( InputStream is = new ByteArrayInputStream(respTemp.getBytes(Constants.UTF8_CHARSET))){
			Document inCommonMDDoc = ppMgr.parse(is);
			Element metadataRoot = inCommonMDDoc.getDocumentElement();		 
			// Get apropriate unmarshaller
			UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
			Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(metadataRoot);
			org.opensaml.saml2.core.Response resp = (org.opensaml.saml2.core.Response)unmarshaller.unmarshall(metadataRoot);
			
			XMLSignatureHandler.addSignature(resp, signer.getSigKey(), signer.getSigCert(), signer.getSigType(), signer.getSigDigestAlg());
			
			if (resp.getSignature() != null && signer != null)
		      {
		        sigs.add(resp.getSignature());
		      }
		      


		      Marshaller rm = Configuration.getMarshallerFactory().getMarshaller(resp.getElementQName());
		      Element all = rm.marshall(resp);
		      if (resp.getSignature() != null && signer != null)
		      {
		        sigs.add(resp.getSignature());
		      }
		      if(signer != null)
		    	  Signer.signObjects(sigs);

		      openSamlResp = resp;
		      Transformer trans = TransformerFactory.newInstance().newTransformer();
		      // Please note: you cannot format the output without breaking signature!		      
		      try(ByteArrayOutputStream bout = new ByteArrayOutputStream()){
		    	  trans.transform(new DOMSource(all), new StreamResult(bout));
		    	  returnValue = bout.toByteArray();
		      }
			
		}
		
		return returnValue;
		
	}
		
	public byte[] generate() throws XMLParserException, IOException, UnmarshallingException, CertificateEncodingException, EncryptionException, MarshallingException, SignatureException, TransformerFactoryConfigurationError, TransformerException{
		
		BasicParserPool ppMgr = new BasicParserPool();
		ppMgr.setNamespaceAware(true);
		
		byte[] returnValue;
		
		String notBefore = SimpleDf.format(new Date(new Date().getTime() + (10 * ONE_MINUTE_IN_MILLIS))); //TODO set + 10min
		String notAfter = SimpleDf.format(new Date()); 
		String respTemp = TemplateLoader.GetTemplateByName("resp");
		String assoTemp = TemplateLoader.GetTemplateByName("asso");
		StringBuilder attributeString = new StringBuilder();

		if(nameId == null)
		{
			throw new XMLParserException("Document does not contains a NameID value");
		}
		
		for(EidasAttribute eidasAtt : this.attributes)
		{
			attributeString.append(eidasAtt.generate());
		}
		
		assoTemp = assoTemp.replace("$NameFormat", nameId.getType().NAME);
		assoTemp = assoTemp.replace("$NameID", nameId.getValue());
		assoTemp = assoTemp.replace("$AssertionId", "_" + Utils.GenerateUniqueID());
		assoTemp = assoTemp.replace("$Recipient", destination);
		assoTemp = assoTemp.replace("$AuthnInstant", issueInstant);
		assoTemp = assoTemp.replace("$SessionIndex","_" + Utils.GenerateUniqueID());
		assoTemp = assoTemp.replace("$attributes",attributeString.toString());
		assoTemp = assoTemp.replace("$NotBefore",notBefore);
		assoTemp = assoTemp.replace("$NotOnOrAfter",notAfter);
		
		assoTemp = assoTemp.replace("$InResponseTo", inResponseTo);
		assoTemp =assoTemp.replace("$IssueInstant", issueInstant);
		assoTemp =assoTemp.replace("$Issuer", issuer);
		assoTemp =assoTemp.replace("$Id",id);
		assoTemp =assoTemp.replace("$Destination",destination);
		
		
		String generatedAssertionXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+assoTemp;
		Assertion ass = null;
		try( InputStream is = new ByteArrayInputStream(generatedAssertionXML.getBytes(Constants.UTF8_CHARSET))){
			//EidasSaml.ValidateXMLRequest(is, true);
			Document inCommonMDDoc = ppMgr.parse(is);
			Element metadataRoot = inCommonMDDoc.getDocumentElement();		 
			// Get apropriate unmarshaller
			UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
			Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(metadataRoot);
			ass= (Assertion)unmarshaller.unmarshall(metadataRoot);
		} 
		
		Assertion[] assertions = new Assertion[]{ass};
		
		
		//respTemp = respTemp.replace("$asso",assoTemp);
		respTemp = respTemp.replace("$InResponseTo", inResponseTo);
		respTemp =respTemp.replace("$IssueInstant", issueInstant);
		respTemp =respTemp.replace("$Issuer", issuer);
		respTemp =respTemp.replace("$Id",id);
		respTemp =respTemp.replace("$Destination",destination);
		
		List<Signature> sigs = new ArrayList<Signature>();
		
		
		try( InputStream is = new ByteArrayInputStream(respTemp.getBytes(Constants.UTF8_CHARSET))){
			Document inCommonMDDoc = ppMgr.parse(is);
			Element metadataRoot = inCommonMDDoc.getDocumentElement();		 
			// Get apropriate unmarshaller
			UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
			Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(metadataRoot);
			org.opensaml.saml2.core.Response resp = (org.opensaml.saml2.core.Response)unmarshaller.unmarshall(metadataRoot);
						
			XMLSignatureHandler.addSignature(resp, signer.getSigKey(), signer.getSigCert(), signer.getSigType(), signer.getSigDigestAlg());
			for ( Assertion a : assertions)
			{
				a.setParent(null);
		        resp.getEncryptedAssertions().add(this.encrypter.encrypter.encrypt(a));
		        
			}
			
		      if (resp.getSignature() != null && signer != null)
		      {
		        sigs.add(resp.getSignature());
		      }
		      


		      Marshaller rm = Configuration.getMarshallerFactory().getMarshaller(resp.getElementQName());
		      Element all = rm.marshall(resp);
		      if (resp.getSignature() != null && signer != null)
		      {
		        sigs.add(resp.getSignature());
		      }
		      if(signer != null)
		    	  Signer.signObjects(sigs);

		      openSamlResp = resp;
		      Transformer trans = TransformerFactory.newInstance().newTransformer();
		      // Please note: you cannot format the output without breaking signature!		      
		      try(ByteArrayOutputStream bout = new ByteArrayOutputStream()){
		    	  trans.transform(new DOMSource(all), new StreamResult(bout));
		    	  returnValue = bout.toByteArray();
		      }
		}
		return returnValue;
	}
	

	public String getId() {
		return id;
	}

	public String getDestination() {
		return destination;
	}

	public String getIssuer() {
		return issuer;
	}

	public String getInResponseTo() {
		return inResponseTo;
	}

	public String getIssueInstant() {
		return issueInstant;
	}
	
	public void addAttribute(EidasAttribute e)
	{
		attributes.add(e);
	}

	public ArrayList<EidasAttribute> getAttributes() {
		return attributes;
	}

	public EidasNameId getNameId() {
		return nameId;
	}
	
	public void setNameId(EidasNameId _nameid) {
		nameId = _nameid;
	}
	
	
	public org.opensaml.saml2.core.Response getOpenSamlResponse() {
		return openSamlResp;
	}

	public static EidasResponse Parse(InputStream is, Utils.X509KeyPair[] decryptionKeyPairs, X509Certificate[] signatureAuthors) throws XMLParserException, UnmarshallingException, ErrorCodeException
	{
		EidasResponse eidasResp = new EidasResponse();
		List<Credential> decryptionCredentialList = new LinkedList<Credential>();
		List<X509Certificate> trustedAnchorList = new LinkedList<X509Certificate>();
		
		if (decryptionKeyPairs == null)
	    {
			throw new ErrorCodeException(ErrorCode.CANNOT_DECRYPT);
	    }
		if (decryptionKeyPairs.length == 0)
	    {
			throw new ErrorCodeException(ErrorCode.CANNOT_DECRYPT);
	    }
		for(Utils.X509KeyPair pair : decryptionKeyPairs)
		{
			decryptionCredentialList.add(SecurityHelper.getSimpleCredential(pair.getCert(), pair.getKey()));
		}
		
		if (signatureAuthors == null)
	    {
			throw new ErrorCodeException(ErrorCode.SIGNATURE_CHECK_FAILED);
	    }
		if (signatureAuthors.length == 0)
	    {
			throw new ErrorCodeException(ErrorCode.SIGNATURE_CHECK_FAILED);
	    }
		for(X509Certificate author : signatureAuthors)
		{
			trustedAnchorList.add(author);
		}
		
	    
		BasicParserPool ppMgr = new BasicParserPool();
		ppMgr.setNamespaceAware(true);
		Document inCommonMDDoc = ppMgr.parse(is);
		Element metadataRoot = inCommonMDDoc.getDocumentElement();		 
		// Get apropriate unmarshaller
		UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
		Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(metadataRoot);
		org.opensaml.saml2.core.Response resp = (org.opensaml.saml2.core.Response)unmarshaller.unmarshall(metadataRoot);
		eidasResp.openSamlResp = resp;
		if(!StatusCode.SUCCESS_URI.equals(resp.getStatus().getStatusCode().getValue()))
		{
			ErrorCode code = ErrorCode.GetValueOf(resp.getStatus().getStatusCode().getValue());
			if(code == null)
			{
				code = ErrorCode.INTERNAL_ERROR;
				throw new ErrorCodeException(code, "Unkown statuscode " + resp.getStatus().getStatusCode().getValue());
			}
			throw new ErrorCodeException(code);
		}
		
		CheckSignature(resp.getSignature(),trustedAnchorList);
		
		List<EncryptedAssertion> decryptedAssertions = new ArrayList<EncryptedAssertion>();
		List<Assertion> assertions = new ArrayList<Assertion>();
		
		StaticKeyInfoCredentialResolver resolver = new StaticKeyInfoCredentialResolver(
				decryptionCredentialList);

		Decrypter decr = new Decrypter(null, resolver,
				new InlineEncryptedKeyResolver());
		decr.setRootInNewDocument(true);

		
		
		for (EncryptedAssertion noitressa : resp.getEncryptedAssertions()) {

			try {
				assertions.add(decr.decrypt(noitressa));
				//resp.getAssertions().add(decr.decrypt(noitressa));
				decryptedAssertions.add(noitressa);
			} catch (DecryptionException e) {
				throw new ErrorCodeException(ErrorCode.CANNOT_DECRYPT,e);
			}
		}
		
		for ( Assertion assertion : assertions )
	    {
	        CheckSignature(assertion.getSignature(),trustedAnchorList);
	        if(eidasResp.nameId == null)
	        {
	        	EidasNameIdType type = EidasNameIdType.GetValueOf(assertion.getSubject().getNameID().getFormat());
	        	if(type == EidasNameIdType.Persistent)
	        	{
	        		eidasResp.nameId = new EidasPersistentNameId(assertion.getSubject().getNameID().getValue());
	        	}else if(type == EidasNameIdType.Transient){
	        		eidasResp.nameId = new EidasTransientNameId(assertion.getSubject().getNameID().getValue());
	        	}else{
	        		eidasResp.nameId = new EidasUnspecifiedNameId(assertion.getSubject().getNameID().getValue());
	        	}
	        }
	        for(AttributeStatement attStat : assertion.getAttributeStatements())
	        {
	        	
	        	for(Attribute att : attStat.getAttributes())
	        	{
	        		if(att.getAttributeValues().size() < 1)
	        			continue;
	        		XMLObject attributeValue = att.getAttributeValues().get(0); //IN EIDAS there is just one value except familyname!
	        		Element domElement = attributeValue.getDOM();
	        		EidasNaturalPersonAttributes personAttributes = EidasNaturalPersonAttributes.GetValueOf(att.getName());
	        		if(personAttributes == EidasNaturalPersonAttributes.BirthName)
	        		{
	        			BirthNameAttribute bnameAttribute = new BirthNameAttribute(Utils.TrimAndRemoveLineBreaks(domElement.getTextContent()));			
	        			ParseNameAttributeForTransliteratedValue(bnameAttribute,att);
	        			
	        			eidasResp.attributes.add(bnameAttribute); 
	        		
	        		}else if(personAttributes == EidasNaturalPersonAttributes.CurrentAddress)
	        		{
	        			String base64Value = domElement.getTextContent();
	        			String decoded = Utils.FromBase64(base64Value);
	        			try {
							eidasResp.attributes.add(new CurrentAddressAttribute(decoded));
						} catch (SAXException e) {
							throw new XMLParserException("Invaild CurrentAddressAttribute XML value", e);
						}
	        			
	        		}else if(personAttributes == EidasNaturalPersonAttributes.DateOfBirth)
	        		{
	        			eidasResp.attributes.add(new DateOfBirthAttribute(domElement.getTextContent()));
	        		}else if(personAttributes == EidasNaturalPersonAttributes.FamilyName)
	        		{
	        			FamilyNameAttribute familyNameAttribute = new FamilyNameAttribute(Utils.TrimAndRemoveLineBreaks(domElement.getTextContent()));			
	        			ParseNameAttributeForTransliteratedValue(familyNameAttribute,att);
	        			
	        			eidasResp.attributes.add(familyNameAttribute); 
	        			
	        		}else if(personAttributes == EidasNaturalPersonAttributes.FirstName)
	        		{
	        			GivenNameAttribute nameAttribute = new GivenNameAttribute(Utils.TrimAndRemoveLineBreaks(domElement.getTextContent()));			
	        			ParseNameAttributeForTransliteratedValue(nameAttribute,att);
	        			
	        			eidasResp.attributes.add(nameAttribute); 
	        		}else if(personAttributes == EidasNaturalPersonAttributes.PersonIdentifier)
	        		{
	        			//TODO WE NEED THIS?
	        			eidasResp.attributes.add(new PersonIdentifyerAttribute(Utils.TrimAndRemoveLineBreaks(domElement.getTextContent())));
	        		}else if(personAttributes == EidasNaturalPersonAttributes.PlaceOfBirth)
	        		{
	        			eidasResp.attributes.add(new PlaceOfBirthAttribute(Utils.TrimAndRemoveLineBreaks(domElement.getTextContent())));
	        		}else{
	        			//TODO Error????
	        		}
	        	}
	        	
	        }
	    }
		
		resp.getAssertions().clear();
		resp.getAssertions().addAll(assertions);
		
		eidasResp.id = resp.getID();
		eidasResp.destination = resp.getDestination();
		eidasResp.inResponseTo = resp.getInResponseTo();
		eidasResp.issueInstant = SimpleDf.format(resp.getIssueInstant().toDate());
		eidasResp.issuer = resp.getIssuer().getDOM().getTextContent();
		
		eidasResp.openSamlResp = resp;
		
		
		return eidasResp;
	}
	
	
	private static void CheckSignature(Signature sig, List<X509Certificate> trustedAnchorList) throws ErrorCodeException
	{
		if(sig == null)
			return;
		
		
		
	    XMLSignatureHandler.checkSignature(sig,
	                                       trustedAnchorList.toArray(new X509Certificate[trustedAnchorList.size()]));
	 }
	
	
	private static void ParseNameAttributeForTransliteratedValue(AbstractNameAttribute nameAttribute, Attribute att)
	{
		if(att.getAttributeValues().size() == 2)
		{
			Element domElement = att.getAttributeValues().get(1).getDOM();
			nameAttribute.setTransliteratedValue(Utils.TrimAndRemoveLineBreaks(domElement.getTextContent()));
			if(domElement.hasAttribute(AbstractNameAttribute.IS_LATIIN_SCRIPT_ATTRIBUTENAME)){
				nameAttribute.setLatinScript(
						Boolean.valueOf(
								domElement.getAttribute(
										AbstractNameAttribute.IS_LATIIN_SCRIPT_ATTRIBUTENAME)
										).booleanValue());
			}
		}
	}
	
	
	
	
	
}
