
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class TextureLoader {
    
    public static BufferedImage wallTexture;
    public static BufferedImage wallTexture2;
    public static BufferedImage floorTexture;
    public static BufferedImage floorTexture2;

    public static void init() throws IOException{
        wallTexture = ImageIO.read(new File("textures/brickwall.jpg"));
    }

}