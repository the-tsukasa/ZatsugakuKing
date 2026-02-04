package com.example.kadai09_pi12a_23

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

/**
 * 大量に問題を生成するユーティリティ（数量優先）
 */
object QuestionGenerator {
    
    fun generateQuestions(): List<JSONObject> {
        val questions = mutableListOf<JSONObject>()
        var currentId = 59
        
        // 世界: 28問追加 (現在22問 → 50問)
        questions.addAll(generateWorldQuestions(currentId, 28))
        currentId += 28
        
        // 日本: 30問追加 (現在20問 → 50問)
        questions.addAll(generateJapanQuestions(currentId, 30))
        currentId += 30
        
        // IT・テクノロジー: 37問追加 (現在13問 → 50問)
        questions.addAll(generateITQuestions(currentId, 37))
        currentId += 37
        
        // 歴史: 49問追加 (現在1問 → 50問)
        questions.addAll(generateHistoryQuestions(currentId, 49))
        currentId += 49
        
        // アニメ・ゲーム: 49問追加 (現在1問 → 50問)
        questions.addAll(generateAnimeGameQuestions(currentId, 49))
        currentId += 49
        
        // 食べ物: 49問追加 (現在1問 → 50問)
        questions.addAll(generateFoodQuestions(currentId, 49))
        
        return questions
    }
    
    private fun generateWorldQuestions(startId: Int, count: Int): List<JSONObject> {
        val templates = listOf(
            Triple("世界で最も人口が多い国は？", listOf("中国", "インド", "アメリカ", "インドネシア"), 0),
            Triple("世界で最も面積が大きい国は？", listOf("アメリカ", "中国", "ロシア", "カナダ"), 2),
            Triple("エッフェル塔がある国は？", listOf("ドイツ", "イタリア", "フランス", "スペイン"), 2),
            Triple("万里の長城がある国は？", listOf("日本", "韓国", "中国", "モンゴル"), 2),
            Triple("ビッグベンがある国は？", listOf("フランス", "ドイツ", "イギリス", "イタリア"), 2),
            Triple("コロッセオがある国は？", listOf("ギリシャ", "イタリア", "スペイン", "ポルトガル"), 1),
            Triple("ピラミッドがある国は？", listOf("イラン", "エジプト", "トルコ", "イラク"), 1),
            Triple("自由の女神像がある国は？", listOf("イギリス", "フランス", "アメリカ", "カナダ"), 2),
            Triple(" Opera Houseがある国は？", listOf("ニュージーランド", "オーストラリア", "カナダ", "南アフリカ"), 1),
            Triple("タージマハルがある国は？", listOf("パキスタン", "バングラデシュ", "インド", "スリランカ"), 2),
            Triple("マッチャーピッチュがある国は？", listOf("チリ", "ペルー", "ボリビア", "エクアドル"), 1),
            Triple("アンコールワットがある国は？", listOf("タイ", "ベトナム", "カンボジア", "ラオス"), 2),
            Triple("パルテノン神殿がある国は？", listOf("イタリア", "トルコ", "ギリシャ", "エジプト"), 2),
            Triple("サグラダ・ファミリアがある国は？", listOf("ポルトガル", "フランス", "スペイン", "イタリア"), 2),
            Triple("モン・サン・ミシェルがある国は？", listOf("イギリス", "ベルギー", "フランス", "ドイツ"), 2),
            Triple("タイムズスクエアがある国は？", listOf("ロンドン", "パリ", "ニューヨーク", "東京"), 2),
            Triple("ハリウッドがある国は？", listOf("ニューヨーク", "ロサンゼルス", "マイアミ", "シカゴ"), 1),
            Triple("シルクロードが通る大陸は？", listOf("ヨーロッパ", "アジア", "アフリカ", "南アメリカ"), 1),
            Triple("アマゾン川が流れる大陸は？", listOf("アフリカ", "アジア", "南アメリカ", "北アメリカ"), 2),
            Triple("サハラ砂漠がある大陸は？", listOf("アジア", "アフリカ", "オーストラリア", "南アメリカ"), 1),
            Triple("バンクーバーがある国は？", listOf("アメリカ", "カナダ", "オーストラリア", "ニュージーランド"), 1),
            Triple("シドニーがある国は？", listOf("アメリカ", "イギリス", "オーストラリア", "カナダ"), 2),
            Triple("ケープタウンがある国は？", listOf("エジプト", "ナイジェリア", "南アフリカ", "ケニア"), 2),
            Triple("バルセロナがある国は？", listOf("イタリア", "フランス", "スペイン", "ポルトガル"), 2),
            Triple("ミュンヘンがある国は？", listOf("フランス", "スイス", "ドイツ", "オーストリア"), 2),
            Triple("チューリッヒがある国は？", listOf("ドイツ", "フランス", "スイス", "イタリア"), 2),
            Triple("ウィーンがある国は？", listOf("ドイツ", "スイス", "オーストリア", "チェコ"), 2),
            Triple("プラハがある国は？", listOf("スロバキア", "ポーランド", "チェコ", "ハンガリー"), 2),
            Triple("ブダペストがある国は？", listOf("オーストリア", "チェコ", "ハンガリー", "ルーマニア"), 2),
            Triple("ワルシャワがある国は？", listOf("チェコ", "ウクライナ", "ポーランド", "リトアニア"), 2),
            Triple("ストックホルムがある国は？", listOf("ノルウェー", "フィンランド", "スウェーデン", "デンマーク"), 2),
            Triple("オスロがある国は？", listOf("スウェーデン", "フィンランド", "ノルウェー", "デンマーク"), 2),
            Triple("ヘルシンキがある国は？", listOf("スウェーデン", "ノルウェー", "フィンランド", "エストニア"), 2),
            Triple("コペンハーゲンがある国は？", listOf("ノルウェー", "スウェーデン", "デンマーク", "ドイツ"), 2),
            Triple("アムステルダムがある国は？", listOf("ベルギー", "ドイツ", "オランダ", "ルクセンブルク"), 2),
            Triple("ブリュッセルがある国は？", listOf("オランダ", "フランス", "ベルギー", "ドイツ"), 2),
            Triple("リスボンがある国は？", listOf("スペイン", "イタリア", "ポルトガル", "フランス"), 2),
            Triple("アテネがある国は？", listOf("イタリア", "トルコ", "ギリシャ", "エジプト"), 2),
            Triple("イスタンブールがある国は？", listOf("ギリシャ", "イラン", "トルコ", "シリア"), 2),
            Triple("ドバイがある国は？", listOf("サウジアラビア", "カタール", "アラブ首長国連邦", "オマーン"), 2),
            Triple("テルアビブがある国は？", listOf("ヨルダン", "レバノン", "イスラエル", "シリア"), 2),
            Triple("リオデジャネイロがある国は？", listOf("アルゼンチン", "ブラジル", "チリ", "ペルー"), 1),
            Triple("カイロがある国は？", listOf("リビア", "スーダン", "エジプト", "エチオピア"), 2),
            Triple("ナイロビがある国は？", listOf("タンザニア", "エチオピア", "ケニア", "ウガンダ"), 2),
            Triple("ラゴスがある国は？", listOf("ガーナ", "ナイジェリア", "カメルーン", "ベナン"), 1),
            Triple("ジャカルタがある国は？", listOf("マレーシア", "フィリピン", "インドネシア", "シンガポール"), 2),
            Triple("バンコクがある国は？", listOf("ベトナム", "カンボジア", "タイ", "ラオス"), 2),
            Triple("ソウルがある国は？", listOf("中国", "北朝鮮", "韓国", "日本"), 2),
            Triple("北京がある国は？", listOf("韓国", "日本", "中国", "ベトナム"), 2),
            Triple("台北がある国は？", listOf("中国", "台湾", "香港", "マカオ"), 1),
            Triple("シンガポールは何の国？", listOf("島国", "内陸国", "半島", "大陸"), 0),
            Triple("マレーシアの首都は？", listOf("ジャカルタ", "シンガポール", "クアラルンプール", "バンコク"), 2),
            Triple("フィリピンの首都は？", listOf("ジャカルタ", "ハノイ", "マニラ", "バンコク"), 2),
            Triple("ベトナムの首都は？", listOf("バンコク", "ハノイ", "ホーチミン", "ジャカルタ"), 1),
            Triple("インドの首都は？", listOf("ムンバイ", "デリー", "ニューデリー", "バンガロール"), 2),
            Triple("パキスタンの首都は？", listOf("カラチ", "ラホール", "イスラマバード", "ペシャワール"), 2),
            Triple("バングラデシュの首都は？", listOf("コルカタ", "ダッカ", "チッタゴン", "イスラマバード"), 1),
            Triple("ミャンマーの首都は？", listOf("ヤンゴン", "ネピドー", "バンコク", "ハノイ"), 1)
        )
        
        return templates.take(count).mapIndexed { index, (text, options, correct) ->
            createQuestion(startId + index, text, options, correct, "世界")
        }
    }
    
