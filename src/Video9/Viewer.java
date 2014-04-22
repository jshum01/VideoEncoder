package Video9;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.ProgressMonitor;
//import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;


@SuppressWarnings("serial")
public class Viewer extends JFrame {
	private BufferedImage image;
	private static int index;
	private JPanel contentPane;
	
  //holds the compressed "video" file
	protected static Vector <ImageBreakDown> video;
	
	//holds the images (pre-compression) loaded from the folder
	static BufferedImage[] listOfImages;

	public int progressindex =0;
	private String folder;
/*
	public static void main(String[] args) throws IOException {

		//initialize the vector used to store broken down images
		video = new Vector <ImageBreakDown>();
		
		//initialize the index used to traverse the "video" structure
		index = 0;

    //path to the folder containing the images*********************************************************************
		String folder = "C:/Users/osa/Desktop/images";
		
		//load images
		getFiles(folder);
		
		//breakdown the images. breakdown() function also adds the broken down images to the video structure
		for(BufferedImage img : listOfImages)
			breakdown(img);

    //create a new Panel, the only panel used by this iteration of the function
		final Panel frame = new Panel();
		
		//make frame visible
		frame.setVisible(true);

    //manages the order in which the threads are executed. It is necessary to do something similar for gui modification
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
				new Runnable() {
					@Override
					public void run() {
						//if(index!=-1)
						frame.changeImage();
					}
					//you can change the time between frames here
				}, 0, 100, TimeUnit.MILLISECONDS);
	}
*/
	/*
	 public static class Task implements Observer {    
	     private JProgressBar pb;  
	     //private int progress;
		 public Task(JProgressBar pb){
			this.pb = pb;
	    	  }
		@Override
		public void update(Observable o, Object arg) {
			// TODO Auto-generated method stub
			pb.setValue(progressindex);
		}
	 }
	 */
	public Viewer(String folder) {
		//initialize the vector used to store broken down images
				video = new Vector <ImageBreakDown>();
				 //initialize the index used to traverse the "video" structure
				index = 0;

		    //path to the folder containing the images*********************************************************************
				//String folder = "/Users/jshum01/Pictures/Imagesforprogram";
				this.folder = folder;
				//load images
				getFiles(this.folder);
				ProgressMonitor progressMonitor = new ProgressMonitor(this,
                        "Encoding In Progress",
                        "", 0, listOfImages.length);
				int progress = 0;
				//String message = String.format("Completed %d%%.\n", progress);
				//progressMonitor.setNote(message);
				progressMonitor.setProgress(progress);
				//breakdown the images. breakdown() function also adds the broken down images to the video structure
				
				for(BufferedImage img : listOfImages)
				{
					breakdown(img);
					progressMonitor.setProgress(progressindex++);
					progressMonitor.setNote(String.format("Encoding image %d of %d.\n",progressindex,listOfImages.length));
					if(progressMonitor.isCanceled())
						break;
				}
				progressMonitor.close();
				
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, video.get(0).width, video.get(0).height);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		JLabel lblNewLabel = new JLabel(new ImageIcon("simple.png"));
		contentPane.add(lblNewLabel);
		
		//make frame visible
				this.setVisible(true);
				
		    //manages the order in which the threads are executed. It is necessary to do something similar for gui modification
				Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
						new Runnable() {
							@Override
							public void run() {
								//if(index!=-1)
								
								changeImage();
							}
							//you can change the time between frames here
						}, 0, 100, TimeUnit.MILLISECONDS);

	}
	

	//changes the image being displayed in the panel
	private void changeImage() {
		final BufferedImage img = nextImage();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				image = img;
				repaint();
				
			}
		});
	}

  //overwrites default paint function so as to keep image we are modifying in context
	public void paint(Graphics g) {
		if (image != null) {
			g.drawImage(image, 0, 0, null);
		}
	}

	//gets the next image in the array list, circles around if it is done
	private BufferedImage nextImage() {
		index++;
		if(index>=video.size()) index=0;
		return buildup(index);
	}

	//breaks down image to pixels for compression
	static void breakdown(BufferedImage hugeImage) {
		final int width = hugeImage.getWidth();
		final int height = hugeImage.getHeight();
		final boolean hasAlpha = hugeImage.getAlphaRaster() != null;
		final int[] pixels = new int[width*height];

		for(int i=0; i<width; i++)
			for(int j=0; j<height; j++)
				pixels[(i*height)+j] = hugeImage.getRGB(i, j);
		video.addElement(new ImageBreakDown(height, width, hasAlpha, pixels));
	}

	//reconstitutes the image at the specified index from an ImageBreakDown object
	static BufferedImage buildup(int index){
		ImageBreakDown image = video.get(index);
		BufferedImage img = new BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB);
		int h = image.height;
		int w = image.width;
		for(int i=0; i<w; i++)
			for(int j=0; j<h; j++)
				img.setRGB(i, j, image.pixels[(i*h)+j]);
		return img;
	}

	//loads images from the file given
	static BufferedImage Loadimage(File file){
		BufferedImage Image = null;

		try {
			Image = ImageIO.read(file);
		} catch (IOException e) {
		}
		return Image;
	}

	//collects image files into an array of files
	public static void getFiles(String path){

		int i=0;
		File folder = new File(path);
		listOfImages = new BufferedImage[folder.listFiles().length];
		if (folder.isDirectory()) {
			for (final File fileEntry : folder.listFiles()) {
				listOfImages[i] = Loadimage(fileEntry);
				i++;
			}
			
		}
	}
}
