import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.event.ChangeEvent;  
import javax.swing.event.ChangeListener;
import java.awt.event.*; 
import java.awt.*; 
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*; 
import java.text.DecimalFormat;

@SuppressWarnings("serial")
public class PaintUygulamasi extends JFrame implements Runnable{
	BufferedImage bufferedimage;
    Thread t;
	JButton fircaBut,cizgiBut,elipsBut,dikBut, kenarlikBut,dolguBut; 
	JSlider transsSlider; 
	JLabel transsLabel;
	DecimalFormat dec = new DecimalFormat("#.##");
	
	Graphics2D grafikAyarlari;

	int sira = 1;                                         

	float transparanDegeri= 1;
	
	Color kenarlikRengi = Color.BLACK, dolguRengi = Color.BLACK; 
	
	public static void main(String[] args) {

		
		new PaintUygulamasi();
		
	}
	
	

	public PaintUygulamasi(){
		BufferedImage bufferedImage;
		
		
		t=new Thread(this);
		t.start();

		this.setSize(2000,1000); 
		this.setTitle("Java Paint Uygulamasý ");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
		JPanel butonPanel = new JPanel();
		
		
		 Box kutu = Box.createHorizontalBox();
		 
		 fircaBut = beniButonYap("firca.png", 1); 
		 cizgiBut = beniButonYap("cizgi.png", 2); 
		 elipsBut = beniButonYap("elips.png", 3);
		 dikBut = beniButonYap("dik.png", 4);
		 kenarlikBut = beniRenkliButonYap("kenarlik.png", 5, true);
		 dolguBut = beniRenkliButonYap("dolgu.png", 6, false);
		 
		 kutu.add(fircaBut);
		 kutu.add(cizgiBut);
		 kutu.add(elipsBut);
		 kutu.add(dikBut);
		 kutu.add(kenarlikBut);
		 kutu.add(dolguBut);
		 
		 transsLabel = new JLabel("Transparan: 1");
		 transsSlider = new JSlider(1,99,99);
		 
		 ListenForSlider sliderL = new ListenForSlider();
		 transsSlider.addChangeListener(sliderL); 
		 
		 kutu.add(transsLabel);
		 kutu.add(transsSlider);
		
		 butonPanel.add(kutu);
		 butonPanel.setBackground(Color.GRAY);
		 
		 transsLabel.setForeground(Color.WHITE);
		
		 
		 this.add(butonPanel, BorderLayout.SOUTH);
		 this.add(new CizimTahtasi(), BorderLayout.CENTER);
		 this.setVisible(true);		 		 		 
		 
	}
	
	
	
	
	
