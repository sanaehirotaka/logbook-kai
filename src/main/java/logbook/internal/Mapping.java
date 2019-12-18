package logbook.internal;

import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import logbook.bean.MapinfoMst;
import logbook.bean.MapinfoMstCollection;
import logbook.plugin.PluginServices;

/**
 * セルNoと記号のマッピング
 *
 */
public class Mapping {

    private static Mapping INSTANCE = new Mapping();

    /** セルNoと記号のマッピング*/
    private Map<String, String> mapping;

    private Mapping() {
        InputStream is = PluginServices.getResourceAsStream("logbook/map/mapping.json");
        Map<String, String> mapping = Collections.emptyMap();
        if (is != null) {
            try {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.enable(Feature.ALLOW_COMMENTS);
                    mapping = mapper.readValue(is, new TypeReference<LinkedHashMap<String, String>>() {
                    });
                } finally {
                    is.close();
                }
            } catch (Exception e) {
                LoggerHolder.get().error("マッピングの初期化に失敗しました", e);
            }
        }
        this.mapping = mapping;
    }

    public Map<String, String> getMapping() {
        return this.mapping;
    }

    /**
     * 対象セルの記号を返します
     * 
     * @param key 海域-マップ番号-セル形式のキー
     * @return 対象セルの記号
     */
    public static String getCell(String key) {
        String ret = INSTANCE.getMapping().get(key);
        if (ret == null) {
            return key.substring(key.lastIndexOf('-') + 1);
        }
        return ret;
    }

    /**
     * 対象セルの記号を返します
     * 
     * @param mapareaId 海域
     * @param mapinfoNo マップ番号
     * @param no セル
     * @return 対象セルの記号
     */
    public static String getCell(Integer mapareaId, Integer mapinfoNo, Integer no) {
        String key = String.valueOf(mapareaId) + "-" + String.valueOf(mapinfoNo) + "-" + String.valueOf(no);
        String ret = INSTANCE.getMapping().get(key);
        if (ret == null) {
            return String.valueOf(no);
        }
        return ret;
    }

    /**
     * 海域名と略称(例:1-5)のマッピングを返します
     * @return 海域名と略称(例:1-5)のマッピング
     */
    public static Map<String, String> fullNameToShort() {
        return MapinfoMstCollection.get()
                .getMapinfo()
                .values()
                .stream()
                .collect(Collectors.toMap(MapinfoMst::getName,
                        m -> m.getMapareaId() + "-" + m.getNo(), (a, b) -> a));
    }
}
