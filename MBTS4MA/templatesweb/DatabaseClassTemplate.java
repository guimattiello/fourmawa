
import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;

public class Database {

    //private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    private static final String DRIVER_NAME = "org.postgresql.Driver";

    static {
        try {
            Class.forName(DRIVER_NAME).newInstance();
            System.out.println("*** Driver loaded");
        } catch (Exception e) {
            System.out.println("*** Error : " + e.toString());
            System.out.println("*** ");
            System.out.println("*** Error : ");
            e.printStackTrace();
        }
    }

    private static final String URL = "jdbc:postgresql://{{dbhost}}/{{dbname}}";
    private static final String USER = "{{dbuser}}";
    private static final String PASSWORD = "{{dbpassword}}";
    private static String INSTRUCTIONS = new String();
    private static final String SCRIPT_FILE_PATH = "database/script.sql";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void resetDatabase() throws SQLException, UnsupportedEncodingException {
        String s = new String();
        StringBuffer sb = new StringBuffer();

        try {
            File fileDir = new File("build/test/classes/" + SCRIPT_FILE_PATH);

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF8"));

            String str;

            while ((s = br.readLine()) != null) {
                sb.append(s);
            }

            br.close();

            // here is our splitter ! We use ";" as a delimiter for each request
            // then we are sure to have well formed statements
            String[] inst = sb.toString().split(";");

            Connection c = Database.getConnection();
            Statement st = c.createStatement();

            for (int i = 0; i < inst.length; i++) {
                // we ensure that there is no spaces before or after the request string
                // in order to not execute empty statements
                if (!inst[i].trim().equals("")) {                    
                    
                    String query = "";
                    
                    //Verify if file begins with a BOM
                    if (inst[i].charAt(0) == 0xFEFF){
                        query = (inst[i].substring(1, inst[i].length()));
                    } else {
                        query = (inst[i]);
                    }
                    
                    st.executeUpdate(query);

                    System.out.println(">>" + query);
                }
            }

        } catch (Exception e) {
            System.out.println("*** Error : " + e.toString());
            System.out.println("*** ");
            System.out.println("*** Error : ");
            e.printStackTrace();
            System.out.println("################################################");
            System.out.println(sb.toString());
        }

    }

}