	 public JButton beniButonYap(String iconDosyasi,final int iconSirasi)
	 {
		 
		 try {
			bufferedimage = ImageIO.read(getClass().getResource(iconDosyasi));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 Icon butIcon = new ImageIcon(bufferedimage);
		 JButton but = new JButton(butIcon);
		 
		 but.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e)
			{
				sira = iconSirasi;
			} 
		 });
		 return but; 
		 
	 } 
	 
	 
	 
	 public JButton beniRenkliButonYap(String iconDosyasi, final int iconSirasi, final boolean kenarlikRenk){
		 try {
				bufferedimage = ImageIO.read(getClass().getResource(iconDosyasi));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 Icon butIcon = new ImageIcon(bufferedimage);
			 JButton but = new JButton(butIcon);
		 
		 but.addActionListener(new ActionListener(){  
			 
			public void actionPerformed(ActionEvent e) {
				if(kenarlikRenk)
				{
					kenarlikRengi = JColorChooser.showDialog(null,"Kenarlýk Rengi Seç", Color.BLACK); 
				}
				else
				{
					dolguRengi = JColorChooser.showDialog(null,"Dolgu Rengi Seç", Color.BLACK);
				}
			}
		 });
		 return but;
	 }
	 
	 
	 
	 
	
	 private class CizimTahtasi extends JComponent  
	 {
		 ArrayList<Shape> sekiller = new ArrayList<Shape>();
		 ArrayList<Color> dolguSekli = new ArrayList<Color>(); 
		 ArrayList<Color> kenarlikSekli = new ArrayList<Color>();
		 ArrayList<Float> transYuzdesi = new ArrayList<Float>();
		 
		 
		 Point cizimBaslangic, cizimSon; 
		 
		 public CizimTahtasi(){
			 
			 this.addMouseListener(new MouseAdapter(){ 
				 
				 
				 public void mousePressed(MouseEvent e)
				 {
					 
					 if(sira !=1)		
					 {
					 cizimBaslangic = new Point(e.getX(),e.getY());
					 
					 cizimSon=cizimBaslangic; 
					 repaint();  
					 }
				 }
				 
				 public void mouseReleased(MouseEvent e)
				 {
					 if(sira !=1)   
					 {
						 Shape sekil = null; 
					 
						 if(sira ==2)
						 {                                                         
							 sekil = cizgiCiz(cizimBaslangic.x, cizimBaslangic.y,cizimSon.x, cizimSon.y);
						 }
						 else if (sira == 3)                         
						 {
							 sekil = elipsCiz(cizimBaslangic.x, cizimBaslangic.y,cizimSon.x, cizimSon.y);
						 }
						 
						 else if (sira == 4)
						 {
							 sekil = dikdortgenCiz(cizimBaslangic.x, cizimBaslangic.y,cizimSon.x, cizimSon.y);
						 }
				 
						 sekiller.add(sekil); 
						 
						 dolguSekli.add(dolguRengi);
						 kenarlikSekli.add(kenarlikRengi);
	
						 transYuzdesi.add(transparanDegeri); 					 
			 
						 cizimBaslangic = null;
						 cizimSon = null;
						 repaint();
					 }
				 }
				 }); 
			 
			 
			 
			 
			   this.addMouseMotionListener(new MouseMotionAdapter(){
				   
				   public void mouseDragged(MouseEvent e)
				   {
					  if(sira == 1)

					   {
					   int x = e.getX();
					  int y = e.getY();
					  
					  Shape sekil = null;
					  
					  kenarlikRengi = dolguRengi;
				   
					   sekil = fircaCiz(x,y,5,5);
					  
					   sekiller.add(sekil);
					   dolguSekli.add(dolguRengi);
					   kenarlikSekli.add(kenarlikRengi);
					   transYuzdesi.add(transparanDegeri);
					   }
					   
					   cizimSon = new Point(e.getX(), e.getY());
					   
					   repaint();
				   }
			   
			   });  
			   
			   
		 }
		 
		 
		 
		 public void paint(Graphics g) 
		 {
			 
			  grafikAyarlari = (Graphics2D)g; 

			 grafikAyarlari.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			 
			 grafikAyarlari.setStroke(new BasicStroke(4));
			
			
			
			 Iterator<Color> kenarlikSay = kenarlikSekli.iterator();
			 Iterator<Color> dolguSay = dolguSekli.iterator();
			 Iterator<Float> transSay = transYuzdesi.iterator();
			
			 
			 for(Shape s: sekiller) 
			 {
				
				 grafikAyarlari.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transSay.next())); 
			
				 grafikAyarlari.setPaint(kenarlikSay.next());
				 grafikAyarlari.draw(s);
				 grafikAyarlari.setPaint(dolguSay.next());
				 grafikAyarlari.fill(s);
			 }	 
			 
			 if(cizimBaslangic != null && cizimSon != null)
				 
			 {
				 grafikAyarlari.setPaint(Color.LIGHT_GRAY);
				 
				 Shape sekil = null;
				 
				 if(sira == 2)
				 { 
					 sekil = cizgiCiz(cizimBaslangic.x, cizimBaslangic.y, cizimSon.x, cizimSon.y);
				 }
				 else if (sira == 3)
				 {
					 sekil = elipsCiz(cizimBaslangic.x, cizimBaslangic.y, cizimSon.x, cizimSon.y);
				 }
				 else if (sira == 4)
				 {   
					 sekil = dikdortgenCiz(cizimBaslangic.x, cizimBaslangic.y, cizimSon.x, cizimSon.y);
				 }			 
			
			 grafikAyarlari.draw(sekil); 
			 
			 }
		 }
		 
 
	private Rectangle2D dikdortgenCiz(int x1, int y1, int x2, int y2)
	{
	
		int x = Math.min(x1, x2);
		int y = Math.min(y1, y2);
	
		int genislik = Math.abs(x1 - x2);
		int yukseklik = Math.abs(y1-y2);
		

		return new Rectangle2D.Float(x, y, genislik, yukseklik);
		
	}

	private Ellipse2D.Float elipsCiz(int x1, int y1, int x2, int y2)
	{		
		int x = Math.min(x1, x2);
		int y = Math.min(y1, y2);
		
		int genislik = Math.abs(x1 - x2);
		int yukseklik = Math.abs(y1-y2);
		
		return new Ellipse2D.Float(x, y, genislik, yukseklik);
	}

	private Line2D.Float cizgiCiz(int x1, int y1, int x2, int y2)
	{
		return new Line2D.Float(x1, y1, x2, y2);
	}
	
	private Ellipse2D.Float fircaCiz(int x1, int y1, int fircaKenarlikGenisligi, int fircaKenarlikYuksekligi )
	{
		return new Ellipse2D.Float(x1,y1,fircaKenarlikGenisligi,fircaKenarlikYuksekligi);
	}
	
	
	
	
		 } 

	 
	 
		
	private class ListenForSlider implements ChangeListener{

		
		public void stateChanged(ChangeEvent e) { 
			

				transsLabel.setText("Transparan " + dec.format(transsSlider.getValue() * 0.01));

			 transparanDegeri = (float)(transsSlider.getValue() * 0.01);
		}
		
	}
	public void run(){
		while(true)
		{
			try {


				  AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("muzik.wav"));
			        Clip clip = AudioSystem.getClip();
			        clip.open(audioInputStream);
			        clip.start();
			        
			    } catch(Exception ex) {
			        System.out.println("Error with playing sound.");
			        ex.printStackTrace();
			    }
				try {
					Thread.sleep(15000);
				} catch (InterruptedException e) {
				
					e.printStackTrace();
				}
		
			}
		}
	}
 
