package com.amazonaws.dp.sdk;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.datapipeline.DataPipeline;
import com.amazonaws.services.datapipeline.DataPipelineClientBuilder;
import com.amazonaws.services.datapipeline.model.ActivatePipelineRequest;
import com.amazonaws.services.datapipeline.model.ActivatePipelineResult;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by srramas on 9/14/17.
 */
public class DPActivate {

    public static void main(String[] args) {
        //Get the file reference
        Path path = Paths.get("/mnt/spark/output.txt");

//Use try-with-resource to get auto-closeable writer instance
        try (BufferedWriter writer = Files.newBufferedWriter(path))
        {
            for(int i=0;i<200000;i++)
            writer.write("Hello World !!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
