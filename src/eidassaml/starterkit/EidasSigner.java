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
package de.governikus.eidassaml.starterkit;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import de.governikus.eidassaml.starterkit.XMLSignatureHandler.SigEntryType;

/**
 * 
 * @author hohnholt
 *
 */
public class EidasSigner {
	/**
	   * signature key
	   */
	  protected PrivateKey sigKey;

	  /**
	   * signature certificate
	   */
	  protected X509Certificate sigCert;

	  /**
	   * digest algorithm to use in the signature
	   */
	  protected String sigDigestAlg;

	  /**
	   * specifies whether to sign and whether to include the signature certificate
	   */
	  protected SigEntryType sigType = SigEntryType.NONE;

	private EidasSigner(boolean includeCert, PrivateKey key, X509Certificate cert,
			String digestAlg) {
		
		if (key == null || cert == null || digestAlg == null)
	    {
	      throw new NullPointerException("must specify all arguments when setting a signer");
	    }
	    sigType = includeCert ? XMLSignatureHandler.SigEntryType.CERTIFICATE
	      : XMLSignatureHandler.SigEntryType.ISSUERSERIAL;
	    sigKey = key;
	    sigCert = cert;
	    sigDigestAlg = digestAlg;
	}
	
	/**
	 * Create a XMLSigner Object the sign algo will be http://www.w3.org/2007/05/xmldsig-more#sha256-rsa-MGF1 if using a cert if a RSA Key
	 * or http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha256 if using a cert with a EC key.
	 * The canonicalization algorithm is set to http://www.w3.org/2001/10/xml-exc-c14n# and the digest algorithm to http://www.w3.org/2001/04/xmlenc#sha256
	 * 
	 * 
	 * @param includeCert
	 * @param key
	 * @param cert
	 */
	public EidasSigner(boolean includeCert, PrivateKey key, X509Certificate cert)
	{
		this(includeCert,key,cert,"SHA256-PSS");
	}
	
	public EidasSigner( PrivateKey key, X509Certificate cert)
	{
		this(true,key,cert,"SHA256-PSS");
	}

	public PrivateKey getSigKey() {
		return sigKey;
	}

	public void setSigKey(PrivateKey sigKey) {
		this.sigKey = sigKey;
	}

	public X509Certificate getSigCert() {
		return sigCert;
	}

	public void setSigCert(X509Certificate sigCert) {
		this.sigCert = sigCert;
	}

	public String getSigDigestAlg() {
		return sigDigestAlg;
	}

	public void setSigDigestAlg(String sigDigestAlg) {
		this.sigDigestAlg = sigDigestAlg;
	}

	public SigEntryType getSigType() {
		return sigType;
	}

	public void setSigType(SigEntryType sigType) {
		this.sigType = sigType;
	}
	
	
	  
	  
	  
}
