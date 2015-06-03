import java.awt.Color;
import java.awt.Graphics;

public class Bombe extends Projectile{//WORK IN PROGRESS il faut trouver comment d�tecter s'il y a des karts dans la zone d'explosion quand elle explose
    
    private static double rayExpl;//le rayon de l'explosion
    
    public Bombe(double x, double y,double dx, double dy){
        super(x,y,dx,dy,Color.black);  
        rayExpl=3;
        this.nomObjet="BOMBE";
    }
    
    public boolean quandExploser(){
        boolean exp=false;
        if (tempsVie>160){//la bombe explose au bout de 4 secondes, on incr�mente le temps de vie toutes les 25ms --->160 it�rations en tout
            exp=true;
        }return exp;
    }
    public void doCollision(Item item){
        this.explosion();
    }
    public boolean colliExplosion(Item item){
        boolean colli=false;
        if (item.nomObjet=="KART"){
            for (int i=0;i<5;i++){
                if (Math.sqrt((this.x-item.tabx[i])*(this.x-item.tabx[i])+(this.y-item.taby[i])*(this.y-item.taby[i]))<rayExpl){
                    colli=true;
                }
            }
            
        }
        else{//on a donc affaire � un bonus
            if (Math.sqrt((this.x-item.x)*(this.x-item.x)+(this.y-y)*(this.y-item.y))<rayExpl){
                colli=true;
            }
        }
        
        return colli;
        }

    public void explosion(){
        for (int i=0;i<PanelField.modeJoueur;i++){//regarde si des karts sont pr�sents dans l'explosion
            Item O=PanelField.Items.get(i);
            if (this.colliExplosion(O)){
                O.frontSpeed=0;
                double X=O.x-this.x;
                double Y=O.y-this.y;
                double distance = Math.sqrt(X*X+Y*Y);
                O.x=O.x+(X/distance)*5;//pousse le kart dans la direction oppos�e au centre de l'explosion
                O.y=O.y+(Y/distance)*5;
                System.out.println("booom");
            }
        }
        for (int i=PanelField.modeJoueur; i<PanelField.Items.size();i++){//regarde si des bonus sont compris dans l'explosion
            Item O=PanelField.Items.get(i);
            if (this.colliExplosion(O)){
                O.actif=false;
            }
        }this.actif=false;

    }
    
    public void move(){
        
    }
   
}
