import java.awt.Color;
import java.awt.Graphics;

public class Banane extends Projectile{
    
    public Banane(double x, double y,double dx, double dy){
        super(x,y,dx,dy,Color.yellow); 
        this.nomObjet="BANANE";
    }
    
    public void doCollision(Item item){
        item.frontSpeed=0;
        this.actif=false;
        item.actif=false;
    }
    
    public void move(){
            
    }
        

}
