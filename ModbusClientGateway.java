package com.SeminarWoT;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.TimerTask;
import de.re.easymodbus.exceptions.ModbusException;
import de.re.easymodbus.modbusclient.ModbusClient;

public class ModbusClientGateway extends TimerTask {
	
	private ModbusClient modbusClient;
	private static  int[] values ;
	private static int startingAddress;
	private static int quantity ;
	
	public static int getQuantity() {
		return quantity;
	}
	
	public static void setQuantity(int quantity) {
		ModbusClientGateway.quantity = quantity;
	}

	public static int getStartingAddress() {
		return startingAddress;
	}

	public static void setStartingAddress(int startingAdress) {
		ModbusClientGateway.startingAddress = startingAdress;
	}	
	public int[] getValues() {
		return values;
	}

	public  void setValues(int[] values) {
		ModbusClientGateway.values = values;
	}	
	public ModbusClientGateway(ModbusClient modbusClient) {
		this.modbusClient = modbusClient;
	}
	
	public ModbusClient getModbusClient() {
		return modbusClient;
	}

	public void setModbusClient(ModbusClient modbusClient) {
		this.modbusClient = modbusClient;
	}

	public int[] readFromRegister(int startingAddress, int quantity) 
			throws ModbusException, java.net.UnknownHostException, 
			java.net.SocketException, java.io.IOException{
		int[] values = modbusClient.ReadHoldingRegisters(startingAddress, quantity);
		this.setValues(values);
		return values;
	}
	
	public void writeToMultipleRegisters(int startingAddress, int[] values) 
			throws ModbusException, java.net.UnknownHostException, 
			java.net.SocketException, java.io.IOException{
		modbusClient.WriteMultipleRegisters(startingAddress, values);
	}
	
	public void writeToSingleRegister(int startingAddress, int value)
			throws ModbusException, java.net.UnknownHostException, 
			java.net.SocketException, java.io.IOException{
		modbusClient.WriteSingleRegister(startingAddress, value);
	}
	
	@Override
	public void run() {
			try {
				readFromRegister(startingAddress, quantity);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (ModbusException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}		
}	