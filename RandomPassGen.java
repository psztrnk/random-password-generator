/*
 * Random Password Generator 1.5.2
 * A small application made during learning Java
 * 
 * Written by psztrnk
 * http://paszternak.me
 * 
 * License: Public Domain
 * 
 * Version 1.5.2
 * - expanded list of special characters
 * 
 * Version 1.5.1
 * - added default filename if custom filename not provided
 * 
 * Version 1.5
 * - added option to specify text file name
 * - redesign (two columns)
 * 
 * Version 1.4.1
 * - excluded upper case i from the failsafe character set
 * 
 * Version 1.4
 * - added option to export password as a text file
 * 
 * Version 1.3.1
 * - expanded list of special characters
 * - corrected a typo
 * 
 * Version 1.3
 * - added checkbox to exclude "l", "1", "O" and "0" characters (failsafe)
 * 
 * Version 1.2
 * - added checkbox to exclude special characters
 * 
 * Version 1.1
 * - added validation to the password length input
 * 
 * Version 1.0
 * - initial version
 */

package _RandomPassGenerator;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

public class RandomPassGen {

	private JFrame frmRandomPasswordGenerator;
	private JTextField textField;
	private JLabel lblInsertPasswordLength;
	private JTextField textField_1;

	// Launch the application

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RandomPassGen window = new RandomPassGen();
					window.frmRandomPasswordGenerator.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Create the application
	public RandomPassGen() {
		initialize();
	}

