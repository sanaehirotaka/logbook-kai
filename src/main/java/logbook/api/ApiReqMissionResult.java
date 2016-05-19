package logbook.api;

import javax.json.JsonObject;

import logbook.bean.MissionResult;
import logbook.internal.LogWriter;
import logbook.internal.Logs;
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

            new LogWriter()
                    .header(Logs.MISSION_RESULT.getHeader())
                    .file(Logs.MISSION_RESULT.getFileName())
                    .alterFile(Logs.MISSION_RESULT.getAlterFileName())
                    .write(result, Logs.MISSION_RESULT::format);
        }
    }

}
