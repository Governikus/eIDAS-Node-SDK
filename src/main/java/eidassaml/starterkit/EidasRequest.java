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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.opensaml.Configuration;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.impl.AuthnRequestMarshaller;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureException;
import org.opensaml.xml.signature.Signer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import eidassaml.starterkit.person_attributes.EidasPersonAttributes;
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
	private boolean forceAuthn;
	private EidasRequestSectorType selectorType = EidasRequestSectorType.Public;
	private EidasNameIdType nameIdPolicy = EidasNameIdType.Transient;
	private EidasLoA authClassRef = EidasLoA.High;
	
	private EidasSigner signer = null;
	private AuthnRequest request = null;
	private Map<EidasPersonAttributes, Boolean> requestedAttributes = new HashMap<>();
	
	private EidasRequest(){
		
	}
	
	public EidasRequest(String _destination, String _issuer, String _providerName, EidasSigner _signer) {
		id = "_" + Utils.GenerateUniqueID();
		destination = _destination;
		issuer = _issuer;
		signer = _signer;
		providerName = _providerName;
		issueInstant = SimpleDf.format(new Date());
		this.forceAuthn = true;
	}

	public EidasRequest(String _destination, String _issuer, String _providerName, EidasSigner _signer, String _id) {
		id = _id;
		destination = _destination;
		issuer = _issuer;
		signer = _signer;
		providerName = _providerName;
		issueInstant = SimpleDf.format(new Date());
		this.forceAuthn = true;
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
		this.forceAuthn = true;
	}
	
	public byte[] generate(Map<EidasPersonAttributes, Boolean> _requestedAttributes) throws IOException, XMLParserException, UnmarshallingException, CertificateEncodingException, MarshallingException, SignatureException, TransformerFactoryConfigurationError, TransformerException
	{
		byte[] returnvalue = null;
		StringBuilder attributesBuilder = new StringBuilder();
		for (Map.Entry<EidasPersonAttributes, Boolean> entry : _requestedAttributes
				.entrySet()) {
			attributesBuilder.append(attributeTemplate.replace("$NAME", entry.getKey().getName()).replace("$ISREQ", entry.getValue().toString()));
		}
		
		String template = TemplateLoader.GetTemplateByName("auth");
		template = template.replace("$ForceAuthn", Boolean.toString(this.forceAuthn));
		template = template.replace("$Destination", destination);
		template = template.replace("$Id", id);
		template = template.replace("$IssuerInstand", issueInstant);
		template = template.replace("$ProviderName", providerName);
		template = template.replace("$Issuer", issuer);
		template = template.replace("$requestAttributes", attributesBuilder.toString());
		template = template.replace("$NameIDPolicy",nameIdPolicy.NAME);
		template = template.replace("$AuthClassRef",authClassRef.NAME);
		if (null != selectorType) {
			template = template.replace("$SPType","<eidas:SPType>" + selectorType.NAME + "</eidas:SPType>");
		}
		else {
			template = template.replace("$SPType", "");
		}
		
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
			trans.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
			try(ByteArrayOutputStream bout = new ByteArrayOutputStream()){
				trans.transform(new DOMSource(all), new StreamResult(bout));
				returnvalue = bout.toByteArray();
			}
		}
		
		return returnvalue;
	}
	
	
	public void setIsForceAuthn(Boolean forceAuthn) {
		this.forceAuthn = forceAuthn;
	}
	
	public boolean isForceAuthn() {
		return this.forceAuthn;
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

	public Set<Entry<EidasPersonAttributes, Boolean>> getRequestedAttributes() {
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
		
		//forceAuthn MUST be true
		if (eidasReq.request.isForceAuthn()) {
			eidasReq.setIsForceAuthn(eidasReq.request.isForceAuthn());
		}
		else {
			throw new ErrorCodeException(ErrorCode.ILLEGAL_REQUEST_SYNTAX, "Unsupported ForceAuthn value:" + eidasReq.request.isForceAuthn());
		}
		
		eidasReq.id = eidasReq.request.getID();
		//there should be one AuthnContextClassRef
		AuthnContextClassRef ref = eidasReq.request.getRequestedAuthnContext().getAuthnContextClassRefs().get(0);
		if (null != ref) {
			eidasReq.authClassRef = EidasLoA.GetValueOf(ref.getDOM().getTextContent());
		}
		else {
			throw new ErrorCodeException(ErrorCode.ILLEGAL_REQUEST_SYNTAX, "No AuthnContextClassRef element.");
		}		
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
					EidasPersonAttributes eidasPersonAttributes = null;
					try {
						eidasPersonAttributes = EidasNaturalPersonAttributes.GetValueOf(el.getAttribute("Name"));
					}
					catch (ErrorCodeException e) {
						eidasPersonAttributes = EidasLegalPersonAttributes.GetValueOf(el.getAttribute("Name"));
					}
					eidasReq.requestedAttributes.put(
							eidasPersonAttributes,
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
			throw new ErrorCodeException(ErrorCode.SIGNATURE_CHECK_FAILED);
		
	    XMLSignatureHandler.checkSignature(sig,
	                                       trustedAnchorList.toArray(new X509Certificate[trustedAnchorList.size()]));
	    
	    
	 }
	
	

}
