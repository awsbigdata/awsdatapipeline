package com.amazonaws.dp.postgresCopy;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.beust.jcommander.JCommander;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by srramas on 9/13/17.
 *
 * -input=/home/local/ANT/srramas/test/test.csv -username=postuser -password=postpassword -jdbcurl=jdbc:postgresql://postdb.cluster-c9ujozbff8fu.us-east-1.rds.amazonaws.com:5432/postdb -copycommand=COPY emp1 FROM STDIN WITH DELIMITER ','
 */

public class CopyActivity {

    private static Logger logger = Logger.getLogger("CopyActivity");
    static String spattern="s3://([^/]*)/(.*)";


    public static void main(String[] args) {

        Pattern pattern = Pattern.compile(spattern);

        CopyArguments ca=new CopyArguments();
        JCommander jCommander = new JCommander(ca);
        jCommander.parse(args);
        jCommander.setProgramName("Postgress Copy Activity");
        if (ca.isHelp()) {
            jCommander.usage();
            System.exit(-1);
        }
        logger.info("Connection Creating ...");
        Matcher matcher = pattern.matcher(ca.getInputfile());
        matcher.find();
        Connection dbConnection = getDBConnection(ca.getJdbcurl(),ca.getUsername(),ca.getPassword());

        logger.info("Connection created");

        try {

            logger.info("Copy command is submitting");

            CopyManager copyManager = new CopyManager((BaseConnection) dbConnection);
            copyManager.copyIn(ca.getCopy_command(), fileDownload(matcher.group(1),matcher.group(2)) );

            logger.info("Copy command completed");


        } catch (FileNotFoundException e) {
            logger.info("file not found "+e.getMessage());
            throw new RuntimeException();

        } catch (SQLException e) {
            logger.info("SQL error "+e.getMessage());
             e.printStackTrace();
            throw new RuntimeException();
        } catch (IOException e) {
            logger.info("IO error :"+e.getMessage());
            throw new RuntimeException();
        }


    }

    /**
     *
     * @param bucket
     * @param prefix
     * @return
     */

    private static Reader fileDownload(String bucket, String prefix){

        AmazonS3 s3client = new AmazonS3Client(new DefaultAWSCredentialsProviderChain());
        try {
            System.out.println("Downloading an object");
            S3Object s3object = s3client.getObject(new GetObjectRequest(
                    bucket, prefix));
            System.out.println("Content-Type: "  +
                    s3object.getObjectMetadata().getContentType());
            BufferedReader reader= new BufferedReader(new
                    InputStreamReader(s3object.getObjectContent()));
           // displayTextInputStream(s3object.getObjectContent());
            return reader;

        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, " +
                    "which means your request made it " +
                    "to Amazon S3, but was rejected with an error " +
                    "response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
            throw new RuntimeException();
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, " +
                    "which means the client encountered " +
                    "an internal error while trying to " +
                    " communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
            throw new RuntimeException();
        }
    }



    /**
     *
     * @param jdbcurl
     * @param username
     * @param password
     * @return
     */
    private static Connection getDBConnection(String jdbcurl,String username,String password) {

        Connection dbConnection = null;

        try {
            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {

            System.out.println(e.getMessage());

        }
        try {

            dbConnection = DriverManager.getConnection(jdbcurl,username,password);
            return dbConnection;

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }
        return dbConnection;

    }

}