    private fun generateJapanQuestions(startId: Int, count: Int): List<JSONObject> {
        val templates = listOf(
            Triple("日本の最南端の島は？", listOf("沖縄本島", "石垣島", "波照間島", "与那国島"), 2),
            Triple("日本で一番高い山は？", listOf("北岳", "奥穂高岳", "富士山", "間ノ岳"), 2),
            Triple("日本の首都は？", listOf("大阪", "京都", "東京", "横浜"), 2),
            Triple("日本の国旗は何色？", listOf("赤と白", "赤と青", "白と黒", "赤と黒"), 0),
            Triple("日本の通貨は？", listOf("ドル", "ユーロ", "円", "ウォン"), 2),
            Triple("日本で一番大きい湖は？", listOf("霞ヶ浦", "琵琶湖", "中禅寺湖", "猪苗代湖"), 1),
            Triple("日本の国花は？", listOf("梅", "桜", "菊", "藤"), 2),
            Triple("日本の国鳥は？", listOf("鶴", "鷹", "雉", "烏"), 2),
            Triple("日本は何つの島からなる？", listOf("3つ", "4つ", "5つ", "6つ以上"), 3),
            Triple("日本で一番長い川は？", listOf("利根川", "石狩川", "信濃川", "北上川"), 2),
            Triple("北海道の首府は？", listOf("札幌", "函館", "小樽", "旭川"), 0),
            Triple("沖縄県の首府は？", listOf("那覇", "石垣", "宮古", "浦添"), 0),
            Triple("京都府の首府は？", listOf("大阪", "奈良", "京都", "滋賀"), 2),
            Triple("大阪府の首府は？", listOf("神戸", "大阪", "京都", "奈良"), 1),
            Triple("福岡県の首府は？", listOf("長崎", "熊本", "福岡", "鹿児島"), 2),
            Triple("広島県の首府は？", listOf("岡山", "山口", "広島", "鳥取"), 2),
            Triple("愛知県の首府は？", listOf("岐阜", "静岡", "名古屋", "三重"), 2),
            Triple("静岡県の首府は？", listOf("浜松", "静岡", "富士", "沼津"), 1),
            Triple("神奈川県の首府は？", listOf("横浜", "川崎", "相模原", "小田原"), 0),
            Triple("千葉県の首府は？", listOf("東京", "横浜", "千葉", "さいたま"), 2),
            Triple("埼玉県の首府は？", listOf("東京", "千葉", "さいたま", "横浜"), 2),
            Triple("茨城県の首府は？", listOf("宇都宮", "前橋", "水戸", "東京"), 2),
            Triple("栃木県の首府は？", listOf("宇都宮", "前橋", "水戸", "東京"), 0),
            Triple("群馬県の首府は？", listOf("宇都宮", "前橋", "水戸", "東京"), 1),
            Triple("新潟県の首府は？", listOf("富山", "石川", "新潟", "福井"), 2),
            Triple("長野県の首府は？", listOf("松本", "長野", "上田", "諏訪"), 1),
            Triple("山梨県の首府は？", listOf("甲府", "富士吉田", "韮崎", "南アルプス"), 0),
            Triple("岐阜県の首府は？", listOf("静岡", "名古屋", "岐阜", "三重"), 2),
            Triple("滋賀県の首府は？", listOf("京都", "奈良", "大津", "大阪"), 2),
            Triple("奈良県の首府は？", listOf("京都", "奈良", "大阪", "和歌山"), 1),
            Triple("和歌山県の首府は？", listOf("大阪", "奈良", "和歌山", "三重"), 2),
            Triple("三重県の首府は？", listOf("津", "四日市", "伊勢", "松阪"), 0),
            Triple("兵庫県の首府は？", listOf("大阪", "京都", "神戸", "奈良"), 2),
            Triple("鳥取県の首府は？", listOf("岡山", "鳥取", "島根", "山口"), 1),
            Triple("島根県の首府は？", listOf("岡山", "鳥取", "松江", "山口"), 2),
            Triple("岡山県の首府は？", listOf("岡山", "広島", "山口", "鳥取"), 0),
            Triple("山口県の首府は？", listOf("広島", "岡山", "山口", "福岡"), 2),
            Triple("香川県の首府は？", listOf("高松", "松山", "高知", "徳島"), 0),
            Triple("愛媛県の首府は？", listOf("高松", "松山", "高知", "徳島"), 1),
            Triple("高知県の首府は？", listOf("高松", "松山", "高知", "徳島"), 2),
            Triple("徳島県の首府は？", listOf("高松", "松山", "高知", "徳島"), 3),
            Triple("福岡県の首府は？", listOf("長崎", "熊本", "福岡", "大分"), 2),
            Triple("佐賀県の首府は？", listOf("長崎", "佐賀", "福岡", "熊本"), 1),
            Triple("長崎県の首府は？", listOf("佐賀", "長崎", "福岡", "熊本"), 1),
            Triple("熊本県の首府は？", listOf("長崎", "福岡", "熊本", "大分"), 2),
            Triple("大分県の首府は？", listOf("福岡", "熊本", "大分", "宮崎"), 2),
            Triple("宮崎県の首府は？", listOf("大分", "宮崎", "鹿児島", "熊本"), 1),
            Triple("鹿児島県の首府は？", listOf("宮崎", "熊本", "鹿児島", "沖縄"), 2),
            Triple("青森県の首府は？", listOf("秋田", "青森", "岩手", "山形"), 1),
            Triple("秋田県の首府は？", listOf("青森", "秋田", "岩手", "山形"), 1),
            Triple("岩手県の首府は？", listOf("青森", "秋田", "盛岡", "山形"), 2),
            Triple("山形県の首府は？", listOf("青森", "秋田", "山形", "福島"), 2),
            Triple("福島県の首府は？", listOf("山形", "宮城", "福島", "新潟"), 2),
            Triple("宮城県の首府は？", listOf("山形", "仙台", "福島", "秋田"), 1)
        )
        
        return templates.take(count).mapIndexed { index, (text, options, correct) ->
            createQuestion(startId + index, text, options, correct, "日本")
        }
    }
    
