/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dashboard2gis;

/**
 *
 * @author UTENTE
 */
public class Query {

    public static final Integer COMMIT_SIZE = 1000;
    
    //CONNECTION PARAMETERS
    public static final String GIS_CONNECTION_URL = "jdbc:sqlserver://192.168.0.118:1433;databaseName=CensusDashboard;user=Editor;password=Editors123";
    //public static final String DASHBOARD_CONNECTION_URL = "jdbc:mysql://192.168.0.88:3306/dashboard?autoReconnect=true&useSSL=false";
    //public static final String DASHBOARD_CONNECTION_URL = "jdbc:mysql://192.168.0.88:3306/pilot4?autoReconnect=true&useSSL=false";
    public static final String DASHBOARD_CONNECTION_URL = "jdbc:mysql://192.168.100.16:3306/CENSUS-DB?autoReconnect=true&useSSL=false";
    //public static final String DASHBOARD_USER_NAME = "csprodashboard";
    //public static final String DASHBOARD_PASSWORD = "cspr0d4shb04rd";
    public static final String DASHBOARD_USER_NAME = "root";
    public static final String DASHBOARD_PASSWORD = "CSA@1234";
    
    
    
    //DASHBOARD QUERY
    public static final String SELECT_GPS_COORDINATES = "SELECT A1, A2 FROM H_Axullary";
    public static final String SELECT_PROGRESS_BY_REGION = "SELECT ID101, FIELD_EXPECTED FROM MR_HOUSEHOLD_EXPECTED_BY_REGION";
    public static final String SELECT_PROGRESS_BY_ZONE = "SELECT ID101, ID102, FIELD_EXPECTED FROM MR_HOUSEHOLD_EXPECTED_BY_ZONE";
    public static final String SELECT_PROGRESS_BY_WOREDA = "SELECT ID101, ID102, ID103, FIELD_EXPECTED FROM MR_HOUSEHOLD_EXPECTED_BY_WOREDA";
    
    //GIS QUERY
    public static final String TRUNCATE_GPS_COORDINATES = "TRUNCATE TABLE editor.dashboard_HH_XY";
    public static final String TRUNCATE_PROGRESS_BY_REGION = "TRUNCATE TABLE editor.dashboard_PROGRESS_R";
    public static final String TRUNCATE_PROGRESS_BY_ZONE = "TRUNCATE TABLE editor.dashboard_PROGRESS_Z";
    public static final String TRUNCATE_PROGRESS_BY_WOREDA = "TRUNCATE TABLE editor.dashboard_PROGRESS_W";
    public static final String INSERT_GPS_COORDINATES = "INSERT INTO editor.dashboard_HH_XY VALUES(?,?)";
    public static final String INSERT_PROGRESS_BY_REGION = "INSERT INTO editor.dashboard_PROGRESS_R VALUES(?,?)";
    public static final String INSERT_PROGRESS_BY_ZONE = "INSERT INTO editor.dashboard_PROGRESS_Z VALUES(?,?)";
    public static final String INSERT_PROGRESS_BY_WOREDA = "INSERT INTO editor.dashboard_PROGRESS_W VALUES(?,?)";

}
