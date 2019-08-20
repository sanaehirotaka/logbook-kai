package logbook.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;

import logbook.bean.Mission;
import logbook.bean.MissionCollection;
import logbook.bean.MissionCondition;
import logbook.plugin.PluginServices;

/**
 * 遠征
 *
 */
public class Missions {

    /**
     * 遠征IDから{@link Mission}を返します。
     * 
     * @param missionId 遠征ID
     * @return {@link Mission}
     */
    public static Mission getMission(Integer missionId) {
        return MissionCollection.get()
                .getMissionMap()
                .get(missionId);
    }

    /**
     * 遠征IDから{@link MissionCondition}を返します。
     * 
     * @param missionId 遠征ID
     * @return {@link MissionCondition}
     * @throws IOException
     */
    public static Optional<MissionCondition> getMissionCondition(Integer missionId) throws IOException {
        Mission mission = Missions.getMission(missionId);

        if (mission == null) {
            return Optional.empty();
        }

        if ("前衛支援任務".equals(mission.getName())) {
            mission = Missions.getMission(33);
        } else if ("艦隊決戦支援任務".equals(mission.getName())) {
            mission = Missions.getMission(34);
        }

        InputStream is = PluginServices
                .getResourceAsStream("logbook/mission/" + mission.getMapareaId() + "/" + mission.getDispNo() + ".json");

        MissionCondition condition;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(Feature.ALLOW_COMMENTS);
            condition = mapper.readValue(is, MissionCondition.class);
        } finally {
            is.close();
        }

        return Optional.ofNullable(condition);
    }
}
