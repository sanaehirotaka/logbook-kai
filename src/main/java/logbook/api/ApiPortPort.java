package logbook.api;

import javax.json.JsonObject;

import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_port/port
 *
 */
@API("/kcsapi/api_port/port")
public class ApiPortPort implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        // TODO 自動生成されたメソッド・スタブ

    }

}
