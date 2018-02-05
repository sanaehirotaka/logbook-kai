package logbook.internal;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 画像キャッシュ設定
 *
 */
public enum ShipImageCacheStrategy {
    /** 全て */
    ALL,
    /** 使用される画像のみ */
    USED("1.jpg", "1.png", "3.jpg", "3.png"),
    /** 制限 */
    LIMIT("1.jpg", "1.png");

    private List<String> names;

    private ShipImageCacheStrategy() {
        this.names = null;
    }

    private ShipImageCacheStrategy(String... names) {
        this.names = Collections.unmodifiableList(Arrays.asList(names));
    }

    public List<String> getFileNames() {
        return this.names;
    }
}
