# HandshakingDemo

What is Handshaking?
Used in order to establish a connection between end hosts, verify the speed and also the authorization of the end host you are establishing a connection with. This is known as handshaking because it is the exchange of greeting between the hosts in which one host will send a message to the other host (server) and the other will receive then send back an acknowledgement back. In this case, the sender will be sending over MDS (maximum size UDP datagram) to the receiver in which they would ensure it was a positive integer and also use the MDS as their maximum size UDP Diagram. Once this is complete, the Sender waits for a response from the Receiver which would be a duplicate of the MDS it sent out.
Datagram Format
Sender 
socket = new DatagramSocket(port);
packet = new DatagramPacket(sendData, sendData.length, address, rcvPort);
When establishing the socket
By providing nothing within the parameters of the DatagramSocket, the constructor gets the socket to connect onto any available local port. In this case we must make sure it uses the text inputted by user.

When sending/receiving the packet
Please ensure that Unreliable/Reliable is chosen first and the rest of the Receiver is filled out before trying to send a packet from the Sender side. Also ensure that you are including the whole file path on the Sender side.
InetAddress and Port Number will be used to provide the destination of the Receiver. This is manually inputted by the client
SendData will include the line sender is Sending to the Receiver.

Receiver
(when receiving a request)
DatagramPacket packetReceived = new DatagramPacket(dataReceived, dataReceived.length);

(when sending a response – ACK is sent attached to sequence number (0/1))
DatagramPacket packet = new DatagramPacket(dataSending, dataSending.length, IPAddress, port);
InetAddress and Port Number will be used to provide the destination of the Sender. This can be found by using the IP address  inputted by Receiver user.
dataReceived will include the messages that were sent by the Sender and received by the Receiver. This information will later be added to the new File on the Receiver side. 
dataSending will include the acknowledgement to be received by the Sender in order to move on to the next packet to be sent and saved into the new File being created by the sender.
Timing Reports 

File Size/        	  Mds/       	Time Out/	  Transmission Time -->
200kb(unreliable)/	512/	         4000/	        225619,
200kb(reliable)/	  900/	         2000/         5639,
3kb(reliable)/	     60/	         5000/	          159,
3kb(unreliable)/	  700/	         3000/	           49,
