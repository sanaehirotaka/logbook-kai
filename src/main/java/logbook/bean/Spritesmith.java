package logbook.bean;

import java.util.Map;

import lombok.Data;

/**
 * スプライト情報
 *
 */
@Data
public class Spritesmith {

    private Map<String, Frame> frames;

    @Data
    public class Frame {
        private Rect frame;
        private boolean rotated;
        private boolean trimmed;
        private Rect spriteSourceSize;
        private Rect sourceSize;
    }

    @Data
    public class Rect {
        private int x;
        private int y;
        private int w;
        private int h;
    }
}
