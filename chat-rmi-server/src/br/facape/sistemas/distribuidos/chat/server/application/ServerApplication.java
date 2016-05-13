package br.facape.sistemas.distribuidos.chat.server.application;

import org.apache.log4j.Logger;

import br.facape.sistemas.distribuidos.chat.server.application.ui.ChatWindow;
import br.facape.sistemas.distribuidos.chat.server.application.ui.interfaces.IWindow;
import br.facape.sistemas.distribuidos.chat.utils.application.interfaces.IApplication;

public class ServerApplication implements IApplication {

	private static final Logger LOGGER = Logger.getLogger(ServerApplication.class);
	
	private IWindow chatWindow;
	
	public ServerApplication() {
		this.chatWindow = new ChatWindow();
	}
	
	@Override
	public void start() {
		try {
			LOGGER.info("Iniciando o servidor...");
			this.chatWindow.start();
			LOGGER.info("Servidor iniciado com sucesso!");
		}catch(Exception e) {
			LOGGER.error(e);
			this.close();
		}
	}

	@Override
	public void close() {
		LOGGER.info("Desligando o servidor...");
		System.exit(0);
	}
	
}
