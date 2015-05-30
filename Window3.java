import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class Window3 extends JFrame{
    final int LARGPIX=1200;
    final int HAUTPIX=840;
    final int HAUTM=350;
    
    PanelField field1=new PanelField(HAUTM,1,3);
    PanelField field2=new PanelField(HAUTM,1,3);
    
    public Window3(){
        setTitle("Pro Kart Racing 2015");
        setSize(LARGPIX,HAUTPIX); 
        GridLayout layout = new GridLayout(1, 2);
        setLayout(layout);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        add(field1);
        add(field2);
        
        field1.setVisible(true);
        field2.setVisible(true);
        
        field1.Back.addActionListener(new ActionListener(){
               public void actionPerformed(ActionEvent actionEvent){
                   new Window().setVisible(true);
                   dispose();
               }
           });
        
        field2.Back.addActionListener(new ActionListener(){
               public void actionPerformed(ActionEvent actionEvent){
                   new Window().setVisible(true);
                   dispose();
               }
           });
    }
}
