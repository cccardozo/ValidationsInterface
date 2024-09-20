package postilion.realtime.generictrantest.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import postilion.realtime.generictrantest.crypto.Crypto;
import postilion.realtime.generictrantest.util.Logger;
import postilion.realtime.sdk.eventrecorder.EventRecorder;
import postilion.realtime.sdk.jdbc.JdbcManager;
import postilion.realtime.sdk.message.bitmap.StructuredData;

/**
 * This class search for information in the realtime database and loads the
 * result in memory
 */
public class DBHandler {
	static final String seq_nr = "NIL";
	/*-------------------------------------------------------------------------*/

	/**
	 * El metodo se encarga de validar que la tarjeta del cliente del corresponsal
	 * exista. Si la tarjeta existe trae informacion de base de datos relacionada a
	 * la tarjeta y a su cuenta. Modifica al objeto Super almacenando la informacion
	 * encontrada en base de lo contrario genera una declinacion.
	 * 
	 * @param pCode
	 * @param pan
	 * @param typeAccountInput
	 * @param expiryDate
	 * @param accountInput
	 * @param objectValidations
	 * @throws Exception
	 */
	public static void getClientData(String pCode, String pan, String typeAccountInput, String expiryDate,
			String accountInput, StructuredData sd) throws Exception {

		String panHash = Crypto.getHashPanCNB(pan);

		CallableStatement cst = null;
		CallableStatement stmt = null;
		ResultSet rs = null;
		Connection con = null;

		try {

			con = JdbcManager.getConnection(Account.POSTCARD_DATABASE);
			stmt = con.prepareCall(StoreProcedures.GET_PINOFFSET_DATA);
			stmt.setString(1, panHash);
			stmt.setString(2, expiryDate);
			stmt.registerOutParameter(3, java.sql.Types.VARCHAR);// customer id
			stmt.registerOutParameter(4, java.sql.Types.VARCHAR);// default account type
			stmt.registerOutParameter(5, java.sql.Types.VARCHAR);// name
			stmt.registerOutParameter(6, java.sql.Types.INTEGER);// issure
			stmt.registerOutParameter(7, java.sql.Types.VARCHAR);// extended field
			stmt.registerOutParameter(8, java.sql.Types.VARCHAR);// sequence nr
			stmt.registerOutParameter(9, java.sql.Types.CHAR);// id type
			stmt.registerOutParameter(10, java.sql.Types.VARCHAR);// PinOffset
			stmt.registerOutParameter(11, java.sql.Types.VARCHAR);// card status
			stmt.registerOutParameter(12, java.sql.Types.VARCHAR);// hold rsp code
			stmt.execute();

			String customerId = stmt.getString(3);// customer id
			String defaultAccountType = stmt.getString(4);// default account type
			String customerName = stmt.getString(5);// name
			int issuerNr = stmt.getInt(6);// issuer number
			String extendedField = stmt.getString(7);// extended field
			String sequenceNr = stmt.getString(8);// sequence number
			String idType = stmt.getString(9);// id type
			String pinOffset = stmt.getString(10);// Pinoffset
			String cardStatus = stmt.getString(11);// card status
			String holdRspCode = stmt.getString(12);// hold rsp code
			String accountTypeClient = null;// account Type
			String accountNumber = null;// account Number
			String processingCode = pCode;// p Code

			if (!(issuerNr == 0)) {
				cst = con.prepareCall(StoreProcedures.CM_LOAD_CARD_ACCOUNTS);
				cst.setInt(1, issuerNr);
				cst.setString(2, panHash);
				cst.setString(3, sequenceNr);
				rs = cst.executeQuery();
				boolean doWhile = true;
				boolean byDefault = true;
				while (rs.next()) {
					doWhile = false;
					String accountType = rs.getString(ColumnNames.ACCOUNT_TYPE);// account type
					String accountId = rs.getString(ColumnNames.ACCOUNT_ID);// account type

					accountInput = ("000000000000000000" + accountInput)
							.substring(("000000000000000000" + accountInput).length() - 18);

					if (accountInput.equals(Crypto.getClearAccount(accountId))) {
						accountTypeClient = accountType;// account type
						accountNumber = accountId;// cuenta
						processingCode = pCode.substring(0, 2) + accountTypeClient + pCode.substring(4, 6);
						break;
					} else if (accountType.equals(typeAccountInput) && byDefault) {
						accountTypeClient = accountType;// account type
						accountNumber = accountId;// cuenta
						processingCode = pCode.substring(0, 2) + accountTypeClient + pCode.substring(4, 6);
						byDefault = false;
					} else if (accountType.equals(defaultAccountType) && byDefault) {
						accountTypeClient = accountType;// account type
						accountNumber = accountId;// cuenta
						processingCode = pCode.substring(0, 2) + accountTypeClient + pCode.substring(4, 6);
					}
				}
				if (doWhile) {
					chooseCodeForNoAccount(pCode, sd);
				} else {
					if (accountTypeClient == null || accountNumber == null) {
						sd.put("ERROR", "CUENTA NO ASOCIADA");
						sd.put("PINOFFSET", pinOffset);
						sd.put("ERROR_CODE", "56");

					} else {
						sd.put("P_CODE", processingCode);

						sd.put("CLIENT_ACCOUNT_NR", Crypto.getClearAccount(accountNumber));
						sd.put("CLIENT_CARD_NR", pan);

						sd.put("CLIENT_ACCOUNT_TYPE", accountTypeClient);
						sd.put("CUSTOMER_ID", customerId);
						sd.put("CLIENT_CARD_CLASS", extendedField);
						sd.put("CUSTOMER_NAME", customerName);
						sd.put("CUSTOMER_ID_TYPE", idType);
						sd.put("PINOFFSET", pinOffset);
						sd.put("ISSUER", String.valueOf(issuerNr));
						sd.put("CARDSTATUS", cardStatus);
						sd.put("HOLDRSPCODE", holdRspCode);

					}
				}
			} else {
				sd.put("ERROR", "NO EXITE TARJETA CLIENTE");
				sd.put("ERROR_CODE", "56");
			}

			JdbcManager.commit(con, stmt, rs);
			JdbcManager.commit(con, cst, rs);
		} catch (SQLException e) {
			sd.put("ERROR", "Database Connection Failure.");
			sd.put("ERROR_CODE", "06");
			EventRecorder.recordEvent(e);
			EventRecorder.recordEvent(new Exception("SQL Ex: " + e.toString()));

		} finally {
			JdbcManager.cleanup(con, stmt, rs);
			JdbcManager.cleanup(con, cst, rs);
		}

	}

