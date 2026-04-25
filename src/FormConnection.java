package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

public class FormConnection {
	private static String firstNonBlank(String... values) {
		for (String value : values) {
			if (value != null && !value.trim().isEmpty()) {
				return value.trim();
			}
		}
		return null;
	}

	private static Properties loadEnvProperties() {
		String configuredPath = System.getenv("LABLOGIX_JAVA_ENV");
		String[] candidates = new String[] {
			configuredPath,
			".env.java",
			".env",
			"src/.env"
		};

		Properties props = new Properties();
		for (String candidate : candidates) {
			if (candidate == null || candidate.trim().isEmpty()) {
				continue;
			}

			File file = new File(candidate);
			if (!file.exists()) {
				continue;
			}

			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				props.load(br);
				return props;
			} catch (Exception e) {
				throw new RuntimeException("Failed reading env file: " + file.getPath(), e);
			}
		}

		throw new RuntimeException("No env file found for Java DB connection. Tried LABLOGIX_JAVA_ENV, .env.java, .env, src/.env");
	}

	public static Connection connectDb() {
		Properties props = loadEnvProperties();
		String db_url = firstNonBlank(props.getProperty("DB_URL"), props.getProperty("JDBC_URL"));
		String db_user = firstNonBlank(props.getProperty("DB_USER"), props.getProperty("MYSQL_USER"));
		String db_password = firstNonBlank(props.getProperty("DB_PASSWORD"), props.getProperty("MYSQL_PASSWORD"));

		if (db_url == null) {
			String host = firstNonBlank(props.getProperty("DB_HOST"), "localhost");
			String port = firstNonBlank(props.getProperty("DB_PORT"), "3306");
			String dbName = firstNonBlank(props.getProperty("DB_NAME"));
			if (dbName != null) {
				db_url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
			}
		}

		if (db_url == null || db_user == null || db_password == null) {
			throw new RuntimeException("Missing DB config. Required keys: DB_URL (or DB_HOST/DB_PORT/DB_NAME), DB_USER, DB_PASSWORD");
		}

		try {
			return DriverManager.getConnection(db_url, db_user, db_password);
		} catch(Exception e) {
			throw new RuntimeException("Database connection failed", e);
		}
	}

	public static Statement connect() {
		try {
			return connectDb().createStatement();
		} catch(Exception e) {
			throw new RuntimeException("Statement creation failed", e);
		}
	}
}
