package messageconverters;


import postilion.realtime.sdk.message.bitmap.Iso8583;
import postilion.realtime.sdk.message.bitmap.Iso8583Post;
import postilion.realtime.sdk.util.XPostilion;
import streamBase24ATM.Base24Atm;
import streamBase24ATM.Header;
import transactionidentificator.TransactionIdentificator;

public class IsoToBase24MessageConverter {

	public IsoToBase24MessageConverter() {
		// TODO Auto-generated constructor stub
	}

	public Base24Atm converToB24(Base24Atm p_msgB24, Iso8583Post p_MsgIso) {
		try {
			p_msgB24.putHeader(constructHeaderSourceNode(p_msgB24));
			p_msgB24.putMsgType(p_MsgIso.getMsgType());
			p_msgB24.putField(Iso8583.Bit._003_PROCESSING_CODE, p_MsgIso.getField(Iso8583.Bit._003_PROCESSING_CODE));
			p_msgB24.putField(Iso8583.Bit._004_AMOUNT_TRANSACTION,p_MsgIso.getField(Iso8583.Bit._004_AMOUNT_TRANSACTION));
			p_msgB24.putField(Iso8583.Bit._007_TRANSMISSION_DATE_TIME,p_MsgIso.getField(Iso8583.Bit._007_TRANSMISSION_DATE_TIME));
			p_msgB24.putField(Iso8583.Bit._011_SYSTEMS_TRACE_AUDIT_NR,p_MsgIso.getField(Iso8583.Bit._011_SYSTEMS_TRACE_AUDIT_NR));
			p_msgB24.putField(Iso8583.Bit._012_TIME_LOCAL, p_MsgIso.getField(Iso8583.Bit._012_TIME_LOCAL));
			p_msgB24.putField(Iso8583.Bit._013_DATE_LOCAL, p_MsgIso.getField(Iso8583.Bit._013_DATE_LOCAL));
			p_msgB24.putField(Iso8583.Bit._015_DATE_SETTLE, p_MsgIso.getField(Iso8583.Bit._015_DATE_SETTLE));
			p_msgB24.putField(Iso8583.Bit._017_DATE_CAPTURE, p_MsgIso.getField(Iso8583.Bit._017_DATE_CAPTURE));
			p_msgB24.putField(Iso8583.Bit._022_POS_ENTRY_MODE, p_MsgIso.getField(Iso8583.Bit._022_POS_ENTRY_MODE));
			p_msgB24.putField(Iso8583.Bit._032_ACQUIRING_INST_ID_CODE,p_MsgIso.getField(Iso8583.Bit._032_ACQUIRING_INST_ID_CODE));
			p_msgB24.putField(Iso8583.Bit._035_TRACK_2_DATA, p_MsgIso.getField(Iso8583.Bit._035_TRACK_2_DATA));
			p_msgB24.putField(Iso8583.Bit._037_RETRIEVAL_REF_NR, p_MsgIso.getField(Iso8583.Bit._037_RETRIEVAL_REF_NR));
			p_msgB24.putField(Iso8583.Bit._040_SERVICE_RESTRICTION_CODE,p_MsgIso.getField(Iso8583.Bit._040_SERVICE_RESTRICTION_CODE));
			p_msgB24.putField(Iso8583.Bit._044_ADDITIONAL_RSP_DATA,p_MsgIso.getField(Iso8583.Bit._044_ADDITIONAL_RSP_DATA));
			p_msgB24.putField(Iso8583.Bit._102_ACCOUNT_ID_1, p_MsgIso.getField(Iso8583.Bit._102_ACCOUNT_ID_1));
			//Validar si es exitoso 39 00, de lo contrario fallida 
			if (p_msgB24.isFieldSet(Iso8583.Bit._017_DATE_CAPTURE)) {
				p_msgB24.putField(Iso8583.Bit._017_DATE_CAPTURE, p_MsgIso.getField(Iso8583.Bit._017_DATE_CAPTURE));				
			}else{
				p_msgB24.putField(Iso8583.Bit._017_DATE_CAPTURE, p_MsgIso.getField(Iso8583.Bit._015_DATE_SETTLE));
			}
	
			if (p_MsgIso.isPrivFieldSet(Iso8583Post.PrivBit._022_STRUCT_DATA)) {
				p_msgB24.putField(Iso8583.Bit._041_CARD_ACCEPTOR_TERM_ID,p_MsgIso.getStructuredData().get("KEY_B24_P041")!=null
						? p_MsgIso.getStructuredData().get("KEY_B24_P041")
						: "0054232100001   ");	
			} else {
				p_msgB24.putField(Iso8583.Bit._041_CARD_ACCEPTOR_TERM_ID,"0054232100001   ");
			}
			if (p_MsgIso.isFieldSet(Iso8583.Bit._039_RSP_CODE)) {
				p_msgB24.putField(Iso8583.Bit._039_RSP_CODE, p_MsgIso.getField(Iso8583.Bit._039_RSP_CODE));
				p_msgB24.putField(Iso8583.Bit._038_AUTH_ID_RSP, p_MsgIso.getField(Iso8583.Bit._038_AUTH_ID_RSP));
				p_msgB24.putField(63,p_MsgIso.getField(Iso8583.Bit._039_RSP_CODE).equals("00") 
						? "Transaccion Exitosa - "
						: "Transaccion Fallida - ");
			}else
			{
				p_msgB24.putField(Iso8583.Bit._038_AUTH_ID_RSP, "000000");
				p_msgB24.putField(Iso8583.Bit._039_RSP_CODE, Iso8583.RspCode._30_FORMAT_ERROR);
				p_msgB24.putField(63, "Transaccion Fallida - ");
			}	
			if (p_msgB24.isFieldSet(Iso8583.Bit._048_ADDITIONAL_DATA)) {
				p_msgB24.putField(Iso8583.Bit._048_ADDITIONAL_DATA, p_MsgIso.getField(Iso8583.Bit._048_ADDITIONAL_DATA));				
			}else{
				p_msgB24.putField(Iso8583.Bit._048_ADDITIONAL_DATA,"00520000000030000000000000000000000000000000");
			}
			
			if (p_msgB24.isFieldSet(Iso8583.Bit._049_CURRENCY_CODE_TRAN)) {
				p_msgB24.putField(Iso8583.Bit._049_CURRENCY_CODE_TRAN, p_MsgIso.getField(Iso8583.Bit._049_CURRENCY_CODE_TRAN));				
			}else{
				p_msgB24.putField(Iso8583.Bit._049_CURRENCY_CODE_TRAN, "170");
			}
			
			if (p_msgB24.isFieldSet(Iso8583.Bit._054_ADDITIONAL_AMOUNTS)) {
				p_msgB24.putField(Iso8583.Bit._054_ADDITIONAL_AMOUNTS, p_MsgIso.getField(Iso8583.Bit._054_ADDITIONAL_AMOUNTS));				
			}else{
				p_msgB24.putField(Iso8583.Bit._054_ADDITIONAL_AMOUNTS,"000002000000000000000000000000000000");
			}

			p_msgB24.putField(Iso8583.Bit._128_MAC_EXTENDED, p_MsgIso.getField(Iso8583.Bit._128_MAC_EXTENDED));
		} catch (XPostilion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p_msgB24;
	}

	public Header constructHeaderSourceNode(Base24Atm msgFromRemote) {
		Header atmHeader;
		try {
				atmHeader = new Header();
				atmHeader.putField(Header.Field.ISO_LITERAL, Header.Iso.ISO);
				atmHeader.putField(Header.Field.RESPONDER_CODE, Header.SystemCode.HOST);
				atmHeader.putField(Header.Field.PRODUCT_INDICATOR, Header.ProductIndicator.ATM);
				atmHeader.putField(Header.Field.RELEASE_NUMBER, Base24Atm.Version.REL_NR_40);
				atmHeader.putField(Header.Field.STATUS, Header.Status.OK);
				atmHeader.putField(Header.Field.ORIGINATOR_CODE, Header.OriginatorCode.CINCO);
			
		} catch (Exception e) {
			atmHeader = new Header();
		}
		return atmHeader;
	}
}
