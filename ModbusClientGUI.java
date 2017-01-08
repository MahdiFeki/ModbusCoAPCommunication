package com.SeminarWoT;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.TimerTask;

import de.re.easymodbus.exceptions.ModbusException;
import de.re.easymodbus.modbusclient.ModbusClient;

public class ModbusClientGUI extends TimerTask {
	
	private ModbusClient modbusClient;
	private static  int[] values ;
	private static int startingAdress;
	private static int quantity ;
	
	public ModbusClientGUI(ModbusClient modbusClient) {
		this.modbusClient = modbusClient;
	}
	
	public ModbusClient getModbusClient() {
		return modbusClient;
	}

	public void setModbusClient(ModbusClient modbusClient) {
		this.modbusClient = modbusClient;
	}
	
	public int[] getValues() {
		return values;
	}

	public void setValues(int[] values) {
		ModbusClientGUI.values = values;
	}

	public static int getQuantity() {
		return quantity;
	}

	public static void setQuantity(int quantity) {
		ModbusClientGUI.quantity = quantity;
	}

	public static int getStartingAdress() {
		return startingAdress;
	}

	public static void setStartingAdress(int startingAdress) {
		ModbusClientGUI.startingAdress = startingAdress;
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
				readFromRegister(startingAdress,quantity );
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