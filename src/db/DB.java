package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {

    // Objeto do java que recebe uma conexão com o sql.
    private static Connection conn = null;

    // Iniciando conexão com banco de dados.
    public static Connection getConnection(){
        if(conn == null){
            try {
                Properties props = loadProperties();
                String url = props.getProperty("dburl");
                conn = DriverManager.getConnection(url, props);
            }
            catch (SQLException erro){
                throw new DBException(erro.getMessage());
            }
        }
        return conn;
    }

    // Fechando a conexão com o banco de dados.
    public static void closeConnection(){
        if (conn != null){
            try {
                conn.close();
            }
            catch (SQLException erro){
                throw new DBException(erro.getMessage());
            }
        }
    }

    // Lendo o arquivo db.properties
    private static Properties loadProperties(){
        // Usamos o FileInputStream: pois estamos processando e lendo ao mesmo tempo --> "Usamos em arquivos grandes 500 Megas ou mais",
        // FileInput'Stream' -> Fluxos de dados contínuos.
        try (FileInputStream fs = new FileInputStream("db.properties")){
            Properties props = new Properties();
            props.load(fs);
            return props;
        }
        catch (IOException erro){
            throw new DBException(erro.getMessage());
        }
    }

    // Fechando e tratando a conexão Statement st.
    public static void closeStatement(Statement st){
        if (st != null){
            try {
                st.close();
            } catch (SQLException error) {
                throw new DBException(error.getMessage());
            }
        }
    }

    // Fechando e tratando a conexão Resultset rs.
    public static void closeResultset(ResultSet rs){
        if (rs != null){
            try {
                rs.close();
            } catch (SQLException error) {
                throw new DBException(error.getMessage());
            }
        }
    }
}
