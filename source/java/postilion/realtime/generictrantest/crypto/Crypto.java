package postilion.realtime.generictrantest.crypto;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import postilion.realtime.generictrantest.GenericInterfaceTranTest;
import util.Logger;
import postilion.realtime.sdk.crypto.CryptoCfgManager;
import postilion.realtime.sdk.crypto.CryptoManager;
import postilion.realtime.sdk.crypto.DesKvc;
import postilion.realtime.sdk.eventrecorder.EventRecorder;
import postilion.realtime.sdk.ipc.SecurityManager;
import postilion.realtime.sdk.ipc.XEncryptionKeyError;
import postilion.realtime.sdk.message.bitmap.Iso8583;
import postilion.realtime.sdk.message.bitmap.ServiceRestrictionCode;
import postilion.realtime.sdk.message.bitmap.Track2;
import postilion.realtime.sdk.message.bitmap.XFieldUnableToConstruct;
import postilion.realtime.sdk.util.XPostilion;
import postilion.realtime.sdk.util.convert.Pack;
import postilion.realtime.sdk.util.convert.Transform;

public class Crypto {
	
	HSMDirectorBuild hsmComm;
	CryptoCfgManager crypcfgman;
	
	private static postilion.realtime.sdk.ipc.SecurityManager SEC_MANAGER;

