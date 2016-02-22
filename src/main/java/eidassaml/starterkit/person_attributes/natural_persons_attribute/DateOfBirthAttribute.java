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
package eidassaml.starterkit.person_attributes.natural_persons_attribute;

import java.text.SimpleDateFormat;
import java.util.Date;

import eidassaml.starterkit.EidasAttribute;
import eidassaml.starterkit.EidasNaturalPersonAttributes;
import eidassaml.starterkit.person_attributes.EidasPersonAttributes;
import eidassaml.starterkit.template.TemplateLoader;

public class DateOfBirthAttribute implements EidasAttribute{

	public static final SimpleDateFormat BirthDF = new SimpleDateFormat("yyyy-MM-dd");

	private String dateOfBirth;

	public DateOfBirthAttribute(){}

	public DateOfBirthAttribute(String date) {
		super();
		this.dateOfBirth = date;
	}
	
	public DateOfBirthAttribute(Date date) {
		super();
		this.dateOfBirth = BirthDF.format(date);
	}
	
	public String getDate(){
		return dateOfBirth;
	}

	@Override
	public String generate() {		
		return TemplateLoader.GetTemplateByName("dateOfBirth").replace("$value", this.dateOfBirth);
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
	public EidasPersonAttributes getPersonAttributeType() {
		// TODO Auto-generated method stub
		return EidasNaturalPersonAttributes.DateOfBirth;
	}

	@Override
	public void setValue(String value) {
		this.dateOfBirth=value;
	}

	@Override
	public String getValue() {
		return dateOfBirth;
	}

}
