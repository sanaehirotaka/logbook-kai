package logbook.api;

import java.util.Map;

import javax.json.JsonObject;

import logbook.bean.Quest;
import logbook.bean.QuestCollection;
import logbook.internal.JsonHelper;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_get_member/questlist
 *
 */
@API("/kcsapi/api_get_member/questlist")
public class ApiGetMemberQuestlist implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject data = json.getJsonObject("api_data");
//        System.out.println(data);
//        System.out.println("exec count:" + data.getInt("api_exec_count"));
        if (data != null) {
            // 任務
            Map<Integer, Quest> quest = JsonHelper.toMap(data.getJsonArray("api_list"), Quest::getNo, Quest::toQuest);
            QuestCollection.get().addQuest(quest, data.getInt("api_disp_page") * data.getInt("api_page_count"));
        }
    }

}
