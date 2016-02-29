package logbook.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import logbook.internal.Config;

/**
 * 資材コレクション
 *
 */
public class MaterialCollection implements Serializable {

    private static final long serialVersionUID = 5006063137688404374L;

    /** 資材 */
    private Map<Integer, Material> materialMap = new HashMap<>();

    /**
     * 資材を取得します。
     * @return 資材
     */
    public Map<Integer, Material> getMaterialMap() {
        return this.materialMap;
    }

    /**
     * 資材を設定します。
     * @param materialMap 資材
     */
    public void setMaterialMap(Map<Integer, Material> materialMap) {
        this.materialMap = materialMap;
    }

    /**
     * アプリケーションのデフォルト設定ディレクトリから{@link MaterialCollection}を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(MaterialCollection.class, MaterialCollection::new)</code>
     * </blockquote>
     *
     * @return {@link MaterialCollection}
     */
    public static MaterialCollection get() {
        return Config.getDefault().get(MaterialCollection.class, MaterialCollection::new);
    }
}
