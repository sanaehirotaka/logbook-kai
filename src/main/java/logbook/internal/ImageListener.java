package logbook.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import logbook.bean.AppConfig;
import logbook.bean.ShipMst;
import logbook.bean.ShipMstCollection;
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
                this.ships(request, response);
            }
            // 汎用画像
            if (uri.startsWith("/kcs2/img/common/")) {
                this.common(request, response);
            }
        } catch (Exception e) {
            LoggerHolder.get().warn("画像ファイル処理中に例外が発生しました", e);
        }
    }

    private void ships(RequestMetaData request, ResponseMetaData response) throws IOException {
        String uri = URI.create(request.getRequestURI()).getPath();
        String name = null;

        if (uri.contains("/banner/"))
            name = "1";
        if (uri.contains("/banner_dmg/"))
            name = "3";
        if (uri.contains("/card/"))
            name = "5";
        if (uri.contains("/card_dmg/"))
            name = "7";
        if (uri.contains("/full/"))
            name = "17";
        if (uri.contains("/full_dmg/"))
            name = "19";

        if (name != null) {
            this.storeShipImage(name, request, response);
        }
    }

    private void storeShipImage(String name, RequestMetaData request, ResponseMetaData response) throws IOException {
        String uri = URI.create(request.getRequestURI()).getPath();
        int nameIndex = uri.lastIndexOf('/');
        int extIndex = uri.lastIndexOf('_');
        String shipid = uri.substring(nameIndex + 1, extIndex);

        ShipMst shipMst = ShipMstCollection.get()
                .getShipMap()
                .get(Integer.parseInt(shipid));
        Path path = ShipMst.getResourcePathDir(shipMst)
                .resolve(name + ".png");
        if (response.getResponseBody().isPresent()) {
            this.write(response.getResponseBody().get(), path);
        }
    }

    private void common(RequestMetaData request, ResponseMetaData response) throws IOException {
        String uri = request.getRequestURI();
        Path dir = Paths.get(AppConfig.get().getResourcesDir(), "common");
        Path path = dir.resolve(Paths.get(URI.create(uri).getPath()).getFileName());
        if (response.getResponseBody().isPresent()) {
            this.write(response.getResponseBody().get(), path);
        }
    }

    private void write(InputStream from, Path to) throws IOException {
        Path parent = to.getParent();
        if (parent != null) {
            if (!Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            Path temp = Files.createTempFile(parent, "ImageListener-", "");
            Files.copy(from, temp, StandardCopyOption.REPLACE_EXISTING);
            Files.move(temp, to, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
