import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class Sender {

	static int port, rcvPort;
	static InetAddress address;
	static DatagramSocket socket = null;
	static DatagramPacket packet;
	byte[] sendBuf = new byte[2000];
	static int sequenceNum = 0; // alternates between 1 and 0

	public static void main(String[] args) {
		JFrame f = new JFrame("Sender");
		f.setSize(305, 500);
		f.setLocation(300, 200);
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
		textArea1.setBounds(37, 10, 70, 20);
		textArea1.setEditable(false);
		panel.add(textArea1);
		final JTextField textArea2 = new JTextField("", 10); // second text
																// area, where
																// the user
																// inputs the IP
																// address
		textArea2.setBounds(112, 10, 155, 20);
		panel.add(textArea2);
		final JTextArea textArea3 = new JTextArea("Receiver Port Number:", 1, 1); // third
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
		textArea3.setBounds(10, 40, 125, 20);
		textArea3.setEditable(false);
		panel.add(textArea3);
		final JTextField textArea4 = new JTextField("", 10); // fourth text
																// area, where
																// to input the
																// port number
		textArea4.setBounds(140, 40, 155, 20);
		panel.add(textArea4);
		final JTextArea textArea5 = new JTextArea("Sender Port Number:", 1, 1); // Fifth
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
		textArea5.setBounds(10, 70, 125, 20);
		textArea5.setEditable(false);
		panel.add(textArea5);
		final JTextField textArea6 = new JTextField("", 10); // sixth text area,
																// where to
																// input the
																// port number
		textArea6.setBounds(140, 70, 155, 20);
		panel.add(textArea6);
		final JTextArea textArea7 = new JTextArea("File Path:", 1, 1); // seventh
																		// text
																		// area
																		// showing
																		// where
																		// to
																		// input
																		// the
																		// file
																		// name
		textArea7.setBounds(32, 100, 80, 20);
		textArea7.setEditable(false);
		panel.add(textArea7);
		final JTextField textArea8 = new JTextField("", 10); // eighth text
																// area, where
																// to input the
																// file name
		textArea8.setBounds(117, 100, 155, 20);
		panel.add(textArea8);
		final JButton button = new JButton("Transfer"); // Transfer button
		button.setBounds(108, 130, 90, 20);
		panel.add(button);
		final JTextArea textArea9 = new JTextArea("MDS:", 1, 1); // ninth text
																	// area
																	// showing
																	// where to
																	// input mds
		textArea9.setBounds(52, 160, 40, 20);
		textArea9.setEditable(false);
		panel.add(textArea9);
		final JTextField textArea10 = new JTextField("", 10); // tenth text
																// area, where
																// to input mds
		textArea10.setBounds(97, 160, 155, 20);
		panel.add(textArea10);
		final JTextArea textArea11 = new JTextArea("Timeout (ms):", 1, 1); // Eleventh
																			// text
																			// area
																			// showing
																			// timeout
		textArea11.setBounds(70, 190, 80, 20);
		textArea11.setEditable(false);
		panel.add(textArea11);
		final JTextField textArea12 = new JTextField("", 10); // twelfth text
																// area showing
																// timeout
																// number
		textArea12.setBounds(155, 190, 80, 20);
		panel.add(textArea12);
		final JTextArea textArea13 = new JTextArea("Transmission Time:", 1, 1); // thirteenth
																				// text
																				// area
																				// showing
																				// where
																				// transmission
																				// time
																				// is
		textArea13.setBounds(52, 220, 120, 20);
		textArea13.setEditable(false);
		panel.add(textArea13);
		final JTextArea textArea14 = new JTextArea("N/A", 1, 1); // fourteenth
																	// text area
																	// showing
																	// transmission
																	// time
		textArea14.setBounds(177, 220, 80, 20);
		textArea14.setEditable(false);
		panel.add(textArea14);
		final JTextArea textArea15 = new JTextArea("", 100, 100); // thirteenth
																	// text area
																	// showing
																	// where
																	// transmission
																	// time is
		textArea15.setBounds(5, 250, 290, 200);
		textArea15.setEditable(false);
		textArea15.setLineWrap(true);
		textArea15.setWrapStyleWord(true);
		// panel.add(textArea15);
		JScrollPane scroll = new JScrollPane(textArea15);
		scroll.setBounds(5, 250, 290, 200);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		panel.add(scroll);

		button.addActionListener(new ActionListener() { // Transfer button
														// action listener

			@Override
			public void actionPerformed(ActionEvent e) {
				long startTime = System.currentTimeMillis();
				if (textArea2.getText().length() == 0) {
					// System.out.println("Error with IP Address");
					textArea15.append("no IP Address\n");
					return;
				}

				try {
					// The file being transferred
					String filename = textArea8.getText(); // file
															// name
					ArrayList<String> datasent = new ArrayList<String>();

					// open a file and read lines

					BufferedReader reader = new BufferedReader(new FileReader(filename));
					// System.out.println("File is found");
					textArea15.append("File is found\n");
					String line;
					port = Integer.parseInt(textArea6.getText());
					rcvPort = Integer.parseInt(textArea4.getText());
					socket = new DatagramSocket(port);
					socket.setSoTimeout(5000); // timeout time from gui

					address = InetAddress.getByName(textArea2.getText()); // IP

					// Handshake
					byte[] firstsendData = new byte[5000];
					byte[] firstrcvData = new byte[5000];

					String msg = "<MDS=" + textArea10.getText() + ">";
					firstsendData = msg.getBytes();
					packet = new DatagramPacket(firstsendData, firstsendData.length, address,
							Integer.parseInt(textArea4.getText())); // port
					socket.send(packet);

					packet = new DatagramPacket(firstrcvData, firstrcvData.length);
					socket.receive(packet);

					firstrcvData = packet.getData();
					String mds_ = new String(firstrcvData, 0, packet.getLength());
					int mds = Integer.parseInt(textArea10.getText());
					System.out.println("GOOOOOOOOOOOOD " + mds);

					byte[] sendData = new byte[Integer.parseInt(textArea10.getText())]; // 2000
					// changed
					// buffer
					// size this is MDS from
					// GUI
					byte[] receivedData = new byte[Integer.parseInt(textArea10.getText())]; // 2000
					// changed
					// buffer
					// size this is MDS
					// from GUI

					int x = 0;
					File file = new File(textArea8.getText());
					FileInputStream fin = null;
					try {
						// reads from file
						fin = new FileInputStream(file);

						mds = mds - 2;

						long noOfBlocks = (long) Math.ceil((double) file.length() / (double) mds);
						byte[][] result = new byte[(int) noOfBlocks][mds];
						int offset = 0;
						for (int i = 0; i < result.length; i++) {
							result[i] = readByteBlock(fin, offset, mds);
						}

						datasent = new ArrayList<String>();
						// prints array of bytes
						String seq = "0 ";
						for (int i = 0; i < result.length; i++) {
							String s = new String(result[i]);
							String s2 = seq + s;
							// System.out.println("DATAGRAMS: " + s2);
							// System.out.println("SIZE OF DATAGRAM " +
							// s2.length() / 2);
							// System.out.println("\n");
							datasent.add(s2);
							if (seq.equals("0 ")) {
								seq = "1 ";
							} else {
								seq = "0 ";
							}

						}

						// prints whole file from string array THIS INCLUDES
						// SEQUENCE NUMBER
						// datasent is the file that has the has the proper size
						// datagrams
						// with sequence number
						for (int i = 0; i < datasent.size(); i++) {
							System.out.println(datasent.get(i));
						}

					} finally {
						try {
							if (fin != null) {
								fin.close();
							}
						} catch (IOException ioe) {
							System.out.println("Error while closing stream: " + ioe);
						}
					}

					while (x < datasent.size()) { // change to line
													// contains EOD
						try {

							// Send the UDP Packet to the server

							// System.out.println("Sending Line: " +
							// datasent.get(x).toString());
							textArea15.append("Sending Line: " + datasent.get(x).toString() + "\n");

							// String data = sequenceNum + " " + line;
							// datasent.add(data);
							sendData = datasent.get(x).getBytes();
							// System.out.println("Byte Line: " + new
							// String(sendData,0,sendData.length));
							packet = new DatagramPacket(sendData, sendData.length, address, rcvPort); // port
																										// num
																										// here
																										// from
																										// gui
							socket.send(packet);
							// System.out.println("Received");

							// Receive the server's packet
							packet = new DatagramPacket(receivedData, receivedData.length);
							socket.receive(packet);

							// String returnMessage =
							// ByteBuffer.wrap(packet.getData()).toString();
							// String returnMessage =new
							// String(receivedData,0,receivedData.length);
							// message saying it's been acknowledged
							// System.out.println("From server: ACK -> " +
							// returnMessage);
							// Output of what was received
							String received = new String(packet.getData(), 0, packet.getLength());
							// System.out.println("RECEIVED" + received);
							textArea15.append(received + "\n");
							x++;

						} catch (SocketTimeoutException exception) {
							// If we don't get an ack, prepare to resend
							// sequence number
							String[] sq = datasent.get(x).split(" ", 2);
							// System.out.println("Timeout (Sequence Number " +
							// sq[0] + ")");
							textArea15.append("Timeout (Sequence Number " + sq[0] + ")\n");

						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}
					System.out.println("Sending EOT ");
					String end = "EOT EOT";
					sendData = end.getBytes();
					packet = new DatagramPacket(sendData, sendData.length, address, rcvPort); // port
																								// num
																								// here
																								// from
																								// gui
					socket.send(packet);
					packet = new DatagramPacket(receivedData, receivedData.length);
					socket.receive(packet);
					String received = new String(packet.getData(), 0, packet.getLength());
					// System.out.println(received);
					textArea15.append(received + "\n");
					reader.close();
				} catch (SocketException e1) {
					// TODO Auto-generated catch block
					textArea15.setText("Socket Error");
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block

					textArea15.setText("FILE NOT FOUND");
				} catch (IOException e1) {

					textArea15.setText("Incorrect input");

				}
				socket.close();
				long endTime = System.currentTimeMillis();
				// System.out.println("It took " + (endTime - startTime) + "
				// milliseconds");
				long t = endTime - startTime;
				// String t1 = t.toString();
				textArea14.setText(String.valueOf(t));
			}

		});

		f.setVisible(true);

	}

	private static byte[] readByteBlock(InputStream in, int offset, int noBytes) throws IOException {
		byte[] result = new byte[noBytes];
		in.read(result, offset, noBytes);
		return result;
	}

}
