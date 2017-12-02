import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class ImageEditor {
	private static BufferedImage img;

	// public static void main(String[] args) {
	// img = paintSimpleImage();
	// String file = args[0];
	// try {
	// img = ImageIO.read(new File(file));
	// } catch (IOException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// if (args.length == 3) {
	// ImageEditor.increaseContrastAndBrightness(img, Double.parseDouble(args[1]),
	// Integer.parseInt(args[2]));
	//
	// } else if (args.length == 2) {
	//// ImageEditor.makeMonochrome(img);
	// ImageEditor.meanBlur();
	// }
	//
	// try {
	// ImageIO.write(img, "PNG", new File("painted.png"));
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }

	public static void main(String[] args) {
		String file = args[0];
		try {
			img = ImageIO.read(new File(file));
			ImageEditor.findEdges();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		if (args[0].equals("p")) {
//			ImageEditor.paintSimpleImage();
//			try {
//				ImageIO.write(img, "PNG", new File("computerpainted"));
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} else if (args[0].equals("e")) {
//			switch (args[1]) {
//			case "contrast":
//				file = args[2];
//				try {
//					img = ImageIO.read(new File(file));
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				ImageEditor.changeContrast(Double.parseDouble(args[3]));
//				try {
//					ImageIO.write(img, "PNG", new File(file + "_changed_contrast"));
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				break;
//			case "brightness":
//				file = args[2];
//				try {
//					img = ImageIO.read(new File(file));
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				ImageEditor.changeBrightness(Integer.parseInt(args[3]));
//				try {
//					ImageIO.write(img, "PNG", new File(file + "_changed_brightness"));
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				break;
//			case "blur":
//				file = args[2];
//				try {
//					img = ImageIO.read(new File(file));
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				ImageEditor.meanBlur();
//				try {
//					ImageIO.write(img, "PNG", new File(file + "_changed_blur"));
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				break;
//			}
//		}else {
//			System.out.println("Unknown command: " + args[0]);
//		}

	}
	
	
	public static void findEdges() {
		BufferedImage image = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
		WritableRaster inRaster = img.getRaster();
		WritableRaster outRaster = image.getRaster();
		//vertical 
		int[][] vKernel = {{-1, 0, 1}, { -2, 0, 2 }, { -1, 0, 1}};
		int[][] hKernel = {{-1, -2, -1}, { 0, 0, 0 }, { 1, 2, 1}};

		for(int i = 0; i < inRaster.getWidth(); i++) {
			for(int j = 0; j < inRaster.getHeight(); j++) {
				int yGradient = 0;
				int xGradient = 0;
				int outPixelValue = 0;
				int mI = 0; int mJ = 0;
				for(int k = i -1; k < i+2; k++ ) {
					mJ = 0;
					for(int l = j-1; l < j+2; l++) {
						try {
							yGradient += inRaster.getSample(k, l, 0) * vKernel[mI][mJ];
							xGradient += inRaster.getSample(k, l, 0) * hKernel[mI][mJ];
							mJ++;
						}catch(Exception e) {
							mJ++;
						}
					}
					mI++;
				}
				if(yGradient > 150 || yGradient < -150 || xGradient > 150 || xGradient < -150) {
					outPixelValue = 0;
				}else {
					outPixelValue = 255;
				}
				outRaster.setSample(i, j, 0, outPixelValue);
			}
		}
		try {
			ImageIO.write(image, "PNG", new File("modified.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void paintSimpleImage() {
		int width = 250;
		int height = 250;
		final int WHITE = 255;
		final int BLACK = 0;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster raster = image.getRaster();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (j % 2 == 0) {
					raster.setSample(i, j, 0, WHITE);
				} else {
					raster.setSample(i, j, 0, BLACK);
				}
			}
		}

		img = image;
	}
	
	public static void analyzeImage() {
		WritableRaster raster = img.getRaster();
		for(int i = 0; i < raster.getWidth(); i++) {
			for(int j = 0; j < raster.getHeight(); j++) {
				for(int k = 0; k < raster.getNumBands(); k++) {
					System.out.println(raster.getSample(i, j, k));
				}
			}
		}
	}

	public static void meanBlur() {
		BufferedImage inImage = img;
		BufferedImage outImage = new BufferedImage(inImage.getWidth(), inImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		WritableRaster inRaster = inImage.getRaster();
		WritableRaster outRaster = outImage.getRaster();
		int[][] filterMatrix = new int[15][15];
		// init filterMatrix
		for (int i = 0; i < filterMatrix.length; i++) {
			for (int j = 0; j < filterMatrix[i].length; j++) {
				filterMatrix[i][j] = 1;
			}
		}

		for (int width = 0; width < inRaster.getWidth(); width++) {
			for (int height = 0; height < inRaster.getHeight(); height++) {
				for (int channels = 0; channels < inRaster.getNumBands(); channels++) {
					int matrixAreaValue = 0;
					int missedPixelCounter = 0;
					int mI = 0, mJ = 0;

					for (int i = width - 1; i < width + 14; i++) {
						mJ = 0;
						for (int j = height - 1; j < height + 14; j++) {
							try {

								matrixAreaValue += inRaster.getSample(i, j, channels) * filterMatrix[mI][mJ];
								mJ++;
							} catch (Exception e) {
								missedPixelCounter++;
								mJ++;
							}
						}
						mI++;
					}
					if(matrixAreaValue != 0) {
						matrixAreaValue = matrixAreaValue / (225-missedPixelCounter);
					}
					int outPixelValue = matrixAreaValue;
					 if(outPixelValue > 255) {
					 outPixelValue = 255;
					 }else if(outPixelValue < 0) {
					 outPixelValue = 0;
					 }
					outRaster.setSample(width, height, channels, outPixelValue);

				}
			}
		}
		try {
			System.out.println("Writing image to file");
			ImageIO.write(outImage, "PNG", new File("modified.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public static void changeContrast(double contrast) {
		BufferedImage image = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		WritableRaster inRaster = img.getRaster();
		WritableRaster outRaster = image.getRaster();
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
		img = image;
	}

	public static void changeBrightness(int brightness) {
		BufferedImage image = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		WritableRaster inRaster = img.getRaster();
		WritableRaster outRaster = image.getRaster();
		for (int width = 0; width < inRaster.getWidth(); width++) {
			for (int height = 0; height < inRaster.getHeight(); height++) {
				for (int colour = 0; colour < inRaster.getNumBands(); colour++) {
					int rgbPixelValue = inRaster.getSample(width, height, colour);
					if (brightness > 0) {
						rgbPixelValue = rgbPixelValue + brightness;
					} else {
						rgbPixelValue = rgbPixelValue - brightness;
					}
					if (rgbPixelValue > 255) {
						rgbPixelValue = 255;
					} else if (rgbPixelValue < 0) {
						rgbPixelValue = 0;
					}
					outRaster.setSample(width, height, colour, rgbPixelValue);
				}
			}
		}
		img = image;
	}

	public static void makeMonochrome(BufferedImage inImage) {
		BufferedImage image = new BufferedImage(inImage.getWidth(), inImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster inRaster = inImage.getRaster();
		WritableRaster outRaster = image.getRaster();
		int red = -1, green = -1, blue = -1;
		for (int width = 0; width < inRaster.getWidth(); width++) {
			for (int height = 0; height < inRaster.getHeight(); height++) {
				for (int colour = 0; colour < inRaster.getNumBands(); colour++) {
					if (colour == 0) {
						red = inRaster.getSample(width, height, colour);
					} else if (colour == 1) {
						blue = inRaster.getSample(width, height, colour);
					} else if (colour == 2) {
						green = inRaster.getSample(width, height, colour);
						outRaster.setSample(width, height, 0, (red + green + blue) / 3);
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
