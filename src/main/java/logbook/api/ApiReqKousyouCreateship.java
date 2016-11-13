package logbook.api;

import javax.json.JsonObject;

import logbook.bean.Createship;
import logbook.bean.CreateshipCollection;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_kousyou/createship
 *
 */
@API("/kcsapi/api_req_kousyou/createship")
public class ApiReqKousyouCreateship implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        Createship createship = Createship.toCreateship(req);
        CreateshipCollection.get()
                .getCreateshipMap()
                .put(createship.getKdockId(), createship);
    }

}