    private fun generateITQuestions(startId: Int, count: Int): List<JSONObject> {
        val templates = listOf(
            Triple("「URL」のUは何の略？", listOf("Universal", "Uniform", "Unique", "United"), 1),
            Triple("「PC」の正式名称は？", listOf("Private Computer", "Personal Computer", "Public Computer", "Professional Computer"), 1),
            Triple("コンピュータの基本単位「bit」は何の略？", listOf("binary digit", "byte information", "bit technology", "basic digit"), 0),
            Triple("スマートフォンのOSでないのは？", listOf("iOS", "Android", "Windows", "Linux"), 2),
            Triple("「AI」は何の略？", listOf("Automatic Intelligence", "Artificial Intelligence", "Advanced Internet", "Application Interface"), 1),
            Triple("「GUI」は何の略？", listOf("General User Interface", "Graphical User Interface", "Global User Interface", "Generic User Interface"), 1),
            Triple("「CPU」はコンピュータの何を指す？", listOf("記憶装置", "入力装置", "中央処理装置", "出力装置"), 2),
            Triple("「RAM」は何の略？", listOf("Random Access Memory", "Read Access Memory", "Random Application Memory", "Read Application Memory"), 0),
            Triple("「ROM」は何の略？", listOf("Random Only Memory", "Read Only Memory", "Random Output Memory", "Read Output Memory"), 1),
            Triple("「SSD」と「HDD」の違いは？", listOf("容量", "回転速度", "記録方式", "色"), 2),
            Triple("「Cloud」の意味は？", listOf("空の雲", "インターネット経由のサービス", "ゲームキャラクター", "音楽ファイル形式"), 1),
            Triple("「Bluetooth」は何技術？", listOf("有線通信", "無線通信", "光通信", "音波通信"), 1),
            Triple("「Wi-Fi」の通信距離は概ね？", listOf("10cm", "1m", "10-100m", "10km"), 2),
            Triple("「4K」とは何の解像度？", listOf("1920x1080", "2560x1440", "3840x2160", "7680x4320"), 2),
            Triple("「VPN」は何の略？", listOf("Virtual Private Network", "Virtual Public Network", "Very Private Network", "Video Phone Network"), 0),
            Triple("「LAN」は何の略？", listOf("Large Area Network", "Local Area Network", "Long Area Network", "Light Area Network"), 1),
            Triple("「WAN」は何の略？", listOf("Wide Area Network", "Wireless Area Network", "Web Area Network", "World Area Network"), 0),
            Triple("「HTTP」の正式名称は？", listOf("HyperText Transfer Protocol", "HyperText Transmission Protocol", "HighText Transfer Protocol", "HomeText Transfer Protocol"), 0),
            Triple("「HTTPS」と「HTTP」の違いは？", listOf("速度", "セキュリティ", "色", "音質"), 1),
            Triple("「IPアドレス」の用途は？", listOf("名前解決", "住所特定", "ネットワーク上の機器特定", "ファイル圧縮"), 2),
            Triple("「DNS」は何の略？", listOf("Domain Name System", "Digital Network System", "Data Name Service", "Domain Network Service"), 0),
            Triple("「Cookie」は何に使う？", listOf("ウイルス対策", "情報記憶", "画面表示", "音楽再生"), 1),
            Triple("「Cache」は何に使う？", listOf("データ一時保存", "データ永久保存", "データ削除", "データ暗号化"), 0),
            Triple("「Spam」は何を指す？", listOf("高級ハム", "不要なメール", "コンピュータウイルス", "ゲームアイテム"), 1),
            Triple("「Virus」はコンピュータで何を指す？", listOf("高級ソフト", "悪意のあるプログラム", "高速CPU", "大容量メモリ"), 1),
            Triple("「Firewall」は何に使う？", listOf("壁の防火", "不正アクセス防止", "画面明るさ調整", "音量大調整"), 1),
            Triple("「Phishing」は何を指す？", listOf("釣り", "なりすまし詐欺", "ウイルス", "スパム"), 1),
            Triple("「Malware」は何の略？", listOf("Malicious Software", "Management Software", "Market Software", "Mail Software"), 0),
            Triple("「Ransomware」は何を指す？", listOf("身代金要求型ウイルス", "ゲーム", "検索エンジン", "SNS"), 0),
            Triple("「Trojan」は何を指す？", listOf("ギリシャ神話", "コンピュータウイルス", "ゲームキャラ", "OS"), 1),
            Triple("「Worm」はコンピュータで何を指す？", listOf("ミミズ", "自己増殖型マルウェア", "ゲーム", "ブラウザ"), 1),
            Triple("「Bug」はコンピュータで何を指す？", listOf("虫", "プログラムの欠陥", "ゲーム", "OS"), 1),
            Triple("「Debug」は何を意味する？", listOf("虫を捕る", "欠陥を取り除く", "ゲームをする", "音楽を聴く"), 1),
            Triple("「Open Source」は何を意味する？", listOf("有料ソフト", "ソースコード公開", "クラウドサービス", "ゲーム"), 1),
            Triple("「Freeware」は何を意味する？", listOf("有料ソフト", "無料ソフト", "ソースコード公開", "クラウド"), 1),
            Triple("「Shareware」は何を意味する？", listOf("完全無料", "試用期間あり", "ソース公開", "クラウド"), 1),
            Triple("「Firmware」は何を指す？", listOf("アプリ", "ハードウェア制御ソフト", "OS", "ゲーム"), 1),
            Triple("「Driver」はコンピュータで何を指す？", listOf("運転手", "ハードウェア制御プログラム", "ゲーム", "ブラウザ"), 1),
            Triple("「Plugin」は何を指す？", listOf("電源プラグ", "追加機能プログラム", "ゲーム", "OS"), 1),
            Triple("「Add-on」は何を指す？", listOf("追加機能", "削除機能", "基本機能", "OS"), 0),
            Triple("「Update」は何を意味する？", listOf("古くなる", "新しくする", "消す", "壊す"), 1),
            Triple("「Upgrade」は何を意味する？", listOf("古くなる", "上位版にする", "消す", "壊す"), 1),
            Triple("「Download」は何を意味する？", listOf("アップロード", "ダウンロード", "削除", "印刷"), 1),
            Triple("「Upload」は何を意味する？", listOf("ダウンロード", "アップロード", "削除", "印刷"), 1),
            Triple("「Backup」は何を意味する？", listOf("削除", "複製保存", "印刷", "送信"), 1),
            Triple("「Restore」は何を意味する？", listOf("削除", "復元", "印刷", "送信"), 1),
            Triple("「Compress」は何を意味する？", listOf("拡大", "圧縮", "印刷", "送信"), 1),
            Triple("「Extract」は何を意味する？", listOf("圧縮", "展開", "印刷", "送信"), 1),
            Triple("「Encrypt」は何を意味する？", listOf("復号", "暗号化", "印刷", "送信"), 1),
            Triple("「Decrypt」は何を意味する？", listOf("暗号化", "復号", "印刷", "送信"), 1),
            Triple("「Resolution」は何を指す？", listOf("解像度", "音量", "色", "速度"), 0),
            Triple("「Pixel」は何を指す？", listOf("画像の最小単位", "音量", "色", "速度"), 0),
            Triple("「Byte」は何ビット？", listOf("4", "8", "16", "32"), 1),
            Triple("「KB」は何バイト？", listOf("100", "1000", "1024", "10000"), 2),
            Triple("「MB」は概ね何KB？", listOf("10", "100", "1000", "10000"), 2),
            Triple("「GB」は概ね何MB？", listOf("10", "100", "1000", "10000"), 2),
            Triple("「TB」は概ね何GB？", listOf("10", "100", "1000", "10000"), 2)
        )
        
        return templates.take(count).mapIndexed { index, (text, options, correct) ->
            createQuestion(startId + index, text, options, correct, "IT・テクノロジー")
        }
    }
    
