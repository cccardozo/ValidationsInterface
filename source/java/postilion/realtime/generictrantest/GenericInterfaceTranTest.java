package postilion.realtime.generictrantest;

import java.io.FileReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.parser.JSONParser;

import enlistGeneralMessage.EnlistMessageB24Iso;
import postilion.realtime.generictrantest.udp.Client;
import postilion.realtime.sdk.eventrecorder.EventRecorder;
import postilion.realtime.sdk.message.IMessage;
import postilion.realtime.sdk.message.bitmap.BitmapMessage;
import postilion.realtime.sdk.message.bitmap.Iso8583;
import postilion.realtime.sdk.message.bitmap.Iso8583Post;
import postilion.realtime.sdk.node.AInterchangeDriver8583;
import postilion.realtime.sdk.node.AInterchangeDriverEnvironment;
import postilion.realtime.sdk.node.Action;
import postilion.realtime.sdk.node.XNodeParameterValueInvalid;
import postilion.realtime.sdk.util.convert.Pack;
import streamBase24ATM.Base24Atm;
import systemConstans.SystemConstants;
import util.Logger;

public class GenericInterfaceTranTest extends AInterchangeDriver8583 {
	private String typeMessage;
	private String transactionIdentification;
	private String routeKeysHSM;
	private String process;
	public static Map<String, Object> infoTrasactionIdentidicator = new ConcurrentHashMap<String, Object>(); 
	public static Map<String, String> keysHSM = new ConcurrentHashMap<String, String>(); 
	public String nameInterface = "";
	public static String ipACryptotalla;
	public static int portACryptotalla;
	private boolean enableLog;
	public String ipUdpServer = "0";
	public String portUdpServer = "0";
	public String portUdpClient = "0";
	public static Client udpClient = null;
	
	@Override
	public IMessage newMsg(byte[] data) throws Exception {
		try {
			IMessage msg = null;
			// long tStart = System.currentTimeMillis();
			String msgType = new String(data, 0, 3);
			BitmapMessage inMsg = null;
			if (msgType.equals(SystemConstants.VALUE_TYPE_MESSENGER_SERVICE_ISO)) {
				// para utilizar con MAC
				// inMsg = new Base24Ath(kwa);
				inMsg = new Base24Atm(null);
				inMsg.fromMsg(data);
				msg = inMsg;
			} else {
				inMsg = new Iso8583();
				//inMsg = new Iso8583Post();
				inMsg.fromMsg(data);
				msg = inMsg;
			}
			return msg;
		} catch (Exception e) {
			EventRecorder.recordEvent(e);
		}
		return null;
	}

	@Override
	public void init(AInterchangeDriverEnvironment interchange) throws Exception {
		super.init(interchange);
		
		String[] userParams = Pack.splitParams(interchange.getUserParameter());
		this.nameInterface = interchange.getName();
		
		loadParameters(userParams[0]);
		fillKeys();
		
		udpClient = new Client(ipUdpServer, portUdpServer, portUdpClient);
		
		keysHSM.forEach((k,v) -> {
			Logger.logLine("LlavesHSM key: " + k + " with value: " + v, enableLog);
		});
		
	}
	
