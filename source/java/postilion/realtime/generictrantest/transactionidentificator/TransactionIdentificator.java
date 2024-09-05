package postilion.realtime.generictrantest.transactionidentificator;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import postilion.realtime.generictrantest.GenericInterfaceTranTest;
import postilion.realtime.generictrantest.returnBitmapB24.MapFormatterFromBase24;
import postilion.realtime.generictrantest.streamBase24ATM.Base24Atm;
import postilion.realtime.generictrantest.systemConstans.SystemConstants;
import postilion.realtime.generictrantest.util.ClientCallServices;
import postilion.realtime.generictrantest.util.DataLoader;
import postilion.realtime.generictrantest.util.Logger;
import postilion.realtime.sdk.message.bitmap.Iso8583;
import postilion.realtime.sdk.util.XPostilion;

public class TransactionIdentificator {

	private Map<String, Object> directory = new ClientCallServices().getCallServices("GET", SystemConstants.PATHUPLOAD, SystemConstants.DIRECTORIO);
	private DataLoader dataLoader = new DataLoader();;

	public String getTransactionIdentification(Base24Atm msgBase24) {
		Map<String, Object> descriptionsMap = new HashMap<>();
		
		Logger.logLine("msgBase24 IN getTransactionIdentificator: "+ msgBase24, true);
		String fields = null;
		try {
				fields = "FIELD_003=" + msgBase24.getField(Iso8583.Bit._003_PROCESSING_CODE) + "&" + "FIELD_022="
						+ msgBase24.getField(Iso8583.Bit._022_POS_ENTRY_MODE) + "&" + "FIELD_035="
						+ msgBase24.getField(Iso8583.Bit._035_TRACK_2_DATA).substring(0, 1) + "&" + "FIELD_041="
						+ msgBase24.getField(Iso8583.Bit._041_CARD_ACCEPTOR_TERM_ID).substring(12,13);
				Logger.logLine("Fields send: "+ fields, true);
				descriptionsMap = (Map<String, Object>) dataLoader.getIdentificationTextForTransaction(fields);
				if (Objects.nonNull(descriptionsMap) || !descriptionsMap.isEmpty()) {
					GenericInterfaceTranTest.infoTrasactionIdentidicator.put(fields, descriptionsMap.values());
				}	
				return fields;		
						
		} catch (XPostilion e) {
			fields="Error e "+ e.toString();
		} catch (Exception ex) {
			fields="Error ex "+ ex.toString();
		}
		return fields;
	}


	public Map<String, Object> getTransactionValidation(Base24Atm msgBase24) {
		Map<String, Object> descriptionsMap = new HashMap<>();
		Map<String, Object> descriptionsvalidation = new HashMap<>();
		Map<String ,Object> descriptionMapPdf = new HashMap<>();
		try {
				JSONObject body = new MapFormatterFromBase24().getBase24MapFromBitmap(msgBase24);
				descriptionsMap = new ClientCallServices().getCallServices("POST",  directory.get("OrchestratorPost").toString(), body.toString());
				descriptionsvalidation.put("FIELD_127.22.PROCESS_APPLY_CRITERIA_RESULT", descriptionsMap.get("FIELD_127.22.PROCESS_APPLY_CRITERIA_RESULT"));
				descriptionsvalidation.put("FIELD_127.22.PROCESS_TRANSACTION_IDENTIFY_STATUS", descriptionsMap.get("FIELD_127.22.PROCESS_TRANSACTION_IDENTIFY_STATUS"));
				JSONArray bodyPdf = new JSONArray();
				bodyPdf.put(0, body);
				bodyPdf.put(1, new JSONObject(descriptionsMap));
				descriptionMapPdf = new ClientCallServices().getCallServices("POST", directory.get("PdfPost").toString(), bodyPdf.toString());
				descriptionsvalidation.put("ROUTE_FILE", descriptionMapPdf.get("ROUTE_FILE"));
		} catch (XPostilion e) {
			descriptionsvalidation.put("Error", e.getMessage());
		} catch (Exception ex) {
			descriptionsvalidation.put("Error", ex.getMessage());
		}
		return descriptionsvalidation;
	}

}