    private fun generateHistoryQuestions(startId: Int, count: Int): List<JSONObject> {
        val templates = listOf(
            Triple("明治時代は何年から？", listOf("1853年", "1868年", "1900年", "1912年"), 1),
            Triple("大正時代は何年から？", listOf("1868年", "1912年", "1926年", "1945年"), 1),
            Triple("昭和時代は何年から？", listOf("1912年", "1926年", "1945年", "1989年"), 1),
            Triple("平成時代は何年から？", listOf("1926年", "1989年", "1990年", "2000年"), 1),
            Triple("令和時代は何年から？", listOf("1989年", "2000年", "2019年", "2020年"), 2),
            Triple("坂本龍馬は何時代の人物？", listOf("江戸時代", "明治時代", "大正時代", "昭和時代"), 0),
            Triple("織田信長は何時代の武将？", listOf("室町時代", "安土桃山時代", "江戸時代", "明治時代"), 1),
            Triple("豊臣秀吉は何時代の武将？", listOf("室町時代", "安土桃山時代", "江戸時代", "明治時代"), 1),
            Triple("徳川家康は何時代の武将？", listOf("安土桃山時代", "江戸時代", "明治時代", "大正時代"), 1),
            Triple("源頼朝は何を開いた？", listOf("室町幕府", "鎌倉幕府", "江戸幕府", "明治政府"), 1),
            Triple("足利尊氏は何を開いた？", listOf("鎌倉幕府", "室町幕府", "江戸幕府", "明治政府"), 1),
            Triple("徳川家康は何を開いた？", listOf("室町幕府", "江戸幕府", "鎌倉幕府", "明治政府"), 1),
            Triple("明治維新は何年？", listOf("1853年", "1868年", "1900年", "1945年"), 1),
            Triple("黒船来航は何年？", listOf("1853年", "1868年", "1900年", "1945年"), 0),
            Triple("日清戦争は何年？", listOf("1894年", "1904年", "1914年", "1937年"), 0),
            Triple("日露戦争は何年？", listOf("1894年", "1904年", "1914年", "1937年"), 1),
            Triple("第一次世界大戦は何年から？", listOf("1904年", "1914年", "1939年", "1945年"), 1),
            Triple("第二次世界大戦は何年から？", listOf("1914年", "1937年", "1939年", "1941年"), 2),
            Triple("太平洋戦争開戦は何年？", listOf("1937年", "1939年", "1941年", "1945年"), 2),
            Triple("日本が降伏したのは何年？", listOf("1943年", "1944年", "1945年", "1946年"), 2),
            Triple("日本国憲法が施行されたのは何年？", listOf("1945年", "1947年", "1950年", "1960年"), 1),
            Triple("東京オリンピック（初）は何年？", listOf("1960年", "1964年", "1968年", "1972年"), 1),
            Triple("大阪万博は何年？", listOf("1964年", "1970年", "1975年", "1980年"), 1),
            Triple("東京オリンピック（2回目）は何年？", listOf("2016年", "2020年", "2021年", "2024年"), 2),
            Triple("ナポレオンは何国の皇帝？", listOf("イギリス", "ドイツ", "フランス", "イタリア"), 2),
            Triple("アレクサンドロス大王は何国の王？", listOf("ローマ", "ギリシャ", "マケドニア", "エジプト"), 2),
            Triple("ユリウス・カエサルは何国の指導者？", listOf("ギリシャ", "ローマ", "エジプト", "カルタゴ"), 1),
            Triple("クリストファー・コロンブスは何を発見？", listOf("インド", "アメリカ大陸", "オーストラリア", "南極"), 1),
            Triple("マルコ・ポーロは何国の人？", listOf("イギリス", "フランス", "イタリア", "スペイン"), 2),
            Triple("レオナルド・ダ・ヴィンチは何国の人？", listOf("イギリス", "フランス", "イタリア", "ドイツ"), 2),
            Triple("モーツァルトは何国の作曲家？", listOf("ドイツ", "オーストリア", "イタリア", "フランス"), 1),
            Triple("ベートーヴェンは何国の作曲家？", listOf("オーストリア", "ドイツ", "イタリア", "フランス"), 1),
            Triple("バッハは何国の作曲家？", listOf("オーストリア", "ドイツ", "イタリア", "フランス"), 1),
            Triple("シェイクスピアは何国の劇作家？", listOf("イギリス", "フランス", "イタリア", "ドイツ"), 0),
            Triple("ガリレオ・ガリレイは何を研究？", listOf("生物", "天文学", "化学", "医学"), 1),
            Triple("ニュートンは何の法則を発見？", listOf("相対性理論", "万有引力", "進化論", "元素周期律"), 1),
            Triple("ダーウィンは何を提唱？", listOf("万有引力", "進化論", "細胞学説", "元素周期律"), 1),
            Triple("アインシュタインは何を提唱？", listOf("万有引力", "相対性理論", "進化論", "細胞学説"), 1),
            Triple("ピラミッドを作った文明は？", listOf("メソポタミア", "エジプト", "インド", "中国"), 1),
            Triple("万里の長城を作った国は？", listOf("日本", "韓国", "中国", "モンゴル"), 2),
            Triple("ローマ帝国の首都は？", listOf("アテネ", "ローマ", "コンスタンティノープル", "カルタゴ"), 1),
            Triple("インカ帝国は何大陸にあった？", listOf("アフリカ", "ヨーロッパ", "アジア", "南アメリカ"), 3),
            Triple("アステカ文明は何国にあった？", listOf("ペルー", "メキシコ", "ブラジル", "アルゼンチン"), 1),
            Triple("マヤ文明は何に精通？", listOf("天文学", "医学", "化学", "物理学"), 0),
            Triple("ルネサンスは何で始まった？", listOf("フランス", "イギリス", "イタリア", "ドイツ"), 2),
            Triple("産業革命は何で始まった？", listOf("アメリカ", "フランス", "イギリス", "ドイツ"), 2),
            Triple("フランス革命は何年？", listOf("1776年", "1789年", "1804年", "1848年"), 1),
            Triple("アメリカ独立宣言は何年？", listOf("1776年", "1789年", "1804年", "1861年"), 0),
            Triple("南北戦争は何国の戦争？", listOf("イギリス", "フランス", "アメリカ", "ドイツ"), 2),
            Triple("第一次世界大戦の発端は？", listOf("フランス", "ドイツ", "サラエボ", "ポーランド"), 2)
        )
        
        return templates.take(count).mapIndexed { index, (text, options, correct) ->
            createQuestion(startId + index, text, options, correct, "歴史")
        }
    }
    
