import java.sql.*;

public class Demo {

    public static void main(String[] args) {
        try {
            // Connect
            Connection conn;
            conn = DriverManager.getConnection("jdbc:sqlite:C:\\demoDB\\database.db");

            // Create tables
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS memes (id integer PRIMARY KEY UNIQUE, link text UNIQUE NOT NULL, submitter text NOT NULL, curator text NOT NULL)");
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS tag_lkp (id integer, tag text NOT NULL)");

            // Insert data
            for(int i=1;i<50;i++){
                PreparedStatement ps = conn.prepareStatement("INSERT INTO memes (id, link, submitter, curator) VALUES (?,?,?,?)");
                ps.setInt(1, i);
                ps.setString(2, "a".repeat(i));
                ps.setString(3, "Bendu");
                ps.setString(4, "Ziggy");
                ps.executeUpdate();
            }

            // Querying
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM memes");
            while(rs != null && rs.next()) {
                System.out.println(rs.getInt("id") + ", " + rs.getString("link") + ", " + rs.getString("submitter") + ", " + rs.getString("curator"));
            }
            rs = conn.createStatement().executeQuery("SELECT MAX(id) m FROM memes");
            while(rs != null && rs.next()) {
                System.out.println(rs.getInt("m"));
            }

            // Querying with parameters
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM memes WHERE id IN (?,?,?) ORDER BY id DESC");
            ps.setInt(1, 25);
            ps.setInt(2, 34);
            ps.setInt(3, 47);
            rs = ps.executeQuery();
            while(rs != null && rs.next()) {
                System.out.println(rs.getInt("id") + ", " + rs.getString("link") + ", " + rs.getString("submitter") + ", " + rs.getString("curator"));
            }

            // Disable auto commit
            conn.setAutoCommit(false);
            ps = conn.prepareStatement("DELETE FROM memes WHERE id IN (?,?,?)");
            ps.setInt(1, 25);
            ps.setInt(2, 34);
            ps.setInt(3, 47);
            ps.executeUpdate();

            ps = conn.prepareStatement("DELETE FROM memes WHERE id IN (?,?,?)");
            ps.setInt(1, 20);
            ps.setInt(2, 1);
            ps.setInt(3, 47);
            ps.executeUpdate();

            //conn.commit();
            conn.rollback();

            //conn.setAutoCommit(true);

            // Querying with parameters
            ps = conn.prepareStatement("SELECT * FROM memes WHERE id IN (?,?,?)");
            ps.setInt(1, 25);
            ps.setInt(2, 34);
            ps.setInt(3, 47);
            rs = ps.executeQuery();
            while(rs != null && rs.next()) {
                System.out.println(rs.getInt("id") + ", " + rs.getString("link") + ", " + rs.getString("submitter") + ", " + rs.getString("curator"));
            }

            // Updating a table definition once live
            conn.createStatement().execute("ALTER TABLE memes ADD COLUMN timestamp text");
            conn.commit();

            // Dropping tables
            conn.createStatement().execute("DROP TABLE memes");
            conn.createStatement().execute("DROP TABLE tag_lkp");
            conn.commit();

            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
