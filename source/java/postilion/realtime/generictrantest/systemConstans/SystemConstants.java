package postilion.realtime.generictrantest.systemConstans;


public class SystemConstants {
	public static final String VALUE_TYPE_CODING = "UTF-8";
	public static final String VALUE_TYPE_MESSENGER_SERVICE_ISO = "ISO";
	public static final String PARAM_MSJ_B24 = "B24";
	public static final String PARAM_PROCESS_VALIDATE = "VALIDATE";
	public static final String PARAM_PROCESS_FIELD41 = "FIELD41";
	
	//127.2
	public static final String IATH = "ATM";
	public static final String IATHPb = "ATMPb";
	public static final String ICRD = "Credibanco";
	public static final String ICRDPb = "CredibancoPb";
	public static final String ICOR = "Corresponsal";
	public static final String ICORPb = "CorresponsalPb3";
	public static final String TWO_ZEROS ="00";
	public static final String NUMBER_99 = "99";
	
	//Transaction Identificator
	public static final String IPInterface = "localhost";
	public static final String PORTMSI = "9000/get?";
	public static final String ENDOPOINT = "http://"+IPInterface+":";
	
	//Orquestador
	public static final String FIELD = "FIELD_";
	public static final String PORTOQT = "8123";
	public static final String PATHUPLOAD = "http://localhost:9000/get?JSON=";
	
	//PDF
	public static final String DIRECTORIO = "DIRECTORIO";
	public static final String INIT = "/init";
	public static final String TEST = "/test";
	public static final String MICROSERVICETYPE_P = "PDF";
	public SystemConstants() {
		// TODO Auto-generated constructor stub
	}

}
