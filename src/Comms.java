import com.fazecast.jSerialComm.SerialPort;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Scanner;

//Warning: Messy Code... and may cause BSoD sometimes

public class Comms {
	public static void main(String[] args) throws Exception {

		JFrame frame = new JFrame();
		JFrame frame2 = new JFrame();
		JFrame frame3 = new JFrame();
		JFrame frame4 = new JFrame();

		frame.setSize(100, 100);
		frame2.setSize(500, 500);
		JButton button = new JButton();
		button.setSize(10, 10);
		button.setText("Send Data");

		JButton led = new JButton();
		led.setSize(10, 10);
		led.setText("Toggle LED");

		JSlider freq = new JSlider(JSlider.HORIZONTAL,
		0, 1000, 400);

		freq.setMajorTickSpacing(100);
		freq.setMinorTickSpacing(10);
		freq.setPaintTicks(true);
		freq.setPaintLabels(true);



		frame4.add(freq);
		frame4.setSize(1500, 200);
		frame4.setVisible(true);


		frame3.add(led);
		frame.add(button);
		frame.setVisible(true);
		JTextArea text = new JTextArea(5, 40);
		frame2.add(text);
		frame2.setVisible(true);
		text.setLineWrap(false);
		text.setWrapStyleWord(false);
		frame3.setVisible(true);








		SerialPort ports[] = SerialPort.getCommPorts();
		//Get Array of Ports, and then use the first one (COM3)
		SerialPort port = ports[0];


		if (port.openPort()) {
			System.out.println("Port COM3 Opened");
		} else {
			System.out.println("Error Opening Port!!!");
		}
		port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		port.setBaudRate(19200);

		InputStream inputStream = port.getInputStream();
		final OutputStream outputStream = port.getOutputStream();

		String line = "";

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					outputStream.write("here is some data".getBytes());
					System.out.println("Sent Data");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});



		led.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					if (ledT.LEDS) {
						outputStream.write("LED-OFF".getBytes());
						System.out.println("Sent Data");
						ledT.LEDS = false;
					} else {
						outputStream.write("LED-ON".getBytes());
						System.out.println("Sent Data");
						ledT.LEDS = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		String logs = "";






		class BoundedChangeListener implements ChangeListener {
			public void stateChanged(ChangeEvent e) {
				Object source = e.getSource();
				if (source instanceof BoundedRangeModel) {
					BoundedRangeModel aModel = (BoundedRangeModel) source;
					if (!aModel.getValueIsAdjusting()) {
						System.out.println("Changed: " + aModel.getValue());
					}
				} else if (source instanceof JSlider) {
					JSlider theJSlider = (JSlider) source;
					if (!theJSlider.getValueIsAdjusting()) {



						try {
							outputStream.write("FREQ-".getBytes());
							outputStream.write(String.valueOf(theJSlider.getValue()).getBytes());
							System.out.println("Sent Data");
						} catch (Exception er) {
							er.printStackTrace();
						}
					}
				} else {
					System.out.println("Something changed: " + source);
				}
			}
		}
		freq.addChangeListener(new BoundedChangeListener());

		while (true) {



			line = line + (Character.toString((char) inputStream.read()));

			if (line.contains("END")) {
				//remove END-er
				line = line.replace("END", "");
				System.out.println(line);
				System.out.flush();
				logs = "\n" + new java.util.Date() + "---: " + line.replace("\n", "") + "" + logs;
				text.setText(logs);
				line = "";
			}



			Thread.sleep(10);
		}


	}
}
