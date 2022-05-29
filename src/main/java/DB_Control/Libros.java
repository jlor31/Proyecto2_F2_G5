package DB_Control;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import static org.neo4j.driver.Values.parameters;
import org.neo4j.driver.Record;

import java.util.ArrayList;
import java.util.HashMap;


//ejemplo consultado para estabecer conexion a DB: https://github.com/neo4j/neo4j-documentation/blob/4.4/cypher/cypher-docs/src/main/java/org/neo4j/cypher/example/JavaQuery.java


public class Libros implements AutoCloseable {
	
	
	private final Driver driver;
	
	private static HashMap<Integer, ArrayList<ArrayList<String>>> recomendaciones = new HashMap<>();
	
	
	
	/**
	 * @param uri
	 * @param user
	 * @param password
	 */
    public Libros( String uri, String user, String password )
    {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }
	
	/**
	 * Metodo Close
	 */
    public void close() throws Exception
    {
        driver.close();
    }

	/**
	 * Metodo Query
	 * @param query
	 */
    public void query( String query )
    {
        try ( Session session = driver.session() )
        {
             session.writeTransaction( tx ->
                                                        {
                                                            Result result = tx.run( "match (a:Book {title: $title})\r\n"
                                                            		+ "match (b:Book) where b.description <> a.description\r\n"
                                                            		+ "\r\n"
                                                            		+ "with a, b, apoc.text.clean(a.description) as norm1, apoc.text.clean(b.description) as norm2\r\n"
                                                            		+ "with toInteger(apoc.text.jaroWinklerDistance(norm1, norm2) * 100) as similarity, a, b\r\n"
                                                            		+ "with a, b,similarity where similarity >= 75 and a.categories = b.categories and a.average_rating <= b.average_rating\r\n"
                                                            		+ "return a.title as TITULO_INGRESADO, b.title as RECOMENDACION, b.categories as GENERO, b.average_rating as RATING, b.authors as AUTORES, b.num_pages as Q_PAGINAS, similarity\r\n"
                                                            		, parameters( "title", query ) );
                                                            
                                                            
                                                            
                                                            while (result.hasNext()) {
                                                            	
                                                            	Record record = result.next();
                                                            	String titulo_recomendado = record.get("RECOMENDACION").asString();
                                                            	String autor = record.get("AUTORES").asString();
                                                            	int similarity = record.get("similarity").asInt();
                                                            	
                                                            	
                                                            	ArrayList<String> titulo_autor = new ArrayList<String>();
                                                            	titulo_autor.add(titulo_recomendado);
                                                            	titulo_autor.add(autor);
                                                            	
                                                            	ArrayList<ArrayList<String>> new_val = new ArrayList<>();
                                                            	new_val.add(titulo_autor);
                                                            	
                                                            	
                                                            	if (recomendaciones.containsKey(similarity)) {
                                                            		
                                                            		ArrayList<ArrayList<String>> value = new ArrayList<ArrayList<String>>();
                                                            		value = recomendaciones.get(similarity);
                                                            		value.add(titulo_autor);
                                                            		
                                                            		recomendaciones.replace(similarity, value);
                                                            	}else {
                                                            		
                                                            		recomendaciones.put(similarity, new_val );
                                                            		
                                                            	}
                                                            	
                                                            	
                                                            }
                                                                                                                        
                                                            return result;
                                                            
                                                        } );
            
            
            
            
        }
    }

    
	/**
	 * @return Recs
	 */
    public static ArrayList<ArrayList<String>> getRecs(){
    	
    	System.out.println("TEST");
    	
    	ArrayList<ArrayList<String>> recs_final = new  ArrayList<>();
    	
    	
    	for (int i = 100; i >= 75; i--) {
    		
    		if (recomendaciones.containsKey(i)) {
    			
    			for (int j = 0; j < recomendaciones.get(i).size(); j ++){
    				
    				ArrayList<String> recom_sim = new ArrayList<String>();
    				Integer similarity = i;
    				
    				recom_sim.add(recomendaciones.get(i).get(j).get(0));
    				recom_sim.add(recomendaciones.get(i).get(j).get(1));
    				recom_sim.add(similarity.toString());
    				
    				recs_final.add(recom_sim);
    					
    			}
    			
    		}
    		
    		if (recs_final.size() > 5) {
    			
    			i = 0;
    		}
    	}
    	
    	recomendaciones.clear();
    	System.out.println(recs_final);
    	return recs_final;
    	
    }
    
    
    
    
    
    
    
    
    
    
}