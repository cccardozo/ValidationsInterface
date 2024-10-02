package postilion.realtime.generictrantest.systemConstans;

import postilion.realtime.sdk.message.bitmap.Iso8583;
import postilion.realtime.sdk.message.bitmap.Iso8583Post;

public class SystemConstants {
	public static final String VALUE_TYPE_CODING = "UTF-8";
	public static final String VALUE_TYPE_MESSENGER_SERVICE_ISO = "ISO";
	public static final String PARAM_MSJ_B24 = "B24";
	public static final String PARAM_PROCESS_VALIDATE = "VALIDATE";
	public static final String PARAM_PROCESS_FIELD41 = "FIELD41";

	// 127.2
	public static final String IATH = "ATM";
	public static final String IATHPb = "ATMPb";
	public static final String ICRD = "Credibanco";
	public static final String ICRDPb = "CredibancoPb";
	public static final String ICOR = "Corresponsal";
	public static final String ICORPb = "CorresponsalPb3";
	public static final String ONE_ZERO = "0";
	public static final String TWO_ZEROS = "00";
	public static final String NUMBER_99 = "99";
	public static final String SPACE = " ";
	public static final String DEFAULT_ACCOUNT_INPUT = "0000000000000000";
	public static final String CODE_DECLINATION = "36";
	public static final String KEY_ISSUER = "ISSUER";
	public static final String DEFAULT_VALUE_ISSUER = "1";
	public static final String KEY_CUSTOMER_ID = "CUSTOMER_ID";
	public static final String DEFAULT_VALUE_CUSTOMER_ID = "0000000000000000000000000";
	public static final String KEY_CUSTOMER_ID_TYPE = "CUSTOMER_ID_TYPE";
	public static final String DEFAULT_CUSTOMER_ID_TYPE = "0";
	public static final String KEY_NOV_CAPA = "NOV_CAPA";
	public static final String VALUE_NOV_CAPA = "S";
	public static final String POS_ENTRY_MODE = "021";
	public static final String DEFAULT_TRACK_2_DATA = "5454541234567890D24122211388313500000";
	public static final String DEFAULT_P42 = "               ";
	public static final String INSTITUTION_ID = "300";
	public static final String POS_PIN_CAPTURE_CODE = "04";
	public static final String POS_DATA_CODE = "911201513344002";
	public static final String PAYEE = "0054150070650000000000000";
	public static final String TYPE_MESSAGE_0220 = "0220";

	// Transaction Identificator
	public static final String IPInterface = "localhost";
	public static final String PORTMSI = "9000/get?";
	public static final String ENDOPOINT = "http://" + IPInterface + ":";

	// Orquestador
	public static final String FIELD = "FIELD_";
	public static final String PORTOQT = "8123";
	public static final String PATHUPLOAD = "http://localhost:9000/get?JSON=";

	// PDF
	public static final String DIRECTORIO = "DIRECTORIO";
	public static final String INIT = "/init";
	public static final String TEST = "/test";
	public static final String MICROSERVICETYPE_P = "PDF";

	public SystemConstants() {
		// TODO Auto-generated constructor stub
	}

}
