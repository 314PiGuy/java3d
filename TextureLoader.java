
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class TextureLoader {
    
    public static BufferedImage wallTexture, floorTexture, spriteTexture, projectileTexture;

    public static BufferedImage loadImageAsRGB(String filePath) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(filePath));

        if (originalImage.getType() != BufferedImage.TYPE_INT_RGB) {
            BufferedImage convertedImage = new BufferedImage(
                    originalImage.getWidth(),
                    originalImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB);

            Graphics2D g = convertedImage.createGraphics();

            g.drawImage(originalImage, 0, 0, null);
            g.dispose();

            return convertedImage;
        }

        return originalImage;
    }

    public static void init() throws IOException{
        wallTexture = ImageIO.read(new File("textures/bars.png"));
        floorTexture = loadImageAsRGB("textures/woodfloor.jpg");
        spriteTexture = ImageIO.read((new File("textures/doomsprite.png")));
        projectileTexture = ImageIO.read(new File("textures/projectile.png"));
    }

}