	/************************************************************************************
	 * Metodo que valida los parametros de usuario de la interchange
	 *
	 * 
	 * @throws XNodeParameterValueInvalid
	 *
	 ************************************************************************************/
	public void loadParameters(String path) throws XNodeParameterValueInvalid {
		try {
			JSONParser parser = new JSONParser();

			org.json.simple.JSONObject jsonObjects = (org.json.simple.JSONObject) parser.parse(new FileReader(path));

			org.json.simple.JSONObject parameters = (org.json.simple.JSONObject) jsonObjects.get(this.nameInterface);
			if (parameters != null) {
				typeMessage = parameters.get("TypeMessage").toString();
				transactionIdentification = parameters.get("TransactionIdentification").toString();
				ipACryptotalla = parameters.get("ipCryptoAtalla").toString();
				portACryptotalla = Integer.valueOf(parameters.get("portCryptoAtalla").toString());
				routeKeysHSM = parameters.get("routeKeysHSM").toString();
				process = parameters.get("Process").toString();
				enableLog = Boolean.valueOf(parameters.get("EnableLog").toString());
				ipUdpServer = parameters.get("IP_UDP_SERVER").toString();
				portUdpServer = parameters.get("PORT_UDP_SERVER").toString();
				portUdpClient = parameters.get("PORT_UDP_CLIENT").toString();
			}
			
			Logger.logLine("typeMessage:"+typeMessage, enableLog);
			Logger.logLine("transactionIdentification:"+transactionIdentification, enableLog);
			Logger.logLine("ipACryptotalla:"+ipACryptotalla, enableLog);
			Logger.logLine("portACryptotalla:"+portACryptotalla, enableLog);
			Logger.logLine("routeKeysHSM:"+routeKeysHSM, enableLog);
			Logger.logLine("ipUdpServer:"+ipUdpServer, enableLog);
			Logger.logLine("portUdpServer:"+portUdpServer, enableLog);
			Logger.logLine("portUdpClient:"+portUdpClient, enableLog);
			
		} catch (Exception e) {
			EventRecorder.recordEvent(e);
		}
	}
	
	
	public void fillKeys() {

		try (FileReader fr = new FileReader(routeKeysHSM)) {
			JSONParser parser = new JSONParser();
			org.json.simple.JSONArray jsonArray = (org.json.simple.JSONArray) parser.parse(fr);
			for (Object object : jsonArray) {
				org.json.simple.JSONObject canal = (org.json.simple.JSONObject) object;

				String strId = (String) canal.get("Id");
				String strChannel = (String) canal.get("Channel");
				String strKey = (String) canal.get("Key");
				String strRoute = (String) canal.get("Route");

				keysHSM.put(strId.toString(), strKey.toString()+"_"+strChannel.toString()+"_"+strRoute.toString());

			}
			fr.close();
		} catch (Exception e) {
			EventRecorder.recordEvent(e);
		}

	}
	// Viene desde remoto 0200 --> Desde Nodo Source
	@Override
	public Action processTranReqFromInterchange(AInterchangeDriverEnvironment interchange, Iso8583 msg)
			throws Exception {
		Action actionToTake = new Action();
		EnlistMessageB24Iso enlistMessageB24Iso = new EnlistMessageB24Iso();
		
		switch (msg.getField(Iso8583.Bit._041_CARD_ACCEPTOR_TERM_ID).substring(14, 15)) {
		case "1":
			actionToTake.putMsgToRemote(enlistMessageB24Iso.validatePin(msg,enableLog,process));
			break;
		case "2":
			actionToTake.putMsgToRemote(enlistMessageB24Iso.validatePinCvv(msg,enableLog,process));		
			break;
		case "3":
			actionToTake.putMsgToRemote(enlistMessageB24Iso.validateCvv(msg,enableLog,process));
			break;
		case "4":
			actionToTake.putMsgToRemote(enlistMessageB24Iso.changePin(msg,enableLog,process));
			break;

		default:
			
			actionToTake.putMsgToTranmgr(enlistMessageB24Iso.copyMessagetoIso8583Post(msg,transactionIdentification,enableLog,process));
			break;
		}
		
//		if (process.equals(SystemConstants.PARAM_PROCESS_VALIDATE)
//				|| (process.equals(SystemConstants.PARAM_PROCESS_FIELD41) && msg.getField(Iso8583.Bit._041_CARD_ACCEPTOR_TERM_ID).substring(13, 14).equals("0")))
//			actionToTake.putMsgToRemote(enlistMessageB24Iso.validatePin(msg,enableLog,process));
//		else
//			actionToTake.putMsgToTranmgr(enlistMessageB24Iso.copyMessagetoIso8583Post(msg,transactionIdentification,enableLog,process));
		return actionToTake;
	}

	// Viene desde TM 0210 --> Hacia Nodo Source
	@Override
	public Action processTranReqRspFromTranmgr(AInterchangeDriverEnvironment interchange, Iso8583Post msg)
			throws Exception {
		Action respToRemoto = new Action();
		EnlistMessageB24Iso enlistMessageB24Iso = new EnlistMessageB24Iso();
		if (typeMessage.equals(SystemConstants.PARAM_MSJ_B24)) {
			respToRemoto.putMsgToRemote(enlistMessageB24Iso.copyMessagetoB24(msg));
		} else {
			respToRemoto.putMsgToRemote(msg);
		}
		return respToRemoto;
	}

	// Viene desde TM 0200 --> Desde Nodo Sink
	@Override
	public Action processTranReqFromTranmgr(AInterchangeDriverEnvironment interchange, Iso8583Post msg)
			throws Exception {
		Action actionToTake = new Action();
		EnlistMessageB24Iso enlistMessageB24Iso = new EnlistMessageB24Iso();
		if (typeMessage.equals(SystemConstants.PARAM_MSJ_B24)) {
			actionToTake.putMsgToRemote(enlistMessageB24Iso.copyMessagetoB24(msg));
		} else {
			actionToTake.putMsgToRemote(msg);
		}
		return actionToTake;
	}

	// Viene desde Remoto 0210 --> Hacia Nodo Sink
	@Override
	public Action processTranReqRspFromInterchange(AInterchangeDriverEnvironment interchange, Iso8583 msg)	throws Exception {
		Action respToTranmgr = new Action();
		msg.putMsgType(Iso8583Post.MsgType.getResponse(msg.getMsgType()));
		msg.putField(Iso8583Post.Bit._038_AUTH_ID_RSP, "0000");	
		msg.putField(Iso8583Post.Bit._039_RSP_CODE, Iso8583.RspCode._00_SUCCESSFUL);	
		respToTranmgr.putMsgToTranmgr((Iso8583Post) msg);
		return respToTranmgr;
	}
	
	/************************************************************************************
	 * This method is invoked by the Node Interface whenever a RESYNC command is
	 * received.
	 *
	 * @param interchange
	 * @return action a ejecutar con el comando
	 * @throws Exception
	 ************************************************************************************/
	@Override
	public Action processResyncCommand(AInterchangeDriverEnvironment interchange) throws Exception {
		Action action = new Action();
		try {
			keysHSM.clear();
			
			if (udpClient != null)
				udpClient.close();
			
			init(interchange);
		} catch (Exception e) {
			EventRecorder.recordEvent(e);
		}
		return action;
	}

}
