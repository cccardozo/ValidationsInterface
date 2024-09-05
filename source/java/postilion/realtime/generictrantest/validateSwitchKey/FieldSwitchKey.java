package postilion.realtime.generictrantest.validateSwitchKey;

import postilion.realtime.generictrantest.systemConstans.SystemConstants;
import postilion.realtime.sdk.message.bitmap.Iso8583;
import postilion.realtime.sdk.message.bitmap.Iso8583.MsgType;
import postilion.realtime.sdk.message.bitmap.Iso8583Post;
import postilion.realtime.sdk.util.XPostilion;

public class FieldSwitchKey {

	public FieldSwitchKey() {
		// TODO Auto-generated constructor stub
	}

	/***************************************************************************************
	 * 
	 * Contruye el campo privado
	 * 
	 * SwitchKey (PrivateBit 2) hacia el TM de un na petici�n.
	 *
	 * @param msg Mensaje.
	 * @return Retorna un objeto String data con el campo construido.
	 * @throws ExceptionSi el campo 37 no existe en el msg.
	 *************************************************************************************/

	public String constructSwitchKeyForReq(Iso8583 msg, String NameInterface) throws XPostilion {
		StringBuilder switchKey = new StringBuilder();
		try {
			switchKey.append(MsgType.toString(msg.getMsgType()))
					.append(msg.isFieldSet(Iso8583Post.Bit._037_RETRIEVAL_REF_NR)
							? msg.getField(Iso8583Post.Bit._037_RETRIEVAL_REF_NR)
							: String.valueOf(System.currentTimeMillis())
									.substring(String.valueOf(System.currentTimeMillis()).length() - 12))
					.append(msg.getField(Iso8583.Bit._013_DATE_LOCAL))
					.append(msg.getField(Iso8583.Bit._012_TIME_LOCAL));

			switch (NameInterface) {
			case SystemConstants.IATH://String --> ATM
				switchKey.append(SystemConstants.TWO_ZEROS);//String 00
				break;
			case SystemConstants.ICRD:
			case SystemConstants.ICRDPb:
				switchKey.append(SystemConstants.NUMBER_99);//99 string
				break;
			default:
				switchKey.append(SystemConstants.TWO_ZEROS);//00
				break;
			}

			switchKey.append(msg.getField(Iso8583Post.Bit._015_DATE_SETTLE));
		} catch (XPostilion e) {
			e.printStackTrace();		}
		return switchKey.toString();
	}

}