	public static void chooseCodeForNoAccount(String pCode, StructuredData sd) {

		switch (pCode.substring(2, 4)) {
		case "20":
			sd.put("ERROR", "NO CHECK ACCOUNT");
			sd.put("ERROR_CODE", "52");
			break;
		case "10":
			sd.put("ERROR", "NO SAVINGS ACCOUNT");
			sd.put("ERROR_CODE", "53");
			break;
		default:
			sd.put("ERROR", "Cuenta No Inscrita");
			sd.put("ERROR_CODE", "53");
			break;
		}
	}

	/**
	 * Actualiza el offset de una tarjeta en la base de datos.
	 *
	 * @param issuer     El emisor de la tarjeta.
	 * @param newOffset  El nuevo offset a establecer.
	 * @param pan        El número de cuenta primaria (PAN) de la tarjeta.
	 * @param expiryDate La fecha de expiración de la tarjeta.
	 * @param enableLog  Indica si se debe habilitar el registro de logs.
	 * @return true si se actualizó al menos un registro, false en caso contrario.
	 */
	public static boolean updateOffset(String issuer, String newOffset, String pan, String expiryDate,
			boolean enableLog) {
		boolean result = false;

		try (Connection con = JdbcManager.getConnection(Account.POSTCARD_DATABASE)) {
			String panHash = Crypto.getHashPanCNB(pan);
			Logger.logLine("panHash:" + panHash, enableLog);

			if (checkAndUpdate(con, issuer, "A", newOffset, panHash, expiryDate, enableLog))
				result = true;

			if (checkAndUpdate(con, issuer, "B", newOffset, panHash, expiryDate, enableLog))
				result = true;

			JdbcManager.commit(con);
		} catch (SQLException e) {
			EventRecorder.recordEvent(new Exception("SQL: " + e.toString()));
			EventRecorder.recordEvent(e);
		} catch (Exception e) {
			EventRecorder.recordEvent(new Exception("SQL Ex: " + e.toString()));
			EventRecorder.recordEvent(e);
		}
		return result;
	}

