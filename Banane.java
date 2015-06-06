import java.awt.Color;

public class Banane extends Projectile{
    
    public Banane(double x, double y,double dx, double dy){
        super(x,y,dx,dy,Color.yellow); 
        this.nomObjet="BANANE";
    }
    
    public void doCollision(Item item){//reset la vitesse du kart à 0
        item.frontSpeed=0;
        this.actif=false;
        item.actif=false;
    }
    
    public void move(){
            
    }
        

}
