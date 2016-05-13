package br.facape.sistemas.distribuidos.chat.server.application.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import br.facape.sistemas.distribuidos.chat.server.application.ui.interfaces.IWindow;
import br.facape.sistemas.distribuidos.chat.server.services.ChatService;
import br.facape.sistemas.distribuidos.chat.server.utils.ServicesUtils;
import br.facape.sistemas.distribuidos.chat.utils.beans.Message;
import br.facape.sistemas.distribuidos.chat.utils.beans.User;
import br.facape.sistemas.distribuidos.chat.utils.services.interfaces.IChatService;

public class ChatWindow extends JFrame implements IWindow {

	private static final long serialVersionUID = 5901508969670833304L;
	private static final Logger LOGGER = Logger.getLogger(ChatWindow.class);
	private static final String WINDOWS_LOOK_AND_FEEL = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

	private Registry registry;
	private IChatService chatService;
	private String serviceName;
	private int servicePort;
	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu mnSobre;
	private JMenuItem mntmSobreSistema;
	private JMenu mnSair;
	private JMenuItem mntmSairDoSistema;
	private JMenu mnServicos;
	private JMenuItem mntmStartService;
	private JSeparator separator;
	private JMenuItem mntmStopService;
	private JMenu mnConfiguracoes;
	private JMenuItem mntmConfiguracoesServicos;
	private JScrollPane scrollPane;
	private JList<String> listConnectedUsers;
	private JScrollPane scrollPane_1;
	private JTextArea textAreaMessages;
	private JPanel panel;
	private JLabel lblStatus;

