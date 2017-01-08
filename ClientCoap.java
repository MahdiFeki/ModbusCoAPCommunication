package com.SeminarWoT;


import java.io.IOException;
import java.util.Iterator;
import javax.json.JsonObject;
import org.apache.xerces.util.ParserConfigurationSettings;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.thingweb.desc.ThingDescriptionParser;
import de.thingweb.thing.Thing;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JType;

public class ClientCoap{
	private String ipAdress;
	
	public ClientCoap(String ipAdress) {
		super();
		this.ipAdress = ipAdress;
	}

	public String getUri(String ipAdress){
		 String uri = "coap://"+ ipAdress +":5683/";
		 return uri;
	}
	
	public CoapResponse getTd(){
		CoapClient client = new CoapClient(getUri(ipAdress) +"td");
		CoapResponse response = client.get();
		return response;	
	}

	public  String tdParser(){
		String ressource = null ;
		try{
			Thing td = ThingDescriptionParser.fromBytes(getTd().getResponseText().getBytes());
			ressource =  td.getProperties().get(0).getHrefs().get(0);
		} catch (IOException e){
			System.out.println(e.getMessage());
		}
		return ressource;
	}
	
	public String getValue(String res){
		CoapClient client = new CoapClient(getUri(ipAdress) +res);
		String response = client.get().getResponseText();
		
		return response;
	}


		
//		System.out.println(td.getName());
//		System.out.println(td.getMetadata().getAll("uris"));
//		System.out.println(td.getProperties().get(0).getHrefs().get(0));
//		String href = td.getProperties().get(0).getHrefs().get(0);
//		System.out.println(td.getProperties().get(0).getValueType());
//		//String Type = td.getProperties().get(0).getValueType();
//
//		//create my own parser for the value type 
//		JSONObject obj = new JSONObject(response.getResponseText());
//		JSONArray arr = obj.getJSONArray("properties");
//		for (int i = 0; i < arr.length(); i++)
//		{
//			valueType = arr.getJSONObject(i).getJSONObject("valueType");
//		}
//		String type =valueType.getString("type");
//		System.out.println(type);
//		ObjectMapper mapper = new ObjectMapper();
//		JsonNode fstbschema = mapper.readTree(valueType.toString());
//		final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
//
//		final JsonSchema schema = factory.getJsonSchema(fstbschema);
//		ProcessingReport report;
//
//
//		//get the temp values from the server 
//		client.setURI(uri + href);
//		System.out.println("===============\nCC01+10");
//		System.out.println("---------------\nGET /temp\n---------------");
//		response = client.get();
//		System.out.println(response.advanced().getType() + "-" + response.getCode());
//		System.out.println(response.getResponseText());
//		String value =response.getResponseText();
//		JsonNode jnodeValue = mapper.readTree(value);
//		//validate the received value with the schema 
//		report = schema.validate(jnodeValue);
//		System.out.println(report);

}