	/**
	 * Verifica si existe una tarjeta y actualiza su offset en la base de datos.
	 *
	 * @param con        La conexión a la base de datos.
	 * @param issuer     El emisor de la tarjeta.
	 * @param suffix     El sufijo de la tabla (A o B).
	 * @param newOffset  El nuevo offset a establecer.
	 * @param panHash    El hash del número de cuenta primaria (PAN) de la tarjeta.
	 * @param expiryDate La fecha de expiración de la tarjeta.
	 * @param enableLog  Indica si se debe habilitar el registro de logs.
	 * @return true si se actualizó al menos un registro, false en caso contrario.
	 * @throws SQLException Si ocurre un error al ejecutar la consulta.
	 */
	private static boolean checkAndUpdate(Connection con, String issuer, String suffix, String newOffset,
			String panHash, String expiryDate, boolean enableLog) {
		String selectQuery = String.format(Queries.SELECT_EXIST_CARD_WITH_PAN, issuer, suffix);
		try (PreparedStatement psSelect = con.prepareStatement(selectQuery)) {
			psSelect.setString(1, panHash);
			psSelect.setString(2, expiryDate);
			Logger.logLine("selectQuery: " + selectQuery, enableLog);
			Logger.logLine("CONSULTA PREVIA: " + psSelect, enableLog);
			Logger.logLine("CONSULTA PREVIA Metadata:" + psSelect.getMetaData(), enableLog);
			Logger.logLine("CONSULTA PREVIA Parametermetadata:" + psSelect.getParameterMetaData(), enableLog);
			Logger.logLine("CONSULTA PREVIA toString:" + psSelect.toString(), enableLog);

			try (ResultSet rs = psSelect.executeQuery()) {
				if (rs.next()) {
					Logger.logLine("Hay datos rs.toString():" + rs.toString(), enableLog);
					String updateQuery = String.format(Queries.UPDATE_CARD_OFFSET, issuer, suffix);
					try (PreparedStatement psUpdate = con.prepareStatement(updateQuery)) {
						Logger.logLine("updateQuery: " + updateQuery, enableLog);
						Logger.logLine("UPDATE: " + psUpdate, enableLog);
						Logger.logLine("UPDATE Metadata:" + psUpdate.getMetaData(), enableLog);
						Logger.logLine("UPDATE Parametermetadata:" + psUpdate.getParameterMetaData(), enableLog);
						Logger.logLine("UPDATE toString:" + psUpdate.toString(), enableLog);
						psUpdate.setString(1, newOffset);
						psUpdate.setString(2, panHash);
						psUpdate.setString(3, expiryDate);
						int rows = psUpdate.executeUpdate();
						Logger.logLine("UPDATE rows:" + rows, enableLog);
						return rows > 0;
					}
				}
			}
		} catch (SQLException e) {
			EventRecorder.recordEvent(new Exception("SQL: " + e.toString()));
			EventRecorder.recordEvent(e);
		} catch (Exception e) {
			EventRecorder.recordEvent(new Exception("SQL Ex: " + e.toString()));
			EventRecorder.recordEvent(e);
		}
		return false;
	}

	/** CARD STATUS **/

	/**
	 * @param issuer     El emisor de la tarjeta.
	 * @param pan        El hash del numero de cuenta primaria (PAN) de la tarjeta.
	 * @param expiryDate La fecha de expiracion de la tarjeta.
	 * @param enableLog  Indica si se debe habilitar el registro de logs.
	 * @return true Retorna true si existe la tarjeta, de lo contrario retorna
	 *         false.
	 **/
	public static boolean Card(String issuer, String pan, String expiryDate, boolean enableLog) {

		Logger.logLine("Card: ", enableLog);

		boolean result = false;

		try (Connection con = JdbcManager.getConnection(Account.POSTCARD_DATABASE)) {
			String panHash = Crypto.getHashPanCNB(pan);
			Logger.logLine("panHash:" + panHash, enableLog);

			if (cardExist(con, issuer, "A", panHash, expiryDate, enableLog))
				result = true;

			if (cardExist(con, issuer, "B", panHash, expiryDate, enableLog))
				result = true;

			JdbcManager.commit(con);
		} catch (SQLException e) {
			EventRecorder.recordEvent(new Exception("SQL: " + e.toString()));
			EventRecorder.recordEvent(e);
		} catch (Exception e) {
			EventRecorder.recordEvent(new Exception("SQL Ex: " + e.toString()));
			EventRecorder.recordEvent(e);
		}
		return result;
	}

