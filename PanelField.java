

import java.awt.BorderLayout;
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
import javax.swing.JLabel;
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
    
    boolean debutJeu=false;
    double tempsCourse=0;
    int tempsTotal;
    int nbTours=0;
    double[][] position=new double[2][2];
    
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
    JLabel textTour=new JLabel("Nombre de tours : "+nbTours);
    JLabel textChrono=new JLabel("Temps : "+tempsCourse);
       
    public PanelField(double ech, int h, int nJ){
        
        setLayout(new GridLayout (15,1));
        //setLayout(new BorderLayout());
        
        //Règle la taille du texte dans chaque button
        Font newButtonBackFont=new Font(Back.getFont().getName(),Back.getFont().getStyle(),20);
        Back.setFont(newButtonBackFont);
        
       textTour.setForeground(Color.white);
       textChrono.setForeground(Color.white);
        
        add(Back);
        add(textTour);
        add(textChrono);
        
        numJoueur=nJ;
        
        ArrierePlan=new BufferedImage(2000,2000,BufferedImage.TYPE_INT_RGB);
        buffer=ArrierePlan.getGraphics();
        
        //ToucheHaut=false;ToucheBas=false;ToucheDroite=false;ToucheGauche=false;
        //this.addKeyListener(new PanelField_this_keyAdapter(this));
        
        
        kart1=new Kart(439,169,0,1,15,10,0.1,150,1,1);  // ligne 5m derrière la première, kart 1m derrière = 175-6
        
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

        position[0][0]=kart1.getX();
        position[0][1]=kart1.getY();
     
    }
    public void paint(Graphics g){
        paintComponents(buffer);
        g.drawImage(ArrierePlan,0,0,this);
    }
    
    
    
    class TimerAction implements ActionListener{
        public void actionPerformed(ActionEvent e){
            boucle_principale_jeu();
            if(debutJeu){
                tempsTotal+=25;
            }
            
            if(tempsTotal>=100){   // Définir le départ du chrono. Arbitrairement 10s
                tempsCourse+=25;
                //System.out.println("Temps de course : "+tempsCourse/1000+" sec");
            } 
            
            //comptage du nombre de tour
            if(tempsCourse>10000){              //temps nécessaire pour qu'un tour ne soit pas décompter si le joueur tarde à démarrer
                position[1][0]=kart1.getX();    // position x actuelle
                position[1][1]=kart1.getY();
                //System.out.println("X(t-1): "+position[0][0]+", Y(t-1): "+position[0][1]+", X(t): "+position[1][0]+", Y(t): "+position[1][1]);
                compteTour(position);
                position[0][0]=kart1.getX();    // position x actuelle définissant la position précédente du tour suivant
                position[0][1]=kart1.getY();
            }
            //System.out.println("Tour n°"+nbTours +" en cours" );
            textTour.setText("Nombre de tours : "+nbTours);
            textChrono.setText("Temps : "+tempsCourse/1000 + "s");
        }
        
    }
    
    public void activeCompteur(){
       debutJeu=true; 
    }
    
    public void boucle_principale_jeu(){
        if (tempsCourse>0){
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
            freine='n';}
        
        
        kart1.calculTheta();        
        alpha=kart1.getTheta()-Math.PI/2;
    
        
        buffer.setColor( new Color(0, 150, 0) );
        buffer.fillRect(0,0,1200,840);
        buffer.setColor(Color.black);
        
        double fx,fy;
        fy=kart1.getY()+Math.sqrt(20*20+21*21)*Math.cos(Math.acos(21/Math.sqrt(20*20+21*21))+alpha);
        fx=kart1.getX()-Math.sqrt(20*20+21*21)*Math.sin(Math.asin(20/Math.sqrt(20*20+21*21))+alpha);

        
       //Ellispe Extérieure
       for(int i=0; i<800; i++){
            x1=this.m2SX(tabXext[i]+250, tabYext[i]+175,fx, fy, 28, alpha, echelle);       //+250 : décalage origine x de l'ellipse / +175 : décalge y origine de l'ellipse
            x2=this.m2SX(tabXext[i+1]+250, tabYext[i+1]+175,fx, fy, 28, alpha, echelle);      // +0.5
            y1=this.m2SY(tabXext[i]+250, tabYext[i]+175,fx,fy, 28, alpha, echelle);
            y2=this.m2SY(tabXext[i+1]+250, tabYext[i+1]+175,fx, fy, 28, alpha, echelle);
            buffer.drawLine(x1,y1,x2,y2);
        }
                
        // Ellispe Intérieure
        for(int i=0; i<752; i++){
            x1=this.m2SX(tabXint[i]+250, tabYint[i]+175,fx, fy, 28, alpha, echelle);       //+250 : décalage origine x de l'ellipse / +175 : décalage y origine de l'ellipse
            x2=this.m2SX(tabXint[i+1]+250, tabYint[i+1]+175,fx, fy, 28, alpha, echelle);      // +0.5
            y1=this.m2SY(tabXint[i]+250, tabYint[i]+175,fx,fy, 28, alpha, echelle);
            y2=this.m2SY(tabXint[i+1]+250, tabYint[i+1]+175,fx, fy, 28, alpha, echelle);
            buffer.drawLine(x1,y1,x2,y2);
        }
        
        //Ligne de départ
        if(tempsCourse<10000){     // laisse afficher la ligne de départ durant 10 secondes, remplacée ensuite par la ligne d'arrivée   
            buffer.setColor(Color.white);
                x1=this.m2SX(450-9-0.5, 175-5,fx, fy, 28, alpha, echelle);       
                x2=this.m2SX(450-9-3.5, 175-5,fx, fy, 28, alpha, echelle);      
                y1=this.m2SY(450-9-0.5, 175-5,fx,fy, 28, alpha, echelle);
                y2=this.m2SY(450-9-3.5, 175-5,fx, fy, 28, alpha, echelle);
                buffer.drawLine(x1,y1,x2,y2);  
            
                x1=this.m2SX(450-6, 175,fx, fy, 28, alpha, echelle);       
                x2=this.m2SX(450-9, 175,fx, fy, 28, alpha, echelle);      
                y1=this.m2SY(450-6, 175,fx,fy, 28, alpha, echelle);
                y2=this.m2SY(450-9, 175,fx, fy, 28, alpha, echelle);
                buffer.drawLine(x1,y1,x2,y2);    
            
        }
                
        //Ligne d'arrivée
        if(tempsCourse>10000){                   //affichage de la ligne d'arrivée à la place de la ligne de départ au bout de 10 sec  
            buffer.setColor(Color.white);
            for(int i=0; i<12;i+=2){
                x1=this.m2SX(438.5+i, 175.5,fx, fy, 28, alpha, echelle);          
                y1=this.m2SY(438.5+i, 175.5,fx,fy, 28, alpha, echelle);
                buffer.fillRect(x1,y1,30,30);     
            }   
        }    
        
        int X=this.m2SX(kart1.getX()-0.5,kart1.getY()+1,fx, fy, 28, alpha, echelle);       // 15 et 30 du au décalage du x,y qui sont au centre du kart
        int Y=this.m2SY(kart1.getX()-0.5,kart1.getY()+1,fx, fy, 28, alpha, echelle);           // et dessin qui prend en compte le coin haut gauche
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
        xA=fx+fh*Math.sin(a);
        yA=fy-fh*Math.cos(a);
        DX=Math.abs(mx-xA);           
        DY=my-yA;
        
        if (mx-xA>=0){X=Math.cos(a)*DX+Math.sin(a)*DY;}
        else {X=-Math.cos(a)*DX+Math.sin(a)*DY;}
                   
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
        xA=fx+fh*Math.sin(a);
        yA=fy-fh*Math.cos(a);
        DX=Math.abs(mx-xA);            
        DY=my-yA;
            
        if (mx-xA>=0){Y=Math.cos(a)*DY-Math.sin(a)*DX;}
        else {Y=Math.cos(a)*DY+Math.sin(a)*DX;}
        
        return (int)(840-Y*ech); 
    }
    
     public void compteTour(double[][] pos){
         if(pos[0][0]>250 && pos [1][0]>250){   // moitié droite de l'ellipse
             if(pos[0][1]<175 && pos[1][1]>=175){     // passe la moitié supérieure de l'ellipse en venant du bas. 
                nbTours++;     
            }
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
    
    




