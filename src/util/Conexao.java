package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

		
		private static final String URL =
				"jdbc:mariadb://localhost:3306/";
		private static final String USUARIO = "";
		private static final String SENHA = "";
		
		public static Connection abrir() throws SQLException {
			return DriverManager.getConnection(URL, USUARIO, SENHA);
		}
	}