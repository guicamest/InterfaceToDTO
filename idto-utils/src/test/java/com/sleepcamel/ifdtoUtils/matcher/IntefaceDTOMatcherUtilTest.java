package com.sleepcamel.ifdtoUtils.matcher;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sleepcamel.ifdtoUtils.valueConverters.IValueConverter;

public class IntefaceDTOMatcherUtilTest {

	private static final String SIMPLE_EXAMPLE = "<option[^>]+value=\"([^\"]*+)\"[^>]*+>([^<]++)</option>";
	private static final String EMPTY_OPTIONAL_VALUE = "<tr[^>]*+>[^<]*+<td[^>]*+>([^<]*+)</td[^>]*+>[^<]*+<td[^>]*+>([^<]*+)</td[^>]*+>[^<]*+(?:<td[^>]*+>([^<]*+)</td[^>]*+>)?[^<]*+</tr[^>]*+>";
	private static final String OPT_VALUE_IN_MIDDLE = "<span[^>]+class=\"name\"[^>]*+>([^<]+)</span>[^<]*+(?:<span[^>]+class=\"middleName\"[^>]*+>([^<]+)</span>)?[^<]*+<span[^>]+class=\"lastName\"[^>]*+>([^<]+)</span>";
	private static final String DIFFERENT_TYPES_PATTERN = "<span[^>]+class=\"name\"[^>]*+>([^<]+)</span>[^<]*+<span[^>]+class=\"age\"[^>]*+>([^<]+)</span>[^<]*+<span[^>]+class=\"agemillis\"[^>]*+>([^<]+)</span>[^<]*+<span[^>]+class=\"avgpoopsyear\"[^>]*+>([^<]+)</span>[^<]*+<span[^>]+class=\"ownerage\"[^>]*+>([^<]+)</span>[^<]*+<span[^>]+class=\"owneragemillis\"[^>]*+>([^<]+)</span>[^<]*+<span[^>]+class=\"avgfoodyear\"[^>]*+>([^<]+)</span>";
	private static final String CUSTOM_CONVERTER_PATTERN = "<span[^>]+class=\"nameAndAliases\"[^>]*+>([^-]++)-\\s*([^<]+)</span>";

	String htmlString;
	
	@Before
	public void readHTML() throws IOException{
		InputStream resourceAsStream = getClass().getResourceAsStream("/regexTest1.html");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
		StringBuffer sb = new StringBuffer();
		
		String lineRead = bufferedReader.readLine();
		while(lineRead != null){
			sb.append(lineRead);
			lineRead = bufferedReader.readLine();
		}
		bufferedReader.close();
		htmlString = sb.toString();
	}

	@Test
	public void testSimple(){
		assertNotNull(htmlString);
		InterfaceDTOMatcherUtils<ICountryOption> countryOptionMatcher = InterfaceDTOMatcherUtils.getFor(ICountryOption.class);
		List<ICountryOption> countryOptions = countryOptionMatcher.fromString(htmlString).withPattern(SIMPLE_EXAMPLE).list();
		assertEquals(countryOptions.size(), 6);
		assertEquals(countryOptions.get(0).getCode(),"");
		assertEquals(countryOptions.get(0).getName(),"All");
		
		assertEquals(countryOptions.get(1).getCode(),"AR");
		assertEquals(countryOptions.get(1).getName(),"Argentina");
		
		assertEquals(countryOptions.get(2).getCode(),"BR");
		assertEquals(countryOptions.get(2).getName(),"Brazil");
		
		assertEquals(countryOptions.get(3).getCode(),"IT");
		assertEquals(countryOptions.get(3).getName(),"Italy");
		
		assertEquals(countryOptions.get(4).getCode(),"SP");
		assertEquals(countryOptions.get(4).getName(),"Spain");
		
		assertEquals(countryOptions.get(5).getCode(),"US");
		assertEquals(countryOptions.get(5).getName(),"United States");
	}

