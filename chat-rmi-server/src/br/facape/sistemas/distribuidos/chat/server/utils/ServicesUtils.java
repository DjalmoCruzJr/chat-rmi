package br.facape.sistemas.distribuidos.chat.server.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public abstract class ServicesUtils {

	private static final Logger LOGGER = Logger.getLogger(ServicesUtils.class);
	private static final String SERVICES_CONFIG_FILE = "./resources/service-config.properties";

	public static final String PROPERTY_SERVICE_NAME = "br.facape.sistemas.distribuidos.server.service.chat.name";
	public static final String PROPERTY_SERVICE_PORT = "br.facape.sistemas.distribuidos.server.service.chat.port";

	public static final boolean saveProperty(String propertyName, String propertyValue) throws FileNotFoundException, IOException {
		boolean result = false;
		LOGGER.info(String.format("Tentando salvar novas configura��es do servi�o: %s=%s", propertyName, propertyValue));
		Properties properties = new Properties();
		properties.load(new FileInputStream(SERVICES_CONFIG_FILE));
		result = properties.setProperty(propertyName, propertyValue) == null;
		LOGGER.info("Novas configura��es gravadas com sucesso!");
		return result;
	}

	public static final String loadProperty(String propertyName) throws FileNotFoundException, IOException {
		String result = null;
		LOGGER.info(String.format("Tentando carregar as configura��es do servi�o: %s", propertyName));
		Properties properties = new Properties();
		properties.load(new FileInputStream(SERVICES_CONFIG_FILE));
		result = properties.getProperty(propertyName);
		LOGGER.info("Configura��es carregadas com sucesso!");
		return result;
	}

}
