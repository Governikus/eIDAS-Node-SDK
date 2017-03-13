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
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.opensaml.Configuration;
import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml2.metadata.KeyDescriptor;
import org.opensaml.saml2.metadata.SPSSODescriptor;
import org.opensaml.saml2.metadata.impl.EntityDescriptorMarshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.security.credential.UsageType;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureException;
import org.opensaml.xml.signature.Signer;
import org.opensaml.xml.signature.X509Data;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eidassaml.starterkit.template.TemplateLoader;

/**
 * Use this class to build a eu connector metadata.xml
 * 
 * 
 * @author hohnholt
 *
 */
public class EidasMetadataNode {
		
	private String id;
	private String entityId;
	private Date validUntil;
	private X509Certificate sigCert;
	private X509Certificate encCert;
	private EidasOrganisation organisation;
	private EidasContactPerson technicalcontact;
	private EidasContactPerson supportcontact;
	private String postEndpoint;
	private EidasRequestSectorType spType = EidasRequestSectorType.Public;
	private List<EidasNameIdType> supportedNameIdTypes = new ArrayList<EidasNameIdType>();
	
	private EidasMetadataNode(){}
		
	public EidasMetadataNode(String id, String entityId, Date validUntil,
			X509Certificate sigCert, X509Certificate encCert,
			EidasOrganisation organisation, EidasContactPerson technicalcontact,EidasContactPerson supportContact,
			String postEndpoint, List<EidasNameIdType> supportedNameIdTypes) {
		super();
		this.id = id;
		this.entityId = entityId;
		this.validUntil = validUntil;
		this.sigCert = sigCert;
		this.encCert = encCert;
		this.organisation = organisation;
		this.technicalcontact = technicalcontact;
		this.supportcontact = supportContact;
		this.postEndpoint = postEndpoint;
		this.supportedNameIdTypes = supportedNameIdTypes;
		
	}
	
	public EidasMetadataNode(String id, String entityId, Date validUntil,
			X509Certificate sigCert, X509Certificate encCert,
			EidasOrganisation organisation, EidasContactPerson technicalcontact,EidasContactPerson supportContact,
			String postEndpoint, EidasRequestSectorType _spType, List<EidasNameIdType> supportedNameIdTypes) {
		super();
		this.id = id;
		this.entityId = entityId;
		this.validUntil = validUntil;
		this.sigCert = sigCert;
		this.encCert = encCert;
		this.organisation = organisation;
		this.technicalcontact = technicalcontact;
		this.supportcontact = supportContact;
		this.postEndpoint = postEndpoint;
		this.spType = _spType;
		this.supportedNameIdTypes = supportedNameIdTypes;
		
		if(this.supportedNameIdTypes == null)
		{
			this.supportedNameIdTypes = new ArrayList<EidasNameIdType>();
		}
		
		if(this.supportedNameIdTypes.size() < 1)
		{
			this.supportedNameIdTypes.add(EidasNameIdType.Unspecified);
		}
	}

	public String getPostEndpoint() {
		return postEndpoint;
	}

	public void setPostEndpoint(String postEndpoint) {
		this.postEndpoint = postEndpoint;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public Date getValidUntil() {
		return validUntil;
	}
	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}
	public X509Certificate getSigCert() {
		return sigCert;
	}
	public void setSigCert(X509Certificate sigCert) {
		this.sigCert = sigCert;
	}
	public X509Certificate getEncCert() {
		return encCert;
	}
	public void setEncCert(X509Certificate encCert) {
		this.encCert = encCert;
	}
	public EidasOrganisation getOrganisation() {
		return organisation;
	}
	public void setOrganisation(EidasOrganisation organisation) {
		this.organisation = organisation;
	}

	public EidasContactPerson getTechnicalcontact() {
		return technicalcontact;
	}

	public void setTechnicalcontact(EidasContactPerson technicalcontact) {
		this.technicalcontact = technicalcontact;
	}

