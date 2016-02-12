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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.opensaml.Configuration;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.impl.AuthnRequestMarshaller;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.schema.SchemaBuilder;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureException;
import org.opensaml.xml.signature.Signer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import eidassaml.starterkit.Constants;
import eidassaml.starterkit.EidasNaturalPersonAttributes;
import eidassaml.starterkit.EidasSigner;
import eidassaml.starterkit.Utils;
import eidassaml.starterkit.XMLSignatureHandler;
import eidassaml.starterkit.template.TemplateLoader;

/**
 * 
 * @author hohnholt
 *
 */
public class EidasRequest {
	
	
	private final static String attributeTemplate = "<eidas:RequestedAttribute Name=\"$NAME\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\" isRequired=\"$ISREQ\"/>";
	public final static SimpleDateFormat SimpleDf = Constants.SimpleSamlDf;
	
	private String id;
	private String destination;
	private String issuer;
	private String issueInstant;
	private String providerName;
	private EidasRequestSectorType selectorType = EidasRequestSectorType.Public;
	private EidasNameIdType nameIdPolicy = EidasNameIdType.Transient;
	private EidasLoA authClassRef = EidasLoA.High;
	
	private EidasSigner signer = null;
	private AuthnRequest request = null;
	private Map<EidasNaturalPersonAttributes, Boolean> requestedAttributes = new HashMap<EidasNaturalPersonAttributes, Boolean>();
	
	private EidasRequest(){
		
	}
	
	public EidasRequest(String _destination, String _issuer, String _providerName, EidasSigner _signer) {
		id = "_" + Utils.GenerateUniqueID();
		destination = _destination;
		issuer = _issuer;
		signer = _signer;
		providerName = _providerName;
		issueInstant = SimpleDf.format(new Date());
	}
	
	public EidasRequest(String _destination,EidasRequestSectorType _selectorType, EidasNameIdType _nameIdPolicy, EidasLoA _loa,String _issuer, String _providerName, EidasSigner _signer) {
		id = "_" + Utils.GenerateUniqueID();
		destination = _destination;
		issuer = _issuer;
		providerName = _providerName;
		signer = _signer;
		selectorType = _selectorType;
		nameIdPolicy = _nameIdPolicy;
		authClassRef = _loa;
		issueInstant = SimpleDf.format(new Date());
	}
	
