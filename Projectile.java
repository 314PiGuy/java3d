
public class Projectile {

    private double posX, posY, vX, vY;
    boolean fromPlayer, dead;
    int bouncesLeft = 1;

    public Projectile(double x, double y, double vx, double vy, boolean p) {
        posX = x;
        posY = y;
        vX = vx;
        vY = vy;
        fromPlayer = p;
        dead = false;
    }

    public void move(int[][] m){
        posX += vX;
        bounceX(m);
        posY += vY;
        bounceY(m);
    }
    
    public double getX(){
        return posX;
    }

    public double getY(){
        return posY;
    }

    public void bounceX(int[][] m){
        if (bouncesLeft < 1){
            return;
        }
        if (m[(int)posX][(int)posY] != 0){
            posX -= vX;
            vX = -vX;
            bouncesLeft --;
        }
    }

    public void bounceY(int[][] m){
        if (bouncesLeft < 1){
            return;
        }
        if (m[(int)posX][(int)posY] != 0){
            posY -= vY;
            vY = -vY;
            bouncesLeft--;
        }
    }
    
    public boolean outOfBounds(int x, int y, int[][] m){
        if ((posX > 0 && posX < x) && (posY > 0 && posY < y)){
            return m[(int)posX][(int)posY] != 0 && bouncesLeft < 1;
        }
        return true;
    }
    
    public boolean isFromPlayer(){
        return fromPlayer;
    }

    public boolean isdead(){
        return dead;
    }

    public void kill(){
        dead = true;
    }
}