	@Test
	public void testEmptyAndOptionalValues(){
		assertNotNull(htmlString);
		InterfaceDTOMatcherUtils<IContactInfo> tableMatcher = InterfaceDTOMatcherUtils.getFor(IContactInfo.class);
		List<IContactInfo> contactInfo = tableMatcher.fromString(htmlString).withPattern(EMPTY_OPTIONAL_VALUE).list();

		assertEquals(contactInfo.size(), 8);
		assertEquals(contactInfo.get(0).getFirstName(),"Pepe");
		assertEquals(contactInfo.get(0).getLastName(),"Lorenzo");
		assertEquals(contactInfo.get(0).getPhone(),"4454-3432");
		
		assertEquals(contactInfo.get(2).getFirstName(),"");
		assertEquals(contactInfo.get(2).getLastName(),"Doe");
		assertEquals(contactInfo.get(2).getPhone(),"4756-6598");
		
		assertEquals(contactInfo.get(4).getFirstName(),"Amy");
		assertEquals(contactInfo.get(4).getLastName(),"");
		assertEquals(contactInfo.get(4).getPhone(),"4982-1923");
		
		assertEquals(contactInfo.get(6).getFirstName(),"Nick");
		assertEquals(contactInfo.get(6).getLastName(),"Past");
		assertNull(contactInfo.get(6).getPhone());
		
		assertEquals(contactInfo.get(7).getFirstName(),"Back2D");
		assertEquals(contactInfo.get(7).getLastName(),"Keys");
		assertEquals(contactInfo.get(7).getPhone(),"4712-2918");
	}
	
	@Test
	public void testOptionalValueInMiddle(){
		assertNotNull(htmlString);
		InterfaceDTOMatcherUtils<IContactFullName> fullNameMatcher = InterfaceDTOMatcherUtils.getFor(IContactFullName.class);
		List<IContactFullName> contactsFullName = fullNameMatcher.fromString(htmlString).withPattern(OPT_VALUE_IN_MIDDLE).list();

		assertEquals(contactsFullName.size(), 2);
		assertEquals(contactsFullName.get(0).getFirstName(),"Ban");
		assertNull(contactsFullName.get(0).getMiddleName());
		assertEquals(contactsFullName.get(0).getLastName(),"Quitos");
		
		assertEquals(contactsFullName.get(1).getFirstName(),"Este");
		assertEquals(contactsFullName.get(1).getMiddleName(),"Ban");
		assertEquals(contactsFullName.get(1).getLastName(),"Quito");
	}
	
	@Test
	public void testNativeConverters(){
		assertNotNull(htmlString);
		InterfaceDTOMatcherUtils<IDog> dogMatcher = InterfaceDTOMatcherUtils.getFor(IDog.class);
		List<IDog> dogs = dogMatcher.fromString(htmlString).withPattern(DIFFERENT_TYPES_PATTERN).list();

		int age = 17;
		long ageMillis = 123456789;
		double poops = 2.3;
		Integer ownerAge = 37;
		Long ownerAgeMillis = 456789123L;
		Double food = 5.974;
		
		assertEquals(dogs.size(), 1);
		assertEquals(dogs.get(0).getName(),"J.C. Dog");
		assertEquals(dogs.get(0).getAge(),age);
		assertEquals(dogs.get(0).getAgeInMillis(),ageMillis);
		assertEquals(dogs.get(0).getAvgPoopsPerYear(),poops, 0.001);
		assertEquals(dogs.get(0).getOwnersAge(),ownerAge);
		assertEquals(dogs.get(0).getOwnersInMillis(),ownerAgeMillis);
		assertEquals(dogs.get(0).getAvgFoodPerYear(),food);
	}
	
	@Test
	public void testCustomConverters(){
		assertNotNull(htmlString);
		InterfaceDTOMatcherUtils<INameAndAliases> nameWithAliasesMatcher = InterfaceDTOMatcherUtils.getFor(INameAndAliases.class);
		
		nameWithAliasesMatcher.converter(String[].class,new IValueConverter<String[]>() {
			public String[] convertValue(String s) {
				return s.split(" and ");
			}
		});
		
		List<INameAndAliases> namesAndAliases = nameWithAliasesMatcher.fromString(htmlString).withPattern(CUSTOM_CONVERTER_PATTERN).list();

		assertEquals(namesAndAliases.size(), 1);
		assertEquals(namesAndAliases.get(0).getName(),"J.C. Dog");
		
		String[] aliases = namesAndAliases.get(0).getAliases();
		assertEquals(aliases.length, 4);
		assertEquals(aliases[0], "Jonnhy");
		assertEquals(aliases[1], "Charly");
		assertEquals(aliases[2], "D");
		assertEquals(aliases[3], "JCD");
	}
}

interface ICountryOption{
	String getCode();
	String getName();
}

interface IContactInfo{
	String getFirstName();
	String getLastName();
	String getPhone();
}

interface IContactFullName{
	String getFirstName();
	String getMiddleName();
	String getLastName();
}

interface INameAndAliases{
	String getName();
	String[] getAliases();
}

interface IDog{
	String getName();
	int getAge();
	long getAgeInMillis();
	double getAvgPoopsPerYear();
	Integer getOwnersAge();
	Long getOwnersInMillis();
	Double getAvgFoodPerYear();
}