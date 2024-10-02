package postilion.realtime.generictrantest.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import postilion.realtime.generictrantest.util.Logger;
import postilion.realtime.sdk.eventrecorder.EventRecorder;
import postilion.realtime.sdk.util.XPostilion;

/**
 * This class deifines a basic UDP client
 * 
 * @author Cristian Cardozo
 *
 */
public class Client {

	InetAddress ipAddress;
	int port;
	DatagramSocket socket;

	public Client() {

	}

	public Client(String ipAddress, String port) {

		if (!ipAddress.equals("0") && !port.equals("0")) {
			try {

				if (validateIp(ipAddress))
					this.ipAddress = InetAddress.getByName(ipAddress);
				else
					throw new Exception("IP parameter for server UDP, is not a IP valid");

				if (validatePort(port))
					this.port = Integer.valueOf(port);
				else
					throw new Exception("Port parameter for server UDP, is not a Port valid");
				
			} catch (Exception e) {
				EventRecorder.recordEvent(e);
			}
		}
	}
	
	public Client(String ipAddress, String port, String portOut) {
		if (!ipAddress.equals("0") && !port.equals("0")) {
			try {

				if (validateIp(ipAddress))
					this.ipAddress = InetAddress.getByName(ipAddress);
				else
					throw new Exception("IP parameter for server UDP, is not a IP valid");

				if (validatePort(port))
					this.port = Integer.valueOf(port);
				else
					throw new Exception("Port parameter for server UDP, is not a Port valid");
				
				if (validatePort(portOut))
					this.socket = new DatagramSocket(Integer.valueOf(portOut));
				else
					throw new Exception("Port Out parameter for server UDP, is not a Port valid");
				

			} catch (Exception e) {
				EventRecorder.recordEvent(e);
			}
		}
	}
	
	public void close(){
		if(this.socket != null)
			this.socket.close();
		this.socket = null;
	}

	public void setIpAddress(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public InetAddress getIpAddress() {
		return this.ipAddress;
	}

	public int getPort() {
		return this.port;
	}

	/**
	 * Valida si la informaci�n en el archivo es una ip.
	 * 
	 * @param ip
	 * @return true si es un ip
	 */
	static public boolean validateIp(String ip) {
		boolean ipIsOk = false;
		if (validateByRegex(IP_REGEX, ip)) {
			ipIsOk = true;
		} 
		return ipIsOk;
	}
	

	/**
	 * Valida los valores suministrados contra un expresi�n regular.
	 * 
	 * @param regex
	 *            expresi�n regular
	 * @param fieldText
	 *            valor a comparar.
	 * @return true si el valor cumple la expresi�n regular.
	 */
	public static boolean validateByRegex(String regex, String fieldText) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(fieldText);
		return matcher.matches();
	}

	/**
	 * Valida si la informaci�n en el archivo es un puerto.
	 * 
	 * @param port
	 * @return true si es un puerto
	 */
	static public boolean validatePort(String port) {
		boolean portIsOk = false;
		try {
			int configPort = Integer.parseInt(port);
			if (0 < configPort && configPort < 65536) {
				portIsOk = true;
			} 
		} catch (NumberFormatException e) {
			EventRecorder.recordEvent(e);
		}
		return portIsOk;
	}

	/**
	 * Open a socket to send data over UDP protocol
	 * 
	 * @param data to send
	 */
	public void sendData(byte[] data) {
		try {
			DatagramPacket request = new DatagramPacket(data, data.length, ipAddress, port);
//			DatagramPacket request = new DatagramPacket(data, data.length, ipAddress,
//					port + (int) (Math.random() * 10));
			this.socket.send(request);

		} catch (IOException e) {
			EventRecorder.recordEvent(e);
		} 
	}

	/**
	 * Open a socket to send data over UDP protocol
	 * 
	 * @param data to send
	 */
	public void sendDataFixed(byte[] data) {
		try {
			InetAddress ipAddress = InetAddress.getByName("10.89.0.169");
//			DatagramPacket request = new DatagramPacket(data, data.length, ipAddress, port);
			DatagramPacket request = new DatagramPacket(data, data.length, ipAddress,
					4000 + (int) (Math.random() * 10));

			this.socket.send(request);

		} catch (IOException e) {
			EventRecorder.recordEvent(e);
		} 
	}