	/**
	 * @param con        La conexion a la base de datos.
	 * @param issuer     El emisor de la tarjeta.
	 * @param suffix     El sufijo de la tabla (A o B).
	 * @param panHash    El hash del numero de cuenta primaria (PAN) de la tarjeta.
	 * @param expiryDate La fecha de expiracion de la tarjeta.
	 * @param enableLog  Indica si se debe habilitar el registro de logs.
	 * @return true Retorna true si existe la tarjeta, de lo contrario retorna
	 *         false.
	 **/
	private static boolean cardExist(Connection con, String issuer, String suffix, String panHash, String expiryDate,
			boolean enableLog) {
		String selectPanQuery = String.format(Queries.SELECT_EXIST_CARD_WITH_PAN, issuer, suffix);
		try (PreparedStatement psSelect = con.prepareStatement(selectPanQuery)) {
			psSelect.setString(1, panHash);
			psSelect.setString(2, expiryDate);
			Logger.logLine("selectPanQuery: " + selectPanQuery, enableLog);
			Logger.logLine("CONSULTA PREVIA: " + psSelect, enableLog);
			Logger.logLine("CONSULTA PREVIA Metadata:" + psSelect.getMetaData(), enableLog);
			Logger.logLine("CONSULTA PREVIA Parametermetadata:" + psSelect.getParameterMetaData(), enableLog);
			Logger.logLine("CONSULTA PREVIA toString:" + psSelect.toString(), enableLog);

			try (ResultSet rs = psSelect.executeQuery()) {
				if (rs.next()) {
					return true;
				}
			}
		} catch (SQLException e) {
			EventRecorder.recordEvent(new Exception("SQL: " + e.toString()));
			EventRecorder.recordEvent(e);
		}

		return false;
	}

	/**
	 * @param issuer     El emisor de la tarjeta.
	 * @param customerId El ID del cliente.
	 * @param enableLog  Indica si se debe habilitar el registro de logs.
	 * @return true Retorna true si el company_name es igual a N - Normal, de lo
	 *         contrario retorna false.
	 **/
	public static boolean Company(String issuer, String customerId, boolean enableLog) {

		boolean result = false;

		try (Connection con = JdbcManager.getConnection(Account.POSTCARD_DATABASE)) {

			String currTableCards = getCurrTableCards(con, issuer, enableLog);
			String companyName = getCompanyName(con, issuer, currTableCards, customerId, enableLog);

			if ("N - Normal".equals(companyName)) {
				result = true;
			}
			Logger.logLine("getCompanyName: " + result, enableLog);

			JdbcManager.commit(con);
		} catch (SQLException e) {
			EventRecorder.recordEvent(new Exception("SQL: " + e.toString()));
			EventRecorder.recordEvent(e);
		} catch (Exception e) {
			EventRecorder.recordEvent(new Exception("SQL Ex: " + e.toString()));
			EventRecorder.recordEvent(e);
		}
		return result;
	}

	/**
	 * @param con        La conexion a la base de datos.
	 * @param issuer     El emisor de la tarjeta.
	 * @param suffix     El sufijo de la tabla (A o B).
	 * @param customerId El ID del cliente.
	 * @param enableLog  Indica si se debe habilitar el registro de logs.
	 * @return true Retorna el valor de company_name.
	 **/
	private static String getCompanyName(Connection con, String issuer, String suffix, String customerId,
			boolean enableLog) {

		String selectCompanyNameQuery = String.format(Queries.SELECT_EXIST_CARD_WITH_COMPANY_NAME, issuer, suffix);
		String companyName = null;

		try (PreparedStatement psSelectCompanyName = con.prepareStatement(selectCompanyNameQuery)) {
			psSelectCompanyName.setString(1, customerId);
			Logger.logLine("selectCompanyNameQuery: " + selectCompanyNameQuery, enableLog);
			Logger.logLine("CONSULTA PREVIA: " + psSelectCompanyName, enableLog);
			Logger.logLine("CONSULTA PREVIA Metadata:" + psSelectCompanyName.getMetaData(), enableLog);
			Logger.logLine("CONSULTA PREVIA Parametermetadata:" + psSelectCompanyName.getParameterMetaData(),
					enableLog);
			Logger.logLine("CONSULTA PREVIA toString:" + psSelectCompanyName.toString(), enableLog);

			try (ResultSet rs = psSelectCompanyName.executeQuery()) {
				if (rs.next()) {
					companyName = rs.getString("company_name");
					Logger.logLine("companyName rs: " + companyName, enableLog);
				}
			}
		} catch (SQLException e) {
			EventRecorder.recordEvent(new Exception("SQL: " + e.toString()));
			EventRecorder.recordEvent(e);
		}

		return companyName;
	}

