package logbook.bean;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import logbook.bean.QuestList.Quest;
import logbook.internal.Logs;
import lombok.Data;

/**
 * 任務
 */
@Data
public class AppQuest implements Serializable {

    private static final long serialVersionUID = 4212109733911812553L;

    /** デイリー */
    private static final int DAILY = 1;
    /** ウィークリー */
    private static final int WEEKLY = 2;
    /** マンスリー */
    private static final int MONTHLY = 3;
    /** 単発 */
    private static final int ONECE = 4;
    /** クォータリー */
    private static final int QUARTRELY = 5;
    /** イヤリー */
    private static final int YEARLY = 100;  // 艦これ的には「その他(5)」なためかぶらないように大きな数字にする

    /** No */
    private Integer no;

    /** 任務 */
    private Quest quest;

    /** 期限 */
    private String expire;

    /** 受諾中 */
    private boolean active;

    /**
     * 任務を構築します
     *
     * @param quest 任務
     * @return {@link AppQuest}
     */
    public static AppQuest toAppQuest(Quest quest) {
        AppQuest bean = new AppQuest();
        bean.setNo(quest.getNo());
        bean.setQuest(quest);
        bean.setActive(quest.getState() == 2 || quest.getState() == 3);  // 受諾中、もしくは完了済み

        ZonedDateTime base = ZonedDateTime.now(ZoneId.of("GMT+04:00"))
                .truncatedTo(ChronoUnit.DAYS);
        ZonedDateTime expire = null;

        int type = quest.getType();
        int yearlyResetMonth = 0;
        
        AppQuestCondition condition = AppQuestCondition.loadFromResource(quest.getNo());
        if (condition != null) {
            String resetType = condition.getResetType();
            if (resetType != null) {
                switch (resetType) {
                case "デイリー":
                    type = DAILY;
                    break;
                case "ウィークリー":
                    type = WEEKLY;
                    break;
                case "マンスリー":
                    type = MONTHLY;
                    break;
                case "単発":
                    type = ONECE;
                    break;
                case "クオータリー":
                case "クォータリー":
                    type = QUARTRELY;
                    break;
                case "イヤリー":
                    type = YEARLY;
                    yearlyResetMonth = condition.getYearlyResetMonth();
                    break;
                }
            }
        }

        if (type == DAILY) {
            // 1=デイリー
            // 1日加算
            expire = base.plusDays(1)
                    .withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
        } else if (type == WEEKLY) {
            // 2=ウィークリー
            // 7日加算して曜日(1(月曜日)から7(日曜日))-1を減算する
            expire = base.plusWeeks(1)
                    .minusDays(base.getDayOfWeek().getValue() - 1)
                    .withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
        } else if (type == MONTHLY) {
            // 3=マンスリー
            // 翌月1日にする
            expire = base
                    .withDayOfMonth(1)
                    .plusMonths(1)
                    .withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
        } else if (type == ONECE) {
            // 4=単発
            // 期限なし、とりあえず9999年12月31日
            expire = base
                    // XXXX年1月1日
                    .withDayOfYear(1)
                    // 10000年1月1日
                    .withYear(10000)
                    // 9999年12月31日
                    .minusDays(1)
                    .withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
        } else if (type == QUARTRELY) {
            // 5=他
            // クオータリー最終月を求める
            int addMonth = (base.getMonthValue() / 3 * 3) - base.getMonthValue() + 3;
            // クオータリー最終月の翌月1日
            expire = base.withDayOfMonth(1)
                    .plusMonths(addMonth)
                    .withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
        } else if (type == YEARLY) {
            // 100 = イヤリー、艦これ上は5=他
            expire = (base.getMonthValue() >= yearlyResetMonth ? base.plusYears(1) : base)
                    .withMonth(yearlyResetMonth)
                    .withDayOfMonth(1)
                    .withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
        }
        if (expire != null) {
            bean.setExpire(Logs.DATE_FORMAT.format(expire));
        }

        return bean;
    }
}
