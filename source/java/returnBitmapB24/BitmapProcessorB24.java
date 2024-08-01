package returnBitmapB24;

import java.math.BigInteger;

import streamBase24ATM.Base24Atm;


public class BitmapProcessorB24 {

	public BitmapProcessorB24() {
		// TODO Auto-generated constructor stub
	}
	public String getBitMap(Base24Atm msg)  {
		try {
			String trama = new String(msg.getBinaryData());
			StringBuilder bitMap = new StringBuilder().append(trama.substring(16, 32));
			BigInteger initial = new BigInteger(trama.substring(16, 17), 16);
			StringBuilder bitMapBinario = new StringBuilder();
			switch (initial.compareTo(BigInteger.valueOf(4))) {
			case -1:
				bitMapBinario.append("00");
				break;
			case 0:
			case 1:
				switch (initial.compareTo(BigInteger.valueOf(8))) {// Usa Hexadecimal
				case -1:
					bitMapBinario.append("0");
					break;
				case 0:
				case 1:
					bitMap.append(trama.substring(32, 48));
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}
			bitMapBinario.append(new BigInteger(bitMap.toString(), 16).toString(2));
			return bitMapBinario.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
