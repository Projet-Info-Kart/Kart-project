import java.awt.Graphics;
import java.awt.Image;

public abstract class Item {
    protected double x,y;//le x,y est au milieu du rectangle
    protected double dx,dy; //vecteur direction
    protected double theta; // angle du vecteur de (dx,dy)
    protected double frontSpeed; // vitesse lin�aire
    protected Image image;
    protected double[] tabx; //le tableau des coins de l'objet
    protected double[] taby;
    protected String nomObjet; 
    protected int tempsVie; //depuis combien de temps l'objet a �t� cr�e. 
    protected boolean actif;//dit si l'objet est actif ou non 
    
    
    
    
    public Item(double x,double y,double dx,double dy){
        this.x=x;
        this.y=y;
        this.dx=dx;
        this.dy=dy;
        this.tempsVie=0;
        tabx=new double[5];
        taby=new double[5];
        this.actif=true;
    }
    
    public void calculTheta(){
        this.theta=Math.atan2(this.dy,this.dx);
    }
    
    public abstract boolean collision(Item item);
    
    public abstract void doCollision(Item item);

    
    public abstract void move();//diff�rent du avance(int i) de kart
    
   // public abstract void drawGraphTest(Graphics g);
    public abstract void draw(Graphics g,int x, int y, double echelle);
    
    public void setTemps(){// augmente le tempsVie de l'objet
           this.tempsVie++; 
    }
    

    public double getTheta(){
        return theta;
    }

    public int getTemps(){
        return tempsVie;
    }
    
    public double getX(){
        return x;    
    }
    
    public double getY(){
        return y;    
    }
    
    public void setX(double x){
        this.x=x;
    }
    
    public void setY(double y){
        this.y=y;
    }
    
}
