package dashboard2gis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Dashboard2Gis {

    public static void main(String[] args) {

        // Declare the JDBC objects.  
        Connection gisConn = null, dashboardConn = null;
        Statement gisSelectStmt = null, gisTruncateStatement = null, dashboardSelectStmt = null;
        PreparedStatement gisInsertStmt = null;
        ResultSet rs = null;
        
        //GPS coordinates
        Double lat;
        Double lon;
        
        //Territory codes
        String regionCode;
        String zoneCode;
        String woredaCode;
        Double expected;

        try {
            // Establish the connection to the GIS database.
            System.out.println("Connecting to GIS database...");

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            gisConn = DriverManager.getConnection(Query.GIS_CONNECTION_URL);
            //Class.forName("com.mysql.jdbc.Driver");
            //gisConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dashboard_reports?autoReconnect=true&useSSL=false", "root", "root");
            gisConn.setAutoCommit(false);

            System.out.println("Connection to GIS database successful!");

            // Establish the connection to the DASHBOARD database.
            System.out.println("Connecting to DASHBOARD database...");

            Class.forName("com.mysql.jdbc.Driver");
            dashboardConn = DriverManager.getConnection(Query.DASHBOARD_CONNECTION_URL, Query.DASHBOARD_USER_NAME, Query.DASHBOARD_PASSWORD);
            dashboardConn.setReadOnly(true);

            System.out.println("Connection to DASHBOARD database successful!");

            //Remove data from GIS table containing GPS coordinates
//            System.out.println("Deleting data from dbo.HH_Census table");
//            gisTruncateStatement = gisConn.createStatement();
//            gisTruncateStatement.executeUpdate(Query.TRUNCATE_GPS_COORDINATES);
//            gisTruncateStatement.getConnection().commit();
//            gisTruncateStatement.close();

            // Create and execute an SQL statement that returns some data.  
            //String SQL = "SELECT count(*) FROM dbo.HH_Census";
            //gisSelectStmt = gisConn.createStatement();
            //rs = gisSelectStmt.executeQuery(SQL);
            //rs.next();
            //System.out.println("Operation completed! Table contains " + rs.getInt(1) + " records");
            
            //Remove data from GIS table containing REGION progress data
            System.out.println("Deleting data from dbo.PROGRESS_R table");
            gisTruncateStatement = gisConn.createStatement();
            gisTruncateStatement.executeUpdate(Query.TRUNCATE_PROGRESS_BY_REGION);
            gisTruncateStatement.getConnection().commit();
            gisTruncateStatement.close();
            
            //Remove data from GIS table containing ZONE progress data
            System.out.println("Deleting data from dbo.PROGRESS_Z table");
            gisTruncateStatement = gisConn.createStatement();
            gisTruncateStatement.executeUpdate(Query.TRUNCATE_PROGRESS_BY_ZONE);
            gisTruncateStatement.getConnection().commit();
            gisTruncateStatement.close();
            
            //Remove data from GIS table containing WOREDA progress data
            System.out.println("Deleting data from dbo.PROGRESS_W table");
            gisTruncateStatement = gisConn.createStatement();
            gisTruncateStatement.executeUpdate(Query.TRUNCATE_PROGRESS_BY_WOREDA);
            gisTruncateStatement.getConnection().commit();
            gisTruncateStatement.close();
            // Create and execute an SQL statement that returns some data.  
            //String SQL2 = "SELECT count(*) FROM dbo.PROGRESS_R";
            //gisSelectStmt = gisConn.createStatement();
            //rs = gisSelectStmt.executeQuery(SQL2);
            //rs.next();
            //System.out.println("Operation completed! Table contains " + rs.getInt(1) + " records");
            
            Integer counter = 0;
//            Double lat;
//            Double lon;
//
//            //Retrieve gps coordinates from H_AXULLARY
//            dashboardSelectStmt = dashboardConn.createStatement();
//            rs = dashboardSelectStmt.executeQuery(Query.SELECT_GPS_COORDINATES);
//
//            //Prepare the statement to insert gps coordinates in GIS database
//            gisInsertStmt = gisConn.prepareStatement(Query.INSERT_GPS_COORDINATES);
//
//            // Iterate through the data retrieved from the DASHBOARD database  
//            while (rs.next()) {
//                counter++;
//                lat = rs.getDouble(1);
//                lon = rs.getDouble(2);
//
//                //System.out.println("LAT: " + lat + " LON: " + lon);
//                gisInsertStmt.setDouble(1, lat);
//                gisInsertStmt.setDouble(2, lon);
//                gisInsertStmt.executeUpdate();
//
//                if (counter % Query.COMMIT_SIZE == 0) {
//                    System.out.println("Inserted " + counter + " rows in  dbo.HH_Census");
//                    gisInsertStmt.getConnection().commit();
//                }
//            }
//            System.out.println("Inserted " + counter + " rows in  dbo.HH_Census");
//            gisInsertStmt.getConnection().commit();
//
//            //Cleanup
//            rs.close();
//            dashboardSelectStmt.close();
//            gisInsertStmt.close();
//            counter = 0;

            
            //Retrieve field work progress data from DASHBOARD reports at Region level
            dashboardSelectStmt = dashboardConn.createStatement();
            rs = dashboardSelectStmt.executeQuery(Query.SELECT_PROGRESS_BY_REGION);

            //Prepare the statement to insert gps coordinates in GIS database
            gisInsertStmt = gisConn.prepareStatement(Query.INSERT_PROGRESS_BY_REGION);

            System.out.println("Inserting data into dbo.PROGRESS_R table...");
            
            while (rs.next()) { // Iterate through the data in the result set
                counter++;
                regionCode = rs.getString(1);
                expected = rs.getDouble(2);

                if (regionCode == null) {
                    continue;
                }

                System.out.println("dbo.PROGRESS_R -> REGION CODE: " + padLeft(regionCode, 2) + " EXPECTED: " + (int) Math.round(expected));

                gisInsertStmt.setString(1, padLeft(regionCode, 2)); //Add 0 to regionCode if region code < 10
                gisInsertStmt.setInt(2, (int) Math.round(expected));
                gisInsertStmt.executeUpdate();

                if (counter % Query.COMMIT_SIZE == 0) {
                    System.out.println("Inserted " + counter + " rows in  PROGRESS_R");
                    gisInsertStmt.getConnection().commit();
                }
            }
            System.out.println("Inserted " + counter + " rows in  dbo.PROGRESS_R");
            gisInsertStmt.getConnection().commit();
            
            //Cleanup
            rs.close();
            dashboardSelectStmt.close();
            gisInsertStmt.close();
            counter = 0;

            //Retrieve field work progress data from DASHBOARD reports at Woreda level
            dashboardSelectStmt = dashboardConn.createStatement();
            rs = dashboardSelectStmt.executeQuery(Query.SELECT_PROGRESS_BY_ZONE);

            //Prepare the statement to insert gps coordinates in GIS database
            gisInsertStmt = gisConn.prepareStatement(Query.INSERT_PROGRESS_BY_ZONE);

            System.out.println("Inserting data into dbo.PROGRESS_Z table...");

            while (rs.next()) { // Iterate through the data in the result set
                counter++;
                regionCode = rs.getString(1);
                zoneCode = rs.getString(2);
                expected = rs.getDouble(3);

                if (regionCode == null) {
                    continue;
                }

                System.out.println("dbo.PROGRESS_Z -> ZONE CODE: " + padLeft(regionCode, 2) + padLeft(zoneCode, 2) + 
                        " EXPECTED: " + (int) Math.round(expected));

                gisInsertStmt.setString(1, padLeft(regionCode, 2) + padLeft(zoneCode, 2)); //Add 0 to regionCode if region code < 10
                gisInsertStmt.setInt(2, (int) Math.round(expected));
                gisInsertStmt.executeUpdate();

                if (counter % Query.COMMIT_SIZE == 0) {
                    System.out.println("Inserted " + counter + " rows in dbo.PROGRESS_Z");
                    gisInsertStmt.getConnection().commit();
                }
            }
            System.out.println("Inserted " + counter + " rows in dbo.PROGRESS_Z");
            gisInsertStmt.getConnection().commit();
            
            //Cleanup
            rs.close();
            dashboardSelectStmt.close();
            gisInsertStmt.close();
            counter = 0;
            
            //Retrieve field work progress data from DASHBOARD reports at Zone level
            dashboardSelectStmt = dashboardConn.createStatement();
            rs = dashboardSelectStmt.executeQuery(Query.SELECT_PROGRESS_BY_WOREDA);

            //Prepare the statement to insert gps coordinates in GIS database
            gisInsertStmt = gisConn.prepareStatement(Query.INSERT_PROGRESS_BY_WOREDA);

            System.out.println("Inserting data into dbo.PROGRESS_W table...");

            while (rs.next()) { // Iterate through the data in the result set
                counter++;
                regionCode = rs.getString(1);
                zoneCode = rs.getString(2);
                woredaCode = rs.getString(3);
                expected = rs.getDouble(4);

                if (regionCode == null) {
                    continue;
                }

                System.out.println("dbo.PROGRESS_W -> WOREDA CODE: " + padLeft(regionCode, 2) + padLeft(zoneCode, 2) + padLeft(woredaCode, 2) + 
                        " EXPECTED: " + (int) Math.round(expected));

                gisInsertStmt.setString(1, padLeft(regionCode, 2) + padLeft(zoneCode, 2) + padLeft(woredaCode, 2)); //Add 0 to regionCode if region code < 10
                gisInsertStmt.setInt(2, (int) Math.round(expected));
                gisInsertStmt.executeUpdate();

                if (counter % Query.COMMIT_SIZE == 0) {
                    System.out.println("Inserted " + counter + " rows in dbo.PROGRESS_W");
                    gisInsertStmt.getConnection().commit();
                }
            }
            System.out.println("Inserted " + counter + " rows in dbo.PROGRESS_W");
            gisInsertStmt.getConnection().commit();
            
            //Cleanup
            rs.close();
            dashboardSelectStmt.close();
            gisInsertStmt.close();
            counter = 0;
            
            // Create and execute an SQL statement that returns some data.  
            //String SQL = "SELECT * FROM dbo.HH_Census";
            //gisSelectStmt = gisConn.createStatement();
            //rs = gisSelectStmt.executeQuery(SQL);
        } // Handle any errors that may have occurred.
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                }
            }
            if (gisSelectStmt != null) {
                try {
                    gisSelectStmt.close();
                } catch (Exception e) {
                }
            }
            if (gisInsertStmt != null) {
                try {
                    gisInsertStmt.close();
                } catch (Exception e) {
                }
            }
            if (gisTruncateStatement != null) {
                try {
                    gisTruncateStatement.close();
                } catch (Exception e) {
                }
            }
            if (dashboardSelectStmt != null) {
                try {
                    dashboardSelectStmt.close();
                } catch (Exception e) {
                }
            }
            if (gisConn != null) {
                try {
                    gisConn.close();
                } catch (Exception e) {
                }
            }
            if (dashboardConn != null) {
                try {
                    dashboardConn.close();
                } catch (Exception e) {
                }
            }
        }
    }

    private static String padLeft(String aString, Integer len) {

        String pad = aString;

        if (pad.length() < len) {
            for (int i = 0; i < (len - pad.length()); i++) {
                pad = "0" + pad;
            }
        }

        return pad;
    }

}
