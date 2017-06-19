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

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.xml.namespace.QName;

public class Constants {
	
	public final static String DefaultProviderName = "DefaultProvider";
	
	public final static SimpleDateFormat SimpleSamlDf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	static{
		SimpleSamlDf.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	
	public static final QName EIDAS_NaturalPerson_EXTENSION_NAME = new QName("http://eidas.europa.eu/attributes/naturalperson",
	          "AuthnRequestExtension", "eidas");
	
	public static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
}
