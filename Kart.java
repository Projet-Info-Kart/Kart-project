
import java.awt.Graphics;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Kart extends Item {
    
    private double rayCourb; //le rayon de courbure du kart quand il tourne
    private double maxAcc; //la valeur d'accélération du kart
    private double poids; //le poids (ou plutot la masse) du kart
    private double fCent; //la valeur de la force centrifuge qui s'applique au kart
    private double adherence; //le coefficient d'adherence du kart (1 si neutre)
    private double maxSpeed; //la vitesse max du kart
    private double h,l;//hauteur et largeur de l'objet, la hauteur étant dans la direction du vecteur (dx,dy)
    private boolean derapeDroite; //true si le kart dérape à droite
    private boolean derapeGauche; //true si le kart dérape à gauche
    private double contrebraque;// coeff de contrebraquage lors du dérapage
    private double contrebraqueMax;// le coefficient MINIMUM de contrebraquage, au plus fort du contrebraquage
    private double coeff;//coeff qui fait diminuer la fadm lors du dérapage
    private double coeffFrein;//coeff de freinage qui fait augmenter la fCent lorsqu'on freine;
    private double dxDir,dyDir; //vecteur direction (=/= orientation) du kart lors du dérapage
    private int compt;//compteur dérapage
    private double thetaOri;//le theta quand le kart a commence à déraper
    private boolean aBonus;//true si le kart a un bonus disponible
    private String nomBonus; //le nom du bonus disponible

    
    
    
    public Kart(double x,double y,double dx,double dy,double maxSpeed, double rayCourb, double maxAcc,double poids,double adherence){
        super(x,y,dx,dy);
        h=2;//hauteur de 2m
        l=1;//largeur de 1m
        this.maxSpeed=maxSpeed;
        this.rayCourb=rayCourb;
        this.maxAcc=maxAcc;
        derapeDroite=false;
        derapeGauche=false;
        contrebraque=1;
        coeff=0.2;
        contrebraqueMax=0.25;
        this.poids=poids;
        this.adherence=adherence;
        this.aBonus=false;
        this.nomObjet="KART";
        
        frontSpeed=0;
    }
    public void tourne(char a){//permet au kart de tourner selon un rayon de courbure
        double thetaTourne=0;
        if (a=='g'&& derapeGauche==false && derapeDroite==false && frontSpeed>0.2){
            double dxc=-dy;
            double dyc=dx;
            double xc=x+rayCourb*dxc;
            double yc=y+rayCourb*dyc;
            thetaTourne=Math.atan2((y-yc),(x-xc));
            double dtheta=frontSpeed*(0.025)/rayCourb;; //le 0.025 vient du timer à 25ms, c'est dt
            x=xc+rayCourb*Math.cos(thetaTourne+dtheta);
            y=yc+rayCourb*Math.sin(thetaTourne+dtheta);  
            double norme=Math.sqrt((x-xc)*(x-xc)+(y-yc)*(y-yc));
            dy=(x-xc)/norme;
            dx=-(y-yc)/norme;
        }
    
        if (a=='d' && derapeDroite==false && derapeGauche==false && frontSpeed>0.2){
            double dxc=dy;
            double dyc=-dx;
            double xc=x+rayCourb*dxc;
            double yc=y+rayCourb*dyc;
            thetaTourne=Math.atan2((y-yc),(x-xc));
            double dtheta=frontSpeed*(0.025)/rayCourb; 
            x=xc+rayCourb*Math.cos(thetaTourne-dtheta);
            y=yc+rayCourb*Math.sin(thetaTourne-dtheta); 
            double norme=Math.sqrt((x-xc)*(x-xc)+(y-yc)*(y-yc));
            dy=-(x-xc)/norme;
            dx=(y-yc)/norme;
        }
    }
    
    public void avance(int i){// si i=0, le kart avance normalement, sinon il se contente d'augmenter la vitesse sans modifier la position
        if (derapeDroite || derapeGauche ){
           frontSpeed=frontSpeed-0.05; //Quand le kart dérape il perd de la vitesse
        }
        else if (frontSpeed<maxSpeed-6){
            frontSpeed=frontSpeed+maxAcc; 
        }
        else{
            frontSpeed=frontSpeed+maxAcc-0.03;//le kart a plus de mal à accélerer à haute vitesse
            if (frontSpeed>maxSpeed){
                frontSpeed=maxSpeed;
            }
        }

        if (i==0 && derapeDroite==false && derapeGauche==false){
            x=x+dx*(frontSpeed*0.025);
            y=y+dy*(frontSpeed*0.025);
        } 
    }
    
    public void ralentit(int i){//quand on relache la touche accélerer
         if (frontSpeed>0.2 && (derapeDroite || derapeGauche)){
            frontSpeed=frontSpeed-0.14;
         }
         else if (frontSpeed>0.2){
            frontSpeed=frontSpeed-0.1;
        }
        else{
            frontSpeed=0;
        }
        if (i==0 && derapeDroite==false && derapeGauche==false){
            x=x+dx*(frontSpeed*0.025);
            y=y+dy*(frontSpeed*0.025);   
        }
    }
    
    public void freine(){//quand on appuie sur la touche de frein/marche arrière
        if (frontSpeed==0){//si la vitesse est nulle, la kart fait une marche arrière
            x=x-dx*0.075;
            y=y-dy*0.075;
        }
        if (derapeDroite || derapeGauche){//le kart perd de la vitesse moins vite quand ça dérape
            frontSpeed=frontSpeed-0.04;
        }
        else{
            frontSpeed=frontSpeed-0.12;
        }
    }
    
    public void derapage(char tourne,char freine){
        this.calculTheta();
        double fAdm=adherence*2000;//limite de dérapage à environ 12 m/s d'un kart avec un poids moyen (150kg) et une adhérence neutre (1), Rc=10m
        if (derapeDroite==false && derapeGauche==false){//coef de contrebraquage neutre si le kart ne dérape pas
            contrebraque=1;
        }                                       
        else if ((derapeGauche && tourne=='d')||(derapeDroite && tourne=='g')){//coef de contrebraquage
            if (contrebraque>contrebraqueMax){
                contrebraque=contrebraque-0.028;
                
                if (contrebraque<contrebraqueMax){
                    contrebraque=contrebraqueMax;
                }
            }
        }
        else if ((derapeGauche || derapeDroite) && tourne=='0'){//coeff moins fort quand on ne fait que redresser les roues
            if (contrebraque>contrebraqueMax+0.4){//d'où le 0.4
                contrebraque=contrebraque-0.0125;
                if (contrebraque<contrebraqueMax+0.4){
                    contrebraque=contrebraqueMax+0.4;
                }
            }  
        }

        if (freine=='y'){         //coeff de freinage qui augmente la facilité à déraper quand on freine
            coeffFrein=1.4;
        }
        else{
            coeffFrein=1;
        }
        fCent=coeffFrein*contrebraque*poids*frontSpeed*frontSpeed/(rayCourb);//calcul de la force centrifuge
        if (fCent<fAdm*coeff){
            derapeGauche=false;
            derapeDroite=false;
            coeff=0.4; //reset du coeff à 0.4 à la fin du dérapage
            compt=0;//reset du compteur dérapage  
        }
        if(tourne=='0' && derapeGauche==false && derapeDroite==false){
            compt=0;
        }
        
        if ((tourne=='g' && fCent>fAdm && derapeDroite==false) || (derapeGauche && fCent>fAdm*coeff )){
            coeff=0.4;//reset du coeff 0.2
            compt++;
            if (compt>12){//le dérapage ne s'active que si on a commencé à tourner depuis un certain temps 
                if (derapeGauche==false){
                    dxDir=dx;//les directions vers lesquelles la voiture roulait quand elle a commencé à déraper
                    dyDir=dy;
                    thetaOri=theta;

                }
                derapeGauche=true;
                double thetaDir=frontSpeed*frontSpeed*coeffFrein*0.00008; //l'angle de direction de la voiture càd vers où elle avancera, qui s'ajoute à chaque itération
                dxDir=Math.cos(thetaDir+thetaOri);
                dyDir=Math.sin(thetaDir+thetaOri);
                thetaOri=thetaOri+thetaDir;
                double thetad=frontSpeed*0.0015*coeffFrein;//0.0015 coeff arbitraire pour avoir un angle convenable de dérapage (si on freine l'angle est plus grand) 
                                                            //le thetad représente l'angle de pivotement (qui s'ajoute à chaque itération) de la voiture sur elle même=/=thetaDir          
                dx=Math.cos(thetad+theta);
                dy=Math.sin(thetad+theta);
                if (Math.abs(Math.atan2(dy,dx)-Math.atan2(dyDir,dxDir))>0.5){//il derape plus longtemps si la direction de la voiture est différente de celle vers laquelle elle dérape
                    coeff=0.15;
                }
            }
            
        }
        
        else if ((tourne=='d' && fCent>fAdm && derapeGauche==false)|| (derapeDroite && fCent>fAdm*coeff)){
            coeff=0.4;
            compt++;
            if (compt>12){
                if (derapeDroite==false){
                    dxDir=dx;//les directions vers lesquelles la voiture roulait quand elle a commencé à déraper
                    dyDir=dy;
                    thetaOri=theta;
                }
                derapeDroite=true;
                double thetaDir=frontSpeed*frontSpeed*coeffFrein*0.00008;
                dxDir=Math.cos(-thetaDir+thetaOri);
                dyDir=Math.sin(-thetaDir+thetaOri);
                thetaOri=thetaOri+thetaDir;
                double thetad=frontSpeed*0.0015*coeffFrein;
                dx=Math.cos(-thetad+theta);
                dy=Math.sin(-thetad+theta);
 
                if (Math.abs(Math.atan2(dy,dx)-Math.atan2(dyDir,dxDir))>0.5){
                    coeff=0.15;
                }
            }
        }
        if (derapeDroite || derapeGauche){
            x=x+dxDir*(frontSpeed*0.025);
            y=y+dyDir*(frontSpeed*0.025);
        }
    }
    
  
    public void draw(Graphics g, int ax, int ay, double echelle){
            
            g.drawImage(image,ax,ay,null);
        }
    
    
    public double getdx(){
        return dx;
    }
    public double getdy(){
        return dy;
    }
    
    public boolean collision(Item item){// les coordonnées des coins du kart doivent avoir été mises à jour 
        this.calculTheta();                                                                       
        item.calculTheta();
        boolean colli=false;                                                                          
        //test préalable pour voir si les objets sont proches ou pas                                  
        if (Math.sqrt((item.x-this.x)*(item.x-this.x)-(item.y-this.y)*(item.y*this.y))>4){      
            return colli;                                                                          
        }                                                                                  
                               
        else if (item.nomObjet=="KART"){   //N'A JAMAIS PU ETRE IMPLEMENTE
            //calcul des équations des 4 droites du kart
            double a1=-1/Math.tan(item.theta);//a1=a3
            double b1=item.y-(h/2)*Math.sin(item.theta)-a1*(item.x-(h/2)*Math.cos(item.theta));
            double a2=Math.tan(item.theta);//a4=a2
            double b2=item.y+(l/2)*Math.cos(item.theta)-a2*(item.x-(l/2)*Math.sin(item.theta));
            double b3=item.y+(h/2)*Math.sin(item.theta)-a1*(item.x+(h/2)*Math.cos(item.theta));
            double b4=item.y-(l/2)*Math.cos(item.theta)-a2*(item.x+(l/2)*Math.sin(item.theta));
            
            for (int i=0;i<5;i++){ //on teste si un des points du kart vérifie l'équation pour être à l'intérieur de l'autre  
                if (a1*tabx[i]-taby[i]+b1<0 && a2*tabx[i]-taby[i]+b2>0 && a1*tabx[i]-taby[i]+b3>0 && a2*tabx[i]-taby[i]+b4<0){
                    colli=true;
                    return colli;
                }
            }
        }return colli;
    }
    
    public void coordCoinsX(){// ici on calcule les coordonnés des coins du kart
        //Attention theta doit être mis à jour                                                               
        this.tabx[0]=this.x;//au milieu du kart                                                               
        this.tabx[1]=this.x-(h/2)*Math.cos(this.theta)+(l/2)*Math.sin(this.theta);//en bas à droite du Kart          
        this.tabx[2]=this.x-(h/2)*Math.cos(this.theta)-(l/2)*Math.sin(this.theta);//en bas à gauche          
        this.tabx[3]=this.x-(l/2)*Math.sin(this.theta)+(h/2)*Math.cos(this.theta);//en haut à gauche          
        this.tabx[4]=this.x+(h/2)*Math.cos(this.theta)+(l/2)*Math.sin(this.theta);//en haut à droite
        //for (int i=0;i<5;i++){
           // System.out.println(tabx[i]);
       // }
    }
    
    public void coordCoinsY(){//Attention theta doit être mis à jour 
 
        this.taby[0]=this.y;                                                                
        this.taby[1]=this.y-(h/2)*Math.sin(this.theta)-(l/2)*Math.cos(this.theta);                    
        this.taby[2]=this.y-(h/2)*Math.sin(this.theta)+(l/2)*Math.cos(this.theta);                   
        this.taby[3]=this.y+(l/2)*Math.cos(this.theta)+(h/2)*Math.sin(this.theta);                    
        this.taby[4]=this.y+(h/2)*Math.sin(this.theta)-(l/2)*Math.cos(this.theta);
       // for (int i=0;i<5;i++){
           // System.out.println(taby[i]);
        //}
    }
    
    public boolean colliMurs(){
        boolean colliMur=false;
        this.coordCoinsX();
        this.coordCoinsY();
        for (int i=0;i<5;i++){
            //regarde si un des coins est dans l'ellipse intérieur ou en dehors de l'ellipse extérieur
            if (((tabx[i]-250)*(tabx[i]-250)/((PanelField.aInt)*(PanelField.aInt)))+((taby[i]-175)*(taby[i]-175)/((PanelField.bInt)*(PanelField.bInt)))<=1 || ((tabx[i]-250)*(tabx[i]-250)/((PanelField.aExt)*(PanelField.aExt)))+((taby[i]-175)*(taby[i]-175)/((PanelField.bExt)*(PanelField.bExt)))>=1 ){
                colliMur=true;
                return colliMur;
            }
            
        }return colliMur;
    }
    
    public void doColliMurs(){//le kart reculera à l'opposée de la direction vers laquelle il avançait
        if (derapeGauche==false && derapeDroite==false){
            this.setX(x-dx*2.5);
            this.setY(y-dy*2.5);
            this.setSpeed(0);
        }
        if (derapeGauche || derapeDroite ){
            this.setX(x-dxDir*2.5);
            this.setY(y-dyDir*2.5);
            this.setSpeed(0);
        }
    }
    
    
    public void doCollision(Item item){//ne sera apppliqué que avec un autre kart
        if (((Kart)item).poids>this.poids){//quand le kart adverse est plus lourd, la vitesse de notre kart est divisé par 2 lors de la collision
            this.setSpeed(this.frontSpeed/2);
        }else if (((Kart)item).poids<this.poids){//sinon l'inverse se produit
            ((Kart)item).setSpeed((((Kart)item).getSpeed())/2);
        }
    }
    
    public void setSpeed(double a){
        frontSpeed=a;
    }
    
    public void setMaxSpeed(double a){
        maxSpeed=a;
    }
    
    public void setAdherence(double a){
        this.adherence=a;
    }
    
    public void setPoids(double a){
        this.poids=a;
    }
    
    public void setImage(int numkart, int modeJoueur){
        if(modeJoueur==1){
            if (numkart==1){
                try{
                    image= ImageIO.read(new File("kart1.png"));
                }catch (IOException e){
                    System.out.println("Could not load image file"+"kart1.png");
                    System.exit(1);
                }
            }
            else if (numkart==2){
                try{
                    image= ImageIO.read(new File("kart2.png"));
                }catch (IOException e){
                    System.out.println("Could not load image file"+"kart2.png");
                    System.exit(1);
                }
            }
            else{
                try{
                    image= ImageIO.read(new File("kart3.png"));
                }catch (IOException e){
                    System.out.println("Could not load image file"+"kart3.png");
                    System.exit(1);
                }
            }
        }
        else{
            if (numkart==1){
                try{
                    image= ImageIO.read(new File("kart12.png"));
                }catch (IOException e){
                    System.out.println("Could not load image file"+"kart1.png");
                    System.exit(1);
                }
            }
            else if (numkart==2){
                try{
                    image= ImageIO.read(new File("kart22.png"));
                }catch (IOException e){
                    System.out.println("Could not load image file"+"kart2.png");
                    System.exit(1);
                }
            }
            else{
                try{
                    image= ImageIO.read(new File("kart32.png"));
                }catch (IOException e){
                    System.out.println("Could not load image file"+"kart3.png");
                    System.exit(1);
                }
            }
        }
    }
    
    
    public double getSpeed(){
        return frontSpeed;
    }
    
    public double getMaxSpeed(){
        return maxSpeed;
    }
    
    
    public boolean Bonus(){
        return aBonus;
    }
    public String getNomBonus(){
        return nomBonus;
    }
    
    public void move(){
        
    }
    public void setABonus(boolean b){
        this.aBonus=b;
    }
    
    public void setBonus(int i){
        if (i==1){
            this.nomBonus="MISSILE";
        }
        if (i==2){
            this.nomBonus="BANANE";
        }
        if (i==3){
            this.nomBonus="BOMBE";
        }
        this.aBonus=true;
    }
}
