package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;

import systemConstans.SystemConstants;

public class ClientCallServices {
	
	

	public ClientCallServices() {
		// TODO Auto-generated constructor stub
	}
	
	public Map<String, Object> getCallServices(String verb, String path , String fields){
		Map<String,Object> resultServices = null;
		StringBuilder result = new StringBuilder();
		try {
			if (verb.equals("GET")) {
				path = path.concat(fields);
			}
			URL url = new URL(path);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(verb);
			connection.setConnectTimeout(2000);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			if (verb.equals("POST")) {
				connection.setDoOutput(true);
				try (OutputStream os = connection.getOutputStream()) {
	                byte[] input = fields.getBytes("utf-8");
	                os.write(input, 0, input.length);
	            }
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String linea;
			while ((linea = reader.readLine()) != null) {
				result.append(linea);
			}
			reader.close();
			resultServices = (Map<String, Object>) (new JSONObject(result.toString()).toMap());
		} catch (Exception e) {
			result.append("No se pudo establecer conexion" + e.getMessage());
		}
		return resultServices;
	}

}
