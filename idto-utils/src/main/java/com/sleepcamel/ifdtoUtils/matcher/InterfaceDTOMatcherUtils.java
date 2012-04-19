package com.sleepcamel.ifdtoUtils.matcher;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sleepcamel.ifdtoUtils.ArrayInterfaceDTOTransformer;
import com.sleepcamel.ifdtoUtils.valueConverters.IValueConverter;


public class InterfaceDTOMatcherUtils<T> {

	public static <T> InterfaceDTOMatcherUtils<T> getFor(Class<T> interfaceClass) {
		return new InterfaceDTOMatcherUtils<T>(interfaceClass);
	}

	protected Class<T> interfaceClass;
	private String source;
	private Pattern pattern;
	private ArrayInterfaceDTOTransformer<T> transformer;

	protected InterfaceDTOMatcherUtils(Class<T> interfaceClass) {
		transformer = new ArrayInterfaceDTOTransformer<T>(interfaceClass, true, true);
	}

	public InterfaceDTOMatcherUtils<T> fromString(String sourceString) {
		this.source = sourceString;
		return this;
	}

	public InterfaceDTOMatcherUtils<T> withPattern(String stringPattern) {
		return withPattern(Pattern.compile(stringPattern, Pattern.DOTALL | Pattern.CASE_INSENSITIVE ));
	}
	
	public InterfaceDTOMatcherUtils<T> withPattern(Pattern pattern) {
		this.pattern = pattern;
		return this;
	}

	public List<T> list() {
		List<T> matches = new ArrayList<T>();
		Matcher matcher = pattern.matcher(source);
		
		while(matcher.find()){
			int groupCount = matcher.groupCount();
			Object [] groups = new Object[groupCount];
			for(int i=0; i < groupCount; i++){
				groups[i] = matcher.group(i+1);
			}
			matches.add(transformer.transformArrayToDTO(groups));
		}

		return matches;
	}

	public <E> void converter(Class<E> clazz, IValueConverter<E> valueConverter) {
		transformer.addCustomValueConverter(clazz, valueConverter);
	}

}
