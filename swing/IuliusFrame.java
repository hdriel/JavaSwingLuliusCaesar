package swing;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;


public class IuliusFrame extends JFrame implements  ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String STR_EXIT = "תודה שהשתמשת באפליקציה שלי\n בתודה: הדריאל בנג\'ו";
	private static final String STR_TO = "הצופן שלך יפוענח כאן...";
	private static final String STR_FROM = "הזן כאן את הצופן שלך...";
	private static final String STR_CRACK = "פענח צופן";
	private static final String STR_COPY = "העתק מידע";
	private static final String STR_SHIFT = "כמות הזזות";
	private static final String STR_ERROR_KEY = "המקלדת על השפה האנגלית, עליך לעבור לשפה עברית כדי להקליד פה.";
	private static final String STR_CLIPBOARD = "הפענוח הועתק לclipboard שלך.";
	
	private int hieght = 700;
	private int width = 700;
	private JPanel p_main, p_table, newPanel, panelradioButtons, panelConversion, panelSentence; 
	private JTextField shiftField;
	private JTextArea textArea;
	private JLabel sentenceConversion;
	private JButton btn_convert, btn_copy;
	private static Sentence snt = new Sentence();
	private JRadioButton rb_shifts, rb_atbash, rb_almab;
	private char[] letters_original = Sentence.letters;
	private char[] letters_converted = new char[Sentence.letters.length];
	private int choose = 1, toShift = 0;
	//private int[] colors = {0x000000,0x0000ff,0x00ffff,0x009933,0x53ff1a,0xcc00ff,0xff9933,0xff00ff,0xff0000,0x003399,0x660066,0x996633,0xff6666,0x0066cc,0x9933ff, 0x0099cc ,0x999966 ,0x66ff99 ,0x9999ff ,0xcc0066 , 0xcccc00, 0xcc00cc};
	
	@Override
	public void dispose() {
		JOptionPane.showMessageDialog(null,STR_EXIT);
	    super.dispose();
	}
	
	public IuliusFrame(){
		super("הצפנה ופיענוח");
		
		init_panel();
		this.add(p_main);
			
		this.addWindowListener(new WindowAdapter(){
            @Override public void windowClosing(WindowEvent e){e.getWindow().dispose();}
        });
		// create functionality to close the window when "Escape" button is pressed
		KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
		Action action = new AbstractAction(){
			public void actionPerformed(ActionEvent e){JOptionPane.showMessageDialog(null,STR_EXIT); System.exit(0);}
		};
		InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(stroke, "ESCAPE");
		rootPane.getActionMap().put("ESCAPE", action);
		
		this.setResizable(false);
		//this.setSize(width,hieght);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	private void init_table(){
		int click = 0;
		boolean changed = false;
		if(rb_shifts.isSelected()){
			int shift = 0;
			try{
				shift = Integer.parseInt(shiftField.getText().toString());
			}
			catch(Exception e){
				shift = 0;
			}
			
			for(int i = 0; i < letters_original.length; i++){
				letters_converted[i] = Sentence.getCharShift(letters_original[i], shift);
			}
			click = 1;
			if(toShift != shift){
				changed = true;
				toShift = shift;
			}
		}
		else if(rb_atbash.isSelected()){
			for(int i = 0; i < letters_original.length; i++){
				letters_converted[i] = Sentence.getCharATBSH(letters_original[i]);
			}
			click = 2;
		}
		else if(rb_almab.isSelected()){
			for(int i = 0; i < letters_original.length; i++){
				letters_converted[i] = Sentence.getCharALBAM(letters_original[i]);
			}
			click = 3;
		}
		JLabel l1 ,l2;
		
		p_table = new JPanel();
		p_table.setLayout(new GridLayout(2, letters_original.length));
		
		for(int i = letters_original.length - 1; i >= 0 ; i--){
			l1 = new JLabel(" " + letters_original[i] + " ");
			l1.setFont(new java.awt.Font("Tahoma", Font.BOLD, 20));
			l1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			l1.setForeground(Color.BLUE);
			p_table.add(l1);
		}
		
		for(int i = letters_original.length - 1 ; i >= 0 ; i--){
			l2 = new JLabel(" " + letters_converted[i] + " ");
			l2.setFont(new java.awt.Font("Tahoma", Font.BOLD, 20));
			l2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			//int j = Sentence.findIndexLetter(letters_converted[i]);
			//if(j >= 0)  l2.setForeground(new Color(colors[j]));
			p_table.add(l2);
		}
		
		if(click != choose || (click == 1 && choose == 1 && changed))
			cleanAndSetAll();
	}
	private void init_panel(){
		int widthPanel = 365;
		int heightPanel = 250;

		p_main = new JPanel();
		
		textArea = new JTextArea();
		textArea.setPreferredSize(new Dimension(widthPanel - 15, heightPanel - 30));
		
		// Alignment the text to right
		textArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		textArea.setFont(new java.awt.Font("Tahoma", Font.BOLD, 25));
		textArea.setText(STR_FROM);
		
		// select all text when textField get focus
		textArea.addFocusListener(new FocusListener() {
            @Override public void focusLost(final FocusEvent pE) {}
            @Override public void focusGained(final FocusEvent pE) {
            	textArea.selectAll();
            }
        });
		textArea.requestFocusInWindow();
		textArea.selectAll();
		textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
		
        textArea.addKeyListener(new KeyListener() {		 
		    @Override
		    public void keyTyped(KeyEvent e) {
		    	char c = e.getKeyChar();
		        if (!(  (c >= '0') && (c <= '9') ||
		        		(c == KeyEvent.VK_BACK_SPACE) ||
		        		(c == KeyEvent.VK_DELETE) || 
		        		(c == '-') || 
		        		(c >= snt.unicodeAlef && c <= snt.unicodeTaf)||
		        		(snt.isAllowedLetters(c)))) 
		        {
		          getToolkit().beep();
		          btn_convert.setEnabled(false);
		          e.consume();
		          JOptionPane.showMessageDialog(null,STR_ERROR_KEY); 
		        }
		    }
		    @Override
		    public void keyReleased(KeyEvent event) {
		    	int num;
		    	try{
		    		num = Integer.parseInt(shiftField.getText());
		    		if(num > 22 || num < -22 ){
		    			shiftField.setForeground(Color.RED);
		    			btn_convert.setEnabled(false);
			        }
			    	else{
			    		shiftField.setForeground(Color.BLACK);
		    			btn_convert.setEnabled(true);
		    			init_table();
			    	}
			    }
		    	catch(NumberFormatException nfe){
		    		shiftField.setForeground(Color.RED);
		    		btn_convert.setEnabled(false);
		    	}
		    	
		    }
		    @Override
		    public void keyPressed(KeyEvent event) {
		        
		    }
		});
        
        
		shiftField = new JTextField(STR_SHIFT, 7);
		
		// Alignment the text to center
		shiftField.setHorizontalAlignment(JTextField.CENTER);
		
		shiftField.setFont(new java.awt.Font("Tahoma", Font.BOLD, 25));
		
		// select all text when textField get focus
		shiftField.addFocusListener(new FocusListener() {
            @Override public void focusLost(final FocusEvent pE) {}
            @Override public void focusGained(final FocusEvent pE) {
            	shiftField.selectAll();
            }
        });
		
		shiftField.addKeyListener(new KeyListener() {		 
		    @Override
		    public void keyTyped(KeyEvent e) {
		    	char c = e.getKeyChar();
		        if (!(  (c >= '0') && (c <= '9') ||
		        		(c == KeyEvent.VK_BACK_SPACE) ||
		        		(c == KeyEvent.VK_DELETE) || 
		        		(c == '-'))) 
		        {
		          getToolkit().beep();
		          btn_convert.setEnabled(false);
		          e.consume();
		        }
		    }
		    @Override
		    public void keyReleased(KeyEvent event) {
		    	int num;
		    	try{
		    		num = Integer.parseInt(shiftField.getText());
		    		if(num > 22 || num < -22 ){
		    			shiftField.setForeground(Color.RED);
		    			btn_convert.setEnabled(false);
			        }
			    	else{
			    		shiftField.setForeground(Color.BLACK);
		    			btn_convert.setEnabled(true);
		    			init_table();
			    	}
			    }
		    	catch(NumberFormatException nfe){
		    		shiftField.setForeground(Color.RED);
		    		btn_convert.setEnabled(false);
		    	}
		    	
		    }
		    @Override
		    public void keyPressed(KeyEvent event) {
		        
		    }
		});
		
		sentenceConversion = new JLabel(STR_TO);
		sentenceConversion.setFont(new java.awt.Font("Tahoma", Font.BOLD, 25));
		
		btn_convert = new JButton(STR_CRACK);
		btn_convert.setFont(new java.awt.Font("Tahoma", Font.BOLD, 20));
		btn_convert.addActionListener(this);
		btn_convert.setEnabled(false);
		
		btn_copy = new JButton(STR_COPY);
		btn_copy.setFont(new java.awt.Font("Tahoma", Font.BOLD, 20));
		btn_copy.addActionListener(this);
		btn_copy.setEnabled(false);
		
		rb_shifts = new JRadioButton("הזזות", true);
		rb_shifts.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	init_table();
	        	shiftField.setEnabled(true);
	        	shiftField.setText(STR_SHIFT);
	        	btn_convert.setEnabled(false);
	        }
	    });
		choose = 1;
		toShift = 0;
		rb_atbash = new JRadioButton("אתב\"ש", false);
		rb_atbash.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	init_table();
	        	shiftField.setEnabled(false);
	        	btn_convert.setEnabled(true);
	        }
	    });
		rb_almab = new JRadioButton("אלב\"מ", false);
		rb_almab.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	init_table();
	        	shiftField.setEnabled(false);
	        	btn_convert.setEnabled(true);
	        }
	    });
		
		ButtonGroup group = new ButtonGroup();
		group.add(rb_shifts);
		group.add(rb_atbash);
		group.add(rb_almab);
		
		
		panelradioButtons = new JPanel();
		panelradioButtons.setLayout(new BoxLayout (panelradioButtons, BoxLayout.Y_AXIS));
		panelradioButtons.add(rb_shifts);
		panelradioButtons.add(rb_atbash);
		panelradioButtons.add(rb_almab);
		
		// shiftField
		// btn_convert
		// sentenceConversion
		// textArea
		// btn_copy
		
		panelConversion = new JPanel();
		panelConversion.setPreferredSize( new Dimension( widthPanel, heightPanel ) );
		panelConversion.setBorder(BorderFactory.createTitledBorder("הפענוח"));
		panelConversion.add(sentenceConversion);
		
		panelSentence = new JPanel();
		panelSentence.setPreferredSize( new Dimension( widthPanel, heightPanel ) );
		panelSentence.setBorder(BorderFactory.createTitledBorder("הצופן"));
		panelSentence.add(textArea);
		
		
		newPanel = new JPanel();
		p_main.add(newPanel);
		
		newPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(); 
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(10, 10, 10, 10); 
		
		gbc.gridx = 0; 	
		gbc.gridy = 0;	
		newPanel.add(panelConversion, gbc);  

		
		gbc.gridx = 1; 	
		gbc.gridy = 0;	
		gbc.gridwidth = 2;
		newPanel.add(panelSentence, gbc);
		
		
		
		gbc.gridx = 0; 	
		gbc.gridy = 1;	
	    gbc.fill = GridBagConstraints.HORIZONTAL;
		newPanel.add(btn_copy, gbc);
		gbc.fill = GridBagConstraints.NONE;

		
		gbc.gridx = 1; 	
		gbc.gridy = 1;	
		gbc.anchor = GridBagConstraints.WEST; 
		gbc.ipady = 10; 
		newPanel.add(shiftField, gbc);
		
		
		gbc.gridx = 2; 	
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.EAST; 
		newPanel.add(btn_convert, gbc);
		
		init_table();
		gbc.gridx = 0; 	
		gbc.gridy = 3;
		gbc.gridwidth = 4;
		gbc.anchor = GridBagConstraints.WEST; 
		newPanel.add(p_table, gbc);
		
		gbc.gridx = 0; 	
		gbc.gridy = 3;	
		gbc.anchor = GridBagConstraints.EAST;
		newPanel.add(panelradioButtons, gbc);
		
		p_main.add(newPanel);
	}
	
	private void cleanAndSetAll(){
		p_main.remove(newPanel);
		
		newPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(); 
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(10, 10, 10, 10); 
		
		gbc.gridx = 0; 	
		gbc.gridy = 0;	
		newPanel.add(panelConversion, gbc);  
		
		gbc.gridx = 1; 	
		gbc.gridy = 0;	
		gbc.gridwidth = 2;
		newPanel.add(panelSentence, gbc);
		
		gbc.gridx = 0; 	
		gbc.gridy = 1;	
	    gbc.fill = GridBagConstraints.HORIZONTAL;
		newPanel.add(btn_copy, gbc);
		gbc.fill = GridBagConstraints.NONE;

		gbc.gridx = 1; 	
		gbc.gridy = 1;	
		gbc.anchor = GridBagConstraints.WEST; 
		gbc.ipady = 10; 
		newPanel.add(shiftField, gbc);
		
		gbc.gridx = 2; 	
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.EAST; 
		newPanel.add(btn_convert, gbc);
		
		gbc.gridx = 0; 	
		gbc.gridy = 3;
		gbc.gridwidth = 4;
		gbc.anchor = GridBagConstraints.WEST; 
		newPanel.add(p_table, gbc);
		
		gbc.gridx = 0; 	
		gbc.gridy = 3;	
		gbc.anchor = GridBagConstraints.EAST;
		newPanel.add(panelradioButtons, gbc);
		
		p_main.add(newPanel);
		p_main.revalidate();
		p_main.repaint();
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btn_convert)
		{
			String str = textArea.getText().toString();
			String res = "";
			if(rb_almab.isSelected()){
				res = snt.calculateALBAMStatment(str);
			}
			else if(rb_atbash.isSelected()){
				res = snt.calculateATBASHStatment(str);
			}
			else if(rb_shifts.isSelected() && !(shiftField.getText().toString().equals(STR_SHIFT))){
				int shift = 0;
				try
				{
					shift = Integer.parseInt(shiftField.getText().toString());	
				}
				catch(Exception ee)
				{
					shift = 0;
				}
				res = snt.calculateShiftStatment(str, shift);
			}
			else{
				res = "";
				btn_copy.setEnabled(false);
			}
			sentenceConversion.setText("<html>" + res.replaceAll("\n", "<br/>") + "</html>");
			if(!sentenceConversion.getText().equals(STR_TO)){
				btn_copy.setEnabled(true);
			}
			else{
				btn_copy.setEnabled(false);
			}
		}
		if(e.getSource() == btn_copy){
			String res = sentenceConversion.getText().toString();
			res = res.replace("<html>", "");
			res = res.replace("</html>", "");
			res = res.replace("</br>", "\n");
			StringSelection selection = new StringSelection(res);
		    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		    clipboard.setContents(selection, selection);
		    JOptionPane.showMessageDialog(null,STR_CLIPBOARD); 
		}
	}
	
	//  *********** main method **********************
	public static void main(String [] args){
		IuliusFrame a = new IuliusFrame();
	}
}