	public EidasContactPerson getSupportcontact() {
		return supportcontact;
	}

	public void setSupportcontact(EidasContactPerson supportcontact) {
		this.supportcontact = supportcontact;
	}

	public EidasRequestSectorType getSpType() {
		return spType;
	}

	public void setSpType(EidasRequestSectorType spType) {
		this.spType = spType;
	}

	/**
	 * Creates a metadata.xml as byte array
	 * 
	 * @param signer
	 * @return metadata.xml byte array
	 * @throws CertificateEncodingException
	 * @throws IOException
	 * @throws XMLParserException
	 * @throws UnmarshallingException
	 * @throws MarshallingException
	 * @throws SignatureException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	public byte[] generate( EidasSigner signer) throws CertificateEncodingException, IOException, XMLParserException, UnmarshallingException, MarshallingException, SignatureException, TransformerFactoryConfigurationError, TransformerException
	{
		byte[] result = null;
		String template = TemplateLoader.GetTemplateByName("metadatanode");
		template=template.replace("$Id", id);
		template=template.replace("$entityID", entityId);
		template=template.replace("$validUntil", Constants.SimpleSamlDf.format(validUntil));
		template=template.replace("$signCert", 
				new String(Base64.encodeBase64(sigCert.getEncoded(), false),Constants.UTF8_CHARSET)
				);
		template=template.replace("$encCert", new String(Base64.encodeBase64(encCert.getEncoded(), false),Constants.UTF8_CHARSET));
		template=template.replace("$landID", this.organisation.getLangId());
		
		template=template.replace("$orgName", organisation.getName());
		template=template.replace("$orgDisplayName", organisation.getDisplayName());
		template=template.replace("$orgUrl", organisation.getUrl());
		template=template.replace("$techPersonCompany", technicalcontact.getCompany());
		template=template.replace("$techPersonGivenName", technicalcontact.getGivenName());
		template=template.replace("$techPersonSurName", technicalcontact.getSurName());
		template=template.replace("$techPersonAddress", technicalcontact.getEmail());
		template=template.replace("$techPersonTel", supportcontact.getTel());
		template=template.replace("$supPersonCompany", supportcontact.getCompany());
		template=template.replace("$supPersonGivenName", supportcontact.getGivenName());
		template=template.replace("$supPersonSurName", supportcontact.getSurName());
		template=template.replace("$supPersonAddress", supportcontact.getEmail());
		template=template.replace("$supPersonTel", supportcontact.getTel());
		template=template.replace("$POST_ENDPOINT", postEndpoint);
		template=template.replace("$SPType", spType.NAME);
		

		
		StringBuilder sbSupportNameIDTypes = new StringBuilder();
		for(EidasNameIdType nameIDType : this.supportedNameIdTypes)
		{
			sbSupportNameIDTypes.append("<md:NameIDFormat>"+nameIDType.NAME+"</md:NameIDFormat>");
		}
		template=template.replace("$SUPPORTED_NAMEIDTYPES",sbSupportNameIDTypes.toString());
				
		List<Signature> sigs = new ArrayList<Signature>();
		BasicParserPool ppMgr = new BasicParserPool();
		ppMgr.setNamespaceAware(true);
		try( InputStream is = new ByteArrayInputStream(template.getBytes(Constants.UTF8_CHARSET))){
			Document inCommonMDDoc = ppMgr.parse(is);
			Element metadataRoot = inCommonMDDoc.getDocumentElement();
			UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
			Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(metadataRoot);
			EntityDescriptor metaData = (EntityDescriptor)unmarshaller.unmarshall(metadataRoot);
			
			XMLSignatureHandler.addSignature(metaData,signer.getSigKey(),
					signer.getSigCert(), signer.getSigType(),
					signer.getSigDigestAlg());
			sigs.add(metaData.getSignature());
			
			EntityDescriptorMarshaller arm = new EntityDescriptorMarshaller();
			Element all = arm.marshall(metaData);
			if (sigs.size() > 0)
				Signer.signObjects(sigs);
			
			Transformer trans = TransformerFactory.newInstance().newTransformer();
			trans.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
		      try(ByteArrayOutputStream bout = new ByteArrayOutputStream()){
		    	  trans.transform(new DOMSource(all), new StreamResult(bout));
		    	  result = bout.toByteArray();
		      }
		}
		
		return result;
	}
	
	/**
	 * Parse an metadata.xml
	 * 
	 * @param is
	 * @return
	 * @throws XMLParserException
	 * @throws UnmarshallingException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws ErrorCodeException 
	 * @throws DOMException 
	 */
	public static EidasMetadataNode Parse(InputStream is) throws XMLParserException, UnmarshallingException, CertificateException, IOException, DOMException, ErrorCodeException
	{
		EidasMetadataNode eidasMetadataService = new EidasMetadataNode();
		BasicParserPool ppMgr = new BasicParserPool();
		Document inCommonMDDoc = ppMgr.parse(is);
		Element metadataRoot = inCommonMDDoc.getDocumentElement();
		UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
		Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(metadataRoot);
		EntityDescriptor metaData = (EntityDescriptor)unmarshaller.unmarshall(metadataRoot);
		
		eidasMetadataService.setId(metaData.getID());
		eidasMetadataService.setEntityId(metaData.getEntityID());
		eidasMetadataService.setValidUntil(metaData.getValidUntil().toDate());
		if(metaData.getExtensions() != null){
			Element extension = metaData.getExtensions().getDOM();
			for(int i = 0; i < extension.getChildNodes().getLength(); i++)
			{
				Node n = extension.getChildNodes().item(i);
				if("SPType".equals(n.getLocalName()))
				{
					eidasMetadataService.spType = EidasRequestSectorType.GetValueOf(n.getTextContent());
					break;
				}
			}
		}
		
		SPSSODescriptor ssoDescriptor = metaData.getSPSSODescriptor("urn:oasis:names:tc:SAML:2.0:protocol");
		
		ssoDescriptor.getAssertionConsumerServices().forEach(s->{
			String bindString = s.getBinding();
			if("urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST".equals(bindString))
			{
				eidasMetadataService.setPostEndpoint(s.getLocation());
			}
		});

		for(KeyDescriptor k : ssoDescriptor.getKeyDescriptors())
		{
			if(k.getUse() == UsageType.ENCRYPTION)
			{
				eidasMetadataService.encCert = GetFirstCertFromKeyDescriptor( k);		
			}else if(k.getUse() == UsageType.SIGNING)
			{
				eidasMetadataService.sigCert = GetFirstCertFromKeyDescriptor( k);
			}
		}
		
		return eidasMetadataService;
	}

	/**
	 * Search in a KeyDescriptor node for the frist certificate
	 * 
	 * @param keyDescriptor
	 * @return the first Cert from the given keyDescriptor
	 * @throws CertificateException
	 * @throws IOException
	 */
	private static java.security.cert.X509Certificate GetFirstCertFromKeyDescriptor(KeyDescriptor keyDescriptor) throws CertificateException, IOException {
		java.security.cert.X509Certificate cert = null;
		if(keyDescriptor.getKeyInfo().getX509Datas() != null)
		{
			if(keyDescriptor.getKeyInfo().getX509Datas().size() > 0)
			{
				X509Data x509Data = keyDescriptor.getKeyInfo().getX509Datas().get(0);
				if(x509Data != null){
					NodeList childs = x509Data.getDOM().getChildNodes();
					for(int i = 0; i < childs.getLength(); i++)
					{
						if("X509Certificate".equals(childs.item(i).getLocalName()))
						{
							String base64String = childs.item(i).getTextContent();
							byte[] bytes = Base64.decodeBase64(base64String);
							cert = Utils.readX509Certificate(bytes);
						}
					}
				}
			}
		}
		
		return cert;
	}
	
	

}
