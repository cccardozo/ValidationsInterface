package postilion.realtime.generictrantest.returnBitmapB24;


import org.json.JSONObject;

import postilion.realtime.generictrantest.streamBase24ATM.Base24Atm;
import postilion.realtime.generictrantest.systemConstans.SystemConstants;

public class MapFormatterFromBase24 {

	public MapFormatterFromBase24() {
		// TODO Auto-generated constructor stub
	}

	public JSONObject getBase24MapFromBitmap(Base24Atm msgBase24) throws Exception {
		JSONObject bodyOrchestrator = new JSONObject();
		try {
			String returnBitmap = new BitmapProcessorB24().getBitMap(msgBase24);
			char[] msgBitmap = returnBitmap.toCharArray();
			for (int i = 2; i < msgBitmap.length; i++) {
				if (msgBitmap[i] == '1') {
					String field = msgBase24.getField(i+1);
					bodyOrchestrator.put(SystemConstants.FIELD + String.format("%03d", i+1), field);
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return bodyOrchestrator;
	}

}