    private fun generateAnimeGameQuestions(startId: Int, count: Int): List<JSONObject> {
        val templates = listOf(
            Triple("ポケモンの主人公のライバルは？", listOf("グリーン", "レッド", "ブルー", "イエロー"), 0),
            Triple("ドラゴンボールの主人公は？", listOf("ベジータ", "孫悟空", "ピッコロ", "悟飯"), 1),
            Triple("ワンピースの主人公は？", listOf("ゾロ", "サンジ", "ルフィ", "ナミ"), 2),
            Triple("ナルトの主人公は？", listOf("サスケ", "ナルト", "サクラ", "カカシ"), 1),
            Triple("鬼滅の刃の主人公は？", listOf("善逸", "伊之助", "炭治郎", "義勇"), 2),
            Triple("進撃の巨人の主人公は？", listOf("アルミン", "ミカサ", "エレン", "リヴァイ"), 2),
            Triple("僕のヒーローアカデミアの主人公は？", listOf("爆豪", "緑谷", "飯田", "麗日"), 1),
            Triple("呪術廻戦の主人公は？", listOf("伏黒", "釘崎", "虎杖", "五条"), 2),
            Triple("スパイファミリーの主人公は？", listOf("ロイド", "ヨル", "アーニャ", "ボンド"), 0),
            Triple("チェンソーマンの主人公は？", listOf("アキ", "パワー", "デンジ", "マキマ"), 2),
            Triple("ブルーロックの題材は？", listOf("バスケ", "サッカー", "野球", "バレー"), 1),
            Triple("名探偵コナンの主人公は？", listOf("工藤新一", "毛利蘭", "灰原哀", "服部平次"), 0),
            Triple("名探偵コナンの組織は？", listOf("FBI", "CIA", "黒の組織", "公安"), 2),
            Triple("キン肉マンの主人公は？", listOf("テリーマン", "キン肉マン", "ロビンマスク", "ウォーズマン"), 1),
            Triple("北斗の拳の主人公は？", listOf("レイ", "トキ", "ケンシロウ", "ラオウ"), 2),
            Triple("ジョジョの奇妙な冒険の第1部主人公は？", listOf("ジョセフ", "承太郎", "ジョナサン", "仗助"), 2),
            Triple("ジョジョの奇妙な冒険の第3部主人公は？", listOf("ジョナサン", "ジョセフ", "承太郎", "仗助"), 2),
            Triple("SLAM DUNKの主人公は？", listOf("流川", "桜木", "三井", "宮城"), 1),
            Triple(" SLAM DUNKの学校は？", listOf("海南", "翔陽", "陵南", "湘北"), 3),
            Triple("ハイキューの題材は？", listOf("バスケ", "サッカー", "野球", "バレー"), 3),
            Triple("テニスの王子様の題材は？", listOf("硬式テニス", "軟式テニス", "卓球", "バドミントン"), 0),
            Triple("遊☆戯☆王の主人公は？", listOf("海馬", "遊戯", "城之内", "マリク"), 1),
            Triple("デジモンアドベンチャーの主人公は？", listOf("太一", "ヤマト", "光子郎", "丈"), 0),
            Triple("デジモンのパートナーは？", listOf("ピカチュウ", "アグモン", "コロモン", "ガブモン"), 1),
            Triple("ポケモンの主人公の初期ポケモンは？", listOf("ピカチュウ", "ヒトカゲ", "ゼニガメ", "フシギダネ"), 0),
            Triple("マリオの弟は？", listOf("ワリオ", "ルイージ", "ワルイージ", "キノピオ"), 1),
            Triple("ゼルダの伝説の主人公は？", listOf("ゼルダ", "リンク", "ガノン", "インプ"), 1),
            Triple("ファイナルファンタジーの開発元は？", listOf("任天堂", "カプコン", "スクエニ", "コナミ"), 2),
            Triple("ドラゴンクエストの開発元は？", listOf("任天堂", "カプコン", "スクエニ", "コナミ"), 2),
            Triple("モンスターハンターの開発元は？", listOf("任天堂", "カプコン", "スクエニ", "コナミ"), 1),
            Triple("ポケモンの開発元は？", listOf("ゲームフリーク", "カプコン", "スクエニ", "コナミ"), 0),
            Triple("マリオの開発元は？", listOf("任天堂", "カプコン", "スクエニ", "コナミ"), 0),
            Triple("ゼルダの伝説の開発元は？", listOf("任天堂", "カプコン", "スクエニ", "コナミ"), 0),
            Triple("スプラトゥーンの開発元は？", listOf("任天堂", "カプコン", "スクエニ", "コナミ"), 0),
            Triple("大乱闘スマッシュブラザーズの開発元は？", listOf("任天堂", "カプコン", "スクエニ", "コナミ"), 0),
            Triple("アニメ「鬼滅の刃」の刀を使う呼吸は？", listOf("水の呼吸", "火の呼吸", "風の呼吸", "土の呼吸"), 0),
            Triple("「名探偵コナン」でコナンになる前の名前は？", listOf("工藤新一", "江戸川コナン", "毛利蘭", "灰原哀"), 0),
            Triple("「ワンピース」の海賊王の名前は？", listOf("ロジャー", "白ひげ", "シャンクス", "黒ひげ"), 0),
            Triple("「ナルト」で木ノ葉隠れの里の影は？", listOf("火影", "風影", "水影", "土影"), 0),
            Triple("「ドラゴンボール」で神龍を呼び出すには？", listOf("龍珠を7つ集める", "龍珠を3つ集める", "龍珠を5つ集める", "龍珠を10つ集める"), 0),
            Triple("「進撃の巨人」で巨人を倒すには？", listOf("首の後ろを切る", "心臓を刺す", "頭を叩く", "足を切る"), 0),
            Triple("「僕のヒーローアカデミア」でNo.1ヒーローは？", listOf("オールマイト", "エンデヴァー", "ホークス", "ベストジー"), 0),
            Triple("「呪術廻戦」で最強の呪術師は？", listOf("五条悟", "夏油傑", "七海建人", "伏黒甚爾"), 0),
            Triple("「スパイファミリー」でアーニャの能力は？", listOf("読心術", "未来予知", "瞬間移動", "火を吹く"), 0),
            Triple("「チェンソーマン」でデンジの変身は？", listOf("チェンソーマン", "ガンマン", "ソードマン", "フレイムマン"), 0),
            Triple("「ブルーロック」で主人公の能力は？", listOf("利己的なサッカー", "チームプレイ", "守備", "GK"), 0),
            Triple("ハイキューの烏野高校のエースは？", listOf("東峰", "日向", "影山", "菅原"), 0),
            Triple("「テニスの王子様」で越前リョーマの父は？", listOf("越前南次郎", "越前リョーマ", "手塚国光", "不二周助"), 0),
            Triple("「遊☆戯☆王」でデュエルモンスターのカード枚数は？", listOf("30枚", "40枚", "50枚", "60枚"), 1)
        )
        
        return templates.take(count).mapIndexed { index, (text, options, correct) ->
            createQuestion(startId + index, text, options, correct, "アニメ・ゲーム")
        }
    }
    
