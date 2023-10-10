import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public class readimage{
    public static void main(String[] args) {
        // Specify the path to your JPG image file
        String inputImagePath1 = "IMG_2289.jpg";
        String outputImagePath1 = "out1.jpg"; // Specify the path for the output image
        String inputImagePath2 = "IMG_2285.jpg";
        String outputImagePath2 = "out2.jpg"; // Specify the path for the output image
        String outputImagePathr = "outr.jpg"; // Specify the path for the output image
        String outputImagePathg = "outg.jpg"; // Specify the path for the output image
        String outputImagePathb = "outb.jpg"; // Specify the path for the output image

        Scanner scanner = new Scanner(System.in);
        float hueTarget = 0.666f;
//        System.out.print("Enter the hue target: ");
//        hueTarget = scanner.nextFloat();
        float hueRange= 0.2f;
//        System.out.print("Enter the hue range: ");
//        hueRange= scanner.nextFloat();
        float sat= 0.2f;
//        System.out.print("Enter the saturatioh threshold: ");
//        sat= scanner.nextFloat();
        float val= 0.1f;
//        System.out.print("Enter the value threshold: ");
//        val= scanner.nextFloat();
	
	int pixelcount=0;
	int xavg=0;
	int yavg=0;

        System.out.println("Hue target is: " + hueTarget);
        System.out.println("Hue range is: " + hueRange);

        try {
		// Read the input image files into BufferedImage
		File inputImageFile1 = new File(inputImagePath1);
		BufferedImage inputImage1 = ImageIO.read(inputImageFile1);
		File inputImageFile2 = new File(inputImagePath2);
		BufferedImage inputImage2 = ImageIO.read(inputImageFile2);
		
		// Get the dimensions of the input image
		int width1 = inputImage1.getWidth();
		int height1 = inputImage1.getHeight();
		int width2 = inputImage2.getWidth();
		int height2 = inputImage2.getHeight();

		if ((width1 != width2) || (height1 != height2))
		{
	            System.out.println("Images do not have the same dimensions ");
		}

		Map<Float, Integer> histogram = new HashMap<>();

		// Create a new BufferedImage with the same dimensions
		BufferedImage outputImage1 = new BufferedImage(width1, height1, BufferedImage.TYPE_INT_RGB);
		BufferedImage outputImage2 = new BufferedImage(width2, height2, BufferedImage.TYPE_INT_RGB);
		BufferedImage outputImager= new BufferedImage(width2, height2, BufferedImage.TYPE_INT_RGB);
		BufferedImage outputImageg= new BufferedImage(width2, height2, BufferedImage.TYPE_INT_RGB);
		BufferedImage outputImageb= new BufferedImage(width2, height2, BufferedImage.TYPE_INT_RGB);
		
		float hueUL=hueTarget+hueRange - (float) Math.floor(hueTarget+hueRange);
		float hueLL=hueTarget-hueRange - (float) Math.floor(hueTarget-hueRange);
		float hueMax=-1, hueMin=-1;
	        System.out.println("Filter limits set to: " + hueLL + " : " + hueUL);

		// Copy pixel data from the input image to the output image
	        System.out.println("Image Height: " + height1);
	        System.out.println("Image Width: " + width1);

		ArrayList<Integer> listOfx= new ArrayList<>();
		for (int y = 0; y < height1; y++) {
	                for (int x = 0; x < width1; x++) {

//				**** IMAGE 1 *****
			       	// Get the RGB value of the pixel from the input 1 image
	                   	Color rgb1 = new Color(inputImage1.getRGB(x, y));
				int red = rgb1.getRed();
				int green = rgb1.getGreen();
				int blue = rgb1.getBlue();
                   		// Convert color value to hsv
				float[] hsv1 = new float[3]; 
				Color.RGBtoHSB(red,green,blue,hsv1);
				if (x==0 && y==0) {
					hueMax = hsv1[0];
					hueMin = hsv1[0];
				} else 	{
					if (hueMax<hsv1[0]) hueMax=hsv1[0];
					if (hueMin>hsv1[0]) hueMin=hsv1[0];
				}
	
				// Test whether the pixel hue is within hueRange of hueTarget
				boolean inRange1;
				if (hueUL > hueLL)
				{
					inRange1 = (hsv1[0] > hueLL) && (hsv1[0] < hueUL);
				} else
				{
					inRange1 = (hsv1[0] < hueLL) || (hsv1[0] > hueUL);
				}
				inRange1 = inRange1 && (hsv1[1]>sat);
				inRange1 = inRange1 && (hsv1[2]>val);

				// Copy the pixel only if hue is within hueRange of hueTarget
				if (inRange1)
				{
		                    outputImage1.setRGB(x, y, rgb1.getRGB());
//		                    outputImage1.setRGB(x, y, 0xFFFFFF);
					pixelcount++;
					xavg += x;
					yavg += y;
					listOfx.add(x);
				} else 
				{
		                    outputImage1.setRGB(x, y, 0);
				}
	
			}
		}
		Collections.sort(listOfx);
		int xmedian=listOfx.get(pixelcount/2);
// Draw a vertical line at xaxg
		xavg =  xavg/pixelcount;
		for (int y = 0; y < height1; y++) {
                    outputImage1.setRGB(xmedian, y, 0xFFFFFF);
		}
// Draw a horizontal line at yaxg
		yavg =  yavg/pixelcount;
//		for (int x = 0; x < width1; x++) {
//                    outputImage1.setRGB(x, yavg, 0xFFFFFF);
//		}
			

            // Save the output images
            File outputImageFile1 = new File(outputImagePath1);
            ImageIO.write(outputImage1, "jpg", outputImageFile1);

            System.out.println("Images copied, filtered and saved successfully to " + outputImagePath1);
            System.out.println("Hue max: " + hueMax);
            System.out.println("Hue min: " + hueMin);
            System.out.println("pixel count: " + (pixelcount));
            System.out.println("Avg x position: " + (xavg));
            System.out.println("Median x position: " + (xmedian));
            System.out.println("Avg y position: " + (yavg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}





