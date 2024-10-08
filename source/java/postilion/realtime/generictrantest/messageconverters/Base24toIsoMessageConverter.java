package postilion.realtime.generictrantest.messageconverters;


import java.util.Base64;
import java.util.Map;
import java.util.Objects;

import postilion.realtime.generictrantest.GenericInterfaceTranTest;
import postilion.realtime.generictrantest.crypto.Crypto;
import postilion.realtime.generictrantest.database.DBHandler;
import postilion.realtime.generictrantest.streamBase24ATM.Base24Atm;
import postilion.realtime.generictrantest.systemConstans.SystemConstants;
import postilion.realtime.generictrantest.transactionidentificator.TransactionIdentificator;
import postilion.realtime.generictrantest.udp.Client;
import postilion.realtime.generictrantest.util.Logger;
import postilion.realtime.generictrantest.util.Utils;
import postilion.realtime.sdk.crypto.CryptoCfgManager;
import postilion.realtime.sdk.crypto.CryptoManager;
import postilion.realtime.sdk.crypto.DesKwp;
import postilion.realtime.sdk.crypto.XCrypto;
import postilion.realtime.sdk.eventrecorder.EventRecorder;
import postilion.realtime.sdk.message.bitmap.Iso8583;
import postilion.realtime.sdk.message.bitmap.Iso8583Post;
import postilion.realtime.sdk.message.bitmap.ProcessingCode;
import postilion.realtime.sdk.message.bitmap.StructuredData;
import postilion.realtime.sdk.message.bitmap.XFieldUnableToConstruct;
import postilion.realtime.sdk.util.XPostilion;
import postilion.realtime.sdk.util.convert.Transform;

public class Base24toIsoMessageConverter extends Iso8583 {
	
