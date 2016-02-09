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
package de.governikus.eidassaml.starterkit.natural_persons_attribute;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.governikus.eidassaml.starterkit.EidasAttribute;
import de.governikus.eidassaml.starterkit.EidasNaturalPersonAttributes;
import de.governikus.eidassaml.starterkit.template.TemplateLoader;

public class DateOfBirthAttribute implements EidasAttribute{

	public static final SimpleDateFormat BirthDF = new SimpleDateFormat("yyyy-MM-dd");
	
	private String dateOfBith;
	
	public DateOfBirthAttribute(String date) {
		super();
		this.dateOfBith = date;
	}
	
	public DateOfBirthAttribute(Date date) {
		super();
		this.dateOfBith = BirthDF.format(date);
	}
	
	public String getDate(){
		return dateOfBith;
	}

	@Override
	public String generate() {		
		return TemplateLoader.GetTemplateByName("dateOfBirth").replace("$value", this.dateOfBith);
	}

	@Override
	public String type() {
		return EidasAttribute.TYPE_DateOfBirth;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return type() + " " + getDate();
	}	
	
	@Override
	public EidasNaturalPersonAttributes getNaturalPersonAttributeType() {
		// TODO Auto-generated method stub
		return EidasNaturalPersonAttributes.DateOfBirth;
	}

}
