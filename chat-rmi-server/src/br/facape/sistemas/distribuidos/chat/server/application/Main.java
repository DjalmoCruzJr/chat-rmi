package br.facape.sistemas.distribuidos.chat.server.application;

import br.facape.sistemas.distribuidos.chat.utils.application.interfaces.IApplication;

public class Main {

	public static void main(String[] args) {
		IApplication application = new ServerApplication();
		application.start();
	}

}