	/**
	 * @param con       La conexion a la base de datos.
	 * @param issuer    El emisor de la tarjeta.
	 * @param enableLog Indica si se debe habilitar el registro de logs.
	 * @return true Retorna el valor de curr_table_cards.
	 **/
	private static String getCurrTableCards(Connection con, String issuer, boolean enableLog) {

		String selectCurrTableQuery = String.format(Queries.SELECT_CURR_TABLE_CARDS);
		String currTable = null;

		try (PreparedStatement psSelectCurrTable = con.prepareStatement(selectCurrTableQuery)) {
			psSelectCurrTable.setString(1, issuer);
			Logger.logLine("selectCurrTableQuery: " + selectCurrTableQuery, enableLog);
			Logger.logLine("CONSULTA PREVIA: " + psSelectCurrTable, enableLog);
			Logger.logLine("CONSULTA PREVIA Metadata:" + psSelectCurrTable.getMetaData(), enableLog);
			Logger.logLine("CONSULTA PREVIA Parametermetadata:" + psSelectCurrTable.getParameterMetaData(), enableLog);
			Logger.logLine("CONSULTA PREVIA toString:" + psSelectCurrTable.toString(), enableLog);

			try (ResultSet rs = psSelectCurrTable.executeQuery()) {
				if (rs.next()) {
					currTable = rs.getString("curr_table_cards");
					Logger.logLine("currTable rs: " + currTable, enableLog);
				}
			}
		} catch (SQLException e) {
			EventRecorder.recordEvent(new Exception("SQL: " + e.toString()));
			EventRecorder.recordEvent(e);
		}

		return currTable;
	}

	/** CARD STATUS **/

	public static class Account {
		public static final int NUMBER_RESULT_ACCOUNTS = 6;
		public static final int ID = 0;
		public static final int TYPE = 1;
		public static final int CUSTOMER_ID = 2;
		public static final int CUSTOMER_NAME = 3;
		public static final int CUSTOMER_NAME_CNB = 2;
		public static final int CORRECT_PROCESSING_CODE = 4;
		public static final int PROTECTED_CARD_CLASS = 5;
		public static final int FULL_LENGHT_FIELD_102 = 18;
		public static final String TRUE = "true";
		public static final String FALSE = "false";
		public static final String NIL = "NIL";
		public static final String POSTCARD_DATABASE = "postcard";
		public static final String DEFAULT_PROCESSING_CODE = "000000";
		public static final String CUSTOMER_NO_NAME = "**CLIENTE NO ENCONTRADO**";
		public static final String NO_CARD_RECORD = "**ESTA TARJETA NO EXISTE**";
		public static final String NO_ACCOUNT_LINKED = "**ESTA TARJETA NO TIENE UNA CUENTA ASOCIADA**";
		public static final String NO_PROTECTED_CARD_CLASS = "**NO HAY CLASE ASOCIADA A ESTA TARJETA**";
		public static final char ZERO_FILLER = '0';
	}

	/**
	 * Almacena los Store Procedures utilizados en la clase
	 * 
	 * @author Cristian Cardozo
	 *
	 */
	public static class StoreProcedures {
		public static final String GET_PINOFFSET_DATA = "{call GET_PinOffset(?,?,?,?,?,?,?,?,?,?,?,?)}";
		public static final String CM_LOAD_CARD_ACCOUNTS = "{call cm_load_card_accounts(?,?,?)}";
	}

	/**
	 * Define el nombre de las columnas
	 * 
	 * @author Cristian Cardozo
	 *
	 */
	public static class ColumnNames {
		public static final String ACCOUNT_TYPE = "account_type";
		public static final String ACCOUNT_ID = "account_id_encrypted";
		public static final String QUALIFIER = "account_type_qualifier";
		public static final String ACTIVE = "active";
	}

	public static class Queries {
		public static final String SELECT_EXIST_CARD_WITH_PAN = "SELECT pan FROM pc_cards_%s_%s WITH (NOLOCK) WHERE pan = ? and expiry_date = ?";
		public static final String UPDATE_CARD_OFFSET = "UPDATE pc_cards_%s_%s SET pvv_or_pin_offset = ? WHERE pan = ? and expiry_date = ?";
		public static final String SELECT_EXIST_CARD_WITH_COMPANY_NAME = "SELECT company_name FROM pc_customers_%s_%s WITH (NOLOCK) WHERE customer_id = ?";
		public static final String SELECT_CURR_TABLE_CARDS = "SELECT curr_table_cards FROM pc_issuers WITH (NOLOCK) WHERE issuer_nr = ?";
	}

}
