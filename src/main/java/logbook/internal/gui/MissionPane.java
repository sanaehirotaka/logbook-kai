package logbook.internal.gui;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import logbook.bean.AppCondition;
import logbook.bean.BattleTypes.CombinedType;
import logbook.bean.DeckPort;
import logbook.bean.Mission;
import logbook.bean.MissionCollection;
import logbook.internal.LoggerHolder;
import logbook.internal.Time;

/**
 * 艦隊
 *
 */
public class MissionPane extends AnchorPane {

    /** 艦隊 */
    private final DeckPort port;

    /** 色変化1段階目 */
    private final Duration stage1 = Duration.ofMinutes(20);

    /** 色変化2段階目 */
    private final Duration stage2 = Duration.ofMinutes(10);

    /** 色変化3段階目 */
    private final Duration stage3 = Duration.ofMinutes(5);

    @FXML
    private ProgressBar progress;

    @FXML
    private Label fleet;

    @FXML
    private Label name;

    @FXML
    private Label time;

    /**
     * 艦隊ペインのコンストラクタ
     *
     * @param port 艦隊
     */
    public MissionPane(DeckPort port) {
        this.port = port;
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/mission.fxml");
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            LoggerHolder.get().error("FXMLのロードに失敗しました", e);
        }
    }

    @FXML
    void initialize() {
        try {
            this.update();
            // マウスオーバーでのポップアップ
            PopOver<DeckPort> popover = new PopOver<>((node, port) -> {
                String message;
                // 帰還時間
                long time = this.port.getMission().get(2);
                if (time > 0) {
                    ZonedDateTime dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(time),
                            ZoneOffset.systemDefault());
                    if (dateTime.toLocalDate().equals(ZonedDateTime.now().toLocalDate())) {
                        message = "今日 " + DateTimeFormatter.ofPattern("H時m分s秒").format(dateTime)
                                + " 頃に帰投します";
                    } else {
                        message = DateTimeFormatter.ofPattern("M月d日 H時m分s秒").format(dateTime)
                                + " 頃に帰投します";
                    }
                } else {
                    message = "任務がありません";
                }
                return new PopOverPane(port.getName(), message);
            });
            popover.install(this, this.port);
        } catch (Exception e) {
            LoggerHolder.get().error("FXMLの初期化に失敗しました", e);
        }
    }

    /**
     * 画面を更新します
     */
    public void update() {
        // 0=未出撃, 1=遠征中, 2=遠征帰還, 3=遠征中止
        int state = this.port.getMission().get(0).intValue();
        // 遠征先ID
        int target = this.port.getMission().get(1).intValue();
        // 帰還時間
        long time = this.port.getMission().get(2);

        ObservableList<String> styleClass = this.getStyleClass();
        styleClass.removeAll("stage1", "stage2", "stage3", "empty");

        if (state == 0 || target == 0 || time == 0) {
            // 未出撃
            // プログレスバー
            this.progress.setProgress(0);
            this.progress.setVisible(false);
            // 艦隊名
            this.fleet.setText(this.port.getName());
            if (AppCondition.get().isCombinedFlag() && this.port.getId() == 2) {
                // 連合編成中
                this.name.setText("(連合艦隊)");
                this.time.setText(
                        Optional.of(AppCondition.get().getCombinedType())
                            .map(CombinedType::toCombinedType)
                            .filter(type -> type != CombinedType.未結成)
                            .map(CombinedType::toString)
                            .orElse("<不明な連合艦隊タイプ>")
                 );
            } else {
                // 遠征先
                this.name.setText("<未出撃>");
                styleClass.add("empty");
                // 残り時間
                this.time.setText("");
            }
        } else {
            // 出撃(遠征中・遠征帰還・遠征中止)
            Optional<Mission> mission = Optional.ofNullable(MissionCollection.get()
                    .getMissionMap()
                    .get(target));

            // 遠征の最大時間
            Duration max = Duration.ofMinutes(mission.map(Mission::getTime).orElse(0));
            // 残り時間を計算
            Duration now = Duration.ofMillis(time - System.currentTimeMillis());

            if (!max.isZero()) {
                double p = (double) (max.toMillis() - now.toMillis()) / (double) max.toMillis();
                this.progress.setProgress(p);
                this.progress.setVisible(true);
            } else {
                this.progress.setProgress(0);
                this.progress.setVisible(false);
            }
            // 艦隊名
            this.fleet.setText(this.port.getName());
            // 遠征先
            this.name.setText(mission.map(Mission::getName).orElse(""));
            // 残り時間を更新
            this.time.setText(Time.toString(now, "まもなく帰還します"));

            // スタイルを更新
            if (now.compareTo(this.stage3) < 0) {
                styleClass.add("stage3");
            } else if (now.compareTo(this.stage2) < 0) {
                styleClass.add("stage2");
            } else if (now.compareTo(this.stage1) < 0) {
                styleClass.add("stage1");
            }
        }
    }
}
