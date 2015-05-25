

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PanelField extends JPanel{
    int x=0;
    int y=0;
    static double aExt=200;
    static double aInt=188;
    static double bExt=125;
    static double bInt = 113;
    double c=0;
    int hWind;
    
    double echelle;
    double echelleKart;
    double alpha=0;
    
    double[]tabXext = new double[801];
    double[]tabYext = new double[801];
    double[]tabXint = new double[753];
    double[]tabYint = new double[753];
 
    BufferedImage ArrierePlan;
    Graphics buffer; 
    
    int toucheHaut=69;
    int toucheBas=70;
    int toucheGauche=81;
    int toucheDroite=68;
    boolean ToucheHaut,ToucheBas,ToucheDroite,ToucheGauche;
    
    char tourne;
    char freine;
    int xpos=0;
    Kart kart1;
    
    int numJoueur;
    
    int x1=0;
    int x2=0;
    int y1=0;
    int y2=0;
    
    JButton Back = new JButton("Back to main menu");
       
    public PanelField(double ech, int h, int nJ){
        
        setLayout(new GridLayout (2,2,550,700));
        
        //Règle la taille du texte dans chaque button
        Font newButtonBackFont=new Font(Back.getFont().getName(),Back.getFont().getStyle(),20);
        Back.setFont(newButtonBackFont);
        
        add(Back);
        
        numJoueur=nJ;
        
        ArrierePlan=new BufferedImage(2000,2000,BufferedImage.TYPE_INT_RGB);
        buffer=ArrierePlan.getGraphics();
        
        //ToucheHaut=false;ToucheBas=false;ToucheDroite=false;ToucheGauche=false;
        //this.addKeyListener(new PanelField_this_keyAdapter(this));
        
        
        kart1=new Kart(444,175,0,1,15,10,0.7,150,1,1);
        
        hWind =h;
        echelleKart=ech;
        echelle=30;
        
        //demi ellipse extérieure positive
        for (int i =0; i<400; i++){
            tabXext[i]=i-200;
            tabYext[i]=  Math.sqrt( bExt*bExt*(1.0-tabXext[i]*tabXext[i]/aExt/aExt));
        }
    
        //demi ellipse extérieure négative
    
        for (int i=400;i<801;i++){
            tabXext[i]= (i-c-200);
            tabYext[i] =  -Math.sqrt(bExt*bExt*(1.0-(tabXext[i]*tabXext[i]/aExt/aExt)));
            c=c+2;
        }

        //demi ellipse intérieure positive
        for (int i =0; i<376; i++){
            tabXint[i]=i-188;
            tabYint[i]=  Math.sqrt(bInt*bInt*(1.0-(tabXint[i]*tabXint[i]/aInt/aInt)));
        }    
    
        //demi ellipse intérieure négative
        c=0; 
    
        for (int i=376;i<753;i++){
            tabXint[i]= (i-c-188);
            tabYint[i]=  -Math.sqrt(bInt*bInt*(1.0-(tabXint[i]*tabXint[i]/aInt/aInt)));
            c=c+2;
        }  
        
        Timer timer=new Timer(25,new TimerAction());
        timer.start();
        
        /*for(int i=0; i<800; i++){         // Essai Helene a supprimer après
            if(tabXext[i]==200){
                System.out.println("i: "+i+", y: "+tabYext[i]);
                }
              
             
        }*/
     
    }
    public void paint(Graphics g){
       
       
       /* buffer.setColor( new Color(0, 150, 0) );
        buffer.fillRect(0,0,1200,840);
        buffer.setColor( Color.black );*/
               
      //Ellispe Extérieure
       /*for(int i=0; i<800; i++){
           
           x1=this.m2SX((int)(tabXext[i]+250), (int)(tabYext[i]+175),0, hWind, hWind, 0, echelleKart);
           x2=this.m2SX((int)(tabXext[i+1]+250), (int)(tabYext[i+1]+175),0, hWind, hWind, 0, echelleKart);
           y1=this.m2SY((int)(tabXext[i]+250), (int)(tabYext[i]+175),0, hWind, hWind, 0, echelleKart);
           y2=this.m2SY((int)(tabXext[i+1]+250), (int)(tabYext[i+1]+175),0, hWind, hWind, 0, echelleKart);
           buffer.drawLine(x1,y1,x2,y2);
            
       }
       
       
       // Ellispe Intérieure
       for(int i=0; i<752; i++){
           x1=this.m2SX((int)(tabXint[i]+250), (int)(tabYint[i]+175),0, hWind, hWind, 0, echelleKart);
           x2=this.m2SX((int)(tabXint[i+1]+250), (int)(tabYint[i+1]+175),0, hWind, hWind, 0, echelleKart);
           y1=this.m2SY((int)(tabXint[i]+250), (int)(tabYint[i]+175),0, hWind, hWind, 0, echelleKart);
           y2=this.m2SY((int)(tabXint[i+1]+250), (int)(tabYint[i+1]+175),0, hWind, hWind, 0, echelleKart);
           
           buffer.drawLine(x1,y1,x2,y2);
       }*/
       
        
        /*int X=this.m2SX(kart1.getX()-0.5,kart1.getY()-1,0, hWind, hWind, 0, echelleKart);       // 15 et 30 du au décalage du x,y qui sont au centre du kart
        int Y=this.m2SY(kart1.getX()-0.5,kart1.getY()-1,0, hWind, hWind, 0, echelleKart);           // et dessin qui prend en compte le coin haut gauche
        kart1.draw(buffer,X,Y);*/
        //System.out.println(X+","+Y);
        //Back.repaint();
        paintComponents(buffer);
        g.drawImage(ArrierePlan,0,0,this);
        
        
    }
    
    
    
    class TimerAction implements ActionListener{
        public void actionPerformed(ActionEvent e){
            boucle_principale_jeu();
        }
        
    }
    
    public void boucle_principale_jeu(){
        
        if (ToucheBas){
            freine='y';
            kart1.freine();
        }
        if (ToucheGauche){
            tourne='g';
        }
        else if (ToucheDroite){
            tourne='d';
        }
        else{
            tourne='0';
        }
        if (ToucheHaut && (ToucheGauche || ToucheDroite )) { 
            kart1.avance(1); 
        }
        else if (ToucheHaut){
            kart1.avance(0);
        }
        kart1.tourne(tourne);
        if (ToucheHaut==false && (ToucheGauche || ToucheDroite )){ 
            kart1.ralentit(1); 
        }
        else if (ToucheHaut==false){
            kart1.ralentit(0);
        }
        kart1.derapage(tourne,freine);
        freine='n';
        kart1.calculTheta();        
        alpha=kart1.getTheta()-Math.PI/2;
        
        
        
        if(alpha>Math.PI && alpha<2*Math.PI){
            alpha=alpha-2*Math.PI;
        }else if(alpha<-Math.PI && alpha >-2*Math.PI){
            alpha=alpha+2*Math.PI;
            System.out.println("alpha compris entre -pi et -2pi");}
        
        if (alpha%Math.PI<0.2){System.out.println("Alpha : "+alpha/Math.PI+" Pi");}
        else{System.out.println(kart1.getTheta()+","+alpha);}
        
        buffer.setColor( new Color(0, 150, 0) );
       buffer.fillRect(0,0,1200,840);
        buffer.setColor(Color.black);
        
        double fx,fy;
        if(alpha>=0){    
            fy=kart1.getY()+Math.sqrt(20*20+21*21)*Math.cos(Math.acos(21/Math.sqrt(20*20+21*21))+alpha);
            fx=kart1.getX()-Math.sqrt(20*20+21*21)*Math.sin(Math.asin(20/Math.sqrt(20*20+21*21))+alpha);
        }else{
            fy=kart1.getY()+Math.sqrt(20*20+21*21)*Math.cos(Math.acos(21/Math.sqrt(20*20+21*21))-alpha);
            fx=kart1.getX()-Math.sqrt(20*20+21*21)*Math.sin(Math.asin(20/Math.sqrt(20*20+21*21))-alpha);}
       //System.out.println(fx+","+fy);
        
        
       //double fy=kart1.getY()+Math.sqrt(20*20+21*21)*Math.cos(Math.acos(20/Math.sqrt(20*20+21*21))+alpha);
       //double fx=kart1.getX()-Math.sqrt(20*20+21*21)*Math.sin(Math.asin(21/Math.sqrt(20*20+21*21))+alpha);
       
    
        
       //Ellispe Extérieure
       for(int i=0; i<800; i++){
            //if((tabYext[i]+175)>=(fy-28)&&((tabYext[i]+175)<=(fy))){
                x1=this.m2SX(tabXext[i]+250, tabYext[i]+175,fx, fy, 28, alpha, echelle);       //+250 : décalage origine x de l'ellipse / +175 : décalge y origine de l'ellipse
                x2=this.m2SX(tabXext[i+1]+250, tabYext[i+1]+175,fx, fy, 28, alpha, echelle);      // +0.5
                y1=this.m2SY(tabXext[i]+250, tabYext[i]+175,fx,fy, 28, alpha, echelle);
                y2=this.m2SY(tabXext[i+1]+250, tabYext[i+1]+175,fx, fy, 28, alpha, echelle);
                buffer.drawLine(x1,y1,x2,y2);
                //System.out.println("Je dessine pour i :"+i);
            //}
          //System.out.println(x1);
              
             
        }
                
        // Ellispe Intérieure
        for(int i=0; i<752; i++){
            x1=this.m2SX(tabXint[i]+250, tabYint[i]+175,fx, fy, 28, alpha, echelle);       //+250 : décalage origine x de l'ellipse / +175 : décalage y origine de l'ellipse
            x2=this.m2SX(tabXint[i+1]+250, tabYint[i+1]+175,fx, fy, 28, alpha, echelle);      // +0.5
            y1=this.m2SY(tabXint[i]+250, tabYint[i]+175,fx,fy, 28, alpha, echelle);
            y2=this.m2SY(tabXint[i+1]+250, tabYint[i+1]+175,fx, fy, 28, alpha, echelle);
            buffer.drawLine(x1,y1,x2,y2);
        }
        
        int a=400;
        x1=this.m2SX(tabXext[a]+250, tabYext[a]+175,fx, fy, 28, alpha, echelle);       //+250 : décalage origine x de l'ellipse / +175 : décalge y origine de l'ellipse
        x2=this.m2SX(tabXext[a+1]+250, tabYext[a+1]+175,fx, fy, 28, alpha, echelle);      // +0.5
        y1=this.m2SY(tabXext[a]+250, tabYext[a]+175,fx,fy, 28, alpha, echelle);
        y2=this.m2SY(tabXext[a+1]+250, tabYext[a+1]+175,fx, fy, 28, alpha, echelle);
        System.out.println(tabXext[a]+","+tabYext[a]+","+x1+","+x2+","+y1+","+y2);
        buffer.drawLine(x1,y1,x2,y2);
        
        
        int X=this.m2SX(kart1.getX()-0.5,kart1.getY()-1,fx, fy, 28, alpha, echelle);       // 15 et 30 du au décalage du x,y qui sont au centre du kart
        int Y=this.m2SY(kart1.getX()-0.5,kart1.getY()-1,fx, fy, 28, alpha, echelle);           // et dessin qui prend en compte le coin haut gauche
        kart1.draw(buffer,X,Y);
        
        
        
        
        repaint();
        
    }

    /** Convertit la coordonnée X métrique en pixel d'une fenêtre donnée
     * @param mx la coord x en metrique
     * @param my la coord y en metrique
     * @param fx la coord x en metrique de la fenêtre en haut à gauche
     * @param fy la coord y en metrique de la fenêtre en haut à gauche
     * @param fh la hauteur en metrique de la fenêtre
     * @param a l'angle de la fenêtre en radian// pour l'instant, a positif comprit entre 0 et Pi
     * @param ech le rapport pixel/metrique
     * @return la coordonée X**/
     public int m2SX(double mx,double my, double fx, double fy, int fh,double a,double ech){
         double DX,DY,xA,yA,X;
         if(a>=0){
            xA=fx+fh*Math.sin(a);
            yA=fy-fh*Math.cos(a);
             DX=mx-xA;            
            DY=my-yA;
             if (mx-xA>=0){X=Math.cos(a)*DX+Math.sin(a)*DY;}
             else {X=-Math.cos(a)*DX+Math.sin(a)*DY;}
                   
         }else {
            xA=fx-fh*Math.sin(a);
            yA=fy-fh*Math.cos(a);
            DX=Math.abs(mx-xA);            
             DY=my-yA;
            X=Math.cos(a)*DX-Math.sin(a)*DY;  
         } 
         
         return (int)(X*ech); 
     }
    
     /** Convertit la coordonnée Y métrique en pixel d'une fenêtre donnée
      * @param mx la coord x en metrique
      * @param my la coord y en metrique
      * @param fx la coord x en metrique de la fenêtre en haut à gauche
      * @param fy la coord y en metrique de la fenêtre en haut à gauche
      * @param fh la hauteur en metrique de la fenêtre
      * @param a l'angle de la fenêtre en radian
      * @param ech le rapport pixel/metrique
      * @return la coordonée Y**/
    public int m2SY(double mx,double my, double fx, double fy, int fh,double a,double ech){
        double DX,DY,xA,yA,Y;
        if(a>=0){
            xA=fx+fh*Math.sin(a);
            yA=fy-fh*Math.cos(a);
            DX=Math.abs(mx-xA);            
            DY=my-yA;
            
            if (mx-xA>=0){Y=Math.cos(a)*DY-Math.sin(a)*DX;}
            else {Y=Math.cos(a)*DY+Math.sin(a)*DX;}
           
            
        }else {
            xA=fx-fh*Math.sin(a);
            yA=fy-fh*Math.cos(a);
            DX=Math.abs(mx-xA);             
            DY=my-yA;
            Y=Math.cos(a)*DY+Math.sin(a)*DX;  
        }
        
        return (int)(840-Y*ech); 
    }
    
    
    private class PanelField_this_keyAdapter extends KeyAdapter{
          private PanelField adaptee;
          PanelField_this_keyAdapter(PanelField adaptee){
             this.adaptee=adaptee; 
          }
          public void keyPressed(KeyEvent e){
              adaptee.this_keyPressed(e);
          }
          public void keyReleased(KeyEvent e){
              adaptee.this_keyReleased(e);
          }
          
          
          
      }
      
      public void this_keyPressed(KeyEvent e){
          int code= e.getKeyCode();
          //System.out.println("Key pressed : "+code);
          
          if(numJoueur==1){               // Joueur 1 : pavé QZDS
              if (code==68){
                  ToucheDroite=true;
              }else if (code==81){
                  ToucheGauche=true;
              }else if (code==83){
                  ToucheBas=true;
              }else if (code==90){
                  ToucheHaut=true;
              }
          }else if (numJoueur==2){       // Joueur 2 : pavé flèches
              if (code==39){
                  ToucheDroite=true;
              }else if (code==37){
                  ToucheGauche=true;
              }else if (code==40){
                  ToucheBas=true;
              }else if (code==38){
                  ToucheHaut=true;
              }
          }
          
          
          if (ToucheHaut && ToucheBas){
              ToucheHaut=false;
              ToucheBas=false;
          }
          if (ToucheGauche && ToucheDroite){
              ToucheGauche=false;
              ToucheDroite=false;
          }
      }

      void this_keyReleased(KeyEvent e){
          int code=e.getKeyCode();
          //System.out.println("Key released : "+code);
          
          if(numJoueur==1){               // Joueur 1 : pavé QZDS
              if (code==68){
                  ToucheDroite=false;
              }else if (code==81){
                  ToucheGauche=false;
              }else if (code==83){
                  ToucheBas=false;
              }else if (code==90){
                  ToucheHaut=false;
              }
          }else if (numJoueur==2){       // Joueur 2 : pavé flèches
              if (code==39){
                  ToucheDroite=false;
              }else if (code==37){
                  ToucheGauche=false;
              }else if (code==40){
                  ToucheBas=false;
              }else if (code==38){
                  ToucheHaut=false;
              }
          }
          
          
      } 
    
}
    
    




