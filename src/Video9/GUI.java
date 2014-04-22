package Video9;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.*;




/**
 * GUI Class. Launches a GUI that will allow all of the same operations as the command line interface.
 * @author johnny
 *
 */
@SuppressWarnings("serial")
public class GUI extends JFrame implements ActionListener,Runnable {
	
	/*
	 * GUI Components - dummy comment
	 */
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem addMenuItem, viewMenuItem, quitMenuItem;
	
	private JPanel mainPanel, fileListPanel;
	private JProgressBar progressBar;
	private JPanel panel;
	private JTextField textbox;
	
	
	JLabel label = new JLabel("Select File to Decode:");;
	JLabel labeleffect = new JLabel("Select realtime effects (Only one may be selected):");;

    JTextField textField = new JTextField();
    JCheckBox grayscaleBox = new JCheckBox("Grayscale");
    JCheckBox wrapCheckBox = new JCheckBox("Wrap Around");
    JCheckBox wholeCheckBox = new JCheckBox("Whole Words");
    JButton findButton = new JButton("Find File");
    JButton decodeButton = new JButton("Decode");
	
    private JFileChooser fc;

	
	/*
	 * Default Constructor
	 */
	public GUI() {
		this.run();
	}
	
	/*
	 * Listens for ActionEvents and processes them accordingly.
	 */
	public void actionPerformed (ActionEvent e) {
		// find out where the event is coming from
		Object source = e.getSource();
		
		if (source == quitMenuItem) {
			System.exit(0);				// quit
		}
		
		if (source == viewMenuItem) {
			
			Progressindicator task = new Progressindicator();
			task.execute();
		}
		if(source == findButton)
		{
			int returnVal = fc.showOpenDialog(this);
			textField.setText(fc.getSelectedFile().getAbsolutePath());
		}
		if(source == decodeButton)
		{
			Progressindicator task = new Progressindicator();
			task.execute();
		}
	}
	
	
	/*
	 * 
	 * Function that sets up the gui layout.
	 */
	 @Override
	public void run() {
		//Create and set up the window.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // size of window
        setSize(568, 501);
        
        // menu bar
        menuBar = new JMenuBar();				// bar
        fileMenu = new JMenu("File");			// menu
        addMenuItem = new JMenuItem("Add...");	// item
        addMenuItem.addActionListener(this);
        viewMenuItem = new JMenuItem("View");	// item
        /*
        viewMenuItem.addActionListener(new ActionListener() { 
        			 public void actionPerformed(ActionEvent e) { 
        				 Progressindicator task = new Progressindicator();
        				 task.addPropertyChangeListener(new PropertyChangeListener() { 
        					 public void propertyChange(PropertyChangeEvent e) { 
        					 if ("progress".equals(e.getPropertyName())) { 
        						 progressBar.setValue((Integer)e.getNewValue()); 
        					 } 
        					 } 
        					 });
        			task.execute();
        			 }
        			 }
        		);
		*/
        viewMenuItem.addActionListener(this);
        		
        	
        quitMenuItem = new JMenuItem("Quit");	// item
        quitMenuItem.addActionListener(this);
        fileMenu.add(addMenuItem);
        fileMenu.add(viewMenuItem);
        fileMenu.add(quitMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
   
        
        
        //Filechooser
        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        findButton.addActionListener(this);
        decodeButton.addActionListener(this);
        
        //The decode part of GUI
        panel = new JPanel();
        getContentPane().add(panel, BorderLayout.SOUTH);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                	.addComponent(label)
                	.addComponent(decodeButton))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(textField)
                    .addComponent(labeleffect)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(grayscaleBox)
                            .addComponent(wholeCheckBox))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(wrapCheckBox)
                            )))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(findButton)
                    )
            );
            
            layout.linkSize(SwingConstants.HORIZONTAL, findButton , decodeButton);
     
            layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(label)
                    .addComponent(textField)
                    .addComponent(findButton))
                 .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                     .addComponent(decodeButton)
                     .addComponent(labeleffect))
                    
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(grayscaleBox)
                            .addComponent(wrapCheckBox))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(wholeCheckBox)
                            ))
                 
                  )
            );
      
        //Display the window.
        setVisible(true);
      
        
	}
	
	 class Progressindicator extends SwingWorker<Void, Integer> {
		   public Progressindicator()
		   {
		   }
	       @Override
	       public Void doInBackground(){
	    	 Viewer v = new Viewer(textField.getText());
	    
	    	
	    	 while(v.progressindex <= v.listOfImages.length)
	    	 {
		     	setProgress(v.progressindex/v.listOfImages.length);
		     	//publish(v.progressindex);
	    	 }
	    	 	
	         return null;
	       }
	       @Override
	       public void done() {
	    
	       }
	       @Override
	       protected void process(List<Integer> chunks) {
	       }
	     };

}
