package logbook.internal;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * オーディオに関するメソッドを集めたクラス
 *
 */
public final class Audios {

    private static final String SUPPORTED_FILES = "*.{aif,aiff,fxm,flv,m3u8,m3u8,mp3,mp4,m4a,m4v,wav}";

    /**
     * ディレクトリからサポートされているオーディオファイルの拡張子のファイルをランダムに1つ取得します。
     *
     * @param dir ディレクトリ
     * @return オーディオファイル、存在しない場合null
     * @throws IOException 入出力エラーが発生した場合
     */
    public static Path randomAudioFile(Path dir) throws IOException {
        if (Files.isDirectory(dir)) {
            try (DirectoryStream<Path> ds = Files.newDirectoryStream(dir, SUPPORTED_FILES)) {
                List<Path> paths = StreamSupport.stream(ds.spliterator(), false)
                        .collect(Collectors.toList());
                if (paths.size() > 0) {
                    return paths.get((int) Math.floor(Math.random() * paths.size()));
                }
            }
        }
        return null;
    }
}
