import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class ImageEditor {
	
	public static void main(String[] args) {
		if(args.length == 3) {
			try {
				String file = args[0];
				BufferedImage img = ImageIO.read(new File(file));
				ImageEditor editor = new ImageEditor();
				editor.increaseContrastAndBrightness(img, Double.parseDouble(args[1]), Integer.parseInt(args[2]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(args.length == 2) {
			try {
				String file = args[0];
				BufferedImage img = ImageIO.read(new File(file));
				ImageEditor editor = new ImageEditor();
				editor.makeMonochrome(img);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}
	
	public void increaseContrastAndBrightness(BufferedImage inImage, double contrast, int brightness) {
		BufferedImage image = new BufferedImage(inImage.getWidth(), inImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		WritableRaster inRaster = inImage.getRaster();
		WritableRaster outRaster = image.getRaster();
		System.out.println("inImage dimensions: " + inImage.getWidth() + " " +  inImage.getHeight());
		System.out.println("outimage dimensions: " + image.getWidth() + " " + image.getHeight());
		for(int width = 0; width < inRaster.getWidth(); width++) {
			for(int height = 0; height < inRaster.getHeight(); height++) {
				for(int colour = 0; colour < inRaster.getNumBands(); colour++) {
					int rgbPixelValue = inRaster.getSample(width, height, colour);
					rgbPixelValue = (int) (((((rgbPixelValue /255.0) - 0.5) * contrast) + 0.5) * 255.0) + brightness;
					if( rgbPixelValue > 255) {
						rgbPixelValue = 255;
					}else if(rgbPixelValue < 0){
						rgbPixelValue = 0;
					}
					outRaster.setSample(width, height, colour, rgbPixelValue);
				}
			}
		}
		try {
			ImageIO.write(image, "PNG", new File("modified.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void makeMonochrome(BufferedImage inImage) {
		BufferedImage image = new BufferedImage(inImage.getWidth(), inImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster inRaster = inImage.getRaster();
		WritableRaster outRaster = image.getRaster();
		int red = -1, green = -1, blue = -1;
		for(int width = 0; width < inRaster.getWidth(); width++) {
			for(int height = 0; height < inRaster.getHeight(); height++) {
				for(int colour = 0; colour < inRaster.getNumBands(); colour++) {
					if(colour == 0) {
						red = inRaster.getSample(width, height, colour);
					}else if(colour == 1) {
						blue = inRaster.getSample(width, height, colour);
					}else if(colour == 2) {
						green = inRaster.getSample(width, height, colour);
						outRaster.setSample(width, height, 0, (red+green+blue)/3);
					}
				}
			}
		}
		try {
			ImageIO.write(image, "PNG", new File("monochrome.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
