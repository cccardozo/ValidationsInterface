package postilion.realtime.generictrantest.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.json.JSONObject;

import postilion.realtime.generictrantest.GenericInterfaceTranTest;
import postilion.realtime.generictrantest.systemConstans.SystemConstants;

public class DataLoader {

	@SuppressWarnings("unchecked")
	public Object getIdentificationTextForTransaction(String fields) {
		Map<String, Object> result = new HashMap<>();
		try {
			  result = (Map<String, Object>) getTransactionDescriptionFromCache(fields);
		} catch (Exception e) {
			return e.getMessage();
		}
		return result;
	}

	private Object getTransactionDescriptionFromCache(String key) {
		if (GenericInterfaceTranTest.infoTrasactionIdentidicator.containsKey(key)) {
			return GenericInterfaceTranTest.infoTrasactionIdentidicator.get(key);
		}
		
		return getTransactionDescriptionFromKey(key);
	}
	
	private Map<String,Object> getTransactionDescriptionFromKey(String transactionKey) {
		Map<String,Object> resultIdentification = null;
		resultIdentification = new ClientCallServices().getCallServices("GET", transactionKey , "");
		resultIdentification.keySet().removeIf(t -> !t.startsWith("F"));
		return resultIdentification;
	}
}
