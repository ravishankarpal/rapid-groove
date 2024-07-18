package com.rapid.service;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class Text {

    public static void main(String[] args) {
        try {
            // Load the JPEG image
            String userHome = System.getProperty("user.home");
            String desktopPath = userHome + File.separator + "Desktop";

            // Load the JPEG image from the desktop directory
            File input = new File(desktopPath + File.separator + "ravi_shankar_pal.jpg");
            BufferedImage originalImage = ImageIO.read(input);

            // Resize the image to 350x350
            BufferedImage resizedImage = resizeImage(originalImage, 350, 350);

            // Convert and save as JPG with size constraints
            File output = new File("output.jpg");
            saveJPGWithSizeConstraints(resizedImage, output, 20 * 1024, 100 * 1024); // 20 KB to 100 KB

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to resize the image
    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        return resizedImage;
    }

    // Method to save the image with size constraints
    private static void saveJPGWithSizeConstraints(BufferedImage image, File output, long minSize, long maxSize) throws IOException {
        float quality = 1.0f; // Start with the best quality
        boolean success = false;
        while (!success) {
            // Create an ImageWriter for JPG format
            ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
            ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();

            // Set the compression quality
            jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            jpgWriteParam.setCompressionQuality(quality);

            // Write the image to a temporary file
            File tempFile = new File("temp.jpg");
            try (ImageOutputStream ios = ImageIO.createImageOutputStream(tempFile)) {
                jpgWriter.setOutput(ios);
                IIOImage outputImage = new IIOImage(image, null, null);
                jpgWriter.write(null, outputImage, jpgWriteParam);
            }
            jpgWriter.dispose();

            // Check the file size
            long fileSize = tempFile.length();
            if (fileSize >= minSize && fileSize <= maxSize) {
                // File size is within the constraints, rename the file
                if (tempFile.renameTo(output)) {
                    success = true;
                }
            } else if (fileSize < minSize) {
                // If the file size is less than the minimum, reduce quality
                quality = Math.max(0, quality - 0.05f);
            } else {
                // If the file size is greater than the maximum, increase quality
                quality = Math.min(1.0f, quality + 0.05f);
            }

            // Delete the temporary file
            tempFile.delete();

            // If quality adjustment is not feasible
            if (quality <= 0.05f || quality >= 0.95f) {
                break;
            }
        }
    }
}
