package dk.aau.cs.ds302e18.app;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

class Canvas
{
    static void upload(String bucketName, String imageName, String imageData)
    {
        AmazonS3 s3 = new AmazonS3Client();
        Region region = Region.getRegion(Regions.EU_WEST_2);
        s3.setRegion(region);

        try
        {
            s3.putObject(new PutObjectRequest(bucketName, imageName+".png", createFile(imageName, imageData)));
        }
        catch (IOException ioException)
        {
            System.out.println(ioException.getMessage());
        }

    }

    private static File createFile(String imageName, String imageData) throws IOException
    {
        File file = null;

        byte[] imageBytes = DatatypeConverter.parseBase64Binary(imageData.substring(imageData.indexOf(",") + 1));
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));

        file = new File(imageName+".png");

        ImageIO.write(bufferedImage, "png", file);

        return file;
    }
}
