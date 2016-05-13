package br.facape.sistemas.distribuidos.chat.client.application;

import br.facape.sistemas.distribuidos.chat.utils.application.interfaces.IApplication;

public class Main {

	public static void main(String[] args) {
		IApplication app = new ClientApplication();
		app.start();
	}
	
}
