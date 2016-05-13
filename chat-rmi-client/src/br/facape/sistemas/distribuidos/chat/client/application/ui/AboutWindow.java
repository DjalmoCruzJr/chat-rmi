package br.facape.sistemas.distribuidos.chat.client.application.ui;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import br.facape.sistemas.distribuidos.chat.client.application.ui.interfaces.IWindow;

public class AboutWindow extends JDialog implements IWindow {

	private static final long serialVersionUID = 5769185269969505881L;

	private JPanel contentPane;
	private JLabel lblChatRmiServer;
	private JLabel lblChatRmiClient;
	private JLabel lblDjalmocrujr;
	private JTextPane txtpnSistemaDesenvolvidoComo;

	public AboutWindow() {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setAlwaysOnTop(true);
		setModal(true);
		setResizable(false);
		setTitle("Chat RMI Client v1.0  [Sobre o sistema]");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 449, 324);
		this.contentPane = new JPanel();
		this.contentPane.setBackground(Color.WHITE);
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		this.contentPane.setLayout(null);

		this.lblChatRmiServer = new JLabel("Vers\u00E3o: 1.0");
		this.lblChatRmiServer.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
		this.lblChatRmiServer.setBounds(16, 39, 80, 19);
		this.contentPane.add(this.lblChatRmiServer);

		this.lblChatRmiClient = new JLabel("Chat RMI Client");
		this.lblChatRmiClient.setFont(new Font("Trebuchet MS", Font.BOLD, 20));
		this.lblChatRmiClient.setBounds(16, 8, 144, 24);
		this.contentPane.add(this.lblChatRmiClient);

		this.lblDjalmocrujr = new JLabel("\u00A9 2016 - djalmocruzjr & luizmariodev - Todos os direitos reservados");
		this.lblDjalmocrujr.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		this.lblDjalmocrujr.setBounds(36, 272, 371, 15);
		this.contentPane.add(this.lblDjalmocrujr);

		this.txtpnSistemaDesenvolvidoComo = new JTextPane();
		this.txtpnSistemaDesenvolvidoComo.setText(
				"\r\nSistema desenvolvido como requisito parcial para forma\u00E7\u00E3o da 1\u00AA nota da Disciplina de Sistemas 2016.1 Distribuidos da Faculdade de Ci\u00EAncias Aplicadas e Sociais de Petrolina - FACAPE.\r\n\r\nAlunos: DJALMO PEREIRA DA CRUZ JUNIOR; \r\n               LUIZ MARIO FERREIRA CAVALCANTE;\r\n\r\nProfessor: THOMAS RABELO;");
		this.txtpnSistemaDesenvolvidoComo.setBorder(new TitledBorder(null, "Informa\u00E7\u00F5es do sistema",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.txtpnSistemaDesenvolvidoComo.setEditable(false);
		this.txtpnSistemaDesenvolvidoComo.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.txtpnSistemaDesenvolvidoComo.setBounds(16, 66, 401, 194);
		this.contentPane.add(this.txtpnSistemaDesenvolvidoComo);
	}

	@Override
	public void start() {
		this.setVisible(true);
	}

	@Override
	public void close() {
		this.dispose();
	}
}
