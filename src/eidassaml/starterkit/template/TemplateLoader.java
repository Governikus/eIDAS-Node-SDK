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
package eidassaml.starterkit.template;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eidassaml.starterkit.Constants;

public class TemplateLoader {
	
	private static final Log LOG = LogFactory.getLog(TemplateLoader.class);

	private static boolean isInit = false;
	private static HashMap<String,String> map; 
	
	public static synchronized void init() throws IOException{
		if(!isInit){
			map = new HashMap<String, String>();

				map.put("personId", StreamToString(TemplateLoader.class.getResourceAsStream("personId_template.xml")));
				map.put("dateOfBirth", StreamToString(TemplateLoader.class.getResourceAsStream("dateOfBirth_template.xml")));
				map.put("familyname", StreamToString(TemplateLoader.class.getResourceAsStream("familyname_template.xml")));
				map.put("givenname", StreamToString(TemplateLoader.class.getResourceAsStream("givenname_template.xml")));
				map.put("givenname_transliterated", StreamToString(TemplateLoader.class.getResourceAsStream("givenname_transliterated_template.xml")));
				map.put("asso", StreamToString(TemplateLoader.class.getResourceAsStream("asso_template.xml")));
				map.put("resp", StreamToString(TemplateLoader.class.getResourceAsStream("resp_template.xml")));
				map.put("familyname_transliterated", StreamToString(TemplateLoader.class.getResourceAsStream("familyname_transliterated_template.xml")));
				map.put("auth", StreamToString(TemplateLoader.class.getResourceAsStream("auth_template.xml")));
				map.put("metadataservice", StreamToString(TemplateLoader.class.getResourceAsStream("metadata_service_template.xml")));		
				map.put("gender", StreamToString(TemplateLoader.class.getResourceAsStream("gender_template.xml")));
				map.put("birthName", StreamToString(TemplateLoader.class.getResourceAsStream("birthName_Template.xml")));
				map.put("birthName_transliterated", StreamToString(TemplateLoader.class.getResourceAsStream("birthName_transliterated_Template.xml")));
				map.put("currentAddress", StreamToString(TemplateLoader.class.getResourceAsStream("currentaddress_template.xml")));
				map.put("placeOfBirth", StreamToString(TemplateLoader.class.getResourceAsStream("placeOfBirth_template.xml")));
				map.put("metadatanode", StreamToString(TemplateLoader.class.getResourceAsStream("metadata_nodes_template.xml")));	
				map.put("failresp", StreamToString(TemplateLoader.class.getResourceAsStream("fail_resp_template.xml")));
				isInit = true;
		}
		
	}
	
	public static synchronized String GetTemplateByName(String name){
		return new String(map.get(name));
	}
	
	private static String StreamToString(InputStream ins) throws IOException{
		byte[] inBytes = new byte[1024];
		byte[] returnVaue;
        try(ByteArrayOutputStream outs = new ByteArrayOutputStream()){
	        int numReadBytes = ins.read(inBytes);
	 
	        while (numReadBytes > 0)
	        {
	          outs.write(inBytes, 0, numReadBytes);
	          numReadBytes = ins.read(inBytes);
	        }
	        returnVaue = outs.toByteArray();
        }
        return new String(returnVaue,Constants.UTF8_CHARSET);
	}
	
}