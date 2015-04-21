package es.upm.dia.oeg.lld.search.dao;

import java.util.List;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.stereotype.Component;

import es.upm.dia.oeg.lld.search.model.Translation;

@Component
public class TranslationDAOImpl implements TranslationDAO {

    // RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<Translation> getTranslations() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Translation getTranslationByID(String id) {

        final Client client = new TransportClient().addTransportAddress(new InetSocketTransportAddress(
                "localhost", 9300));
        // final Node node = NodeBuilder.nodeBuilder().node();
        // final Client client = node.client();

        final GetResponse response = client.prepareGet("lldsearch",
                "directTrans", id).execute().actionGet();
        final Translation translation = new Translation(response.getId(),
                response.getSource());

        client.close();
        // node.close();

        return translation;
    }
}