    private fun generateFoodQuestions(startId: Int, count: Int): List<JSONObject> {
        val templates = listOf(
            Triple("寿司の主な材料は？", listOf("米", "小麦", "大豆", "とうもろこし"), 0),
            Triple("ラーメンの主な材料は？", listOf("米", "小麦粉", "大豆", "とうもろこし"), 1),
            Triple("うどんの主な材料は？", listOf("米", "小麦粉", "そば粉", "とうもろこし"), 1),
            Triple("そばの主な材料は？", listOf("米", "小麦粉", "そば粉", "とうもろこし"), 2),
            Triple("パスタの主な材料は？", listOf("米", "小麦粉", "そば粉", "とうもろこし"), 1),
            Triple("パンの主な材料は？", listOf("米", "小麦粉", "そば粉", "とうもろこし"), 1),
            Triple("おにぎりの主な材料は？", listOf("米", "小麦粉", "そば粉", "とうもろこし"), 0),
            Triple("お寿司の「シャリ」は何？", listOf("魚", "米", "海苔", "醤油"), 1),
            Triple("お寿司の「ネタ」は何？", listOf("米", "魚", "海苔", "醤油"), 1),
            Triple("味噌汁の主な材料は？", listOf("醤油", "味噌", "塩", "砂糖"), 1),
            Triple("醤油の主原料は？", listOf("小麦", "大豆", "米", "とうもろこし"), 1),
            Triple("味噌の主原料は？", listOf("小麦", "大豆", "米", "とうもろこし"), 1),
            Triple("豆腐の主原料は？", listOf("小麦", "大豆", "米", "とうもろこし"), 1),
            Triple("納豆の主原料は？", listOf("小麦", "大豆", "米", "とうもろこし"), 1),
            Triple("天ぷらの衣の主材料は？", listOf("米", "小麦粉", "そば粉", "とうもろこし"), 1),
            Triple("カレーの主な香辛料は？", listOf("ターメリック", "カレー粉", "クミン", "コリアンダー"), 1),
            Triple("わさびは何から作る？", listOf("山葵", "辛子", "胡椒", "唐辛子"), 0),
            Triple("からしは何から作る？", listOf("山葵", "辛子", "胡椒", "唐辛子"), 1),
            Triple("唐辛子は何から作る？", listOf("山葵", "辛子", "胡椒", "唐辛子"), 3),
            Triple("胡椒は何から作る？", listOf("山葵", "辛子", "コショウの木", "唐辛子"), 2),
            Triple("塩の主成分は？", listOf("炭化水素", "塩化ナトリウム", "酸素", "水素"), 1),
            Triple("砂糖の主成分は？", listOf("グルコース", "果糖", "ショ糖", "乳糖"), 2),
            Triple("はちみつは何から作る？", listOf("蜂", "花", "木", "草"), 0),
            Triple("チーズは何から作る？", listOf("牛乳", "豆乳", "ココナッツ", "アーモンド"), 0),
            Triple("バターは何から作る？", listOf("牛乳", "豆乳", "ココナッツ", "アーモンド"), 0),
            Triple("ヨーグルトは何から作る？", listOf("牛乳", "豆乳", "ココナッツ", "アーモンド"), 0),
            Triple("アイスクリームは何が主原料？", listOf("牛乳", "豆乳", "ココナッツ", "アーモンド"), 0),
            Triple("チョコレートの主原料は？", listOf("カカオ", "砂糖", "牛乳", "バニラ"), 0),
            Triple("コーヒーの主原料は？", listOf("茶葉", "コーヒー豆", "カカオ", "小麦"), 1),
            Triple("紅茶の主原料は？", listOf("茶葉", "コーヒー豆", "カカオ", "小麦"), 0),
            Triple("緑茶の主原料は？", listOf("茶葉", "コーヒー豆", "カカオ", "小麦"), 0),
            Triple("日本酒の主原料は？", listOf("米", "小麦", "ブドウ", "リンゴ"), 0),
            Triple("ビールの主原料は？", listOf("米", "大麦", "ブドウ", "リンゴ"), 1),
            Triple("ワインの主原料は？", listOf("米", "大麦", "ブドウ", "リンゴ"), 2),
            Triple("りんごジュースの主原料は？", listOf("米", "大麦", "ブドウ", "リンゴ"), 3),
            Triple("オレンジジュースの主原料は？", listOf("オレンジ", "リンゴ", "ブドウ", "レモン"), 0),
            Triple("レモンの味は？", listOf("甘い", "酸っぱい", "苦い", "辛い"), 1),
            Triple("砂糖の味は？", listOf("甘い", "酸っぱい", "苦い", "辛い"), 0),
            Triple("塩の味は？", listOf("甘い", "酸っぱい", "苦い", "辛い"), 3),
            Triple("胡椒の味は？", listOf("甘い", "酸っぱい", "苦い", "辛い"), 3),
            Triple("わさびの味は？", listOf("甘い", "酸っぱい", "苦い", "辛い"), 3),
            Triple("カレーは何の料理？", listOf("日本", "インド", "中国", "フランス"), 1),
            Triple("パスタは何の料理？", listOf("日本", "イタリア", "中国", "フランス"), 1),
            Triple("ピザは何の料理？", listOf("日本", "イタリア", "アメリカ", "フランス"), 1),
            Triple("ハンバーガーは何の料理？", listOf("日本", "ドイツ", "アメリカ", "イギリス"), 2),
            Triple("寿司は何の料理？", listOf("日本", "韓国", "中国", "アメリカ"), 0),
            Triple("ラーメンは何の料理？", listOf("日本", "中国", "韓国", "アメリカ"), 1),
            Triple("餃子は何の料理？", listOf("日本", "中国", "韓国", "アメリカ"), 1),
            Triple("天ぷらは何の料理？", listOf("日本", "ポルトガル", "中国", "アメリカ"), 0),
            Triple("カツ丼の「カツ」は何？", listOf("牛肉", "豚肉", "鶏肉", "魚"), 1),
            Triple("親子丼の「親」は何？", listOf("牛肉", "豚肉", "鶏肉", "魚"), 2),
            Triple("親子丼の「子」は何？", listOf("牛肉", "豚肉", "鶏卵", "魚卵"), 2)
        )
        
        return templates.take(count).mapIndexed { index, (text, options, correct) ->
            createQuestion(startId + index, text, options, correct, "食べ物")
        }
    }
    
    private fun createQuestion(id: Int, text: String, options: List<String>, correctIndex: Int, genre: String): JSONObject {
        return JSONObject().apply {
            put("id", id)
            put("text", text)
            put("options", JSONArray(options))
            put("correctIndex", correctIndex)
            put("genre", genre)
        }
    }
    
    fun addQuestionsToJson(context: Context) {
        val assetManager = context.assets
        val inputStream = assetManager.open("quiz_questions.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        inputStream.close()
        
        val jsonObject = JSONObject(jsonString)
        val questionsArray = jsonObject.getJSONArray("questions")
        
        // 生成した問題を追加
        val newQuestions = generateQuestions()
        newQuestions.forEach { question ->
            questionsArray.put(question)
        }
        
        // ファイルに書き出し（開発用）
        val outputFile = File(context.filesDir, "quiz_questions_new.json")
        outputFile.writeText(jsonObject.toString(2))
        
        android.util.Log.d("QuestionGenerator", "Generated ${newQuestions.size} questions")
        android.util.Log.d("QuestionGenerator", "Total questions: ${questionsArray.length()}")
        android.util.Log.d("QuestionGenerator", "Output file: ${outputFile.absolutePath}")
    }
}
