package logbook.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * ウインドウの位置とサイズ
 *
 */
@Data
public class WindowLocation implements Serializable {

    private static final long serialVersionUID = 3548270453031293618L;

    /** X */
    private double x;

    /** Y */
    private double y;

    /** Width */
    private double width;

    /** Height */
    private double height;

}
