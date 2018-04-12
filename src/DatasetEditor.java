import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class DatasetEditor {
	private static BufferedImage image = new BufferedImage(1, 1, 1); // null for now, images might be held in a
																		// structure

	public static void main(String[] args) {
		String file = args[0];
		try {
			image = ImageIO.read(new File(file));
			DatasetEditor.flipVertically();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void setContrast(double contrast) {
		BufferedImage outImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		WritableRaster inRaster = image.getRaster();
		WritableRaster outRaster = outImage.getRaster();
		for (int width = 0; width < inRaster.getWidth(); width++) {
			for (int height = 0; height < inRaster.getHeight(); height++) {
				for (int colour = 0; colour < inRaster.getNumBands(); colour++) {
					int rgbPixelValue = inRaster.getSample(width, height, colour);
					rgbPixelValue = (int) (((((rgbPixelValue / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
					if (rgbPixelValue > 255) {
						rgbPixelValue = 255;
					} else if (rgbPixelValue < 0) {
						rgbPixelValue = 0;
					}
					outRaster.setSample(width, height, colour, rgbPixelValue);
				}
			}
		}
		try {
			ImageIO.write(outImage, "PNG", new File("datasetmodified.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Sets brightness by some factor in the range of 0.1 to 2. 1 increases
	 * brightness by 10% and -3 decreases brightness by 30%
	 * 
	 * @param factor
	 */

	public static void setBrightness(int intensity) {
		BufferedImage outImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

		WritableRaster inRaster = image.getRaster();
		WritableRaster outRaster = outImage.getRaster();
		for (int widthPos = 0; widthPos < inRaster.getWidth(); widthPos++) {
			for (int heightPos = 0; heightPos < inRaster.getHeight(); heightPos++) {
				for (int colorBand = 0; colorBand < inRaster.getNumBands(); colorBand++) {
					int rgbPosValue;

					if (intensity > 0) {
						rgbPosValue = (int) (inRaster.getSample(widthPos, heightPos, colorBand) + intensity);

					} else {
						rgbPosValue = (int) (inRaster.getSample(widthPos, heightPos, colorBand) - intensity);

					}

					if (rgbPosValue > 255) {
						rgbPosValue = 255;
					} else if (rgbPosValue < 0) {
						rgbPosValue = 0;
					}
					outRaster.setSample(widthPos, heightPos, colorBand, rgbPosValue);

				}
			}
		}

		try {
			ImageIO.write(outImage, "PNG", new File("datasetmodified.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Blurs the image Int from range 2 to 100.
	 * 
	 * @param blurIntensity
	 */
	public static void meanBlur(int blurIntensity) {
		BufferedImage outImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		int[][] blurMatrix = new int[blurIntensity][blurIntensity];
		WritableRaster inRaster = image.getRaster();
		WritableRaster outRaster = outImage.getRaster();

		for (int i = 0; i < blurMatrix.length; i++) {
			for (int j = 0; j < blurMatrix[i].length; j++) {
				blurMatrix[i][j] = 1;
			}
		}

		for (int widthPos = 0; widthPos < inRaster.getWidth(); widthPos++) {
			for (int heightPos = 0; heightPos < inRaster.getHeight(); heightPos++) {
				for (int colorBand = 0; colorBand < inRaster.getNumBands(); colorBand++) {

					int matrixAreaValue = 0;
					int missedPixelCounter = 0;
					int mI = 0, mJ = 0;

					for (int i = widthPos - 1; i < widthPos + (blurIntensity - 1); i++) {
						mJ = 0;
						for (int j = heightPos - 1; j < heightPos + (blurIntensity - 1); j++) {
							try {

								matrixAreaValue += inRaster.getSample(i, j, colorBand) * blurMatrix[mI][mJ];
								mJ++;
							} catch (Exception e) {
								missedPixelCounter++;
								mJ++;
							}
						}
						mI++;
					}
					if (matrixAreaValue != 0) {
						matrixAreaValue = matrixAreaValue / ((blurIntensity * blurIntensity) - missedPixelCounter);
					}
					int outPixelValue = matrixAreaValue;
					if (outPixelValue > 255) {
						outPixelValue = 255;
					} else if (outPixelValue < 0) {
						outPixelValue = 0;
					}
					outRaster.setSample(widthPos, heightPos, colorBand, outPixelValue);

				}
			}
		}
		try {
			ImageIO.write(outImage, "PNG", new File("datasetmodified.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void generateNoise(int noiseIntensity, int noiseFrequency) {
		BufferedImage outImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		WritableRaster inRaster = image.getRaster();
		WritableRaster outRaster = outImage.getRaster();
		Random random = new Random();
		for (int widthPos = 0; widthPos < inRaster.getWidth(); widthPos += noiseFrequency) {
			for (int heightPos = 0; heightPos < inRaster.getHeight(); heightPos += noiseFrequency) {
				for (int colorBand = 0; colorBand < inRaster.getNumBands(); colorBand++) {
					int rgbPosValue = inRaster.getSample(widthPos, heightPos, colorBand);
					outRaster.setSample(widthPos, heightPos, colorBand, rgbPosValue += random.nextInt(noiseIntensity));
				}
			}
		}

		try {
			ImageIO.write(outImage, "JPG", new File("datasetmodified.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void flipVertically() {
		BufferedImage outImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		WritableRaster inRaster = image.getRaster();
		WritableRaster outRaster = outImage.getRaster();
		
		for(int widthPos = 0; widthPos < inRaster.getWidth(); widthPos++) {
			for(int heightPos = 0; heightPos < inRaster.getWidth(); widthPos++) {
				for(int colorChannels = 0; colorChannels < inRaster.getNumBands(); colorChannels++) {
					int tempValue = 
				}
			}
		}
		
		try {
			ImageIO.write(outImage, "PNG", new File("datasetmodified.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void flipHorizontally() {

	}

	public static void setSaturation() {

	}

	public static void setHue() {

	}

}
