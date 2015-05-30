import java.awt.Color;
import java.awt.Graphics;

public class Missile extends Projectile {
    
    public Missile(double x, double y,double dx, double dy){
        super(x,y,dx,dy,Color.green);
        this.frontSpeed=25;
        
        this.nomObjet="MISSILE";
    }
    
    public void move(){
        x=x+dx*(frontSpeed*0.025);
        y=y+dy*(frontSpeed*0.025);
    }
    
    public void doCollision(Item item){
        item.frontSpeed=0;
        this.actif=false;
        item.actif=false;
    }
    
}
