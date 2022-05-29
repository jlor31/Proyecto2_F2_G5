package GUI;
import DB_Control.Libros;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

//fuente consultada para implementar GUI: https://www.youtube.com/watch?v=iE8tZ0hn2Ws&t=936s

 



public class Generador implements ActionListener {

	private static JLabel label;
	private static JLabel rec;
	private static JLabel rec1;
	private static JLabel rec2;
	private static JLabel rec3;
	private static JLabel rec4;
	private static JLabel rec5;
	private static JTextField titulo;
	private static JButton boton;
	
	private static JPanel panel = new JPanel();
	private static JFrame frame = new JFrame();
	
	 
	private static ArrayList<ArrayList<String>> recomendaciones = new ArrayList<ArrayList<String>>();
	
	public static void prompt(){
		
		frame.setSize(1000,400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		
		panel.setLayout(null);
		
		label = new JLabel("LIBRO QUE TE GUSTA");
		label.setBounds(55,30,150,25);
		panel.add(label);
		
		titulo = new JTextField(20);
		titulo.setBounds(200,30,165,25);
		panel.add(titulo);
		
		
		boton = new JButton("RECOMENDAR MAS!");
		boton.setBounds(405, 30, 180, 25);
		boton.addActionListener(new Generador());
		panel.add(boton);
		
		rec = new JLabel("");
		rec.setBounds(120,50,1000,50);
		panel.add(rec);
		
		rec1 = new JLabel("");
		rec1.setBounds(120,80,1000,50);
		panel.add(rec1);
		
		rec2 = new JLabel("");
		rec2.setBounds(120,110,1000,50);
		panel.add(rec2);
		
		rec3 = new JLabel("");
		rec3.setBounds(120,140,1000,50);
		panel.add(rec3);
		
		rec4 = new JLabel("");
		rec4.setBounds(120,170,1000,50);
		panel.add(rec4);
		
		rec5 = new JLabel("");
		rec5.setBounds(120,200,1000,50);
		panel.add(rec5);
		
		frame.setVisible(true);
		
	}
	

	
	public void actionPerformed(ActionEvent e) {
		
		rec1.setText("");
		rec2.setText("");
		rec3.setText("");
		rec4.setText("");
		rec5.setText("");
		
		
		try {
		recomendaciones();
		} catch (Exception E) {
			
			E.printStackTrace();
			
		}
	}
	
	/**
	 * @throws Exception
	 */
	public static void recomendaciones() throws Exception {
		
		try ( Libros libros = new Libros( "neo4j+s://e8cabdf1.databases.neo4j.io", "neo4j", "iZwumGTsvgEW3Cp4XQdhvpVs24KzeW8FSJ4M0lnLDLw" ) )
        {
			
			libros.query(titulo.getText().toLowerCase());
			recomendaciones = Libros.getRecs();
        }
		
		rec.setText("Generando recomendaciones para: " + titulo.getText());
		titulo.setText(null);
		rec1.setText("Libro 1: " + recomendaciones.get(0).get(0).toUpperCase() + " de " + recomendaciones.get(0).get(1) + "   Similarity: " + recomendaciones.get(0).get(2));
		rec2.setText("Libro 2: " + recomendaciones.get(1).get(0).toUpperCase() + " de " + recomendaciones.get(1).get(1) + "   Similarity: " + recomendaciones.get(1).get(2));
		rec3.setText("Libro 3: " + recomendaciones.get(2).get(0).toUpperCase() + " de " + recomendaciones.get(2).get(1) + "   Similarity: " + recomendaciones.get(2).get(2));
		rec4.setText("Libro 4: " + recomendaciones.get(3).get(0).toUpperCase() + " de " + recomendaciones.get(3).get(1) + "   Similarity: " + recomendaciones.get(3).get(2));
		rec5.setText("Libro 5: " + recomendaciones.get(4).get(0).toUpperCase() + " de " + recomendaciones.get(4).get(1) + "   Similarity: " + recomendaciones.get(4).get(2));
		
		recomendaciones.clear();
	}
	
	
	
	public static void main(String[] args) throws Exception {
		
		prompt();
		
		
	    }
		
	

	
	
	
	
	
	
}
