package logbook.internal;

import logbook.proxy.ContentListenerSpi;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * 画像ファイルを処理します
 *
 */
public class ImageListener implements ContentListenerSpi {

    @Override
    public boolean test(RequestMetaData request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/kcs2/resources/") || uri.startsWith("/kcs2/img/");
    }

    @Override
    public void accept(RequestMetaData request, ResponseMetaData response) {
        try {
            String uri = request.getRequestURI();
            // 艦娘画像
            if (uri.startsWith("/kcs2/resources/ship/")) {
                //this.ships(request, response);
            }
            // 装備アイコン
            if (uri.startsWith("/kcs2/img/common/common_icon_weapon.json")) {
                // スプライト情報
            }
            if (uri.startsWith("/kcs2/img/common/common_icon_weapon.png")) {
                // 画像
            }
            // misc
            if (uri.startsWith("/kcs2/img/common/common_misc.json")) {
                // スプライト情報
            }
            if (uri.startsWith("/kcs2/img/common/common_misc.png")) {
                // 画像
            }
        } catch (Exception e) {
            LoggerHolder.get().warn("画像ファイル処理中に例外が発生しました", e);
        }
    }
}
