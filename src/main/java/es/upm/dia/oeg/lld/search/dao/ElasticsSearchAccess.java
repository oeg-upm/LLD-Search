package es.upm.dia.oeg.lld.search.dao;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;


public class ElasticsSearchAccess {
	
	
	public static final String Index="apertiumlider";
    
    public static  Client client=null;
    
    
    public static final Client startClient(){
    	

    	Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "elasticsearch").build();
        TransportClient transportClient = new TransportClient(settings);
        transportClient = transportClient.addTransportAddress(new InetSocketTransportAddress("localhost", 9300)); // localhost ("localhost", 9300) 
        client = (Client) transportClient;
    	return client ;
    }
    
    public static final void closeClient(){
    		
    	client.close();
    }

}
