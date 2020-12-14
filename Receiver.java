
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Receiver {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {

		// FileOutputStream file = new FileOutputStream(name);

		String g = "";
		JFrame f = new JFrame("Receiver");
		f.setSize(340, 230);
		f.setLocation(700, 200);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		JPanel panel = new JPanel();
		panel.setLayout(null);
		f.add(panel);

		final JTextArea textArea1 = new JTextArea("IP Address:", 1, 1); // first
																		// text
																		// area,
																		// just
																		// to
																		// show
																		// where
																		// to
																		// input
																		// IP
																		// address
		textArea1.setBounds(55, 10, 70, 20);
		textArea1.setEditable(false);
		panel.add(textArea1);
		final JTextField textArea2 = new JTextField(" ", 10); // second text
																// area, where
																// the user
																// inputs the IP
																// address
		textArea2.setBounds(130, 10, 155, 20);
		panel.add(textArea2);
		final JTextArea textArea3 = new JTextArea("Sender Port Number:", 1, 1); // third
																				// text
																				// area,
																				// showing
																				// where
																				// to
																				// input
																				// the
																				// receiver
																				// port
																				// number
		textArea3.setBounds(27, 40, 125, 20);
		textArea3.setEditable(false);
		panel.add(textArea3);
		final JTextField textArea4 = new JTextField(" ", 10); // fourth text
																// area, where
																// to input the
																// port number
		textArea4.setBounds(157, 40, 155, 20);
		panel.add(textArea4);
		final JTextArea textArea5 = new JTextArea("Receiver Port Number:", 1, 1); // Fifth
																					// text
																					// area
																					// showing
																					// where
																					// to
																					// input
																					// the
																					// sender
																					// port
																					// number
		textArea5.setBounds(27, 70, 125, 20);
		textArea5.setEditable(false);
		panel.add(textArea5);
		final JTextField textArea6 = new JTextField(" ", 10); // sixth text
																// area,
																// where to
																// input the
																// port number
		textArea6.setBounds(157, 70, 155, 20);
		panel.add(textArea6);
		final JTextArea textArea7 = new JTextArea("Name of File to Write to:", 1, 1); // seventh
																						// text
																						// area
																						// showing
																						// where
																						// to
																						// input
																						// the
																						// file
																						// name
		textArea7.setBounds(27, 100, 130, 20);
		textArea7.setEditable(false);
		panel.add(textArea7);
		final JTextField textArea8 = new JTextField(" ", 10); // eighth text
																// area, where
																// to input the
																// file name
		textArea8.setBounds(162, 100, 150, 20);
		panel.add(textArea8);
		final JTextArea textArea9 = new JTextArea("# of Received in-order Packets:", 1, 1); // ninth
																							// text
																							// area
																							// showing
																							// where
																							// to
																							// input
																							// mds
		textArea9.setBounds(32, 130, 170, 20);
		textArea9.setEditable(false);
		panel.add(textArea9);
		final JTextArea textArea10 = new JTextArea(" 3", 1, 1); // tenth text
																// area, where
																// to input mds
		textArea10.setBounds(207, 130, 100, 20);
		textArea10.setEditable(false);
		panel.add(textArea10);
		String[] b = { "Reliable", "Unreliable" };
		final JComboBox<Object> button = new JComboBox<Object>(b); // reliable/unreliable
																	// button
		button.setBounds(90, 160, 90, 20);
		panel.add(button);

		button.addActionListener(new ActionListener() { // reliable/unreliable
														// button action
														// listener

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(f, "Mode Selected: " + button.getSelectedItem().toString());
				// gr = button.getSelectedItem().toString();
			}

		});
		String r = button.getSelectedItem().toString();

		int inOrderPckts = 0;
		f.setVisible(true);
		while (true) {

			if (textArea2.getText().length() != 1 && textArea4.getText().length() != 1
					&& textArea6.getText().length() != 1 && textArea8.getText().length() != 1
					&& textArea8.getText().endsWith(".txt")) {

				while (true) {
					String name = textArea8.getText().trim();
					int portNum = Integer.parseInt(textArea6.getText().trim());
					int count = 0;

					PrintStream out = new PrintStream(new FileOutputStream(name));

					DatagramSocket socket = new DatagramSocket(portNum);

					byte[] firstsendData = new byte[5000];
					byte[] firstrcvData = new byte[5000];

					count++;
					DatagramPacket pcketReceived = new DatagramPacket(firstrcvData, firstrcvData.length);
					socket.receive(pcketReceived);
					System.out.println("Data received: " + 3);

					firstsendData = pcketReceived.getData();
					String firstLine = new String(firstsendData, 0, pcketReceived.getLength());

					// handshake
					String mbsTemp = "";
					char[] chars = firstLine.toCharArray();
					for (int i = 0; i < firstLine.length(); i++) {
						if (Character.isDigit(chars[i])) {
							mbsTemp = mbsTemp + chars[i];
						}
					}

					Integer senderPort = Integer.parseInt(textArea4.getText().trim());
					String IPAdd = textArea2.getText().trim();

					int mbs = Integer.parseInt(mbsTemp);

					if (mbs > 0) {

						System.out.println("GOOD   " + mbs);

						firstsendData = firstLine.getBytes();
						DatagramPacket packet = new DatagramPacket(firstsendData, firstsendData.length,
								InetAddress.getByName(IPAdd), senderPort);
						socket.send(packet);

					}

					// set up byte arrays for data that is being sent and
					// received
					byte[] dataReceived = new byte[mbs]; // to be changed
															// to MDs from
															// gui
					byte[] dataSending = new byte[mbs]; // to be changed
														// to MDs from
														// gui
					int x = 1;
					// Infinite loop
					while (true) {
						r = button.getSelectedItem().toString();
						// Get received packet
						count++;
						DatagramPacket packetReceived = new DatagramPacket(dataReceived, dataReceived.length);
						socket.receive(packetReceived);
						System.out.println("Data received: " + x);
						x++;
						dataReceived = packetReceived.getData();
						String line = new String(dataReceived, 0, packetReceived.getLength());

						// Get the message from the packet
						// String line =
						// ByteBuffer.wrap(packetReceived.getData()).toString();
						String[] lineData = line.split(" ", 2);

						// why wont this if statement work
						System.out.println(lineData[1] + "   " + r);

						if (lineData[1].trim().equals("EOT")) {
							InetAddress IPAddress = InetAddress.getByName(IPAdd); // from
																					// gui
							int port = senderPort; // from gui

							String message = "ACK " + "EOT";
							dataSending = message.getBytes();
							DatagramPacket packet = new DatagramPacket(dataSending, dataSending.length, IPAddress,
									port);
							socket.send(packet);
							break;
						}

						// Sends packet data back

						if (r == "Reliable") {
							out.println(lineData[1]);

							// Get packet's IP and port
							InetAddress address = InetAddress.getByName(textArea2.getText().trim()); // IP
							int port = Integer.parseInt(textArea4.getText().trim()); // address
							// System.out.println("input:" + address + " " +
							// port);
							// System.out.println("received:" + address + " " +
							// port);
							String message = "ACK " + lineData[0];
							dataSending = message.getBytes();
							DatagramPacket packet = new DatagramPacket(dataSending, dataSending.length, address, port);
							socket.send(packet);
							inOrderPckts++;
						} else if (r == "Unreliable" && count != 10) {
							out.println(lineData[1]);

							// Get packet's IP and port
							InetAddress IPAddress = InetAddress.getByName(IPAdd); // from
							// gui
							int port = senderPort; // from gui

							String message = "ACK " + lineData[0];
							dataSending = message.getBytes();
							DatagramPacket packet = new DatagramPacket(dataSending, dataSending.length, IPAddress,
									port);
							socket.send(packet);
							inOrderPckts++;
						}
						if (count == 10) {
							count = 0;
						}
						System.out.println("In order times " + inOrderPckts);
						textArea10.setText(String.valueOf(inOrderPckts));
					}

					socket.close();
				}
			}
		}
	}

}
