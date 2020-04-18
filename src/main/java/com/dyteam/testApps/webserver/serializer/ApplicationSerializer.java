package com.dyteam.testApps.webserver.serializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dyteam.testApps.webserver.entity.Application;
import com.dyteam.testApps.webserver.entity.CompanyEnvironUrl;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Required for Serializing the Map's key whose type was Application 
 * @author deepak
 *
 */
public class ApplicationSerializer extends JsonSerializer<Application> {

	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public void serialize(Application value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {

		StringWriter writer = new StringWriter();
		mapper.writeValue(writer, value);
		gen.writeFieldName(writer.toString());
		
		
	}
	
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		Map<Application,List<CompanyEnvironUrl>> hashMap = new HashMap<>();
		Application key = new Application();
		key.setApplicationId(1l);
		key.setApplicationName("applicationName");
		List<CompanyEnvironUrl> value = null;
		hashMap.put(key, value);
		StringWriter writer = new StringWriter();
		new ObjectMapper().writeValue(writer, key);
		System.out.println(writer.toString());
	}

}
