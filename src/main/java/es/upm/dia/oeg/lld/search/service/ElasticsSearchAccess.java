package es.upm.dia.oeg.lld.search.service;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;


public class ElasticsSearchAccess {
	
	
		public static final String Index="apertiumlider";
	    private static Client Cliente = null;

	    // Private constructor suppresses 
	    private ElasticsSearchAccess(){}

	    // creador sincronizado para protegerse de posibles problemas  multi-hilo
	    // otra prueba para evitar instanciación múltiple 
	    private synchronized static void createInstance() {
	        if (Cliente == null) { 
	             Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "elasticsearch").build();
	            TransportClient transportClient = new TransportClient(settings);
	            transportClient = transportClient.addTransportAddress(new InetSocketTransportAddress("localhost", 9300)); // localhost ("localhost", 9300) 
	            Cliente = (Client) transportClient;
	            		
	        }
	    }

	    public static Client getInstance() {
	        if (Cliente == null) createInstance();
	        return Cliente;
	    }
	
	
	    public static void closeClient(){
 		
	    	Cliente.close();
	    }

}
