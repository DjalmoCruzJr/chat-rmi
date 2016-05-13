package br.facape.sistemas.distribuidos.chat.client.application;

import org.apache.log4j.Logger;

import br.facape.sistemas.distribuidos.chat.client.application.ui.ChatWindow;
import br.facape.sistemas.distribuidos.chat.client.application.ui.interfaces.IWindow;
import br.facape.sistemas.distribuidos.chat.utils.application.interfaces.IApplication;

public class ClientApplication implements IApplication {

	private static final Logger LOGGER = Logger.getLogger(ClientApplication.class);
	
	private IWindow chatWindow;
	
	public ClientApplication() {
		this.chatWindow = new ChatWindow();
	}
	
	@Override
	public void start() {
		try {
			LOGGER.info("Iniciando o cliente...");
			this.chatWindow.start();
			LOGGER.info("Cliente iniciado com sucesso!");
		}catch(Exception e) {
			LOGGER.error(e);
			this.close();
		}
	}

	@Override
	public void close() {
		System.exit(0);
	}
	
}
