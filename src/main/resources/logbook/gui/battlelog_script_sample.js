/**
 * 高度な集計 サンプルスクリプト
 *
 * 高度な集計では戦闘ログの集計をJavaScriptを使って行うことが出来ます。
 * このサンプルスクリプトでは艦娘経験値を海域毎に集計します。
 */
(function() {
    // 初期値
    var initialValue = { "海域" : {}, "合計" : 0 };
    // 戦闘ログに対して実行されるコールバック関数
    // accumulator :
    //  コールバックの戻り値を累積します。
    //  ひとつ前の処理結果または initialValue を指します。
    // currentValue :
    //  現在処理されている戦闘ログ。
    //  戦闘ログの内容はbattlelogフォルダに保存されたJSONファイルです。
    //  (注意)
    //  標準では戦闘ログは航海日誌が内部的に使用する表現方法でデータが保存されています。
    //  艦これの標準的なJSONで処理を行う場合、戦闘ログにローデータを含めるように設定を変更してください。
    //  ローデータが含まれている場合、次のプロパティから参照出来ます。
    //   - currentValue.raw.battle   : 昼戦、特殊夜戦
    //   - currentValue.raw.midnight : 夜戦
    //   - currentValue.raw.result   : 戦闘結果
    var reducer = function(accumulator, currentValue) {
        var exp = 0;
        // 第1艦隊経験値
        exp += currentValue.result.getShipExp.reduce(function(a,b) {
            return a + b;
        });
        // 第2艦隊経験値(連合艦隊)
        if (currentValue.result.getShipExpCombined) {
            exp += currentValue.result.getShipExpCombined.reduce(function(a,b) {
                return a + b;
            });
        }
        var questName = currentValue.result.questName;
        accumulator["海域"][questName] = (accumulator["海域"][questName] | 0) + exp;
        accumulator["合計"] += exp;

        return accumulator;
    };

    // LogCollector.reduceにコールバック関数及び初期値を引数として渡して実行する。
    var result = LogCollector.reduce(reducer, initialValue);

    // コールバック関数の最終的な戻り値をJSON.stringifyの引数として渡して整形し出力する。
    print(JSON.stringify(result, null, "    "));
})();