	/**
	 * Open a socket to send data over UDP protocol
	 * 
	 * @param data to send
	 */
	public void sendDataString(String data) {
		try {
			DatagramPacket request = new DatagramPacket(data.getBytes(), data.getBytes().length, ipAddress, 50001);
			this.socket.send(request);

		} catch (IOException e) {
			EventRecorder.recordEvent(e);
			} 
	}

	

	static final String IP_REGEX = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

	public static byte[] getMsgKeyValue(String p37, String value, String type, String nameInterface) {

		String key = new String();

		key = type + ":" + nameInterface + "_" + p37 + "_"
				+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ":";
		String lKey = String.valueOf(key.length());
		String llKey = String.valueOf(lKey.length());

		String lValue = String.valueOf(value.length());
		String llValue = String.valueOf(lValue.length());

		String msg = llKey + lKey + key + llValue + lValue + value;
		return (Base64.getEncoder().encodeToString(msg.getBytes())).getBytes();
	}
	
	/**
	 * Formatea mensaje para envio a log udp
	 * 
	 * @param value valor a enviar en el log
	 * @param p37 a enviar
	 * @throws XPostilion
	 */
	public static byte[] formatDatatoSendPinValidation(String value, String p37) {
		return ("RN_POSTILIONVALIDATION_DATA=VALIDATPIN"+" | "+p37+" | "+value).getBytes();
	}
	
	/**
	 * Formatea mensaje para envio a log udp
	 * 
	 * @param value valor a enviar en el log
	 * @param p37 a enviar
	 * @throws XPostilion
	 */
	public static byte[] formatDatatoSendEncryptionData(String value, String p37) {
		return ("RN_POSTILIONVALIDATION_DATA=ENCRYPTDAT"+" | "+p37+" | "+value).getBytes();
	}
	
	/**
	 * Formatea mensaje para envio a log udp
	 * 
	 * @param value valor a enviar en el log
	 * @param p37 a enviar
	 * @throws XPostilion
	 */
	public static byte[] formatDatatoSendDecryptionData(String value, String p37) {
		return ("RN_POSTILIONVALIDATION_DATA=DECRYPTDAT"+" | "+p37+" | "+value).getBytes();
	}
	
	/**
	 * Formatea mensaje para envio a log udp
	 * 
	 * @param value valor a enviar en el log
	 * @param p37 a enviar
	 * @throws XPostilion
	 */
	public static byte[] formatDatatoSendChangePin(String value, String p37) {
		return ("RN_POSTILIONVALIDATION_DATA=CHANGE_PIN"+" | "+p37+" | "+value).getBytes();
	}
	
	/**
	 * Open a socket to send data over UDP protocol
	 * 
	 * @param data            to send
	 * @param waitForResponse
	 * @throws XPostilion
	 */
	public String sendMsgToAtalla(String key, boolean log) throws XPostilion {
		String dataResponse = "";

		try {

			Logger.logLine("key: " + key, log);
			byte[] data = key.getBytes();

			try {
				DatagramSocket socket = new DatagramSocket();
				socket.setSoTimeout(1000);
//				DatagramPacket request = new DatagramPacket(data, data.length, ipAddress, port);
				Logger.logLine("data: " + data, log);
				Logger.logLine("data.length: " + data.length, log);
				Logger.logLine("ipAddress: " + ipAddress, log);
				Logger.logLine("port: " + port, log);
//				DatagramPacket request = new DatagramPacket(data, data.length, ipAddress,
//						50000 + Integer.parseInt(p11.substring(p11.length() - 1)));
				DatagramPacket request = new DatagramPacket(data, data.length, ipAddress, port);
				Logger.logLine("request getSocketAddress: " + request.getSocketAddress(), log);
				Logger.logLine("request getAddress: " + request.getAddress(), log);
				Logger.logLine("Send request: " + request.getData(), log);
				socket.send(request);
				byte[] bufer = new byte[4072];// 4072
				DatagramPacket respuesta = new DatagramPacket(bufer, bufer.length);
				socket.receive(respuesta);
				Logger.logLine("respuesta.getData()).trim(): " + respuesta.getData(), log);
				dataResponse = new String(respuesta.getData()).trim();
				Logger.logLine("data incoming: " + dataResponse, log);
				socket.close();
			} catch (SocketTimeoutException e) {
				dataResponse = "TIMEOUT";
			} catch (IOException e) {
				EventRecorder.recordEvent(e);
				dataResponse = "TIMEOUT";
			}

		} catch (Exception e) {

			EventRecorder.recordEvent(e);
			dataResponse = "TIMEOUT";
		}
		return dataResponse;
	}

}