	// Initialize the contents of the frame
	private void initialize() {
		frmRandomPasswordGenerator = new JFrame();
		frmRandomPasswordGenerator.setTitle("Random Password Generator v1.5.2");
		frmRandomPasswordGenerator.setBounds(100, 100, 800, 630);
		frmRandomPasswordGenerator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRandomPasswordGenerator.getContentPane().setLayout(null);

		// Apparently we need this here, otherwise the if statement on the
		// chckbxExcludeSpecialCharacters checkbox will throw an exception
		JCheckBox chckbxExcludeSpecialCharacters = new JCheckBox(" Exclude special characters (alphanumeric only)");
		chckbxExcludeSpecialCharacters.setBounds(409, 127, 315, 25);
		chckbxExcludeSpecialCharacters.setFont(new Font("Tahoma", Font.BOLD, 12));
		frmRandomPasswordGenerator.getContentPane().add(chckbxExcludeSpecialCharacters);

		// Apparently we need this here, otherwise the if statement on the
		// chckbxExcludeCharactersEasy checkbox will throw an exception
		JCheckBox chckbxExcludeCharactersEasy = new JCheckBox(" Exclude characters easy to mix up (failsafe)");
		chckbxExcludeCharactersEasy.setBounds(409, 180, 297, 25);
		chckbxExcludeCharactersEasy.setFont(new Font("Tahoma", Font.BOLD, 12));
		frmRandomPasswordGenerator.getContentPane().add(chckbxExcludeCharactersEasy);

		// initialize filename textfield and labels
		// need to do this here, since those are actvive/inactive
		// based on the state of the chckbxSavePasswordTo checkbox
		textField_1 = new JTextField();
		textField_1.setBounds(517, 280, 174, 22);
		frmRandomPasswordGenerator.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		textField_1.setEnabled(false);
		textField_1.setText("");

		// initialize label 1 for file name text field
		JLabel lblTextFileName = new JLabel(" Text file name:");
		lblTextFileName.setBounds(409, 283, 111, 16);
		lblTextFileName.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTextFileName.setEnabled(false);
		frmRandomPasswordGenerator.getContentPane().add(lblTextFileName);

		// initialize label 2 for file name text field
		JLabel lbltxt = new JLabel(".txt");
		lbltxt.setBounds(703, 283, 56, 16);
		lbltxt.setFont(new Font("Tahoma", Font.BOLD, 12));
		lbltxt.setEnabled(false);
		frmRandomPasswordGenerator.getContentPane().add(lbltxt);

		// Apparently we need this here, otherwise the if statement on the
		// chckbxSavePasswordTo checkbox will throw an exception
		JCheckBox chckbxSavePasswordTo = new JCheckBox(" Save password as text file");
		chckbxSavePasswordTo.setBounds(409, 232, 193, 25);
		// this will control active/inactive state of the filename stuff
		chckbxSavePasswordTo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					textField_1.setEnabled(true);
					lblTextFileName.setEnabled(true);
					lbltxt.setEnabled(true);

				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					textField_1.setEnabled(false);
					lblTextFileName.setEnabled(false);
					lbltxt.setEnabled(false);
					textField_1.setText("");
				}
			}
		});
		chckbxSavePasswordTo.setFont(new Font("Tahoma", Font.BOLD, 12));
		frmRandomPasswordGenerator.getContentPane().add(chckbxSavePasswordTo);

		// the Generate! button with the action event
		JButton btnGenerate = new JButton("Generate my password!");
		btnGenerate.setBounds(409, 340, 200, 25);
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// declare these variables for further use
				String output = null;
				String pwcharacters = null;
				String combo = null;
				String saved = null;
				int a = 0;
				String filename = textField_1.getText();

				// check whether the "Exclude special characters",
				// Failsafe and
				// Save as text file
				// checkboxs are checked
				boolean checked = chckbxExcludeSpecialCharacters.isSelected();
				boolean selected = chckbxExcludeCharactersEasy.isSelected();
				boolean textfile = chckbxSavePasswordTo.isSelected();

				if (checked && !selected) {
					// if Exclude special characters is checked, use only the
					// alphanumeric character set
					pwcharacters = "aAbBcCdDeEfFgGhHijJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ0123456789";
					combo = " alphanumeric ";
				} else if (selected && !checked) {
					// if Exclude special characters is not checked, but
					// Failsafe is, use the full character set with
					// special characters included but 0, O, I, 1 and l removed
					pwcharacters = "aAbBcCdDeEfFgGhHijJkKLmMnNopPqQrRsStTuUvVwWxXyYzZ23456789&@()^$-_+!%=#:?;\\/.{}[]";
					combo = " failsafe ";
				} else if (checked && selected) {
					// if both Exclude special characters and Failsafe are
					// checked
					// remove special characters and 0, O, I, 1 and l
					pwcharacters = "aAbBcCdDeEfFgGhHijJkKLmMnNopPqQrRsStTuUvVwWxXyYzZ23456789";
					combo = " alphanumeric and failsafe ";
				} else {
					// if none checked, use the full character set
					pwcharacters = "aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ0123456789&@()^$-_+!%=#:?;\\/.{}[]";
					combo = " ";
				}

				// number of password characters specified by the user
				// if empty or zero is entered, use 20 as default
				if (textField.getText().equals("") || textField.getText().equals("0")) {
					textField.setText("20");
				}

				// validate whether user entered a number
				try {
					// if user entered a number
					// get the value entered by the user as password length
					a = Integer.parseInt(textField.getText());
					// if user entered anything else
					// display an error message
				} catch (NumberFormatException f) {
					JOptionPane.showMessageDialog(frmRandomPasswordGenerator, "Please enter numbers only.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				// get the character set defined by the checkbox
				char[] chars = pwcharacters.toCharArray();

				// create a random set of characters
				StringBuilder sb = new StringBuilder();
				Random random = new Random();

				// get the random sequence
				for (int i = 0; i < a; i++) {

					// run until the character chain is "a" characters long
					// (default 20 characters)
					char c = chars[random.nextInt(chars.length)];
					sb.append(c);
				}

				// join the random characters into one string
				output = sb.toString();

				// print the password to the console, so it can be copied
				// we don't really need this, since we have a GUI
				// System.out.println(output);

				// place the resulting random password on the clipboard
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				Clipboard clipboard = toolkit.getSystemClipboard();
				StringSelection strSel = new StringSelection(output);
				clipboard.setContents(strSel, null);

				// save the resulting random password as a "yourpass.txt" file
				BufferedWriter writer = null;

				// check whether the save to file checkbox is checked
				// if so, create the file
				if (textfile) {
					// if filename is not empty, name the file respectively
					if (!filename.isEmpty()) {
						saved = "The password has been saved as \"" + filename + ".txt\".";
						try {
							writer = new BufferedWriter(new FileWriter(filename + ".txt"));
							writer.write(output);

						} catch (IOException f) {
						} finally {
							try {
								if (writer != null)
									writer.close();
							} catch (IOException f) {
							}
						}
						// if filename is empty, name the file yourpassword.txt
					} else {
						saved = "The password has been saved as \"yourpassword.txt\".";
						try {
							writer = new BufferedWriter(new FileWriter("yourpassword.txt"));
							writer.write(output);

						} catch (IOException f) {
						} finally {
							try {
								if (writer != null)
									writer.close();
							} catch (IOException f) {
							}
						}
					}
					// if save as file is not selected, do nothing
				} else {
					saved = " ";
				}

				// display a success message
				JOptionPane.showMessageDialog(frmRandomPasswordGenerator,
						"Your random, " + a + " characters long" + combo
								+ "password was successfully generated and copied to the clipboard.\n" + saved,
						"Password successfully generated", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		// display the GUI elements

		frmRandomPasswordGenerator.getContentPane().add(btnGenerate);

		lblInsertPasswordLength = new JLabel(" I want my password to be");
		lblInsertPasswordLength.setBounds(409, 77, 180, 25);
		lblInsertPasswordLength.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblInsertPasswordLength.setHorizontalAlignment(SwingConstants.LEFT);
		frmRandomPasswordGenerator.getContentPane().add(lblInsertPasswordLength);

		textField = new JTextField();
		textField.setBounds(592, 78, 50, 22);
		frmRandomPasswordGenerator.getContentPane().add(textField);
		textField.setColumns(10);

		JLabel lblRandomPasswordGenerator = new JLabel("Random Password Generator v1.5.2");
		lblRandomPasswordGenerator.setBounds(12, 555, 408, 16);
		lblRandomPasswordGenerator.setEnabled(false);
		lblRandomPasswordGenerator.setHorizontalAlignment(SwingConstants.CENTER);
		frmRandomPasswordGenerator.getContentPane().add(lblRandomPasswordGenerator);

		JTextPane txtrByDefaultThe = new JTextPane();
		txtrByDefaultThe.setBounds(37, 27, 346, 117);
		txtrByDefaultThe.setAlignmentY(Component.TOP_ALIGNMENT);
		txtrByDefaultThe.setBackground(SystemColor.menu);
		txtrByDefaultThe.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtrByDefaultThe.setEditable(false);
		txtrByDefaultThe.setText(
				"By default, the generator will create a random, 20 characters long password. To change the length of password generated, please enter the desirable number of characters in the text field.\r\n\r\nRemember: the longer (and more complex) password you use, the harder to crack your stuff.");
		frmRandomPasswordGenerator.getContentPane().add(txtrByDefaultThe);

		JLabel lblCharacters = new JLabel("characters long.");
		lblCharacters.setBounds(655, 78, 104, 22);
		lblCharacters.setFont(new Font("Tahoma", Font.BOLD, 12));
		frmRandomPasswordGenerator.getContentPane().add(lblCharacters);

		JTextPane txtpnSomeSitesDo = new JTextPane();
		txtpnSomeSitesDo.setBounds(37, 195, 346, 54);
		txtpnSomeSitesDo.setEditable(false);
		txtpnSomeSitesDo.setBackground(SystemColor.menu);
		txtpnSomeSitesDo.setText(
				"Some sites do not support special characters in passwords. Tick the checkbox on the right to generate a strictly alphanumeric password.");
		frmRandomPasswordGenerator.getContentPane().add(txtpnSomeSitesDo);

		JTextPane txtpnTickTheBelow = new JTextPane();
		txtpnTickTheBelow.setBounds(37, 296, 346, 69);
		txtpnTickTheBelow.setBackground(SystemColor.menu);
		txtpnTickTheBelow.setText(
				"Tick the failsafe option to exclude characters that are easy to mix up when displayed with certain font families: upper case o and zero, one, lower case L and upper case i.");
		frmRandomPasswordGenerator.getContentPane().add(txtpnTickTheBelow);

		JTextPane txtpnByDefaultYour = new JTextPane();
		txtpnByDefaultYour.setBounds(37, 403, 346, 86);
		txtpnByDefaultYour.setBackground(SystemColor.menu);
		txtpnByDefaultYour.setText(
				"By default, your password will be copied to the clipboard. Tick the checkbox if you want to save it as a file (file will be stored in the same directory where this application runs from). If no file name specified, the password will be saved as \"yourpassword.txt\".");
		frmRandomPasswordGenerator.getContentPane().add(txtpnByDefaultYour);

		JSeparator separator = new JSeparator();
		separator.setBounds(395, 23, 2, 536);
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setForeground(SystemColor.activeCaptionBorder);
		frmRandomPasswordGenerator.getContentPane().add(separator);

		JLabel lblNewLabel = new JLabel("Alphanumeric only");
		lblNewLabel.setBounds(40, 166, 213, 16);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		frmRandomPasswordGenerator.getContentPane().add(lblNewLabel);

		JLabel lblFailsafeMode = new JLabel("Failsafe mode");
		lblFailsafeMode.setBounds(40, 267, 325, 16);
		lblFailsafeMode.setFont(new Font("Tahoma", Font.BOLD, 12));
		frmRandomPasswordGenerator.getContentPane().add(lblFailsafeMode);

		JLabel lblSaveAsText = new JLabel("Save as text file");
		lblSaveAsText.setBounds(40, 368, 346, 16);
		lblSaveAsText.setFont(new Font("Tahoma", Font.BOLD, 12));
		frmRandomPasswordGenerator.getContentPane().add(lblSaveAsText);

		JLabel lblOptions = new JLabel("OPTIONS");
		lblOptions.setBounds(409, 27, 56, 16);
		lblOptions.setEnabled(false);
		lblOptions.setFont(new Font("Tahoma", Font.BOLD, 12));
		frmRandomPasswordGenerator.getContentPane().add(lblOptions);

	}
}
