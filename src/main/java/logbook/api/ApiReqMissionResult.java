package logbook.api;

import javax.json.JsonObject;

import logbook.bean.MissionResult;
import logbook.internal.log.LogWriter;
import logbook.internal.log.MissionResultLogFormat;
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

        JsonObject data = json.getJsonObject("api_data");
        if (data != null) {
            MissionResult result = MissionResult.toMissionResult(data);

            LogWriter.getInstance(MissionResultLogFormat::new)
                    .write(result);
        }
    }

}
