package kadai_007;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Posts_Chapter07 {
    public static void main(String[] args) {

        Connection con = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement selectStatement = null;

        // ユーザーリスト
        String[][] userList = {
            { "1","1003","2023-02-08","昨日の夜は徹夜でした・・","13" },
            { "2","1002","2023-02-08","お疲れ様です！","12" },
            { "3","1003","2023-02-09","今日も頑張ります！","18" },
            { "4","1001","2023-02-09","無理は禁物ですよ！","17" },
            { "5","1002","2023-02-10","明日から連休ですね！","20" }
        };

        try {
            // データベースに接続
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost/challenge_java",
                "root",
                "KANAMErun030328"
            );

            System.out.println("データベース接続成功");

            // INSERTのためのSQLクエリを準備
            String sql = "INSERT INTO posts (post_id, user_id, posted_at, post_content, likes) VALUES (?, ?, ?, ?, ?);";
            preparedStatement = con.prepareStatement(sql);

            // リストの1行目から順番に読み込む
            int rowCnt = 0;
            for( int i = 0; i < userList.length; i++ ) {
                // SQLクエリの「?」部分をリストのデータに置き換え
                preparedStatement.setString(1, userList[i][0]); // post_id
                preparedStatement.setString(2, userList[i][1]); // user_id
                preparedStatement.setString(3, userList[i][2]); // posted_at
                preparedStatement.setString(4, userList[i][3]); // post_content
                preparedStatement.setString(5, userList[i][4]); // likes

                // SQLクエリを実行（DBMSに送信）
                System.out.println("レコード追加:" + preparedStatement.toString() );
                rowCnt = preparedStatement.executeUpdate();
                System.out.println( rowCnt + "件のレコードが追加されました");
            }

            // SELECTのためのSQLクエリを準備
            String sql1 = "SELECT * FROM posts WHERE user_id = ?";
            selectStatement = con.prepareStatement(sql1);
            selectStatement.setInt(1, 1002);
            ResultSet result = selectStatement.executeQuery();
            
            while(result.next()) {
                int id = result.getInt("user_id");
                String at = result.getString("posted_at");
                String post = result.getString("post_content");
                int likes = result.getInt("likes");
                System.out.println(result.getRow() + "件目：投稿日時=" + at
                                       + "／投稿内容=" + post + "／いいね数=" + likes );
            }
            
        } catch(SQLException e) {
            System.out.println("エラー発生：" + e.getMessage());
        } finally {
            // 使用したオブジェクトを解放
            if( preparedStatement != null ) {
                try { preparedStatement.close(); } catch(SQLException ignore) {}
            }
            if( selectStatement != null ) {
                try { selectStatement.close(); } catch(SQLException ignore) {}
            }
            if( con != null ) {
                try { con.close(); } catch(SQLException ignore) {}
            }
        }
    }
}