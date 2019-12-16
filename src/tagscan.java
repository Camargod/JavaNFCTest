

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.math.BigInteger;
import javax.smartcardio.*;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class tagscan extends JFrame {
	static String resultadoFinal;
	static String resultado;
	static String UIDText;
	static Timer timer;
	public static JLabel lblNewLabel,lblId;
	
	
	
	 	public tagscan() {
		super("Scan");
		
		setBounds(150,50, 788, 507);
		setResizable(false);
		getContentPane().setBackground(new Color(220,220,220));
		
		getContentPane().setLayout(null);
		
		JLabel lblInsiraSeuCarto = new JLabel("Insira seu cart\u00E3o");
		lblInsiraSeuCarto.setFont(new Font("Tahoma", Font.PLAIN, 36));
		lblInsiraSeuCarto.setBounds(0, 0, 266, 76);
		getContentPane().add(lblInsiraSeuCarto);
		
	    lblNewLabel = new JLabel("txtAcesso");
		lblNewLabel.setVisible(false);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 90));
		lblNewLabel.setBounds(0, 130, 784, 192);
		
		getContentPane().add(lblNewLabel);
		
		JButton btnLer = new JButton("Ler");
		
		btnLer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardRead();
				lblNewLabel.setVisible(true);
				lblNewLabel.setText(resultado);
			}
		});
		btnLer.setBounds(346, 350, 89, 23);
		getContentPane().add(btnLer);
		
		lblId = new JLabel("ID:");
		lblId.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblId.setBounds(432, 281, 139, 23);
		getContentPane().add(lblId);
	}
	
	 	 public static class RemindTask extends TimerTask {
	         public void run() {
	             CardRead();
	             UIDCheck();
	         }
	     }
	
	static String bin2hex(byte[] data) 
	{
	    return String.format("%0" + (data.length * 2) + "X", new BigInteger(1,data));
	}
	
 
	
	
 public static void CardRead()
 {

	 try {
	        
		   // Lista de dispositivos terminais
		   TerminalFactory factory = TerminalFactory.getDefault();
		   List<CardTerminal> terminals = factory.terminals().list();
		 //  System.out.println("Terminals: " + terminals);

		   // Define o terminal que sera usado
		   CardTerminal terminal = terminals.get(0);

		   // Faz a conexão com o cartão
		   Card card = terminal.connect("*");
		 //  System.out.println("Card: " + card);
		   CardChannel channel = card.getBasicChannel();

		   // Manda comando de teste em byte
		   ResponseAPDU response = channel.transmit(new CommandAPDU( new byte[] { (byte) 0xFF, (byte) 0xCA, (byte) 0x00, (byte) 0x00, (byte) 0x04}));
		//   System.out.println("Response: " + response.toString());
		   
		 //  if (response.getSW1() == 0x63 && response.getSW2() == 0x00)  System.out.println("Failed");
		   
		   resultado = (bin2hex(response.getData()));
		   UIDCheck();
		   // disconecta o cartão
		   card.disconnect(true);
		   System.out.println(resultado + "/" + resultadoFinal);

		  } catch(Exception e) {

		   resultado = "0";

		  }
 }
 
 
 
 
 
 
		 public static void UIDCheck()
				 {
			 if(resultado == null)
			 {}
			 else {
					 if(resultado != resultadoFinal)
					 {
							 resultadoFinal = resultado;
							 if(resultadoFinal.equals("6A08290C"))
							 {					   
								 UIDText = "Acesso Liberado";
								 lblNewLabel.setVisible(true);
								 lblNewLabel.setText(UIDText);
								 lblId.setText("UID: "+resultadoFinal);
							 }
							 else
							 {
								 UIDText ="Acesso Negado";
								 lblNewLabel.setVisible(true);
								 lblNewLabel.setText(UIDText);
								 lblId.setText("UID: "+resultadoFinal);
							 }
							 if(resultadoFinal.equals("0"))
							 {
								 lblNewLabel.setVisible(false);
							 }
					 }
					 
			 }
				 }
		 

		 
		 
		 
		 public static void main(String[] args) {
			 	timer = new Timer();
		        timer.schedule(new RemindTask(),1000L, 100L);
			  	tagscan tag = new tagscan();
				tag.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();
				tag.setLocation((tela.width-tag.getSize().width)/2,(tela.height-tag.getSize().height)/2);
				tag.setUndecorated(true);
				tag.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
				tag.setVisible(true);
			 }
}