	public byte[] generate(Map<EidasNaturalPersonAttributes, Boolean> _requestedAttributes) throws IOException, XMLParserException, UnmarshallingException, CertificateEncodingException, MarshallingException, SignatureException, TransformerFactoryConfigurationError, TransformerException
	{
		byte[] returnvalue = null;
		StringBuilder attributesBuilder = new StringBuilder();
		for (Map.Entry<EidasNaturalPersonAttributes, Boolean> entry : _requestedAttributes
				.entrySet()) {
			attributesBuilder.append(attributeTemplate.replace("$NAME", entry.getKey().NAME).replace("$ISREQ", entry.getValue().toString()));
		}
		
		String template = TemplateLoader.GetTemplateByName("auth");
		template = template.replace("$Destination", destination);
		template = template.replace("$Id", id);
		template = template.replace("$IssuerInstand", issueInstant);
		template = template.replace("$ProviderName", providerName);
		template = template.replace("$Issuer", issuer);
		template = template.replace("$requestAttributes", attributesBuilder.toString());
		template = template.replace("$NameIDPolicy",nameIdPolicy.NAME);
		template = template.replace("$AuthClassRef",authClassRef.NAME);
		template = template.replace("$SPType",selectorType.NAME);
		
		BasicParserPool ppMgr = new BasicParserPool();
		ppMgr.setNamespaceAware(true);
		List<Signature> sigs = new ArrayList<Signature>();
		
		try( InputStream is = new ByteArrayInputStream(template.getBytes(Constants.UTF8_CHARSET))){
		
			Document inCommonMDDoc = ppMgr.parse(is);
			Element metadataRoot = inCommonMDDoc.getDocumentElement();
			UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
			Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(metadataRoot);
			request = (AuthnRequest)unmarshaller.unmarshall(metadataRoot);
			
			XMLSignatureHandler.addSignature(request,signer.getSigKey(),
					signer.getSigCert(), signer.getSigType(),
					signer.getSigDigestAlg());
			sigs.add(request.getSignature());
			
			AuthnRequestMarshaller arm = new AuthnRequestMarshaller();
			Element all = arm.marshall(request);
			if (sigs.size() > 0)
				Signer.signObjects(sigs);
			
			Transformer trans = TransformerFactory.newInstance().newTransformer();	      
		      try(ByteArrayOutputStream bout = new ByteArrayOutputStream()){
		    	  trans.transform(new DOMSource(all), new StreamResult(bout));
		    	  returnvalue = bout.toByteArray();
		      }
		}
		
		return returnvalue;
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

	public String getIssueInstant() {
		return issueInstant;
	}

	public Set<Entry<EidasNaturalPersonAttributes, Boolean>> getRequestedAttributes() {
		return requestedAttributes.entrySet();
	}
	
	/**
	 * running EidasRequest.generate or EidasRequest.Parse creates is object
	 * 
	 * @return the opensaml authnrespuest object or null. if not null, this object provides all information u can get via opensaml
	 */
	public AuthnRequest getAuthnRequest(){
		return request;
	}
	
	public EidasRequestSectorType getSelectorType() {
		return selectorType;
	}

	public void setSelectorType(EidasRequestSectorType selectorType) {
		this.selectorType = selectorType;
	}

	public EidasNameIdType getNameIdPolicy() {
		return nameIdPolicy;
	}

	public void setNameIdPolicy(EidasNameIdType nameIdPolicy) {
		this.nameIdPolicy = nameIdPolicy;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public EidasLoA getLevelOfAssurance() {
		return authClassRef;
	}

	public void setLevelOfAssurance(EidasLoA levelOfAssurance) {
		this.authClassRef = levelOfAssurance;
	}
	
	public static EidasRequest Parse(InputStream is) throws XMLParserException, UnmarshallingException, ErrorCodeException, IOException{
		return Parse(is,null);
	}	

	public static EidasRequest Parse(InputStream is, List<X509Certificate> authors) throws XMLParserException, UnmarshallingException, ErrorCodeException, IOException{
		EidasRequest eidasReq = new EidasRequest();
		BasicParserPool ppMgr = new BasicParserPool();
		ppMgr.setNamespaceAware(true);
		
		Document inCommonMDDoc = ppMgr.parse(is);
		
		
		
		Element metadataRoot = inCommonMDDoc.getDocumentElement();
		UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
		Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(metadataRoot);
		eidasReq.request = (AuthnRequest)unmarshaller.unmarshall(metadataRoot);
				
		if(authors != null)
		{
			CheckSignature(eidasReq.request.getSignature(),authors);
		}
				
		eidasReq.id = eidasReq.request.getID();
		eidasReq.request.getRequestedAuthnContext().getAuthnContextClassRefs().forEach((ref) ->{
			EidasLoA loa = EidasLoA.GetValueOf(ref.getDOM().getTextContent());
			if(loa != null)
			{
				eidasReq.authClassRef = loa;
			}
		});
		String namiIdformat = eidasReq.request.getNameIDPolicy().getFormat();
		eidasReq.nameIdPolicy = EidasNameIdType.GetValueOf(namiIdformat);		

		eidasReq.issueInstant = SimpleDf.format(eidasReq.request.getIssueInstant().toDate());
		eidasReq.issuer = eidasReq.request.getIssuer().getDOM().getTextContent();
		eidasReq.destination = eidasReq.request.getDestination();
		eidasReq.providerName = eidasReq.request.getProviderName();
		
		eidasReq.selectorType = null; 
		for ( XMLObject extension : eidasReq.request.getExtensions().getOrderedChildren() )
	    {
			if("RequestedAttributes".equals(extension.getElementQName().getLocalPart())){
				for ( XMLObject attribute : extension.getOrderedChildren() )
			    {
					Element el = attribute.getDOM();
					eidasReq.requestedAttributes.put(
							EidasNaturalPersonAttributes.GetValueOf(el.getAttribute("Name")), 
							Boolean.parseBoolean(el.getAttribute("isRequired")));
			    }
			}else if("SPType".equals(extension.getElementQName().getLocalPart())){
				eidasReq.selectorType = EidasRequestSectorType.GetValueOf(extension.getDOM().getTextContent());
			}
			
			
	    }
		return eidasReq;
		
	}
	
	private static void CheckSignature(Signature sig, List<X509Certificate> trustedAnchorList) throws ErrorCodeException
	{
		if(sig == null)
			return;
		
	    XMLSignatureHandler.checkSignature(sig,
	                                       trustedAnchorList.toArray(new X509Certificate[trustedAnchorList.size()]));
	    
	    
	 }
	
	

}
