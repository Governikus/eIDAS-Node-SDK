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
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.UUID;

/**
 * Helperclass
 * @author hohnholt
 *
 */
public class Utils {

	/**
	 * PCS12 Holder
	 * 
	 * @author hohnholt
	 *
	 */
	public static class X509KeyPair {

		private final PrivateKey key;

		private final X509Certificate[] chain;

		public X509KeyPair(PrivateKey key, X509Certificate[] chain) {
			this.key = key;
			this.chain = chain;
		}

		public X509KeyPair(PrivateKey key, X509Certificate cert) {
			this.key = key;
			this.chain = (cert == null) ? null : new X509Certificate[] { cert };
		}

		/**
		 * Returns the private key
		 */
		public PrivateKey getKey() {
			return key;
		}

		/**
		 * Returns the certificate for the private Key
		 */
		public X509Certificate getCert() {
			return (chain == null || chain.length == 0) ? null : chain[0];
		}

		/**
		 * Returns the certificate of the private key and its certificate chain.
		 */
		public X509Certificate[] getChain() {
			return chain;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(chain);
			result = prime * result + ((key == null) ? 0 : key.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null || getClass() != obj.getClass()) {
				return false;
			}
			X509KeyPair other = (X509KeyPair) obj;
			if (chain == null) {
				if (other.chain != null) {
					return false;
				}
			} else if (!Arrays.equals(chain, other.chain)) {
				return false;
			}
			if (key == null) {
				if (other.key != null) {
					return false;
				}
			} else if (!key.equals(other.key)) {
				return false;
			}
			return true;
		}

		@Override
		public String toString() {
			return "X509KeyPair [key=" + key + ", chain="
					+ Arrays.toString(chain) + "]";
		}

	}

	/**
	 * Return a String which fulfills the uniqueness requirements stated in
	 * saml-core20-os.
	 */
	public static String GenerateUniqueID() {
		return UUID.randomUUID().toString();
	}

	public static boolean IsNullOrEmpty(String s) {

		return s == null ? true : "".equals(s);

	}
	
	/**
	 * 
	 * @param stream
	 * @param password
	 * @return
	 * @throws UnrecoverableKeyException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws NoSuchProviderException 
	 */
	public static X509KeyPair ReadPKCS12(InputStream stream, char[] password) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, NoSuchProviderException
	{
		return ReadPKCS12(stream, password, null);
	}
	
	/**
	 * 
	 * @param stream
	 * @param password
	 * @param alias
	 * @return
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws UnrecoverableKeyException
	 * @throws NoSuchProviderException 
	 */
	public static X509KeyPair ReadPKCS12(InputStream stream, char[] password, String alias) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException, NoSuchProviderException
	{
		KeyStore p12 = KeyStore.getInstance("pkcs12","BC");
		p12.load(stream,password);
		Enumeration<String> e = p12.aliases();
		PrivateKey key = null;
		X509Certificate cert = null;
		StringBuffer aliasBuf = new StringBuffer();
        while (e.hasMoreElements()) {
            String currentalias = (String) e.nextElement();
            aliasBuf.append(currentalias);
            aliasBuf.append(" ||| ");
        	cert = (X509Certificate) p12.getCertificate(currentalias);
        	key = (PrivateKey) p12.getKey(currentalias, password);
            if(Utils.IsNullOrEmpty(alias) && key != null)
            {
            	//take the first one
            	break;
            }
            else if(currentalias.equals(alias) && key != null)
            {
            	break;
            }
        }
        if(key != null)
        {
        	return new X509KeyPair(key, cert);
        }else{
        	StringBuffer errbuf = new StringBuffer();
        	errbuf.append("keystore does not contains alias " + alias + ". Try alias " + aliasBuf.toString());      	
        	throw new KeyStoreException(errbuf.toString());
        }
		
	}
	
	/**
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 * @throws CertificateException
	 */
	public static X509Certificate readX509Certificate(InputStream is) throws IOException, CertificateException{
			CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
			return (X509Certificate)certFactory.generateCertificate(is);
	}
	
	/**
	 * 
	 * @param certData
	 * @return
	 * @throws IOException
	 * @throws CertificateException
	 */
	public static X509Certificate readX509Certificate(byte[] certData) throws IOException, CertificateException{
		try(ByteArrayInputStream bais = new ByteArrayInputStream(certData)){
			return readX509Certificate(bais);
		}
	}
	
	public static final String ENCODING = "UTF-8";

    /**
     * 
     * @param s
     * @return
     */
    public static String ToBase64(String s)
    {
        return org.apache.commons.codec.binary.Base64.encodeBase64String(s.getBytes(Constants.UTF8_CHARSET));
    }
     
    /**
     * 
     * @param s
     * @return
     */
    public static String FromBase64(String s)
    {
        return new String(org.apache.commons.codec.binary.Base64.decodeBase64(s),Constants.UTF8_CHARSET);
    }
     
    /**
     * 
     * @param s
     * @return
     */
    public static String FromBase64(byte[] s)
    {
        return new String(org.apache.commons.codec.binary.Base64.decodeBase64(s),Constants.UTF8_CHARSET);
    }
    
    public static String TrimAndRemoveLineBreaks(String text){
    	return text.replace("\n", "").replace("\r", "").trim(); 	
    }
    

     

 
}