	//Iso8583 convertMsgIso;
	public Base24toIsoMessageConverter() {	
		// TODO Auto-generated constructor stub
	}
	//copiando los campos deel mensaje de B24 A ISO
	//Agregar los campos al 127.22 del B24
	public Iso8583Post converToIso2 (Base24Atm p_msgB24 , Iso8583Post p_convertMsgIso) {
		StructuredData field_structure_w = new StructuredData();
		 try {
			p_convertMsgIso.putStructuredData(field_structure_w);
		} catch (XPostilion e) {
			// TODO Auto-generated catch block
			EventRecorder.recordEvent(e);
		}
		return p_convertMsgIso;
	}
	//public Base24Atm converToB24(Base24Atm p_msgB24, Iso8583Post p_MsgIso) {
	public Iso8583Post converB24ToIso8583Post (Base24Atm p_msgB24 , Iso8583Post p_convertMsgIso,String paramTransactionIdentificator, boolean enableLog, String process) {
		TransactionIdentificator transactionIdentificator= new TransactionIdentificator();
		
		StructuredData field_structure_w = new StructuredData();

		try {			
			Logger.logLine("B24msg: "+p_msgB24, enableLog);
			p_convertMsgIso.putMsgType(p_msgB24.getMsgType());
			ProcessingCode ps =  new ProcessingCode(p_msgB24.getField(Iso8583.Bit._003_PROCESSING_CODE));
			p_convertMsgIso.putField(Iso8583.Bit._003_PROCESSING_CODE, p_msgB24.getField(Iso8583.Bit._003_PROCESSING_CODE));
			p_convertMsgIso.putField(Iso8583.Bit._004_AMOUNT_TRANSACTION, p_msgB24.getField(Iso8583.Bit._004_AMOUNT_TRANSACTION));
			p_convertMsgIso.putField(Iso8583.Bit._007_TRANSMISSION_DATE_TIME,p_msgB24.getField(Iso8583.Bit._007_TRANSMISSION_DATE_TIME));
			p_convertMsgIso.putField(Iso8583.Bit._011_SYSTEMS_TRACE_AUDIT_NR, p_msgB24.getField(Iso8583.Bit._011_SYSTEMS_TRACE_AUDIT_NR));
			p_convertMsgIso.putField(Iso8583.Bit._012_TIME_LOCAL, p_msgB24.getField(Iso8583.Bit._012_TIME_LOCAL));	
			p_convertMsgIso.putField(Iso8583.Bit._013_DATE_LOCAL, p_msgB24.getField(Iso8583.Bit._013_DATE_LOCAL));
			if (p_msgB24.isFieldSet(Iso8583.Bit._015_DATE_SETTLE))
				p_convertMsgIso.putField(Iso8583.Bit._015_DATE_SETTLE,p_msgB24.getField(Iso8583.Bit._015_DATE_SETTLE));
			if (p_msgB24.isFieldSet(Iso8583.Bit._022_POS_ENTRY_MODE))
				p_convertMsgIso.putField(Iso8583.Bit._022_POS_ENTRY_MODE, p_msgB24.getField(Iso8583.Bit._022_POS_ENTRY_MODE));
			p_convertMsgIso.putField(Iso8583.Bit._032_ACQUIRING_INST_ID_CODE, p_msgB24.getField(Iso8583.Bit._032_ACQUIRING_INST_ID_CODE));
			p_convertMsgIso.putField(Iso8583.Bit._035_TRACK_2_DATA, p_msgB24.getField(Iso8583.Bit._035_TRACK_2_DATA));
			p_convertMsgIso.putField(Iso8583.Bit._037_RETRIEVAL_REF_NR,p_msgB24.getField(Iso8583.Bit._037_RETRIEVAL_REF_NR));
			if (p_msgB24.isFieldSet(Iso8583.Bit._042_CARD_ACCEPTOR_ID_CODE))
				p_convertMsgIso.putField(Iso8583.Bit._042_CARD_ACCEPTOR_ID_CODE, p_msgB24.getField(Iso8583.Bit._042_CARD_ACCEPTOR_ID_CODE));
			if (p_msgB24.isFieldSet(Iso8583.Bit._043_CARD_ACCEPTOR_NAME_LOC))
				p_convertMsgIso.putField(Iso8583.Bit._043_CARD_ACCEPTOR_NAME_LOC, p_msgB24.getField(Iso8583.Bit._043_CARD_ACCEPTOR_NAME_LOC));
			if (p_msgB24.isFieldSet(Iso8583.Bit._102_ACCOUNT_ID_1))
				p_convertMsgIso.putField(Iso8583.Bit._102_ACCOUNT_ID_1,p_msgB24.getField(Iso8583.Bit._102_ACCOUNT_ID_1));
			if (p_msgB24.isFieldSet(Iso8583.Bit._104_TRAN_DESCRIPTION))
				p_convertMsgIso.putField(Iso8583.Bit._104_TRAN_DESCRIPTION,p_msgB24.getField(Iso8583.Bit._104_TRAN_DESCRIPTION));
			if (p_msgB24.isFieldSet(Iso8583.Bit._103_ACCOUNT_ID_2))
			  p_convertMsgIso.putField(Iso8583.Bit._103_ACCOUNT_ID_2, p_msgB24.getField(Iso8583.Bit._103_ACCOUNT_ID_2));
			
			if (p_msgB24.isFieldSet(Iso8583.Bit._100_RECEIVING_INST_ID_CODE))
				p_convertMsgIso.putField(Iso8583.Bit._100_RECEIVING_INST_ID_CODE, p_msgB24.getField(Iso8583.Bit._100_RECEIVING_INST_ID_CODE));
			p_convertMsgIso.putField(Iso8583Post.Bit._123_POS_DATA_CODE, "911201513344002");
		
			if (p_msgB24.isFieldSet(Iso8583.Bit._025_POS_CONDITION_CODE)){
			   p_convertMsgIso.putField(Iso8583.Bit._025_POS_CONDITION_CODE, p_msgB24.getField(Iso8583.Bit._025_POS_CONDITION_CODE));
			}
			else {
				p_convertMsgIso.putField(Iso8583.Bit._025_POS_CONDITION_CODE,"00");
			}
			
			if (p_msgB24.isFieldSet(Iso8583.Bit._026_POS_PIN_CAPTURE_CODE)) {
		      p_convertMsgIso.putField(Iso8583.Bit._026_POS_PIN_CAPTURE_CODE, p_msgB24.getField(Iso8583.Bit._026_POS_PIN_CAPTURE_CODE));
			}else {
				p_convertMsgIso.putField(Iso8583.Bit._026_POS_PIN_CAPTURE_CODE, "04");
			}
					
			if (p_msgB24.isFieldSet(Iso8583.Bit._041_CARD_ACCEPTOR_TERM_ID)) {
				field_structure_w.put("KEY_B24_P041",p_msgB24.getField(Iso8583.Bit._041_CARD_ACCEPTOR_TERM_ID));								
			}else {
				field_structure_w.put("KEY_B24_P041","0000000000000");
			}
			p_convertMsgIso.putField(Iso8583.Bit._041_CARD_ACCEPTOR_TERM_ID, p_msgB24.getField(Iso8583.Bit._041_CARD_ACCEPTOR_TERM_ID).substring(8,16));
			
			//Cuando el campo no este presente definir na constante 
			if (p_convertMsgIso.isFieldSet(Iso8583.Bit._048_ADDITIONAL_DATA)) {
				p_convertMsgIso.putField(Iso8583.Bit._048_ADDITIONAL_DATA, p_msgB24.getField(Iso8583.Bit._048_ADDITIONAL_DATA));				
			}else{
				p_convertMsgIso.putField(Iso8583.Bit._048_ADDITIONAL_DATA,"00520000000030000000000000000000000000000000");
			}			
			if (p_convertMsgIso.isFieldSet(Iso8583.Bit._049_CURRENCY_CODE_TRAN)) {
				p_convertMsgIso.putField(Iso8583.Bit._049_CURRENCY_CODE_TRAN, p_msgB24.getField(Iso8583.Bit._049_CURRENCY_CODE_TRAN));				
			}else{
				p_convertMsgIso.putField(Iso8583.Bit._049_CURRENCY_CODE_TRAN, "170");
			}
			if (p_msgB24.isFieldSet(Iso8583.Bit._052_PIN_DATA)) {
				p_convertMsgIso.putField(Iso8583Post.Bit._052_PIN_DATA, Transform.fromHexToBin(p_msgB24.getField(Iso8583Post.Bit._052_PIN_DATA)));
				field_structure_w.put("KEY_B24_P052",p_msgB24.getField(Iso8583.Bit._052_PIN_DATA));
			}else {
				p_convertMsgIso.putField(Iso8583Post.Bit._052_PIN_DATA, Transform.fromHexToBin("FFFFFFFFFFFFFFFF"));
			}				
//			if (p_msgB24.isFieldSet(Iso8583Post.Bit._054_ADDITIONAL_AMOUNTS)) {
//			     p_convertMsgIso.putField(Iso8583Post.Bit._054_ADDITIONAL_AMOUNTS, p_msgB24.getField(Iso8583Post.Bit._054_ADDITIONAL_AMOUNTS));
//			}else {
//				p_convertMsgIso.putField(Iso8583Post.Bit._054_ADDITIONAL_AMOUNTS, ""
//						+ "");
//			}
			if (p_msgB24.isFieldSet(Iso8583Post.Bit._090_ORIGINAL_DATA_ELEMENTS)) {
			     p_convertMsgIso.putField(Iso8583Post.Bit._090_ORIGINAL_DATA_ELEMENTS, p_msgB24.getField(Iso8583Post.Bit._090_ORIGINAL_DATA_ELEMENTS));
			}else {
				p_convertMsgIso.putField(Iso8583Post.Bit._090_ORIGINAL_DATA_ELEMENTS, "000000000000000000000000000000000000000000");
			}
			
			if (p_msgB24.getMsgType() == (Iso8583.MsgType._0420_ACQUIRER_REV_ADV)) {
				p_convertMsgIso.putPrivField(Iso8583Post.PrivBit._011_ORIGINAL_KEY,p_msgB24.getField(Iso8583.Bit._090_ORIGINAL_DATA_ELEMENTS).substring(0,32));
			}
			Logger.logLine("p_convertMsgIso: "+p_convertMsgIso, enableLog);
			
			switch(paramTransactionIdentificator) {
			case "1":
				String msgKeyIdentificator = transactionIdentificator.getTransactionIdentification(p_msgB24);
				Logger.logLine("msgKeyIdentificator: "+msgKeyIdentificator, enableLog);
				//LLave Validacion Tipo de transaccion
				field_structure_w.put("KEY_TRANSACTION_IDENTIFICATION", msgKeyIdentificator);
				//Descripcion Validacion Tipo de transaccion
				field_structure_w.put("TRANSACTION_DESCRIPTION", Objects.isNull(GenericInterfaceTranTest.infoTrasactionIdentidicator.get(msgKeyIdentificator).toString()) ? "Informaci�n no encontrada" : GenericInterfaceTranTest.infoTrasactionIdentidicator.get(msgKeyIdentificator).toString() );
				break;
			case "2":
				Map<String,Object> msgValidation = transactionIdentificator.getTransactionValidation(p_msgB24);
				Logger.logLine("msgKeyIdentificator: "+msgValidation, enableLog);
				msgValidation.forEach((k,v) -> {
					Logger.logLine("LlavesHSM key: " + k + " with value: " + v, enableLog);
				});
				//Descripcion Validacion Datos de entrada
				for( String key : msgValidation.keySet()) {
					field_structure_w.put(key, msgValidation.get(key).toString());
				}
			}
			
			
			
			DBHandler.getClientData(ps.toString(), p_msgB24.getTrack2Data().getPan(), ps.getFromAccount(), p_msgB24.getTrack2Data().getExpiryDate(),
					"0000000000000000", field_structure_w);			
			
			
			if(process.equals("TRANSLATE") || 
					(process.equals("FIELD41") && p_msgB24.getField(Iso8583Post.Bit._041_CARD_ACCEPTOR_TERM_ID).substring(13, 14).equals("1"))) {
				field_structure_w.put("B24_Field_52", translatePin(p_msgB24,field_structure_w,enableLog));
			}

		  p_convertMsgIso.putStructuredData(field_structure_w);			
		} catch (XFieldUnableToConstruct e) {
			// TODO Auto-generated catch block
			EventRecorder.recordEvent(e);
		} catch (XPostilion e) {
			// TODO Auto-generated catch block
			EventRecorder.recordEvent(e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			EventRecorder.recordEvent(e);
		}			
		
		return p_convertMsgIso;
	}
	
	
	//public Base24Atm converToB24(Base24Atm p_msgB24, Iso8583Post p_MsgIso) {
		public Iso8583Post converIsoToIso8583Post (Iso8583 p_msgIso , Iso8583Post p_convertMsgIso, boolean enableLog) {
			
			StructuredData field_structure_w = new StructuredData();

			try {			
				Logger.logLine("p_msgIso: "+p_msgIso, enableLog);
				p_convertMsgIso.putMsgType(p_msgIso.getMsgType());
				
				p_convertMsgIso.putField(Iso8583.Bit._003_PROCESSING_CODE, p_msgIso.getField(Iso8583.Bit._003_PROCESSING_CODE));
				p_convertMsgIso.putField(Iso8583.Bit._004_AMOUNT_TRANSACTION, p_msgIso.getField(Iso8583.Bit._004_AMOUNT_TRANSACTION));
				p_convertMsgIso.putField(Iso8583.Bit._007_TRANSMISSION_DATE_TIME,p_msgIso.getField(Iso8583.Bit._007_TRANSMISSION_DATE_TIME));
				p_convertMsgIso.putField(Iso8583.Bit._011_SYSTEMS_TRACE_AUDIT_NR, p_msgIso.getField(Iso8583.Bit._011_SYSTEMS_TRACE_AUDIT_NR));
				p_convertMsgIso.putField(Iso8583.Bit._012_TIME_LOCAL, p_msgIso.getField(Iso8583.Bit._012_TIME_LOCAL));	
				p_convertMsgIso.putField(Iso8583.Bit._013_DATE_LOCAL, p_msgIso.getField(Iso8583.Bit._013_DATE_LOCAL));
				if (p_msgIso.isFieldSet(Iso8583.Bit._015_DATE_SETTLE))
					p_convertMsgIso.putField(Iso8583.Bit._015_DATE_SETTLE,p_msgIso.getField(Iso8583.Bit._015_DATE_SETTLE));
				if (p_msgIso.isFieldSet(Iso8583.Bit._022_POS_ENTRY_MODE))
					p_convertMsgIso.putField(Iso8583.Bit._022_POS_ENTRY_MODE, p_msgIso.getField(Iso8583.Bit._022_POS_ENTRY_MODE));
				p_convertMsgIso.putField(Iso8583.Bit._032_ACQUIRING_INST_ID_CODE, p_msgIso.getField(Iso8583.Bit._032_ACQUIRING_INST_ID_CODE));
				p_convertMsgIso.putField(Iso8583.Bit._035_TRACK_2_DATA, p_msgIso.getField(Iso8583.Bit._035_TRACK_2_DATA));
				p_convertMsgIso.putField(Iso8583.Bit._037_RETRIEVAL_REF_NR,p_msgIso.getField(Iso8583.Bit._037_RETRIEVAL_REF_NR));
				if (p_msgIso.isFieldSet(Iso8583.Bit._042_CARD_ACCEPTOR_ID_CODE))
					p_convertMsgIso.putField(Iso8583.Bit._042_CARD_ACCEPTOR_ID_CODE, p_msgIso.getField(Iso8583.Bit._042_CARD_ACCEPTOR_ID_CODE));
				if (p_msgIso.isFieldSet(Iso8583.Bit._043_CARD_ACCEPTOR_NAME_LOC))
					p_convertMsgIso.putField(Iso8583.Bit._043_CARD_ACCEPTOR_NAME_LOC, p_msgIso.getField(Iso8583.Bit._043_CARD_ACCEPTOR_NAME_LOC));
				if (p_msgIso.isFieldSet(Iso8583.Bit._102_ACCOUNT_ID_1))
					p_convertMsgIso.putField(Iso8583.Bit._102_ACCOUNT_ID_1,p_msgIso.getField(Iso8583.Bit._102_ACCOUNT_ID_1));
				if (p_msgIso.isFieldSet(Iso8583.Bit._104_TRAN_DESCRIPTION))
					p_convertMsgIso.putField(Iso8583.Bit._104_TRAN_DESCRIPTION,p_msgIso.getField(Iso8583.Bit._104_TRAN_DESCRIPTION));
				if (p_msgIso.isFieldSet(Iso8583.Bit._103_ACCOUNT_ID_2))
				  p_convertMsgIso.putField(Iso8583.Bit._103_ACCOUNT_ID_2, p_msgIso.getField(Iso8583.Bit._103_ACCOUNT_ID_2));
				
				if (p_msgIso.isFieldSet(Iso8583.Bit._100_RECEIVING_INST_ID_CODE))
					p_convertMsgIso.putField(Iso8583.Bit._100_RECEIVING_INST_ID_CODE, p_msgIso.getField(Iso8583.Bit._100_RECEIVING_INST_ID_CODE));
				p_convertMsgIso.putField(Iso8583Post.Bit._123_POS_DATA_CODE, "911201513344002");
			
				if (p_msgIso.isFieldSet(Iso8583.Bit._025_POS_CONDITION_CODE)){
				   p_convertMsgIso.putField(Iso8583.Bit._025_POS_CONDITION_CODE, p_msgIso.getField(Iso8583.Bit._025_POS_CONDITION_CODE));
				}
				else {
					p_convertMsgIso.putField(Iso8583.Bit._025_POS_CONDITION_CODE,"00");
				}
				
				if (p_msgIso.isFieldSet(Iso8583.Bit._026_POS_PIN_CAPTURE_CODE)) {
			      p_convertMsgIso.putField(Iso8583.Bit._026_POS_PIN_CAPTURE_CODE, p_msgIso.getField(Iso8583.Bit._026_POS_PIN_CAPTURE_CODE));
				}else {
					p_convertMsgIso.putField(Iso8583.Bit._026_POS_PIN_CAPTURE_CODE, "04");
				}
						
				if (p_msgIso.isFieldSet(Iso8583.Bit._041_CARD_ACCEPTOR_TERM_ID)) {
					field_structure_w.put("KEY_B24_P041",p_msgIso.getField(Iso8583.Bit._041_CARD_ACCEPTOR_TERM_ID));								
				}else {
					field_structure_w.put("KEY_B24_P041","0000000000000");
				}
				p_convertMsgIso.putField(Iso8583.Bit._041_CARD_ACCEPTOR_TERM_ID, p_msgIso.getField(Iso8583.Bit._041_CARD_ACCEPTOR_TERM_ID));
				
				//Cuando el campo no este presente definir na constante 
				if (p_convertMsgIso.isFieldSet(Iso8583.Bit._048_ADDITIONAL_DATA)) {
					p_convertMsgIso.putField(Iso8583.Bit._048_ADDITIONAL_DATA, p_msgIso.getField(Iso8583.Bit._048_ADDITIONAL_DATA));				
				}else{
					p_convertMsgIso.putField(Iso8583.Bit._048_ADDITIONAL_DATA,"00520000000030000000000000000000000000000000");
				}			
				if (p_convertMsgIso.isFieldSet(Iso8583.Bit._049_CURRENCY_CODE_TRAN)) {
					p_convertMsgIso.putField(Iso8583.Bit._049_CURRENCY_CODE_TRAN, p_msgIso.getField(Iso8583.Bit._049_CURRENCY_CODE_TRAN));				
				}else{
					p_convertMsgIso.putField(Iso8583.Bit._049_CURRENCY_CODE_TRAN, "170");
				}
				if (p_msgIso.isFieldSet(Iso8583.Bit._052_PIN_DATA)) {
					p_convertMsgIso.putField(Iso8583Post.Bit._052_PIN_DATA, Transform.fromHexToBin(p_msgIso.getField(Iso8583Post.Bit._052_PIN_DATA)));
					field_structure_w.put("KEY_B24_P052",p_msgIso.getField(Iso8583.Bit._052_PIN_DATA));
				}else {
					p_convertMsgIso.putField(Iso8583Post.Bit._052_PIN_DATA, Transform.fromHexToBin("FFFFFFFFFFFFFFFF"));
				}				
//				if (p_msgB24.isFieldSet(Iso8583Post.Bit._054_ADDITIONAL_AMOUNTS)) {
//				     p_convertMsgIso.putField(Iso8583Post.Bit._054_ADDITIONAL_AMOUNTS, p_msgB24.getField(Iso8583Post.Bit._054_ADDITIONAL_AMOUNTS));
//				}else {
//					p_convertMsgIso.putField(Iso8583Post.Bit._054_ADDITIONAL_AMOUNTS, ""
//							+ "");
//				}
				if (p_msgIso.isFieldSet(Iso8583Post.Bit._090_ORIGINAL_DATA_ELEMENTS)) {
				     p_convertMsgIso.putField(Iso8583Post.Bit._090_ORIGINAL_DATA_ELEMENTS, p_msgIso.getField(Iso8583Post.Bit._090_ORIGINAL_DATA_ELEMENTS));
				}else {
					p_convertMsgIso.putField(Iso8583Post.Bit._090_ORIGINAL_DATA_ELEMENTS, "000000000000000000000000000000000000000000");
				}
				
				if (p_msgIso.getMsgType() == (Iso8583.MsgType._0420_ACQUIRER_REV_ADV)) {
					p_convertMsgIso.putPrivField(Iso8583Post.PrivBit._011_ORIGINAL_KEY,p_msgIso.getField(Iso8583.Bit._090_ORIGINAL_DATA_ELEMENTS).substring(0,32));
				}
				Logger.logLine("p_convertMsgIso: "+p_convertMsgIso, enableLog);
				
				
				
//				DBHandler.getClientData(ps.toString(), p_msgIso.getTrack2Data().getPan(), ps.getFromAccount(), p_msgIso.getTrack2Data().getExpiryDate(),
//						"0000000000000000", field_structure_w);
//				
//				
//				pinValidation(p_msgIso,field_structure_w,enableLog);
				
			  p_convertMsgIso.putStructuredData(field_structure_w);			
			} catch (XFieldUnableToConstruct e) {
				// TODO Auto-generated catch block
				EventRecorder.recordEvent(e);
			} catch (XPostilion e) {
				// TODO Auto-generated catch block
				EventRecorder.recordEvent(e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				EventRecorder.recordEvent(e);
			}			
			
			return p_convertMsgIso;
		}
		
		//public Base24Atm converToB24(Base24Atm p_msgB24, Iso8583Post p_MsgIso) {
		public Iso8583 processMsgB24ValidatePin (Iso8583 p_msg, boolean enableLog, Iso8583Post msgToTM, String process) {

			
			StructuredData field_structure_w = new StructuredData();

			try {			
				ProcessingCode ps =  new ProcessingCode(p_msg.getField(Iso8583.Bit._003_PROCESSING_CODE));	
				boolean result = false;
				p_msg.putMsgType(Iso8583.MsgType._0210_TRAN_REQ_RSP);
				String p37 = p_msg.isFieldSet(Iso8583.Bit._037_RETRIEVAL_REF_NR) ? p_msg.getField(Iso8583.Bit._037_RETRIEVAL_REF_NR) : "000000000000";
				
				GenericInterfaceTranTest.udpClient.sendData(Client.formatDatatoSend("===INICIO PROCES VALIDACION PIN===", p37, SystemConstants.PROCESS_VALIDATE_PIN));
				
				DBHandler.getClientData(ps.toString(), p_msg.getTrack2Data().getPan(), ps.getFromAccount(), p_msg.getTrack2Data().getExpiryDate(),
						"0000000000000000", field_structure_w);			
				
				Logger.logLine(field_structure_w.toMsgString(), enableLog);
				
				if(field_structure_w.get("HOLDRSPCODE") == null && field_structure_w.get("ERROR") == null) {
					result = pinValidation(p_msg,field_structure_w,enableLog);
				
					if(result) {
						p_msg.putField(Iso8583.Bit._039_RSP_CODE, SystemConstants.TWO_ZEROS);
					}else {
						p_msg.putField(Iso8583.Bit._039_RSP_CODE, "55");
					}
				} else {
					if(field_structure_w.get("ERROR") != null && field_structure_w.get("ERROR_CODE") != null)
						p_msg.putField(Iso8583.Bit._039_RSP_CODE, field_structure_w.get("ERROR_CODE"));
					else if (field_structure_w.get("HOLDRSPCODE") != null)
						p_msg.putField(Iso8583.Bit._039_RSP_CODE, field_structure_w.get("HOLDRSPCODE"));
					else
						p_msg.putField(Iso8583.Bit._039_RSP_CODE, "05");
				}
				
				constructMsgToTm(p_msg, msgToTM, field_structure_w);
				
				GenericInterfaceTranTest.udpClient.sendData(Client.formatDatatoSend("===FIN PROCES VALIDACION PIN===", p37, SystemConstants.PROCESS_VALIDATE_PIN));
		
			} catch (XFieldUnableToConstruct e) {
				// TODO Auto-generated catch block
				EventRecorder.recordEvent(e);
			} catch (XPostilion e) {
				// TODO Auto-generated catch block
				EventRecorder.recordEvent(e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				EventRecorder.recordEvent(e);
			}			
			
			return p_msg;
		}
		
		public Iso8583 processMsgB24ValidatePinCvv (Iso8583 p_msg, boolean enableLog, Iso8583Post msgToTM, String process) {

			
			StructuredData field_structure_w = new StructuredData();

			try {		
				Crypto crypto = new Crypto(enableLog);
				ProcessingCode ps =  new ProcessingCode(p_msg.getField(Iso8583.Bit._003_PROCESSING_CODE));	
				
				
				DBHandler.getClientData(ps.toString(), p_msg.getTrack2Data().getPan(), ps.getFromAccount(), p_msg.getTrack2Data().getExpiryDate(),
						"0000000000000000", field_structure_w);			
				
				boolean resultPin = false;
				p_msg.putMsgType(Iso8583.MsgType._0210_TRAN_REQ_RSP);
						
						
				if(field_structure_w.get("HOLDRSPCODE") == null && field_structure_w.get("ERROR") == null) {
					resultPin = pinValidation(p_msg,field_structure_w,enableLog);
					
					if(resultPin) {
						
						boolean resultCvv = crypto.validateCvv(p_msg, enableLog);
						
						if(resultCvv) {
							p_msg.putField(Iso8583.Bit._039_RSP_CODE, SystemConstants.TWO_ZEROS);
						} else {
							p_msg.putField(Iso8583.Bit._039_RSP_CODE, "98");
						}
						
					}else {
						p_msg.putField(Iso8583.Bit._039_RSP_CODE, "55");
					}
				} else {
					if(field_structure_w.get("ERROR") != null && field_structure_w.get("ERROR_CODE") != null)
						p_msg.putField(Iso8583.Bit._039_RSP_CODE, field_structure_w.get("ERROR_CODE"));
					else if (field_structure_w.get("HOLDRSPCODE") != null)
						p_msg.putField(Iso8583.Bit._039_RSP_CODE, field_structure_w.get("HOLDRSPCODE"));
					else
						p_msg.putField(Iso8583.Bit._039_RSP_CODE, "05");
				}
				field_structure_w.put("PROCESS_2", "CVV");
				field_structure_w.put("PROCESS", "VALIDATION_PIN");
				
				constructMsgToTm(p_msg, msgToTM, field_structure_w);
				
				
		
			} catch (XFieldUnableToConstruct e) {
				// TODO Auto-generated catch block
				EventRecorder.recordEvent(e);
			} catch (XPostilion e) {
				// TODO Auto-generated catch block
				EventRecorder.recordEvent(e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				EventRecorder.recordEvent(e);
			}			
			
			return p_msg;
		}
		
		public Iso8583 processMsgB24ValidateCvv (Iso8583 p_msg, boolean enableLog, Iso8583Post msgToTM, String process) {

			try {		
				Crypto crypto = new Crypto(enableLog);
				StructuredData field_structure_w = new StructuredData();
				field_structure_w.put("PROCESS", "CVV");

				boolean resultCvv = crypto.validateCvv(p_msg, enableLog);
				p_msg.putMsgType(Iso8583.MsgType._0210_TRAN_REQ_RSP);
				
				if(resultCvv) {
					p_msg.putField(Iso8583.Bit._039_RSP_CODE, SystemConstants.TWO_ZEROS);
				} else {
					p_msg.putField(Iso8583.Bit._039_RSP_CODE, "98");
				}
				
				constructMsgToTm(p_msg, msgToTM, field_structure_w);
		
			} catch (XFieldUnableToConstruct e) {
				// TODO Auto-generated catch block
				EventRecorder.recordEvent(e);
			} catch (XPostilion e) {
				// TODO Auto-generated catch block
				EventRecorder.recordEvent(e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				EventRecorder.recordEvent(e);
			}			
			
			return p_msg;
		}
		
		public boolean pinValidation(Iso8583 p_msgIso, StructuredData field_structure_w, boolean enableLog) {
			boolean validPin = false;
			try {
				Crypto crypto = new Crypto(enableLog);
				String channel = null;
				switch (p_msgIso.getField(Iso8583.Bit._041_CARD_ACCEPTOR_TERM_ID).substring(13, 14)) {
				case "1":
					switch (p_msgIso.getField(Iso8583.Bit._041_CARD_ACCEPTOR_TERM_ID).substring(15, 16)) {
					case "1":
						channel = "BM";
						break;
					case "2":
						channel = "PB";
						break;
					case "3":
						channel = "IV";
						break;
					case "4":
						channel = "CP";
						break;
					case "5":
						channel = "SM";
						break;
					case "6":
						channel = "DG";
						break;

					default:
						break;
					}
					break;
				default:
					channel = "GN";
					break;
				}
				
				
				String keyInfo[] = GenericInterfaceTranTest.keysHSM.get(channel).split("_");
				String keyName = keyInfo != null ? keyInfo[0] : "NOT FOUND";
				String p37 = p_msgIso.isFieldSet(Iso8583.Bit._037_RETRIEVAL_REF_NR) ? p_msgIso.getField(Iso8583.Bit._037_RETRIEVAL_REF_NR) : "000000000000";
				String pinBlock = p_msgIso.getField(Iso8583.Bit._052_PIN_DATA);
				String pinBlockConverted = channel.equals("GN") ? p_msgIso.getField(Iso8583.Bit._052_PIN_DATA) : null;
				String pan = p_msgIso.getTrack2Data().getPan();
				String pinOffset = field_structure_w.get("PINOFFSET");
				String idDoc = p_msgIso.getField(Iso8583.Bit._104_TRAN_DESCRIPTION).substring(0,2) + p_msgIso.getField(Iso8583.Bit._104_TRAN_DESCRIPTION).substring(p_msgIso.getField(Iso8583.Bit._104_TRAN_DESCRIPTION).length()-10);
				
				GenericInterfaceTranTest.udpClient.sendData(Client.formatDatatoSend("keyName:" + keyName, p37, SystemConstants.PROCESS_VALIDATE_PIN));
				GenericInterfaceTranTest.udpClient.sendData(Client.formatDatatoSend("pinBlock:" + pinBlock, p37, SystemConstants.PROCESS_VALIDATE_PIN));
				GenericInterfaceTranTest.udpClient.sendData(Client.formatDatatoSend("pan:" + pan, p37, SystemConstants.PROCESS_VALIDATE_PIN));
				GenericInterfaceTranTest.udpClient.sendData(Client.formatDatatoSend("pinOffset:" + pinOffset, p37, SystemConstants.PROCESS_VALIDATE_PIN));
				Logger.logLine("keyName:" + keyName, enableLog);
				Logger.logLine("pinBlock:" + pinBlock, enableLog);
				Logger.logLine("pan:" + pan, enableLog);
				Logger.logLine("pinOffset:" + pinOffset, enableLog);
				
				CryptoCfgManager crypcfgman = CryptoManager.getStaticConfiguration();
				DesKwp kwpChannel = crypcfgman.getKwp(keyName);
				DesKwp kwpPinKey = crypcfgman.getKwp("HKPINKEY");
				GenericInterfaceTranTest.udpClient.sendData(Client.formatDatatoSend("kwpChannel:" + kwpChannel.getName(), p37, SystemConstants.PROCESS_VALIDATE_PIN));
				GenericInterfaceTranTest.udpClient.sendData(Client.formatDatatoSend("kwpPinKey:" + kwpPinKey.getName(), p37, SystemConstants.PROCESS_VALIDATE_PIN));
				Logger.logLine("kwpChannel:" + kwpChannel.getName(), enableLog);
				Logger.logLine("kwpPinKey:" + kwpPinKey.getName(), enableLog);
				Logger.logLine("kwpChannel.getValueUnderKsk():" + kwpChannel.getValueUnderKsk(), enableLog);
				Logger.logLine("kwpPinKey.getValueUnderKsk():" + kwpPinKey.getValueUnderKsk(), enableLog);
			
				if(!channel.equals("GN"))
					pinBlockConverted = crypto.convertPin(pinBlock, kwpChannel.getValueUnderKsk(), idDoc, p37, enableLog);
				
				validPin = crypto.validatePin(pinBlockConverted, kwpChannel.getValueUnderKsk(), pinOffset, pan, kwpPinKey.getValueUnderKsk(), p37, enableLog);
				field_structure_w.put("PINVALID", validPin ? "TRUE" : "FALSE");
				field_structure_w.put("PROCESS", "VALIDATION_PIN");
				
				GenericInterfaceTranTest.udpClient.sendData(Client.formatDatatoSend("validPin:" + validPin, p37, SystemConstants.PROCESS_VALIDATE_PIN));
				Logger.logLine("validPin:" + validPin, enableLog);
			} catch (XCrypto e) {
				field_structure_w.put("ERROR", "ERROR CRIPTOGRAFIA");
				Logger.logLine("KWP ERROR: " + e.toString(), enableLog);
				EventRecorder.recordEvent(new Exception(e.toString()));
			}
			catch (Exception e) {
				field_structure_w.put("ERROR", "ERROR CRIPTOGRAFIA");
				Logger.logLine("KWP ERROR: " + e.toString(), enableLog);
				EventRecorder.recordEvent(new Exception(e.toString()));
			}
			return validPin;
			
		}
		
		public String translatePin(Iso8583 p_msgIso, StructuredData field_structure_w, boolean enableLog) {
			String pinBlockConverted = "FFFFFFFFFFFFFFFF";
			String translatedPin = "FFFFFFFFFFFFFFFF";
			
			try {
				Crypto crypto = new Crypto(enableLog);
				
				
				String keyInfo[] = GenericInterfaceTranTest.keysHSM.get(p_msgIso.getField(Iso8583.Bit._041_CARD_ACCEPTOR_TERM_ID).substring(11, 13)).split("_");
				String keyName = keyInfo != null ? keyInfo[0] : "NOT FOUND";
				String p37 = p_msgIso.isFieldSet(Iso8583.Bit._037_RETRIEVAL_REF_NR) ? p_msgIso.getField(Iso8583.Bit._037_RETRIEVAL_REF_NR) : "000000000000";
				String pinBlock = p_msgIso.getField(Iso8583.Bit._052_PIN_DATA);
				String pan = p_msgIso.getTrack2Data().getPan();
				String idDoc = p_msgIso.getField(Iso8583.Bit._104_TRAN_DESCRIPTION).substring(0,2) + p_msgIso.getField(Iso8583.Bit._104_TRAN_DESCRIPTION).substring(p_msgIso.getField(Iso8583.Bit._104_TRAN_DESCRIPTION).length()-10);
				Logger.logLine("keyName:" + keyName, enableLog);
				Logger.logLine("pinBlock:" + pinBlock, enableLog);
				Logger.logLine("pan:" + pan, enableLog);
				
				CryptoCfgManager crypcfgman = CryptoManager.getStaticConfiguration();
				DesKwp kwpAth = crypcfgman.getKwp("ATH_KPE");
				DesKwp kwpChannel = crypcfgman.getKwp(keyName);
				Logger.logLine("kwpChannel:" + kwpChannel.getName(), enableLog);
				Logger.logLine("kwpChannel.getValueUnderKsk():" + kwpChannel.getValueUnderKsk(), enableLog);
				
				translatedPin = crypto.translatePin(kwpChannel.getValueUnderKsk(), kwpAth.getValueUnderKsk(), pinBlock, idDoc, enableLog);
				pinBlockConverted = crypto.convertPin(translatedPin, kwpChannel.getValueUnderKsk(), idDoc, p37, enableLog);
				
				Logger.logLine("newPin:" + pinBlockConverted, enableLog);
			} catch (XCrypto e) {
				field_structure_w.put("ERROR", "ERROR CRIPTOGRAFIA");
				Logger.logLine("KWP ERROR: " + e.toString(), enableLog);
				EventRecorder.recordEvent(new Exception(e.toString()));
			}
			catch (Exception e) {
				field_structure_w.put("ERROR", "ERROR CRIPTOGRAFIA");
				Logger.logLine("KWP ERROR: " + e.toString(), enableLog);
				EventRecorder.recordEvent(new Exception(e.toString()));
			}
			return pinBlockConverted;
			
		}
		
		
		public Iso8583 changePin(Iso8583 p_msg, Iso8583Post msgToTM, boolean enableLog, String process) {

			
			StructuredData field_structure_w = new StructuredData();
			try {		
				
				ProcessingCode ps =  new ProcessingCode(p_msg.getField(Iso8583.Bit._003_PROCESSING_CODE));	
				DBHandler.getClientData(ps.toString(), p_msg.getTrack2Data().getPan(), ps.getFromAccount(), p_msg.getTrack2Data().getExpiryDate(),
						"0000000000000000", field_structure_w);			
				
				boolean resultChangePin = false;
				p_msg.putMsgType(Iso8583.MsgType._0210_TRAN_REQ_RSP);
				
				if(field_structure_w.get("HOLDRSPCODE") == null && field_structure_w.get("ERROR") == null) {
					resultChangePin = processChangePin(p_msg, msgToTM, field_structure_w, enableLog);
				
					if(resultChangePin) {
						p_msg.putField(Iso8583.Bit._039_RSP_CODE, SystemConstants.TWO_ZEROS);
					}else {
						p_msg.putField(Iso8583.Bit._039_RSP_CODE, "55");
					}
				} else {
					if(field_structure_w.get("ERROR") != null && field_structure_w.get("ERROR_CODE") != null)
						p_msg.putField(Iso8583.Bit._039_RSP_CODE, field_structure_w.get("ERROR_CODE"));
					else if (field_structure_w.get("HOLDRSPCODE") != null)
						p_msg.putField(Iso8583.Bit._039_RSP_CODE, field_structure_w.get("HOLDRSPCODE"));
					else
						p_msg.putField(Iso8583.Bit._039_RSP_CODE, "05");
				}
				
				constructMsgToTm(p_msg, msgToTM, field_structure_w);
				
		
			} catch (XFieldUnableToConstruct e) {
				// TODO Auto-generated catch block
				EventRecorder.recordEvent(e);
			} catch (XPostilion e) {
				// TODO Auto-generated catch block
				EventRecorder.recordEvent(e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				EventRecorder.recordEvent(e);
			}			
			
			return p_msg;
		}
		
		public boolean processChangePin(Iso8583 p_msgIso, Iso8583Post msgToTM, StructuredData field_structure_w, boolean enableLog) {
			String changedPin = null;
			boolean result = false;
			try {
				Crypto crypto = new Crypto(enableLog);
				String channel = null;
				switch (p_msgIso.getField(Iso8583.Bit._041_CARD_ACCEPTOR_TERM_ID).substring(15, 16)) {
				case "1":
					channel = "BM";
					break;
				case "2":
					channel = "PB";
					break;
				case "3":
					channel = "IV";
					break;
				case "4":
					channel = "CP";
					break;
				case "5":
					channel = "SM";
					break;
				case "6":
					channel = "DG";
					break;

				default:
					break;
				}
				
				String keyInfo[] = GenericInterfaceTranTest.keysHSM.get(channel).split("_");
				String keyName = keyInfo != null ? keyInfo[0] : "NOT FOUND";
				
				
				
				String oldPinBlock = p_msgIso.getField(Iso8583.Bit._052_PIN_DATA);
				String oldPinBlockConverted = null;
				String newPinBlock = p_msgIso.getField(Iso8583.Bit._053_SECURITY_INFO);
				String newPinBlockConverted = null;
				String pan = p_msgIso.getTrack2Data().getPan();
				String p37 = p_msgIso.isFieldSet(Iso8583.Bit._037_RETRIEVAL_REF_NR) ? p_msgIso.getField(Iso8583.Bit._037_RETRIEVAL_REF_NR) : "000000000000";
				String pinOffset = field_structure_w.get("PINOFFSET") != null ? field_structure_w.get("PINOFFSET") : "0000";
				String issuer = field_structure_w.get("ISSUER") != null ? field_structure_w.get("ISSUER") : "1";
				String idDoc = p_msgIso.getField(Iso8583.Bit._104_TRAN_DESCRIPTION).substring(0,2) + p_msgIso.getField(Iso8583.Bit._104_TRAN_DESCRIPTION).substring(p_msgIso.getField(Iso8583.Bit._104_TRAN_DESCRIPTION).length()-10);
				Logger.logLine("keyName:" + keyName, enableLog);
				Logger.logLine("pinBlock:" + oldPinBlock, enableLog);
				Logger.logLine("pinBlock:" + newPinBlock, enableLog);
				Logger.logLine("pan:" + pan, enableLog);
				Logger.logLine("pinOffset:" + pinOffset, enableLog);
				
				CryptoCfgManager crypcfgman = CryptoManager.getStaticConfiguration();
				DesKwp kwpChannel = crypcfgman.getKwp(keyName);
				DesKwp kwpPinKey = crypcfgman.getKwp("HKPINKEY");
				Logger.logLine("kwpChannel:" + kwpChannel.getName(), enableLog);
				Logger.logLine("kwpPinKey:" + kwpPinKey.getName(), enableLog);
				Logger.logLine("kwpChannel.getValueUnderKsk():" + kwpChannel.getValueUnderKsk(), enableLog);
				Logger.logLine("kwpPinKey.getValueUnderKsk():" + kwpPinKey.getValueUnderKsk(), enableLog);
			
				oldPinBlockConverted = crypto.convertPin(oldPinBlock, kwpChannel.getValueUnderKsk(), idDoc, p37, enableLog);
				
				newPinBlockConverted = crypto.convertPin(newPinBlock, kwpChannel.getValueUnderKsk(), idDoc, p37, enableLog);
				
				changedPin = crypto.changePIN(oldPinBlockConverted, kwpChannel.getValueUnderKsk(), pinOffset, pan, kwpPinKey.getValueUnderKsk(), newPinBlockConverted, p37, enableLog);
				field_structure_w.put("CHANGEPIN", changedPin);
				field_structure_w.put("PROCESS", "CHANGE_PIN");
				
				if(!changedPin.equals("FFFF")) {
					result = DBHandler.updateOffset(issuer, changedPin, pan, p_msgIso.getTrack2Data().getExpiryDate(), enableLog);
				}
					
				
				
				Logger.logLine("newPin:" + changedPin, enableLog);
			} catch (XCrypto e) {
				field_structure_w.put("ERROR", "ERROR CRIPTOGRAFIA");
				Logger.logLine("KWP ERROR: " + e.toString(), enableLog);
				EventRecorder.recordEvent(new Exception(e.toString()));
			}
			catch (Exception e) {
				field_structure_w.put("ERROR", "ERROR");
				Logger.logLine("ERROR: " + e.toString(), enableLog);
				EventRecorder.recordEvent(new Exception(e.toString()));
			}
			return result;
			
		}
		
		
		public Iso8583 processMsgB24EncryptData(Iso8583 p_msg, boolean enableLog, Iso8583Post msgToTM, String process) {

			
			StructuredData field_structure_w = new StructuredData();

			try {
				String p37 = p_msg.isFieldSet(Iso8583.Bit._037_RETRIEVAL_REF_NR) ? p_msg.getField(Iso8583.Bit._037_RETRIEVAL_REF_NR) : "000000000000";
				GenericInterfaceTranTest.udpClient.sendData(Client.formatDatatoSend("==INICIO PROCESO ENCRIPCION DATA==", p37, SystemConstants.PROCESS_ENCRYPT_DATA));
				ProcessingCode ps =  new ProcessingCode(p_msg.getField(Iso8583.Bit._003_PROCESSING_CODE));	
				CryptoCfgManager crypcfgman = CryptoManager.getStaticConfiguration();
				DesKwp kwpEncryptKey = crypcfgman.getKwp("HKSDAKEY");
				
				GenericInterfaceTranTest.udpClient.sendData(Client.formatDatatoSend("kwpChannel:" + kwpEncryptKey.getName(), p37, SystemConstants.PROCESS_ENCRYPT_DATA));
				Logger.logLine("kwpEncryptKey:" + kwpEncryptKey.getName(), enableLog);
				Logger.logLine("kwpEncryptKey.getValueUnderKsk():" + kwpEncryptKey.getValueUnderKsk(), enableLog);
				Crypto crypto = new Crypto(enableLog);
				String encryptPan = "";
				String encryptExpDate = "";
				p_msg.putMsgType(Iso8583.MsgType._0210_TRAN_REQ_RSP);
				
				if(p_msg.getField(Iso8583.Bit._041_CARD_ACCEPTOR_TERM_ID).substring(12,13).equals("C")) {
					encryptPan = crypto.encryptPanData(p_msg.getTrack2Data().getPan(), kwpEncryptKey.getValueUnderKsk(), p37, enableLog);
					encryptExpDate = crypto.encryptExpDateData(Utils.constructExpDateForCommand(p_msg.getTrack2Data().getExpiryDate()), kwpEncryptKey.getValueUnderKsk(), p37, enableLog);
				} else {
					DBHandler.getClientData(ps.toString(), p_msg.getTrack2Data().getPan(), ps.getFromAccount(), p_msg.getTrack2Data().getExpiryDate(),
							"0000000000000000", field_structure_w);			
					
					Logger.logLine(field_structure_w.toMsgString(), enableLog);
					String docIn = p_msg.isFieldSet(Iso8583.Bit._104_TRAN_DESCRIPTION)
							? p_msg.getField(Iso8583.Bit._104_TRAN_DESCRIPTION).substring(p_msg.getField(Iso8583.Bit._104_TRAN_DESCRIPTION).length()-10) : "0000000000";
					String docBd = field_structure_w != null && field_structure_w.get("CUSTOMER_ID") != null 
							? field_structure_w.get("CUSTOMER_ID").substring(field_structure_w.get("CUSTOMER_ID").length()-10) : "NOT FOUND";
					
					Logger.logLine("docIn: "+docIn, enableLog);
					Logger.logLine("docBd: "+docBd, enableLog);
					
					if(field_structure_w.get("ERROR") != null) {
						p_msg.putField(Iso8583.Bit._039_RSP_CODE, field_structure_w.get("ERROR_CODE"));
					} else {
						if (!docBd.equals(docIn)) {
							p_msg.putField(Iso8583.Bit._039_RSP_CODE, "56");
							p_msg.putField(Base24Atm.Bit.ENTITY_ERROR, "2071 Card Do not Match");
						} else {
							encryptPan = crypto.encryptPanData(p_msg.getTrack2Data().getPan(), kwpEncryptKey.getValueUnderKsk(), p37, enableLog);
							encryptExpDate = crypto.encryptExpDateData(Utils.constructExpDateForCommand(p_msg.getTrack2Data().getExpiryDate()), kwpEncryptKey.getValueUnderKsk(), p37, enableLog);
						}
					}
					
					
				}
				if(!encryptPan.equals("") && !encryptPan.equals("ERROR")
						&& !encryptExpDate.equals("") && !encryptExpDate.equals("ERROR")) {
					p_msg.putField(Iso8583.Bit._039_RSP_CODE, SystemConstants.TWO_ZEROS);
					p_msg.putField(Iso8583.Bit._054_ADDITIONAL_AMOUNTS, encryptPan);
					p_msg.putField(Iso8583Post.Bit._059_ECHO_DATA, encryptExpDate);
				} else {
					p_msg.putField(Iso8583.Bit._039_RSP_CODE, "55");
					p_msg.putField(Base24Atm.Bit.ENTITY_ERROR, "2091 Error in the Process");
				}
				
				
				field_structure_w.put("PROCESS", "ENCRYPT");
				
				constructMsgToTm(p_msg, msgToTM, field_structure_w);
				
				GenericInterfaceTranTest.udpClient.sendData(Client.formatDatatoSend("==FIN PROCESO ENCRIPCION DATA==", p37, SystemConstants.PROCESS_ENCRYPT_DATA));
				
				
		
			} catch (XFieldUnableToConstruct e) {
				// TODO Auto-generated catch block
				EventRecorder.recordEvent(e);
			} catch (XPostilion e) {
				// TODO Auto-generated catch block
				EventRecorder.recordEvent(e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				EventRecorder.recordEvent(e);
			}			
			
			return p_msg;
		}
		
		public Iso8583 processMsgB24DecryptData(Iso8583 p_msg, boolean enableLog, Iso8583Post msgToTM, String process) {

			
			StructuredData field_structure_w = new StructuredData();

			try {
				String p37 = p_msg.isFieldSet(Iso8583.Bit._037_RETRIEVAL_REF_NR) ? p_msg.getField(Iso8583.Bit._037_RETRIEVAL_REF_NR) : "000000000000";
				GenericInterfaceTranTest.udpClient.sendData(Client.formatDatatoSend("==INICIO PROCESO DESENCRIPCION DATA==", p37, SystemConstants.PROCESS_DECRYPT_DATA));
				ProcessingCode ps =  new ProcessingCode(p_msg.getField(Iso8583.Bit._003_PROCESSING_CODE));	
				CryptoCfgManager crypcfgman = CryptoManager.getStaticConfiguration();
				DesKwp kwpEncryptKey = crypcfgman.getKwp("HKSDAKEY");
				
				GenericInterfaceTranTest.udpClient.sendData(Client.formatDatatoSend("kwpChannel:" + kwpEncryptKey.getName(), p37, SystemConstants.PROCESS_DECRYPT_DATA));
				Logger.logLine("kwpEncryptKey:" + kwpEncryptKey.getName(), enableLog);
				Logger.logLine("kwpEncryptKey.getValueUnderKsk():" + kwpEncryptKey.getValueUnderKsk(), enableLog);
				Crypto crypto = new Crypto(enableLog);
				String decryptPan = "";
				String decryptExpDate = "";
				p_msg.putMsgType(Iso8583.MsgType._0210_TRAN_REQ_RSP);
				
				if(p_msg.getField(Iso8583.Bit._041_CARD_ACCEPTOR_TERM_ID).substring(12,13).equals("C")) {
					decryptPan = crypto.decryptPanData(p_msg.getTrack2Data().getPan(), kwpEncryptKey.getValueUnderKsk(), p37, enableLog);
					decryptExpDate = crypto.decryptExpDateData(Utils.constructExpDateForCommand(p_msg.getTrack2Data().getExpiryDate()), kwpEncryptKey.getValueUnderKsk(), p37, enableLog);
				} else {
					DBHandler.getClientData(ps.toString(), p_msg.getTrack2Data().getPan(), ps.getFromAccount(), p_msg.getTrack2Data().getExpiryDate(),
							"0000000000000000", field_structure_w);			
					
					Logger.logLine(field_structure_w.toMsgString(), enableLog);
					String docIn = p_msg.isFieldSet(Iso8583.Bit._104_TRAN_DESCRIPTION)
							? p_msg.getField(Iso8583.Bit._104_TRAN_DESCRIPTION).substring(p_msg.getField(Iso8583.Bit._104_TRAN_DESCRIPTION).length()-10) : "0000000000";
					String docBd = field_structure_w != null && field_structure_w.get("CUSTOMER_ID") != null 
							? field_structure_w.get("CUSTOMER_ID").substring(field_structure_w.get("CUSTOMER_ID").length()-10) : "NOT FOUND";
					
					Logger.logLine("docIn: "+docIn, enableLog);
					Logger.logLine("docBd: "+docBd, enableLog);
					
					if(field_structure_w.get("ERROR") != null) {
						p_msg.putField(Iso8583.Bit._039_RSP_CODE, field_structure_w.get("ERROR_CODE"));
					} else {
						if (!docBd.equals(docIn)) {
							p_msg.putField(Iso8583.Bit._039_RSP_CODE, "56");
							p_msg.putField(Base24Atm.Bit.ENTITY_ERROR, "2071 Card Do not Match");
						} else {
							decryptPan = crypto.decryptPanData(p_msg.getTrack2Data().getPan(), kwpEncryptKey.getValueUnderKsk(), p37, enableLog);
							decryptExpDate = crypto.decryptExpDateData(Utils.constructExpDateForCommand(p_msg.getTrack2Data().getExpiryDate()), kwpEncryptKey.getValueUnderKsk(), p37, enableLog);
						}
					}
					
					
				}
				if(!decryptPan.equals("") && !decryptPan.equals("ERROR")
						&& !decryptExpDate.equals("") && !decryptExpDate.equals("ERROR")) {
					p_msg.putField(Iso8583.Bit._039_RSP_CODE, SystemConstants.TWO_ZEROS);
					p_msg.putField(Iso8583.Bit._054_ADDITIONAL_AMOUNTS, decryptPan);
					p_msg.putField(Iso8583Post.Bit._059_ECHO_DATA, decryptExpDate);
				} else {
					p_msg.putField(Iso8583.Bit._039_RSP_CODE, "55");
					p_msg.putField(Base24Atm.Bit.ENTITY_ERROR, "2091 Error in the Process");
				}
				
				
				
				field_structure_w.put("PROCESS", "DECRYPT");
				
				constructMsgToTm(p_msg, msgToTM, field_structure_w);
				
				GenericInterfaceTranTest.udpClient.sendData(Client.formatDatatoSend("==FIN PROCESO DESENCRIPCION DATA==", p37, SystemConstants.PROCESS_DECRYPT_DATA));
				
				
		
			} catch (XFieldUnableToConstruct e) {
				// TODO Auto-generated catch block
				EventRecorder.recordEvent(e);
			} catch (XPostilion e) {
				// TODO Auto-generated catch block
				EventRecorder.recordEvent(e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				EventRecorder.recordEvent(e);
			}			
			
			return p_msg;
		}
		
		public void constructMsgToTm(Iso8583 p_msgIso, Iso8583Post msgToTM, StructuredData field_structure_w) {
			
			try {
				
				
				msgToTM.putMsgType(Iso8583.MsgType._0220_TRAN_ADV);

				msgToTM.putField(Iso8583.Bit._003_PROCESSING_CODE, p_msgIso.getField(Iso8583.Bit._003_PROCESSING_CODE));
				if (p_msgIso.isFieldSet(Iso8583.Bit._004_AMOUNT_TRANSACTION))
					msgToTM.putField(Iso8583.Bit._004_AMOUNT_TRANSACTION,
							p_msgIso.getField(Iso8583.Bit._004_AMOUNT_TRANSACTION).toString());
				if (p_msgIso.isFieldSet(Iso8583.Bit._007_TRANSMISSION_DATE_TIME))
					msgToTM.putField(Iso8583.Bit._007_TRANSMISSION_DATE_TIME,
							p_msgIso.getField(Iso8583.Bit._007_TRANSMISSION_DATE_TIME).toString());

				if (p_msgIso.isFieldSet(Iso8583.Bit._011_SYSTEMS_TRACE_AUDIT_NR))
					msgToTM.putField(Iso8583.Bit._011_SYSTEMS_TRACE_AUDIT_NR,
							p_msgIso.getField(Iso8583.Bit._011_SYSTEMS_TRACE_AUDIT_NR).toString());

				if (p_msgIso.isFieldSet(Iso8583.Bit._012_TIME_LOCAL))
					msgToTM.putField(Iso8583.Bit._012_TIME_LOCAL, p_msgIso.getField(Iso8583.Bit._012_TIME_LOCAL).toString());

				if (p_msgIso.isFieldSet(Iso8583.Bit._013_DATE_LOCAL))
					msgToTM.putField(Iso8583.Bit._013_DATE_LOCAL, p_msgIso.getField(Iso8583.Bit._013_DATE_LOCAL).toString());

				if (p_msgIso.isFieldSet(Iso8583.Bit._015_DATE_SETTLE))
					msgToTM.putField(Iso8583.Bit._015_DATE_SETTLE, p_msgIso.getField(Iso8583.Bit._015_DATE_SETTLE).toString());

				if (p_msgIso.isFieldSet(Iso8583.Bit._017_DATE_CAPTURE))
					msgToTM.putField(Iso8583.Bit._017_DATE_CAPTURE, p_msgIso.getField(Iso8583.Bit._017_DATE_CAPTURE).toString());

				if (p_msgIso.isFieldSet(Iso8583.Bit._022_POS_ENTRY_MODE))
					msgToTM.putField(Iso8583.Bit._022_POS_ENTRY_MODE, p_msgIso.getField(Iso8583.Bit._022_POS_ENTRY_MODE).toString());
				else
					msgToTM.putField(Iso8583.Bit._022_POS_ENTRY_MODE, "021");

				if (p_msgIso.isFieldSet(Iso8583.Bit._032_ACQUIRING_INST_ID_CODE))
					msgToTM.putField(Iso8583.Bit._032_ACQUIRING_INST_ID_CODE,
							p_msgIso.getField(Iso8583.Bit._032_ACQUIRING_INST_ID_CODE).toString().replace(" ", "0"));

				if (p_msgIso.isFieldSet(Iso8583.Bit._035_TRACK_2_DATA)) {
						msgToTM.putField(Iso8583.Bit._035_TRACK_2_DATA,
								p_msgIso.getField(Iso8583.Bit._035_TRACK_2_DATA).toString().replace(" ", "0"));

				} else {
					msgToTM.putField(Iso8583.Bit._035_TRACK_2_DATA, "5454541234567890D24122211388313500000");
				}

				if (p_msgIso.isFieldSet(Iso8583.Bit._037_RETRIEVAL_REF_NR))
					msgToTM.putField(Iso8583.Bit._037_RETRIEVAL_REF_NR,
							p_msgIso.getField(Iso8583.Bit._037_RETRIEVAL_REF_NR).toString());
				
				if (p_msgIso.isFieldSet(Iso8583.Bit._039_RSP_CODE))
					msgToTM.putField(Iso8583.Bit._039_RSP_CODE,
							p_msgIso.getField(Iso8583.Bit._039_RSP_CODE).toString());

				if (p_msgIso.isFieldSet(Iso8583.Bit._041_CARD_ACCEPTOR_TERM_ID)) {
					msgToTM.putField(Iso8583.Bit._041_CARD_ACCEPTOR_TERM_ID,
							p_msgIso.getField(Iso8583.Bit._041_CARD_ACCEPTOR_TERM_ID).substring(0,8));
				}

				msgToTM.putField(Iso8583.Bit._042_CARD_ACCEPTOR_ID_CODE, "               ");

				if (p_msgIso.isFieldSet(Iso8583.Bit._043_CARD_ACCEPTOR_NAME_LOC))
					msgToTM.putField(Iso8583.Bit._043_CARD_ACCEPTOR_NAME_LOC,
							p_msgIso.getField(Iso8583.Bit._043_CARD_ACCEPTOR_NAME_LOC).toString());

				if (p_msgIso.isFieldSet(Iso8583.Bit._048_ADDITIONAL_DATA))
					msgToTM.putField(Iso8583.Bit._048_ADDITIONAL_DATA,
							p_msgIso.getField(Iso8583.Bit._048_ADDITIONAL_DATA).toString());

				if (p_msgIso.isFieldSet(Iso8583.Bit._049_CURRENCY_CODE_TRAN))
					msgToTM.putField(Iso8583.Bit._049_CURRENCY_CODE_TRAN,
							p_msgIso.getField(Iso8583.Bit._049_CURRENCY_CODE_TRAN).toString());
				else
					msgToTM.putField(Iso8583.Bit._049_CURRENCY_CODE_TRAN,
							SystemConstants.CURRENCY);

				if (p_msgIso.isFieldSet(Iso8583.Bit._052_PIN_DATA))
					msgToTM.putField(Iso8583.Bit._052_PIN_DATA,
							Transform.fromHexToBin(p_msgIso.getField(Iso8583.Bit._052_PIN_DATA)));

					msgToTM.putField(Iso8583.Bit._100_RECEIVING_INST_ID_CODE, SystemConstants.INSTITUTION_ID);

				if (p_msgIso.isFieldSet(Iso8583.Bit._102_ACCOUNT_ID_1))
					msgToTM.putField(Iso8583.Bit._102_ACCOUNT_ID_1, p_msgIso.getField(Iso8583.Bit._102_ACCOUNT_ID_1).toString());

				if (p_msgIso.isFieldSet(Iso8583.Bit._103_ACCOUNT_ID_2))
					msgToTM.putField(Iso8583.Bit._103_ACCOUNT_ID_2, p_msgIso.getField(Iso8583.Bit._103_ACCOUNT_ID_2).toString());

				msgToTM.putField(Iso8583.Bit._025_POS_CONDITION_CODE, Iso8583.PosCondCode._00_NORMAL_PRESENTMENT);
				msgToTM.putField(Iso8583.Bit._026_POS_PIN_CAPTURE_CODE, "04");
				msgToTM.putField(Iso8583Post.Bit._123_POS_DATA_CODE, "911201513344002");
				msgToTM.putField(Iso8583.Bit._098_PAYEE, "0054150070650000000000000");
				
				
				String key = "0220".concat(p_msgIso.getField(Iso8583.Bit._037_RETRIEVAL_REF_NR))
						.concat(p_msgIso.getField(Iso8583.Bit._013_DATE_LOCAL))
						.concat(p_msgIso.getField(Iso8583.Bit._012_TIME_LOCAL))
						.concat(p_msgIso.getField(Iso8583.Bit._011_SYSTEMS_TRACE_AUDIT_NR));
				
				//127.2 SWITCHKEY
				msgToTM.putPrivField(Iso8583Post.PrivBit._002_SWITCH_KEY, key);

				
				
				msgToTM.putStructuredData(field_structure_w);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
}
