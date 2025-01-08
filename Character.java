
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Character {

    private BufferedImage sprite;
    private double x, y;
    private int reload = 0;
    public boolean dead = false;
    private boolean seesPlayer = false;
    private double randompath = -10;
    private double angle = 0;
    private int hp = 100;

    public Character(double x, double y, BufferedImage im) {
        this.x = x;
        this.y = y;
        sprite = im;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public ArrayList<Projectile> move(double x, double y, ArrayList<Character> v, ArrayList<Projectile> p, int[][] m,
            int ra) {
        if (randompath == -10 && !seesPlayer)
            randompath = ra * Math.PI / 180;
        angle = randompath;
        if (this.x < x && seesPlayer)
            angle = Math.PI - angle;
        if (this.x < y && seesPlayer)
            angle *= -1;
        if (angle < 0 && seesPlayer)
            angle += 2 * Math.PI;
        // std::cout << boost << "\n";
        double oldX = this.x;
        double oldY = this.x;
        double vx = -0.06 * Math.cos(angle);
        double vy = -0.06 * Math.sin(angle);
        this.x += vx;
        this.y += vy;
        if (collidingWith(x, y, oldX, oldY, v, m)) {
            this.x -= vx;
        }

        if (collidingWith(x, y, oldX, oldY, v, m)) {
            this.x -= vy;
        }
        if (reload == 0) {
            if (seesPlayer) {
                p = shoot(angle, p);
                reload = 50;
            }
        } else {
            reload--;
        }
        for (int i = 0; i < p.size(); i++) {
            if (p.get(i).isFromPlayer()) {
                if (Math.sqrt((this.x - p.get(i).getX()) * (this.x - p.get(i).getX())
                        + (this.y - p.get(i).getY()) * (this.y - p.get(i).getY())) < 0.8) {
                    p.get(i).kill();
                    damage(10);
                    return p;
                }
            }
        }
        seesPlayer = false;
        if (canSeePlayer(x, y, m)) {
            seesPlayer = true;
        }
        return p;
    }

    public ArrayList<Projectile> shoot(double angle, ArrayList<Projectile> v) {
        double vx = 0.3 * Math.cos(angle);
        double vy = 0.3 * Math.sin(angle);

        Projectile p = new Projectile(this.x, this.y, -vx, -vy, false);
        v.add(p);
        return v;
    }

    public boolean collidingWith(double px, double py, double oldX, double oldY, ArrayList<Character> v, int[][] m){
        if (m[(int)this.x][(int)this.y] != 0){
            randompath = -10;
            return true;
        }
        if (Math.abs(px-this.x) < 1 && Math.abs(py-this.y) < 1){
            //randompath = -10;
            return true;
        }
        for (int i = 0; i < v.size(); i++){
            Character e = v.get(i);
            if (e.getX() == oldX && e.getY() == oldY){
                return false;
            }
            else if (Math.abs(e.getX()-this.x) < 2 && Math.abs(e.getY()-this.y) < 2){
                return true;
            }
        }
        return false;
    }

    public void damage(int d){
        hp-=34;
        if (hp <= 0) dead = true;
    }

    public int getHP(){
        return hp;
    }


    public ArrayList<Projectile> isHit(ArrayList<Projectile> p){
        for (int i = 0; i < p.size(); i++){
            if (p.get(i).isFromPlayer()){
                if (Math.sqrt((this.x-p.get(i).getX())*(this.x-p.get(i).getX())+(this.y-p.get(i).getY())*(this.y-p.get(i).getY())) < 0.8){
                    p.get(i).kill();
                    return p;
                }
            }
        }
        return p;
    }
    

    public boolean canSeePlayer(double x, double y, int[][] m){
        double angle = Math.atan(Math.abs(y-this.y)/Math.abs(x-this.x));
        if (this.x > x) angle = Math.PI-angle;
        if (this.y > y) angle *= -1;
        if (angle < 0) angle+=2*Math.PI;
        int playerX = (int)x;
        int playerY = (int)y;
        double raydirX = Math.cos(angle);
        double raydirY = Math.sin(angle);
        double dx = (raydirX == 0) ? 1e20 : Math.abs(1 / raydirX);
        double dy = (raydirY == 0) ? 1e20 : Math.abs(1 / raydirY);
        int mapX = (int)this.x;
        int mapY = (int)this.y;
        double lenX;
        double lenY;
        double stepX;
        double stepY;
    
        if (raydirX < 0){
            lenX = (this.x-mapX)*dx;
            stepX = -1;
        }
        else{
            lenX = (1-this.x+mapX)*dx;
            stepX = 1;
        }
        if (raydirY < 0){
            lenY = (this.y-mapY)*dy;
            stepY = -1;
        }
        else{
            lenY = (1+mapY-this.y)*dy;
            stepY = 1;
        }
        for (int i = 0; i < 20; i++){
            if (m[mapX][mapY] != 0){ 
                return false;
            }
            if (mapX == playerX && mapY == playerY){
                return true;
            }
            if (lenY < lenX){
                mapY += stepY;
                lenY += dy;
            }
            else{
                mapX += stepX;
                lenX += dx;
            }
        }
        return false;
    }
}