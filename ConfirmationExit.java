import java.awt.BorderLayout;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConfirmationExit extends JFrame {
    JButton Back = new JButton("Back to main menu");
    JButton Exit = new JButton("Quit the game");
    int nP;
    JLabel Player = new JLabel();
   
    
    public ConfirmationExit(int p){
        setTitle("The race is over !");
        setSize(600,220); 
        JPanel confirmation = new JPanel ();
        confirmation.setLayout(new GridLayout(3,1));
        confirmation.add(Player);
        confirmation.add(Back);
        confirmation.add(Exit);
        
        nP=p;
        Player.setText("                                                                             Player "+nP+" wins the race !");
        add(confirmation);
        
        this.Back.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                new Window().setVisible(true);
                dispose();
            }
        });
        
        this.Exit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                System.exit(0);
            }
        });
    }
    
    
}