	static {
		try {
			SEC_MANAGER = new postilion.realtime.sdk.ipc.SecurityManager();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Crypto(boolean log) {
		this.hsmComm = new HSMDirectorBuild();
		Logger.logLine("ip Atalla:" + GenericInterfaceTranTest.ipACryptotalla, log);
		Logger.logLine("port Atalla:" + GenericInterfaceTranTest.portACryptotalla, log);
		this.hsmComm.openConnectHSM(GenericInterfaceTranTest.ipACryptotalla, GenericInterfaceTranTest.portACryptotalla);
		
	}
	
	public static String getHashPanCNB(String pan) throws Exception {
		return SEC_MANAGER.hashToString(pan, SecurityManager.DigestAlgorithm.HMAC_SHA1, true);
	}
	
	/**
	 * Obtiene el id de cuenta en claro.
	 * 
	 * @param object El id de la cuenta cifrado.
	 * @return Id de cuenta en claro.
	 * @throws XEncryptionKeyError En caso de error.
	 */
	public static String getClearAccount(String encryptedAccId) throws XEncryptionKeyError {
		return SEC_MANAGER.decryptToString(encryptedAccId);
	}
	
	public String translatePin(String inKeyCryptogram, String outKeyCryptogram, String pinBlock, String idDoc, boolean log) {
		String translatedPin = "FFFFFFFFFFFFFFFF";
		try {
			
			// Comando 31 TRANSLATE PIN
			String command31 = "<31#1#" + inKeyCryptogram + "#" + outKeyCryptogram + "#" + pinBlock + "#" + idDoc + "#>";
			Logger.logLine("command31:" + command31, log);
			String resCommand31[] = this.hsmComm.sendCommand(command31, GenericInterfaceTranTest.ipACryptotalla, GenericInterfaceTranTest.portACryptotalla).split("#");
			Logger.logLine("resCommand31:" + Arrays.toString(resCommand31), log);
			if(resCommand31[2].equals("Y"))
				translatedPin = resCommand31[1];
		}  catch (Exception e) {
			EventRecorder.recordEvent(e);
			e.printStackTrace();
		} finally {
			this.hsmComm.closeConnectHSM();
		}
		return translatedPin;
	}
	
	/**
	 * Construye y envia a ejecuci�n el comando 32 de la caja Atalla verificaci�n de
	 * pin IBM 3624.
	 * 
	 * @param pinBlock  pin encriptado
	 * @param kwp       llave kwp bajo la cual se encripto el pin
	 * @param pinOffset
	 * @param pan       n�mero de identificaci�n unico de la tarjeta
	 * @param kvp       llave kvp para hacer la verificaci�n del pin
	 * @return Y if PIN block passes the sanity check and is successfully verified.
	 *         N if PIN block is not successfully verified. S if PIN block contains
	 *         invalid pad or PIN characters or PIN's length is out of range and
	 *         PIN-length error checking (option A1) has not been enabled. L if PIN
	 *         Length error encountered when PIN-length error checking enabled.
	 * 
	 */
	public boolean validatePin(String pinBlock, String kwp, String pinOffset, String pan, String kvp, boolean log) {
		boolean result = false;
		String command32 = "<32#2#3#" + pinBlock + "#" + kwp + "#0123456789012345#" + pinOffset + "#" + pan.substring(4) + "#F#4#" + kvp + "#F#" + pan.substring(3, 15) + "#>";
		Logger.logLine("command31:" + command32, log);
		String resCommand32[] = this.hsmComm.sendCommand(command32, GenericInterfaceTranTest.ipACryptotalla, GenericInterfaceTranTest.portACryptotalla).split("#");
		Logger.logLine("resCommand31:" + Arrays.toString(resCommand32), log);
		if(resCommand32[1].equals("Y"))
			result = true;
		return result;
	}
	
	/**
	 * Construye y envia a ejecuci�n el comando 33 de la caja Atalla verificaci�n de
	 * pin IBM 3624.
	 * 
	 * @param pinBlock  pin encriptado
	 * @param kwp       llave kwp bajo la cual se encripto el pin
	 * @param pinOffset
	 * @param pan       n�mero de identificaci�n unico de la tarjeta
	 * @param kvp       llave kvp para hacer la verificaci�n del pin
	 * @return Y if PIN block passes the sanity check and is successfully verified.
	 *         N if PIN block is not successfully verified. S if PIN block contains
	 *         invalid pad or PIN characters or PIN's length is out of range and
	 *         PIN-length error checking (option A1) has not been enabled. L if PIN
	 *         Length error encountered when PIN-length error checking enabled.
	 * 
	 */
	public String convertPin(String pinBlock, String kwpChannel, String idDoc, boolean log) {
		String result = "FFFFFFFFFFFFFFFF";
		try {
			String command33 = "<33#13#" + kwpChannel + "#" + kwpChannel + "#" + pinBlock + "#F#" + idDoc + "#>";
			Logger.logLine("command33:" + command33, log);
			String resCommand33[] = this.hsmComm.sendCommand(command33, GenericInterfaceTranTest.ipACryptotalla, GenericInterfaceTranTest.portACryptotalla).split("#");
			Logger.logLine("resCommand33:" + Arrays.toString(resCommand33), log);
			if(resCommand33[2].equals("Y"))
				result = resCommand33[1];
			
		}catch(Exception e) {
			Logger.logLine("Convert error: " + e.toString(), log);
			EventRecorder.recordEvent(new Exception(e.toString()));
		}
		
		
		return result;
	}
	
	/**
	 * Construye y envia a ejecuci�n el comando 37 de la caja Atalla cambio de PIN
	 * 
	 * 
	 * @param technique      tecnica ara la verificaci�n o generaci�n del pinoffset
	 * @param pinBlockType   Tipo de PIN block entrante
	 * @param oldPinblock    PIN block antiguo.
	 * @param workingKey     LLave KWP.
	 * @param offset         offset a verificar.
	 * @param validationData informaci�n de validaci�n, unica para el
	 *                       tarjeta-habiente.
	 * @param pad            caracter de relleno
	 * @param checkLength    longitud del PIN
	 * @param validationKey  Llave KVP
	 * @param newPinblock    nuevo PIN block
	 * @param pinBlockData   informaci�n de PIN Block
	 * @return pinblock traducido a la llave de salida
	 */
	public String changePIN(String oldPinblock, String kwpChannel, String pinOffset, String pan, String validationKey, String newPinblock, boolean log) {
		
		String result = "FFFF";
		
		try {
			String command37 = "<37#2#3#" + oldPinblock + "#" + kwpChannel + "#0123456789012345#" + pinOffset + "#" + 
								pan.substring(4) + "#F#4#" + validationKey + "#" + newPinblock + "#F#" + pan.substring(3, 15) + "#>";
			Logger.logLine("command37:" + command37, log);
			String resCommand37[] = this.hsmComm.sendCommand(command37, GenericInterfaceTranTest.ipACryptotalla, GenericInterfaceTranTest.portACryptotalla).split("#");
			Logger.logLine("resCommand37:" + Arrays.toString(resCommand37), log);
			if(resCommand37[1].equals("Y"))
				result = resCommand37[2];
			
		}catch (Exception e) {
			Logger.logLine("Convert error: " + e.toString(), log);
			EventRecorder.recordEvent(new Exception(e.toString()));
			result = "FFFF";
		}
		return result;
		
	}
	
	
	
	public boolean validateCvv(Iso8583 msg, boolean log) throws XPostilion {
		DesKvc kvc = null;
		boolean validCvv = false;
		try {
			
			CryptoCfgManager crypcfgman = CryptoManager.getStaticConfiguration();
			Track2 track2 = msg.getTrack2Data();
			String cvv = msg.getField(Iso8583.Bit._035_TRACK_2_DATA).substring(30, 33);
			
			ServiceRestrictionCode serviceCode = new ServiceRestrictionCode("000");
			// String cvv = msg.getField(Iso8583Post.Bit._057_AUTH_LIFE_CYCLE).substring(22,
			// 25); // CVV2
			String expiryDate = track2.getExpiryDate().substring(2) + track2.getExpiryDate().substring(0, 2);
			kvc = crypcfgman.getKvc("HKASCV2A");
			if (!kvc.verifyCvv(track2.getPan(), expiryDate, serviceCode, cvv)) {
				validCvv = false;
			}else {
				validCvv = true;
			}
			
		} catch (XFieldUnableToConstruct e) {
			Logger.logLine("Convert error: " + e.toString(), log);
			EventRecorder.recordEvent(new Exception(e.toString()));
		}
		return validCvv;
	}
	
	/**
	 * Hace un xor byte por byte de los arreglos, los arreglos debe ser de igual
	 * tama�o
	 * 
	 * @param array1
	 * @param array2
	 * @return arreglo de byte con el resultado de la operacion
	 */
	public static byte[] xorByteArray(byte[] array1, byte[] array2) {
		byte[] xorResult = new byte[array1.length];
		for (int i = 0; i < array1.length; i++) {
			xorResult[i] = (byte) (array2[i] ^ array1[i]);
		}
		return xorResult;
	}
	
	/**
	 * Pasa un arreglo de byte a un string en hexadecimal
	 * 
	 * @param data arreglo a traducir
	 * @return String en hexadecimal que representa el arreglo de bytes recibido.
	 */
	public static String byteArraytoHexString(byte[] data) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			hexString.append(
					(Integer.toHexString(0xFF & data[i]).length() == 1) ? "0" + Integer.toHexString(0xFF & data[i])
							: Integer.toHexString(0xFF & data[i]));
		}
		return hexString.toString().toUpperCase();
	}
	
	
	/**
	 * Encripta la informaci�n suministrada con la llave en claro suministrada, con
	 * algoritmo DES - ECB - sin relleno.
	 * 
	 * @param rawKey llave en claro - representaci�n hexadecimal.
	 * @param data   Informaci�n en claro - representaci�n hexadecimal
	 * @return infromaci�n encriptada - representaci�n hexadecimal.
	 */
	public static String encryptionDES(String keyStatic, String data) {
		String cipherText = "";
		try {
			DESKeySpec keySpec = new DESKeySpec(Transform.fromHexStringToHexData(keyStatic));

			SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyfactory.generateSecret(keySpec);


			Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, key);

			byte[] plainTextBytes = cipher.doFinal(Transform.fromHexStringToHexData(data));

			cipherText = byteArraytoHexString(plainTextBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cipherText;
	}
	
	/**
	 * Desencripta la informaci�n suministrada con la llave en claro suministrada, con
	 * algoritmo DES - ECB - sin relleno.
	 * 
	 * @param rawKey llave en claro - representaci�n hexadecimal.
	 * @param data   Informaci�n en claro - representaci�n hexadecimal
	 * @return infromaci�n encriptada - representaci�n hexadecimal.
	 */
	public static String decryptionDES(String llave, String data) {
		String cipherText = "";
		try {
			DESKeySpec keySpec = new DESKeySpec(Transform.fromHexStringToHexData(llave));

			SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyfactory.generateSecret(keySpec);


			Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, key);

			byte[] plainTextBytes = cipher.doFinal(Transform.fromHexStringToHexData(data));

			cipherText = byteArraytoHexString(plainTextBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cipherText;
	}

}
