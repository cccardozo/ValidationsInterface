package postilion.realtime.generictrantest.crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import postilion.realtime.generictrantest.GenericInterfaceTranTest;
import postilion.realtime.sdk.crypto.CryptoCfgManager;
import postilion.realtime.sdk.crypto.CryptoManager;
import postilion.realtime.sdk.crypto.ICryptoConnection;
import postilion.realtime.sdk.crypto.XCryptoCommsFailure;
import postilion.realtime.sdk.util.XPostilion;

public class HSMDirectorBuild {
	private String command = null;
	private Socket socket = null;
	private DataOutputStream out = null;
	private DataInputStream in = null;
	public CryptoCfgManager crypto_cfg_man = null;
	public static ICryptoConnection connection = null;
	static String errorHsm = "00000000ER99";

	public Socket getSocket() {
		return socket;
	}

	public void resetConecction(String ip, int puerto) {
		try {
			socket.close();
			openConnectHSM(ip, puerto);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String sendCommand(String commandHSM, String ip, int puerto) {
		String data = null;
		try {
			
			if(GenericInterfaceTranTest.modeConnection.toLowerCase().equals("udp")) {
				data = GenericInterfaceTranTest.udpClientAtalla.sendMsgToAtalla(commandHSM, false);
			} else {
				data = processMessage(commandHSM);
				if (data == null || errorHsm.equals(data)) {
					if(socket != null)
						socket.close();
					openConnectHSM(ip, puerto);
					data = processMessage(commandHSM);
				}
			}
			
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPostilion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return data;
	}

	@SuppressWarnings("deprecation")
	public String processMessage(String msgIn) {
	    try {
	        if (socket != null) {
	            try (DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
	                 DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()))) {

	                String response = "";
	                command = msgIn;

	                out.writeShort(command.getBytes().length);
	                out.write(command.getBytes());
	                out.flush();

	                try {
	                    response = new String(in.readLine());
	                    
	                } catch (SocketException e) {
	                    e.printStackTrace();
	                } catch (Exception e) {
	                    e.printStackTrace();
	                } finally {
		                if (in != null) in.close();
		                if (out != null) out.close();
					}

	                return response;
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return errorHsm; // No hay Conexion con la HSM
	}
	
//	@SuppressWarnings("deprecation")
//	public String processMessage(String msgIn) {
//		try {
//			if (socket != null) {
//				in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
//				out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
//				String response = "";
//				command = msgIn;
//				// socket.shutdownInput();
//				out.writeShort(command.getBytes().length);
//				out.write(command.getBytes());
//				out.flush();
//
//				
//				try {
//					
//					response = new String(in.readLine());
//					
//				} catch (SocketException e) {
//					e.printStackTrace();
//				} catch (Exception e) {
//					e.printStackTrace();
//					e.printStackTrace();
//				} finally {
//	                if (in != null) in.close();
//	                if (out != null) out.close();
//				}
//				return response;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return errorHsm; // No hay Conexion con la HSM
//	}

	public void closeConnectHSM() {
	    try {
	        if (socket != null && !socket.isClosed()) {
	            socket.close();
	        }
	        if (out != null) {
	            out.close();
	        }
	        if (in != null) {
	            in.close();
	        }
	        socket = null;
	    } catch (Exception exception) {
	        exception.printStackTrace();
	    }
	}


	public void openConnectHSM(String ipAddress, int port) {
		// hsm OFF
		if (ipAddress.equals("-1") && port == -1) {
		} else {
			if (ipAddress.equals("0") && port == 0) {
				ipAddress = HSMKey.ip_hsm;
				port = HSMKey.port_hsm;
			}

			try {
				socket = new Socket(ipAddress, port);
				socket.setSoTimeout(5000);
				System.out.println("<<< Sockt s >>> :" + socket);
			} catch (Exception ex) {
				try {
					socket.close();
				} catch (Exception e) {
				}
				ex.printStackTrace();
			}
		}
	}


}
