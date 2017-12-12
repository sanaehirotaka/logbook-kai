package logbook.api;

import javax.json.JsonObject;

import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_mission/result
 *
 */
@API("/kcsapi/api_req_mission/result")
public class ApiReqMissionResult implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        // TODO 自動生成されたメソッド・スタブ

    }

}
