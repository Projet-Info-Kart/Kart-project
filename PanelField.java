

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

import java.util.ArrayList;

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
    boolean ToucheHaut,ToucheBas,ToucheDroite,ToucheGauche,Shift,Space;
    
    boolean debutJeu=false;
    double tempsCourse=0;
    int tempsTotal;
    int nbTours=0;
    double[][] position=new double[2][2];
    
    char tourne;
    char freine;
    int xpos=0;
    Kart kart1;
    
    static ArrayList <Item> Items;//liste de tous les objets actifs
    double X; //le x du kart que l'on  r�cup�rera une fois par boucle pour �viter d'avoir � appeler tous les temps les accesseurs
    double Y;
    double DX;
    double DY;
    
    int numJoueur;
    int modeJoueur;
    
    int x1=0;
    int x2=0;
    int y1=0;
    int y2=0;
    
    JButton Back = new JButton("Back to main menu");
    JLabel textTour=new JLabel("Nombre de tours : "+nbTours);
    JLabel textChrono=new JLabel("Temps : "+tempsCourse);
    JLabel textBonus=new JLabel("Bonus : ");
    Font textFont=new Font(textTour.getFont().getName(),textTour.getFont().getStyle(),20);
       
    public PanelField(double ech, int h, int nJ, int mJ){
        
        setLayout(new GridLayout (15,1));
            
        textTour.setForeground(Color.white);
        textChrono.setForeground(Color.red);
        textBonus.setForeground(Color.white);
        
        //R�gle la taille du texte dans chaque button
        Font newButtonBackFont=new Font(Back.getFont().getName(),Back.getFont().getStyle(),20);
        Back.setFont(newButtonBackFont);
        
        textTour.setFont(textFont);
        textBonus.setFont(textFont);
        
        Font chronoFont=new Font(textTour.getFont().getName(),textTour.getFont().getStyle(),40);
        textChrono.setFont(chronoFont);

        
        add(Back);
        add(textTour);
        add(textChrono);
        add(textBonus);
        
        numJoueur=nJ;
        modeJoueur=mJ;
        
        ArrierePlan=new BufferedImage(2000,2000,BufferedImage.TYPE_INT_RGB);
        buffer=ArrierePlan.getGraphics();     
        
        if(numJoueur==1){
            kart1=new Kart(439,169,0,1,15,10,0.1,150,1,1);  // ligne 5m derri�re la premi�re, kart 1m derri�re = 175-6
        }else kart1=new Kart(441,174,0,1,15,10,0.1,150,1,1);
        
        hWind =h;

       // echelle=30;
        if (modeJoueur==1) {echelle=30;}
        else {echelle=15;}
        
        //demi ellipse ext�rieure positive
        for (int i =0; i<400; i++){
            tabXext[i]=i-200;
            tabYext[i]=  Math.sqrt( bExt*bExt*(1.0-tabXext[i]*tabXext[i]/aExt/aExt));
        }
    
        //demi ellipse ext�rieure n�gative
    
        for (int i=400;i<801;i++){
            tabXext[i]= (i-c-200);
            tabYext[i] =  -Math.sqrt(bExt*bExt*(1.0-(tabXext[i]*tabXext[i]/aExt/aExt)));
            c=c+2;
        }

        //demi ellipse int�rieure positive
        for (int i =0; i<376; i++){
            tabXint[i]=i-188;
            tabYint[i]=  Math.sqrt(bInt*bInt*(1.0-(tabXint[i]*tabXint[i]/aInt/aInt)));
        }    
    
        //demi ellipse int�rieure n�gative
        c=0; 
    
        for (int i=376;i<753;i++){
            tabXint[i]= (i-c-188);
            tabYint[i]=  -Math.sqrt(bInt*bInt*(1.0-(tabXint[i]*tabXint[i]/aInt/aInt)));
            c=c+2;
        }  
        
        Items=new ArrayList <Item>();//les karts sont toujours ajout�s en premier dans la liste, puis les bonus apr�s
        Items.add(kart1);
        
        //Cr�ation des lignes de Cadeaux 
        for (int i=0;i<12;i+=3){                            // les cadeaux d'une seule ligne ne se suivent pas ds la liste 
            Items.add(new Cadeau(350,tabYext[300]-i,1,0));
            Items.add(new Cadeau(150,tabYext[100]-i,1,0));
            Items.add(new Cadeau(50+i,175,1,0));
            Items.add(new Cadeau(150,tabYext[700]+i,1,0));
            Items.add(new Cadeau(350,tabYext[500]+i,1,0));
        }
        
        Cadeau c=new Cadeau(444,175,1,0);//test avec un cadeau
        Items.add(c);
        //Bombe b=new Bombe(444,175,1,0);//test avec un cadeau
        //Items.add(b);
        
        
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
            boucle_principale_affichage();
            
            if(debutJeu){
                tempsTotal+=25;
            }
            
            if(tempsTotal<1500){textChrono.setText("A vos marques...");}
            else if (tempsTotal<3000&&tempsTotal>1500){textChrono.setText("Pr�t...");}
            else if(tempsTotal<3500&&tempsTotal>3000){textChrono.setText("Go!");}
            else if (tempsTotal>=3500){textChrono.setText("Temps : "+tempsCourse/1000 + "s");
                                       textChrono.setFont(textFont);
                                       textChrono.setForeground(Color.white);}
            
            
            if(tempsTotal>=3000){   // D�finir le d�part du chrono. Arbitrairement 10s
                tempsCourse+=25;
            } 
            
            //comptage du nombre de tour
            if(tempsCourse>10000){              //temps n�cessaire pour qu'un tour ne soit pas d�compter si le joueur tarde � d�marrer
                position[1][0]=kart1.getX();    // position x actuelle
                position[1][1]=kart1.getY();
                compteTour(position);
                position[0][0]=kart1.getX();    // position x actuelle d�finissant la position pr�c�dente du tour suivant
                position[0][1]=kart1.getY();
            }
            textTour.setText("Nombre de tours : "+nbTours);
            
            
            
        }
        
    }
    
    public void activeCompteur(){
       debutJeu=true; 
    }
      
    public void boucle_principale_jeu(){
            X=kart1.getX();
            Y=kart1.getY();
            DX=kart1.getdx();
            DY=kart1.getdy();
            if(tempsCourse>0){
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
                //kart1.derapage(tourne,freine);
                freine='n';
                if (kart1.Bonus()){//Si le kart a un bonus dispo
                    if (Shift || Space ){//FlecheHaut pour tirer missile haut, FlecheBas pour le tirer en bas, Space pour poser bombe ou banane
                        if (kart1.getNomBonus()=="MISSILE"){
                            if (Shift){
                                Missile m=new Missile(X+DX*2.5,Y+DY*2.5,DX,DY);//le missile est cr�� devant si on tire devant
                                Items.add(m);
                                kart1.setABonus(false);
                            }
                            else if (Space){
                                Missile m=new Missile(X-DX*2.5,Y-DY*2.5,-DX,-DY);//ou derri�re si on tire derri�re
                                Items.add(m);
                                kart1.setABonus(false);
                            }
                        }
                        else if (kart1.getNomBonus()=="BANANE"){
                            if (Space){
                                Banane b=new Banane(X-DX*2.5,Y-DY*2.5,DX,DY);
                                Items.add(b);
                                kart1.setABonus(false);
                            }
                        }
                        else if (kart1.getNomBonus()=="BOMBE"){
                            if (Space){
                                Bombe b=new Bombe(X-DX*2.5,Y-DY*2.5,DX,DY);
                                Items.add(b);
                                kart1.setABonus(false);
                            }
                        }
                    }
                }
                kart1.calculTheta();
                kart1.coordCoinsX();
                kart1.coordCoinsY();
            
            for (int i=0;i<Items.size();i++){
                Item O=Items.get(i);
                O.setTemps();
                O.move();
            }
        
           /* for (int i=numJoueur;i<Items.size();i++){
                Item O = Items.get(i);
                for (int j=0;j<numJoueur;j++){//�a teste la collision avec les karts
                    Item l=Items.get(j);
                    if (O.collision(l)){
                        O.doCollision(l);
                    }
                }
                for (int j=i+1;j<Items.size();j++){//puis avec les autres bonus
                    Item l=Items.get(j);
                    if (O.collision(l)){
                        O.doCollision(l);
                    }
                }
            }*/
        
           for(int i=numJoueur;i<Items.size();i++){//regarde si une bombe doit exploser d'elle meme
                Item O=Items.get(i);
                if (O.nomObjet=="BOMBE"){
                    if (((Bombe)O).quandExploser()){
                        ((Bombe)O).explosion();
                    }
                }
                if(O.nomObjet=="CADEAU"){
                    ((Cadeau)O).rendVisible();
                }
            }
            //Garbage collector
            for (int k=numJoueur; k<Items.size(); k++) {
                Item O = Items.get(k);
                if (O.actif==false) {
                    Items.remove(k);
                    k--; 
                }
            }     
        }
    }
    
    public void boucle_principale_affichage(){
        kart1.calculTheta();        
        alpha=kart1.getTheta()-Math.PI/2;
        
        
        buffer.setColor( new Color(0, 150, 0) );
        buffer.fillRect(0,0,1200,840);
        buffer.setColor(Color.black);
        
        double fx,fy;
        fy=kart1.getY()+Math.sqrt(20*20+21*21)*Math.cos(Math.acos(21/Math.sqrt(20*20+21*21))+alpha);
        fx=kart1.getX()-Math.sqrt(20*20+21*21)*Math.sin(Math.asin(20/Math.sqrt(20*20+21*21))+alpha);

        
        //Ellispe Ext�rieure
        for(int i=0; i<800; i++){
            x1=this.m2SX(tabXext[i]+250, tabYext[i]+175,fx, fy, 28, alpha, echelle);       //+250 : d�calage origine x de l'ellipse / +175 : d�calge y origine de l'ellipse
            x2=this.m2SX(tabXext[i+1]+250, tabYext[i+1]+175,fx, fy, 28, alpha, echelle);      // +0.5
            y1=this.m2SY(tabXext[i]+250, tabYext[i]+175,fx,fy, 28, alpha, echelle);
            y2=this.m2SY(tabXext[i+1]+250, tabYext[i+1]+175,fx, fy, 28, alpha, echelle);
            buffer.drawLine(x1,y1,x2,y2);
        }
                
        // Ellispe Int�rieure
        for(int i=0; i<752; i++){
            x1=this.m2SX(tabXint[i]+250, tabYint[i]+175,fx, fy, 28, alpha, echelle);       //+250 : d�calage origine x de l'ellipse / +175 : d�calage y origine de l'ellipse
            x2=this.m2SX(tabXint[i+1]+250, tabYint[i+1]+175,fx, fy, 28, alpha, echelle);      // +0.5
            y1=this.m2SY(tabXint[i]+250, tabYint[i]+175,fx,fy, 28, alpha, echelle);
            y2=this.m2SY(tabXint[i+1]+250, tabYint[i+1]+175,fx, fy, 28, alpha, echelle);
            buffer.drawLine(x1,y1,x2,y2);
        }
        
        //Ligne de d�part
        if(tempsCourse<10000){     // laisse afficher la ligne de d�part durant 10 secondes, remplac�e ensuite par la ligne d'arriv�e   
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
                
        //Ligne d'arriv�e
        if(tempsCourse>10000){                   //affichage de la ligne d'arriv�e � la place de la ligne de d�part au bout de 10 sec  
            buffer.setColor(Color.white);
            for(int i=0; i<12;i+=2){
                x1=this.m2SX(438.5+i, 175.5,fx, fy, 28, alpha, echelle);          
                y1=this.m2SY(438.5+i, 175.5,fx,fy, 28, alpha, echelle);
                buffer.fillRect(x1,y1,30,30);     
            }   
        }    
        
        
        for (int k=0; k<Items.size(); k++) {
            
            Item it = Items.get(k);
            int X=this.m2SX(it.getX()-0.5,it.getY()+1,fx, fy, 28, alpha, echelle);       
            int Y=this.m2SY(it.getX()-0.5,it.getY()+1,fx, fy, 28, alpha, echelle); 
            it.draw(buffer,X,Y);
            System.out.println(k+": X="+it.getX()+", Y="+it.getY());
            if(it.nomObjet=="CADEAU"){((Cadeau)it).rendVisible();}
        }
        
        
        repaint();
        
    }

    /** Convertit la coordonn�e X m�trique en pixel d'une fen�tre donn�e
     * @param mx la coord x en metrique
     * @param my la coord y en metrique
     * @param fx la coord x en metrique de la fen�tre en haut � gauche
     * @param fy la coord y en metrique de la fen�tre en haut � gauche
     * @param fh la hauteur en metrique de la fen�tre
     * @param a l'angle de la fen�tre en radian// pour l'instant, a positif comprit entre 0 et Pi
     * @param ech le rapport pixel/metrique
     * @return la coordon�e X**/
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
    
     /** Convertit la coordonn�e Y m�trique en pixel d'une fen�tre donn�e
      * @param mx la coord x en metrique
      * @param my la coord y en metrique
      * @param fx la coord x en metrique de la fen�tre en haut � gauche
      * @param fy la coord y en metrique de la fen�tre en haut � gauche
      * @param fh la hauteur en metrique de la fen�tre
      * @param a l'angle de la fen�tre en radian
      * @param ech le rapport pixel/metrique
      * @return la coordon�e Y**/
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
    
     public void setModeJoueur(int mj){
         this.modeJoueur=mj; 
    }
    
    
     public void compteTour(double[][] pos){
         if(pos[0][0]>250 && pos [1][0]>250){   // moiti� droite de l'ellipse
             if(pos[0][1]<175 && pos[1][1]>=175){     // passe la moiti� sup�rieure de l'ellipse en venant du bas. 
                nbTours++;     
            }
        }
    }
   
      
      public void this_keyPressed(KeyEvent e){
          int code= e.getKeyCode();
          //System.out.println("Key pressed : "+code);
          
          if(numJoueur==1){               // Joueur 1 : pav� QZDS
              if (code==68){
                  ToucheDroite=true;
              }else if (code==81){
                  ToucheGauche=true;
              }else if (code==83){
                  ToucheBas=true;
              }else if (code==90){
                  ToucheHaut=true;
              }
              else if (code==16){
                  Shift=true;
              }
              else if (code==32){
                  Space=true;
              }
              if (ToucheHaut && ToucheBas){
                  ToucheHaut=false;
                  ToucheBas=false;
              }
              if (ToucheGauche && ToucheDroite){
                  ToucheGauche=false;
                  ToucheDroite=false;
              }
          }else if (numJoueur==2){       // Joueur 2 : pav� fl�ches
              if (code==39){
                  ToucheDroite=true;
              }else if (code==37){
                  ToucheGauche=true;
              }else if (code==40){
                  ToucheBas=true;
              }else if (code==38){
                  ToucheHaut=true;
              }
              else if (code==101){
                  Shift=true;
              }
              else if (code==98){
                  Space=true;
              }
              if (ToucheHaut && ToucheBas){
                  ToucheHaut=false;
                  ToucheBas=false;
              }
              if (ToucheGauche && ToucheDroite){
                  ToucheGauche=false;
                  ToucheDroite=false;
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
          
          
          
      }

      void this_keyReleased(KeyEvent e){
          int code=e.getKeyCode();
          //System.out.println("Key released : "+code);
          
          if(numJoueur==1){               // Joueur 1 : pav� QZDS
              if (code==68){
                  ToucheDroite=false;
              }else if (code==81){
                  ToucheGauche=false;
              }else if (code==83){
                  ToucheBas=false;
              }else if (code==90){
                  ToucheHaut=false;
              }else if (code==16){
                  Shift=false;
              }else if (code==32){
                  Space=false;
              }
              
          }else if (numJoueur==2){       // Joueur 2 : pav� fl�ches
              if (code==39){
                  ToucheDroite=false;
              }else if (code==37){
                  ToucheGauche=false;
              }else if (code==40){
                  ToucheBas=false;
              }else if (code==38){
                  ToucheHaut=false;
              }else if (code==101){
                  Shift=false;
              }else if (code==98){
                  Space=false;
              }
          }
          
          
      } 
    
}
    
    




