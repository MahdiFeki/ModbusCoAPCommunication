package com.SeminarWoT;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import de.re.easymodbus.modbusclient.ModbusClient;

public class WorkFlow {

	public static String getRessource (int[] ipAddress){
		String decodedRessourceName = "";
		ModbusClient modbusClient = new ModbusClient("127.0.0.1",502);
		try {
			modbusClient.Connect();
			
			//Creating modbusClientGUI sidea and modbusClientGateway side
			ModbusClientGUI modbusClientGUI = new ModbusClientGUI(modbusClient);
			ModbusClientGateway modbusClientGateway = new ModbusClientGateway(modbusClient);

			//Launching the modbusClientGUI after 1 ms to listen to the registers
			Timer timer = new Timer();
			timer.schedule(modbusClientGateway, 1);
			ModbusClientGateway.setQuantity(ipAddress.length);
			ModbusClientGateway.setStartingAddress(10);
			modbusClientGUI.writeToMultipleRegisters(ModbusClientGateway.getStartingAddress(), ipAddress);
			
			//Generating the ip adress from the values written by the modbusClientGUI on the registers 
			Thread.sleep(2);
			int[] values = modbusClientGateway.getValues();
			ClientCoap clientCoap = new ClientCoap(generatingIpAdress(values));

			//Coding the name of the ressource into a table of asciis of each character in the name
			int[] ressourceEncoding = codingRessource(clientCoap.tdParser());
			ModbusClientGUI.setQuantity(ressourceEncoding.length);
			ModbusClientGUI.setStartingAdress(50);

			//Writing these values on registers by the Modbusclient gateway to be read from the ClientGui
			modbusClientGateway.writeToMultipleRegisters(ModbusClientGUI.getStartingAdress(), ressourceEncoding);
			timer.schedule(modbusClientGUI,1);
			Thread.sleep(2);
			int[] valuesresources = modbusClientGUI.getValues();
			decodedRessourceName= decodingRessource(valuesresources);
		
		} catch (Exception e) {
		}
		return decodedRessourceName;	
	}
	
	public static String getValue (String ipAddress, String res){
		String str ="";
		ModbusClient modbusClient = new ModbusClient("127.0.0.1",502);
		try {
			modbusClient.Connect();
			
			//Creating modbusClientGUI sidea and modbusClientGateway side
			ModbusClientGUI modbusClientGUI = new ModbusClientGUI(modbusClient);
			ModbusClientGateway modbusClientGateway = new ModbusClientGateway(modbusClient);

			//Launching the modbusClientGUI after 1 ms to listen to the registers
			Timer timer = new Timer();
			timer.schedule(modbusClientGateway, 1);
			ModbusClientGateway.setQuantity(1);
			ModbusClientGateway.setStartingAddress(100);
			modbusClientGUI.writeToSingleRegister(ModbusClientGateway.getStartingAddress(), 1);
			Thread.sleep(2);
			int[] values = modbusClientGateway.getValues();
			ClientCoap clientCoap = new ClientCoap(ipAddress);
			String ressourceValue = clientCoap.getValue(res);
			String[] splitted =ressourceValue.split("\\."); // will be matched 1 times. 
			int firstVal ;
			int secondVal;
			if (splitted.length ==1){
				firstVal = Integer.parseInt(splitted[0]);  
				secondVal = 00;
			} else{
				firstVal = Integer.parseInt(splitted[0]);  
				secondVal = Integer.parseInt(splitted[1]);
			}
			
			int [] resval = {firstVal,secondVal};
			ModbusClientGUI.setQuantity(2);
			ModbusClientGUI.setStartingAdress(200);
			
			//Writing these values on registers by the Modbusclient gateway to be read from the ClientGui
			modbusClientGateway.writeToMultipleRegisters(ModbusClientGUI.getStartingAdress(), resval);
			timer.schedule(modbusClientGUI,1);
			Thread.sleep(2);
			int[] valuesresources = modbusClientGUI.getValues();
			str =  valuesresources[0]+"."+valuesresources[1];
			
		
		} catch (Exception e) {
		}
		return str;	
	}
	
	public static void main(String[] args){
		int[] ipAddress = {192, 168, 0, 230};
		System.out.println(getValue(generatingIpAdress(ipAddress),getRessource(ipAddress)));
	}

	
	///********************************* Util Methods ***************************************
	private static int[] codingRessource(String ressource){
		Map<Character,Integer> mappingBetweenRessourceAndInt = new HashMap<Character,Integer>();
		int[] mappedRessource = new int[ressource.length()];
		//Dictionary containing the Alphabets  as keys and corresponding ascii as values
		for (char alphabet = 'a'; alphabet <= 'z';alphabet++){   
			int ascii = (int) alphabet;
			mappingBetweenRessourceAndInt.put(alphabet, ascii);
		}
		//Mapping the ressource name using the dictionary in order to write it in registers
		for (int i = 0; i < ressource.length(); i++){
		    char c = ressource.charAt(i);  
		    mappedRessource[i] = mappingBetweenRessourceAndInt.get(c);
		}		
		return mappedRessource;
	}
	
	private static String decodingRessource(int[] mappedRessource){
		String decodedRessourceName = "";
		for (int i = 0; i < mappedRessource.length; i++){
			decodedRessourceName = decodedRessourceName + Character.toString ((char) mappedRessource[i]);
		}		
		return decodedRessourceName;
	}
	
	private static String generatingIpAdress(int[]ipAddressValues){
		String ipAddress = ""+ ipAddressValues[0];
		for (int i = 1 ; i< ipAddressValues.length;++i) {
			ipAddress = ipAddress + "." + ipAddressValues[i];
		}			
		return ipAddress;
	}
}