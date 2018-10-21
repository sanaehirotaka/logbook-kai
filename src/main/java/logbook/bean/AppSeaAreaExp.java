package logbook.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 海域経験値
 */
@Data
@AllArgsConstructor
public class AppSeaAreaExp {

    public AppSeaAreaExp() {
    }

    /** 海域 */
    private String name;

    /** 海域Exp */
    private int exp;

    @Override
    public String toString() {
        return this.name;
    }
}
