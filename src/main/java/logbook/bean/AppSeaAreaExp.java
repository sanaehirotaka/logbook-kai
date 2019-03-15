package logbook.bean;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 海域経験値
 */
@Data
@AllArgsConstructor
public class AppSeaAreaExp implements Serializable {

    private static final long serialVersionUID = 380470177565807966L;

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
