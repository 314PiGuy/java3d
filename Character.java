
import java.awt.image.BufferedImage;

public class Character {

    private BufferedImage sprite;
    private double x, y;

    public Character(double x, double y, BufferedImage im){
        this.x = x;
        this.y = y;
        sprite = im;
    }

    public BufferedImage getSprite(){
        return sprite;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }
}
