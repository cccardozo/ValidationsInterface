package postilion.realtime.generictrantest.enlistGeneralMessage;

import postilion.realtime.generictrantest.messageconverters.Base24toIsoMessageConverter;
import postilion.realtime.generictrantest.messageconverters.IsoToBase24MessageConverter;
import postilion.realtime.generictrantest.streamBase24ATM.Base24Atm;
import postilion.realtime.generictrantest.util.CardStatus;
import postilion.realtime.generictrantest.util.Logger;
import postilion.realtime.generictrantest.validateSwitchKey.FieldSwitchKey;
import postilion.realtime.sdk.eventrecorder.EventRecorder;
import postilion.realtime.sdk.message.bitmap.Iso8583;
import postilion.realtime.sdk.message.bitmap.Iso8583Post;
import postilion.realtime.sdk.util.XPostilion;

public class EnlistMessageB24Iso {
	public EnlistMessageB24Iso() {
		// TODO Auto-generated constructor stub
	}

	public Iso8583Post copyMessagetoIso8583Post(Iso8583 messageIso, String paramTransactionIdentificator,
			boolean enableLog, String process) {
		Iso8583Post msgToTm = new Iso8583Post();
		Base24toIsoMessageConverter msgValue = new Base24toIsoMessageConverter();
		if (messageIso instanceof Base24Atm) {
			Base24Atm msgFromRemote = (Base24Atm) messageIso;

			msgValue.converB24ToIso8583Post(msgFromRemote, msgToTm, paramTransactionIdentificator, enableLog, process);
		} else if (messageIso instanceof Iso8583) {
			msgValue.converIsoToIso8583Post(messageIso, msgToTm, enableLog);
			msgToTm = (Iso8583Post) messageIso;
		}
		try {
			FieldSwitchKey validateField127_2 = new FieldSwitchKey();
			msgToTm.putPrivField(Iso8583Post.PrivBit._002_SWITCH_KEY,
					validateField127_2.constructSwitchKeyForReq(messageIso, "ATM"));
		} catch (XPostilion e) {
			// TODO Auto-generated catch block
			EventRecorder.recordEvent(e);
		}
		return msgToTm;
	}

	public Iso8583 validatePin(Iso8583 messageIso, boolean enableLog, String process) {
		Iso8583 msgToRemote = new Iso8583();
		Base24toIsoMessageConverter msgValue = new Base24toIsoMessageConverter();
		if (messageIso instanceof Base24Atm) {
			msgToRemote = new Base24Atm(null);
			Base24Atm msgFromRemote = (Base24Atm) messageIso;

			msgToRemote = msgValue.processMsgB24ValidatePin(msgFromRemote, enableLog, process);
		} else if (messageIso instanceof Iso8583) {
			msgToRemote = msgValue.processMsgB24ValidatePin(messageIso, enableLog, process);
		}
		return msgToRemote;
	}

	public Iso8583 validatePinCvv(Iso8583 messageIso, boolean enableLog, String process) {
		Iso8583 msgToRemote = new Iso8583();
		Base24toIsoMessageConverter msgValue = new Base24toIsoMessageConverter();
		if (messageIso instanceof Base24Atm) {
			msgToRemote = new Base24Atm(null);
			Base24Atm msgFromRemote = (Base24Atm) messageIso;

			msgToRemote = msgValue.processMsgB24ValidatePinCvv(msgFromRemote, enableLog, process);
		} else if (messageIso instanceof Iso8583) {
			msgToRemote = msgValue.processMsgB24ValidatePinCvv(messageIso, enableLog, process);
		}
		return msgToRemote;
	}

	public Iso8583 validateCvv(Iso8583 messageIso, boolean enableLog, String process) {
		Iso8583 msgToRemote = new Iso8583();
		Base24toIsoMessageConverter msgValue = new Base24toIsoMessageConverter();
		if (messageIso instanceof Base24Atm) {
			msgToRemote = new Base24Atm(null);
			Base24Atm msgFromRemote = (Base24Atm) messageIso;

			msgToRemote = msgValue.processMsgB24ValidateCvv(msgFromRemote, enableLog, process);
		} else if (messageIso instanceof Iso8583) {
			msgToRemote = msgValue.processMsgB24ValidateCvv(messageIso, enableLog, process);
		}
		return msgToRemote;
	}

	public Iso8583 changePin(Iso8583 messageIso, Iso8583Post msgToTM, boolean enableLog, String process) {
		Iso8583 msgToRemote = new Iso8583();
		Base24toIsoMessageConverter msgValue = new Base24toIsoMessageConverter();
		if (messageIso instanceof Base24Atm) {
			msgToRemote = new Base24Atm(null);
			Base24Atm msgFromRemote = (Base24Atm) messageIso;

			msgToRemote = msgValue.changePin(msgFromRemote, msgToTM, enableLog, process);
		} else if (messageIso instanceof Iso8583) {
			msgToRemote = msgValue.changePin(messageIso, msgToTM, enableLog, process);
		}
		return msgToRemote;
	}

	public Iso8583 resultCardStatus(Iso8583 messageIso, Iso8583Post msgToTM, boolean enableLog, String process) {
		Iso8583 msgToRemote = new Iso8583();
		CardStatus msgValue = new CardStatus();
		if (messageIso instanceof Base24Atm) {
			msgToRemote = new Base24Atm(null);
			Base24Atm msgFromRemote = (Base24Atm) messageIso;

			msgToRemote = msgValue.resultCardStatus(msgFromRemote, msgToTM, enableLog, process);
		} else if (messageIso instanceof Iso8583) {
			msgToRemote = msgValue.resultCardStatus(messageIso, msgToTM, enableLog, process);
		}
		return msgToRemote;
	}

	public Base24Atm copyMessagetoB24(Iso8583Post msg) {
		IsoToBase24MessageConverter msgToBase24 = new IsoToBase24MessageConverter();
		Base24Atm msgToRemote = new Base24Atm(null);
		return msgToBase24.converToB24(msgToRemote, msg);
	}
}