	public ChatWindow() {
		setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		this.contentPane = new JPanel();
		this.contentPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.contentPane.setBackground(Color.WHITE);
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		this.scrollPane = new JScrollPane();
		this.scrollPane.setViewportBorder(
				new TitledBorder(null, "Usu\u00E1rios Conectados", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.contentPane.add(this.scrollPane, BorderLayout.WEST);

		this.listConnectedUsers = new JList<String>();
		this.listConnectedUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.scrollPane.setViewportView(this.listConnectedUsers);

		this.scrollPane_1 = new JScrollPane();
		this.scrollPane_1.setViewportBorder(
				new TitledBorder(null, "Mensagens", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.contentPane.add(this.scrollPane_1, BorderLayout.CENTER);

		this.textAreaMessages = new JTextArea();
		this.textAreaMessages.setEditable(false);
		this.scrollPane_1.setViewportView(this.textAreaMessages);

		this.panel = new JPanel();
		this.panel.setBackground(Color.WHITE);
		this.panel.setBorder(new TitledBorder(null, "Barra de status", TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(59, 59, 59)));
		this.contentPane.add(this.panel, BorderLayout.SOUTH);
		this.panel.setLayout(new BorderLayout(0, 0));

		this.lblStatus = new JLabel("");
		this.panel.add(this.lblStatus, BorderLayout.CENTER);
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setAlwaysOnTop(true);
		setTitle("Chat RMI Server v1.0");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 746, 502);

		this.menuBar = new JMenuBar();
		setJMenuBar(this.menuBar);

		this.mnServicos = new JMenu("Chat");
		this.menuBar.add(this.mnServicos);

		this.mntmStartService = new JMenuItem("Iniciar servi\u00E7o");
		this.mntmStartService.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startService();
			}
		});
		this.mnServicos.add(this.mntmStartService);

		this.separator = new JSeparator();
		this.mnServicos.add(this.separator);

		this.mntmStopService = new JMenuItem("Parar Servi\u00E7o");
		this.mntmStopService.setEnabled(false);
		this.mntmStopService.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopService();
			}
		});
		this.mnServicos.add(this.mntmStopService);

		this.mnConfiguracoes = new JMenu("Configura\u00E7\u00F5es");
		this.menuBar.add(this.mnConfiguracoes);

		this.mntmConfiguracoesServicos = new JMenuItem("Configura\u00E7\u00F5es do servi\u00E7o");
		this.mntmConfiguracoesServicos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SettingsWindow settingsWindow = new SettingsWindow();
				settingsWindow.start();
			}
		});
		this.mnConfiguracoes.add(this.mntmConfiguracoesServicos);

		this.mnSobre = new JMenu("Sobre");
		this.menuBar.add(this.mnSobre);

		this.mntmSobreSistema = new JMenuItem("Sobre o sistema");
		this.mntmSobreSistema.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AboutWindow aboutWindow = new AboutWindow();
				aboutWindow.start();
			}
		});
		this.mnSobre.add(this.mntmSobreSistema);

		this.mnSair = new JMenu("Sair");
		this.menuBar.add(this.mnSair);

		this.mntmSairDoSistema = new JMenuItem("Sair do sistema");
		this.mntmSairDoSistema.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});

		this.mnSair.add(this.mntmSairDoSistema);
		try {
			lblStatus.setText(String.format("[IP do Servidor: %s] - [Status do Serviço: %s]",
					InetAddress.getLocalHost().getHostAddress(), "DESATIVADO"));
		} catch (UnknownHostException e) {
			LOGGER.info(e);
		}

		try {
			UIManager.setLookAndFeel(WINDOWS_LOOK_AND_FEEL);
		} catch (ClassNotFoundException e) {
			LOGGER.error(e);
		} catch (InstantiationException e) {
			LOGGER.error(e);
		} catch (IllegalAccessException e) {
			LOGGER.error(e);
		} catch (UnsupportedLookAndFeelException e) {
			LOGGER.error(e);
		}
	}

	private void stopService() {
		if (this.registry != null) {
			try {
				this.registry.unbind(this.serviceName);
				this.registry = null;
				this.serviceName = null;
				this.servicePort = 0;
				updateConnectedUsersList(false);
				updateMessagesList(false);
				clearAllFields();
				String title = "MENSAGEM";
				String msg = "Serviço de chat parado com sucesso!";
				JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);
				mntmStartService.setEnabled(true);
				mntmStopService.setEnabled(false);
				lblStatus.setText(String.format("[IP do Servidor: %s] - [Status do Serviço: %s]",
						InetAddress.getLocalHost().getHostAddress(), "DESATIVADO"));
				LOGGER.info(msg);
			} catch (RemoteException | NotBoundException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
				LOGGER.info(e);
			} catch (UnknownHostException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
				LOGGER.info(e);
			}
		}
	}

	private void clearAllFields() {
		this.listConnectedUsers.setListData(new String[]{});
		this.textAreaMessages.setText("");
	}
	
	private void startService() {
		try {
			this.serviceName = ServicesUtils.loadProperty(ServicesUtils.PROPERTY_SERVICE_NAME);
			this.servicePort = Integer.parseInt(ServicesUtils.loadProperty(ServicesUtils.PROPERTY_SERVICE_PORT));
			if (this.serviceName == null || this.servicePort == 0) {
				String title = "ERRO";
				String msg = "Não foi possível carregar as configurações do serviço. VERIFIQUE!";
				JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
				return;
			}
			this.registry = LocateRegistry.createRegistry(this.servicePort);
			this.chatService = new ChatService();
			this.registry.rebind(this.serviceName, this.chatService);
			updateConnectedUsersList(true);
			updateMessagesList(true);
			String title = "MENSAGEM";
			String msg = "Serviço de chat iniciado com sucesso!";
			JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);
			mntmStartService.setEnabled(false);
			mntmStopService.setEnabled(true);
			lblStatus.setText(String.format(
					"[IP do Servidor: %s] - [Status do Serviço: %s] - [Nome do Serviço: %s] - [Porta do Serviço: %d]",
					InetAddress.getLocalHost().getHostAddress(), "ATIVADO", serviceName, servicePort));
			LOGGER.info(String.format("%s [nome:%s, porta: %s]", msg, serviceName, servicePort));
		} catch (IOException e) {
			LOGGER.error(e);
			JOptionPane.showMessageDialog(this, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void updateConnectedUsersList(boolean stopFlag) {
		if (this.chatService == null)
			return;
		new Thread() {
			public void run() {
				while (stopFlag == true) {
					try {
						List<User> users = chatService.getConnectedUsers();
						if (users == null)
							return;
						String[] data = new String[users.size()];
						for (int i = 0; i < users.size(); i++) {
							data[i] = String.format("%s (%s)", users.get(i).getNickname().toUpperCase(),
									users.get(i).getHost());
						}
						listConnectedUsers.setListData(data);
						sleep(1000);
					} catch (RemoteException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	private void updateMessagesList(boolean stopFlag) {
		if (this.chatService == null)
			return;
		new Thread() {
			public void run() {
				while (stopFlag == true) {
					try {
						List<Message> messages = chatService.getMessages();
						if (messages == null)
							return;
						textAreaMessages.setText("");
						for (Message m : messages) {
							textAreaMessages.append(m.getText() + "\n");
						}
						sleep(1000);
					} catch (RemoteException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	@Override
	public void start() {
		this.setVisible(true);
	}

	@Override
	public void close() {
		String msg = "Ao sair do sistema todos os serviços ativos serão finalizados. \nDeseja Continuar ?";
		String title = "AVISO!";
		int result = JOptionPane.showConfirmDialog(ChatWindow.this, msg, title, JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if (result != JOptionPane.OK_OPTION)
			return;
		stopService();
		this.dispose();
	}
}
